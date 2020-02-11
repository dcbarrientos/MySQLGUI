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
import java.sql.Statement;
import java.util.HashMap;

import com.github.vertical_blank.sqlformatter.SqlFormatter;

import ar.com.dcbarrientos.mysqlgui.Application;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Database {
	public static final int DEFAULT_PORT = 3306;

	private final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private final String DABASE_URL = "jdbc:mysql://";

	public static int NONE = -1;
	public static int SERVER = 0;
	public static int DATABASE = 1;
	public static int TABLE = 2;
	public static int VIEW = 3;
	public static int STORED_PROCEDURE = 4;
	public static int FUNCTION = 5;
	public static int SEPARATOR = 6;

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
	private String selectedElement;
	private int selectedType;
//	private String selectedTable = "";
	public boolean isSelectedDB = false;
//	public boolean isSelectedTable = false;

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
		// Length, Primary key, not null, unique, binary, unsigned, zero fill,
		// autoincrement, generated

		mySqlDataType.put("BINARY", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("BLOB", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("LONGBLOB", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("MEDIUMBLOB", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("TINYBLOB", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("VARBINARY", new Boolean[] { true, true, true, true, false, false, false, false, true });

		mySqlDataType.put("DATE", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("DATETIME", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("TIME", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("TIMESTAMP", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("YEAR", new Boolean[] { true, true, true, true, false, false, false, false, true });

		mySqlDataType.put("GEOMETRY", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("GEOMETRYCOLLECTION",
				new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("LINESTRING", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("MULTILINESTRING",
				new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("MULTIPOINT", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("MULTIPOLYGON", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("POINT", new Boolean[] { false, true, true, true, false, false, false, false, true });
		mySqlDataType.put("POLYGON", new Boolean[] { false, true, true, true, false, false, false, false, true });

		mySqlDataType.put("BIGINT", new Boolean[] { true, true, true, true, false, true, true, true, true });
		mySqlDataType.put("DECIMAL", new Boolean[] { false, true, true, true, false, true, true, true, true });
		mySqlDataType.put("DOUBLE", new Boolean[] { false, true, true, true, false, true, true, true, true });
		mySqlDataType.put("FLOAT", new Boolean[] { false, true, true, true, false, true, true, true, true });
		mySqlDataType.put("INT", new Boolean[] { true, true, true, true, false, true, true, true, true });
		mySqlDataType.put("MEDIUMINT", new Boolean[] { true, true, true, true, false, true, true, true, true });
		mySqlDataType.put("REAL", new Boolean[] { false, true, true, true, false, true, true, true, true });
		mySqlDataType.put("SMALLINT", new Boolean[] { true, true, true, true, false, true, true, true, true });
		mySqlDataType.put("TINYINT", new Boolean[] { true, true, true, true, false, true, true, true, true });

		mySqlDataType.put("CHAR", new Boolean[] { true, true, true, true, true, false, false, false, true });
		mySqlDataType.put("NCHAR", new Boolean[] { true, true, true, true, true, false, false, false, true });
		mySqlDataType.put("NVARCHAR", new Boolean[] { true, true, true, true, true, false, false, false, true });
		mySqlDataType.put("VARCHAR", new Boolean[] { true, true, true, true, true, false, false, false, true });

		mySqlDataType.put("LONGTEXT", new Boolean[] { false, true, true, true, true, false, false, false, true });
		mySqlDataType.put("MEDIUMTEXT", new Boolean[] { false, true, true, true, true, false, false, false, true });
		mySqlDataType.put("TEXT", new Boolean[] { true, true, true, true, true, false, false, false, true });
		mySqlDataType.put("TINYTEXT", new Boolean[] { false, true, true, true, true, false, false, false, true });

		mySqlDataType.put("BIT", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("BOOLEAN", new Boolean[] { false, true, true, true, false, true, true, true, true });
		mySqlDataType.put("ENUM", new Boolean[] { true, true, true, true, false, false, false, false, true });
		mySqlDataType.put("SET", new Boolean[] { true, true, true, true, false, false, false, false, true });

		// mySqlDataType.put("INTEGER", new Boolean[]{true});
		// mySqlDataType.put("DEC", new Boolean[]{true});
		// mySqlDataType.put("NUMERIC", new Boolean[]{true});
		// mySqlDataType.put("FIXED", new Boolean[]{true});
		// mySqlDataType.put("DOUBLE PRECISION", new Boolean[]{true});
		// mySqlDataType.put("BOOL", new Boolean[]{false});

	}

	/**
	 * Attempts to establish a connection to the given database. The
	 * <code>DriverManager</code> attempts to select an appropriate driver from the
	 * set of registered JDBC drivers.
	 */
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

	/**
	 * Get the current connection.
	 * 
	 * @return current connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Sets the given catalog name in order to select a subspace of this
	 * <code>Connection</code> object's database in which to work.
	 * <P>
	 * If the driver does not support catalogs, it will silently ignore this
	 * request.
	 * <p>
	 * Calling {@code setCatalog} has no effect on previously created or prepared
	 * {@code Statement} objects. It is implementation defined whether a DBMS
	 * prepare operation takes place immediately when the {@code Connection} method
	 * {@code prepareStatement} or {@code prepareCall} is invoked. For maximum
	 * portability, {@code setCatalog} should be called before a {@code Statement}
	 * is created or prepared.
	 *
	 * @param catalog the name of a catalog (subspace in this
	 *                <code>Connection</code> object's database) in which to work
	 * @see #getCatalog
	 */
	public boolean setCatalog(String catalog) {
		try {
			connection.setCatalog(catalog);
		} catch (SQLException e) {
			if (Application.DEBUG)
				e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Creates a <code>Statement</code> object for sending SQL statements to the
	 * database. SQL statements without parameters are normally executed using
	 * <code>Statement</code> objects. If the same SQL statement is executed many
	 * times, it may be more efficient to use a <code>PreparedStatement</code>
	 * object.
	 * <P>
	 * Result sets created using the returned <code>Statement</code> object will by
	 * default be type <code>TYPE_FORWARD_ONLY</code> and have a concurrency level
	 * of <code>CONCUR_READ_ONLY</code>. The holdability of the created result sets
	 * can be determined by calling {@link #getHoldability}.
	 *
	 * @return a new default <code>Statement</code> object
	 */
	public Statement createStatement() {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			if (Application.DEBUG)
				e.printStackTrace();
		}

		return null;
	}

	/**
	 * Releases this <code>Connection</code> object's database and JDBC resources
	 * immediately instead of waiting for them to be automatically released.
	 * <P>
	 * Calling the method <code>close</code> on a <code>Connection</code> object
	 * that is already closed is a no-op.
	 * <P>
	 * It is <b>strongly recommended</b> that an application explicitly commits or
	 * rolls back an active transaction prior to calling the <code>close</code>
	 * method. If the <code>close</code> method is called and there is an active
	 * transaction, the results are implementation-defined.
	 * <P>
	 */
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

	/**
	 * Gives a <code>boolean</code> that will change depending on if there is a
	 * connection or not.
	 * 
	 * @return true if it is connected, false if is not.
	 */
	public boolean isConnected() {
		if (connection == null)
			return false;

		return true;
	}

	
	/**
	 * Retrieves a <code>String</code> with the database server version.
	 * 
	 * @return the server version.
	 */
	public String getVersion() {
		Query query = new Query(this);
		String v = "";
		query.executeQuery("Select version();");
		query.next();
		v = query.getString(1);
		query.close();

		return v;
	}

	/**
	 * Get the current error message.
	 * 
	 * @return the error message.
	 */
	public String getErrorMessage() {
		return errorCode + ": " + errorMessage;
	}

	/**
	 * Get the current connection id (root@server.com).
	 * 
	 * @return the current connection id.
	 */
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

	// Nadie la usa
	public String getDefaultCharset(String charset) {
		Query query = new Query(this);
		String defaultCollation = "";
		query.executeQuery("SHOW CHARACTER SET WHERE CHARSET = '" + charset + "';");
		query.next();
		defaultCollation = query.getString("Value");
		query.close();

		return defaultCollation;
	}

	public void setSelectedElement(String db, String element, int type) {
		setSelectedDatabase(db);

		selectedType = type;
		if (type == NONE)
			selectedElement = "";
		else
			selectedElement = element;

	}

	public void setSelectedDatabase(String db) {
		try {
			if (db != null)
				connection.setCatalog(db);
//			else
//				connection.setCatalog("");

		} catch (SQLException e) {
			if (Application.DEBUG)
				e.printStackTrace();
		}

		if (db == null || db.equals(""))
			this.isSelectedDB = false;
		else
			this.isSelectedDB = true;

		this.selectedDb = db;

//		this.selectedTable = "";
		this.selectedElement = "";
		this.selectedType = NONE;
//		this.isSelectedTable = false;
	}

	public String getSelectedDatabase() {
		return selectedDb;
	}

	public String getSelectedElement() {
		return this.selectedElement;
	}

	public int getSelectedType() {
		return this.selectedType;
	}

	public boolean isTableSelected() {
		if (selectedType == TABLE)
			return true;
		return false;
	}

	/**
	 * Verifica si existe una base de datos.
	 * 
	 * @param databaseName Base de datos a verificar
	 * @return -1 si existe, 0 si existe
	 */
	public int existeDatabase(String databaseName) {
		int resu = 0;

		if (databaseName == null || databaseName.length() == 0)
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

		if (tableName == null || tableName.length() == 0)
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

	public static String trimCuote(String value) {
		if (value.startsWith("'") || value.startsWith("\"") || value.startsWith("`")) {
			value = value.substring(1);
		}

		if (value.endsWith("'") || value.endsWith("\"") || value.endsWith("`")) {
			value = value.substring(0, value.length() - 1);
		}

		return value;
	}

	public String getDefaultEngine() {
		String engine = "";
		Query query = new Query(this);
		query.executeQuery(Query.SQL_ENGINE_LIST);
		while (query.next() && engine.length() == 0) {
			if (query.getString("Support").equals("DEFAULT")) {
				engine = query.getString("Engine");
			}
		}

		return engine;
	}

	public String format(String sql) {
		return SqlFormatter.format(sql);
	}

	public String unformat(String sql) {
		String resu = "";
		String[] lineas = sql.split("\n");
		for (int i = 0; i < lineas.length; i++) {
			resu += lineas[i].trim() + " ";
		}

		return resu;
	}
}
