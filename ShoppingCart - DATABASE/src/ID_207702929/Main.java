package ID_207702929;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;

import ID_207702929.Product.theCategory;

public class Main {
	private static SystemOnline app;



	public static void main(String[] args)
			throws IOException, OnlineStoreGeneralException, ProductQuantityNotAvailableException,
			CartProductNotExistException, CartProductAlreadyExistException, ClassNotFoundException, SQLException {

		
		ConnectDBL test = new ConnectDBL();
		test.readDatabase();
		
		Product[] allProducts = new Product[test.productsStock.length];
		Product[] costumerProducts = new Product[test.productsCart.length];
		
		copyProductArray(test.productsStock, allProducts);
		copyProductArray(test.productsCart, costumerProducts);
		
		Stock shopStock = new Stock(allProducts);
		Cart costumerCart = new Cart(costumerProducts);
		
		app = new SystemOnline(shopStock, costumerCart);

		Scanner scan = new Scanner(System.in);

		final int Q1 = 1;
		final int Q2 = 2;
		final int Q3 = 3;
		final int Q4 = 4;
		final int Q5 = 5;
		final int Q6 = 6;
		final int Q7 = 7;
		final int EXIT = 8;
		int choice;

		System.out.println("welcom this is a online system:\n");
		System.out.println("choose from menu your request:\n");
		app.presentStock();
		do {
			System.out.println("         ==MENUE==         ");
			System.out.println("1) Present stock");
			System.out.println("2) Add a new product to cart");
			System.out.println("3) update a product from cart");
			System.out.println("4) Remove a product from cart");
			System.out.println("5) Show client shopping cart");
			System.out.println("6) our catalog");
			System.out.println("7) chooseing another sort for stock");
			System.out.println("8) Exit");
			
			choice = scan.nextInt();

			switch (choice) {

			case Q1:

				presentStock(scan);

				break;

			case Q2:
				addProduct(scan, test);
				break;

			case Q3:
				updateProduct(scan, test);

				break;
			case Q4:
				removeProduct(scan, test);

				break;

			case Q5:
				showMyCart(scan);
				break;
			case Q6:
				showTheCatalog(scan);
				break;
			case Q7:
				sortByUser(scan, allProducts);
				break;

			case EXIT:
				test.CloseConnection();
				System.out.println("Bye!\n");
				break;
			default:
				System.out.println("Invalid input\n");
				break;

			}

		} while (choice != EXIT);

		scan.close();
	}
	
	public static void copyProductArray(Product[] source, Product[] destination) {
	        if (source.length != destination.length) {
	            throw new IllegalArgumentException("Arrays must be of the same length");
	        }

	        for (int i = 0; i < source.length; i++) {
	            destination[i] = source[i];
	        }
	    }

	// option 7
	private static void sortByUser(Scanner scan, Product[] allProducts) {
		System.out.println("1) Sort by category");
		System.out.println("2) Sort by name");
		System.out.println("3) Sory by quantity");
		int userChoice = scan.nextInt();

		switch (userChoice) {

		case 1:

			app.SortByCatagory(allProducts);

			break;

		case 2:
			app.SortByname(allProducts);

			break;

		case 3:

			app.SortByQuantity(allProducts);
			break;

		}
	}

	// option 8
	private static void exitFromSystem(Scanner scan, String filename, Product[] productsReamin) throws IOException {
		System.out.println("Option 8 selected: Exit.");
		saveProducts(productsReamin, filename);
		System.out.println("Thanks for choosing System online\nhave a nice day :)");
	}

