package hr.fer.courses.aos.crypto;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HashFuncParamsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox<Integer> hashsizeList;
	
	public HashFuncParamsPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel titlePanel = new JPanel();
		this.add(titlePanel, BorderLayout.PAGE_START);
		titlePanel.add(new JLabel("MESSAGE DIGEST SHA-1", SwingConstants.LEFT));

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));
		this.add(bodyPanel, BorderLayout.CENTER);

		
		JPanel hashsizePanel = new JPanel();
		bodyPanel.add(hashsizePanel);
		hashsizePanel.add(new JLabel("HASHSIZE:"));
		hashsizeList = new JComboBox<>(CryptoParams.SHA1_HASHSIZES);
		hashsizePanel.add(hashsizeList);
	}
	
	public Integer getHashsize() {
		return (Integer) hashsizeList.getSelectedItem();
	}

}
