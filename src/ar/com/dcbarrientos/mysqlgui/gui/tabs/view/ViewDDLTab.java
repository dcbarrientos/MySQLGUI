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
 * ViewDDLTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 1 nov. 2019, 11:35:29 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs.view;

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
import ar.com.dcbarrientos.mysqlgui.gui.tabs.ViewTab;
import ar.com.dcbarrientos.mysqlgui.tools.SQLTextArea;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class ViewDDLTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("ViewDDLTab.title");
	private String sql;
//	private boolean isNew;

	private JLabel ddlDescription;
	private JToolBar toolBar;
	private JButton refreshSql;
	private JButton formatSql;
	private JButton generateDDL;
	private RTextScrollPane scrollPane;
	private SQLTextArea textArea;
	private ViewTab viewTab;

//	public ViewDDLTab(Ventana ventana, Database database, ViewTab viewTab, boolean isNew) {
	public ViewDDLTab(Ventana ventana, Database database, ViewTab viewTab) {
		super(ventana, database);
		this.viewTab = viewTab;
//		this.isNew = true;

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
				// TODO: Falta hacer RefreshSQL
				// setSQL(viewTab.getDefinition());
			}
		});
		toolBar.add(refreshSql);

		formatSql = new JButton("Fo");
		formatSql.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				textArea.setText(database.format(textArea.getText()));
			}
		});
		toolBar.add(formatSql);

		generateDDL = new JButton("Ge");
		generateDDL.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				updateDDL();
			}
		});
		toolBar.add(generateDDL);

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

	public void updateDDL() {
		textArea.setText(database.format(viewTab.getDefinition()));
	}

	protected void loadData() {
		if (!viewTab.isNew)
			textArea.setText(database.format(sql));
	}
}
