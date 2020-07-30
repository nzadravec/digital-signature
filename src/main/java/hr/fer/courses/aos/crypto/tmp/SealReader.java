package hr.fer.courses.aos.crypto.tmp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.google.gson.Gson;

import hr.fer.courses.aos.crypto.DigitalSeal;
import hr.fer.courses.aos.crypto.Util;

public class SealReader {

	private static final Path SEAL_PATH = Paths.get("./data/seal.json");
	private static final Path PLAINTEXT_PATH = Paths.get("./data/plaintext.txt");

	private static final String RECEIVER_KEYS_DIRECTORY = "./data/Bob/keys/";
	private static final String SENDER_KEYS_DIRECTORY = "./data/Alice/keys/";

	public static void main(String[] args) throws Exception {

		Gson gson = new Gson();
		DigitalSeal seal = gson.fromJson(new String(Files.readAllBytes(SEAL_PATH)), DigitalSeal.class);
		
		Signature signature = Signature.getInstance(seal.getMethod()[2] + "with" + seal.getMethod()[1]);
		
		Path publicKeyPath = Paths.get(SENDER_KEYS_DIRECTORY + seal.getMethod()[1].toLowerCase() + seal.getKeyLength()[1] + "PublicKey");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Files.readAllBytes(publicKeyPath));
        KeyFactory publicKeyFactory = KeyFactory.getInstance(seal.getMethod()[1].split("/")[0]);
        PublicKey publicKey = publicKeyFactory.generatePublic(publicKeySpec);
        
        signature.initVerify(publicKey);
		
        String envelope = seal.getEnvelopeData() + seal.getEnvelopeCryptKey();
        signature.update(envelope.getBytes());
        boolean verified = signature.verify(Util.hextobyte(seal.getSignature()));
        
        if(verified) {
        	System.out.println("seal verified");
        } else {
        	System.out.println("seal not verified");
        	System.exit(0);
        }

		Path privateKeyPath = Paths.get(RECEIVER_KEYS_DIRECTORY + seal.getMethod()[1].toLowerCase() + seal.getKeyLength()[1] + "PrivateKey");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Files.readAllBytes(privateKeyPath));
		KeyFactory privateKeyFactory = KeyFactory.getInstance(seal.getMethod()[1].split("/")[0]);
		PrivateKey privateKey = privateKeyFactory.generatePrivate(privateKeySpec);

		Cipher acipher = Cipher.getInstance(seal.getMethod()[1]);
		acipher.init(Cipher.UNWRAP_MODE, privateKey);

		String cipherAlgorithm = seal.getMethod()[0];
		String[] cipherAlgParams = seal.getMethod()[0].split("/");

		SecretKey secretKey = (SecretKey) acipher.unwrap(Util.hextobyte(seal.getEnvelopeCryptKey()), cipherAlgParams[0],
				Cipher.SECRET_KEY);
		
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		if (cipherAlgParams.length == 1 || cipherAlgorithm.split("/")[1].equals("ECB")) {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
		} else {
			byte[] iv = Util.hextobyte(seal.getInitVector());
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
		}
		
		byte[] data = cipher.doFinal(Base64.getDecoder().decode(seal.getEnvelopeData()));
		Files.write(PLAINTEXT_PATH, data);
		
		System.out.println("seal opened and saved at " + PLAINTEXT_PATH.normalize().toAbsolutePath());
	}

}
