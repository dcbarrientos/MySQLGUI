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
 * MainPanel.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 6 feb. 2019, 17:24:10 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.DatabaseTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.HostTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.QueryTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.TableTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.ViewTab;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public final int HOST_INDEX = 0;
	public final int DATABASE_INDEX = 1;
	public final int TABLE_INDEX = 2;

	private Ventana ventana;
	private Database database;

	private Vector<DatabaseElement> tabList;
	private DatabaseTree tree;
	private JTabbedPane tabbedPane;
	private HostTab hostTab;
	private QueryTab queryTab;
	private DatabaseTab databaseTab = null;
	private TableTab tableTab = null;
	private ViewTab viewTab = null;
	private JTextArea txtMessages;

	public MainPanel(Ventana ventana) {
		this.ventana = ventana;

	}

	private void initComponents() {
		setLayout(new BorderLayout());
		tabList = new Vector<DatabaseElement>();

		JSplitPane mainSplit = new JSplitPane();
		mainSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(mainSplit, BorderLayout.CENTER);

		JSplitPane secondarySplit = new JSplitPane();
		mainSplit.setLeftComponent(secondarySplit);

		JScrollPane scrollPane_1 = new JScrollPane();
		mainSplit.setRightComponent(scrollPane_1);

		txtMessages = new JTextArea();
		txtMessages.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPane_1.setViewportView(txtMessages);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		hostTab = new HostTab(ventana, database);
		tabList.add(hostTab);
		tabbedPane.add(hostTab.title, hostTab);

		queryTab = new QueryTab(ventana, database);
		tabList.add(queryTab);
		tabbedPane.add(queryTab.title, queryTab);

		secondarySplit.setRightComponent(tabbedPane);

		JScrollPane scrollPane = new JScrollPane();
		secondarySplit.setLeftComponent(scrollPane);

		tree = new DatabaseTree(ventana, database);
		tabList.add(tree);
		scrollPane.setViewportView(tree);
	}

	public void setConnection(Database database) {
		this.database = database;
		initComponents();
	}

	public void addMessage(String msg) {
		String id = "[" + database.getConnectionID() + "] ";
		txtMessages.append(id + msg + "\n");

	}

	public void selectRoot() {
//		database.setSelectedTable("", "");
		database.setSelectedElement("", "", Database.NONE);
		if (tableTab != null) {
			tabList.remove(tableTab);
			tabbedPane.remove(tableTab);
			tableTab = null;
		}
		if (databaseTab != null) {
			tabList.remove(databaseTab);
			tabbedPane.remove(databaseTab);
			databaseTab = null;
		}

		tabbedPane.repaint();

		tabbedPane.setSelectedIndex(HOST_INDEX);
	}

	public void setSelectedDatabase(String selectedDB, boolean show) {
		// TODO: que hacer cuando selectedDB es null, no hay db seleccionada.
		database.setSelectedDatabase(selectedDB);
		if (databaseTab == null) {
			// TODO: show true debo eliminar tableTab.
			databaseTab = new DatabaseTab(ventana, database);
			tabList.add(databaseTab);
			tabbedPane.insertTab(databaseTab.title + selectedDB, null, databaseTab, null, DATABASE_INDEX);
		} else {
			tabbedPane.setTitleAt(DATABASE_INDEX, databaseTab.title + selectedDB);
			if (tableTab != null && show) {
				tabList.remove(tableTab);
				tabbedPane.remove(tableTab);
				tableTab = null;
				tabbedPane.repaint();
			} else if (viewTab != null && show) {
				tabList.remove(viewTab);
				tabbedPane.remove(viewTab);
				viewTab = null;
				tabbedPane.repaint();
			}
			// Faltan los else if para storeProcedure y function
		}
		databaseTab.setSelectedDatabase(selectedDB);

		if (show)
			tabbedPane.setSelectedIndex(DATABASE_INDEX);

		//TODO: Cuando selecciono una base de datos me anula la selección en el jtree
		tree.setSelectedDatabase(selectedDB);
	}

	public void setSelectedTable(String selectedDB, String selectedTable) {
		// TODO: cuando se hace click en una tabla.
//		database.setSelectedTable(selectedDB, selectedTable);
		database.setSelectedElement(selectedDB, selectedTable, Database.TABLE);

		setSelectedDatabase(selectedDB, false);
		if (viewTab != null) {
			tabList.remove(viewTab);
			tabbedPane.remove(viewTab);
			viewTab = null;
		}
		if (tableTab == null) {
			tableTab = new TableTab(ventana, database, false);
			tabList.add(tableTab);
			tabbedPane.insertTab(tableTab.title + selectedTable, null, tableTab, null, TABLE_INDEX);
		} else {
			tabbedPane.setTitleAt(TABLE_INDEX, tableTab.title + selectedTable);
			tabbedPane.repaint();
			// TODO: faltan los else if de stored procedure y function

		}
		tableTab.setSelectedTable(selectedDB, selectedTable);
		tabbedPane.setSelectedIndex(TABLE_INDEX);

		// tree.setSelectedTable(selectedDB, selectedTable);
	}

	public void setSelectedView(String selectedDB, String selectedView) {
		database.setSelectedElement(selectedDB, selectedView, Database.VIEW);

		setSelectedDatabase(selectedDB, false);
		if (tableTab != null) {
			tabList.remove(tableTab);
			tabbedPane.remove(tableTab);
			tableTab = null;
			// TODO: faltan los else if de stored procedure y function
		}
		if (viewTab == null) {
			viewTab = new ViewTab(ventana, database, false);
			tabList.add(viewTab);
			tabbedPane.insertTab(viewTab.title + selectedView, null, viewTab, null, TABLE_INDEX);
		} else {
			tabbedPane.setTitleAt(TABLE_INDEX, viewTab.title + selectedView);
			tabbedPane.repaint();
		}
		if (selectedView.length() == 0)
			viewTab.setEmptyElement(selectedDB);
		else
			viewTab.setSelectedElement(selectedDB, selectedView, Database.VIEW);
		tabbedPane.setSelectedIndex(TABLE_INDEX);
	}

	public void setTreeSelection(String db, String element, int type, boolean show) {
		tree.setSelection(db, element, type, show);
	}

	public void setTreeSelection(String db, boolean show) {
		tree.setSelection(db, show);
	}

	public void deleteDatabase() {
		if (database.getSelectedDatabase().length() > 0) {
			DeleteDatabase dd = new DeleteDatabase(ventana, database);
			if (dd.deleteDatabase(database.getSelectedDatabase())) {
				// selectDatabase("", false);

				if (tableTab != null) {
					tabList.remove(tableTab);
					tabbedPane.remove(tableTab);
					tableTab = null;
				}

				tabList.remove(databaseTab);
				tabbedPane.remove(databaseTab);
				databaseTab = null;
//				database.setSelectedTable("", "");
				database.setSelectedElement("", "", Database.NONE);
				// databaseTree.selectRoot();

				refresh();
			}
		} else {
			JOptionPane.showMessageDialog(null, ventana.resource.getString("DeleteDatabase.selectDB"),
					ventana.resource.getString("DeleteDatabase.title"), JOptionPane.ERROR_MESSAGE);
		}

	}

	public void refresh() {
		for (DatabaseElement element : tabList)
			element.refresh();
	}

	public void selectTableTab(int index) {
		tableTab.selectTab(index);
	}

	public void showTableData(String databaseName, String tableName) {
		if (tableTab != null) {
			tableTab.selectTab(TableTab.DATA_INDEX);
		}
	}
}
