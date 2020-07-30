package hr.fer.courses.aos.crypto.tmp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

import hr.fer.courses.aos.crypto.CryptoParams;

public class RSAKeyPairsGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeyPairsGenerator(int keysize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keysize);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        for(int keysize : CryptoParams.RSA_KEYSIZES) {
        	RSAKeyPairsGenerator keyPairGenerator = new RSAKeyPairsGenerator(keysize);
            keyPairGenerator.writeToFile("data/Bob/rsa" + keysize + "PublicKey", keyPairGenerator.getPublicKey().getEncoded());
            keyPairGenerator.writeToFile("data/Bob/rsa" + keysize + "PrivateKey", keyPairGenerator.getPrivateKey().getEncoded());
            
            keyPairGenerator = new RSAKeyPairsGenerator(Integer.valueOf(keysize));
            keyPairGenerator.writeToFile("data/Alice/rsa" + keysize + "PublicKey", keyPairGenerator.getPublicKey().getEncoded());
            keyPairGenerator.writeToFile("data/Alice/rsa" + keysize + "PrivateKey", keyPairGenerator.getPrivateKey().getEncoded());
        }
    }
}