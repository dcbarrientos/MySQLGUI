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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ar.com.dcbarrientos.db.Database;
import ar.com.dcbarrientos.db.Query;
import ar.com.dcbarrientos.gui.DatabaseElement;
import ar.com.dcbarrientos.gui.Ventana;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class StatisticsTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;
	

	public String title;
	private final int COLUMN_COUNT = 4;
	
	private JTable statisticsTable;
	private String[] columnHeader = new String[COLUMN_COUNT];
	private String[][] datos;
	public int statisticsCount = 0;
	

	public StatisticsTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		title = resource.getString("StatisticsTab.title");
		loadData();
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		statisticsTable = new JTable();
		statisticsTable.setModel(getModel());
		scrollPane.setViewportView(statisticsTable);		
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
				return statisticsCount;
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
		columnHeader[0] = resource.getString("StatisticsTab.commandtype");
		columnHeader[1] = resource.getString("StatisticsTab.total_count");		
		columnHeader[2] = resource.getString("StatisticsTab.average_hour");		
		columnHeader[3] = resource.getString("StatisticsTab.percentage");		
		
		Query uQuery = new Query(database);
		uQuery.executeQuery("SHOW GLOBAL STATUS LIKE 'Uptime';");
		uQuery.next();
		int uptime = uQuery.getInt(2);
		uQuery.close();
		
		String sql = "SHOW GLOBAL STATUS LIKE 'Com\\_%';";
		Query query = new Query(database);
		query.executeQuery(sql);
		statisticsCount = query.getRowCount();
		datos = new String[statisticsCount][];
		
		int i = 0;
		int total = 0;
		while(query.next()) {
			datos[i] = new String[COLUMN_COUNT];
			datos[i][0] = query.getString(1);
			datos[i][1] = query.getString(2);
			total += Integer.parseInt(datos[i][1]);
			datos[i][2] = String.format("%.2f", query.getDouble(2)/(uptime / 3600));
			i++;
		}
		
		for(int j = 0; j < datos.length; j++) {
			datos[j][3] = String.format("%.2f", Double.parseDouble(datos[j][1]) * 100 / total) + "%";
		}
		
		java.util.Arrays.sort(datos, new java.util.Comparator<String[]>() {
			@Override
			public int compare(String[] o1, String[] o2) {
				return Integer.compare(Integer.parseInt(o2[1]), Integer.parseInt(o1[1]));
			}
		});
		
		ventana.addMessage(sql + "\n");
		query.close();
		return r;
	}

	@Override
	public void refresh() {
		loadData();
		statisticsTable.repaint();
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
