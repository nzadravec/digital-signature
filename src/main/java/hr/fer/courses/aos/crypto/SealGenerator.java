package hr.fer.courses.aos.crypto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class SealGenerator {

	private Path filePath;
	private String cipherAlgorithm;
	private int keysize;
	private String aCipherAlgorithm;
	private Path publicKeyPath;
	private Path privateKeyPath;
	private int akeysize;
	private String hashAlgorithm;

	public SealGenerator(Path filePath, String cipherAlgorithm, int keysize, String aCipherAlgorithm,
			Path publicKeyPath, Path privateKeyPath, int akeysize, String hashAlgorithm) {
		super();
		this.filePath = filePath;
		this.cipherAlgorithm = cipherAlgorithm;
		this.keysize = keysize;
		this.aCipherAlgorithm = aCipherAlgorithm;
		this.publicKeyPath = publicKeyPath;
		this.privateKeyPath = privateKeyPath;
		this.akeysize = akeysize;
		this.hashAlgorithm = hashAlgorithm;
	}

	public DigitalSeal generate() throws Exception {
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
		String envelopeData = Base64.getEncoder().encodeToString(cryptedData);

		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Files.readAllBytes(publicKeyPath));
		KeyFactory publicKeyFactory = KeyFactory.getInstance(aCipherAlgorithm.split("/")[0]);
		PublicKey publicKey = publicKeyFactory.generatePublic(publicKeySpec);

		Cipher acipher = Cipher.getInstance(aCipherAlgorithm);
		acipher.init(Cipher.WRAP_MODE, publicKey);

		byte[] cryptedKey = acipher.wrap(secretKey);
		String envelopeCryptKey = Util.bytetohex(cryptedKey);

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Files.readAllBytes(privateKeyPath));
		KeyFactory privateKeyFactory = KeyFactory.getInstance(aCipherAlgorithm.split("/")[0]);
		PrivateKey privateKey = privateKeyFactory.generatePrivate(privateKeySpec);

		String envelope = envelopeData + envelopeCryptKey;

		Signature signature = Signature.getInstance(hashAlgorithm + "with" + aCipherAlgorithm);
		signature.initSign(privateKey);
		signature.update(envelope.getBytes());

		byte[] sign = signature.sign();

		String[] method = new String[] { cipherAlgorithm, aCipherAlgorithm, hashAlgorithm };
		int[] keyLength = new int[] { keysize, akeysize };

		return new DigitalSeal(filePath.normalize().toAbsolutePath().toString(), method, keyLength, Util.bytetohex(iv),
				envelopeData, envelopeCryptKey, Util.bytetohex(sign));
	}

}
