package hr.fer.courses.aos.crypto.tmp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import com.google.gson.Gson;

import hr.fer.courses.aos.crypto.DigitalSignature;
import hr.fer.courses.aos.crypto.Util;

public class SignatureReader {
	
	private static final Path SIGNATURE_PATH = Paths.get("./data/signature.json");
	
	private static final String SENDER_KEYS_DIRECTORY = "./data/Alice/keys/";

	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		DigitalSignature digitalSign = gson.fromJson(new String(Files.readAllBytes(SIGNATURE_PATH)),
				DigitalSignature.class);
		
		Signature signature = Signature.getInstance(digitalSign.getMethod()[0] + "with" + digitalSign.getMethod()[1]);
	
		Path publicKeyPath = Paths.get(SENDER_KEYS_DIRECTORY + digitalSign.getMethod()[1].toLowerCase() + digitalSign.getKeyLength() + "PublicKey");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Files.readAllBytes(publicKeyPath));
        KeyFactory keyFactory = KeyFactory.getInstance(digitalSign.getMethod()[1].split("/")[0]);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        
        signature.initVerify(publicKey);
		
        signature.update(Files.readAllBytes(Paths.get(digitalSign.getFilePath())));
        boolean verified = signature.verify(Util.hextobyte(digitalSign.getSignature()));
        
        if(verified) {
        	System.out.println("signature verified");
        } else {
        	System.out.println("signature not verified");
        }
        
	}
	
}
