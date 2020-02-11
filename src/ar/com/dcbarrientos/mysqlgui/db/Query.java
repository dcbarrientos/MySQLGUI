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
import java.sql.SQLTimeoutException;
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

	public static final String SQL_SHOW_TABLES_FROM = "SHOW FULL TABLES FROM `%s`;";
	public static final String SQL_COPY_STRUCTURE = "CREATE TABLE %s.%s LIKE %s.%s;";
	public static final String SQL_COPY_DATA = "INSERT %s.%s SELECT * FROM %s.%s;";
	public static final String SQL_SHOW_DATABASES = "SHOW DATABASES;";
	public static final String SQL_TRUNCATE_TABLE = "TRUNCATE TABLE %s.%s;";
	public static final String SQL_DELETE_TABLE = "DELETE FROM %s.%s;";
	public static final String SQL_DROP_TABLE = "DROP TABLE %s.%s;";
	public static final String SQL_DROP_VIEW = "DROP VIEW %s.%s;";
	public static final String SQL_COLLATION_LIST = "SELECT * FROM `information_schema`.`COLLATIONS`;";
	public static final String SQL_COLLATION_LIST_PARAM = "SELECT * FROM `information_schema`.`COLLATIONS` WHERE COLLATION_NAME LIKE  '%s' ESCAPE '\\\\';";
	public static final String SQL_ENGINE_LIST = "SHOW ENGINES;";
	public static final String SQL_CHARSET_LIST = "SHOW CHARACTER SET;";
	public static final String SQL_SHOW_CREATE_TABLE = "SHOW CREATE TABLE %s.%s;";

	public static int ERROR = -1;

	public Query(Database database) {
		this.database = database;
	}

	/**
	 * Executes the given SQL statement, which may return multiple results. In some
	 * (uncommon) situations, a single SQL statement may return multiple result sets
	 * and/or update counts. Normally you can ignore this unless you are (1)
	 * executing a stored procedure that you know may return multiple results or (2)
	 * you are dynamically executing an unknown SQL string.
	 * <P>
	 * The <code>execute</code> method executes an SQL statement and indicates the
	 * form of the first result. You must then use the methods
	 * <code>getResultSet</code> or <code>getUpdateCount</code> to retrieve the
	 * result, and <code>getMoreResults</code> to move to any subsequent result(s).
	 * <p>
	 * <strong>Note:</strong>This method cannot be called on a
	 * <code>PreparedStatement</code> or <code>CallableStatement</code>.
	 * 
	 * @param sql any SQL statement
	 * @return <code>true</code> if the first result is a <code>ResultSet</code>
	 *         object; <code>false</code> if it is an update count or there are no
	 *         results
	 * @exception SQLException if a database access error occurs, this method is
	 *                         called on a closed <code>Statement</code>, the method
	 *                         is called on a <code>PreparedStatement</code> or
	 *                         <code>CallableStatement</code>
	 * @throws SQLTimeoutException when the driver has determined that the timeout
	 *                             value that was specified by the
	 *                             {@code setQueryTimeout} method has been exceeded
	 *                             and has at least attempted to cancel the
	 *                             currently running {@code Statement}
	 * @see #getResultSet
	 * @see #getUpdateCount
	 * @see #getMoreResults
	 */
	public boolean execute(String sql) {
		boolean res = false;
		st = database.createStatement();
		try {
			res = st.execute(sql);
			if (res) {
				result = st.getResultSet();
			}
		} catch (SQLException e) {
			res = false;
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return res;
	}

	/**
	 * Execute a query
	 * 
	 * @param sqlTxt Query to execute
	 * @return true if query executed successfuly, false otherwise.
	 */
	public boolean executeQuery(String sqlTxt) {
		try {
			st = database.createStatement();

			result = st.executeQuery(sqlTxt);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG) {
				System.out.println(sqlTxt);
				e.printStackTrace();
			}
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
		int res = ERROR;

		try {
			st = database.createStatement();
			res = st.executeUpdate(sql);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return res;
	}

	/**
	 * Returns the number of rows in this <code>ResultSet</code> object.
	 *
	 * @return the number of rows
	 * @exception SQLException if a database access error occurs
	 */
	public int getRowCount() {
		int c = 0;
		if (result != null) {
			try {
				result.last();
				c = result.getRow();
				result.beforeFirst();
			} catch (SQLException e) {
				setError(e);

				if (Application.DEBUG)
					e.printStackTrace();
			}
		}

		return c;
	}

	/**
	 * Releases this <code>ResultSet</code> object's database and JDBC resources
	 * immediately instead of waiting for this to happen when it is automatically
	 * closed.
	 *
	 * <P>
	 * The closing of a <code>ResultSet</code> object does <strong>not</strong>
	 * close the <code>Blob</code>, <code>Clob</code> or <code>NClob</code> objects
	 * created by the <code>ResultSet</code>. <code>Blob</code>, <code>Clob</code>
	 * or <code>NClob</code> objects remain valid for at least the duration of the
	 * transaction in which they are created, unless their <code>free</code> method
	 * is invoked.
	 * <p>
	 * When a <code>ResultSet</code> is closed, any <code>ResultSetMetaData</code>
	 * instances that were created by calling the <code>getMetaData</code> method
	 * remain accessible.
	 *
	 * <P>
	 * <B>Note:</B> A <code>ResultSet</code> object is automatically closed by the
	 * <code>Statement</code> object that generated it when that
	 * <code>Statement</code> object is closed, re-executed, or is used to retrieve
	 * the next result from a sequence of multiple results.
	 * <p>
	 * Calling the method <code>close</code> on a <code>ResultSet</code> object that
	 * is already closed is a no-op.
	 *
	 *
	 * @exception SQLException if a database access error occurs
	 */
	public boolean close() {
		try {
			if (result != null)
				result.close();
			if (st != null)
				st.close();
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Moves the cursor forward one row from its current position. A
	 * <code>ResultSet</code> cursor is initially positioned before the first row;
	 * the first call to the method <code>next</code> makes the first row the
	 * current row; the second call makes the second row the current row, and so on.
	 * <p>
	 * When a call to the <code>next</code> method returns <code>false</code>, the
	 * cursor is positioned after the last row. Any invocation of a
	 * <code>ResultSet</code> method which requires a current row will result in a
	 * <code>SQLException</code> being thrown. If the result set type is
	 * <code>TYPE_FORWARD_ONLY</code>, it is vendor specified whether their JDBC
	 * driver implementation will return <code>false</code> or throw an
	 * <code>SQLException</code> on a subsequent call to <code>next</code>.
	 *
	 * <P>
	 * If an input stream is open for the current row, a call to the method
	 * <code>next</code> will implicitly close it. A <code>ResultSet</code> object's
	 * warning chain is cleared when a new row is read.
	 *
	 * @return <code>true</code> if the new current row is valid; <code>false</code>
	 *         if there are no more rows
	 * @exception SQLException if a database access error occurs or this method is
	 *                         called on a closed result set
	 */
	public boolean next() {
		try {
			return result.next();
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}
		return false;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>String</code> in the Java
	 * programming language.
	 *
	 * @param columnIndex the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the value
	 *         returned is <code>null</code>
	 * @exception SQLException if the columnIndex is not valid; if a database access
	 *                         error occurs or this method is called on a closed
	 *                         result set
	 */
	public String getString(int columnIndex) {
		try {
			return result.getString(columnIndex);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>String</code> in the Java
	 * programming language.
	 *
	 * @param columnLabel the label for the column specified with the SQL AS clause.
	 *                    If the SQL AS clause was not specified, then the label is
	 *                    the name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the value
	 *         returned is <code>null</code>
	 * @exception SQLException if the columnLabel is not valid; if a database access
	 *                         error occurs or this method is called on a closed
	 *                         result set
	 */
	public String getString(String columnLabel) {
		try {
			return result.getString(columnLabel);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>int</code> in the Java programming
	 * language.
	 *
	 * @param columnIndex the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the value
	 *         returned is <code>0</code>
	 * @exception SQLException if the columnIndex is not valid; if a database access
	 *                         error occurs or this method is called on a closed
	 *                         result set
	 */
	public int getInt(int columnIndex) {
		try {
			return result.getInt(columnIndex);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>int</code> in the Java programming
	 * language.
	 *
	 * @param columnLabel the label for the column specified with the SQL AS clause.
	 *                    If the SQL AS clause was not specified, then the label is
	 *                    the name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the value
	 *         returned is <code>0</code>
	 * @exception SQLException if the columnLabel is not valid; if a database access
	 *                         error occurs or this method is called on a closed
	 *                         result set
	 */
	public int getInt(String columnLabel) {
		try {
			return result.getInt(columnLabel);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>double</code> in the Java
	 * programming language.
	 *
	 * @param columnIndex the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the value
	 *         returned is <code>0</code>
	 * @exception SQLException if the columnIndex is not valid; if a database access
	 *                         error occurs or this method is called on a closed
	 *                         result set
	 */
	public double getDouble(int columnIndex) {
		try {
			return result.getDouble(columnIndex);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Returns the number of columns in this <code>ResultSet</code> object.
	 *
	 * @return the number of columns
	 * @exception SQLException if a database access error occurs
	 */
	public int getColumnCount() {
		try {
			return result.getMetaData().getColumnCount();
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Retrieves the values of this <code>ResultSet</code> as a <code>Vector</code>
	 * of <code>Object</code>.
	 *
	 * @return the values of the <code>ResultSet</code>; if the
	 *         <code>ResultSet</code> is empty returns null.
	 */
	public Vector<Object[]> getDataAsObjectVector() {
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
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
			return null;
		}

		return datos;
	}

	/**
	 * Returns the current <code>ResultSet</code>
	 *
	 * @return the current <code>ResultSet</code>
	 */
	public ResultSet getResultSet() {
		return result;
	}

	/**
	 * Get the column names of the current
	 * <code>ResultSet</cod> as a <code>String</code> vector.
	 *
	 * @return column names, null if the <code>ResultSet</code> is empty.
	 */
	public String[] getColumnNames() {
		String[] columnsName = null;

		try {
			if (result.getMetaData().getColumnCount() > 0) {
				columnsName = new String[result.getMetaData().getColumnCount()];
				for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
					columnsName[i] = result.getMetaData().getColumnName(i + 1);
				}
			}
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
		}

		return columnsName;
	}

	/**
	 * Retrieves the current result as an update count; if the result is a
	 * <code>ResultSet</code> object or there are no more results, -1 is returned.
	 * This method should be called only once per result.
	 *
	 * @return the current result as an update count; -1 if the current result is a
	 *         <code>ResultSet</code> object or there are no more results
	 */
	public int getUpdateCount() {
		int r = -2;

		if (st != null) {
			try {
				r = st.getUpdateCount();
			} catch (SQLException e) {
				setError(e);

				if (Application.DEBUG)
					e.printStackTrace();
			}
		}

		return r;
	}

	/**
	 * Get the error message of the current query.
	 * 
	 * @return error message.
	 */
	public String getErrorMessage() {
		return errorCode + ": " + errorMsg;
	}

	public boolean setCatalog(String catalogName) {
		try {
			database.getConnection().setCatalog(catalogName);
		} catch (SQLException e) {
			setError(e);

			if (Application.DEBUG)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Set the error variablesa, errorMsg and errorCode, of the current query.
	 * 
	 * @param e the SQLException wich catched the error.
	 */
	private void setError(SQLException e) {
		errorMsg = e.getMessage();
		errorCode = e.getErrorCode();
	}
}
