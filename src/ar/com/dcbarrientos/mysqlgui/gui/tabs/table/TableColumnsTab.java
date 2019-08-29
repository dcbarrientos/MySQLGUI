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
 * TableColumnsTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 28 feb. 2019, 11:24:51 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs.table;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.NewColumnDialog;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.ColumnModel;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;
import ar.com.dcbarrientos.mysqlgui.model.TableRenderer;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TableColumnsTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("TableColumnsTab.title");

	private String definition;

	private static int COLUMN_COUNT = 14;

	private final int COLUMN_ORDER_INDEX = 0;
	private final int COLUMN_NAME_INDEX = 1;
	private final int COLUMN_DATA_TYPE_INDEX = 2;
	private final int COLUMN_DATA_LENGTH_INDEX = 3;
	private final int COLUMN_NOT_NULL_INDEX = 4;
	private final int COLUMN_AUTO_INCREMENT_INDEX = 5;
	private final int COLUMN_ZEROFILL_INDEX = 6;
	private final int COLUMN_UNSIGNED_INDEX = 7;

	private final int COLUMN_KEY_INDEX = 8;
	private final int COLUMN_DEFAULT_INDEX = 9;
	private final int COLUMN_EXTRA_INDEX = 10;
	private final int COLUMN_EXPRESSION_INDEX = 11;
	private final int COLUMN_COMMENT_INDEX = 12;
	private final int COLUMN_COLLATION_INDEX = 13;

	private final String columnIcon = "/images/campo_secundario.gif";
	private final String primaryKeyIcon = "/images/campo_primario.gif";

	private Class<?>[] classes = { JLabel.class, String.class, String.class, String.class, Boolean.class, Boolean.class,
			Boolean.class, Boolean.class, String.class, String.class, String.class, String.class, String.class,
			String.class };

