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
 * NewIndexDialog.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 11 jun. 2019, 11:35:17 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import ar.com.dcbarrientos.mysqlgui.model.ColumnModel;
import ar.com.dcbarrientos.mysqlgui.model.IndexModel;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class NewIndexDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private final int COLUMN_COUNT = 4;

	private JPanel panel;
	private JLabel lblTable;
	private JLabel lblName;
	private JLabel lblType;
	private JTextField textTableName;
	private JTextField textIndexName;
	private JComboBox<String> cbType;
	private JLabel lblBlockSize;
	private JTextField textBlockSize;
	private JLabel lblStorageSize;
	private JComboBox<String> cbStorageSize;
	private JLabel lblParser;
	private JTextField textAnalizador;
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private JTextArea textComments;
	private JPanel panel_2;
	private JScrollPane scrollPane_1;
	private JTable tableFields;
	private TableModel tableModel;

	private String tableName;

	private IndexModel indexModel;
	private ResourceBundle resource;

	private String[] headers;
	private Class<?>[] columnsClasses;
	private JComboBox<String> cbOrder;
	private Vector<Object[]> datos;

	private Vector<ColumnModel> columns;
	private boolean[] isEditable;
	private boolean isNew;

	public NewIndexDialog() {
		this.isNew = false;
		
		initComponents();
	}

	public NewIndexDialog(Ventana ventana, Vector<ColumnModel> columns, String tableName) {
		this.resource = ventana.resource;
		this.columns = columns;
		this.tableName = tableName;
		this.isNew = true;
		
		initComponents();
	}

	public NewIndexDialog(Ventana ventana, Vector<ColumnModel> columns, IndexModel indexModel, String tableName) {
		this(ventana, columns, tableName);

		this.indexModel = indexModel;
		this.isNew = false;
		
		initData();
	}

	private void initComponents() {
		setTitle("New Index");

		headers = new String[COLUMN_COUNT];
		headers[0] = "";
		headers[1] = resource.getString("NewIndexDialog.column1");// "Column";
		headers[2] = resource.getString("NewIndexDialog.column2");// "#";
		headers[3] = resource.getString("NewIndexDialog.column3");// "Order";

		cbOrder = new JComboBox<String>();
		cbOrder.addItem(IndexModel.COMBO_ASC);
		cbOrder.addItem(IndexModel.COMBO_DESC);

		columnsClasses = new Class<?>[COLUMN_COUNT];
		columnsClasses[0] = Boolean.class;
		columnsClasses[1] = String.class;
		columnsClasses[2] = String.class;
		columnsClasses[3] = String.class;

		isEditable = new boolean[COLUMN_COUNT];
		isEditable[0] = isEditable[2] = isEditable[3] = true;
		isEditable[1] = false;

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnSaveMouseClicked(e);
			}
		});

		btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE).addContainerGap())
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup().addComponent(btnSave)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel).addGap(56)))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE).addGroup(groupLayout
								.createParallelGroup(Alignment.BASELINE).addComponent(btnSave).addComponent(btnCancel))
						.addContainerGap()));

		lblTable = new JLabel(resource.getString("NewIndexDialog.table"));

		lblName = new JLabel(resource.getString("NewIndexDialog.name"));

		lblType = new JLabel(resource.getString("NewIndexDialog.type"));

		textTableName = new JTextField();
		textTableName.setColumns(10);
		textTableName.setEditable(false);

		textIndexName = new JTextField();
		textIndexName.setColumns(10);

		cbType = new JComboBox<String>();
		cbType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				switch (cbType.getSelectedIndex()) {

				case IndexModel.INDEX_SPATIAL:
				case IndexModel.INDEX_FULLTEXT:
					cbStorageSize.setEnabled(false);
					break;
				default:
					cbStorageSize.setEnabled(true);
					break;
				}
			}
		});

		lblBlockSize = new JLabel(resource.getString("NewIndexDialog.block_size"));

		textBlockSize = new JTextField();
		textBlockSize.setColumns(10);

		lblStorageSize = new JLabel(resource.getString("NewIndexDialog.storage_size"));

		cbStorageSize = new JComboBox<String>();

		lblParser = new JLabel(resource.getString("NewIndexDialog.parser"));

		textAnalizador = new JTextField();
		textAnalizador.setColumns(10);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, resource.getString("NewIndexDialog.comments"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));

		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				resource.getString("NewIndexDialog.fields"), TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel.createSequentialGroup().addContainerGap().addGroup(gl_panel
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING,
								gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
												.addComponent(lblBlockSize).addComponent(lblParser)
												.addGroup(gl_panel.createSequentialGroup()
														.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
																.addComponent(lblTable).addComponent(lblName))
														.addPreferredGap(ComponentPlacement.RELATED)))
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(textAnalizador, GroupLayout.DEFAULT_SIZE, 332,
																Short.MAX_VALUE))
												.addComponent(textTableName, GroupLayout.DEFAULT_SIZE, 336,
														Short.MAX_VALUE)
												.addGroup(gl_panel.createSequentialGroup()
														.addComponent(textIndexName, GroupLayout.DEFAULT_SIZE, 170,
																Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(lblType)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(cbType, GroupLayout.PREFERRED_SIZE, 130,
																GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_panel.createSequentialGroup()
														.addComponent(textBlockSize, GroupLayout.DEFAULT_SIZE, 129,
																Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(lblStorageSize)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(cbStorageSize, GroupLayout.PREFERRED_SIZE, 130,
																GroupLayout.PREFERRED_SIZE)))))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textTableName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTable))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textIndexName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(cbType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblType).addComponent(lblName))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textBlockSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(cbStorageSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStorageSize).addComponent(lblBlockSize))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textAnalizador, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblParser))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE).addContainerGap()));
		panel_2.setLayout(new BorderLayout(0, 0));

		scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		tableFields = new JTable();

		tableModel = new TableModel();
		tableModel.setColumnHeaders(headers);
		tableModel.setColumnsClasses(columnsClasses);
		tableModel.setEditableColumns(true);
		tableModel.setEditableColumns(isEditable);

		tableFields.setModel(tableModel);
		tableFields.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cbOrder));

		scrollPane_1.setViewportView(tableFields);
		panel_1.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		textComments = new JTextArea();
		scrollPane.setViewportView(textComments);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		pack();
		setLocationRelativeTo(null);

		initData();
	}

	private void initData() {
		textTableName.setText(tableName);

		cbType.addItem(IndexModel.INDEXTYPELIST[IndexModel.INDEX_INDEX]);
		cbType.addItem(IndexModel.INDEXTYPELIST[IndexModel.INDEX_UNIQUE]);
		cbType.addItem(IndexModel.INDEXTYPELIST[IndexModel.INDEX_FULLTEXT]);
		cbType.addItem(IndexModel.INDEXTYPELIST[IndexModel.INDEX_PRIMARY]);
		cbType.addItem(IndexModel.INDEXTYPELIST[IndexModel.INDEX_SPATIAL]);

		cbStorageSize.addItem("");
		cbStorageSize.addItem("BTREE");
		cbStorageSize.addItem("HASH");
		cbStorageSize.addItem("RTREE");

		datos = new Vector<Object[]>();
		if (columns != null) {
			for (ColumnModel column : columns) {
				Object[] dato = new Object[COLUMN_COUNT];
				dato[0] = false;
				dato[1] = column.name;
				// dato[2] = "";//i;

				dato[3] = IndexModel.COMBO_ASC;
				datos.add(dato);
			}
		}
		tableModel.setData(datos);
		
		if(!isNew) {
			textIndexName.setText(indexModel.name);
			cbType.setSelectedItem(indexModel.indexType);
			textBlockSize.setText(indexModel.blockSize);
			cbStorageSize.setSelectedItem(indexModel.storageType);
			textAnalizador.setText(indexModel.parser);
			textComments.setText(indexModel.comment);
			for(int i = 0; i < indexModel.fields.size(); i++) {
				String[] f = indexModel.fields.get(i);
				for(int j = 0; j < tableModel.getRowCount(); j++){
					if(tableModel.getValueAt(j, 1).equals(f[0])) {
						tableModel.setValueAt(true, j, 0);
						break;
					}
				}
			}
		}
	}

	private void btnSaveMouseClicked(MouseEvent e) {
		//TODO: en new index dialog tengo que procesar el Analizador/parser de cada campo y del indice en gral.
		indexModel = new IndexModel();
		indexModel.name = textIndexName.getText();
		indexModel.indexType = (String) cbType.getSelectedItem();
		indexModel.blockSize = textBlockSize.getText();
		indexModel.storageType = (String) cbStorageSize.getSelectedItem();
		indexModel.parser = textAnalizador.getText();
		indexModel.comment = textComments.getText();

		for (int i = 0; i < datos.size(); i++) {
			if ((boolean) datos.get(i)[0]) {
				indexModel.addField((String) datos.get(i)[1], (String) datos.get(i)[2], (String) datos.get(i)[3]);
			}
		}

		dispose();
	}

	public IndexModel showDialog() {
		setModal(true);
		setVisible(true);

		return indexModel;
	}
}
