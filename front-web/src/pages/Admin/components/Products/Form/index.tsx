import { makeRequest } from 'core/utils/request';
import { useState } from 'react';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormData = {
  name: string;
  price:string;
  category: string;
  description:string;
}

type FormEvent = React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>;

const Form = () => {
  const [formData, setFormData] = useState<FormData>({
    name: '',
    price:'',
    category: '',
    description: ''
  });
  
  const handleOnChange = (event: FormEvent) => {
    const name = event.target.name;
    const value = event.target.value;
    setFormData(data => ({...data, [name]: value}));
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const payload = {
      ...formData,
      imgUrl: 'https://www.casanissei.com/media/catalog/product/cache/16a9529cefd63504739dab4fc3414065/p/l/playstation_5_perfil_-_1.jpg',
      categories: [{id:formData.category}]
    }
    //console.log(payload);
    makeRequest({url: '/products', method: 'POST', data: payload})
    .then(() => {
      setFormData({
        name: '',
        price:'',
        category: '',
        description: ''});
      });
  }

  return (
    <form onSubmit={handleSubmit}>
      <BaseForm title="CADASTRAR UM produto">
        <div className="row">
          <div className="col-6">
            <input
              value={formData.name}
              name="name"
              type="text"
              className="form-control mb-5"
              onChange={handleOnChange}
              placeholder="Nome do produto"
            />
            <select 
              value={formData.category}
              name="category"
              className="form-control mb-5" 
              onChange={handleOnChange}
            >
              <option value="1">Livros</option>
              <option value="3">Computadores</option>
              <option value="2">Eletronicos</option>
            </select>
            <input
              value={formData.price}
              name="price"
              type="text"
              className="form-control"
              onChange={handleOnChange}
              placeholder="Preço"
            />
          </div>
          <div className="col-6">
            <textarea 
              value={formData.description}
              name="description" 
              className="form-control"
              cols={30} 
              rows={10}
              onChange={handleOnChange}
            />
          </div>
        </div>
      </BaseForm>
    </form>
  );
}

export default Form;