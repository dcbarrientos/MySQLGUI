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
 * ColumnModel.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 18 abr. 2019, 10:33:05 
 */

package ar.com.dcbarrientos.mysqlgui.model;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class ColumnModel {
	public String name;
	public String dataType;
	public String length;
	public boolean autoincrement;
	public boolean notNull;
	public boolean binary;
	public boolean unsigned;
	public boolean zerofill;
	public boolean unique;
	public String columnDefault;
	public String collate;
	public String charset;
	public String comment;

	public String getDefinition() {
		String definition = "";
		definition = "`" + name + "` ";
		definition += dataType.toUpperCase();
		if (length.length() > 0)
			definition += "(" + length + ") ";
		else
			definition += " ";
		
		if(autoincrement)
			definition += "AUTOINCREMENT ";
		if(notNull)
			definition += "NOT NULL ";
		if(!notNull)
			definition += "NULL ";
		if(binary)
			definition += "BINARY ";
		if(unsigned)
			definition += "UNSIGNED ";
		if(zerofill)
			definition += "ZEROFILL ";
		if(columnDefault.length() > 0)
			definition += "DEFAULT '" + columnDefault + "' ";
		if(comment != null)
			definition += "COMMENT '" + comment + "' ";
		if(comment != null)
			definition += "COLLATE " + collate + " ";
		if(comment != null)
			definition += "CHARTSET " + charset + " ";
		
		return definition;
	}
	/*
	 * [UNIQUE [KEY]] [[PRIMARY] KEY] [COLUMN_FORMAT {FIXED|DYNAMIC|DEFAULT}]
	 * [STORAGE {DISK|MEMORY}] [reference_definition] [check_constraint_definition]
	 * | data_type [GENERATED ALWAYS] AS (expr) [VIRTUAL | STORED] [NOT NULL | NULL]
	 * [UNIQUE [KEY]] [[PRIMARY] KEY] [reference_definition]
	 * [check_constraint_definition]
	 */
}
