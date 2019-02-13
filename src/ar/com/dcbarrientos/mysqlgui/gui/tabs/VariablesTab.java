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

package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class VariablesTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;
	
	public String title;
	private final int COLUMN_COUNT = 3;
	
	private JTable variablesTable;
	private String[] columnHeader = new String[COLUMN_COUNT];
	LinkedHashMap <String, String[]> datos;
	public int variablesCount = 0;
	

	public VariablesTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		title = resource.getString("Variables.title");
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
			private static final long serialVersionUID = 1L;

			@Override
			public String getColumnName(int column) {
				return columnHeader[column];
			}
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				String valor;
				if(columnIndex == 0)
					valor = new ArrayList<String>(datos.keySet()).get(rowIndex);
				else
					valor = new ArrayList<String[]>(datos.values()).get(rowIndex)[columnIndex -1];
				return valor;
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
			
			@Override
			public void fireTableDataChanged() {
				super.fireTableDataChanged();
			}
		};
		
		return model;
	}
	
	protected void loadData() {
		
		columnHeader[0] = resource.getString("Variables.variable");
		columnHeader[1] = resource.getString("Variables.session");
		columnHeader[2] = resource.getString("Variables.global");
		
		String sql1 = "SHOW VARIABLES;";
		Query query = new Query(database);
		query.executeQuery(sql1);
		variablesCount = query.getRowCount();
		
		String sql2 = "SHOW GLOBAL VARIABLES";
		Query query2 = new Query(database);
		query2.executeQuery(sql2);
		
		if(variablesCount < query2.getRowCount())
			variablesCount = query2.getRowCount();
		
		datos =  new LinkedHashMap <String, String[]>();
		
		String[] valores;
		while(query.next()) {
			valores = new String[COLUMN_COUNT - 1];
			valores[0] = query.getString(2);
			datos.put(query.getString(1), valores);
		}
		
		while(query2.next()) {
			valores = new String[COLUMN_COUNT - 1];
			if(datos.get(query2.getString(1)) == null) {
				valores[1] = query2.getString(2);
				datos.put(query2.getString(1), valores);
			}else {
				valores[0] = datos.get(query2.getString(1))[0];
				valores[1] = query2.getString(2);
				datos.replace(query2.getString(1), valores);
			}
		}
		
		ventana.addMessage(sql1 + "\n");
		ventana.addMessage(sql2 + "\n");
		query.close();
		query2.close();
	}

	@Override
	public void refresh() {
		loadData();
		variablesTable.repaint();
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
