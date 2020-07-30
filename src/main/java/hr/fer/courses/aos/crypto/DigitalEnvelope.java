package hr.fer.courses.aos.crypto;

public class DigitalEnvelope {

	private final String description = "Envelope";
	
	private String filePath;
	
	private String[] method;
	
	private int[] keyLength;
	
	private String initVector;
	
	private String envelopeData;
	
	private String envelopeCryptKey;

	public DigitalEnvelope(String filePath, String[] method, int[] keyLength, String initVector, String envelopeData,
			String envelopeCryptKey) {
		super();
		this.filePath = filePath;
		this.method = method;
		this.keyLength = keyLength;
		this.initVector = initVector;
		this.envelopeData = envelopeData;
		this.envelopeCryptKey = envelopeCryptKey;
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
	
}
