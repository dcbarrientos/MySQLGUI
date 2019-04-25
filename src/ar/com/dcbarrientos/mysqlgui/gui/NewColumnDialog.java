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

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.model.ColumnModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class NewColumnDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private Database database;
	private ColumnModel columnModel = null;
	private String defaultCollation;
	private HashMap<String, String> charsetList;

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
	private JTextField textField_2;
	private JCheckBox chkNotNull;
	private JCheckBox chkUnique;
	private JCheckBox chkBinary;
	private JCheckBox chkUnsigned;
	private JCheckBox chkZerofill;
	private JCheckBox chkAutoIncrement;
	private JLabel lblComents;
	private JScrollPane scrollPane;
	private JTextPane txtComments;

	/**
	 * @wbp.parser.constructor
	 */
	public NewColumnDialog(Database database) {
		this.database = database;
		initComponents();
	}

	public NewColumnDialog(Database database, ColumnModel columnModel) {
		this(database);
		this.columnModel = columnModel;
		loadColumnData();
	}

	private void initComponents() {
		setPreferredSize(new Dimension(470, 350));

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
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 434, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnAccept))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		lblName = new JLabel("Name:");

		txtName = new JTextField();
		txtName.setColumns(10);

		lblCollation = new JLabel("Collation:");

		lblDatatype = new JLabel("Datatype:");

		cbDataType = new JComboBox<String>();

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
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		chkNotNull = new JCheckBox("Not null");
		
		chkUnique = new JCheckBox("Unique");
		
		chkBinary = new JCheckBox("Binary");
		
		chkUnsigned = new JCheckBox("Unsigned");
		
		chkZerofill = new JCheckBox("Zerofill");
		
		chkAutoIncrement = new JCheckBox("Auto increment");
		
		lblComents = new JLabel("Coments:");
		
		scrollPane = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addComponent(chkNotNull)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chkUnique)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chkBinary)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chkUnsigned)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chkZerofill)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chkAutoIncrement))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblDatatype)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblLength)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtLength, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblCharset)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cbCharset, 0, 136, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblCollation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbCollation, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addComponent(lblDefault)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
						.addComponent(lblComents, Alignment.LEADING))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDatatype)
						.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLength)
						.addComponent(txtLength, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCollation)
						.addComponent(lblCharset)
						.addComponent(cbCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbCollation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDefault)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(chkNotNull)
						.addComponent(chkUnique)
						.addComponent(chkBinary)
						.addComponent(chkUnsigned)
						.addComponent(chkZerofill)
						.addComponent(chkAutoIncrement))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblComents)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		txtComments = new JTextPane();
		scrollPane.setViewportView(txtComments);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		pack();
		setLocationRelativeTo(null);
		initData();
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
		
		query.close();
	}

	private void loadColumnData() {

	}

	private void updateCollation() {
		cbCollation.removeAllItems();
		
		String sql = String.format(Query.SQL_COLLATION_LIST_PARAM, cbCharset.getSelectedItem().toString() + "\\_%");
		Query query = new Query(database);
		query.executeQuery(sql);
		cbCollation.addItem("");
		while(query.next())
			cbCollation.addItem(query.getString("COLLATION_NAME"));
		
		cbCollation.setSelectedItem(charsetList.get(cbCharset.getSelectedItem()));
		query.close();
	}

	private void btnAcceptClicked(MouseEvent e) {
		columnModel = new ColumnModel();
		columnModel.name = txtName.getText();
		columnModel.dataType = (String)cbDataType.getSelectedItem();
		columnModel.length = txtLength.getText();
		columnModel.charset = (String)cbCharset.getSelectedItem();
		columnModel.collate = (String)cbCollation.getSelectedItem();
		columnModel.notNull = chkNotNull.isSelected();
		columnModel.unique = chkUnique.isSelected();
		columnModel.binary = chkBinary.isSelected();
		columnModel.unsigned = chkUnsigned.isSelected();
		columnModel.zerofill = chkZerofill.isSelected();
		columnModel.autoincrement = chkAutoIncrement.isSelected();
		columnModel.comment = txtComments.getText();
		
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
