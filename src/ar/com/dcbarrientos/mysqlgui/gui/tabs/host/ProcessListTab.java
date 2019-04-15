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

package ar.com.dcbarrientos.mysqlgui.gui.tabs.host;

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
public class ProcessListTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;
	
	
	public String title;
	public int processCount = 0;
	
	private final int COLUMN_COUNT = 8;
	
	private JTable processesTable;
	private String[] columnHeaders;
	private Vector<Object[]> datos;
	private TableModel tableModel;

	public ProcessListTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		title = resource.getString("ProcessList.title");
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		processesTable = new JTable();
		
		tableModel = new TableModel();
		loadData();
		processesTable.setModel(tableModel);
		scrollPane.setViewportView(processesTable);		
	}

	protected void loadData() {
		columnHeaders = new String[COLUMN_COUNT];
		columnHeaders[0] = resource.getString("ProcessList.id"); 
		columnHeaders[1] = resource.getString("ProcessList.user");
		columnHeaders[2] = resource.getString("ProcessList.host");
		columnHeaders[3] = resource.getString("ProcessList.db");
		columnHeaders[4] = resource.getString("ProcessList.command");
		columnHeaders[5] = resource.getString("ProcessList.time");
		columnHeaders[6] = resource.getString("ProcessList.state");
		columnHeaders[7] = resource.getString("ProcessList.info");	
		tableModel.setColumnHeaders(columnHeaders);
		
		String sql = "SHOW PROCESSLIST";
		Query query = new Query(database);
		query.executeQuery(sql);
	
		datos = new Vector<Object[]>();
		
		String[] fila;
		while(query.next()) {
			fila = new String[COLUMN_COUNT];
			for(int j = 0; j < COLUMN_COUNT; j++)
				fila[j] = query.getString(j + 1);

			datos.add(fila);
		}
		
		tableModel.setData(datos);
		processCount = datos.size();
		
		query.close();
		ventana.addMessage(sql);
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
