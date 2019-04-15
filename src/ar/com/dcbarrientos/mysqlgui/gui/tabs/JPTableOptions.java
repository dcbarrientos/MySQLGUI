package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;

public class JPTableOptions extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JPanel borderTableOptions;
	private JLabel lblTableName;
	private JLabel lblEngine;
	private JLabel lblAutoIncrement;
	private JLabel lblCharset;
	private JLabel lblCollation;
	private JLabel lblDescription;
	private JTextField txtAutoIncrement;
	private JComboBox cbEngine;
	private JTextField txtTableName;
	private JComboBox cbCharset;
	private JComboBox cbCollation;
	private JTextArea txtDescription;
	
	
	
	public JPTableOptions() {
		initComponents();
	}
	private void initComponents() {
		setPreferredSize(new Dimension(450, 200));
		setLayout(new BorderLayout(0, 0));
		
		borderTableOptions = new JPanel();
		borderTableOptions.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(borderTableOptions, BorderLayout.CENTER);
		
		lblTableName = new JLabel("Table name:");
		
		lblEngine = new JLabel("Engine:");
		
		lblAutoIncrement = new JLabel("Auto Increment:");
		
		lblCharset = new JLabel("Charset:");
		
		lblCollation = new JLabel("Collation:");
		
		lblDescription = new JLabel("Description:");
		
		txtAutoIncrement = new JTextField();
		txtAutoIncrement.setColumns(10);
		
		cbEngine = new JComboBox();
		
		txtTableName = new JTextField();
		txtTableName.setColumns(10);
		
		cbCharset = new JComboBox();
		
		cbCollation = new JComboBox();
		
		txtDescription = new JTextArea();
		GroupLayout gl_borderTableOptions = new GroupLayout(borderTableOptions);
		gl_borderTableOptions.setHorizontalGroup(
			gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_borderTableOptions.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAutoIncrement)
						.addComponent(lblEngine)
						.addComponent(lblTableName)
						.addComponent(lblCharset)
						.addComponent(lblCollation)
						.addComponent(lblDescription))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
						.addComponent(txtDescription, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
						.addComponent(cbEngine, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtAutoIncrement, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtTableName, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
						.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(cbCollation, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(cbCharset, Alignment.LEADING, 0, 141, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_borderTableOptions.setVerticalGroup(
			gl_borderTableOptions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_borderTableOptions.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTableName)
						.addComponent(txtTableName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEngine)
						.addComponent(cbEngine, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAutoIncrement)
						.addComponent(txtAutoIncrement, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCharset)
						.addComponent(cbCharset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCollation)
						.addComponent(cbCollation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_borderTableOptions.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDescription)
						.addComponent(txtDescription, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(140, Short.MAX_VALUE))
		);
		borderTableOptions.setLayout(gl_borderTableOptions);
	}
}
