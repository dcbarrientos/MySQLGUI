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
 * TableTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 6 feb. 2019, 19:00:05 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TableTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("TableTab.title");
	
	public TableTab(Ventana ventana, Database database) {
		super(ventana, database);
		// TODO Auto-generated constructor stub
	}
	
	public void setSelectedTable(String selectedDb, String selectedTable) {
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
