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
public class StatusTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title;
	private final int COLUMN_COUNT = 2;

	private JTable statusTable;
	private String[] columnHeaders;
	private Vector<String[]> datos;
	private TableModel tableModel;
	public int statusCount = 0;

	public StatusTab(Ventana ventana, Database database) {
		super(ventana, database);

		title = resource.getString("StatusTab.title");
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		statusTable = new JTable();
		tableModel = new TableModel();
		loadData();
		statusTable.setModel(tableModel);
		
		scrollPane.setViewportView(statusTable);
	}

	private boolean loadData() {
		boolean r = false;
		columnHeaders = new String[COLUMN_COUNT];
		columnHeaders[0] = resource.getString("StatusTab.variable");
		columnHeaders[1] = resource.getString("StatusTab.value");
		tableModel.setColumnHeaders(columnHeaders);

		String sql = "SHOW GLOBAL STATUS;";
		Query query = new Query(database);
		query.executeQuery(sql);
		
		datos = new Vector<String[]>();
		String[] fila;
		
		while (query.next()) {
			fila = new String[COLUMN_COUNT];
			fila[0] = query.getString(1);
			fila[1] = query.getString(2);
			datos.add(fila);
		}

		statusCount = datos.size();
		tableModel.setData(datos);

		ventana.addMessage(sql + "\n");
		query.close();
		return r;
	}

	@Override
	public void refresh() {
		loadData();
		statusTable.repaint();
	}

	private boolean mShown = false;

	public void addNotify() {
		super.addNotify();

		if (mShown)
			return;

		mShown = true;
	}

}
