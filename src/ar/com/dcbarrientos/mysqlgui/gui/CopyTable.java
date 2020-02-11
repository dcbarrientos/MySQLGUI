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
 * CopyToDialog.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 5 mar. 2019, 11:02:28 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class CopyTable extends JDialog {
	private static final long serialVersionUID = 1L;

	private final int STRUCTURE_INDEX = 0;
	private final int STRUCTURE_DATA_INDEX = 1;

	private Ventana ventana;
	private ResourceBundle resource;
	private Database database;
	private String databaseName;
	private String tableName;
	private String[] cbCopyDatos;
	private String[] databaseList;

	private JPanel panel;
	private JLabel lblDescription;
	private JLabel lblNewName;
	private JTextField txtTableName;
	private JLabel lblCopy;
	private JComboBox<String> cbCopy;
	private JButton btnOk;
	private JButton btnCancel;

	private boolean isOk = false;
	private JLabel lblDatabase;
	private JComboBox<String> cbDestinationDatabase;

	public CopyTable(Ventana ventana, Database database, String databaseName, String tableName) {
		this.ventana = ventana;
		this.resource = ventana.resource;
		this.database = database;
		this.databaseName = databaseName;
		this.tableName = tableName;

		initComponents();
	}

	private void initComponents() {
		cbCopyDatos = new String[2];
		cbCopyDatos[STRUCTURE_INDEX] = resource.getString("CopyTable.cbCopy1");
		cbCopyDatos[STRUCTURE_DATA_INDEX] = resource.getString("CopyTable.cbCopy2");
		loadDatabaseList();

		panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));

		btnOk = new JButton(resource.getString("General.OK"));
		btnOk.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnOkMouseClicked(e);
			}
		});

		btnCancel = new JButton(resource.getString("General.cancel"));
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnCancelMouseClicked(e);
			}
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addGroup(groupLayout
								.createParallelGroup(Alignment.BASELINE).addComponent(btnCancel).addComponent(btnOk))
						.addContainerGap()));

		lblNewName = new JLabel(resource.getString("CopyTable.lblNewName"));

		txtTableName = new JTextField();
		txtTableName.setText(tableName + resource.getString("CopyTable.tableNameAddition"));
		txtTableName.setColumns(10);
		txtTableName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				setDescription();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		lblDescription = new JLabel();

		lblCopy = new JLabel(resource.getString("CopyTable.lblCopy"));

		cbCopy = new JComboBox<String>(cbCopyDatos);

		lblDatabase = new JLabel(resource.getString("CopyTable.lblDatabase"));

		cbDestinationDatabase = new JComboBox<String>(databaseList);
		cbDestinationDatabase.setSelectedItem(databaseName);
		cbDestinationDatabase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDescription();
			}
		});

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblDescription)
										.addGroup(gl_panel.createSequentialGroup().addComponent(lblCopy)
												.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(cbCopy,
														GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel.createSequentialGroup()
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
														.addComponent(lblDatabase).addComponent(lblNewName))
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
														.addComponent(txtTableName, GroupLayout.DEFAULT_SIZE, 335,
																Short.MAX_VALUE)
														.addComponent(cbDestinationDatabase, 0, 335, Short.MAX_VALUE))))
								.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap().addComponent(lblDescription)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewName).addComponent(
						txtTableName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblDatabase).addComponent(
						cbDestinationDatabase, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblCopy).addComponent(cbCopy,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		panel.setLayout(gl_panel);

		setDescription();

		setTitle(resource.getString("General.copyTable"));
		getContentPane().setLayout(groupLayout);
		setPreferredSize(new Dimension(450, 200));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
	}

	private void loadDatabaseList() {
		Query query = new Query(database);
		if (query.executeQuery(Query.SQL_SHOW_DATABASES)) {
			databaseList = new String[query.getRowCount()];
			int i = 0;
			while (query.next()) {
				databaseList[i] = query.getString(1);
				i++;
			}
		}
		query.close();
	}

	private void btnOkMouseClicked(MouseEvent e) {
		isOk = false;
		Query query = new Query(database);
		String sql = String.format(Query.SQL_COPY_STRUCTURE, (String) cbDestinationDatabase.getSelectedItem(),
				txtTableName.getText(), databaseName, tableName);
		
		ventana.addMessage(sql);
		if (query.executeUpdate(sql) < 0) {
			JOptionPane.showMessageDialog(null, query.errorCode + ": " + query.errorMsg, this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
		} else {
			if (cbCopy.getSelectedIndex() == STRUCTURE_DATA_INDEX) {
				sql = String.format(Query.SQL_COPY_DATA, (String) cbDestinationDatabase.getSelectedItem(),
						txtTableName.getText(), databaseName, tableName);

				if (query.executeUpdate(sql) < 0) {
					JOptionPane.showMessageDialog(null, query.errorCode + ": " + query.errorMsg, this.getTitle(),
							JOptionPane.ERROR_MESSAGE);
				}else {
					isOk = true;
				}
			} else
				isOk = true;
		}

		query.close();
		if(isOk)
			dispose();
	}

	private void btnCancelMouseClicked(MouseEvent e) {
		isOk = false;
		dispose();
	}

	public boolean showDialog() {
		setVisible(true);

		return isOk;
	}

	private void setDescription() {
		String description = String.format(resource.getString("CopyTable.description"), databaseName, tableName,
				(String) cbDestinationDatabase.getSelectedItem(), txtTableName.getText());
		lblDescription.setText(description);
	}
}
