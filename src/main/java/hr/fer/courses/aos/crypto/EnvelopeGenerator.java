package hr.fer.courses.aos.crypto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EnvelopeGenerator {

	private Path filePath;
	private String cipherAlgorithm;
	private int keysize;
	private String aCipherAlgorithm;
	private Path publicKeyPath;
	private int akeysize;

	public EnvelopeGenerator(Path filePath, String cipherAlgorithm, int keysize, String aCipherAlgorithm,
			Path publicKeyPath, int akeysize) {
		super();
		this.filePath = filePath;
		this.cipherAlgorithm = cipherAlgorithm;
		this.keysize = keysize;
		this.aCipherAlgorithm = aCipherAlgorithm;
		this.publicKeyPath = publicKeyPath;
		this.akeysize = akeysize;
	}

	public DigitalEnvelope generate() throws Exception {
		String[] cipherAlgParams = cipherAlgorithm.split("/");

		KeyGenerator keyGen = KeyGenerator.getInstance(cipherAlgParams[0]);
		keyGen.init(keysize);
		SecretKey secretKey = keyGen.generateKey();

		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		byte[] iv = null;
		if (cipherAlgParams.length == 1 || cipherAlgorithm.split("/")[1].equals("ECB")) {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} else {
			SecureRandom srand = new SecureRandom();
			iv = new byte[cipher.getBlockSize()];
			srand.nextBytes(iv);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
		}

		byte[] cryptedData = cipher.doFinal(Files.readAllBytes(filePath));

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Files.readAllBytes(publicKeyPath));
		KeyFactory keyFactory = KeyFactory.getInstance(aCipherAlgorithm.split("/")[0]);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		Cipher acipher = Cipher.getInstance(aCipherAlgorithm);
		acipher.init(Cipher.WRAP_MODE, publicKey);

		byte[] cryptedKey = acipher.wrap(secretKey);

		String[] method = new String[] { cipherAlgorithm, aCipherAlgorithm };
		int[] keyLength = new int[] { keysize, akeysize };

		return new DigitalEnvelope(filePath.normalize().toAbsolutePath().toString(), method, keyLength,
				Util.bytetohex(iv), Base64.getEncoder().encodeToString(cryptedData), Util.bytetohex(cryptedKey));
	}

}
