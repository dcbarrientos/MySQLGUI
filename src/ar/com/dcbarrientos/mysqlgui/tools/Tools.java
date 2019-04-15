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
 * Tools.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 14 feb. 2019, 11:16:12 
 */

package ar.com.dcbarrientos.mysqlgui.tools;

import java.text.DecimalFormat;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class Tools {
	public static String[] units = {"bytes", "KB.", "Mb.", "Gb.", "Tb.", "Pb."};
	
	public static String convertUnits(double bytes) {
		DecimalFormat df2 = new DecimalFormat("0.00");
		int i = 0;
		while(bytes > 1024) {
			i++;
			bytes /= 1024;
		}
		
		return df2.format(bytes) + " " + units[i];
	}
}
