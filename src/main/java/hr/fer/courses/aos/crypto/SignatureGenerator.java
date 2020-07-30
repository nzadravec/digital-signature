package hr.fer.courses.aos.crypto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignatureGenerator {

	private Path filePath;
	private String hashAlgorithm;
	private String aCipherAlgorithm;
	private Path privateKeyPath;
	private int keyLength;

	public SignatureGenerator(Path filePath, String hashAlgorithm, String aCipherAlgorithm, Path privateKeyPath,
			int keyLength) {
		super();
		this.filePath = filePath;
		this.hashAlgorithm = hashAlgorithm;
		this.aCipherAlgorithm = aCipherAlgorithm;
		this.privateKeyPath = privateKeyPath;
		this.keyLength = keyLength;
	}

	public DigitalSignature generate() throws Exception {

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Files.readAllBytes(privateKeyPath));
		KeyFactory keyFactory = KeyFactory.getInstance(aCipherAlgorithm.split("/")[0]);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		Signature signature = Signature.getInstance(hashAlgorithm + "with" + aCipherAlgorithm);
		signature.initSign(privateKey);
		signature.update(Files.readAllBytes(filePath));

		byte[] sign = signature.sign();

		String[] method = new String[] { hashAlgorithm, aCipherAlgorithm };

		return new DigitalSignature(filePath.normalize().toAbsolutePath().toString(), method, keyLength,
				Util.bytetohex(sign));
	}

}
