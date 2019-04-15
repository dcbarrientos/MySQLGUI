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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.CellRenderer;
import ar.com.dcbarrientos.mysqlgui.gui.CopyTable;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.EmptyTable;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.TableTab;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;
import ar.com.dcbarrientos.mysqlgui.tools.Tools;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TablesPanel extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("DatabaseTab.title");

	private final int COLUMN_COUNT = 17;
	private final int NAME_COLUMN_INDEX = 0;

	private String[] columnHeaders;
	private Vector<Object[]> datos;

	// private String selectedDB;
 
	private JLabel titleLabel;
	private JToolBar toolBar;
	private JButton jbViewData;
	private JButton jbShowTableProperties;
	private JButton jbCreateTable;
	private JButton jbEditTable;
	private JButton jbEmptyTable;
	private JButton jbDropTable;
	private JButton jbCopyTable;
	private JScrollPane scrollPane;
	private JTable table;
	private TableModel model;

	private CellRenderer cellRenderer;

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

		jbCreateTable = new JButton();
		jbCreateTable.setIcon(new ImageIcon(getClass().getResource("/images/InsertRecord.gif")));
		jbCreateTable.setToolTipText(resource.getString("DatabaseTab.toolbar.createtable"));
		jbCreateTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbInsertRecordMouseClicked(e);
			}
		});
		toolBar.add(jbCreateTable);

		jbEditTable = new JButton();
		jbEditTable.setIcon(new ImageIcon(getClass().getResource("/images/InsertRecord.gif")));
		jbEditTable.setToolTipText(resource.getString("DatabaseTab.toolbar.edittable"));
		jbEditTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jbInsertRecordMouseClicked(e);
			}
		});
		toolBar.add(jbEditTable);

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

		cellRenderer = new CellRenderer();
		table.setDefaultRenderer(Object.class, cellRenderer);
		scrollPane.setViewportView(table);
	}

	protected void jbCopyTableMouseClicked(MouseEvent e) {
		if (isTableSelected()) {

			CopyTable copyTable = new CopyTable(ventana, database, selectedDB, getSelectedTableName());
			if (copyTable.showDialog()) {
				ventana.refresh();
			}
		}
	}

	protected void jbDropTableMouseClicked(MouseEvent e) {
		if (isTableSelected()) {
			String msg = String.format(resource.getString("DropTable.warning"), selectedDB, getSelectedTableName());
			if (JOptionPane.showConfirmDialog(null, msg, resource.getString("DropTable.title"),
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				
				Query query = new Query(database);
				
				String sql = String.format(Query.SQL_DROP_TABLE, selectedDB, getSelectedTableName());
				ventana.addMessage(sql);
				if (query.executeUpdate(sql) < 0) {
					JOptionPane.showMessageDialog(null, query.errorCode + ": " + query.errorMsg,
							resource.getString("DropTable.title"), JOptionPane.ERROR_MESSAGE);
				}else {
					ventana.refresh();
				}
				
				query.close();
			}
		}
	}

	protected void jbEmptyTableMouseClicked(MouseEvent e) {
		if (isTableSelected()) {
			EmptyTable et = new EmptyTable(ventana, database, selectedDB, getSelectedTableName());
			if (et.showDialog())
				ventana.refresh();
		}
	}

	protected void jbInsertRecordMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		ventana.addMessage("Falta hacer");

	}

	protected void jbShowTablePropertiesMouseClicked(MouseEvent e) {
		if (isTableSelected()) {

			String tableName = getSelectedTableName();
			ventana.setSelectedTable(selectedDB, tableName);
			ventana.selectTableTab(TableTab.COLUMNS_INDEX);
		}
	}

	protected void jbViewDataMouseClicked(MouseEvent e) {
		if (isTableSelected()) {

			String tableName = getSelectedTableName();
			ventana.setSelectedTable(selectedDB, tableName);
			ventana.selectTableTab(TableTab.DATA_INDEX);
		}
	}

	private String getSelectedTableName() {
		return (String) table.getValueAt(table.getSelectedRow(), NAME_COLUMN_INDEX);
	}

	protected void loadData() {
		String sql = "SHOW TABLE STATUS FROM `" + selectedDB + "`;";
		Query query = new Query(database);
		query.executeQuery(sql);
		ventana.addMessage(sql);

		Query query2 = new Query(database);
		query2.executeQuery("SHOW FULL TABLES IN " + selectedDB + " WHERE TABLE_TYPE LIKE 'VIEW';");
		Vector<Object[]> viewsList = query2.getDataAsObjectVector();
		query2.close();

		datos = new Vector<Object[]>();

		String[] fila;
		while (query.next()) {
			if (getIndexOf(viewsList, query.getString("Name"), 0) < 0) {
				fila = new String[COLUMN_COUNT];

				for (int i = 0; i < COLUMN_COUNT; i++) {
					if (i > 5 && i < 10)
						fila[i] = Tools.convertUnits(query.getDouble(i + 1));
					else
						fila[i] = query.getString(i + 1);
				}

				datos.add(fila);
			}
		}

		model.setData(datos);
		query.close();
	}

	private boolean isTableSelected() {
		if (table.getSelectedRow() >= 0 && table.getSelectedColumn() >= 0)
			return true;
		return false;
	}

	private int getIndexOf(Vector<Object[]> src, String text, int column) {
		for (int i = 0; i < src.size(); i++) {
			if (src.get(i)[column].equals(text))
				return i;
		}
		return -1;
	}
}
