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
 * TableModel.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 11 feb. 2019, 10:57:52 
 */

package ar.com.dcbarrientos.mysqlgui.model;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private Vector<Object[]> data;
	private String[] columnHeaders;
	private Class<?>[] classes;
	private boolean[] editableColumns;
	private boolean editableTable = false;;

	public TableModel() {
		data = new Vector<Object[]>();
	}

	public TableModel(Vector<Object[]> data, String[] columnHeaders) {
		this.data = data;
		this.columnHeaders = columnHeaders;
	}

	@Override
	public int getRowCount() {
		if (data != null)
			return data.size();

		return 0;
	}

	@Override
	public int getColumnCount() {
		return columnHeaders.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex)[columnIndex];
	}

	@Override
	public void setValueAt(Object dato, int rowIndex, int columnIndex) {
		data.get(rowIndex)[columnIndex] = dato;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public String getColumnName(int column) {
		return columnHeaders[column];
	}

	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
	}

	public void setData(Vector<Object[]> data) {
		this.data = data;
		super.fireTableDataChanged();
	}

	public void setColumnHeaders(String[] columnHeaders) {
		this.columnHeaders = columnHeaders;
		super.fireTableStructureChanged();
	}

	public void addRecord(Object[] records) {
		data.add(records);
		fireTableDataChanged();
	}

	public void setColumnsClasses(Class<?>[] classes) {
		this.classes = classes;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (classes != null)
			return classes[columnIndex];

		return String.class;
	}

	/**
	 * Establece columna por columna cuál de ellas es editable.
	 * 
	 * @param editableColumns Array de booleans con tantos elementos como columnas
	 *                        para determinar si son editables.
	 */
	public void setEditableColumns(boolean[] editableColumns) {
		this.editableColumns = editableColumns;
	}

	/**
	 * Establece si todas las celdas de la tabla son editables. Si se utiliza
	 * setEditableColumns(boolean[] editableColumns) esta queda sin efecto.
	 * 
	 * @param editableTable true para que todas las celdas sean editables. false,
	 *                      ninguna.
	 */
	public void setEditableColumns(boolean editableTable) {
		this.editableTable = editableTable;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (editableColumns != null)
			return editableColumns[columnIndex];

		return editableTable;
	}
}
