package ID_207702929;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class SystemOnline implements Reservable {
	private Stock shopStock;
	private Cart consumerCart;
	protected final int MAX_AMOUNT_PER_ONE = 10;

	public SystemOnline(Stock shopStock, Cart consumerCart) {
		this.shopStock = shopStock;
		this.consumerCart = consumerCart;
	}

	public Stock getShopStock() {
		return shopStock;
	}

	public void setShopStock(Stock shopStock) {
		this.shopStock = shopStock;
	}

	public Cart getConsumerCart() {
		return consumerCart;
	}

	public void setConsumerCart(Cart consumerCart) {
		this.consumerCart = consumerCart;
	}

	// other methods
	public Product locateProductById(int id) {
		for (int i = 0; i < this.shopStock.getStockProducts().length; i++) {
			if (this.shopStock.getStockProducts()[i].getId() == id) {
				return this.getShopStock().getStockProducts()[i];
			}
		}
		return null;
	}

	public void presentStock() {

		for (int i = 0; i < this.shopStock.getStockProducts().length; i++) {
			if (this.shopStock.getStockProducts()[i].getQuantity() > 0) {
				System.out.println((this.shopStock.getStockProducts())[i]);
			}
			}
	}

	public void SortByCatagory(Product[] products) {
		Arrays.sort(products, new Comparator<Product>() {
			@Override
			public int compare(Product product1, Product product2) {
				return product1.getCategory().compareTo(product2.getCategory());
			}
		});
	}

	public void SortByname(Product[] products) {

		Arrays.sort(products, new Comparator<Product>() {
			@Override
			public int compare(Product product1, Product product2) {
				return product1.getProductName().compareTo(product2.getProductName());
			}
		});

	}

	public void SortByQuantity(Product[] products) {
		Arrays.sort(products, new Comparator<Product>() {
			@Override
			public int compare(Product product1, Product product2) {
				return product1.getQuantity() - product2.getQuantity();
			}
		});
	}

	public void sortStockarr() {
		Product[] products = this.shopStock.getStockProducts();
		for (int i = 0; i < products.length - 1; i++) {
			for (int j = 0; j < products.length - i - 1; j++) {
				if (products[j].getCategory().ordinal() > products[j + 1].getCategory().ordinal()) {
					// swap the elemnt
					Product temp = products[j];
					products[j] = products[j + 1];
					products[j + 1] = temp;
				}
			}
		}
		for (int i = 0; i < products.length; i++) {
			products[i].setId(i);
		}
	}

	public void sortCartarr() {
		Product[] products = this.consumerCart.getCoustomerProducts();
		for (int i = 0; i < products.length - 1; i++) {
			for (int j = 0; j < products.length - i - 1; j++) {
				if (products[j].getCategory().ordinal() > products[j + 1].getCategory().ordinal()) {
					// swap the elemnt
					Product temp = products[j];
					products[j] = products[j + 1];
					products[j + 1] = temp;
				}
			}
		}
		for (int i = 0; i < products.length; i++) {
			products[i].setId(i);
		}
	}

	public Product[] presentStockWithoutProductInCart() {
		Product[] remain = this.shopStock.getStockProducts();
		for (int i = 0; i < this.shopStock.getStockProducts().length; i++) {
			if (this.shopStock.getStockProducts()[i].getQuantity() > 0) {
				System.out.println(this.shopStock.getStockProducts()[i]);
				remain[i] = this.shopStock.getStockProducts()[i];

			}
		}
		return remain;
	}

	public boolean presentCosumerCart() throws IOException {
		int counter = 0;
		for (int i = 0; i < this.consumerCart.getCoustomerProducts().length; i++) {
			if (consumerCart.getCoustomerProducts()[i].getQuantity() > 0) {
				System.out.println(this.consumerCart.getCoustomerProducts()[i]);

				LocalDateTime timestamp = this.consumerCart.getCoustomerProducts()[i].getTimeStamp();
				if (timestamp != null) {
					System.out.println(
							"Timestamp: " + timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
				}

				counter++;
			}
		}
		if (counter == 0) {
			System.out.println("your cart is empty,please enter products");
			return false;
		}
		return true;
	}

	public boolean removeProductById(int id, ConnectDBL DBL) throws CartProductNotExistException, SQLException {
		Product[] currntCostumerCart = this.consumerCart.getCoustomerProducts();
		// cart is empty
		boolean checkingIfCartEmpty = checkingEmptyCart();
		if (checkingIfCartEmpty) {
			System.out.println("cart is empty ");
			return false;
			// break;
		}

		// Invalid id ,scan ID doesnt exist in cart

		if (id > currntCostumerCart.length) {
			System.out.println("doesnt exsist this id:");
			return false;
			// break;
		}
		boolean atCart = consumerCart.getCoustomerProducts()[id].getQuantity() > 0;
		if (!atCart) {
			throw new CartProductNotExistException();

		}
		if (id != consumerCart.getCoustomerProducts()[id].getId()) {
			System.out.println("Invalid Id,product doesent exsist\n");
			return false;
			// break;
		} else {
			// get the quantity i have in cart
			int currentQuantity = currntCostumerCart[id].getQuantity();
			currntCostumerCart[id].setQuantity(0); // set to 0
			consumerCart.setCoustomerProducts(currntCostumerCart);

			// return to stock
			Product[] newAmountInStock = this.shopStock.getStockProducts();
			int sotckNumOfProducts = newAmountInStock[id].getQuantity();
			newAmountInStock[id].setQuantity(sotckNumOfProducts + currentQuantity);
			shopStock.setStockProducts(newAmountInStock);
			
			DBL.addOrRemoveProductCart(-currentQuantity, id);
			
			return true;

		}

	}

//############################################################################################

	public void updateProductById(String action, int id, int quantity, ConnectDBL DBL)
			throws OnlineStoreGeneralException, ProductQuantityNotAvailableException, CartProductNotExistException,
			CartProductAlreadyExistException, IOException, ReachedMaxAmountException, SQLException {

		switch (action) {
		case "add": {
			Product[] currntCostumerCart = this.consumerCart.getCoustomerProducts();
//			// Invalid id for Id that is bigger then all products in the stock
			if (id > shopStock.getStockProducts().length ) {
				throw new OnlineStoreGeneralException();

			}
			if (quantity > shopStock.getStockProducts()[id].getQuantity() || quantity < 0) {
				throw new ProductQuantityNotAvailableException();
			}
			if (consumerCart.getCoustomerProducts()[id] instanceof Electronics) {
				if (quantity > 3) {
					System.out.println("Electronic products: maximum 3 products");
					break;
				}
			}
			
			
			int nowAtCart = consumerCart.getCoustomerProducts()[id].getQuantity();
			if (nowAtCart + quantity > MAX_AMOUNT_PER_ONE) {
				throw new ReachedMaxAmountException();
			}

			boolean addWhileInCart = currntCostumerCart[id].getQuantity() > 0;
			if (addWhileInCart) {
				throw new CartProductAlreadyExistException();
			}

			// writeDataToMyCart(currntCostumerCart, id, quantity);
			int currentQuantity = currntCostumerCart[id].getQuantity();
			currntCostumerCart[id].setQuantity(currentQuantity + quantity);
			currntCostumerCart[id].setTimeStamp(LocalDateTime.now());
			consumerCart.setCoustomerProducts(currntCostumerCart);

			// reduce the amount in the stock

			Product[] newAmountInStock = this.shopStock.getStockProducts();
			int sotckNumOfProducts = newAmountInStock[id].getQuantity();
			int newQuantityOfProductInStock = sotckNumOfProducts - quantity;
			reserve(newQuantityOfProductInStock, id);
			
			DBL.addOrRemoveProductCart(quantity, id);

			break;

		}

		case "update": {
			Product[] currntCostumerCart = this.consumerCart.getCoustomerProducts();
			// id doesnt exist
			if (id > currntCostumerCart.length) {
				throw new OnlineStoreGeneralException();
			}
			// cart is empty
			boolean checkingIfCartEmpty = checkingEmptyCart();
			if (checkingIfCartEmpty) {
				System.out.println("cart is empty ");
				break;
			}

			// checking if product in cart for updating
			boolean atCart = consumerCart.getCoustomerProducts()[id].getQuantity() > 0;
			if (!atCart) {
				throw new CartProductNotExistException();

			}

			if (consumerCart.getCoustomerProducts()[id] instanceof Electronics && quantity > 3) {
				System.out.println("Electronic products: maximum 3 products");
				break;
			}

			else {
				
				int currentQuantity = currntCostumerCart[id].getQuantity();
				int difference = quantity + currentQuantity;

				if( difference < 0) {
					System.out.println("Invalid quantity");
					break;
				}
				
				currntCostumerCart[id].setQuantity(difference);
				consumerCart.setCoustomerProducts(currntCostumerCart);

				// reduce the amount in the stock
				Product[] newAmountInStock = this.shopStock.getStockProducts();
				int sotckNumOfProducts = newAmountInStock[id].getQuantity();
				int newQuantityOfProductInStock = sotckNumOfProducts - quantity;
				reserve(newQuantityOfProductInStock, id);
				currntCostumerCart[id].setTimeStamp(LocalDateTime.now());
				
				DBL.addOrRemoveProductCart(quantity, id);

				break;
			}
		}
		}
	}

	public boolean checkingEmptyCart() {
		Product[] currntCostumerCart = this.consumerCart.getCoustomerProducts();
		for (int i = 0; i < consumerCart.getCoustomerProducts().length; i++) {
			if (currntCostumerCart[i].getQuantity() > 0) {
				return false;

			}

		}
		return true;
	}

	@Override
	public String toString() {
		return "SystemOnline [shopStock=" + shopStock + ", consumerCart=" + consumerCart + "]";
	}

	@Override
	public void reserve(int quantity, int id) throws ProductQuantityNotAvailableException {
		Product[] newAmountInStock = this.shopStock.getStockProducts();
		newAmountInStock[id].setQuantity(quantity);
		shopStock.setStockProducts(newAmountInStock);

	}

}
