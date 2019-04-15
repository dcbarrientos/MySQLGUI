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
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
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

	private int columnOrder = 1;

	private String[] columnsName;
	private Vector<Object[]> data;
	private JScrollPane scroll;
	private JTable table;
	private TableModel tableModel;
	private JToolBar toolBar;
	private JButton editButton;
	private JButton addButton;
	private JButton deleteButton;
	private JButton upButton;
	private JButton downButton;

	public TableColumnsTab(Ventana ventana, Database database) {
		super(ventana, database);
		columnOrder = 1;

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
		scroll = new JScrollPane();
		scroll.setViewportView(table);

		add(scroll, BorderLayout.CENTER);

		toolBar = new JToolBar();
		
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
		
		add(toolBar, BorderLayout.SOUTH);
	}

	protected void loadData() {
		if (definition != null) {
			tableModel = new TableModel();
			tableModel.setColumnsClasses(classes);
			tableModel.setColumnHeaders(columnsName);

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

			tableModel.setData(data);
			table.setModel(tableModel);
			columnOrder = 1;
		}
	}

	public void addRecord(String linea) {
		String[] datos = linea.split(" ");
		Object[] records = new Object[COLUMN_COUNT];
		String dato;

		// Indice de la columna
		JLabel campo = new JLabel(String.valueOf(columnOrder));
		if(columnOrder == 1)
			campo.setIcon(new ImageIcon(getClass().getResource(primaryKeyIcon)));
		else
			campo.setIcon(new ImageIcon(getClass().getResource(columnIcon)));
		
		records[COLUMN_ORDER_INDEX] = campo;
		
		columnOrder++;

		// Nombre del campo
		records[COLUMN_NAME_INDEX] = datos[0].substring(1, datos[0].length() - 1);

		// Tipo y longitud del dato
		dato = datos[1].trim();
		if (dato.indexOf("(") > 0) {
			records[COLUMN_DATA_TYPE_INDEX] = dato.substring(0, dato.indexOf("("));
			records[COLUMN_DATA_LENGTH_INDEX] = dato.substring(dato.indexOf("(") + 1, dato.indexOf(")"));
		} else {
			records[COLUMN_DATA_TYPE_INDEX] = dato;
			records[COLUMN_DATA_LENGTH_INDEX] = "";
		}

		int i = 2;
		while (i < datos.length) {
			switch (datos[i].toUpperCase().trim()) {
			case "NOT":
				i++;
				records[COLUMN_NOT_NULL_INDEX] = true;
				break;
			case "NULL":
				records[COLUMN_NOT_NULL_INDEX] = false;
				break;
			case "DEFAULT":
				// TODO: Sacar o dejar las '' dependiendo del dato que lo usa. BIGINT sin,
				// VARCHAR con.
				i++;
				records[COLUMN_DEFAULT_INDEX] = datos[i];
				break;
			case "AUTO_INCREMENT":
				//TODO: Sólo para integers y tipo floating-point 
				records[COLUMN_AUTO_INCREMENT_INDEX] = true;
				break;
			case "BINARY":
//				tableStructure.setValueAt(true, selectedRow, CNewTableField.BINARY_INDEX);
//				tableStructure.setRowSelectionInterval(selectedRow, selectedRow);
				break;
			case "UNSIGNED":
				records[COLUMN_UNSIGNED_INDEX] = true;
				break;
			case "ZEROFILL":
				records[COLUMN_ZEROFILL_INDEX] = true;
				break;
			case "COMMENT":
				i++;
				records[COLUMN_COMMENT_INDEX] = datos[i];
				break;
			case "COLLATE":
				i++;
				records[COLUMN_COLLATION_INDEX] = datos[i];
				break;
			case "CHARACTER":
			case "CHARSET":
				break;
			}
			i++;
		}
		data.add(records);
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	private void editButtonMouseClicked(MouseEvent e) {
		//TODO: falta implementar.
	}

	private void addButtonMouseClicked(MouseEvent e) {
		//TODO: falta implementar.
	}

	private void deleteButtonMouseClicked(MouseEvent e) {
		//TODO: falta implementar.
	}

	private void upButtonMouseClicked(MouseEvent e) {
		//TODO: falta implementar.
	}

	private void downButtonMouseClicked(MouseEvent e) {
		//TODO: falta implementar.
	}

}

//CREATE TABLE `cartas` (
//		  `Ca_Cli` smallint(5) NOT NULL DEFAULT '0',
//		  `Ca_Fecha` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
//		  `Ca_Tipo` varchar(1) NOT NULL DEFAULT '',
//		  `Ca_Obs` text,
//		  PRIMARY KEY (`Ca_Cli`,`Ca_Fecha`,`Ca_Tipo`)
//		) ENGINE=InnoDB DEFAULT CHARSET=latin1