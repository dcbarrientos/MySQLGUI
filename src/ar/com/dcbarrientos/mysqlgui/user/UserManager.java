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
 * UserManager.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 15 nov. 2019, 11:53:21 
 */

package ar.com.dcbarrientos.mysqlgui.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class UserManager extends JFrame{
	private static final long serialVersionUID = 1L;
	
	JTabbedPane mainPane;
	JScrollPane userScroll;
	JTree userTree;
	JPanel botonera;
	JButton btnCerrar;
	
	public UserManager() {
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		
		userTree = new JTree();
		userScroll = new JScrollPane();
		userScroll.setViewportView(userTree);
		add(userScroll, BorderLayout.WEST);
		
		mainPane = new JTabbedPane();
		add(mainPane, BorderLayout.CENTER);
		
		botonera = new JPanel();
		botonera.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		btnCerrar = new JButton("Cerrar");
		botonera.add(btnCerrar);
		
		add(botonera, BorderLayout.SOUTH);
	}
}
