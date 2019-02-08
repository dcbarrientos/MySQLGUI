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

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class MainPanel extends JPanel{
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
		scrollPane.setViewportView(tree);		
	}
	
	public void setConnection(Database database) {
		this.database = database;
		initComponents();
	}
	
	public void addMessage(String msg) {
		String id = "[" + database.getConnectionID() + "] ";
		txtMessages.append(id + msg);
		
	}
	
	public void selectDatabase(String selectedDB, boolean show) {
		//TODO: cuando se hace click en una base de datos en tree.
		
		database.setSelectedDatabase(selectedDB);
		if(databaseTab == null) {
			//TODO: show true debo eliminar tableTab.
			databaseTab = new DatabaseTab(ventana, database);
			tabList.add(databaseTab);
			tabbedPane.insertTab(databaseTab.title + selectedDB, null, databaseTab, null, DATABASE_INDEX);
		} else {
			tabbedPane.setTitleAt(DATABASE_INDEX, databaseTab.title + selectedDB);
			if(tableTab!=null && show) {
				tabList.remove(tableTab);
				tabbedPane.remove(tableTab);
				tableTab = null;
				tabbedPane.repaint();
			}
		}
		databaseTab.setSelectedDatabase(selectedDB);
		
		if(show)
			tabbedPane.setSelectedIndex(DATABASE_INDEX);
	}
	
	public void selectedTable(String selectedDB, String selectedTable) {
		//TODO: cuando se hace click en una tabla.
		database.setSelectedTable(selectedDB, selectedTable);
		
		selectDatabase(selectedDB, false);
		if(tableTab == null) {
			tableTab = new TableTab(ventana, database);
			tabList.add(tableTab);
			tabbedPane.insertTab(tableTab.title + selectedTable, null, tableTab, null, TABLE_INDEX);
		} else {
			tabbedPane.setTitleAt(TABLE_INDEX, tableTab.title + selectedTable);
			
		}
		tableTab.setSelectedTable(selectedDB, selectedTable);
		tabbedPane.setSelectedIndex(TABLE_INDEX);
	}

	public void refresh() {
		for(DatabaseElement element: tabList)
			element.refresh();
	}
}
