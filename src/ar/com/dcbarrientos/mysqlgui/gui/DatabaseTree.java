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
 * DatabaseTree.java
 *
 * Description:	    <Descripcion>
 * @author			Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 * Created on 1 feb. 2019, 19:58:34 
 */

package ar.com.dcbarrientos.mysqlgui.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;
import ar.com.dcbarrientos.mysqlgui.model.TreeElement;
import ar.com.dcbarrientos.mysqlgui.model.TreeRenderer;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class DatabaseTree extends DatabaseElement {
	private static final long serialVersionUID = 1L;

	private JTree tree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode id;

	private TreeRenderer renderer;

	private ImageIcon openServerIcon = new ImageIcon(getClass().getResource("/images/DatabaseServer.gif"));
	private ImageIcon closeServerIcon = new ImageIcon(getClass().getResource("/images/DatabaseServer.gif"));
	private ImageIcon openDatabaseIcon = new ImageIcon(getClass().getResource("/images/Database.gif"));
	private ImageIcon closeDatabaseIcon = new ImageIcon(getClass().getResource("/images/database2.gif"));
	private ImageIcon openTableIcon = new ImageIcon(getClass().getResource("/images/table.gif"));
	private ImageIcon closeTableIcon = new ImageIcon(getClass().getResource("/images/table2.gif"));
	// TODO: Cambiar iconos
	private ImageIcon openViewIcon = new ImageIcon(getClass().getResource("/images/close.gif"));
	private ImageIcon closeViewIcon = new ImageIcon(getClass().getResource("/images/close2.gif"));

	public final boolean isTreeSelecting = false; // Determina si tree está cambiando la selección o viene de otra
													// parte. Por ej: cuando

	private boolean isExpandingCollasing;

	public DatabaseTree(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
	}

	private void initComponents() {
		tree = new JTree();

		loadData();
		renderer = new TreeRenderer();
		tree.setCellRenderer(renderer);

		tree.expandPath(new TreePath(id.getPath()));
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				treeValueChanged(e);
			}

		});
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				mouseAndKeyEvent();
			}
		});
		tree.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				mouseAndKeyEvent();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {

			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				// TODO Auto-generated method stub
				isExpandingCollasing = true;
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				// TODO Auto-generated method stub
				isExpandingCollasing = true;

			}

		});

		setLayout(new BorderLayout());
		add(tree, BorderLayout.CENTER);
	}

	protected void loadData() {
		TreeElement e = new TreeElement(database.getConnectionID(), Database.SERVER, openServerIcon, closeServerIcon);
		id = new DefaultMutableTreeNode(e);
		treeModel = new DefaultTreeModel(id);

		DefaultMutableTreeNode db;

		Query query = new Query(database);
		query.executeQuery("SHOW DATABASES;");
		while (query.next()) {
			e = new TreeElement(query.getString(1), Database.DATABASE, openDatabaseIcon, closeDatabaseIcon);
			db = new DefaultMutableTreeNode(e);

			// Procedimiento para cargar tablas y vistas en el tree
			Query tableQuery = new Query(database);
			String sql = String.format(Query.SQL_SHOW_TABLES_FROM, query.getString(1));
			tableQuery.executeQuery(sql);
			boolean emptyTables = true;
			boolean emptyViews = true;

			TreeElement te = new TreeElement(resource.getString("DatabaseTree.tables"), Database.SEPARATOR,
					openTableIcon, closeTableIcon);
			DefaultMutableTreeNode tables = new DefaultMutableTreeNode(te);
			TreeElement ve = new TreeElement(resource.getString("DatabaseTree.views"), Database.SEPARATOR, openViewIcon,
					closeViewIcon);
			DefaultMutableTreeNode views = new DefaultMutableTreeNode(ve);

			while (tableQuery.next()) {
				if (tableQuery.getString("table_type").equals("VIEW")) {
					emptyViews = false;
					e = new TreeElement(tableQuery.getString(1), Database.VIEW, openViewIcon, closeViewIcon);
					DefaultMutableTreeNode view = new DefaultMutableTreeNode(e);
					views.add(view);
				} else if (tableQuery.getString("table_type").equals("BASE TABLE")) {
					emptyTables = false;
					e = new TreeElement(tableQuery.getString(1), Database.TABLE, openTableIcon, closeTableIcon);
					DefaultMutableTreeNode table = new DefaultMutableTreeNode(e);
					tables.add(table);
				}

			}
			if (emptyTables) {
				e = new TreeElement(resource.getString("DatabaseTree.txtEmpty"), Database.TABLE, openTableIcon,
						closeTableIcon);
				DefaultMutableTreeNode table = new DefaultMutableTreeNode(e);
				tables.add(table);
			}
			if (emptyViews) {
				e = new TreeElement(resource.getString("DatabaseTree.txtEmpty"), Database.VIEW, openViewIcon,
						closeViewIcon);
				DefaultMutableTreeNode view = new DefaultMutableTreeNode(e);
				views.add(view);
			}
			db.add(tables);
			db.add(views);
			id.add(db);
			// Fin procedimiento que carga tablas y vistas en el tree

			// Procedimiento que carga Stored Procedures en el tree
			// TODO: Procedimiento que carga StoredProcedures en el tree
			// Fin del procedimiento que carga Stored Procedures en el tree

			// Procedimiento que carga Funciones en el tree
			// TODO: Procedimiento que carga Funciones en el tree
			// Fin del procedimiento que carga Funciones en el tree

			tableQuery.close();
		}
		query.close();
		tree.setModel(treeModel);

	}

	private boolean isCategory(TreePath path) {
		TreeElement e = getLastElement(path);
		if (e.type == Database.SEPARATOR) {
			return true;
		}
		return false;
	}

	private boolean isDatabase(TreePath path) {
		TreeElement e = getLastElement(path);
		if (e.type == Database.DATABASE)
			return true;
		return false;
	}

	private boolean isTable(TreePath path) {
		TreeElement e = getLastElement(path);
		if (e.type == Database.TABLE)
			return true;
		return false;
	}

	private boolean isStoredProcedure(TreePath path) {
		TreeElement e = getLastElement(path);
		if (e.type == Database.STORED_PROCEDURE)
			return true;
		return false;
	}

	private boolean isView(TreePath path) {
		TreeElement e = getLastElement(path);
		if (e.type == Database.VIEW)
			return true;
		return false;
	}

	private boolean isFunction(TreePath path) {
		TreeElement e = getLastElement(path);
		if (e.type == Database.FUNCTION)
			return true;
		return false;
	}

	public void treeValueChanged(TreeSelectionEvent e) {
		TreePath path = e.getPath();
		if (isCategory(path)) {
			selectedDB = getElementAt(path, path.getPathCount() - 2).name;
			selectedElement = "";
			selectedType = Database.SEPARATOR;
		} else {
			if (isDatabase(path)) {
				selectedDB = getLastElement(path).name;
				selectedElement = "";
				selectedType = Database.DATABASE;
			} else if (isTable(path)) {
				selectedDB = getElementAt(path, path.getPathCount() - 3).name;
				selectedElement = getLastElement(path).name;
				selectedType = Database.TABLE;
			} else if (isView(path)) {
				selectedDB = getElementAt(path, path.getPathCount() - 3).name;
				selectedElement = getLastElement(path).name;
				selectedType = Database.VIEW;
			} else if (isStoredProcedure(path)) {
				selectedDB = getElementAt(path, path.getPathCount() - 3).name;
				selectedElement = getLastElement(path).name;
				selectedType = Database.STORED_PROCEDURE;
			} else if (isFunction(path)) {
				selectedDB = getElementAt(path, path.getPathCount() - 3).name;
				selectedElement = getLastElement(path).name;
				selectedType = Database.FUNCTION;
			}
		}
	}

	public void mouseAndKeyEvent() {
		if (!isExpandingCollasing) {
			if (selectedType == Database.DATABASE || selectedType == Database.TABLE || selectedType == Database.VIEW) {
				int r = database.existeTabla(selectedDB, selectedElement);
				if (r == -2) {
					selectRoot();
				} else if (r == -1) {
					ventana.setSelectedDatabase(selectedDB);
				} else {
					if (selectedType == Database.TABLE)
						ventana.setSelectedTable(selectedDB, selectedElement);
					else
						ventana.setSelectedView(selectedDB, selectedElement);
//					JOptionPane.showMessageDialog(null, "Selecciono la vista " + selectedDB + "." + selectedElement);
					// TODO: falta implementar mostrar vistas
				}
				System.out.println("Mostrar");
			} else if (selectedType == Database.STORED_PROCEDURE) {
				// TODO: falta implementar
				JOptionPane.showMessageDialog(null,
						"Selecciono el Stored Procedure " + selectedDB + "." + selectedElement);
			} else if (selectedType == Database.FUNCTION) {
				// TODO: Falta implementar
				JOptionPane.showMessageDialog(null, "Selecciono la funcion " + selectedDB + "." + selectedElement);
			}
		}
		isExpandingCollasing = false;
	}

	/**
	 * Selecciona un elemento de una base de datos
	 * 
	 * @param db      Nombre de la base de datos que contiene el elemento.
	 * @param element Nombre del elemento a buscar
	 * @param type    Tipo de elemento a buscar.
	 * @param show    Verdadero si se debe mostrar el nodo encontrado.
	 * @return Devuelve el nodo encontrado.
	 */
	public DefaultMutableTreeNode setSelection(String db, String element, int type, boolean show) {
		DefaultMutableTreeNode dbNode = setSelection(db, false);
		if (dbNode != null) {
			return setElementSelection(dbNode, element, type, show);
		}

		return null;
	}

	/**
	 * Busca un elemento a partir del Root
	 * 
	 * @param element Nombre del elemento a buscar
	 * @param show    Verdadero si se debe mostar el nodo encontrado
	 * @return Devuelve el nodo encontrado.
	 */
	public DefaultMutableTreeNode setSelection(String element, boolean show) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getRoot();
		return setElementSelection(node, element, Database.DATABASE, show);

	}

	/**
	 * Selecciona un nodo a partir de un nodo padre.
	 * 
	 * @param node    Nodo padre a partir del cual se hace la busqueda.
	 * @param element Nombre del elemento a buscar
	 * @param type    Tipo de elemento a buscar
	 * @param show    Verdadero si se debe mostrar el nodo encontrado
	 * @return Devuelve el nodo encontrado.
	 */
	public DefaultMutableTreeNode setElementSelection(DefaultMutableTreeNode node, String element, int type,
			boolean show) {
		Enumeration<?> enumeration = node.depthFirstEnumeration();
		TreeElement r;
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) enumeration.nextElement();
			r = (TreeElement) n.getUserObject();
			if (r.name.equals(element) && r.type == type) {
				if (show) {
					TreePath tp = new TreePath(n.getPath());
					tree.scrollPathToVisible(tp);
					tree.setSelectionPath(tp);
				}
				return n;
			}
		}

		return null;
	}

	private TreeElement getElement(DefaultMutableTreeNode node) {
		return (TreeElement) (node.getUserObject());
	}

	private TreeElement getLastElement(TreePath path) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		return getElement(node);
	}

	private TreeElement getElementAt(TreePath path, int pos) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getPathComponent(pos);
		return getElement(node);
	}

	@Override
	public void refresh() {
		loadData();

		tree.revalidate();
		tree.repaint();
	}

	public void setSelectedDatabase(String db) {
		setSelection(db, false);
	}

	public void selectRoot() {
//		TreePath path = new TreePath(id);
//		tree.expandPath(path);
//		tree.setSelectionPath(path);
		ventana.selectRoot();
	}
}
