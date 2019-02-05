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
 * DatabaseTree.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 1 feb. 2019, 19:58:34 
 */

package ar.com.dcbarrientos.gui;

import java.awt.BorderLayout;

import javax.swing.JTree;

import ar.com.dcbarrientos.db.Database;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class DatabaseTree extends DatabaseElement{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTree tree;
	
	public DatabaseTree(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
		loadData();
	}
	
	void initComponents() {
		tree = new JTree();
		
		setLayout(new BorderLayout());
		add(tree, BorderLayout.CENTER);
	}
	
	private void loadData() {
		
		System.out.println("Connection ID: " + database.getConnectionID());
	}

	@Override
	public void refresh() {
		ventana.refresh();
	}

}
