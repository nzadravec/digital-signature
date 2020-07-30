package hr.fer.courses.aos.crypto;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AsymmCipherParamsPanel extends JPanel {

private static final long serialVersionUID = 1L;

	private JComboBox<Integer> keysizeList;
	
	public AsymmCipherParamsPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel titlePanel = new JPanel();
		this.add(titlePanel, BorderLayout.PAGE_START);
		titlePanel.add(new JLabel("ASYMMETRIC CIPHER RSA"));
		
		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));
		this.add(bodyPanel, BorderLayout.CENTER);
		
		
		JPanel keysizePanel = new JPanel();
		bodyPanel.add(keysizePanel);
		keysizePanel.add(new JLabel("KEYSIZE:"));
		keysizeList = new JComboBox<>(CryptoParams.RSA_KEYSIZES);
		keysizePanel.add(keysizeList);
	}
	
	public Integer getKeysize() {
		return (Integer) keysizeList.getSelectedItem();
	}
	
}
