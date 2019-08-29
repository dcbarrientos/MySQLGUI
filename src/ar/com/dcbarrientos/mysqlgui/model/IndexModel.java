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
 * IndexModel.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 10 jun. 2019, 11:31:34 
 */

package ar.com.dcbarrientos.mysqlgui.model;

import java.util.Vector;

import ar.com.dcbarrientos.mysqlgui.db.Database;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class IndexModel {
	public final int FIELD_COUNT = 3;

	public final int FIELD_NAME = 0;
	public final int FIELD_LENGTH = 1;
	public final int FIELD_ORDER = 2;

	public final static String[] INDEXTYPELIST = { "INDEX", "UNIQUE", "FULLTEXT", "PRIMARY", "SPATIAL" };
	public final static int INDEX_INDEX = 0;
	public final static int INDEX_UNIQUE = 1;
	public final static int INDEX_FULLTEXT = 2;
	public final static int INDEX_PRIMARY = 3;
	public final static int INDEX_SPATIAL = 4;

	public final static String COMBO_ASC = "ASC";
	public final static String COMBO_DESC = "DESC";

	public String name; //
	public String originalName; //Contiene el nombre original del indice por si editan su nombre.
	public String indexType; //
	public Vector<String[]> fields; //
	public String blockSize;
	public String storageType; //
	public String parser;
	public String comment; //
	public boolean visible; // false si hay que borrar este indice

	public IndexModel() {
		fields = new Vector<String[]>();

		name = "";
		indexType = "";
		blockSize = "";
		storageType = "";
		parser = "";
		comment = "";
		visible = true;
	}

	// Order: ASC|DESC

	public void addFields(String columnName, String order) {
		String[] field = new String[FIELD_COUNT];

		if (columnName.endsWith(")")) {
			field[FIELD_NAME] = Database.trimCuote(columnName.substring(0, columnName.indexOf("(") - 1));
			field[FIELD_LENGTH] = columnName.substring(columnName.indexOf("(") + 1, columnName.length() - 1);
		} else
			field[FIELD_NAME] = Database.trimCuote(columnName);

		if (order.equals(""))
			field[FIELD_ORDER] = "ASC";
		else
			field[FIELD_ORDER] = order;

		fields.add(field);
	}

	public void addField(String columnName, String lengthExpr, String order) {
		String[] field = new String[FIELD_COUNT];
		field[FIELD_NAME] = columnName;
		field[FIELD_LENGTH] = lengthExpr;
		field[FIELD_ORDER] = order;

		fields.add(field);
	}

	public String getDefinition() {
		String definition = "";
		if (!indexType.equals(INDEXTYPELIST[INDEX_INDEX])) {
			definition += indexType + " ";
		}
		definition += "KEY";

		if (!indexType.equals(INDEXTYPELIST[INDEX_PRIMARY])) {
			definition += " `" + name + "`";
		}

		definition += " (";
		// TODO: lista de campos que forman el indice
		int fc = 0;
		for (int i = 0; i < fields.size(); i++) {
			if (fc > 0)
				definition += ", ";
			definition += "`" + fields.get(i)[FIELD_NAME] + "`";

			if (fields.get(i)[FIELD_LENGTH] != null && fields.get(i)[FIELD_LENGTH].length() > 0)
				definition += "(" + fields.get(i)[FIELD_LENGTH] + ")";

			if (!COMBO_ASC.equals(fields.get(i)[FIELD_ORDER]) && fields.get(i)[FIELD_ORDER].length() > 0)
				definition += " " + COMBO_DESC;

			fc++;
		}

		definition += ")";
		// TODO: Procesar parser/analizador

		if (storageType.length() > 0)
			definition += " USING " + storageType;

		if (blockSize.length() > 0)
			definition += " KEY_BLOCK_SIZE=" + blockSize;

		if (comment.length() > 0)
			definition += " COMMENT '" + comment + "'";

		return definition;
	}

	public String getFieldList() {
		String list = "";
		int c = 0;
		for (int i = 0; i < fields.size(); i++) {
			if (c > 0)
				list += ", ";
			list += fields.get(i)[FIELD_NAME];
			c++;
		}

		return list;
	}
}
