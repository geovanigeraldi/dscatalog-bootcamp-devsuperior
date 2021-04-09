import { ReactComponent as Computador } from "../../../../core/assets/images/Computador.svg";
import ProductPrice from "../../../../core/components/ProductPrice";
import './styles.scss'

const ProductCard = () => (
  <div className="card-base border-radius-10 product-card">
    <Computador />
    <div className="product-info">
      <h6 className="product-name">
        Computador Desktop - Intel Core i7
      </h6>
      <ProductPrice price="2.779,00" />
    </div>
  </div>
);

export default ProductCard;