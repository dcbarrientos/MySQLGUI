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
 * QueryTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 6 feb. 2019, 18:53:20 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.fife.ui.rtextarea.RTextScrollPane;

import ar.com.dcbarrientos.mysqlgui.Application;
import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;
import ar.com.dcbarrientos.mysqlgui.tools.SQLTextArea;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class QueryTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;
	public String title = resource.getString("QueryTab.title");

	private JToolBar toolBar;
	private JLabel titleLabel;
	private JButton btnExecute;
	private JSplitPane mainPane;
	private RTextScrollPane queryScroll;
	private JScrollPane resultScroll;
	private JTable resultTable;
	private SQLTextArea textArea;

	private Statement st;
	private TableModel tableModel;

	public QueryTab(Ventana ventana, Database database) {
		super(ventana, database);

		try {
			st = database.getConnection().createStatement();
		} catch (SQLException e) {
			if (Application.DEBUG)
				e.printStackTrace();
		}

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		titleLabel = new JLabel(resource.getString("QueryTab.titleLabel"));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBackground(Color.BLACK);
		titleLabel.setOpaque(true);
		add(titleLabel, BorderLayout.NORTH);

		toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		add(toolBar, BorderLayout.WEST);

		btnExecute = new JButton(resource.getString("QueryTab.btnExecute"));
		btnExecute.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnExecuteMouseClicked(e);
			}
		});
		toolBar.add(btnExecute);

		mainPane = new JSplitPane();
		mainPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(mainPane, BorderLayout.CENTER);

		textArea = new SQLTextArea();
		
		queryScroll = new RTextScrollPane();
		queryScroll.setViewportView(textArea);
		queryScroll.setLineNumbersEnabled(true);
		mainPane.setTopComponent(queryScroll);

		resultTable = new JTable();

		resultScroll = new JScrollPane();
		resultScroll.setViewportView(resultTable);
		mainPane.setBottomComponent(resultScroll);

	}

	private void btnExecuteMouseClicked(MouseEvent e) {
		execute(textArea.getText());
	}

	private void execute(String sql) {
		try {
			if (st.execute(sql)) {
				ResultSet result = st.getResultSet();

				int cc = result.getMetaData().getColumnCount();

				String[] columnHeader = new String[cc];
				for (int i = 0; i < cc; i++)
					columnHeader[i] = result.getMetaData().getColumnName(i + 1);

				Vector<Object[]> datos = new Vector<Object[]>();
				String[] fila;

				while (result.next()) {
					fila = new String[cc];
					for (int i = 0; i < fila.length; i++)
						fila[i] = result.getString(i + 1);
					datos.add(fila);
				}
				tableModel = new TableModel(datos, columnHeader);
				resultTable.setModel(tableModel);
				refresh();

				result.close();
			} else {
				int res = st.getUpdateCount();
				System.out.println(res);
			}
		} catch (SQLException e1) {
			String msg = e1.getErrorCode() + ": " + e1.getMessage();
			JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
			ventana.addMessage(msg);
			if (Application.DEBUG)
				e1.printStackTrace();
		}
	}
	
}

