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
 * ProceduresTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 14 feb. 2019, 11:53:35 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs.databasetab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.CellRenderer;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class ProceduresTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("ProceduresTab.title");

	private final int COLUMN_COUNT = 10;

	private JLabel titleLabel;
	private JScrollPane scrollPanel;
	private JTable table;
	private TableModel tableModel;

	private String[] columnHeaders;
	private Vector<Object[]> datos;

	private CellRenderer cellRenderer;
	
	public ProceduresTab(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		columnHeaders = new String[COLUMN_COUNT];
		for (int i = 0; i < COLUMN_COUNT; i++)
			columnHeaders[i] = resource.getString("ProceduresTab.column" + (i + 1));

		titleLabel = new JLabel("New label");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBackground(Color.BLACK);
		titleLabel.setOpaque(true);
		add(titleLabel, BorderLayout.NORTH);

		scrollPanel = new JScrollPane();
		add(scrollPanel, BorderLayout.CENTER);

		table = new JTable();
		tableModel = new TableModel();
		tableModel.setColumnHeaders(columnHeaders);
		table.setModel(tableModel);
		
		cellRenderer = new CellRenderer();
		table.setDefaultRenderer(Object.class, cellRenderer);

		scrollPanel.setViewportView(table);
	}

	protected void loadData() {
		Query query = new Query(database);
		String sql = "SHOW PROCEDURE STATUS WHERE `Db`='" + selectedDB + "';";
		query.executeQuery(sql);
		ventana.addMessage(sql);

		datos = query.getDataAsObjectVector();

		tableModel.setData(datos);

		query.close();
	}
}
