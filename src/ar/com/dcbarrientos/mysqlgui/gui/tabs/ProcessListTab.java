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
public class ProcessListTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;
	
	
	public String title;
	private final int COLUMN_COUNT = 8;
	
	private JTable processesTable;
	private String[] columnHeader = new String[COLUMN_COUNT];

	private String[][] datos;
	public int processCount = 0;
	

	public ProcessListTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		title = resource.getString("ProcessList.title");
		loadData();
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		processesTable = new JTable();
		processesTable.setModel(getModel());
		scrollPane.setViewportView(processesTable);		
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
				return processCount;
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

		columnHeader[0] = resource.getString("ProcessList.id"); 
		columnHeader[1] = resource.getString("ProcessList.user");
		columnHeader[2] = resource.getString("ProcessList.host");
		columnHeader[3] = resource.getString("ProcessList.db");
		columnHeader[4] = resource.getString("ProcessList.command");
		columnHeader[5] = resource.getString("ProcessList.time");
		columnHeader[6] = resource.getString("ProcessList.state");
		columnHeader[7] = resource.getString("ProcessList.info");	
		
		String sql = "SHOW PROCESSLIST";
		Query query = new Query(database);
		query.executeQuery(sql);
		processCount = query.getRowCount();
	
		datos = new String[processCount][];
		
		int i = 0;
		while(query.next()) {
			datos[i] = new String[COLUMN_COUNT];
			for(int j = 0; j < COLUMN_COUNT; j++)
				datos[i][j] = query.getString(j + 1);

			i++;
		}
		
		query.close();
		ventana.addMessage(sql + "\n");
		return r;
	}

	@Override
	public void refresh() {
		loadData();
		processesTable.repaint();
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
