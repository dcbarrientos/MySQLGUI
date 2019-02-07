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
 * DatabaseTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 6 feb. 2019, 11:30:29 
 */

package ar.com.dcbarrientos.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ar.com.dcbarrientos.db.Database;
import ar.com.dcbarrientos.db.Query;
import ar.com.dcbarrientos.gui.DatabaseElement;
import ar.com.dcbarrientos.gui.Ventana;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class DatabaseTab extends DatabaseElement{
	private static final long serialVersionUID = 1L;
	
	public String title = resource.getString("DatabaseTab.title");
	
	private final int COLUMN_COUNT = 8;
	private Vector<String[]> datos;
	
	private String selectedDB;
	
	private JLabel lblNewLabel;
	private JToolBar toolBar;
	private JButton jbViewData;
	private JButton jbShowTableProperties;
	private JButton jbInsertRecord;
	private JButton jbEmptyTable;
	private JButton jbDropTable;
	private JButton jbCopyTable;
	private JScrollPane scrollPane;
	private JTable table;

	public DatabaseTab(Ventana ventana, Database database) {
		super(ventana, database);
		
		initComponents();
	}
	
	private void initComponents() {
		
		setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setOpaque(true);
		add(lblNewLabel, BorderLayout.NORTH);
		
		toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		add(toolBar, BorderLayout.WEST);
		
		jbViewData = new JButton("New button");
		toolBar.add(jbViewData);
		
		jbShowTableProperties = new JButton("New button");
		toolBar.add(jbShowTableProperties);
		
		jbInsertRecord = new JButton("New button");
		toolBar.add(jbInsertRecord);
		
		jbEmptyTable = new JButton("New button");
		toolBar.add(jbEmptyTable);
		
		jbDropTable = new JButton("New button");
		toolBar.add(jbDropTable);
		
		jbCopyTable = new JButton("New button");
		toolBar.add(jbCopyTable);
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setModel(getModel());
		scrollPane.setViewportView(table);
	}
	
	private TableModel getModel() {
		TableModel model = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return datos.get(rowIndex)[columnIndex];
			}
			
			@Override
			public int getRowCount() {
				return datos.size();
			}
			
			@Override
			public int getColumnCount() {
				return COLUMN_COUNT;
			}
		};
		
		return model;
	}
	
	private void loadData() {
		datos = new Vector<String[]>();
		
		Query query = new Query(database);
		String sql = "SHOW TABLE STATUS FROM `"+ selectedDB + "`;";
		query.executeQuery(sql);
		ventana.addMessage(sql);
		
		String[] fila;
		while(query.next()) {
			fila = new String[COLUMN_COUNT];
			fila[0] = query.getString("Name");
			fila[1] = query.getString("Rows");
			fila[2] = Integer.toString(query.getInt("Data_length") + query.getInt("Index_length"));
			fila[3] = query.getString("Create_time");
			fila[4] = query.getString("Update_time");
			fila[5] = query.getString("Engine");
			fila[6] = query.getString("Comment");
			fila[7] = resource.getString("DatabaseTab.tipoTabla");
			datos.add(fila);
		}
		
		query.close();
	}
	
	public void setSelectedDatabase(String databaseName) {
		this.selectedDB = databaseName;
		refresh();
	}

	@Override
	public void refresh() {
		loadData();
		this.repaint();
	}

}
