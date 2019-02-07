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

package ar.com.dcbarrientos.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.dcbarrientos.Application;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Query {
	private Database database;
	private Statement st;
	private ResultSet result;
	
	public Query(Database database) {
		this.database = database;
	}
	
	public boolean executeQuery(String sqlTxt) {
		try {
			st = database.getConnection().createStatement();
			result = st.executeQuery(sqlTxt);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int getRowCount() {
		int c = 0;
		if(result != null) {
			try {
				result.last();
				c = result.getRow();
				result.beforeFirst();
			} catch (SQLException e) {
				if(Application.DEBUG)
					e.printStackTrace();
			}
		}
		
		return c;
	}

	public boolean close() {
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
	
	public boolean next() {
		try {
			return result.next();
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();	
		}
		return false;
	}
	
	public String getString(int columnIndex) {
		try {
			return result.getString(columnIndex);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}
		return null;
	}
	
	public String getString(String columnLabel) { 
		try {
			return result.getString(columnLabel);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}
		
		return null;
	}
	
	public int getInt(int columnIndex) {
		try {
			return result.getInt(columnIndex);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getInt(String columnLabel) {
		try {
			return result.getInt(columnLabel);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}
		return 0;
	}
	
	public double getDouble(int columnIndex) {
		try {
			return result.getDouble(columnIndex);
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}
		
		return 0;
	}
}
