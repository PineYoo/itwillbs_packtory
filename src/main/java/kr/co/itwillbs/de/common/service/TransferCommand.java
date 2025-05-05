package kr.co.itwillbs.de.common.service;

import kr.co.itwillbs.de.common.vo.TransferDTO;

public interface TransferCommand {

	// 몇몇 필드는 enum이 가능해보이고... 복잡하구만?
	
	public static final String IS_DELETE_N = "N";
	public static final String REG_ID_TRANSFER = "Transfer";
	public static final String REG_ID_QC_COMMAND = "QcCommander";
	public static final String IN_DESINATION_LOCATION_1 = "5";
	public static final String IN_DESINATION_LOCATION_2 = "6";
	public static final String IN_MEMO_PREFIX = "OI";
	public static final String QC_DESINATION_LOCATION_1 = "9";
	public static final String QC_DESINATION_LOCATION_2 = "10";
	public static final String QC_DESINATION_LOCATION_3 = "11";
	public static final String QC_RESULT_SUCCESS = "1";
	public static final String QC_RESULT_FAIL = "2";
	
	void execute(TransferDTO dto) throws Exception;
}
