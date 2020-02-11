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
 * CreateTable.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 23 ago. 2019, 16:45:59 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JDialog;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.TableTab;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class CreateTableDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private Ventana ventana;
	private Database database;
	private ResourceBundle resource;
	private TableTab tableTab;
	
	private String dbName;
		
	public CreateTableDialog() {
		
	}
	
	public CreateTableDialog(Ventana ventana, Database database, String dbName) {
		this.ventana = ventana;
		this.database = database;
		this.resource = ventana.resource;
		this.dbName = dbName;
		
		initComponents();
	}
	
	public void initComponents() {
		setTitle(resource.getString("CreateTableDialog.title"));
		setLayout(new BorderLayout());
		
		tableTab = new TableTab(ventana, database, true);
		tableTab.setSelectedDatabase(dbName);
		tableTab.parent = this;
		add(tableTab, BorderLayout.CENTER);
		
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
	}
	
	public void showDialog() {
		setVisible(true);
	}
	
	public void close() {
		dispose();
	}
}
