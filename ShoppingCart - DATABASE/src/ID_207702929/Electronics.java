package ID_207702929;

import java.io.Serializable;

public class Electronics extends Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8802017650481771403L;

	public Electronics(int id, theCategory category, String productName, int quantity, String param1, String param2) {
		super(id, category, productName, quantity,  param1, param2);
	}

	

	@Override
	public String toString() {
		return super.toString();
	}

}
