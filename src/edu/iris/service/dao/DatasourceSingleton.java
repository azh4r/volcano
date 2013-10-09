package edu.iris.service.dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * 
 * 
 * @author azhar
 *
 */
public class DatasourceSingleton {
	
	private static DataSource ds = null;
	
	protected DatasourceSingleton() throws Exception {
	}

	private static DataSource getDatasource() throws Exception {
		// TODO Auto-generated method stub
		if (ds == null) {
			try {
				InitialContext context = new InitialContext();
				DataSource ds = (DataSource) context.lookup( "java:/comp/env/jdbc/postgres" );
				return ds;
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error occurred while creating datasource: "+e.getMessage());
			}	
		} else 
			return ds;
		
	}

	public static Connection getConnection() throws Exception {
		return getDatasource().getConnection();
	}
	

}
