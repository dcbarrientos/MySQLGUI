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
 * TreeElement.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 25 oct. 2019, 11:07:15 
 */

package ar.com.dcbarrientos.mysqlgui.model;

import javax.swing.ImageIcon;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class TreeElement {

	public String name;
	public int type;
	public ImageIcon openIcon;
	public ImageIcon closeIcon;

	public TreeElement(String name, int type, ImageIcon openIcon, ImageIcon closeIcon) {
		this.name = name;
		this.type = type;
		this.openIcon = openIcon;
		this.closeIcon = closeIcon;
	}
}
