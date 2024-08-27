package ID_207702929;

import java.io.Serializable;

public class Books extends Product implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7330647860331954095L;

	public Books(int id, theCategory category, String productName, int quantity, String param1, String param2) {
		super(id,category,productName,quantity,param1,param2);
	}
	

	public String toString() {
		return super.toString();
	}
}
