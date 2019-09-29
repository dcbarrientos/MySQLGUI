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
	public String originalName;
	public String dataType;
	public String length;
	public String collate;
	public String charset;
	public String columnDefault;
	public boolean notNull;
	public boolean unique;
	public boolean binary;
	public boolean unsigned;
	public boolean zerofill;
	public boolean autoincrement;
	public boolean generated;
	public boolean virtual;
	public boolean stored;
	public String comment;
	public boolean isNew;
	public boolean isDeleted;
	
	public ColumnModel() {
		name = "";
		dataType = "";
		length = "";
		collate = "";
		charset = "";
		comment = "";
		isNew = true;
		isDeleted = false;
	}
	
	public String getDefinition() {
		String definition = "";
		definition = "`" + name + "` ";
		definition += dataType.toUpperCase();
		if (length != null && length.length() > 0)
			definition += "(" + length + ") ";
		else
			definition += " ";
		
		if(generated) {
			definition += "GENERATED ALWAYS AS (" + columnDefault + ") ";
			if(virtual)
				definition += "VIRTUAL ";
			else
				definition += "STORED ";
		}
		
		if(unique)
			definition += "UNIQUE ";
		if(notNull)
			definition += "NOT NULL ";
		if(!notNull)
			definition += "NULL ";
		
		if(!generated) {
			if(autoincrement)
				definition += "AUTO_INCREMENT ";
			if(binary)
				definition += "BINARY ";
			if(unsigned)
				definition += "UNSIGNED ";
			if(zerofill)
				definition += "ZEROFILL ";
			if(columnDefault != null && columnDefault.length() > 0 && !generated)
				definition += "DEFAULT '" + columnDefault + "' ";
			if(collate.length() >0)
				definition += "COLLATE " + collate + " ";
			if(charset.length() > 0)
				definition += "CHARTSET " + charset + " ";
		}
		
		if(comment.length() > 0)
			definition += "COMMENT '" + comment + "' ";
		
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
