package hr.fer.courses.aos.crypto;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.google.gson.Gson;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String SENDER_KEYS_DIRECTORY = "./data/Alice/keys/";
	private static final String RECEIVER_KEYS_DIRECTORY = "./data/Bob/keys/";
	private static final String SAVING_DIRECTORY_PATH = "./data/";

	private static final Path DEFAULT_FILE_PATH = Paths.get("./data/Alice/files/message.txt");

	private Path filePath = DEFAULT_FILE_PATH;

	private SymmCipherParamsPanel symmCipherParamsPanel;
	private AsymmCipherParamsPanel asymmCipherParamsPanel;
	private HashFuncParamsPanel hashFuncParamsPanel;

	public GUI(String programFunc) {
		boolean flag = Arrays.stream(CryptoParams.PROGRAM_FUNCTIONS).anyMatch(x -> x.equals(programFunc.toLowerCase()));
		if (!flag) {
			System.err.println("Unknown function");
			System.exit(2);
		}

		setTitle(programFunc.toUpperCase());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setSize(300, 500);
		setLocationRelativeTo(null);

		initGUI(programFunc.toLowerCase());
	}

	private void initGUI(String programFunc) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		JPanel filePanel = new JPanel();
		cp.add(filePanel, BorderLayout.PAGE_START);
		filePanel.add(new JLabel("FILE:"));
		JLabel fileName = new JLabel(filePath.getFileName().toString());
		filePanel.add(fileName);
		JButton fileChooser = new JButton("Change");
		filePanel.add(fileChooser);
		fileChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser("./data/Alice/files");
				int returnVal = fc.showOpenDialog(GUI.this);
				if (returnVal != JFileChooser.APPROVE_OPTION) {
					return;
				}

				filePath = fc.getSelectedFile().toPath();
				if (!Files.isRegularFile(filePath)) {
					JOptionPane.showMessageDialog(GUI.this, "not regular file", "error", JOptionPane.ERROR_MESSAGE);
				}
				if (!Files.isReadable(filePath)) {
					JOptionPane.showMessageDialog(GUI.this, "not readable", "error", JOptionPane.ERROR_MESSAGE);
				}

				fileName.setText(filePath.getFileName().toString());
			}
		});

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		cp.add(new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel[] algParamPanels = null;
		switch (programFunc) {
		case "envelope":
			symmCipherParamsPanel = new SymmCipherParamsPanel();
			asymmCipherParamsPanel = new AsymmCipherParamsPanel();
			algParamPanels = new JPanel[] { symmCipherParamsPanel, asymmCipherParamsPanel };
			break;
		case "signature":
			hashFuncParamsPanel = new HashFuncParamsPanel();
			asymmCipherParamsPanel = new AsymmCipherParamsPanel();
			algParamPanels = new JPanel[] { hashFuncParamsPanel, asymmCipherParamsPanel };
			break;
		case "seal":
			symmCipherParamsPanel = new SymmCipherParamsPanel();
			asymmCipherParamsPanel = new AsymmCipherParamsPanel();
			hashFuncParamsPanel = new HashFuncParamsPanel();
			algParamPanels = new JPanel[] { symmCipherParamsPanel, asymmCipherParamsPanel, hashFuncParamsPanel };
			break;
		}

		for (JPanel panel : algParamPanels) {
			mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
			mainPanel.add(panel);
		}

		JButton createButton = new JButton("Create");
		cp.add(createButton, BorderLayout.PAGE_END);
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				switch (programFunc) {
				case "envelope":
					createEnvelope(programFunc);

					break;
				case "signature":
					createSignature(programFunc);

					break;
				case "seal":

					String cipherName = symmCipherParamsPanel.getName();
					int keysize = symmCipherParamsPanel.getKeysize();
					String cipherMode = symmCipherParamsPanel.getMode();

					int aKeysize = asymmCipherParamsPanel.getKeysize();

					Integer hashsize = hashFuncParamsPanel.getHashsize();

					Path publicKeyPath = Paths.get(RECEIVER_KEYS_DIRECTORY + "rsa" + aKeysize + "PublicKey");
					Path privateKeyPath = Paths.get(SENDER_KEYS_DIRECTORY + "rsa" + aKeysize + "PrivateKey");

					try {
						DigitalSeal seal = new SealGenerator(filePath,
								cipherName + "/" + cipherMode + "/" + CryptoParams.PADDING_SCHEME, keysize, "RSA",
								publicKeyPath, privateKeyPath, aKeysize, "SHA" + hashsize).generate();
						Gson gson = new Gson();
						Files.write(Paths.get(SAVING_DIRECTORY_PATH + programFunc + ".json"),
								gson.toJson(seal).getBytes());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(GUI.this, e1.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					JOptionPane.showMessageDialog(GUI.this, "Seal created", "success", JOptionPane.INFORMATION_MESSAGE);

					break;
				}
			}

			private void createEnvelope(String programFunc) {
				String cipherName = symmCipherParamsPanel.getName();
				int keysize = symmCipherParamsPanel.getKeysize();
				String cipherMode = symmCipherParamsPanel.getMode();

				int aKeysize = asymmCipherParamsPanel.getKeysize();

				Path publicKeyPath = Paths.get(RECEIVER_KEYS_DIRECTORY + "rsa" + aKeysize + "PublicKey");

				try {
					DigitalEnvelope envelope = new EnvelopeGenerator(filePath,
							cipherName + "/" + cipherMode + "/" + CryptoParams.PADDING_SCHEME, keysize, "RSA",
							publicKeyPath, aKeysize).generate();
					Gson gson = new Gson();
					Files.write(Paths.get(SAVING_DIRECTORY_PATH + programFunc + ".json"),
							gson.toJson(envelope).getBytes());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(GUI.this, e1.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(GUI.this, "Envelope created", "success", JOptionPane.INFORMATION_MESSAGE);
			}

			private void createSignature(String programFunc) {
				Integer hashsize = hashFuncParamsPanel.getHashsize();
				int aKeysize = asymmCipherParamsPanel.getKeysize();

				Path privateKeyPath = Paths.get(SENDER_KEYS_DIRECTORY + "rsa" + aKeysize + "PrivateKey");

				try {
					DigitalSignature signature = new SignatureGenerator(filePath, "SHA" + hashsize, "RSA",
							privateKeyPath, aKeysize).generate();
					Gson gson = new Gson();
					Files.write(Paths.get(SAVING_DIRECTORY_PATH + programFunc + ".json"),
							gson.toJson(signature).getBytes());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(GUI.this, e1.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(GUI.this, "Signature created", "success",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Program expects single argument");
			System.exit(1);
		}

		SwingUtilities.invokeLater(() -> new GUI(args[0]).setVisible(true));
	}

}