	private static void saveProducts(Product[] productsReamin, String filename) throws IOException {
		ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(filename));
		file.writeObject(productsReamin);
		file.close();

	}

	public static Product[] readFromData(String filename) throws IOException, ClassNotFoundException {
		ObjectInputStream file = new ObjectInputStream(new FileInputStream(filename));
		Product[] allProducts = (Product[]) file.readObject();
		file.close();
		return allProducts;
	}

	// option 1
	private static void presentStock(Scanner scan) {
		System.out.println("Option 1 selected: Present stock.");
		app.presentStock();

	}

	// option 2
	private static void addProduct(Scanner scan, ConnectDBL DBL) throws OnlineStoreGeneralException, CartProductNotExistException,
			ProductQuantityNotAvailableException, CartProductAlreadyExistException, IOException, SQLException {
		System.out.println("Option 2 selected: Add a new product to cart.");
		System.out.println("Enter product id:");
		int id = scan.nextInt();
		System.out.println("Enter amount:");
		int amount = scan.nextInt();
		try {
			app.updateProductById("add", id, amount, DBL);
		} catch (OnlineStoreGeneralException ex) {
			System.out.println("Invalid Id,product doesent exsist");
		} catch (ProductQuantityNotAvailableException e) {
			System.out.println("Invalid quantity");
		} catch (CartProductAlreadyExistException e) {
			System.out.println("you are tring to add a product that already exist in your cart.");
		} catch (ReachedMaxAmountException e) {
			System.out.println("you are tring to buy more than the maximum per product.\n please enter less than "
					+ app.MAX_AMOUNT_PER_ONE);

		}
	}

	// option 3
	private static void updateProduct(Scanner scan, ConnectDBL DBL) throws ProductQuantityNotAvailableException,
			CartProductNotExistException, OnlineStoreGeneralException, CartProductAlreadyExistException, IOException, SQLException {
		System.out.println("Option 3 selected: update a product from cart.");
		System.out.println("Enter product id:");
		int id = scan.nextInt();
		System.out.println("Enter amount:");
		int amount = scan.nextInt();
		try {
			app.updateProductById("update", id, amount, DBL);
		} catch (CartProductNotExistException ex) {
			System.out.println("Invalid Id,product doesent exsist in your cart\nyou need to add it first.\n");
		} catch (ProductQuantityNotAvailableException e) {
			System.out.println("you are tring to add quantity that doesent exist");
		} catch (ReachedMaxAmountException e) {
			System.out.println("you reached to the max amount per one product.");
		}
	}

	// option 4
	private static void removeProduct(Scanner scan, ConnectDBL DBL) throws CartProductNotExistException, SQLException {
		System.out.println("Option 4 selected: Remove a product from cart.");
		System.out.println("Enter product id:");
		int id = scan.nextInt();
		try {
			app.removeProductById(id, DBL);
		} catch (CartProductNotExistException e) {
			System.out.println("you are tring to remove a product that doesnt exist in your cart\nadd it first ");
		}
	}

	// option 5
	private static void showMyCart(Scanner scan) throws IOException {
		System.out.println("Option 5 selected: Show client shopping cart.");
		app.presentCosumerCart();
	}

	// option 6
	private static void showTheCatalog(Scanner scan) {
		System.out.println("Option 6 selected:our catalog.");
		app.presentStockWithoutProductInCart();
	}

	public static Product[] readFromFile(String fileName) throws IOException {
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		br.readLine(); // Skip the header line

		int numOfProducts = 0;
		while (br.readLine() != null) {
			numOfProducts++;
		}

		br.close();

		Product[] allProducts = new Product[numOfProducts];

		// Read the file again to create Product objects and store them in the array
		br = new BufferedReader(new FileReader(f));
		br.readLine(); // Skip the header line

		String line;
		int i = 0;

		while ((line = br.readLine()) != null) {
			String[] list = line.split(",");
			theCategory category = theCategory.valueOf(list[0]);
			String productName = list[1];
			int quantity = Integer.parseInt(list[2]);
			String param1 = list[3];
			String param2 = list[4];
			Clothing.sex size;
			if (category == theCategory.Clothing) {
				size = Clothing.sex.valueOf(list[5]);
				allProducts[i] = new Clothing(i, category, productName, quantity, param1, param2, size);
			} else {
				if (category == theCategory.Books) {
					allProducts[i] = new Books(i, category, productName, quantity, param1, param2);
				} else {
					allProducts[i] = new Electronics(i, category, productName, quantity, param1, param2);
				}

			}

			i++;
		}
		br.close();

		return allProducts;
	}
	
	public static Product[] initizlizeCosumerCart(String fileName) throws IOException {
		File f = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(f));
		br.readLine(); // Skip the header line

		int numOfProducts = 0;
		while (br.readLine() != null) {
			numOfProducts++;
		}
		br.close();

		Product[] allProducts = new Product[numOfProducts];

		// Read the file again to create Product objects and store them in the array
		br = new BufferedReader(new FileReader(f));
		br.readLine(); // Skip the header line

		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
			String[] list = line.split(",", -1);
			theCategory category = theCategory.valueOf(list[0]);
			String productName = list[1];
			String param1 = list[3];
			String param2 = list[4];
			Clothing.sex size;
			if (category == theCategory.Clothing) {
				size = Clothing.sex.valueOf(list[5]);
				allProducts[i] = new Clothing(i, category, productName, 0, param1, param2, size);
			} else {
				if (category == theCategory.Books) {
					allProducts[i] = new Books(i, category, productName, 0, param1, param2);
				} else {
					allProducts[i] = new Electronics(i, category, productName, 0, param1, param2);
				}

			}
			i++;
		}
		br.close();

		return allProducts;
	}
}
