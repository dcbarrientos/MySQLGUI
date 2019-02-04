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
 * DatabaseElement.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 1 feb. 2019, 21:46:49 
 */

package ar.com.dcbarrientos.gui;

import javax.swing.JPanel;

import ar.com.dcbarrientos.db.Database;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public abstract class DatabaseElement extends JPanel{
	private static final long serialVersionUID = 1L;
	
	protected Database database;
	protected Ventana ventana;
	
	public DatabaseElement(Ventana ventana, Database database) {
		this.ventana = ventana;
		this.database = database;
	}
	
	public abstract void refresh();
}
