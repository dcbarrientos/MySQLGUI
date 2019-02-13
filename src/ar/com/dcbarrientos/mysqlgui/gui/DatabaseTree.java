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

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import ar.com.dcbarrientos.mysqlgui.db.Database;
import ar.com.dcbarrientos.mysqlgui.db.Query;

/**
 * @author Diego Barrientos <dc_barrientos@yahoo.com.ar>
 *
 */
public class DatabaseTree extends DatabaseElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTree tree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode id;

	private DefaultTreeCellRenderer renderer;
	private ImageIcon databaseClosedNode;
	private ImageIcon databaseOpenedNode;
	private ImageIcon tableNode;

	public final int ROOT_COUNT = 0;
	public final int USER_COUNT = 1;
	public final int DATABASE_COUNT = 2;
	public final int TABLE_COUNT = 3;

	public DatabaseTree(Ventana ventana, Database database) {
		super(ventana, database);

		initComponents();
	}

	private void initComponents() {
		tree = new JTree();

		databaseClosedNode = new ImageIcon(getClass().getResource("/images/Database2.gif"));
		databaseOpenedNode = new ImageIcon(getClass().getResource("/images/database.gif"));
		tableNode = new ImageIcon(getClass().getResource("/images/table.gif"));

		renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(databaseOpenedNode);
		renderer.setClosedIcon(databaseClosedNode);
		renderer.setLeafIcon(tableNode);

		loadData();
		tree.setModel(treeModel);
		tree.setCellRenderer(renderer);

		tree.expandPath(new TreePath(id.getPath()));
			tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				treeValueChanged(e);
			}

		});
		setLayout(new BorderLayout());
		add(tree, BorderLayout.CENTER);
	}

	protected void loadData() {
		id = new DefaultMutableTreeNode(database.getConnectionID());
		treeModel = new DefaultTreeModel(id);

		DefaultMutableTreeNode db;

		Query query = new Query(database);
		query.executeQuery("SHOW DATABASES;");
		while (query.next()) {
			db = new DefaultMutableTreeNode(query.getString(1));
			treeModel.insertNodeInto(db, id, id.getChildCount());

			Query tableQuery = new Query(database);
			String sql = "SHOW TABLES FROM " + query.getString(1) + ";";
			tableQuery.executeQuery(sql);
			boolean empty = true;
			while (tableQuery.next()) {
				empty = false;
				DefaultMutableTreeNode table = new DefaultMutableTreeNode(tableQuery.getString(1));
				treeModel.insertNodeInto(table, db, db.getChildCount());
			}
			if (empty) {
				DefaultMutableTreeNode table = new DefaultMutableTreeNode(resource.getString("DatabaseTree.txtEmpty"));
				treeModel.insertNodeInto(table, db, db.getChildCount());
			}
			tableQuery.close();
		}
		query.close();
	}

	private boolean isDatabase(TreePath path) {
		if (path.getPathCount() == DATABASE_COUNT)
			return true;
		return false;
	}

	private boolean isTable(TreePath path) {
		if (path.getPathCount() == TABLE_COUNT)
			return true;
		return false;
	}

	public void treeValueChanged(TreeSelectionEvent e) {
		TreePath path = e.getPath();
		if (isDatabase(path)) {
			ventana.selectDatabase(path.getLastPathComponent().toString());
		} else if (isTable(path)) {
			ventana.selectTable(path.getPathComponent(DATABASE_COUNT - 1).toString(),
					path.getLastPathComponent().toString());

		}
	}

	@Override
	public void refresh() {
		//TODO: Refresh para datatree
		ventana.refresh();
	}

}
