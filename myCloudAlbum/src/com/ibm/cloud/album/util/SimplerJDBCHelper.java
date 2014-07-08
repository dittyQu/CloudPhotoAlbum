package com.ibm.cloud.album.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;

import com.ibm.nosql.json.api.BasicDBList;
import com.ibm.nosql.json.api.BasicDBObject;
import com.ibm.nosql.json.util.JSON;

public class SimplerJDBCHelper {
	public static Connection getConn(){
		Connection conn = null;
		String driver = "com.ibm.db2.jcc.DB2Driver";
		String user = null;
		String password = null;
		String databaseUrl = null;
	
		// 'VCAP_SERVICES' contains all the credentials of services bound to this application.
        // Parse it to obtain the for DB2 connection info
        String VCAP_SERVICES = System.getenv ("VCAP_SERVICES");
        if (VCAP_SERVICES != null) {
          System.out.println ("SimplerJDBCHelper retrieves VCAP_SERVICES content: " + VCAP_SERVICES);
          // parse the VCAP JSON structure	
          BasicDBObject obj = (BasicDBObject) JSON.parse (VCAP_SERVICES);
          String thekey = null;
          Set<String> keys = obj.keySet();
          System.out.println ("Searching through VCAP keys");
    	  // Look for the VCAP key that holds the SQLDB information
          for (String eachkey : keys) {
        	  System.out.println ("Key is: " + eachkey);
        	  if (eachkey.contains("SQLDB")) {
        		  thekey = eachkey;
        		  break;
        	  }
          }
          if (thekey == null) {
        	  System.out.println("Cannot find any SQLDB service in the VCAP; exiting");
          }
          
          BasicDBList list = (BasicDBList) obj.get (thekey);
          obj = (BasicDBObject) list.get ("0");
          System.out.println("Service found: " + obj.get("name"));
          obj = (BasicDBObject) obj.get ("credentials");
          String databaseHost = (String) obj.get ("host");
          String databaseName = (String) obj.get ("db");
          Integer port = (Integer) obj.get ("port");
          user = (String) obj.get ("username");
          password = (String) obj.get ("password");
          databaseUrl = "jdbc:db2://" + databaseHost + ":" + port + "/" + databaseName;
          driver = "com.ibm.db2.jcc.DB2Driver";
          System.out.println("databaseUrl->" + databaseUrl);
          System.out.println("SQLDB service is available in the VCAP now.");
        } else {
        	driver = "com.ibm.db2.jcc.DB2Driver";
        	user = "u38350";
        	password = "rWyZE8kl9r";
        	databaseUrl = "jdbc:db2://198.11.239.114:50000/I_043998";
        	System.out.println("VCAP_SERVICES is null. init the database connection paramater by hard code.");
        } 
       
       
	    try{  
	    	Class.forName(driver);
	    	conn = DriverManager.getConnection(databaseUrl , user , password ) ;   
	    }catch(SQLException se){   
	    	 System.out.println("DB connection failed!");   
	    	 se.printStackTrace() ;
	    }catch(Exception ex){
	    	System.out.println("DB connection failed-driver exception!");
	    	ex.printStackTrace();
	    }
	  return conn;
	}
	
	/**
	 * Don't forget We need to use this way to close pstmt and connection in persistence layer.
	 * @param sql
	 * @param mapper SQL execution parameters
	 * @param conn
	 * @param pstmt
	 * @return
	 */
	
	public static int executeUpdate(ArrayList paramList, Connection conn, PreparedStatement pstmt) {
		int returnCode = -100;
		try{
			Iterator paramIter = paramList.iterator();
			int i=1;
			while(paramIter.hasNext()){
				String eachParam= (String)paramIter.next();
				pstmt.setString(i, eachParam);
				i++;
			}
			returnCode = pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}
	
	public static int executeUpdate(PreparedStatement pstmt) {
		int returnCode = -100;
		try{
			returnCode = pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}
	
	/**
	 * Don't forget: After execute query, we need to close resultset, prepared statement and connection in
	 * persistence layer
	 * @param mapper query parameters
	 * @param conn
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet query(ArrayList paramList, Connection conn, PreparedStatement pstmt )throws SQLException{
		ResultSet result = null;
		try{
			Iterator paramIter = paramList.iterator();
			int i=1;
			while(paramIter.hasNext()){
				String eachParam= (String)paramIter.next();
				pstmt.setString(i, eachParam);
				i++;
			}
			result = pstmt.executeQuery();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Don't forget: After execute query, we need to close resultset, prepared statement and connection in
	 * persistence layer
	 * @param mapper query parameters
	 * @param conn
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet query(Connection conn, PreparedStatement pstmt )throws SQLException{
		ResultSet result = null;
		try{
			result = pstmt.executeQuery();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet result){
		//close connections.
		if(result != null){
			try{
				result.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(conn != null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstmt){
		//close connections.
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(conn != null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
