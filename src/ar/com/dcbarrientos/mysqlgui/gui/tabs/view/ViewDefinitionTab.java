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
 * ViewDefinitionTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 7 nov. 2019, 11:19:33 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs.view;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.fife.ui.rtextarea.RTextScrollPane;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.View;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.ViewTab;
import ar.com.dcbarrientos.mysqlgui.tools.SQLTextArea;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class ViewDefinitionTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JLabel lblAlgorithm;
	private JComboBox<String> cmbAlgorithm;
	private JLabel lblDefiner;
	private JTextField txtDefiner;
	private JLabel lblSecurity;
	private JComboBox<String> cmbSecurity;
	private JLabel lblCheckOption;
	private JComboBox<String> cmbCheckOption;
	private JLabel lblAs;
	private RTextScrollPane scrollPane;
	private SQLTextArea txtSql;

	private String sql;
	private ViewTab viewTab;
//	private boolean isNew;

	private String originalAlgorithm;
	private String originalDefiner;
	private String originalSecurity;
	private String originalCheckOption;
	private String originalSql;

//	public ViewDefinitionTab(Ventana ventana, Database database, ViewTab viewTab, boolean isNew) {
	public ViewDefinitionTab(Ventana ventana, Database database, ViewTab viewTab) {
		super(ventana, database);
		this.viewTab = viewTab;
//		this.isNew = isNew;

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		add(panel, BorderLayout.CENTER);

		lblAlgorithm = new JLabel("Algorithm:");

		cmbAlgorithm = new JComboBox<String>();

		lblDefiner = new JLabel("Definer:");

		txtDefiner = new JTextField();
		txtDefiner.setColumns(10);

		lblSecurity = new JLabel("Security:");

		cmbSecurity = new JComboBox<String>();

		lblCheckOption = new JLabel("Check Option:");

		cmbCheckOption = new JComboBox<String>();

		lblAs = new JLabel("As:");

		scrollPane = new RTextScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblAlgorithm)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(cmbAlgorithm,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblDefiner).addGap(18)
										.addComponent(txtDefiner, GroupLayout.PREFERRED_SIZE, 262,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblSecurity)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(cmbSecurity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblCheckOption)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(cmbCheckOption,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblAs)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblAlgorithm).addComponent(
						cmbAlgorithm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblDefiner).addComponent(
						txtDefiner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblSecurity).addComponent(
						cmbSecurity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblCheckOption).addComponent(
						cmbCheckOption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAs).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
				.addContainerGap()));

		txtSql = new SQLTextArea();
		scrollPane.setViewportView(txtSql);
		panel.setLayout(gl_panel);

		initData();
	}

	private void initData() {
		cmbAlgorithm.addItem(View.ALGORITHM1);
		cmbAlgorithm.addItem(View.ALGORITHM2);
		cmbAlgorithm.addItem(View.ALGORITHM3);

		cmbSecurity.addItem(View.SECURITY1);
		cmbSecurity.addItem(View.SECURITY2);
		cmbSecurity.addItem(View.SECURITY3);

		cmbCheckOption.addItem(View.CHECK_OPTION1);
		cmbCheckOption.addItem(View.CHECK_OPTION2);
		cmbCheckOption.addItem(View.CHECK_OPTION3);

		txtDefiner.setText(database.getConnectionID());
	}

	protected void loadData() {
		if (viewTab.isNew) {
			cmbAlgorithm.setSelectedIndex(0);
			txtDefiner.setText(database.getConnectionID());
			cmbSecurity.setSelectedIndex(0);
			cmbCheckOption.setSelectedIndex(0);
			txtSql.setText("");
		} else {
			int index, index2;
			String header = sql.substring(0, sql.indexOf("AS"));
			String[] headers = header.split(" ");
			String value;
			for (int i = 0; i < headers.length; i++) {
				if (headers[i].indexOf("ALGORITHM=") >= 0) {
					value = headers[i].substring(headers[i].indexOf("=") + 1, headers[i].length());
					cmbAlgorithm.getModel().setSelectedItem(value);
				} else if (headers[i].indexOf("DEFINER=") >= 0) {
					value = headers[i].substring(headers[i].indexOf("=") + 1, headers[i].length());
					txtDefiner.setText(value);
				} else if (headers[i].equals("SQL")) {
					i += 2;
					cmbSecurity.setSelectedItem(headers[i]);
				}

			}

			// Consulta de la vista.
			index = sql.indexOf("AS") + 3;
			if (sql.lastIndexOf("CHECK OPTION") < 0)
				index2 = sql.length();
			else
				index2 = sql.lastIndexOf("WITH");

			txtSql.setText(database.format(sql.substring(index, index2)));

			if (sql.lastIndexOf("CHECK OPTION") >= 0) {
				String[] t = sql.substring(sql.lastIndexOf("WITH"), sql.length()).split(" ");
				cmbCheckOption.setSelectedItem(t[1]);
			}
		}

		originalAlgorithm = cmbAlgorithm.getSelectedItem().toString();
		originalDefiner = txtDefiner.getText();
		originalSecurity = cmbSecurity.getSelectedItem().toString();
		originalCheckOption = cmbCheckOption.getSelectedItem().toString();
		originalSql = txtSql.getText();
	}

	public String getDefinition() {
//		String definition = "USE `" + selectedDB + "`;\n";
		String definition = "CREATE OR REPLACE ";

		definition += " ALGORITHM = " + cmbAlgorithm.getSelectedItem();
		definition += " DEFINER = " + txtDefiner.getText();
		if (((String) cmbSecurity.getSelectedItem()).length() > 0) {
			definition += " SQL SECURITY " + cmbSecurity.getSelectedItem();
		}

		definition += " VIEW `" + selectedDB + "`.`" + viewTab.getViewName() + "`";

		definition += " AS " + txtSql.getText();

		if (((String) cmbCheckOption.getSelectedItem()).length() > 0) {
			definition += " WITH " + cmbCheckOption.getSelectedItem() + " CHECK OPTION";
		}

		return definition + ";";
	}

	public void setSQL(String sql) {
		this.sql = sql;
		loadData();
	}

	public boolean isViewChanged() {
		if (viewTab.isNew)
			return true;

		if (!originalAlgorithm.equals(cmbAlgorithm.getSelectedItem().toString()))
			return true;
		if (!originalDefiner.equals(txtDefiner.getText()))
			return true;
		if (!originalSecurity.equals(cmbSecurity.getSelectedItem().toString()))
			return true;
		if (!originalCheckOption.equals(cmbCheckOption.getSelectedItem().toString()))
			return true;
		if (!originalSql.equals(txtSql.getText()))
			return true;

		return false;
	}
}
