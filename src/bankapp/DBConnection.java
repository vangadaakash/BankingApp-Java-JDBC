package bankapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	 public static Connection getConnection() {
	    	Connection con = null;
	    	 String url = "jdbc:mysql://localhost:3306/simplebank?useSSL=false&allowPublicKeyRetrieval=true";
	         String uName = "root";
	         String pass = "12345"; 
	    	try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection(url, uName, pass);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
	    	return con;
	    }
}
