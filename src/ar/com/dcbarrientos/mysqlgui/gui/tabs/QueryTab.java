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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.fife.ui.rtextarea.RTextScrollPane;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.ExecuteQueryModel;
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
	private JButton btnExecuteLine;
	private JButton btnBeautify;
	private JSplitPane mainPane;
	private RTextScrollPane queryScroll;
	private JScrollPane resultScroll;
	private JTable resultTable;
	private SQLTextArea textArea;
	private JTabbedPane tabbedPane;

	private TableModel tableModel;
	private int idQuery;
	private int rowCount;

	public QueryTab(Ventana ventana, Database database) {
		super(ventana, database);
		idQuery = 0;
		rowCount = 0;

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

		btnExecute = new JButton("Ex");
		btnExecute.setToolTipText(resource.getString("QueryTab.btnExecute"));
		btnExecute.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				processQueries();
				refresh();
			}
		});
		toolBar.add(btnExecute);

		btnExecuteLine = new JButton("EL");
		btnExecuteLine.setToolTipText(resource.getString("QueryTab.btnExecuteLine"));
		btnExecuteLine.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				tabbedPane.removeAll();
				addQuery(getSqlLine());
				refresh();
			}
		});
		toolBar.add(btnExecuteLine);

		btnBeautify = new JButton("b");
		btnBeautify.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				textArea.setText(database.format(textArea.getText()));
			}
		});
		toolBar.add(btnBeautify);

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

		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		mainPane.setBottomComponent(tabbedPane);

	}

	/**
	 * Executes the given sql statement which will return
	 * <code>ExecuteQueryModel</code> with the result information. This information
	 * can be a <code>TableModel</code> or the update count. It will depend on the
	 * sql statement.
	 * 
	 * @param sql statement
	 * @return result information, a TableModel or the update count.
	 */
	private ExecuteQueryModel execute(String sql) {
		ExecuteQueryModel eqm = new ExecuteQueryModel();
		Query query = new Query(database);
		if (query.execute(sql)) {
			idQuery++;
			rowCount = query.getRowCount();

			Vector<Object[]> datos = query.getDataAsObjectVector();
			String[] columnHeader = query.getColumnNames();
			tableModel = new TableModel(datos, columnHeader);

			query.close();

			eqm.tableModel = tableModel;
			eqm.updateCount = -1;
			ventana.addMessage(sql);
		} else {
			eqm.updateCount = query.getUpdateCount();
			if (eqm.updateCount >= 0) {
				ventana.addMessage(
						eqm.updateCount + " " + resource.getString("QueryTab.rowsAffected") + "(" + sql + ")");
			} else {
				eqm.updateCount = -1;
				eqm.tableModel = null;
				String msg = query.errorCode + ": " + query.errorMsg;
				JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
				ventana.addMessage(msg);

			}
		}

		return eqm;
	}

	/**
	 * Gets the sql statement where the keyboard cursor is located in the text area.
	 * 
	 * @return sql statement.
	 */
	private String getSqlLine() {
		String[] sqls = split(textArea.getText());

		int carPos = textArea.getCaretPosition();
		int i = 0;
		int acum = 0;

		while (i < sqls.length) {
			acum += sqls[i].length();
			if (acum >= carPos) {
				sqls[i] = sqls[i].trim();
				return sqls[i].substring(0, sqls[i].lastIndexOf(";"));
			}
			i++;
		}
		return null;
	}

	/**
	 * Splits a given text using ; as splitter.
	 * 
	 * @param text string to split
	 * @return the array of strings computed by splitting this string around matches
	 *         of ;
	 */
	private String[] split(String text) {
		String[] t = text.split(";");
		for (int i = 0; i < t.length; i++)
			t[i] = t[i] + ";";

		return t;
	}

	/**
	 * Takes the text in the text area, identifies all the sql statement and
	 * executes them.
	 */
	private void processQueries() {
		tabbedPane.removeAll();
		String[] sqls = split(textArea.getText());
		boolean correct = true;
		int i = 0;
		while (i < sqls.length && correct) {
			String sql = sqls[i].substring(0, sqls[i].lastIndexOf(";")).trim();
			if (sql.length() > 0) {
				correct = addQuery(sql);
			}
			i++;
		}
	}

	/**
	 * Takes the sql statement, executes it and create a tab with the result.
	 * Finally adds the tab to the <code>JTabbedPane</code> with all the tabs.
	 * 
	 * @param sql statement.
	 * @return <code>true</code> if added a tab, it means it was a SELECT statement;
	 *         <code>false</code> if it is an update count or there are no results
	 */
	private boolean addQuery(String sql) {
		ExecuteQueryModel eqm = execute(sql);
		boolean resu = false;
		if (eqm.updateCount < 0 && eqm.tableModel != null) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());

			JLabel description = new JLabel(" " + rowCount + " " + resource.getString("DataTab.rows"));
			description.setForeground(Color.WHITE);
			description.setBackground(Color.BLACK);
			description.setOpaque(true);
			panel.add(description, BorderLayout.NORTH);

			JTable table = new JTable();
			table.setModel(eqm.tableModel);
			JScrollPane scroll = new JScrollPane();

			scroll.setViewportView(table);
			panel.add(scroll, BorderLayout.CENTER);

			tabbedPane.add(panel);
			tabbedPane.setTitleAt(tabbedPane.getTabCount() - 1, resource.getString("QueryTab.title") + " " + idQuery);
			resu = true;
		} else if (eqm.updateCount >= 0) {
			resu = true;
		}

		return resu;
	}
}
