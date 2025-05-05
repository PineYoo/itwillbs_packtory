package kr.co.itwillbs.de.common.constant;

public enum TransType {
	
	IN("1"),
	OUT("2"),
	MOVE("3"),
	QC("4");

	private final String code;

	TransType(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}
}
