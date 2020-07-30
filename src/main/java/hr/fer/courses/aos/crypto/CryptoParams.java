package hr.fer.courses.aos.crypto;

import java.util.HashMap;
import java.util.Map;

public class CryptoParams {

	public static final String[] PROGRAM_FUNCTIONS = new String[] { "envelope", "signature",
			"seal" };
	
	public static final String[] SYMMETRIC_CIPHERS = new String[] { "AES", "DESede" };
	
	private static final Integer[] AES_KEYSIZES = new Integer[] { 192, 128, 256 };
	private static final Integer[] DESede_KEYSIZES = new Integer[] { 168 };
	public static final Integer[] RSA_KEYSIZES = new Integer[] { 1024, 2048, 4096 };
	
	private static final String[] AES_FEEDBACK_MODES = new String[] { "CBC", "OFB", "CFB", "ECB" };
	private static final String[] DESede_FEEDBACK_MODES = new String[] { "ECB", "CBC", "CFB", "OFB" };
	
	public static final Integer[] SHA1_HASHSIZES = new Integer[] { 512, 384, 256, 224 };
	
	public static final Map<String, Integer[]> SYMMETRIC_CIPER_TO_KEYSIZES;
	public static final Map<String, String[]> SYMMETRIC_CIPER_TO_FEEDBACK_MODES;
	
	static {
		//SYMMETRIC_CIPER_TO_KEYSIZES = Map.of("AES", AES_KEYSIZES, "DESede", DESede_KEYSIZES);
		SYMMETRIC_CIPER_TO_KEYSIZES = new HashMap<>();
		SYMMETRIC_CIPER_TO_KEYSIZES.put("AES", AES_KEYSIZES);
		SYMMETRIC_CIPER_TO_KEYSIZES.put("DESede", DESede_KEYSIZES);
		//SYMMETRIC_CIPER_TO_FEEDBACK_MODES = Map.of("AES", AES_FEEDBACK_MODES, "DESede", DESede_FEEDBACK_MODES);
		SYMMETRIC_CIPER_TO_FEEDBACK_MODES = new HashMap<>();
		SYMMETRIC_CIPER_TO_FEEDBACK_MODES.put("AES", AES_FEEDBACK_MODES);
		SYMMETRIC_CIPER_TO_FEEDBACK_MODES.put("DESede", DESede_FEEDBACK_MODES);
	}
	
	public static final String PADDING_SCHEME = "ISO10126Padding";

}
