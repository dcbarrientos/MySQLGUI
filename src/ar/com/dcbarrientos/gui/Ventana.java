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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import ar.com.dcbarrientos.db.Database;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Ventana extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Database database;
	
	public Ventana(Database database) {
		super();
		this.database = database;
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane mainSplit = new JSplitPane();
		mainSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(mainSplit, BorderLayout.CENTER);
		
		JSplitPane secondarySplit = new JSplitPane();
		mainSplit.setLeftComponent(secondarySplit);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		secondarySplit.setRightComponent(tabbedPane);
		
		JScrollPane scrollPane = new JScrollPane();
		secondarySplit.setLeftComponent(scrollPane);
		
		DatabaseTree tree = new DatabaseTree(database);
		scrollPane.setViewportView(tree);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		mainSplit.setRightComponent(scrollPane_1);
		
		JTextArea textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel = new JLabel("New label");
		panel.add(lblNewLabel);
		initComponents();
	}
	
	private void initComponents() {
		
	}
}
