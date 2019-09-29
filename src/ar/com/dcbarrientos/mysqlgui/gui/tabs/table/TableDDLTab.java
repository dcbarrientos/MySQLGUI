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
 * TableDDLTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 28 feb. 2019, 11:46:37 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs.table;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import org.fife.ui.rtextarea.RTextScrollPane;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.TableTab;
import ar.com.dcbarrientos.mysqlgui.tools.SQLTextArea;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TableDDLTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("TableDDLTab.title");
	private String sql;
	
	private JLabel ddlDescription;
	private JToolBar toolBar;
	private JButton refreshSql;
	private RTextScrollPane scrollPane;
	private SQLTextArea textArea;
	private TableTab tableTab;

	public TableDDLTab(Ventana ventana, Database database, TableTab tableTab) {
		super(ventana, database);
		this.tableTab = tableTab;
		
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		
		toolBar = new JToolBar();
		toolBar.setOrientation(JToolBar.VERTICAL);
		add(toolBar, BorderLayout.WEST);
		
		refreshSql = new JButton("Re");
		refreshSql.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setSQL(tableTab.getDefinition());
			}
		});
		toolBar.add(refreshSql);

		ddlDescription = new JLabel();
		add(ddlDescription, BorderLayout.NORTH);

		textArea = new SQLTextArea();
		textArea.setText("");

		scrollPane = new RTextScrollPane();
		scrollPane.setViewportView(textArea);
		scrollPane.setLineNumbersEnabled(true);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void setSQL(String sql) {
		this.sql = sql;
		loadData();
	}
	
	protected void loadData() {
//		if (selectedDB != null && selectedTable != null) {
//			String sql = "SHOW CREATE TABLE " + selectedDB + "." + selectedTable;
//
//			Query query = new Query(database);
//			ventana.addMessage(sql);
//			if (query.executeQuery(sql)) {
//				query.next();
//				textArea.setText(query.getString(2));
//			}
//		}
		
		textArea.setText(sql);
	}
}
