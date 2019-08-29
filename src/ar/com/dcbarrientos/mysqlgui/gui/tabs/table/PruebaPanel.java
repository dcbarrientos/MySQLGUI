package ar.com.dcbarrientos.mysqlgui.gui.tabs.table;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class PruebaPanel extends JPanel{
	private JPanel indexesPanel;
	private JPanel columnsPanel;
	private JPanel optionsPanel;
	private JPanel buttonPanel;
	private JButton btnAdd;
	private JButton btnDelete;
	private JScrollPane scrollPane;
	private JTable table;
	private JScrollPane scrollPane_1;
	private JTable table_1;
	private JLabel lblStorageType;
	private JComboBox comboBox;
	private JLabel lblKeyBlockSize;
	private JTextField textField;
	private JLabel lblParser;
	private JTextField textField_1;
	private JPanel panel;
	private JScrollPane scrollPane_2;
	private JTextArea textArea;
	public PruebaPanel() {
		
		initComponents();
	}
	private void initComponents() {
		
		indexesPanel = new JPanel();
		indexesPanel.setBorder(new TitledBorder(null, "Indexes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		columnsPanel = new JPanel();
		columnsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Index columns", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		optionsPanel = new JPanel();
		optionsPanel.setBorder(new TitledBorder(null, "Index options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(indexesPanel, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(columnsPanel, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(optionsPanel, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(optionsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(indexesPanel, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
							.addComponent(columnsPanel, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)))
					.addContainerGap())
		);
		
		lblStorageType = new JLabel("Storage type:");
		
		comboBox = new JComboBox();
		
		lblKeyBlockSize = new JLabel("Key block size:");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		lblParser = new JLabel("Parser:");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Index comment", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_optionsPanel = new GroupLayout(optionsPanel);
		gl_optionsPanel.setHorizontalGroup(
			gl_optionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_optionsPanel.createSequentialGroup()
					.addGroup(gl_optionsPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
						.addGroup(gl_optionsPanel.createSequentialGroup()
							.addGroup(gl_optionsPanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_optionsPanel.createSequentialGroup()
									.addGroup(gl_optionsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblStorageType)
										.addComponent(lblKeyBlockSize))
									.addPreferredGap(ComponentPlacement.RELATED))
								.addComponent(lblParser))
							.addGroup(gl_optionsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
								.addComponent(comboBox, 0, 59, Short.MAX_VALUE))))
					.addContainerGap())
		);
		gl_optionsPanel.setVerticalGroup(
			gl_optionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_optionsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_optionsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStorageType)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_optionsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblKeyBlockSize)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_optionsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblParser))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
		);
		panel.setLayout(new BorderLayout(0, 0));
		
		scrollPane_2 = new JScrollPane();
		panel.add(scrollPane_2, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane_2.setViewportView(textArea);
		optionsPanel.setLayout(gl_optionsPanel);
		columnsPanel.setLayout(new BorderLayout(0, 0));
		
		scrollPane_1 = new JScrollPane();
		columnsPanel.add(scrollPane_1, BorderLayout.CENTER);
		
		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);
		indexesPanel.setLayout(new BorderLayout(0, 0));
		
		buttonPanel = new JPanel();
		indexesPanel.add(buttonPanel, BorderLayout.NORTH);
		
		btnAdd = new JButton("Add");
		buttonPanel.add(btnAdd);
		
		btnDelete = new JButton("Delete");
		buttonPanel.add(btnDelete);
		
		scrollPane = new JScrollPane();
		indexesPanel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		setLayout(groupLayout);
	}
}
