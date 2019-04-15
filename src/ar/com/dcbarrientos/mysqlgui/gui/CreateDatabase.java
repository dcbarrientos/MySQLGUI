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
 * CreateDatabase.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 20 feb. 2019, 10:47:54 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class CreateDatabase extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel lblName;
	private JTextField textName;
	private JLabel lblCollaiton;
	private JComboBox<String> cbCollation;
	private JLabel lblDefault;
	private JLabel lblCreate;
	private JScrollPane scrollPane;
	private JLabel lblDefaultCollation;
	private JButton btnCreate;
	private JButton btnCancel;
	private JTextArea textCreate;

	private Ventana ventana;
	private Database database;
	private ResourceBundle resource;
	private String defaultCollation;
	private boolean createdDB;

	public CreateDatabase(Ventana ventana, Database database) {
		this.ventana = ventana;
		this.database = database;
		this.resource = ventana.resource;
		this.defaultCollation = getDefaultCollation();
		this.createdDB = false;

		initComponents();
	}

	private void initComponents() {
		setPreferredSize(new Dimension(450, 270));
		setSize(new Dimension(450, 270));
		setTitle(resource.getString("CreateDatabaseDialog.title"));

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btnCreate = new JButton("Create");
		btnCreate.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnCreateMouseClicked(e);
			}
		});
		btnCreate.setPreferredSize(new Dimension(89, 23));

		btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnCancelMouseClicked(e);
			}
		});
		btnCancel.setPreferredSize(new Dimension(89, 23));

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		lblName = new JLabel("Name:");

		textName = new JTextField();
		textName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				textCreate.setText(getSQLText());
			}
		});
		textName.setColumns(10);

		lblCollaiton = new JLabel("Collaiton:");

		cbCollation = new JComboBox<String>();
		cbCollation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textCreate != null)
					textCreate.setText(getSQLText());
			}
		});
		cbCollation.setModel(new DefaultComboBoxModel<>(getCollations()));
		cbCollation.setSelectedItem(defaultCollation);

		lblDefault = new JLabel("Default:");

		lblCreate = new JLabel("CREATE");

		scrollPane = new JScrollPane();

		lblDefaultCollation = new JLabel(defaultCollation);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup().addComponent(lblDefault)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblDefaultCollation, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
						.addComponent(lblCreate)
						.addGroup(gl_panel.createSequentialGroup()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblCollaiton)
										.addComponent(lblName))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(textName, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
										.addComponent(cbCollation, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblName).addComponent(textName,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblCollaiton).addComponent(
						cbCollation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblDefault)
						.addComponent(lblDefaultCollation))
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblCreate)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE).addGap(20)));

		textCreate = new JTextArea();
		textCreate.setFont(new Font("Monospaced", Font.PLAIN, 10));
		scrollPane.setViewportView(textCreate);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);

		setLocationRelativeTo(null);
	}

	private String[] getCollations() {
		String[] collations = null;
		String sql = "SELECT * FROM `information_schema`.`COLLATIONS`";
		ventana.addMessage(sql);

		Query query = new Query(database);
		if (query.executeQuery(sql)) {
			collations = new String[query.getRowCount()];
			int i = 0;
			while (query.next()) {
				collations[i] = query.getString(1);
				i++;
			}
			query.close();

		}

		return collations;
	}

	private String getDefaultCollation() {
		String defaultCollation = "";
		String sqlTxt = "SHOW VARIABLES LIKE 'collation_server';";
		ventana.addMessage(sqlTxt);

		Query query = new Query(database);
		if (query.executeQuery(sqlTxt))
			query.next();
		defaultCollation = query.getString("Value");

		query.close();

		return defaultCollation;
	}

	private void btnCreateMouseClicked(MouseEvent e) {
		ventana.addMessage(getSQLText());

		Query query = new Query(database);
		if (query.executeUpdate(getSQLText()) < 0) {
			createdDB = false;
			JOptionPane.showMessageDialog(null, query.errorCode + ": " + query.errorMsg,
					resource.getString("CreateDatabaseDialog.errortitle"), JOptionPane.ERROR_MESSAGE);
		} else {
			createdDB = true;
		}

		query.close();
		if (createdDB)
			this.setVisible(false);
	}

	private void btnCancelMouseClicked(MouseEvent e) {
		this.dispose();
	}

	public boolean showDialog() {
		this.setModal(true);
		this.setVisible(true);

		return createdDB;
	}
	
	private String getSQLText() {
		return "CREATE DATABASE `" + textName.getText() + "` /*!40100 COLLATE '" + cbCollation.getSelectedItem()
		+ "' */";
	}
}
