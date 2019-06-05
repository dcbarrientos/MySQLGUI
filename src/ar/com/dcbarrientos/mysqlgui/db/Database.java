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

package ar.com.dcbarrientos.mysqlgui.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import ar.com.dcbarrientos.mysqlgui.Application;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Database {
	public static final int DEFAULT_PORT = 3306;
	
	private final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private final String DABASE_URL = "jdbc:mysql://";
	
	public final int DATATYPE_LENTH_INDEX = 0;
	public final int DATATYPE_PRIMARY_KEY_INDEX = 1;
	public final int DATATYPE_NOT_NULL_INDEX = 2;
	public final int DATATYPE_UNIQUE_INDEX = 3;
	public final int DATATYPE_BINARY_INDEX = 4;
	public final int DATATYPE_UNSIGNED_INDEX = 5;
	public final int DATATYPE_ZEROFILL_INDEX = 6;
	public final int DATATYPE_AUTOINCREMENT_INDEX = 7;
	public final int DATATYPE_GENERATED_INDEX = 8;

	private String url;
	public String host;
	public String user;
	private int port;
	private String pass;
	private Connection connection;
	private String selectedDb = "";
	private String selectedTable = "";
	public boolean isSelectedDB = false;
	public boolean isSelectedTable = false;

	private String errorMessage = "";
	private int errorCode;

	public HashMap<String, Boolean[]> mySqlDataType;

	public Database(String host, String user, String pass) {
		this(host, DEFAULT_PORT, user, pass);
	}

	public Database(String host, int port, String user, String pass) {
		loadData();

		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.url = DABASE_URL + host + ":" + this.port;
		connect();
	}

	private void loadData() {
		mySqlDataType = new HashMap<String, Boolean[]>();
		// Los booleans de esta estructura habilitan o deshabilitan controles.
		// Length, Primary key, not null, unique, binary, unsigned, zero fill, autoincrement, generated

		mySqlDataType.put("BINARY", 			new Boolean[]{true, true, true, true, false, false, false, false, true});
		mySqlDataType.put("BLOB", 				new Boolean[]{true, true, true, true, false, false, false, false, true});
		mySqlDataType.put("LONGBLOB", 			new Boolean[]{true, true, true, true, false, false, false, false, true});
		mySqlDataType.put("MEDIUMBLOB", 		new Boolean[]{false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("TINYBLOB", 			new Boolean[]{false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("VARBINARY", 			new Boolean[]{true, true, true, true, false, false, false, false, true});
		
		mySqlDataType.put("DATE", 				new Boolean[]{false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("DATETIME", 			new Boolean[]{true, true, true, true, false, false, false, false, true});
		mySqlDataType.put("TIME", 				new Boolean[]{true, true, true, true, false, false, false, false, true});
		mySqlDataType.put("TIMESTAMP", 			new Boolean[]{true, true, true, true, false, false, false, false, true});
		mySqlDataType.put("YEAR", 				new Boolean[]{true, true, true, true, false, false, false, false, true});

		mySqlDataType.put("GEOMETRY", 			new Boolean[] {false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("GEOMETRYCOLLECTION", new Boolean[] {false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("LINESTRING", 		new Boolean[] {false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("MULTILINESTRING", new Boolean[] {false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("MULTIPOINT", new Boolean[] {false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("MULTIPOLYGON", new Boolean[] {false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("POINT", new Boolean[] {false, true, true, true, false, false, false, false, true});
		mySqlDataType.put("POLYGON", new Boolean[] {false, true, true, true, false, false, false, false, true});
		
		mySqlDataType.put("BIGINT", 			new Boolean[]{true, true, true, true, false, true, true, true, true});
		mySqlDataType.put("DECIMAL", 			new Boolean[]{false, true, true, true, false, true, true, true, true});
		mySqlDataType.put("DOUBLE", 			new Boolean[]{false, true, true, true, false, true, true, true, true});
		mySqlDataType.put("FLOAT", 				new Boolean[]{false, true, true, true, false, true, true, true, true});
		mySqlDataType.put("INT", 				new Boolean[]{true, true, true, true, false, true, true, true, true});
		mySqlDataType.put("MEDIUMINT", 			new Boolean[]{true, true, true, true, false, true, true, true, true});
		mySqlDataType.put("REAL", 				new Boolean[]{false, true, true, true, false, true, true, true, true});
		mySqlDataType.put("SMALLINT", 			new Boolean[]{true, true, true, true, false, true, true, true, true});
		mySqlDataType.put("TINYINT", 			new Boolean[]{true, true, true, true, false, true, true, true, true});

		mySqlDataType.put("CHAR", 				new Boolean[]{true, true, true, true, true, false, false, false, true});
		mySqlDataType.put("NCHAR", 				new Boolean[]{true, true, true, true, true, false, false, false, true});
		mySqlDataType.put("NVARCHAR", 			new Boolean[]{true, true, true, true, true, false, false, false, true});
		mySqlDataType.put("VARCHAR", 			new Boolean[]{true, true, true, true, true, false, false, false, true});

		mySqlDataType.put("LONGTEXT", 			new Boolean[]{false, true, true, true, true, false, false, false, true});
		mySqlDataType.put("MEDIUMTEXT", 		new Boolean[]{false, true, true, true, true, false, false, false, true});
		mySqlDataType.put("TEXT", 				new Boolean[]{true, true, true, true, true, false, false, false, true});
		mySqlDataType.put("TINYTEXT", 			new Boolean[]{false, true, true, true, true, false, false, false, true});

		mySqlDataType.put("BIT", 				new Boolean[]{true, true, true, true, false, false, false, false, true});
		mySqlDataType.put("BOOLEAN", 			new Boolean[]{false, true, true, true, false, true, true, true, true});
		mySqlDataType.put("ENUM", 				new Boolean[]{true, true, true, true, false, false, false, false, true});

		//mySqlDataType.put("INTEGER", 			new Boolean[]{true});
		//mySqlDataType.put("DEC", 				new Boolean[]{true});
		//mySqlDataType.put("NUMERIC", 			new Boolean[]{true});
		//mySqlDataType.put("FIXED", 				new Boolean[]{true});
		//mySqlDataType.put("DOUBLE PRECISION", 	new Boolean[]{true});
		//mySqlDataType.put("BOOL", 				new Boolean[]{false});

	}

	public Connection connect() {
		connection = null;

		try {
			Class.forName(DATABASE_DRIVER);
			connection = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			errorMessage = e.getMessage();
			errorCode = -1;
			if (Application.DEBUG)
				e.printStackTrace();
		} catch (SQLException e) {
			errorMessage = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
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
			if (Application.DEBUG)
				e.printStackTrace();

			errorMessage = e.getMessage();
			errorCode = e.getErrorCode();

			return false;
		}
		return true;
	}

	public boolean isConnected() {
		if (connection == null)
			return false;

		return true;
	}

	public String getVersion() {
		Query query = new Query(this);
		String v = "";
		query.executeQuery("Select version();");
		query.next();
		v = query.getString(1);
		query.close();

		return v;
	}

	public String getErrorMessage() {
		return errorCode + ": " + errorMessage;
	}

	public String getConnectionID() {
		return user + "@" + host;
	}
	
	public String getDefaultCharset() {
		Query query = new Query(this);
		String defaultCharset = "";
		query.executeQuery("SHOW VARIABLES LIKE  'character_set_database';");
		query.next();
		defaultCharset = query.getString("Value");
		query.close();
		
		return defaultCharset;
	}
	
	public String getDefaultCharset(String charset) {
		Query query = new Query(this);
		String defaultCollation = "";
		query.executeQuery("SHOW CHARACTER SET WHERE CHARSET = '" + charset + "';");
		query.next();
		defaultCollation = query.getString("Value");
		query.close();
		
		return defaultCollation;
	}	

	public void setSelectedTable(String db, String table) {
		setSelectedDatabase(db);

		if (table.equals(""))
			this.isSelectedTable = false;
		else
			this.isSelectedTable = true;

		this.selectedTable = table;
	}

	public void setSelectedDatabase(String db) {
		if (db.equals(""))
			this.isSelectedDB = false;
		else
			this.isSelectedDB = true;

		this.selectedDb = db;

		this.selectedTable = "";
		this.isSelectedTable = false;
	}

	public String getSelectedDatabase() {
		return selectedDb;
	}

	public String getSelectedTable() {
		return this.selectedTable;
	}

	/**
	 * Verifica si existe una base de datos.
	 * 
	 * @param databaseName Base de datos a verificar
	 * @return -1 si existe, 0 si existe
	 */
	public int existeDatabase(String databaseName) {
		int resu = 0;

		if (databaseName.length() == 0)
			resu--;
		else {
			Query query = new Query(this);
			String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA" + " WHERE SCHEMA_NAME = '" + databaseName
					+ "'";
			query.executeQuery(sql);

			if (!query.next())
				resu--;
		}
		return resu;
	}

	/**
	 * Verifica si existe una tabla en cierta base de datos
	 * 
	 * @param databaseName nombre de la base de datos a verificar
	 * @param tableName    nombre de la tabla a verificar
	 * @return -2 si la tabla y la base de datos no existe, -1 si solo la tabla no
	 *         existe, 0 si existen ambas.
	 */
	public int existeTabla(String databaseName, String tableName) {
		int resu = 0;
		resu += existeDatabase(databaseName);

		if (tableName.length() == 0)
			resu--;
		else {
			Query query = new Query(this);
			String sql = "SHOW TABLES FROM " + databaseName + " LIKE '" + tableName + "'";
			query.executeQuery(sql);

			if (!query.next())
				resu--;
		}

		return resu;
	}
}
