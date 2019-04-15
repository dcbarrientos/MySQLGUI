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
 * CellRenderer.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 16 feb. 2019, 10:50:51 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class CellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Establecemos el fondo blanco o vacio
		setBackground(null);

		// Constructor de la clase DefaultTableCellRenderer
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// Establecemos las filas que queremos cambiar el color. == 0 para pares y != 0
		// para impares
		boolean oddRow = (row % 2 == 0);
		
		//Creamos un color para las filas. El 200, 200, 200 en RGB es un color gris
		Color c = new Color(200, 200, 200);
		
		if(oddRow)
			setBackground(c);
		
		return this;
	}
}
