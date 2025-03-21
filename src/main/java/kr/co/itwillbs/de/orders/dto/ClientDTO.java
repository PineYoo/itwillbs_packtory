package kr.co.itwillbs.de.orders.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClientDTO {
	
	private Long idx;
	
	private String code;				//	거래처코드
	private String companyName;			//	상호명
	private String ownerName;			//	대표자명
	private String companyCategory;		//	업태
	private String companySubject;		//	종목
	private String phoneNumber;			//	전화번호
	private String eMail;				//	E-mail
	private String faxNumber;			//	팩스번호
	private String address1;			//	주소1
	private String address2;			//	주소2
	private String creditLimit;			//	여신한도
	private String isDeleted;			//	삭제유무
	private String memo;				//	메모
	private String status;				//	상태
	private String statusMessage;		//	상태_상세내용
	private String regId;				//	작성자
	private LocalDateTime regDate;		//	작성일자
	private String modId;				//	최종 작성자
	private LocalDateTime modDate;		//	최종작성일자
	
}
