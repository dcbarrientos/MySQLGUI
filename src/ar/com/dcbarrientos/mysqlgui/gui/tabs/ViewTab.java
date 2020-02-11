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
 * ViewTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 1 nov. 2019, 10:35:56 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.ScriptToDatabaseDialog;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.view.ViewDDLTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.view.ViewDefinitionTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.view.ViewInfoTab;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class ViewTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("ViewTab.title");

	public static final int INFO_INDEX = 0;
	public static final int DEFINITION_INDEX = 1;
	public static final int DDL_INDEX = 2;
	public static final int DATA_INDEX = 3;

	public boolean isNew;
	private String sqlCreateView;

	private JTabbedPane tabbedPanel;
	private JPanel namePanel;
	private ViewInfoTab infoTab;
	private ViewDefinitionTab definitionTab;
	private ViewDDLTab ddlTab;
	private DataTab dataTab;
	// private ViewDataTab dataTab;
	private JLabel lblName;
	private JTextField txtViewName;
	private JPanel panel;
	private JPanel buttonPanel;
	private JButton btnApply;
	private JButton btnCancel;

	public boolean viewChanged;
	private String originalName;

	private Vector<DatabaseElement> tabList;

	public ViewTab(Ventana ventana, Database database, boolean isNew) {
		super(ventana, database);
		this.isNew = isNew;
		this.viewChanged = false;

		initComponents();
		clean();
	}

	private void initComponents() {
		setLayout(new BorderLayout(0, 0));

		tabList = new Vector<DatabaseElement>();

		tabbedPanel = new JTabbedPane();
		tabbedPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane jtp = (JTabbedPane) e.getSource();
				if (jtp.getSelectedIndex() == DDL_INDEX) {
					ddlTab.updateDDL();
				}
			}
		});
		add(tabbedPanel, BorderLayout.CENTER);

		infoTab = new ViewInfoTab(ventana, database);
		tabList.add(infoTab);
		tabbedPanel.add(infoTab);

		definitionTab = new ViewDefinitionTab(ventana, database, this);
		tabList.add(definitionTab);
		tabbedPanel.add(definitionTab);

		ddlTab = new ViewDDLTab(ventana, database, this);
		tabList.add(ddlTab);
		tabbedPanel.add(ddlTab);

		dataTab = new DataTab(ventana, database);
		tabList.add(dataTab);
		tabbedPanel.add(dataTab);

		tabbedPanel.setTitleAt(INFO_INDEX, "Info");
		tabbedPanel.setTitleAt(DEFINITION_INDEX, "Definition");
		tabbedPanel.setTitleAt(DDL_INDEX, "DDL");
		tabbedPanel.setTitleAt(DATA_INDEX, "Data");

		namePanel = new JPanel();
		namePanel.setAlignmentY(0.1f);
		add(namePanel, BorderLayout.NORTH);

		lblName = new JLabel("Name:");

		txtViewName = new JTextField();
		txtViewName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
//				definitionTab.setViewName(txtViewName.getText());
				ddlTab.updateDDL();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		txtViewName.setColumns(10);
		GroupLayout gl_namePanel = new GroupLayout(namePanel);
		gl_namePanel.setHorizontalGroup(gl_namePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_namePanel.createSequentialGroup().addContainerGap().addComponent(lblName)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(txtViewName,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(308, Short.MAX_VALUE)));
		gl_namePanel.setVerticalGroup(gl_namePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_namePanel.createSequentialGroup().addGap(5)
						.addGroup(gl_namePanel.createParallelGroup(Alignment.BASELINE, false).addComponent(lblName)
								.addComponent(txtViewName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		namePanel.setLayout(gl_namePanel);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		btnApply = new JButton(resource.getString("General.apply"));
		btnApply.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				createView();
			}
		});
		buttonPanel.add(btnApply);

		btnCancel = new JButton(resource.getString("General.cancel"));
		buttonPanel.add(btnCancel);

		add(buttonPanel, BorderLayout.SOUTH);

		panel = new JPanel();
		add(panel, BorderLayout.WEST);
	}

	private void createView() {
		if (isViewChanged()) {
			ScriptToDatabaseDialog std = new ScriptToDatabaseDialog(ventana, database);
			std.setSQLSript(definitionTab.getDefinition());

			if (std.showDialog()) {
				if (isNew) {
					JOptionPane.showMessageDialog(null, resource.getString("ViewTab.newViewCreaated"),
							resource.getString("ViewTab.name"), JOptionPane.INFORMATION_MESSAGE);
					ventana.setSelectedView(selectedDB, selectedElement);
				} else
					JOptionPane.showMessageDialog(null, resource.getString("TableTab.changesApplied"),
							resource.getString("ViewTab.name"), JOptionPane.INFORMATION_MESSAGE);
				refresh();
			}
		} else {
			JOptionPane.showMessageDialog(null, resource.getString("TableTab.noChangeDetected"),
					resource.getString("ViewTab.name"), JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private void clean() {

	}

	public void setSelectedElement(String selectedDb, String selectedElement, int type) {
		this.selectedDB = selectedDb;
		this.selectedElement = selectedElement;
		this.selectedType = Database.VIEW;

		for (DatabaseElement element : tabList) {
			element.setSelectedElement(selectedDB, selectedElement, selectedType);
		}

		this.refresh();
	}

	public void setEmptyElement(String selectedDB) {
		isNew = true;
		loadData();
		for(DatabaseElement element : tabList) {
			element.setSelectedDatabase(selectedDB);
		}
	}

	protected void loadData() {
		if (isNew) {
			txtViewName.setText(resource.getString("ViewTab.newView"));
			sqlCreateView = "";
			originalName = "";
			tabbedPanel.setSelectedIndex(DEFINITION_INDEX);
		} else {
			txtViewName.setText(selectedElement);
			originalName = selectedElement;

			String sql = String.format(Query.SQL_SHOW_CREATE_TABLE, selectedDB, selectedElement);
			Query query = new Query(database);
			if (query.executeQuery(sql)) {
				query.next();
				sqlCreateView = query.getString("Create view");
			}
			query.close();
		}

		if (sqlCreateView.length() > 0) {
			ddlTab.setSQL(sqlCreateView);
			definitionTab.setSQL(sqlCreateView);
//		definitionTab.setViewName(txtViewName.getText());
			// dataTab.setSQL(sqlCreateView);
		}
	}

	public String getDefinition() {
		return definitionTab.getDefinition();
	}

	public String getViewName() {
		return txtViewName.getText();
	}

	public void updateDLL() {
		ddlTab.updateDDL();
	}

	private boolean isViewChanged() {
		if (definitionTab.isViewChanged())
			return true;
		if (!originalName.equals(txtViewName.getText()))
			return true;

		return false;
	}

	public void refresh() {
		loadData();

		for (DatabaseElement element : tabList) {
			element.refresh();
		}

		this.revalidate();
		this.repaint();
	}
}
