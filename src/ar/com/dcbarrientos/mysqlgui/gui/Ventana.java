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
 * Ventana.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 1 feb. 2019, 19:38:42 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import ar.com.dcbarrientos.mysqlgui.db.Database;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Ventana extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ResourceBundle resource;
	private Database database;
	private MainPanel mainPanel;
	private JDesktopPane desktop;

	public Ventana(ResourceBundle resource) {
		super();

		this.resource = resource;
		initComponents();
	}

	private void initComponents() {
		getContentPane().setLayout(new BorderLayout(0, 0));

		desktop = new JDesktopPane();
		desktop.setLayout(new BorderLayout());
		getContentPane().add(desktop, BorderLayout.CENTER);

		/* Status bar */
		JPanel statusBar = new JPanel();
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		statusBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("New label");
		statusBar.add(lblNewLabel);

		/* Menu bar */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);

		JMenuItem mnuConnect = new JMenuItem("Connect");
		mnuFile.add(mnuConnect);

		JMenuItem mnuClose = new JMenuItem("Close");
		mnuFile.add(mnuClose);

		JSeparator mnuFileSeparator = new JSeparator();
		mnuFile.add(mnuFileSeparator);

		JMenuItem mnuExit = new JMenuItem("Exit");
		mnuFile.add(mnuExit);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mnTools.add(mntmRefresh);
		
		JSeparator separator = new JSeparator();
		mnTools.add(separator);
		
		JMenuItem mntmCreateDatabase = new JMenuItem("Create database...");
		mnTools.add(mntmCreateDatabase);
		
		JMenuItem mntmCreateTable = new JMenuItem("Create table...");
		mnTools.add(mntmCreateTable);
		
		JMenuItem mntmDropDatabase = new JMenuItem("Drop database...");
		mnTools.add(mntmDropDatabase);
		
		JMenuItem mntmDropTable = new JMenuItem("Drop table...");
		mnTools.add(mntmDropTable);
		
		JSeparator separator_1 = new JSeparator();
		mnTools.add(separator_1);
		
		JMenuItem mntmUserManager = new JMenuItem("User Manager...");
		mnTools.add(mntmUserManager);

		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton btnConnect = new JButton("");
		btnConnect.setToolTipText("Connect");
		btnConnect.setIcon(new ImageIcon(Ventana.class.getResource("/images/Connection.GIF")));
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				connect();
			}
		});
		toolBar.add(btnConnect);

		JButton btnClose = new JButton("");
		btnClose.setToolTipText("Close connections");
		btnClose.setIcon(new ImageIcon(Ventana.class.getResource("/images/Close.GIF")));
		toolBar.add(btnClose);

		toolBar.addSeparator();

		JButton btnCreateDatabase = new JButton("");
		btnCreateDatabase.setToolTipText("Create database");
		btnCreateDatabase.setIcon(new ImageIcon(Ventana.class.getResource("/images/CreateDatabase.GIF")));
		btnCreateDatabase.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnCreateDatabaseMouseClicked(e);
			}
		});
		toolBar.add(btnCreateDatabase);

		JButton btnDeleteDatabase = new JButton("");
		btnDeleteDatabase.setToolTipText("Drop database");
		btnDeleteDatabase.setIcon(new ImageIcon(Ventana.class.getResource("/images/DropDatabase.GIF")));
		btnDeleteDatabase.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnDeleteDatabaseMouseClicked(e);
			}
		});
		
		JButton btnCreateTable = new JButton("");
		btnCreateTable.setToolTipText("Create table");
		btnCreateTable.setIcon(new ImageIcon(Ventana.class.getResource("/images/CreateTable.GIF")));
		toolBar.add(btnCreateTable);
		toolBar.add(btnDeleteDatabase);
		
		JButton btnDropTable = new JButton("");
		btnDropTable.setToolTipText("Drop table");
		btnDropTable.setIcon(new ImageIcon(Ventana.class.getResource("/images/DropTable.GIF")));
		toolBar.add(btnDropTable);
		
		toolBar.addSeparator();
		
		JButton btnRefresh = new JButton("");
		btnRefresh.setToolTipText("Refresh");
		btnRefresh.setIcon(new ImageIcon(Ventana.class.getResource("/images/Refresh.GIF")));
		toolBar.add(btnRefresh);
		
		JButton btnUserManager = new JButton("");
		btnUserManager.setToolTipText("User manager");
		btnUserManager.setIcon(new ImageIcon(Ventana.class.getResource("/images/UserManager.GIF")));
		toolBar.add(btnUserManager);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void connect() {
		Login login = new Login(resource);
		Database database = login.showDialog();
		if (database != null) {
			this.database = database;
			mainPanel = new MainPanel(this);
			mainPanel.setConnection(this.database);
			;
			desktop.add(mainPanel, BorderLayout.CENTER);
			desktop.revalidate();
			desktop.repaint();
		}
	}

	public void addMessage(String msg) {
		mainPanel.addMessage(msg);

	}
	
	public void selectRoot() {
		mainPanel.selectRoot();
	}

	public void setSelectedDatabase(String selectedDb) {
		mainPanel.setSelectedDatabase(selectedDb, true);
	}

	public void setSelectedTable(String selectedDB, String selectedTable) {
		mainPanel.setSelectedTable(selectedDB, selectedTable);
	}
	
	public void setSelectedView(String selectedDB, String selectedView) {
		mainPanel.setSelectedView(selectedDB, selectedView);
	}
	
//	public void setSelectedStoredProcedure(String selectedDB, String selectedStoredProcedure) {
//		mainPanel.setselectedStoredProcedure(selectedDB, selectedStoredProcedure);
//	}
//	
//	public void setSelectedFunction(String selectedDB, String selectedFunction) {
//		mainPanel.setSelectedFunction(selectedDB, selectedFunction);
//	}
	
	public void setTreeSelection(String db, String element, int type, boolean show) {
		mainPanel.setTreeSelection(db, element, type, show);
	}
	
	public void setTreeSelection(String db, boolean show) {
		mainPanel.setTreeSelection(db, show);
	}

	public void refresh() {
		mainPanel.refresh();
	}

	private void btnCreateDatabaseMouseClicked(MouseEvent e) {
		CreateDatabase cdb = new CreateDatabase(this, database);
		if (cdb.showDialog())
			refresh();
	}

	private void btnDeleteDatabaseMouseClicked(MouseEvent e) {
		//TODO: mover esto a mainPanel
//		if (database.getSelectedDatabase().length() > 0) {
//			DeleteDatabase dd = new DeleteDatabase(this, database);
//			dd.deleteDatabase(database.getSelectedDatabase());
//			mainPanel.selectDatabase("", false);
//			refresh();
//		} else {
//			JOptionPane.showMessageDialog(null, resource.getString("DeleteDatabase.selectDB"),
//					resource.getString("DeleteDatabase.title"), JOptionPane.ERROR_MESSAGE);
//		}
		mainPanel.deleteDatabase();
	}
	
	public void selectTableTab(int index) {
		mainPanel.selectTableTab(index);
	}
	
	public void showTableData(String databaseName, String tableName) {
		mainPanel.showTableData(databaseName, tableName);
	}
	
}
