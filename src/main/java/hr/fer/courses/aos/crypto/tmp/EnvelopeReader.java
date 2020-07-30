package hr.fer.courses.aos.crypto.tmp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.google.gson.Gson;

import hr.fer.courses.aos.crypto.DigitalEnvelope;
import hr.fer.courses.aos.crypto.Util;

public class EnvelopeReader {

	private static final Path ENVELOPE_PATH = Paths.get("./data/envelope.json");
	private static final Path PLAINTEXT_PATH = Paths.get("./data/plaintext.txt");

	private static final String RECEIVER_KEYS_DIRECTORY = "./data/Bob/keys/";

	public static void main(String[] args) throws Exception {

		Gson gson = new Gson();
		DigitalEnvelope envelope = gson.fromJson(new String(Files.readAllBytes(ENVELOPE_PATH)), DigitalEnvelope.class);

		Path privateKeyPath = Paths.get(RECEIVER_KEYS_DIRECTORY + envelope.getMethod()[1].toLowerCase() + envelope.getKeyLength()[1] + "PrivateKey");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Files.readAllBytes(privateKeyPath));
		KeyFactory keyFactory = KeyFactory.getInstance(envelope.getMethod()[1].split("/")[0]);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		Cipher acipher = Cipher.getInstance(envelope.getMethod()[1]);
		acipher.init(Cipher.UNWRAP_MODE, privateKey);

		String cipherAlgorithm = envelope.getMethod()[0];
		String[] cipherAlgParams = envelope.getMethod()[0].split("/");

		SecretKey secretKey = (SecretKey) acipher.unwrap(Util.hextobyte(envelope.getEnvelopeCryptKey()), cipherAlgParams[0],
				Cipher.SECRET_KEY);
		
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		if (cipherAlgParams.length == 1 || cipherAlgorithm.split("/")[1].equals("ECB")) {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
		} else {
			byte[] iv = Util.hextobyte(envelope.getInitVector());
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
		}
		
		byte[] data = cipher.doFinal(Base64.getDecoder().decode(envelope.getEnvelopeData()));
		Files.write(PLAINTEXT_PATH, data);
		
		System.out.println("envelope opened and saved at " + PLAINTEXT_PATH.normalize().toAbsolutePath());
	}

}
