package ar.com.dcbarrientos.mysqlgui.gui.tabs;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MiPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel lblAlgorithm;
	private JComboBox<String> cmbAlgorithm;
	private JLabel lblDefiner;
	private JTextField txtDefiner;
	private JLabel lblSecurity;
	private JComboBox<String> cmbSecurity;
	private JLabel lblCheckOption;
	private JComboBox<String> cmbCheckOption;
	private JLabel lblAs;
	private JScrollPane scrollPane;
	private JTextArea txtSql;
	
	public MiPanel() {
		initComponents();
	}
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		
		lblAlgorithm = new JLabel("Algorithm:");
		
		cmbAlgorithm = new JComboBox<String>();
		
		lblDefiner = new JLabel("Definer:");
		
		txtDefiner = new JTextField();
		txtDefiner.setColumns(10);
		
		lblSecurity = new JLabel("Security:");
		
		cmbSecurity = new JComboBox<String>();
		
		lblCheckOption = new JLabel("Check Option:");
		
		cmbCheckOption = new JComboBox<String>();
		
		lblAs = new JLabel("As:");
		
		scrollPane = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblAlgorithm)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cmbAlgorithm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblDefiner)
							.addGap(18)
							.addComponent(txtDefiner, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblSecurity)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cmbSecurity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblCheckOption)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cmbCheckOption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblAs)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAlgorithm)
						.addComponent(cmbAlgorithm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDefiner)
						.addComponent(txtDefiner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSecurity)
						.addComponent(cmbSecurity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCheckOption)
						.addComponent(cmbCheckOption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAs)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		txtSql = new JTextArea();
		scrollPane.setViewportView(txtSql);
		panel.setLayout(gl_panel);
	}
}
