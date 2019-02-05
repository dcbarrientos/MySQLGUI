/*
 *  Copyright (C) 2019 Diego Barrientos <dc_barrientos@yahoo.com.ar>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/** 
 * Connection.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 1 feb. 2019, 18:06:45 
 */

package ar.com.dcbarrientos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.dcbarrientos.Application;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Database {
	public static final int DEFAULT_PORT = 3306;
	private final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private final String DABASE_URL = "jdbc:mysql://";
	
	private String url;
	public String host;
	public String user;
	private int port;
	private String pass;
	private Connection connection;
	private Statement st;
	private ResultSet result;
	
	private String errorMessage = "";
	private int errorCode;
	

	public Database(String host, String user, String pass) {
		this(host, DEFAULT_PORT, user, pass);
	}

	public Database(String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.url = DABASE_URL + host + ":" + this.port;
		connect();
	}

	public Connection connect() {
		connection = null;

		try {
			Class.forName(DATABASE_DRIVER);
			connection = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			errorMessage = e.getMessage();
			errorCode = -1;
			if(Application.DEBUG)
				e.printStackTrace();
		} catch (SQLException e) {
			errorMessage = e.getMessage();
			errorCode = e.getErrorCode();
			
			if(Application.DEBUG)
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean close() {
		try {
			connection.close();
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();

			errorMessage = e.getMessage();
			errorCode = e.getErrorCode();

			return false;
		}
		return true;
	}
	
	
	public boolean isConnected() {
		if(connection == null)
			return false;
		
		return true;
	}
	
	public boolean executeQuery(String sqlTxt) {
		try {
			st = connection.createStatement();
			result = st.executeQuery(sqlTxt);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int getQueryCount() {
		int c = 0;
		if(result != null) {
			try {
				result.last();
				c = result.getRow();
				result.first();
			} catch (SQLException e) {
				if(Application.DEBUG)
					e.printStackTrace();
			}
		}
		
		return c;
	}
	
	public String getVersion() {
		String v = "";
		try {
			this.executeQuery("Select version();");
			result.next();
			v = result.getString(1);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}
		
		closeQuery();
		return v;
	}
	
	public boolean closeQuery() {
		try {
			result.close();
			st.close();
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public ResultSet getQueryResult() {
		return result;
	}
	
	public String getErrorMessage() {
		return errorCode + ": " + errorMessage;
	}
	
	public String getConnectionID() {
		return user + "@" + host;
	}
}
