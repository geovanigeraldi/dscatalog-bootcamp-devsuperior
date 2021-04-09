import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { makeRequest } from 'core/utils/request';
import ProductCard from './components/ProductCard';
import { ProductResponse } from 'core/types/Product';
import ProductCardLoader from './components/Loaders/ProductCardLoader';

import './styles.scss'

const Catalog = () => {
  const [productResponse, setProductResponse] = useState<ProductResponse>();
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    const params = {
      page:0,
      linesPerPage: 10
    }
    setIsLoading(true);
    makeRequest({ url:'/products', params })    
    .then(response => setProductResponse(response.data))
    .finally(() => { 
        setIsLoading(false);
    });
  }, []);


  return (
    <div className="catalog-container">
      <h1 className="catalog-title">
        Catálogo de produtos
      </h1>
      <div className="catalog-products">
        {isLoading 
          ? <ProductCardLoader />
          : (productResponse?.content.map(product => (
              <Link key={product.id}
                to={`/products/${product.id}`}> 
                <ProductCard 
                  product={product}
                /> 
              </Link>))
            )
        }
      </div>
    </div>
  );
}

export default Catalog;