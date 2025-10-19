package bankapp;
import java.sql.*;
import java.util.Scanner;

public class Main {
   static Scanner sc = new Scanner(System.in);
   public static void main(String []args) {
	   while(true) {
		   System.out.println("\n--- Simple Bank Menu ---");
		   System.out.println("1. Register");
		   System.out.println("2. Login");
		   System.out.println("0. Exit");
		   System.out.println("Choose: ");
		   int choice = sc.nextInt();
		   sc.nextLine();
		   
		   switch(choice) {
		   case 1: register(); break;
		   case 2: login(); break;
		   case 0: System.exit(0);
		   default: System.out.println("Invalid");
		   }
	   }
   }
	   static void register() {
		   try(Connection con = DBConnection.getConnection()){
			   System.out.print("Name: ");
			   String name = sc.nextLine();
			   System.out.println("Email: ");
			   String email = sc.nextLine();
			   System.out.println("Password: ");
			   String pass = sc.nextLine();
			   
			   PreparedStatement ps = con.prepareStatement(
					   "insert into users (name, email, password) values (?, ?, ?)"
			   );
				ps.setString(1, name);
				ps.setString(2, email);
				ps.setString(3, pass);
				int i = ps.executeUpdate();
				System.out.println(i > 0 ? "Registered Success" : "Failed to register.");
		   } catch(Exception e) {
			   System.out.println(e);
		   }
	   }
	   static void login() {
		   try(Connection con = DBConnection.getConnection()){
			   System.out.println("Email: ");
			   String email = sc.nextLine();
			   System.out.println("Password: ");
			   String pass = sc.nextLine();
			   
			   PreparedStatement ps = con.prepareStatement(
					   "select * from users where email=? and password=?"
			   );
				ps.setString(1, email);
				ps.setString(2, pass);
				ResultSet rs = ps.executeQuery();
				
				if(rs.next()) {
					int userId = rs.getInt("id");
					System.out.println("Login Successful! Welcome " + rs.getString("name"));
					userMenu(con, userId);
				}else {
					System.out.println("Invalid Creditionals.");
				}
	        }catch(Exception e) {
	        	System.out.println(e);
	        }
   }
	   static void userMenu(Connection con, int usereId) {
		   while(true) {
			   System.out.println("\n--- User Menu ---");
			   System.out.println("1. Check Balance");
			   System.out.println("2. Deposit");
			   System.out.println("3. Withdraw");
			   System.out.println("4. Logout");
			   System.out.println("Choose: ");
			   int choice = sc.nextInt();
			   
			   switch(choice) {
			   case 1: checkBalance(con, usereId); break;
			   case 2: deposit(con, usereId); break;
			   case 3: withdraw(con, usereId); break;
			   case 4: 
				   System.out.println("Logged out successfully");
			       return;
			   default: System.out.println("Invalid");
			   }
		   }
	   }
	   static void checkBalance(Connection con, int userId) {
		   try {
			   PreparedStatement ps = con.prepareStatement(
					   "select balance from users where id=?"
					  );
			   ps.setInt(1, userId);
			   ResultSet rs = ps.executeQuery();
			   if(rs.next()) {
				   System.out.println("Your Balance: " + rs.getDouble("balance"));
			   }
		   }catch(Exception e) {
			   System.out.println(e);
		   }
	   }
	   static void deposit(Connection con, int userId) {
		   try {
			   System.out.println("Enter amount: ");
			   double amt = sc.nextDouble();
			   PreparedStatement ps = con.prepareStatement(
					   "update users set balance = balance + ? where id = ?"
					   );
			   ps.setDouble(1, amt);
			   ps.setInt(2, userId);
			   ps.executeUpdate();
			   System.out.println("Amout deposited");
		   }catch(Exception e) {
			   System.out.println(e);
		   }
	   }
	   static void withdraw(Connection con, int userId) {
		   try {
			   System.out.println("Enter amount: ");
			   double amt = sc.nextDouble();
			   
			   PreparedStatement ps1 = con.prepareStatement(
					   "select balance from users where id=? "
					   );
			   ps1.setInt(1, userId);
			   ResultSet rs = ps1.executeQuery();
			   if(rs.next() && rs.getDouble("balance") >= amt) {
				   PreparedStatement ps2 = con.prepareStatement(
						   "update users set balance = balance - ? where id = ?"
						   );
				   ps2.setDouble(1, amt);
				   ps2.setInt(2, userId);
				   ps2.executeUpdate();
				   System.out.println("Amount withdrwan!");
			   }else {
				   System.out.println("Insufficient balance.");
			   }
		   }catch(Exception e) {
			   System.out.println(e);
		   }
	   }
}
