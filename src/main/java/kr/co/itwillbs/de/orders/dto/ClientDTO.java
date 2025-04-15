package kr.co.itwillbs.de.orders.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredSessionIds(fields = {"regId", "modId"})
public class ClientDTO {
	
	private Long idx;
	
	//	거래처코드
	private String companyNumber;
	//	상호명
	private String companyName;
	//	대표자명
	private String ownerName;
	//	업태
	private String companyCategory;
	//	종목
	private String companySubject;
	//	전화번호
	private String phoneNumber;
	// E-mail
	// Jackson이 직렬화(또는 역직렬화)할 때 
	// 내부적으로 "카멜 케이스"를 다시 정의하면서 email로 바뀜
	// @JsonProperty 사용 시 JSON에서도 eMail로 보이게 됨
    @JsonProperty("eMail")	
	private String eMail;
	//	팩스번호
	private String faxNumber;
	//	우편번호
	private String postCode;
	//	주소1
	private String address1;
	//	주소2
	private String address2;
	//	여신한도
	private String creditLimit;
	//	삭제유무
	private String isDeleted;
	//	메모
	private String memo;
	//	상태
	private String status;
	//	상태_상세내용
	private String statusMessage;
	//	작성자
	private String regId;
	//	작성일자
	private LocalDateTime regDate;
	//	최종 작성자
	private String modId;
	//	최종작성일자
	private LocalDateTime modDate;
	
	// --------------------------------
	// t_client_info
	//	담당자 사번
	private String clientEmployeeId;
	//	담당자
	private String clientEmployeeName;
	//	담장자 메모
	private String clientEmployeeMemo;
	//	전화1
	private String phone1;
	//	전화2
	private String phone2;
	
	
}
