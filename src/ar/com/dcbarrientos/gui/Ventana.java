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

package ar.com.dcbarrientos.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

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

import ar.com.dcbarrientos.db.Database;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
		
		/*Status bar*/
		JPanel statusBar = new JPanel();
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		statusBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel = new JLabel("New label");
		statusBar.add(lblNewLabel);
		
		/*Menu bar*/
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
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				connect();
			}
		});
		toolBar.add(btnConnect);
		
		JButton btnClose = new JButton("Close");
		toolBar.add(btnClose);
		
		toolBar.addSeparator();

		JButton btnCreateDatabase= new JButton("Create Database");
		toolBar.add(btnCreateDatabase);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void connect() {
		Login login = new Login(resource);
		Database database = login.showDialog();
		if(database != null) {
			this.database = database;
			mainPanel = new MainPanel(this);
			mainPanel.setConnection(this.database);;
			desktop.add(mainPanel, BorderLayout.CENTER);
			desktop.revalidate();
			desktop.repaint();
		}
	}
	
	public void addMessage(String msg) {
		mainPanel.addMessage(msg);
		
	}
	
	public void selectDatabase(String selectedDb) {
		//TODO: cuando se hace click en una base de datos en tree.
		
		mainPanel.selectDatabase(selectedDb, true);
	}
	
	public void selectTable(String selectedDB, String selectedTable) {
		//TODO: cuando se hace click en una table en tree.
		
		mainPanel.selectedTable(selectedDB, selectedTable);
	}
	
	public void refresh() {
		mainPanel.refresh();
	}
}
