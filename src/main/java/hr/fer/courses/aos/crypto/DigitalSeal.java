package hr.fer.courses.aos.crypto;

public class DigitalSeal {

	private final String description = "Seal";
	
	private String filePath;
	
	private String[] method;
	
	private int[] keyLength;
	
	private String initVector;
	
	private String envelopeData;
	
	private String envelopeCryptKey;
	
	private String signature;

	public DigitalSeal(String filePath, String[] method, int[] keyLength, String initVector, String envelopeData,
			String envelopeCryptKey, String signature) {
		super();
		this.filePath = filePath;
		this.method = method;
		this.keyLength = keyLength;
		this.initVector = initVector;
		this.envelopeData = envelopeData;
		this.envelopeCryptKey = envelopeCryptKey;
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

	public int[] getKeyLength() {
		return keyLength;
	}

	public String getInitVector() {
		return initVector;
	}

	public String getEnvelopeData() {
		return envelopeData;
	}

	public String getEnvelopeCryptKey() {
		return envelopeCryptKey;
	}
	
	public String getSignature() {
		return signature;
	}
	
}
