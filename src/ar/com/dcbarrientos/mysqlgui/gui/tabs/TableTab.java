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
 * TableTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 6 feb. 2019, 19:00:05 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.ScriptToDatabaseDialog;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableColumnsTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableDDLTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableDataTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableForeignKeysTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableGrantsTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableIndexesTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableInfoTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableOptionsTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TablePartitionsTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.table.TableTriggersTab;
import ar.com.dcbarrientos.mysqlgui.model.ColumnModel;
import ar.com.dcbarrientos.mysqlgui.model.IndexModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TableTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("TableTab.title");
	private boolean isNew;
	private String sqlCreateTable;

	public static final int INFO_INDEX = 0;
	public static final int COLUMNS_INDEX = 1;
	public static final int INDEXES_INDEX = 2;
	public static final int TRIGGERS_INDEX = 3;
	public static final int FOREIGNKEYS_INDEX = 4;
	public static final int PARTITIONS_INDEX = 5;
	public static final int GRANTS_INDEX = 6;
	public static final int OPTIONS_INDEX = 7;
	public static final int DDL_INDEX = 8;
	public static final int DATA_INDEX = 9;

	private final int DEFAULT = 0;

	private JPanel jpTableOptions;
	private JPanel borderTableOptions;
	private JLabel lblTableName;
	private JLabel lblEngine;
	private JLabel lblAutoIncrement;
	private JLabel lblCharset;
	private JLabel lblCollation;
	private JLabel lblDescription;
	private JTextField txtAutoIncrement;
	private JComboBox<String> cbEngine;
	private JTextField txtTableName;
	private JComboBox<String> cbCharset;
	private JComboBox<String> cbCollation;
	private JTextArea txtComment;

	private String txtAutoIncrementOriginal;
	private String cbEngineOriginal;
	private String txtTableNameOriginal;
	private String cbCharsetOriginal;
	private String cbCollationOriginal;
	private String txtCommentOriginal;

	private boolean istxtAutoIncrementChanged;
	private boolean iscbEngineChanged;
	private boolean istxtTableNameChanged;
	private boolean iscbCharsetChanged;
	private boolean iscbCollationChanged;
	private boolean istxtCommentChanged;

	private JTabbedPane tabPane;
	private TableInfoTab tableInfoTab;
	private TableColumnsTab tableColumnsTab;
	private TableIndexesTab tableIndexesTab;
	private TableTriggersTab tableTriggersTab;
	private TableForeignKeysTab tableForeignTab;
	private TablePartitionsTab tablePartitionsTab;
	private TableGrantsTab tableGrantsTab;
	private TableOptionsTab tableOptionsTab;
	private TableDDLTab tableDDLTab;
	private TableDataTab tableDataTab;
	private JPanel buttonPanel;
	private JButton btnApply;
	private JButton btnCancel;

	Vector<DatabaseElement> tabList;

	public TableTab(Ventana ventana, Database database, boolean isNew) {
		super(ventana, database);
		this.isNew = isNew;

		initComponents();
		clean();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		tabList = new Vector<DatabaseElement>();

		jpTableOptions = new JPanel();
		jpTableOptions.setLayout(new BorderLayout());
		jpTableOptions.setPreferredSize(new Dimension(450, 200));
		add(jpTableOptions, BorderLayout.NORTH);

		borderTableOptions = new JPanel();
		borderTableOptions.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		jpTableOptions.add(borderTableOptions, BorderLayout.CENTER);

		lblTableName = new JLabel("Table name:");

		lblEngine = new JLabel("Engine:");

		lblAutoIncrement = new JLabel("Auto Increment:");

		lblCharset = new JLabel("Charset:");

		lblCollation = new JLabel("Collation:");

		lblDescription = new JLabel("Description:");

		txtAutoIncrement = new JTextField();
		txtAutoIncrement.setColumns(10);
		txtAutoIncrement.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (txtAutoIncrement.getText().equals(txtAutoIncrementOriginal))
					istxtAutoIncrementChanged = false;
				else
					istxtAutoIncrementChanged = true;
			}
		});

		cbEngine = new JComboBox<String>(getComboList(Query.SQL_ENGINE_LIST, "Engine", ""));
		cbEngine.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (((String) cbEngine.getSelectedItem()).equals(cbEngineOriginal))
					iscbEngineChanged = true;
				else
					iscbEngineChanged = false;
			}
		});

		txtTableName = new JTextField();
		txtTableName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (txtTableName.getText().equals(txtTableNameOriginal))
					istxtTableNameChanged = false;
				else
					istxtTableNameChanged = true;
			}

			public void keyPressed(KeyEvent e) {
			}
		});
		txtTableName.setColumns(10);

		cbCharset = new JComboBox<String>(getComboList(Query.SQL_CHARSET_LIST, "Charset", "Default Charset"));
		cbCharset.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (!((String) e.getItem()).equals("Default Charset")) {
						String sql = String.format(Query.SQL_COLLATION_LIST_PARAM, ((String) e.getItem()) + "%");
						DefaultComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(
								getComboList(sql, "COLLATION_NAME", "Default Collation"));
						cbCollation.setModel(cbm);
					}
				}
				if (((String) cbCharset.getSelectedItem()).equals(cbCharsetOriginal))
					iscbCharsetChanged = false;
				else
					iscbCharsetChanged = true;
			}
		});

		cbCollation = new JComboBox<String>();
		cbCollation.addItem("Default Collation");
		cbCollation.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (((String) cbCollation.getSelectedItem()).equals(cbCollationOriginal))
					iscbCollationChanged = false;
				else
					iscbCollationChanged = true;
			}
		});

		txtComment = new JTextArea();
		txtComment.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (txtComment.getText().equals(txtCommentOriginal))
					istxtCommentChanged = false;
				else
					istxtCommentChanged = true;
			}
		});
		GroupLayout gl_borderTableOptions = new GroupLayout(borderTableOptions);
		gl_borderTableOptions.setHorizontalGroup(gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_borderTableOptions.createSequentialGroup().addContainerGap()
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAutoIncrement).addComponent(lblEngine).addComponent(lblTableName)
								.addComponent(lblCharset).addComponent(lblCollation).addComponent(lblDescription))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
								.addComponent(txtComment, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
								.addComponent(cbEngine, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtAutoIncrement, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtTableName, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
								.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(cbCollation, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(cbCharset, Alignment.LEADING, 0, 141, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_borderTableOptions.setVerticalGroup(gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_borderTableOptions.createSequentialGroup().addContainerGap()
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTableName).addComponent(txtTableName, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE).addComponent(lblEngine)
								.addComponent(cbEngine, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAutoIncrement).addComponent(txtAutoIncrement,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE).addComponent(lblCharset)
								.addComponent(cbCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCollation).addComponent(cbCollation, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDescription)
								.addComponent(txtComment, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(140, Short.MAX_VALUE)));
		borderTableOptions.setLayout(gl_borderTableOptions);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		add(buttonPanel, BorderLayout.SOUTH);

		btnApply = new JButton(resource.getString("TableTab.btnApply"));
		btnApply.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnApplyMouseClicked(e);
			}
		});
		buttonPanel.add(btnApply);

		btnCancel = new JButton(resource.getString("TableTab.btnCancel"));
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnCancelMouseClicked(e);
			}
		});
		buttonPanel.add(btnCancel);

		tabPane = new JTabbedPane();
		add(tabPane, BorderLayout.CENTER);

		tableInfoTab = new TableInfoTab(ventana, database);
		tabList.add(tableInfoTab);
		tabPane.insertTab(tableInfoTab.title, null, tableInfoTab, null, INFO_INDEX);

		tableColumnsTab = new TableColumnsTab(ventana, database, isNew);
		tabList.add(tableColumnsTab);
		tabPane.insertTab(tableColumnsTab.title, null, tableColumnsTab, null, COLUMNS_INDEX);

		tableIndexesTab = new TableIndexesTab(ventana, database, isNew);
		tabList.add(tableIndexesTab);
		tabPane.insertTab(tableIndexesTab.title, null, tableIndexesTab, null, INDEXES_INDEX);

		tableTriggersTab = new TableTriggersTab(ventana, database);
		tabList.add(tableTriggersTab);
		tabPane.insertTab(tableTriggersTab.title, null, tableTriggersTab, null, TRIGGERS_INDEX);

		tableForeignTab = new TableForeignKeysTab(ventana, database);
		tabList.add(tableForeignTab);
		tabPane.insertTab(tableForeignTab.title, null, tableForeignTab, null, FOREIGNKEYS_INDEX);

		tablePartitionsTab = new TablePartitionsTab(ventana, database);
		tabList.add(tablePartitionsTab);
		tabPane.insertTab(tablePartitionsTab.title, null, tablePartitionsTab, null, PARTITIONS_INDEX);

		tableGrantsTab = new TableGrantsTab(ventana, database);
		tabList.add(tableGrantsTab);
		tabPane.insertTab(tableGrantsTab.title, null, tableGrantsTab, null, GRANTS_INDEX);

		tableOptionsTab = new TableOptionsTab(ventana, database);
		tabList.add(tableOptionsTab);
		tabPane.insertTab(tableOptionsTab.title, null, tableOptionsTab, null, OPTIONS_INDEX);

		tableDDLTab = new TableDDLTab(ventana, database, this);
		tabList.add(tableDDLTab);
		tabPane.insertTab(tableDDLTab.title, null, tableDDLTab, null, DDL_INDEX);
		tabPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tp = (JTabbedPane) e.getSource();
				if (tp.getSelectedIndex() == DDL_INDEX) {
					tableDDLTab.setSQL(getDefinition());
				}
			}

		});

		tableDataTab = new TableDataTab(ventana, database);
		tabList.add(tableDataTab);
		tabPane.insertTab(tableDataTab.title, null, tableDataTab, null, DATA_INDEX);

	}

	protected void loadData() {
		istxtAutoIncrementChanged = false;
		iscbEngineChanged = false;
		istxtTableNameChanged = false;
		iscbCharsetChanged = false;
		iscbCollationChanged = false;
		istxtCommentChanged = false;

		if (!isNew) {
			txtTableName.setText(selectedTable);
			txtTableNameOriginal = selectedTable;

			String sql = String.format(Query.SQL_SHOW_CREATE_TABLE, selectedDB, selectedTable);
			// String txtCreate = "";
			Query query = new Query(database);
			if (query.executeQuery(sql)) {
				query.next();
				sqlCreateTable = query.getString("Create table");
			}
			query.close();

			if (sqlCreateTable.length() > 0)
				processCreateTable();
		} else {
			txtTableName.setText(resource.getString("TableTab.tableName"));
			tableIndexesTab.setTableName(txtTableName.getText());
		}
	}

	private void processCreateTable() {
		// Definición de los registros.
		// loadColumnsDefinition(sqlCreateTable.substring(sqlCreateTable.indexOf("\n") +
		// 1, sqlCreateTable.lastIndexOf(")")).trim());
		loadTableDefinition(
				sqlCreateTable.substring(sqlCreateTable.lastIndexOf(")") + 1, sqlCreateTable.length()).trim());

		tableColumnsTab.setDefinition(
				sqlCreateTable.substring(sqlCreateTable.indexOf("\n") + 1, sqlCreateTable.lastIndexOf(")")).trim());

		tableIndexesTab.setDefinition(
				sqlCreateTable.substring(sqlCreateTable.indexOf("\n") + 1, sqlCreateTable.lastIndexOf(")")).trim());

		tableDDLTab.setSQL(sqlCreateTable);
	}

	private void loadTableDefinition(String tableAttribs) {
		// clean();
		String[] attribs = tableAttribs.split(" ");
		String attrib;
		String value;
		int i = 0;
		while (i < attribs.length) {
			String[] tokens = attribs[i].split("=");
			attrib = tokens[0];
			if (tokens.length > 1)
				value = tokens[1];
			else {
				i++;
				tokens = attribs[i].split("=");
				attrib += " " + tokens[0];
				value = tokens[1];
			}

			switch (attrib) {
			case "ENGINE":
				cbEngine.setSelectedItem(value);
				cbEngineOriginal = value;
				break;
			case "AUTO_INCREMENT":
				txtAutoIncrement.setText(value);
				txtAutoIncrementOriginal = value;
				break;
			case "DEFAULT CHARSET":
				cbCharset.getModel().setSelectedItem(value);
				cbCharsetOriginal = value;
				break;
			case "COLLATE":
				cbCollation.setSelectedItem(value);
				cbCollationOriginal = value;
				break;
			case "COMMENT":
				String comment = value.substring(1, value.length());
				while (i < attribs.length && !comment.endsWith("'")) {
					i++;
					comment += " " + attribs[i];
				}
				txtComment.setText(comment.substring(0, comment.length() - 1));
				txtCommentOriginal = txtComment.getText();
				break;
			default:
				break;
			}

			i++;
		}
	}

	private String[] getComboList(String sql, String columnName, String defaultValue) {
		Query query = new Query(database);
		String[] lista = null;

		if (query.executeQuery(sql)) {
			int i = 0;
			if (defaultValue.length() > 0) {
				lista = new String[query.getRowCount() + 1];
				lista[0] = defaultValue;
				i++;
			} else
				lista = new String[query.getRowCount()];

			while (query.next()) {
				lista[i] = query.getString(columnName);
				i++;
			}
		}
		query.close();

		return lista;
	}

	public void setSelectedTable(String selectedDb, String selectedTable) {
		this.selectedDB = selectedDb;
		this.selectedTable = selectedTable;

		for (DatabaseElement element : tabList) {
			element.setSelectedTable(selectedDB, selectedTable);
		}

		this.refresh();
	}

	public void selectTab(int index) {
		tabPane.setSelectedIndex(index);
	}

	public void btnApplyMouseClicked(MouseEvent e) {
		String sql = getDefinition();

		// TODO: Codigo para ejecutar sql.
		// TODO: Tomar en cuenta el cambio de nombre cuando hago refresh.

		ScriptToDatabaseDialog sdb = new ScriptToDatabaseDialog(ventana, database);
		sdb.setSQLSript(sql);
		if (sdb.showDialog()) {
			if (isNew) {
				ventana.refresh();
				ventana.setSelectedTable(selectedDB, selectedTable);
			} else
				refresh();
		}
		System.out.println(sql);
	}

	public String getDefinition() {
		selectedTable = txtTableName.getText();
		txtTableNameOriginal = selectedTable;

		String sql = "";
		if (isNew)
			sql = "CREATE TABLE `" + selectedDB + "`.`" + selectedTable + "`(\n";
		else
			sql = "ALTER TABLE `" + selectedDB + "`.`" + selectedTable + "`\n";

		if (istxtTableNameChanged) {
			sql += "\nRENAME TO `" + selectedDB + "`.`" + txtTableName.getText() + "`";
		}

		// Proceso columnas
		Vector<ColumnModel> columns = tableColumnsTab.getDefinitionsForSQL();
		for (int i = 0; i < columns.size(); i++) {
			if (isNew) {
				sql += "\t" + columns.get(i).getDefinition();
			} else {
				if (columns.get(i).isNew) {
					// Es una columna nueva
					sql += "ADD COLUMN " + columns.get(i).getDefinition();
				} else if (columns.get(i).isDeleted) {
					// Es una columna que hay que borrar
					sql += "DROP COLUMN " + columns.get(i).name;
				} else {
					// Es una columna modificada.
					sql += "CHANGE `" + columns.get(i).originalName + "` " + columns.get(i).getDefinition();
				}
			}

			if (i < columns.size() - 1)
				sql += ", \n";
		}

		// Proceso indices
		Vector<IndexModel> indices = tableIndexesTab.getDefinitionsForSQL();
		if (indices.size() > 0 && columns.size() > 0)
			sql += ", \n";

		for (int i = 0; i < indices.size(); i++) {
			if (isNew) {
				sql += "\t" + indices.get(i).getDefinition();
			} else {
				if (indices.get(i).isNew) {
					sql += "ADD INDEX " + indices.get(i).getDefinition();
				} else if (indices.get(i).isDeleted) {
					sql += "DROP INDEX " + indices.get(i).name;
				} else {
					sql += "DROP INDEX " + indices.get(i).originalName + ",\n";
					sql += "ADD INDEX " + indices.get(i).getDefinition();
				}
			}

			if (i < indices.size() - 1)
				sql += ",\n";
		}

		if (isNew)
			sql += "\n)";

		// Opciones
		if (iscbEngineChanged)
			sql += "\nENGINE = " + cbEngine.getSelectedItem();

		if (iscbCharsetChanged) {
			if (cbCharset.getSelectedIndex() != DEFAULT)
				sql += "\nDEFAULT CHARACTER SET = " + cbCharset.getSelectedItem();
		}

		if (iscbCollationChanged) {
			if (cbCollation.getSelectedIndex() != DEFAULT)
				sql += "\nCOLLATE = " + cbCollation.getSelectedItem();
		}

		if (istxtAutoIncrementChanged && txtAutoIncrement.getText().length() > 0)
			sql += " \nAUTOINCREMENT = " + txtAutoIncrement.getText();

		if (istxtCommentChanged && txtComment.getText().length() > 0)
			sql += "\nCOMMENT '" + txtComment.getText() + "'";

		sql += ";";

		return sql;
	}

	public void btnCancelMouseClicked(MouseEvent e) {

	}

	private void clean() {
		// TODO pasar a resource.
		cbEngine.setSelectedItem(database.getDefaultEngine());
		txtAutoIncrement.setText("");
		cbCharset.setSelectedIndex(DEFAULT);
		cbCharset.setSelectedIndex(DEFAULT);
		txtComment.setText("");
	}

	@Override
	public void refresh() {
		loadData();

		for (DatabaseElement element : tabList)
			element.refresh();

		tableIndexesTab.setColumns(tableColumnsTab.definitionColumns);
		this.revalidate();
		this.repaint();
	}

}
