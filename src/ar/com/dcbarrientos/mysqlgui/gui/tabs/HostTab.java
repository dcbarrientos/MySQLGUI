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

package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.host.ProcessListTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.host.StatisticsTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.host.StatusTab;
import ar.com.dcbarrientos.mysqlgui.gui.tabs.host.VariablesTab;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class HostTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;

	public final String title = "Host";
	
	private JLabel hostDescription;
	private VariablesTab variables;
	private ProcessListTab process;
	private StatusTab status;
	private StatisticsTab statistics;
	private Vector<DatabaseElement> tabList;
	
	
	public HostTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		initComponents();
		//refresh();
	}
	
	public void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		hostDescription = new JLabel("New label");
		hostDescription.setBackground(Color.BLACK);
		hostDescription.setForeground(Color.WHITE);
		hostDescription.setOpaque(true);
		hostDescription.setText(" " + database.user + " running MySQL - version " + database.getVersion());
		add(hostDescription, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		tabList = new Vector<DatabaseElement>(); 
		
		variables = new VariablesTab(ventana, database);
		tabList.add(variables);
		tabbedPane.add(variables.title + " (" + variables.variablesCount + ")", variables);
		
		process = new ProcessListTab(ventana, database);
		tabList.add(process);
		tabbedPane.add(process.title + " (" + process.processCount + ")", process);
		
		status = new StatusTab(ventana, database);
		tabList.add(status);
		tabbedPane.add(status.title + " (" + status.statusCount + ")", status);
		
		statistics = new StatisticsTab(ventana, database);
		tabList.add(statistics);
		tabbedPane.add(statistics.title + "(" + statistics.statisticsCount + ")", statistics);
		
		add(tabbedPane, BorderLayout.CENTER);

	}

	@Override
	public void refresh() {
		for(DatabaseElement element: tabList) {
			element.refresh();
		}
	}

}
