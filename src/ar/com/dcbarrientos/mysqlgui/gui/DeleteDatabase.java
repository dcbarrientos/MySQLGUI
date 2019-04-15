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
 * DeleteDatabase.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 24 feb. 2019, 11:47:23 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class DeleteDatabase {
	private ResourceBundle resource;
	private Database database;

	public DeleteDatabase(Ventana ventana, Database database) {
		this.resource = ventana.resource;
		this.database = database;
	}

	public boolean deleteDatabase(String databaseName) {

		String msg = resource.getString("DeleteDatabase.verification") + databaseName + "\"?";
		String title = resource.getString("DeleteDatabase.title");
		if (JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) 
		{
			Query query = new Query(database);
			int r = query.executeUpdate("DROP DATABASE `" + databaseName + "`");
			if(r < 0) {
				JOptionPane.showMessageDialog(null, query.errorCode + ": " + query.errorMsg, title, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			query.close();
			
			return true;
		}
		
		return false;
	}
}