//	private int columnOrder = 1;

	private String[] columnsName;
	private Vector<Object[]> data;
	public Vector<ColumnModel> definitionColumns;
	private HashMap<Integer, String> alterColumns;

	private JScrollPane scroll;
	private JTable table;
	private TableModel tableModel;
	private JToolBar toolBar;
	private JButton editButton;
	private JButton addButton;
	private JButton deleteButton;
	private JButton upButton;
	private JButton downButton;

	private boolean isNew;

	public TableColumnsTab(Ventana ventana, Database database, boolean isNew) {
		super(ventana, database);
		this.isNew = isNew;
//		columnOrder = 1;

		initComponents();
	}

	private void initComponents() {
		columnsName = new String[COLUMN_COUNT];
		for (int i = 0; i < COLUMN_COUNT; i++) {
			columnsName[i] = resource.getString("TableColumnsTab.column" + (i + 1));
		}

		setLayout(new BorderLayout());
		table = new JTable();
		table.setDefaultRenderer(JLabel.class, new TableRenderer());

		tableModel = new TableModel();
		tableModel.setColumnsClasses(classes);
		tableModel.setColumnHeaders(columnsName);

		scroll = new JScrollPane();
		scroll.setViewportView(table);

		add(scroll, BorderLayout.CENTER);

		toolBar = new JToolBar();
		toolBar.setOrientation(JToolBar.VERTICAL);
		editButton = new JButton("Edit");
		editButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				editButtonMouseClicked(e);
			}
		});
		toolBar.add(editButton);

		addButton = new JButton("Add");
		addButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				addButtonMouseClicked(e);
			}
		});
		toolBar.add(addButton);

		deleteButton = new JButton("Delete");
		deleteButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				deleteButtonMouseClicked(e);
			}
		});
		toolBar.add(deleteButton);

		upButton = new JButton("Up");
		upButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				upButtonMouseClicked(e);
			}
		});
		toolBar.add(upButton);

		downButton = new JButton("Down");
		downButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				downButtonMouseClicked(e);
			}
		});
		toolBar.add(downButton);

		add(toolBar, BorderLayout.WEST);
	}

	protected void loadData() {
		definitionColumns = new Vector<ColumnModel>();
		alterColumns = new HashMap<Integer, String>();

		if (definition != null) {

			String[] lineas = definition.split("\n");
			data = new Vector<Object[]>();

			for (int i = 0; i < lineas.length; i++) {
				if (lineas[i].trim().startsWith("`")) {
					String linea = lineas[i].trim();
					if (linea.endsWith(",") || linea.endsWith(";"))
						linea = linea.substring(0, linea.length() - 1);
					addRecord(linea);
				}
			}

			// Generar loadTableData a partir de aca
//			tableModel.setData(data);
			table.setModel(tableModel);
//			columnOrder = 1;
		}
		showDefinition();
		loadTableData();
	}

	private void showDefinition() {
		for (ColumnModel c : definitionColumns)
			System.out.println(c.getDefinition());
	}

	public void addRecord(String linea) {
		String[] datos = linea.split(" ");
		String dato;
		ColumnModel columnModel = new ColumnModel();

		int i = 0;
		// Nombre del campo
		columnModel.name = datos[i];
		while(datos[i].charAt(datos[i].length()-1) != '`') {
			i++;
			columnModel.name += " " + datos[i]; 
		}
		columnModel.name = columnModel.name.substring(1, columnModel.name.length() - 1);
		i++;
		
		// Tipo y longitud del dato
		dato = datos[i].trim();
		if (dato.indexOf("(") > 0) {
			columnModel.dataType = dato.substring(0, dato.indexOf("(")).toUpperCase();
			columnModel.length = dato.substring(dato.indexOf("(") + 1, dato.indexOf(")"));
		} else {
			columnModel.dataType = dato.toUpperCase();
		}
		
		i++;
		while (i < datos.length) {
			switch (datos[i].toUpperCase().trim()) {
			case "NOT":
				i++;
				columnModel.notNull = true;
				break;
			case "NULL":
				columnModel.notNull = false;
				break;
			case "DEFAULT":
				// TODO: Sacar o dejar las '' dependiendo del dato que lo usa. BIGINT sin,
				// VARCHAR con.
				i++;
				columnModel.columnDefault = datos[i];
				break;
			case "AUTO_INCREMENT":
				// TODO: Sólo para integers y tipo floating-point
				columnModel.autoincrement = true;
				break;
			case "BINARY":
//				tableStructure.setValueAt(true, selectedRow, CNewTableField.BINARY_INDEX);
//				tableStructure.setRowSelectionInterval(selectedRow, selectedRow);
				break;
			case "UNSIGNED":
				columnModel.unsigned = true;
				break;
			case "ZEROFILL":
				columnModel.zerofill = true;
				break;
			case "COMMENT":
				i++;
				columnModel.comment = datos[i];
				break;
			case "COLLATE":
				i++;
				columnModel.collate = datos[i];
				break;
			case "CHARACTER":
			case "CHARSET":
				columnModel.charset = datos[i];
				break;
			}
			i++;
		}
		definitionColumns.add(columnModel);
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	private void editButtonMouseClicked(MouseEvent e) {
		// TODO: falta implementar.
		int fila = table.getSelectedRow();
		if (fila >= 0) {
			String columnName = (String) table.getValueAt(fila, COLUMN_NAME_INDEX);
			NewColumnDialog nc = new NewColumnDialog(database, ventana, getColumnModel(columnName));
			ColumnModel cm = nc.showDialog();
			if (cm != null) {
				int in = replaceIndex(definitionColumns, columnName, cm);
				if (!alterColumns.containsKey(in)) {
					alterColumns.put(in, columnName);
				}

				// TODO: Lo que falta segun TableIndexesTab
				loadTableData();
				tableModel.fireTableDataChanged();
				mostrarLista(getDefinitionsForSQL());
			}
		}
	}

	private void mostrarLista(Vector<ColumnModel> definitionsForSQL) {
		// TODO Auto-generated method stub
		
	}

	private void addButtonMouseClicked(MouseEvent e) {
		// TODO: falta implementar.
		NewColumnDialog nc = new NewColumnDialog(database, ventana);
		ColumnModel cm = nc.showDialog();
		if (cm != null) {

		}
	}

	private void deleteButtonMouseClicked(MouseEvent e) {
		// TODO: falta implementar.
	}

	private void upButtonMouseClicked(MouseEvent e) {
		// TODO: falta implementar.
	}

	private void downButtonMouseClicked(MouseEvent e) {
		// TODO: falta implementar.
	}

	private int replaceIndex(Vector<ColumnModel> list, String columnName, ColumnModel im) {
		int index = -1;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equals(columnName)) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			list.add(im);
			index = list.size() - 1;
		} else
			list.set(index, im);

		return index;
	}

	public Vector<ColumnModel> getDefinitionsForSQL() {
		Vector<ColumnModel> definitions = null;
		if (isNew)
			return definitionColumns;
		else {
			definitions = new Vector<ColumnModel>();
			for (int i : alterColumns.keySet()) {
				definitionColumns.get(i).originalName = alterColumns.get(i);
				definitions.add(definitionColumns.get(i));
			}
		}

		return definitions;
	}

	private void loadTableData() {
		data = new Vector<Object[]>();

		for (int i = 0; i < definitionColumns.size(); i++) {
			Object[] record = new Object[COLUMN_COUNT];
			ColumnModel column = definitionColumns.get(i);

			// Indice de la columna
			JLabel campo = new JLabel(String.valueOf(i + 1));
			if (i == 0)
				campo.setIcon(new ImageIcon(getClass().getResource(primaryKeyIcon)));
			else
				campo.setIcon(new ImageIcon(getClass().getResource(columnIcon)));

			record[COLUMN_ORDER_INDEX] = campo;

			// Nombre del campo
			record[COLUMN_NAME_INDEX] = column.name;

			// Tipo y longitud del dato
			record[COLUMN_DATA_TYPE_INDEX] = column.dataType;
			record[COLUMN_DATA_LENGTH_INDEX] = column.length;

			record[COLUMN_NOT_NULL_INDEX] = column.notNull;
			record[COLUMN_DEFAULT_INDEX] = column.columnDefault;
			record[COLUMN_AUTO_INCREMENT_INDEX] = column.autoincrement;
			record[COLUMN_UNSIGNED_INDEX] = column.unsigned;
			record[COLUMN_ZEROFILL_INDEX] = column.zerofill;
			record[COLUMN_COMMENT_INDEX] = column.comment;
			record[COLUMN_COLLATION_INDEX] = column.collate;

			data.add(record);
		}
		tableModel.setData(data);
	}
	
	private ColumnModel getColumnModel(String columnName) {
		for(int i = 0; i < definitionColumns.size(); i++) {
			if(definitionColumns.get(i).name.equals(columnName)) {
				return definitionColumns.get(i);
			}
		}
		
		return null;
	}
}

//CREATE TABLE `cartas` (
//		  `Ca_Cli` smallint(5) NOT NULL DEFAULT '0',
//		  `Ca_Fecha` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
//		  `Ca_Tipo` varchar(1) NOT NULL DEFAULT '',
//		  `Ca_Obs` text,
//		  PRIMARY KEY (`Ca_Cli`,`Ca_Fecha`,`Ca_Tipo`)
//		) ENGINE=InnoDB DEFAULT CHARSET=latin1