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
 * DataTab.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 4 mar. 2019, 10:39:35 
 */

package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.gui.DatabaseElement;
import ar.com.dcbarrientos.mysqlgui.gui.Ventana;
import ar.com.dcbarrientos.mysqlgui.model.TableModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class DataTab extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	public String title = resource.getString("DataTab.title");

	private JLabel tableDataDescription;

	private JScrollPane scrollPane;
	private JTable table;
	private TableModel tableModel;
	private String[] tableHeaders;
	private Vector<Object[]> data;

	private JToolBar toolBar;
	private JButton jbPrimero;
	private JButton jbAnterior;
	private JButton jbSiguiente;
	private JButton jbUltimo;
	private JButton jbInsertar;
	private JButton jbBorrar;
	private JButton jbEditar;
	private JButton jbAceptar;
	private JButton jbCancelar;
	private JButton jbRefresh;
	private JCheckBox jcLimite;
	private JTextField txtLimiteInferior;
	private JTextField txtLimiteCount;
	private JButton jbOk;

	public DataTab() {
		super();
		initComponents();
	}
	
	public DataTab(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		tableDataDescription = new JLabel("");
		tableDataDescription.setOpaque(true);
		tableDataDescription.setBackground(Color.BLACK);
		tableDataDescription.setForeground(Color.WHITE);
		add(tableDataDescription, BorderLayout.NORTH);

		table = new JTable();

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		add(scrollPane, BorderLayout.CENTER);

		toolBar = new JToolBar();

		jbPrimero = new JButton();
		jbPrimero.setIcon(new ImageIcon(DataTab.class.getResource("/images/Primero.gif")));
		jbPrimero.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				table.setRowSelectionInterval(0, 0);
			}
		});
		toolBar.add(jbPrimero);

		jbAnterior = new JButton();
		jbAnterior.setIcon(new ImageIcon(DataTab.class.getResource("/images/Anterior.gif")));
		jbAnterior.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow();
				
				if (selectedRow > 0) {
					table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
				}
			}
		});
		toolBar.add(jbAnterior);

		jbSiguiente = new JButton();
		jbSiguiente.setIcon(new ImageIcon(DataTab.class.getResource("/images/Siguiente.gif")));
		jbSiguiente.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow();

				if (selectedRow < tableModel.getRowCount() - 1) {
					table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
				}
			}
		});
		toolBar.add(jbSiguiente);

		jbUltimo = new JButton();
		jbUltimo.setIcon(new ImageIcon(DataTab.class.getResource("/images/Ultimo.gif")));
		jbUltimo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				table.setRowSelectionInterval(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
			}
		});
		toolBar.add(jbUltimo);

		toolBar.addSeparator();

		jbInsertar = new JButton();
		jbInsertar.setIcon(new ImageIcon(DataTab.class.getResource("/images/InsertarRegistro.gif")));
		toolBar.add(jbInsertar);

		jbBorrar = new JButton();
		jbBorrar.setIcon(new ImageIcon(DataTab.class.getResource("/images/DeleteRecord.gif")));
		toolBar.add(jbBorrar);

		jbEditar = new JButton();
		jbEditar.setIcon(new ImageIcon(DataTab.class.getResource("/images/EditRecord.gif")));
		toolBar.add(jbEditar);

		jbAceptar = new JButton();
		jbAceptar.setIcon(new ImageIcon(DataTab.class.getResource("/images/AceptarEdit.gif")));
		toolBar.add(jbAceptar);

		jbCancelar = new JButton();
		jbCancelar.setIcon(new ImageIcon(DataTab.class.getResource("/images/CancelarEdit.gif")));
		toolBar.add(jbCancelar);

		jbRefresh = new JButton();
		jbRefresh.setIcon(new ImageIcon(DataTab.class.getResource("/images/RefreshData.gif")));
		toolBar.add(jbRefresh);

		toolBar.addSeparator();

		jcLimite = new JCheckBox(resource.getString("DataTab.limit"));
		jcLimite.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				loadData();
			}
		});
		toolBar.add(jcLimite);

		txtLimiteInferior = new JTextField();
		txtLimiteInferior.setText("0");
		toolBar.add(txtLimiteInferior);

		txtLimiteCount = new JTextField();
		txtLimiteCount.setText("100");
		toolBar.add(txtLimiteCount);

		jbOk = new JButton(resource.getString("General.OK"));
		jbOk.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				loadData();
			}
		});
		toolBar.add(jbOk);

		add(toolBar, BorderLayout.SOUTH);
	}

	protected void loadData() {
		if (selectedDB != null && selectedElement != null) {
			
			String sql = "Select * from " + selectedDB + "." + selectedElement;
			if(jcLimite.isSelected() && txtLimiteCount.getText().length() > 0 && txtLimiteInferior.getText().length() > 0)
				sql += " limit " + txtLimiteCount.getText() + " offset " + txtLimiteInferior.getText();
			sql += ";";
			
			
			Query query = new Query(database);
			query.executeQuery(sql);

			tableHeaders = query.getColumnNames();
			data = query.getDataAsObjectVector();
			tableDataDescription.setText(query.getRowCount() + " " + resource.getString("DataTab.rows"));

			tableModel = new TableModel(data, tableHeaders);
			table.setModel(tableModel);
		}
	}

}
