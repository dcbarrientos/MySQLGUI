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
 * ScriptToDatabaseDialog.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 16 sep. 2019, 11:49:16 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fife.ui.rtextarea.RTextScrollPane;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.tools.SQLTextArea;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class ScriptToDatabaseDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private Database database;
	private ResourceBundle resource;
	
	private boolean saved;
	private JPanel panel;
	private JLabel titulo;
	private JPanel botones;
	private JButton btnApply;
	private JButton btnCancel;
	private RTextScrollPane scrollPane;
	private SQLTextArea sqlTextArea;
	
	public ScriptToDatabaseDialog() {
		initComponents();
		
	}
	
	public ScriptToDatabaseDialog(Ventana ventana, Database database) {
		this.resource = ventana.resource;
		this.database = database;
		this.saved = false;
		
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);

		//JLabel con el título 
		titulo = new JLabel("Titulo");
		panel.add(titulo, BorderLayout.NORTH);
		
		//TextArea con el script sql.
		sqlTextArea = new SQLTextArea();
		sqlTextArea.setText("");
		
		scrollPane = new RTextScrollPane();
		scrollPane.setViewportView(sqlTextArea);
		scrollPane.setLineNumbersEnabled(true);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		//Botonera.
		botones = new JPanel();
		botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		btnApply = new JButton(resource.getString("ScriptToDatabaseDialog.btnApply"));
		btnApply.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnApplyMouseClicked(e);
			}
		});
		botones.add(btnApply);
		
		btnCancel = new JButton(resource.getString("ScriptToDatabaseDialog.btnCancel"));
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnCancelMouseClicked(e);
			}
		});
		botones.add(btnCancel);
		
		panel.add(botones, BorderLayout.SOUTH);
		
		titulo = new JLabel();
		setModal(true);
		setLocationRelativeTo(null);
	}

	private void btnCancelMouseClicked(MouseEvent e) {
		saved = false;
		dispose();
	}	
	
	private void btnApplyMouseClicked(MouseEvent e) {
		//TODO: transaccion para ejecutar la o las sql.
		Query query = new Query(database);
		if(query.executeUpdate(sqlTextArea.getText()) != Query.ERROR) {
			saved = true;
			dispose();			
		}else {
			JOptionPane.showMessageDialog(null, query.errorMsg, "Error sql", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setSQLSript(String sql) {
		sqlTextArea.setText(sql);
	}
	
	public boolean showDialog() {
		setVisible(true);
		return saved;
	}
}
