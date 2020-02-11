/*
 *  Copyright (C) 2019 Luna
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
 * ViewPanel.java
 *
 * Description:	    <Descripcion>
 * @author			Luna
 *
 * Created on 12 feb. 2019, 16:46:00 
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
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;

/**
 * @author Luna
 *
 */
public class ViewPanel extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("ViewPanel.title");
	private final int COLUMN_COUNT = 1;

	private JLabel titleLabel;
	private JScrollPane scrollPane;
	private JTable table;
	private TableModel tableModel;

	private String[] columnHeaders;
	private Vector<Object[]> datos;

	private CellRenderer cellRenderer;

	private JToolBar toolBar;
	private JButton btnAddView;
	private JButton btnEditView;
	private JButton btnDropView;

	public ViewPanel(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
	}

	private void initComponents() {
		columnHeaders = new String[COLUMN_COUNT];
		columnHeaders[0] = resource.getString("ViewPanel.header1");

		setLayout(new BorderLayout());

		titleLabel = new JLabel("New label");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBackground(Color.BLACK);
		titleLabel.setOpaque(true);
		add(titleLabel, BorderLayout.NORTH);

		table = new JTable();
		tableModel = new TableModel();
		tableModel.setColumnHeaders(columnHeaders);
		table.setModel(tableModel);

		cellRenderer = new CellRenderer();
		table.setDefaultRenderer(Object.class, cellRenderer);

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		add(scrollPane, BorderLayout.CENTER);

		toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		add(toolBar, BorderLayout.WEST);

		btnAddView = new JButton("Add");
		btnAddView.setToolTipText(resource.getString("ViewPanel.toolbar.addview"));
		btnAddView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				addView();
			}
		});
		toolBar.add(btnAddView);

		btnEditView = new JButton();
		btnEditView.setToolTipText(resource.getString("ViewPanel.toolbar.editview"));
		btnEditView.setIcon(new ImageIcon(getClass().getResource("/images/InsertRecord.gif")));
		btnEditView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				editView();
			}
		});
		toolBar.add(btnEditView);

		btnDropView = new JButton();
		btnDropView.setToolTipText(resource.getString("ViewPanel.toolbar.dropview"));
		btnDropView.setIcon(new ImageIcon(getClass().getResource("/images/DropTable.gif")));
		btnDropView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				deleteView();
			}
		});
		toolBar.add(btnDropView);
	}

	protected void loadData() {
		Query query = new Query(database);
		String sql = "SHOW FULL TABLES IN " + selectedDB + " WHERE TABLE_TYPE LIKE 'VIEW';";
		query.executeQuery(sql);
		ventana.addMessage(sql);

		datos = new Vector<Object[]>();

		while (query.next()) {
			String[] fila = { query.getString(1) };

			datos.add(fila);
		}

		tableModel.setData(datos);
		query.close();
	}

	private void addView() {
		ventana.setSelectedView(selectedDB, "");
	}

	private void editView() {
		String viewName = getSelectedView();
		ventana.setSelectedView(selectedDB, viewName);
	}

	private void deleteView() {
		String viewName = getSelectedView();
		if (viewName.length() > 0) {
			String msg = String.format(resource.getString("DropView.warning"), selectedDB, viewName);
			int r = JOptionPane.showConfirmDialog(null, msg,
					resource.getString("DropView.title"), JOptionPane.YES_NO_OPTION);
			if(r == JOptionPane.YES_OPTION) {
				Query query = new Query(database);
				String sql = String.format(Query.SQL_DROP_VIEW, selectedDB, viewName);
				ventana.addMessage(sql);
				
				if(query.executeUpdate(sql)< 0) {
					JOptionPane.showMessageDialog(null, query.getErrorMessage(), resource.getString("DropView.title"), JOptionPane.ERROR_MESSAGE);
				} else {
					ventana.setSelectedDatabase(selectedDB);
					ventana.refresh();
				}
				query.close();
			}
		}
	}

	private String getSelectedView() {
		return table.getValueAt(table.getSelectedRow(), 0).toString();
	}
}
