package ID_207702929;

import java.io.Serializable;

public class Clothing extends Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8535605770625063844L;

	public enum sex{M,F,empty};
	
	final private sex sex;

	public Clothing(int id, theCategory category, String productName, int quantity, String param1, String param2,sex sex) {
		super(id, category, productName, quantity, param1, param2);
		this.sex=sex;
	}


	@Override
	public String toString() {
		return super.toString()+ " ,sex=" + sex  ;
	}
	
	
	
	

}
