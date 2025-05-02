package kr.co.itwillbs.de.mes.dto;

/*
 * DB에서 받아오는걸 record를 써보고 싶어서 써봤는데
 * new 한 뒤에 생성자로 데이터를 집어넣기 때문에 select 에 있는 column과 갯수 순서가 모두 일치해야한다.
 * 유지보수를 위해 그냥 DTO class를 만드는게 정신건강에 좋을 것 같다.
 * */
public record QcRequiredLogDTO (
	String idx,
	String transType,
	String transTypeName,
	String code,
	String productIdx,
	String materialIdx,
	String unit,
	String unitName,
	String quantity,
	String status,
	//String statusName,
	String lotIdx,
	String lotNumber,
	String qcLogIdx,
	String qcResult,
	String qcResultName,
	String qcName,
	String qcTypeName,
	String regId,
	String regDate,
	String modId,
	String modDate
) {}
