package ID_207702929;

import java.sql.*;
import java.time.LocalDateTime;

import ID_207702929.Clothing.sex;
import ID_207702929.Product.theCategory;

public class ConnectDBL {

	Connection conn = null;
	Product[] productsStock;
	Product[] productsCart;

	private Connection ConnectDB() {
		try {
			Class.forName("org.postgresql.Driver");
			String dbUrl = "jdbc:postgresql://localhost:5432/ShoppingCart";
			conn = DriverManager.getConnection(dbUrl, "postgres", "123123");	
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState()); 
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void CloseConnection() throws SQLException {
		conn.close();
	}
	
	private void CleanupPstmt(PreparedStatement pstmt) {
		if (pstmt != null) {
			try { pstmt.close();
			} catch (SQLException sqlEx) { 
				
			} 
			pstmt = null; 
			}
	}
	
	private void Cleanuprs(ResultSet rs) {
		if (rs != null) {
			try { rs.close();
			} catch (SQLException sqlEx) { 
				
			} 
			rs = null; 
			}
	}
	
	
	
	private void Cleanup(PreparedStatement pstmt, ResultSet rs) {
		CleanupPstmt(pstmt);
		Cleanuprs(rs);
	}

	
//----------Initialize from the database----------
	public int readDatabase() throws SQLException {
		
		if (conn == null) {
			ConnectDB();
		}
        PreparedStatement pstmt = null;
        
        productsStock =  new Product[initializeArray("stock", pstmt)];
        productsCart =  new Product[initializeArray("stock", pstmt)];
        
        String query = "SELECT * FROM Product";
        pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
		
        int i = 0;
        while (rs.next()) {
        	
            int productId = rs.getInt("ProductID");
            String productName = rs.getString("ProductName");
            String category = rs.getString("Category");
            
            int quantityInStock = readDataCartOrStock("stock", productId, pstmt);
            productsStock[i] = readDataCategory(productId, productName, category, quantityInStock, pstmt);
            
            int quantityInCart = readDataCartOrStock("cart", productId, pstmt);
            if(quantityInCart > 0) {
            	productsCart[i] = readDataCategory(productId, productName, category, quantityInCart, pstmt);
            	readLocalDateTime(productsCart[i], pstmt);
            } else {
            	productsCart[i] = readDataCategory(productId, productName, category, 0, pstmt);
                }
            i++;
			}
        Cleanup(pstmt, rs);
		return 1;
	}
		
	private int initializeArray(String tableName, PreparedStatement pstmt) throws SQLException {
		String query = "SELECT count_rows_in_table(?) AS row_count"; 
		pstmt = conn.prepareStatement(query);  
		pstmt.setString(1, tableName); 
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int numOfProducts = rs.getInt("row_count");
		Cleanuprs(rs);
		return numOfProducts;
	}

	private void readLocalDateTime(Product product, PreparedStatement pstmt) throws SQLException {
	    String query = "SELECT * FROM CART WHERE productid = ?";
	    pstmt = conn.prepareStatement(query);
	    pstmt.setInt(1, product.getId());
	    ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            LocalDateTime time = rs.getObject("TimeDate", LocalDateTime.class);
	            product.setTimeStamp(time);
	        } else {
	            System.out.println("No results found for product ID: " + product.getId());
	    }
	        Cleanuprs(rs);
	}

	private int readDataCartOrStock(String str, int productID, PreparedStatement pstmt) throws SQLException {
	    String query = "SELECT * FROM " + str + " WHERE ProductID = ?";
	    int num = 0;
	    pstmt = conn.prepareStatement(query);
	    pstmt.setInt(1, productID);
	    ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            num = rs.getInt("Quantity");
	        }
	    Cleanuprs(rs);
	    return num;
	}

	private Product readDataCategory(int productID , String productName, String strCategory, int quantity, PreparedStatement pstmt) throws SQLException {
		
		if (strCategory.equalsIgnoreCase("books")) {
			return readDataCategoryBooks(productID, productName, strCategory, quantity, pstmt);
			
		} else if (strCategory.equalsIgnoreCase("clothing")) {
			return readDataCategoryClothing(productID, productName, strCategory, quantity, pstmt);
			
		} else if (strCategory.equalsIgnoreCase("electronics")) {
			return readDataCategoryElectronics(productID, productName, strCategory, quantity, pstmt);
		} else {
			return null; 
		}
	}
	
	private Books readDataCategoryBooks(int productID , String productName, String strCategory, int quantity, PreparedStatement pstmt) throws SQLException {
		Books book = null;
	    String query = "SELECT * FROM books WHERE ProductID = ?";
	    pstmt = conn.prepareStatement(query);
	    pstmt.setInt(1, productID);
	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
	        int pages = rs.getInt("pages");
	        String author = rs.getString("author");
	        book = new Books(productID, theCategory.valueOf(strCategory), productName, quantity, String.valueOf(pages), author);
	    }
	    Cleanuprs(rs);
	    return book;
	}
	
	private Clothing readDataCategoryClothing(int productID , String productName, String strCategory, int quantity, PreparedStatement pstmt) throws SQLException {
		Clothing clothing = null;
	    String query = "SELECT * FROM clothing WHERE ProductID = ?";
	    pstmt = conn.prepareStatement(query);
	    pstmt.setInt(1, productID);
	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
	        String size = rs.getString("size");
	        String color = rs.getString("color");
	        String sex1 = rs.getString("sex");
	        clothing = new Clothing(productID, theCategory.valueOf(strCategory), productName, quantity, size, color, sex.valueOf(sex1));
	    }

	    Cleanuprs(rs);
	    return clothing;
	}
	
	private Electronics readDataCategoryElectronics(int productID, String productName, String strCategory, int quantity, PreparedStatement pstmt) throws SQLException {
	    Electronics electronics = null;
	    String query = "SELECT * FROM electronic WHERE ProductID = ?";
	    pstmt = conn.prepareStatement(query);
	    pstmt.setInt(1, productID);
	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
	        String model = rs.getString("model");
	        String company = rs.getString("company");
	        electronics = new Electronics(productID, theCategory.valueOf(strCategory), productName, quantity, model, company);
	    }

	    Cleanuprs(rs);
	    return electronics;
	}

//----------add and remove function ----------
	
public void addOrRemoveProductCart(int amount, int productid) throws SQLException  {
	String query = "UPDATE cart SET quantity = quantity + ? WHERE productid = ?;";
	PreparedStatement pstmt = conn.prepareStatement(query);
	pstmt.setInt(1, amount);
	pstmt.setInt(2, productid);
    int affectedRows = pstmt.executeUpdate();
    if (affectedRows == 0) {
        System.out.println("No rows affected. Product ID may not exist.");
    }
}


}
