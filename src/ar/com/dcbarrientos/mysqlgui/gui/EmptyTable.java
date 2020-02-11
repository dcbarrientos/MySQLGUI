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
 * EmptyTable.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 5 mar. 2019, 18:21:31 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.Dimension;
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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class EmptyTable extends JDialog {
	private static final long serialVersionUID = 1L;

	private final int DELETE_OPTION = 0;

	private Ventana ventana;
	private ResourceBundle resource;
	private Database database;
	private String databaseName;
	private String tableName;
	private boolean isOk;

	private JPanel panel;
	private JLabel lblDescription;
	private JLabel lblOperation;
	private JComboBox<String> cbOperation;
	private JButton btnOk;
	private JButton btnCancel;

	public EmptyTable(Ventana ventana, Database database, String databaseName, String tableName) {
		this.ventana = ventana;
		this.resource = ventana.resource;
		this.database = database;
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.isOk = false;

		initComponents();
	}

	private void initComponents() {
		setPreferredSize(new Dimension(450, 160));

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btnOk = new JButton(resource.getString("General.OK"));
		btnOk.setPreferredSize(new Dimension(100, 23));
		btnOk.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnOkMouseClicked(e);
			}
		});

		btnCancel = new JButton(resource.getString("General.cancel"));
		btnCancel.setPreferredSize(new Dimension(100, 23));
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				isOk = false;
				dispose();
			}
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(17, Short.MAX_VALUE)));

		String txtDescription = String.format(resource.getString("EmptyTable.lblDescription"), databaseName, tableName);
		lblDescription = new JLabel(txtDescription);

		lblOperation = new JLabel(resource.getString("EmptyTable.lblOperation"));

		cbOperation = new JComboBox<String>();
		cbOperation.addItem(resource.getString("EmptyTable.option1"));
		cbOperation.addItem(resource.getString("EmptyTable.option2"));

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblDescription)
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblOperation)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(cbOperation, 0, 332, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap().addComponent(lblDescription)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblOperation).addComponent(
						cbOperation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(65, Short.MAX_VALUE)));

		setTitle(resource.getString("EmptyTable.title"));
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setModal(true);
	}

	public boolean showDialog() {
		setVisible(true);

		return isOk;
	}

	private void btnOkMouseClicked(MouseEvent e) {
		String warning = String.format(resource.getString("EmptyTable.warning"), databaseName, tableName,
				(String) cbOperation.getSelectedItem());
		if (JOptionPane.showConfirmDialog(null, warning, this.getTitle(),
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			
			Query query = new Query(database);
			String sql = "";
			if (cbOperation.getSelectedIndex() == DELETE_OPTION)
				sql = Query.SQL_DELETE_TABLE;
			else
				sql = Query.SQL_TRUNCATE_TABLE;

			sql = String.format(sql, databaseName, tableName);
			ventana.addMessage(sql);
			if (query.executeUpdate(sql) < 0) {
				JOptionPane.showMessageDialog(null, query.errorCode + ": " + query.errorMsg, this.getTitle(),
						JOptionPane.ERROR_MESSAGE);
			} else {
				isOk = true;
			}
			query.close();

			if (isOk)
				dispose();
		}
	}
}
