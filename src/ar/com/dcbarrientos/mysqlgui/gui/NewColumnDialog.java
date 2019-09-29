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
 * NewColumnDialog.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 22 abr. 2019, 10:34:13 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.model.ColumnModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class NewColumnDialog extends JDialog {
	private static final long serialVersionUID = 1L;
//TODO: pasar los textos a resource.
	private Database database;
	private ResourceBundle resource;
	private ColumnModel columnModel = null;
	private HashMap<String, String> charsetList;
//	private boolean isNew;

	private JPanel panel;
	private JButton btnAccept;
	private JButton btnCancel;
	private JLabel lblName;
	private JTextField txtName;
	private JLabel lblCollation;
	private JLabel lblDatatype;
	private JComboBox<String> cbDataType;
	private JLabel lblLength;
	private JTextField txtLength;
	private JLabel lblCharset;
	private JComboBox<String> cbCharset;
	private JComboBox<String> cbCollation;
	private JLabel lblDefault;
	private JTextField txtDefault;
	private JCheckBox chkNotNull;
	private JCheckBox chkUnique;
	private JCheckBox chkBinary;
	private JCheckBox chkUnsigned;
	private JCheckBox chkZerofill;
	private JCheckBox chkAutoIncrement;
	private JLabel lblComents;
	private JScrollPane scrollPane;
	private JTextPane txtComments;
	private JCheckBox chkGenerated;
	private ButtonGroup bGGenerated;
	private JRadioButton rdVirtual;
	private JRadioButton rdStored;
	private boolean isNew;

	public NewColumnDialog() {
		this.isNew = true;

		initComponents();
	}

	public NewColumnDialog(Database database, Ventana ventana) {
		this.database = database;
		this.resource = ventana.resource;
		this.isNew = true;

		initComponents();
	}

	public NewColumnDialog(Database database, Ventana ventana, ColumnModel columnModel) {
		this(database, ventana);
		this.columnModel = columnModel;
		this.isNew = false;

		loadColumnData();
	}

	private void initComponents() {
		setPreferredSize(new Dimension(470, 375));
		setTitle(resource.getString("NewColumnDialog.title"));

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btnAccept = new JButton("Aceptar");
		btnAccept.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnAcceptClicked(e);
			}
		});

		btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnCancelClicked(e);
			}
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 434, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		groupLayout
				.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING,
										groupLayout.createSequentialGroup().addContainerGap()
												.addComponent(panel, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
														.addComponent(btnCancel).addComponent(btnAccept))
												.addContainerGap()));

		lblName = new JLabel("Name:");

		txtName = new JTextField();
		txtName.setColumns(10);

		lblCollation = new JLabel("Collation:");

		lblDatatype = new JLabel("Datatype:");

		cbDataType = new JComboBox<String>();
		cbDataType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				cbDataTypeChanged();
			}
		});

		lblLength = new JLabel("Length:");

		txtLength = new JTextField();
		txtLength.setColumns(10);

		lblCharset = new JLabel("Charset:");

		cbCharset = new JComboBox<String>();
		cbCharset.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					updateCollation();
				}
			}
		});

		cbCollation = new JComboBox<String>();

		lblDefault = new JLabel("Default:");

		txtDefault = new JTextField();
		txtDefault.setColumns(10);

		chkNotNull = new JCheckBox("Not null");
		chkUnique = new JCheckBox("Unique");
		chkBinary = new JCheckBox("Binary");
		chkUnsigned = new JCheckBox("Unsigned");
		chkZerofill = new JCheckBox("Zerofill");
		chkAutoIncrement = new JCheckBox("Auto increment");

		lblComents = new JLabel("Coments:");

		scrollPane = new JScrollPane();

		chkGenerated = new JCheckBox("Generated");

		chkGenerated.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rdVirtual.setEnabled(chkGenerated.isSelected());
				rdStored.setEnabled(chkGenerated.isSelected());
				chkBinary.setEnabled(!chkGenerated.isSelected());
				chkNotNull.setEnabled(!chkGenerated.isSelected());
				if (chkGenerated.isSelected())
					lblDefault.setText("Expression:");
				else
					lblDefault.setText("Default:");
			}
		});

		rdVirtual = new JRadioButton("Virtual");
		rdVirtual.setSelected(true);
		rdVirtual.setEnabled(false);
		rdStored = new JRadioButton("Stored");
		rdStored.setSelected(false);
		rdStored.setEnabled(false);

		bGGenerated = new ButtonGroup();
		bGGenerated.add(rdStored);
		bGGenerated.add(rdVirtual);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addComponent(chkGenerated)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(rdVirtual)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(rdStored))
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
								.addGroup(gl_panel.createSequentialGroup().addComponent(chkNotNull)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(chkUnique)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(chkBinary)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(chkUnsigned)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(chkZerofill)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(chkAutoIncrement))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblName)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblDatatype)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, 173,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblLength)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtLength, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblCharset)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(cbCharset, 0, 152, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblCollation)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(cbCollation,
												GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblDefault)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(txtDefault, GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
								.addComponent(lblComents))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblName).addComponent(txtName,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblDatatype)
						.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLength).addComponent(txtLength, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblCollation)
						.addComponent(lblCharset)
						.addComponent(cbCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(cbCollation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblDefault).addComponent(
						txtDefault, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(chkNotNull)
						.addComponent(chkUnique).addComponent(chkBinary).addComponent(chkUnsigned)
						.addComponent(chkZerofill).addComponent(chkAutoIncrement))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(chkGenerated)
						.addComponent(rdVirtual).addComponent(rdStored))
				.addGap(3).addComponent(lblComents).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE).addContainerGap()));

		txtComments = new JTextPane();
		scrollPane.setViewportView(txtComments);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		pack();
		setLocationRelativeTo(null);

		initData();
	}

	private void cbDataTypeChanged() {
		Boolean[] attribs = database.mySqlDataType.get(cbDataType.getSelectedItem());
		txtLength.setEnabled(attribs[database.DATATYPE_LENTH_INDEX]);
		chkNotNull.setEnabled(attribs[database.DATATYPE_NOT_NULL_INDEX]);
		chkUnique.setEnabled(attribs[database.DATATYPE_UNIQUE_INDEX]);
		chkBinary.setEnabled(attribs[database.DATATYPE_BINARY_INDEX]);
		chkUnsigned.setEnabled(attribs[database.DATATYPE_UNSIGNED_INDEX]);
		chkZerofill.setEnabled(attribs[database.DATATYPE_ZEROFILL_INDEX]);
		chkAutoIncrement.setEnabled(attribs[database.DATATYPE_AUTOINCREMENT_INDEX]);

	}

	private void initData() {
		Query query = new Query(database);
		charsetList = new HashMap<String, String>();

		query.executeQuery(Query.SQL_CHARSET_LIST);
		cbCharset.addItem("");
		while (query.next()) {
			charsetList.put(query.getString("Charset"), query.getString("Default collation"));
			cbCharset.addItem(query.getString("Charset"));
		}

		for (String key : database.mySqlDataType.keySet())
			cbDataType.addItem(key);

		query.close();
		if (!isNew)
			loadColumnData();
	}

	private void loadColumnData() {
		txtName.setText(columnModel.name);
		cbDataType.setSelectedItem(columnModel.dataType);
		txtLength.setText(columnModel.length);
		cbCharset.setSelectedItem(columnModel.charset);
		cbCollation.setSelectedItem(columnModel.collate);
		txtDefault.setText(columnModel.columnDefault);
		chkNotNull.setSelected(columnModel.notNull);
		chkUnique.setSelected(columnModel.unique);
		chkBinary.setSelected(columnModel.binary);
		chkUnsigned.setSelected(columnModel.unsigned);
		chkZerofill.setSelected(columnModel.zerofill);
		chkAutoIncrement.setSelected(columnModel.autoincrement);
		chkGenerated.setSelected(columnModel.generated);
		txtComments.setText(columnModel.comment);
		rdVirtual.setSelected(columnModel.virtual);
		rdStored.setSelected(columnModel.stored);
	}

	private void updateCollation() {
		cbCollation.removeAllItems();

		String sql = String.format(Query.SQL_COLLATION_LIST_PARAM, cbCharset.getSelectedItem().toString() + "\\_%");
		Query query = new Query(database);
		query.executeQuery(sql);
		cbCollation.addItem("");
		while (query.next())
			cbCollation.addItem(query.getString("COLLATION_NAME"));

		cbCollation.setSelectedItem(charsetList.get(cbCharset.getSelectedItem()));
		query.close();
	}

	private void btnAcceptClicked(MouseEvent e) {
		columnModel = new ColumnModel();
		columnModel.name = txtName.getText();
		columnModel.dataType = (String) cbDataType.getSelectedItem();
		columnModel.length = txtLength.getText();
		columnModel.columnDefault = txtDefault.getText();
		columnModel.charset = (String) cbCharset.getSelectedItem();
		if(cbCollation.getSelectedIndex()>=0)
			columnModel.collate = (String) cbCollation.getSelectedItem();
		columnModel.notNull = chkNotNull.isSelected();
		columnModel.unique = chkUnique.isSelected();
		columnModel.binary = chkBinary.isSelected();
		columnModel.unsigned = chkUnsigned.isSelected();
		columnModel.zerofill = chkZerofill.isSelected();
		columnModel.autoincrement = chkAutoIncrement.isSelected();
		columnModel.comment = txtComments.getText();
		columnModel.generated = chkGenerated.isSelected();
		columnModel.virtual = rdVirtual.isSelected();
		columnModel.stored = rdStored.isSelected();

		dispose();
	}

	private void btnCancelClicked(MouseEvent e) {
		dispose();
	}

	public ColumnModel showDialog() {
		this.setModal(true);
		this.setVisible(true);

		return columnModel;
	}
}
