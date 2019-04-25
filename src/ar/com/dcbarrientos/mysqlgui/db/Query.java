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
 * Query.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 5 feb. 2019, 19:21:33 
 */

package ar.com.dcbarrientos.mysqlgui.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import ar.com.dcbarrientos.mysqlgui.Application;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Query {
	private Database database;
	private Statement st;
	private ResultSet result;
	
	public String errorMsg;
	public int errorCode;

	public static final String SQL_SHOW_TABLES_FROM = "SHOW TABLES FROM `%s`;";
	public static final String SQL_COPY_STRUCTURE = "CREATE TABLE %s.%s LIKE %s.%s;";
	public static final String SQL_COPY_DATA = "INSERT %s.%s SELECT * FROM %s.%s;";
	public static final String SQL_SHOW_DATABASES = "SHOW DATABASES;";
	public static final String SQL_TRUNCATE_TABLE = "TRUNCATE TABLE %s.%s;";
	public static final String SQL_DELETE_TABLE = "DELETE FROM %s.%s;";
	public static final String SQL_DROP_TABLE = "DROP TABLE %s.%s;";
	public static final String SQL_COLLATION_LIST = "SELECT * FROM `information_schema`.`COLLATIONS`;";
	public static final String SQL_COLLATION_LIST_PARAM = "SELECT * FROM `information_schema`.`COLLATIONS` WHERE COLLATION_NAME LIKE  '%s' ESCAPE '\\\\';";
	public static final String SQL_ENGINE_LIST = "SHOW ENGINES;";
	public static final String SQL_CHARSET_LIST = "SHOW CHARACTER SET;";
	public static final String SQL_SHOW_CREATE_TABLE = "SHOW CREATE TABLE %s.%s;";
	
	public Query(Database database) {
		this.database = database;
	}

	/**
	 * Execute a query
	 * @param sqlTxt Query to execute
	 * @return true if query executed successfuly, false otherwise.
	 */
	public boolean executeQuery(String sqlTxt) {
		try {
			st = database.getConnection().createStatement();
			result = st.executeQuery(sqlTxt);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Ejecuta una consulta en la base de datos
	 * 
	 * @param sql Consulta a
	 * @return (1) the row count for SQL Data Manipulation Language (DML) statements
	 *         (2) 0 for SQL statements that return nothing (3) -1 cuando ocurre un
	 *         error, su description en errorMsg y errorCode
	 */
	public int executeUpdate(String sql) {
		int res = -1;

		try {
			st = database.getConnection().createStatement();
			res = st.executeUpdate(sql);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return res;
	}

	public int getRowCount() {
		int c = 0;
		if (result != null) {
			try {
				result.last();
				c = result.getRow();
				result.beforeFirst();
			} catch (SQLException e) {
				errorMsg = e.getMessage();
				errorCode = e.getErrorCode();

				if (Application.DEBUG)
					e.printStackTrace();
			}
		}

		return c;
	}

	public boolean close() {
		try {
			if (result != null)
				result.close();
			if (st != null)
				st.close();
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean next() {
		try {
			return result.next();
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}
		return false;
	}

	public String getString(int columnIndex) {
		try {
			return result.getString(columnIndex);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}
		return null;
	}

	public String getString(String columnLabel) {
		try {
			return result.getString(columnLabel);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return null;
	}

	public int getInt(int columnIndex) {
		try {
			return result.getInt(columnIndex);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return 0;
	}

	public int getInt(String columnLabel) {
		try {
			return result.getInt(columnLabel);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}
		return 0;
	}

	public double getDouble(int columnIndex) {
		try {
			return result.getDouble(columnIndex);
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return 0;
	}

	public int getColumnCount() {
		try {
			return result.getMetaData().getColumnCount();
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return -1;
	}

	public Vector<Object[]> getDataAsObjectVector(){
		int cc = getColumnCount();
		Vector<Object[]> datos = new Vector<Object[]>();

		try {
			result.beforeFirst();
			while (result.next()) {
				String[] fila = new String[cc];

				for (int i = 0; i < cc; i++)
					fila[i] = result.getString(i + 1);

				datos.add(fila);
			}

			result.beforeFirst();
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
			return null;
		}

		return datos;
	}

	public Vector<String[]> getDataAsStringVector() {
		int cc = getColumnCount();
		Vector<String[]> datos = new Vector<String[]>();

		try {
			result.beforeFirst();
			while (result.next()) {
				String[] fila = new String[cc];

				for (int i = 0; i < cc; i++)
					fila[i] = result.getString(i + 1);

				datos.add(fila);
			}

			result.beforeFirst();
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
			return null;
		}

		return datos;
	}

	public Vector<Object[]> getDataAsStringVector(String[] columnNames) {
		int cc = columnNames.length;
		Vector<Object[]> datos = new Vector<Object[]>();
		String[] fila;
		try {
			result.beforeFirst();
			while (result.next()) {
				fila = new String[cc];

				for (int i = 0; i < cc; i++)
					fila[i] = result.getString(columnNames[i]);

				datos.add(fila);
			}

			result.beforeFirst();
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			errorCode = e.getErrorCode();

			if (Application.DEBUG)
				e.printStackTrace();
			return null;
		}

		return datos;
	}

	public String[] getColumnsName() {
		String[] columnsName = null;

		try {
			if (result.getMetaData().getColumnCount() > 0) {
				columnsName = new String[result.getMetaData().getColumnCount()];
				for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
					columnsName[i] = result.getMetaData().getColumnName(i + 1);
				}
			}
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}

		return columnsName;
	}

}
