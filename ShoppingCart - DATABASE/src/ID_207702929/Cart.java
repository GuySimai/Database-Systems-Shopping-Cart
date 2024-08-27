package ID_207702929;

import java.io.Serializable;
import java.util.Arrays;

public class Cart implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2716497024798920835L;
	/**
	 * 
	 */
	
	private Product[]coustomerProducts;
	
	public Cart(Product[]coustomerProducts) {
		this.coustomerProducts=coustomerProducts;
	}

	public Product[] getCoustomerProducts() {
		return coustomerProducts;
	}

	public void setCoustomerProducts(Product[] coustomerProducts) {
		this.coustomerProducts = coustomerProducts;
	}

	@Override
	public String toString() {
		return "Cart [coustomerProducts=" + Arrays.toString(coustomerProducts) + "]";
	}
	
	

}
