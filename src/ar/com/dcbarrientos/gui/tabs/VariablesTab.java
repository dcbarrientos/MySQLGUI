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
 * VariablesTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 4 feb. 2019, 11:47:53 
 */

package ar.com.dcbarrientos.gui.tabs;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ar.com.dcbarrientos.Application;
import ar.com.dcbarrientos.db.Database;
import ar.com.dcbarrientos.gui.DatabaseElement;
import ar.com.dcbarrientos.gui.Ventana;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class VariablesTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;
	
	public final String TITLE = "Variables";
	private final int COLUMN_COUNT = 2;
	
	private JTable variablesTable;
	private String[] columnHeader = {"Variables", "Value"};
	private String[][] datos;
	public int variablesCount = 0;
	

	public VariablesTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		loadData();
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		variablesTable = new JTable();
		variablesTable.setModel(getModel());
		scrollPane.setViewportView(variablesTable);		
	}
	
	private TableModel getModel() {
		TableModel model = new AbstractTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getColumnName(int column) {
				return columnHeader[column];
			}
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return datos[rowIndex][columnIndex];
			}
			
			@Override
			public int getRowCount() {
				return variablesCount;
			}
			
			@Override
			public int getColumnCount() {
				return COLUMN_COUNT;
			}
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
		
		return model;
	}
	
	private boolean loadData() {
		boolean r = false;
		
		database.executeQuery("Show variables;");
		variablesCount = database.getQueryCount();
		datos = new String[variablesCount][];
		
		try {
			int i = 0;
			while(database.getQueryResult().next()) {
				datos[i] = new String[2];
				datos[i][0] = database.getQueryResult().getString(1);
				datos[i][1] = database.getQueryResult().getString(2);
				i++;
			}
		} catch (SQLException e) {
			if(Application.DEBUG)
				e.printStackTrace();
		}
		
		database.closeQuery();
		return r;
	}

	@Override
	public void refresh() {
		loadData();
	}
	
 	private boolean mShown = false;
  	
	public void addNotify() 
	{
		super.addNotify();
		
		if (mShown)
			return;

		mShown = true;
	}

}
