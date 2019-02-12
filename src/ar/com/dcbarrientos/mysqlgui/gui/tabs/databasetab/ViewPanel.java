/*
 *  Copyright (C) 2019 Luna
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
 * ViewPanel.java
 *
 * Description:	    <Descripcion>
 * @author			Luna
 *
 * Created on 12 feb. 2019, 16:46:00 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs.databasetab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;

/**
 * @author Luna
 *
 */
public class ViewPanel extends DatabaseElement{
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("ViewPanel.title");
	private final int COLUMN_COUNT = 8;
	
	private JLabel titleLabel;
	private JScrollPane scrollPane;
	private JTable table;
	private TableModel tableModel;
	
	private String[] columnHeaders;
	private Vector<String[]> datos;
	
	public ViewPanel(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
	}
	
	private void initComponents() {
		//TODO: CARGAR LOS NOMBRES DE LAS COLUMNAS
		
		setLayout(new BorderLayout());
		
		titleLabel = new JLabel("New label");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBackground(Color.BLACK);
		titleLabel.setOpaque(true);
		add(titleLabel, BorderLayout.NORTH);
		
		table = new JTable();
		tableModel = new TableModel();
		
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		add(scrollPane, BorderLayout.CENTER);
		
	}
	
	private void loadData() {
		Query query = new Query(database);
		//TODO: modificar la sql para que solo muestre las vistas
		//TODO: corregir el panel de tablas para que solo muestre tablas.
		
		String sql = "SHOW PROCEDURE STATUS WHERE `Db`='" + selectedDB + "';";
		query.executeQuery(sql);
		
		datos = new Vector<String[]>();
		String[] fila = new String[COLUMN_COUNT];
		
		while(query.next()) {
			
			
			datos.add(fila);
		}
		
		query.close();
	}
	
	public void setSelectedDatabase(String databaseName) {
		selectedDB = databaseName;
		
		refresh();
	}

	@Override
	public void refresh() {
		loadData();
		revalidate();
		repaint();
	}

}
