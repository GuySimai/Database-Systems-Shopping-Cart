package ID_207702929;


import java.io.Serializable;
import java.time.LocalDateTime;




public abstract class Product implements Serializable   {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2863268769871845746L;

	public enum theCategory {
		Books, Clothing, Electronics
	};

	private LocalDateTime timeStamp;
	private int id;
	private String productName;
	private int quantity;
	private String param1;
	private String param2;
	final private theCategory category;

	public Product(int id, theCategory category, String productName, int quantity, String param1, String param2) {
		this.id = id;
		this.category = category;
		this.productName = productName;
		this.quantity = quantity;
		this.param1 = param1;
		this.param2 = param2;

	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public String getParam1() {
		return param1;
	}

	public String getParam2() {
		return param2;
	}

	public theCategory getCategory() {
		return category;
	}

	@Override
	public String toString() {

		return "Product --> category=" + category + ", id=" + id + ", productName= " + productName + ", quantity="
				+ quantity + ", param1=" + param1 + ", param2=" + param2;
	}

}
