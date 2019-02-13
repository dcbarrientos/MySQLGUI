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
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class StatisticsTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title;
	public int statisticsCount = 0;
	
	private final int COLUMN_COUNT = 4;

	private JTable statisticsTable;
	private String[] columnHeaders;
	private Vector<String[]> datos;
	
	private TableModel tableModel;

	public StatisticsTab(Ventana ventana, Database database) {
		super(ventana, database);

		title = resource.getString("StatisticsTab.title");
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		statisticsTable = new JTable();
		tableModel = new TableModel();
		loadData();
		statisticsTable.setModel(tableModel);
		
		scrollPane.setViewportView(statisticsTable);
	}

	protected void loadData() {
		columnHeaders = new String[COLUMN_COUNT];
		columnHeaders[0] = resource.getString("StatisticsTab.commandtype");
		columnHeaders[1] = resource.getString("StatisticsTab.total_count");
		columnHeaders[2] = resource.getString("StatisticsTab.average_hour");
		columnHeaders[3] = resource.getString("StatisticsTab.percentage");
		tableModel.setColumnHeaders(columnHeaders);
		
		Query uQuery = new Query(database);
		uQuery.executeQuery("SHOW GLOBAL STATUS LIKE 'Uptime';");
		uQuery.next();
		int uptime = uQuery.getInt(2);
		uQuery.close();

		String sql = "SHOW GLOBAL STATUS LIKE 'Com\\_%';";
		Query query = new Query(database);
		query.executeQuery(sql);
		
		datos = new Vector<String[]>();

		int total = 0;
		String[] fila;
		while (query.next()) {
			fila = new String[COLUMN_COUNT];
			fila[0] = query.getString(1);
			fila[1] = query.getString(2);
			total += Integer.parseInt(fila[1]);
			fila[2] = String.format("%.2f", query.getDouble(2) / (uptime / 3600));
			datos.add(fila);
		}

		for (int j = 0; j < datos.size(); j++) {
			datos.get(j)[3] = String.format("%.2f", Double.parseDouble(datos.get(j)[1]) * 100 / total) + "%";
		}

		datos.sort(new java.util.Comparator<String[]>() {
			@Override
			public int compare(String[] o1, String[] o2) {
				return Integer.compare(Integer.parseInt(o2[1]), Integer.parseInt(o1[1]));
			}
		});

		tableModel.setData(datos);
		statisticsCount = datos.size();
		
		ventana.addMessage(sql + "\n");
		query.close();
	}

	@Override
	public void refresh() {
		loadData();
		statisticsTable.repaint();
	}

	private boolean mShown = false;

	public void addNotify() {
		super.addNotify();

		if (mShown)
			return;

		mShown = true;
	}

}
