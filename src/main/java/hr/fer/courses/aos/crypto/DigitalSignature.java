package hr.fer.courses.aos.crypto;

public class DigitalSignature {
	
	private final String description = "Signature";
	
	private String filePath;
	
	private String[] method;

	private int keyLength;
	
	private String signature;

	public DigitalSignature(String filePath, String[] method, int keyLength, String signature) {
		super();
		this.filePath = filePath;
		this.method = method;
		this.keyLength = keyLength;
		this.signature = signature;
	}

	public String getDescription() {
		return description;
	}

	public String getFilePath() {
		return filePath;
	}

	public String[] getMethod() {
		return method;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public String getSignature() {
		return signature;
	}

}
