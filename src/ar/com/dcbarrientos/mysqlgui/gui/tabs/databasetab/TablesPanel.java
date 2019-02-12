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
 * DatabasePanel.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 8 feb. 2019, 10:37:14 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs.databasetab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TablesPanel extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("DatabaseTab.title");

	private final int COLUMN_COUNT = 8;
	private String[] columnHeaders;
	private Vector<String[]> datos;

	private String selectedDB;

	private JLabel titleLabel;
	private JToolBar toolBar;
	private JButton jbViewData;
	private JButton jbShowTableProperties;
	private JButton jbInsertRecord;
	private JButton jbEmptyTable;
	private JButton jbDropTable;
	private JButton jbCopyTable;
	private JScrollPane scrollPane;
	private JTable table;
	private TableModel model;

	public TablesPanel(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
	}

	private void initComponents() {
		columnHeaders = new String[COLUMN_COUNT];
		for (int i = 0; i < COLUMN_COUNT; i++)
			columnHeaders[i] = resource.getString("DatabaseTab.tabla.header" + (i + 1));

		setLayout(new BorderLayout(0, 0));

		titleLabel = new JLabel("New label");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBackground(Color.BLACK);
		titleLabel.setOpaque(true);
		add(titleLabel, BorderLayout.NORTH);

		toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		add(toolBar, BorderLayout.WEST);

		jbViewData = new JButton();
		jbViewData.setToolTipText(resource.getString("DatabaseTab.toolbar.viewdata"));
		jbViewData.setIcon(new ImageIcon(getClass().getResource("/images/ViewData.gif")));
		jbViewData.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbViewDataMouseClicked(e);
			}
		});
		toolBar.add(jbViewData);

		jbShowTableProperties = new JButton();
		jbShowTableProperties.setIcon(new ImageIcon(getClass().getResource("/images/TableProperties.gif")));
		jbShowTableProperties.setToolTipText(resource.getString("DatabaseTab.toolbar.showproperties"));
		jbShowTableProperties.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbShowTablePropertiesMouseClicked(e);
			}
		});
		toolBar.add(jbShowTableProperties);

		jbInsertRecord = new JButton();
		jbInsertRecord.setIcon(new ImageIcon(getClass().getResource("/images/InsertRecord.gif")));
		jbInsertRecord.setToolTipText(resource.getString("DatabaseTab.toolbar.insertrecord"));
		jbInsertRecord.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbInsertRecordMouseClicked(e);
			}
		});
		toolBar.add(jbInsertRecord);

		jbEmptyTable = new JButton();
		jbEmptyTable.setIcon(new ImageIcon(getClass().getResource("/images/Emptytable.gif")));
		jbEmptyTable.setToolTipText(resource.getString("DatabaseTab.toolbar.emptytable"));
		jbEmptyTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbEmptyTableMouseClicked(e);
			}
		});
		toolBar.add(jbEmptyTable);

		jbDropTable = new JButton();
		jbDropTable.setIcon(new ImageIcon(getClass().getResource("/images/DropTable.gif")));
		jbDropTable.setToolTipText(resource.getString("DatabaseTab.toolbar.droptable"));
		jbDropTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbDropTableMouseClicked(e);
			}
		});
		toolBar.add(jbDropTable);

		jbCopyTable = new JButton();
		jbCopyTable.setIcon(new ImageIcon(getClass().getResource("/images/Copytable.gif")));
		jbCopyTable.setToolTipText(resource.getString("DatabaseTab.toolbar.copytable"));
		jbCopyTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbCopyTableMouseClicked(e);
			}
		});
		toolBar.add(jbCopyTable);

		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		model = new TableModel();
		model.setColumnHeaders(columnHeaders);
		table.setModel(model);
		scrollPane.setViewportView(table);
	}

	protected void jbCopyTableMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	protected void jbDropTableMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	protected void jbEmptyTableMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	protected void jbInsertRecordMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	protected void jbShowTablePropertiesMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	protected void jbViewDataMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void loadData() {

		// TODO: hacer un procedimiento por consulta, las columnas tienen nombres
		// distintos.

		ejecutarConsulta("SHOW TABLE STATUS FROM `" + selectedDB + "`", resource.getString("DatabaseTab.tipo.Tabla"));
		// ejecutarConsulta("SHOW FUNCTION STATUS WHERE `Db`= '"+ selectedDB + "'",
		// resource.getString("DatabaseTab.tipo.Function"));
		// ejecutarConsulta("SHOW PROCEDURE STATUS WHERE `Db`='"+ selectedDB + "'",
		// resource.getString("DatabaseTab.tipo.Procedure"));
		// ejecutarConsulta("SHOW TRIGGERS FROM `"+ selectedDB + "`;",
		// resource.getString("DatabaseTab.tipo.Trigger"));
	}

	private void ejecutarConsulta(String sql, String type) {
		Query query = new Query(database);
		query.executeQuery(sql);
		ventana.addMessage(sql + ";\n");

		datos = new Vector<String[]>();

		String[] fila;
		while (query.next()) {
			fila = new String[COLUMN_COUNT];
			fila[0] = query.getString("Name");
			fila[1] = query.getString("Rows");
			fila[2] = Integer.toString(query.getInt("Data_length") + query.getInt("Index_length"));
			fila[3] = query.getString("Create_time");
			fila[4] = query.getString("Update_time");
			fila[5] = query.getString("Engine");
			fila[6] = query.getString("Comment");
			if (type.equals(resource.getString("DatabaseTab.tipo.Tabla")) && fila[6].equals("VIEW")) {
				fila[7] = resource.getString("DatabaseTab.tipo.View");
			} else {
				fila[7] = type;
			}
			datos.add(fila);
		}

		model.setData(datos);
		query.close();

	}

	public void setSelectedDatabase(String databaseName) {
		this.selectedDB = databaseName;
		refresh();
	}

	@Override
	public void refresh() {
		loadData();

		// model.fireTableDataChanged();
		revalidate();
		repaint();
	}
}
