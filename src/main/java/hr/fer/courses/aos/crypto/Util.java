package hr.fer.courses.aos.crypto;

public class Util {

	public static byte[] hextobyte(String hex) {
		if(hex.length() % 2 != 0) {
			throw new IllegalArgumentException("Input string is odd-sized.");
		}
		
		hex = hex.toLowerCase();
		int len = hex.length();
		byte[] bs = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			char c = hex.charAt(i);
			char c2 = hex.charAt(i + 1);
			if (Character.digit(c, 16) == -1 || Character.digit(c2, 16) == -1) {
				throw new IllegalArgumentException("Input string has invalid characters.");
			}
			bs[i / 2] = (byte) ((Character.digit(c, 16) << 4) + Character.digit(c2, 16));
		}

		return bs;
	}
	
	public static String bytetohex(byte[] bs) {
		int len = bs.length;
		StringBuilder hexBuilder = new StringBuilder();

		for (int i = 0; i < len; i++) {
			hexBuilder.append(Integer.toHexString((bs[i] >>> 4) & 0xf));
			hexBuilder.append(Integer.toHexString(bs[i] & 0xf));
		}
		return hexBuilder.toString();
	}
	
}
