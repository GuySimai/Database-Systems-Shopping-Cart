package ID_207702929;

import java.io.Serializable;
import java.util.Arrays;

public class Stock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5187204839462248370L;
	/**
	 * 
	 */

	private Product[] stockProducts;
	
	public Stock( Product[] stockProducts) {
		this.stockProducts=stockProducts;
	}

	public Product[] getStockProducts() {
		return stockProducts;
	}

	public void setStockProducts(Product[] stockProducts) {
		this.stockProducts = stockProducts;
	}

	@Override
	public String toString() {
		return "Stock [stockProducts=" + Arrays.toString(stockProducts) + "]";
	}
	

}
