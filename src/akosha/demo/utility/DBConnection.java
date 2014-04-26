package akosha.demo.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

 public static Connection get() throws IllegalArgumentException{
	 
	 Connection conn=null;
	 try{
		/* Class.forName("com.mysql.jdbc.Driver");
		 String url="jdbc:mysql://mysql-suhail.jelastic.vpsblocks.com.au/akoshadashboard";
		 String user_id="root";
	     String password="Ie0ZYR32me";*/
		 Class.forName("oracle.jdbc.driver.OracleDriver");
		 String url="jdbc:oracle:thin:@localhost:1521:orcl";
		 String user_id="scott";
		 String password="tadpole50";
		 conn=DriverManager.getConnection(url, user_id, password);
		 
	 }catch (ClassNotFoundException e) {
		// TODO: handle exception
		 e.printStackTrace();
	}catch (SQLException e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	 return conn;
 }	
 

}
