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
 * TableIndexesTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 28 feb. 2019, 11:32:00 
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
import ar.com.dcbarrientos.mysqlgui.gui.NewIndexDialog;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.ColumnModel;
import ar.com.dcbarrientos.mysqlgui.model.IndexModel;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;
import ar.com.dcbarrientos.mysqlgui.model.TableRenderer;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TableIndexesTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_COUNT = 4;

	private final String primaryKeyIcon = "/images/campo_primario.gif";
	private final String secondaryKeyIcon = "/images/campo_secundario.gif";

	public String title = resource.getString("TableIndexesTab.title");

	private Class<?>[] classes = { JLabel.class, String.class, String.class, String.class };

	private JToolBar toolBar;
	private JButton btnAdd;
	private JButton btnEdit;
	private JButton btnDelete;
	private JScrollPane scrollPane;
	private JTable table;
	private TableModel tableModel;

	private String[] columnsName;

	private Vector<Object[]> data;
	private String definition;

	private Vector<ColumnModel> columns;

	private Vector<IndexModel> definitionIndexes;
	private HashMap<Integer, String> alterIndexes; // Lista de indices de los Indices a modificar con sus nombres
													// originales, en caso que este haya sido modificado.
	private Vector<Integer> newIndexes; // Lista de indices nuevos, estos son utilizados cuando edito una tabla y agrego
										// un campo.

	private boolean isNew;

	public TableIndexesTab(Ventana ventana, Database database, boolean isNew) {
		super(ventana, database);

		this.isNew = isNew;

		if (isNew) {
			definitionIndexes = new Vector<IndexModel>();
			alterIndexes = new HashMap<Integer, String>();
			newIndexes = new Vector<Integer>();
		}

		initComponents();
	}

	private void initComponents() {
		columnsName = new String[COLUMN_COUNT];
		columnsName[0] = "";
		columnsName[1] = resource.getString("TableIndexesTab.tableColumn1");
		columnsName[2] = resource.getString("TableIndexesTab.tableColumn2");
		columnsName[3] = resource.getString("TableIndexesTab.tableColumn3");

		setLayout(new BorderLayout());

		toolBar = new JToolBar();
		toolBar.setOrientation(JToolBar.VERTICAL);
		add(toolBar, BorderLayout.WEST);

		btnAdd = new JButton(resource.getString("TableIndexesTab.btnAdd"));
		btnAdd.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnAddMouseClicked(e);
			}
		});
		toolBar.add(btnAdd);

		btnEdit = new JButton(resource.getString("TableIndexesTab.btnEdit"));
		btnEdit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnEditMouseClicked(e);
			}
		});
		toolBar.add(btnEdit);

		btnDelete = new JButton(resource.getString("TableIndexesTab.btnDelete"));
		btnDelete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnDeleteMouseClicked(e);
			}
		});
		toolBar.add(btnDelete);

		table = new JTable();
		table.setDefaultRenderer(JLabel.class, new TableRenderer());

		tableModel = new TableModel();
		tableModel.setColumnsClasses(classes);
		tableModel.setColumnHeaders(columnsName);
		table.setModel(tableModel);

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void loadData() {
		definitionIndexes = new Vector<IndexModel>();

		if (!isNew) {
			alterIndexes = new HashMap<Integer, String>();
			newIndexes = new Vector<Integer>();
		}

		if (definition != null) {
			String[] lineas = definition.split("\n");

			for (int i = 0; i < lineas.length; i++) {
				if (!lineas[i].trim().startsWith("`")) {
					addRecord(lineas[i].trim());
				}
			}

			loadTableData();
		}
	}

	private void loadTableData() {
		data = new Vector<Object[]>();

		for (int i = 0; i < definitionIndexes.size(); i++) {
			if (!definitionIndexes.get(i).isDeleted) {
				Object[] rec = new Object[COLUMN_COUNT];
				JLabel label = new JLabel(Integer.toString(i + 1));
				if (definitionIndexes.get(i).indexType.equals(IndexModel.INDEXTYPELIST[IndexModel.INDEX_PRIMARY]))
					label.setIcon(new ImageIcon(getClass().getResource(primaryKeyIcon)));
				else
					label.setIcon(new ImageIcon(getClass().getResource(secondaryKeyIcon)));

				rec[0] = label;
				rec[1] = definitionIndexes.get(i).name;
				rec[2] = definitionIndexes.get(i).indexType;
				rec[3] = definitionIndexes.get(i).getFieldList();

				data.add(rec);
			}
		}

		tableModel.setData(data);
	}

	private void addRecord(String linea) {
		if (isIndex(linea)) {
			String[] datos = linea.split(" ");
			IndexModel indexModel = new IndexModel();

//		  PRIMARY KEY (`id`),
//		  UNIQUE KEY `index4` (`f1`),
//		  UNIQUE KEY `multiple` (`f1`(3),`f3`) USING BTREE,
//		  KEY `secundaria` (`f1`) USING BTREE KEY_BLOCK_SIZE=22 COMMENT ''''''comment'''''',
//		  FULLTEXT KEY `sdfasdf` (`f1`) COMMENT ''comentarios''

			int i = 0;

			// Tipo de indice
			if (datos[i].equals("KEY")) {
				indexModel.indexType = "INDEX";
			} else {
				indexModel.indexType = datos[i];
				i++;
			}
			i++;

			// Nombre si lo tiene
			if (!indexModel.indexType.equals("PRIMARY")) {
				indexModel.name = Database.trimCuote(datos[i]);
				i++;
			} else
				indexModel.name = "PRIMARY";

			String tmp = linea.substring(linea.indexOf('(') + 1, linea.lastIndexOf(')'));
			String[] indexColumns = tmp.split(",");

			for (int in = 0; in < indexColumns.length; in++) {
				String f = indexColumns[in];

				String name = f.substring(1, f.lastIndexOf('`'));

				String length = "";
				if (f.indexOf('(') > 0)
					length = f.substring(f.indexOf('(') + 1, f.indexOf(')'));

				String order = "";
				if (f.contains(IndexModel.COMBO_DESC))
					order = IndexModel.COMBO_DESC;

				indexModel.addField(name, length, order);
			}

			i++;
			while (i < datos.length) {
				switch (datos[i]) {
				case "USING":
					i++;
					indexModel.storageType = datos[i];
					break;
				case "COMMENT":
					i++;
					indexModel.comment = Database.trimCuote(datos[i].substring(0, datos[i].length() - 1));
					break;
				case "VISIBLE":
					indexModel.visible = true;
					break;
				case "INVISIBLE":
					indexModel.visible = false;
					break;
				case "WITH":
					i += 2;
					indexModel.parser = Database.trimCuote(datos[i]);
				default:
					if (datos[i].startsWith("KEY_BLOCK_SIZE")) {
						indexModel.blockSize = datos[i].substring(datos[i].indexOf("=") + 1);
					}
					break;
				}

				i++;
			}
			definitionIndexes.add(indexModel);
			// alterIndexes.add(indexModel);
		}
	}
	
	private boolean isIndex(String linea) {
		//linea.startsWith("PRIMARY") || linea.startsWith("KEY") || linea.startsWith("INDEX"
	}

	public void setColumns(Vector<ColumnModel> columns) {
		this.columns = columns;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void btnDeleteMouseClicked(MouseEvent e) {
		int fila = table.getSelectedRow();
		if (fila >= 0) {
			String indexName = (String) table.getValueAt(fila, 1);
			IndexModel indexModel = getIndex(definitionIndexes, indexName);
			indexModel.isDeleted = true;
			// TODO: Cambiar getIndexIndex por definitionIndexes.indexOf(indexModel);
			Integer in = getIndexIndex(definitionIndexes, indexModel.name);

			if (newIndexes.contains(in)) {
				newIndexes.remove(in);
				definitionIndexes.remove(indexModel);
			} else if (!alterIndexes.containsKey(in)) {
				alterIndexes.put(in, indexName);
			}

			loadTableData();
			tableModel.fireTableDataChanged();
		}
	}

	public void btnEditMouseClicked(MouseEvent e) {
		int fila = table.getSelectedRow();
		if (fila >= 0) {
			String indexName = (String) table.getValueAt(fila, 1);

			NewIndexDialog nid = new NewIndexDialog(ventana, columns, getIndex(definitionIndexes, indexName),
					selectedTable);
			IndexModel indexModel = nid.showDialog();
			if (indexModel != null) {
				int in = replaceIndex(definitionIndexes, indexName, indexModel);

				if (!newIndexes.contains(in)) {
					if (!alterIndexes.containsKey(in)) {
						alterIndexes.put(in, indexName);
					}
				}
				loadTableData();
				tableModel.fireTableDataChanged();
				mostrarLista(getDefinitionsForSQL());
			}
		}
	}

	public void btnAddMouseClicked(MouseEvent e) {
		NewIndexDialog nid = new NewIndexDialog(ventana, columns, selectedTable);
		IndexModel indexModel = nid.showDialog();

		if (indexModel != null) {
			indexModel.isNew = true;
			definitionIndexes.add(indexModel);

			// Solo uso newIndexes cuando estoy editando una tabla y agrego un indice.
			if (!isNew)
				newIndexes.add(definitionIndexes.indexOf(indexModel));

			loadTableData();
			tableModel.fireTableDataChanged();
		}
	}

	private int replaceIndex(Vector<IndexModel> list, String indexName, IndexModel im) {
		int index = -1;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equals(indexName)) {
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

	private int getIndexIndex(Vector<IndexModel> list, String indexName) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).name.equals(indexName)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Devuelve el indice de una lista con el nombre <code>indexName</code>.
	 * 
	 * @param list      Lista en la que se busca el indice
	 * @param indexName Nombre del indice a buscar
	 * @return Devuelve un <code>IndexModel</code> cuyo nombre coincide con
	 *         indexName, null si no existe.
	 */
	private IndexModel getIndex(Vector<IndexModel> list, String indexName) {
		IndexModel result;
		for (int i = 0; i < list.size(); i++) {
			result = list.get(i);
			if (result.name.equals(indexName)) {
				return result;
			}
		}

		return null;
	}

	/**
	 * Genera una lista de definiciones a partir de la que contiene los originales
	 * con los indices modificados y nuevos para mostrar en la tabla.
	 * 
	 * @return Devuelve una lista de indices para mostrar en la tabla.
	 */
	public Vector<IndexModel> getDefinitionsForSQL() {
		Vector<IndexModel> definitions = null;
		if (isNew)
			return definitionIndexes;
		else {
			definitions = new Vector<IndexModel>();
			IndexModel indexModel;

			// Cargo los indices modificados.
			for (int i : alterIndexes.keySet()) {
				indexModel = definitionIndexes.get(i);
				indexModel.originalName = alterIndexes.get(i);
				indexModel.isNew = false;
				definitions.add(indexModel);
			}

			// Cargo los nuevos indices.
			for (int i = 0; i < newIndexes.size(); i++) {
				indexModel = definitionIndexes.get(newIndexes.get(i));
				indexModel.isNew = true;
				definitions.add(indexModel);
			}
		}

		return definitions;
	}

	public void setTableName(String tableName) {
		selectedTable = tableName;
	}

	private void mostrarLista(Vector<IndexModel> lista) {
		System.out.println("--------------------SQL index -----------------------");
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i).name);
			System.out.println(lista.get(i).getDefinition());
		}
		System.out.println("----------------------------- -----------------------");
	}
}
