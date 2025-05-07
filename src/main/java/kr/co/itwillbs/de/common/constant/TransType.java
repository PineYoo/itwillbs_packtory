package kr.co.itwillbs.de.common.constant;

/**
 * <pre>
 * t_warehouse_transaction 의 trans_type과 일치하기위한 enum
 * 실제 공통코드에 입력된 코드값은 IN("1")과 OUT("2") 두개 임
 * 나머지 명령들은 자바코드를 제어하기위해 만들어졌다
 * </pre>
 */
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
