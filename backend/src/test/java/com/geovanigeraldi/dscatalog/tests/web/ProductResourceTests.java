package com.geovanigeraldi.dscatalog.tests.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geovanigeraldi.dscatalog.dto.ProductDTO;
import com.geovanigeraldi.dscatalog.services.ProductService;
import com.geovanigeraldi.dscatalog.services.exceptions.DataBaseException;
import com.geovanigeraldi.dscatalog.services.exceptions.ResourceNotFoundException;
import com.geovanigeraldi.dscatalog.tests.factories.ProductFactory;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objMapper;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	private Long existingId;	
	private Long nonExistingId;
	private Long dependentId;
	private ProductDTO newProductDto;
	private ProductDTO existingProductDto;
	private PageImpl<ProductDTO> page;
	
	private String operatorUserName;
	private String operatorPassword;
	
	@BeforeEach
	void setUp() throws Exception {
		operatorUserName = "alex@gmail.com";
		operatorPassword = "123456";
		
		existingId = 1L;
		nonExistingId = 2L;
	
		newProductDto = ProductFactory.createProductDTO(null);
		existingProductDto = ProductFactory.createProductDTO(existingId);
		
		page = new PageImpl<>(List.of(existingProductDto));
		
		when(service.findById(existingId)).thenReturn(existingProductDto);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		when(service.findAllPaged(any(), anyString(), any())).thenReturn(page);
		
		when(service.insert(any())).thenReturn(existingProductDto);
		
		when(service.update(eq(existingId), any())).thenReturn(existingProductDto);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DataBaseException.class).when(service).delete(dependentId);
	}
	
	@Test
	public void updateShouldReturnProductWhenIdExists() throws Exception {
		String accessToken = obtainAccessToken(operatorUserName, operatorPassword);		
		String jsonBody = objMapper.writeValueAsString(newProductDto);
		String expectedName = newProductDto.getName();
		Double expectedPrice = newProductDto.getPrice();
		
		mockMvc.perform(
				put("/products/{id}", existingId)
				.header("Authorization", "Bearer "+accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.id").value(existingId))
		.andExpect(jsonPath("$.name").value(expectedName))
		.andExpect(jsonPath("$.price").value(expectedPrice))
		;			
	}
	
	@Test
	public void updateShouldReturnNotFoundDoesNotExist() throws Exception {
		String accessToken = obtainAccessToken(operatorUserName, operatorPassword);		
		String jsonBody = objMapper.writeValueAsString(newProductDto);
		mockMvc.perform(
				put("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer "+accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isNotFound())
		;			
	}
	
	@Test
	public void findByAllShouldReturnPage() throws Exception {
		mockMvc.perform(
				get("/products")
				.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").exists())
		;
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		mockMvc.perform(
				get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.id").value(existingId))
		;
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		mockMvc.perform(
				get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isNotFound());
	}
	
	private String obtainAccessToken(String username, String password) throws Exception {
		 
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("client_id", clientId);
	    params.add("username", username);
	    params.add("password", password);
	 
	    ResultActions result 
	    	= mockMvc.perform(post("/oauth/token")
	    		.params(params)
	    		.with(httpBasic(clientId, clientSecret))
	    		.accept("application/json;charset=UTF-8"))
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType("application/json;charset=UTF-8"));
	 
	    String resultString = result.andReturn().getResponse().getContentAsString();
	 
	    JacksonJsonParser jsonParser = new JacksonJsonParser();
	    return jsonParser.parseMap(resultString).get("access_token").toString();
	}	
}