package hr.fer.courses.aos.crypto;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SymmCipherParamsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> nameList;
	private JComboBox<Integer> keysizeList;
	private JComboBox<String> modeList;
	
	public SymmCipherParamsPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel titlePanel = new JPanel();
		this.add(titlePanel, BorderLayout.PAGE_START);
		titlePanel.add(new JLabel("SYMMETRIC CIPHER", SwingConstants.LEFT));

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));
		this.add(bodyPanel, BorderLayout.CENTER);


		JPanel namePanel = new JPanel();
		bodyPanel.add(namePanel);
		namePanel.add(new JLabel("NAME:"));
		nameList = new JComboBox<>(CryptoParams.SYMMETRIC_CIPHERS);
		namePanel.add(nameList);


		JPanel modePanel = new JPanel();
		bodyPanel.add(modePanel);
		modePanel.add(new JLabel("MODE:"));
		modeList = new JComboBox<>();
		modePanel.add(modeList);
		setModeCombobox(nameList, modeList);


		JPanel keysizePanel = new JPanel();
		bodyPanel.add(keysizePanel);
		keysizePanel.add(new JLabel("KEYSIZE:"));
		keysizeList = new JComboBox<>();
		keysizePanel.add(keysizeList);
		setKeysizeCombobox(nameList, keysizeList);

		nameList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setKeysizeCombobox(nameList, keysizeList);
				setModeCombobox(nameList, modeList);
			}

		});
	}
	
	public String getName() {
		return (String) nameList.getSelectedItem();
	}
	
	public Integer getKeysize() {
		return (Integer) keysizeList.getSelectedItem();
	}
	
	public String getMode() {
		return (String) modeList.getSelectedItem();
	}

	private void setKeysizeCombobox(JComboBox<String> nameList, JComboBox<Integer> keysizeList) {
		String cipher = (String)nameList.getSelectedItem();
		Integer[] keysizes = CryptoParams.SYMMETRIC_CIPER_TO_KEYSIZES.getOrDefault(cipher, null);
		ComboBoxModel<Integer> newKeysizeModel;
		if (keysizes == null) {
			newKeysizeModel = new DefaultComboBoxModel<>();
		} else {
			newKeysizeModel = new DefaultComboBoxModel<>(keysizes);
		}
		keysizeList.setModel(newKeysizeModel);
	}

	private void setModeCombobox(JComboBox<String> nameList, JComboBox<String> modeList) {
		String cipher = (String)nameList.getSelectedItem();
		String[] modes = CryptoParams.SYMMETRIC_CIPER_TO_FEEDBACK_MODES.getOrDefault(cipher, null);
		ComboBoxModel<String> newModesModel;
		if (modes == null) {
			newModesModel = new DefaultComboBoxModel<String>();
		} else {
			newModesModel = new DefaultComboBoxModel<String>(modes);
		}
		modeList.setModel(newModesModel);
	}
}
