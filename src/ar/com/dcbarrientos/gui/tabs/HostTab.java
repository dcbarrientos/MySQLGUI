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
 * HostTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 4 feb. 2019, 11:35:43 
 */

package ar.com.dcbarrientos.gui.tabs;

import ar.com.dcbarrientos.db.Database;
import ar.com.dcbarrientos.gui.DatabaseElement;
import ar.com.dcbarrientos.gui.Ventana;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JTabbedPane;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class HostTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;

	public final String title = "Host";
	
	private VariablesTab variables;
	
	public HostTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		initComponents();
	}
	
	public void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setOpaque(true);
		add(lblNewLabel, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		variables = new VariablesTab(ventana, database);
		tabbedPane.add(variables.TITLE, variables);
		
		
		add(tabbedPane, BorderLayout.CENTER);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
