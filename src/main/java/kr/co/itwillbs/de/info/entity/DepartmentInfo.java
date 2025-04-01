package kr.co.itwillbs.de.info.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.itwillbs.de.info.dto.DepartmentInfoDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_department_info")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DepartmentInfo {

	@Id
	@Column(name = "idx")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx; // 인덱스
	
	@Column(length = 10, nullable = false)
	private String departmentCode; // 대표부서코드
	
	@Column(length = 10, nullable = false)
	private String parentCode; // 부모코드
	
	@Column(length = 10, nullable = false)
	private String childCode; // 하위부서코드
	
	@Column(length = 50, nullable = false)
	private String childName; // 하위부서이름
	
	@Column(length = 10, nullable = false)
	private String rankNumber; // 정렬순서
	
	@Column(length = 2)
	private String isDeleted;
	
	@Column(length = 10, nullable = false)
	private String locationIdx; // 장소참조
	
	@Column(length = 50, nullable = false)
	private String regId; // 작성자
	
	@CreatedDate	// 엔티티 생성 시점에 생성 시간 자동 기록
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") // 출력 시 사용할 포멧 지정
	private LocalDateTime regDate; // 작성일자시간
	
	@Column(length = 50, nullable = false)
	private String modId; // 최종작성자
	
	@LastModifiedDate	// 엔티티 수정 되는 시점에 수정 시간 자동 기록
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") // 출력 시 사용할 포멧 지정
	private LocalDateTime modDate; // 최종작성일자
	
	@PrePersist
	public void prePersist() {
	    if (this.isDeleted == null) {
	        this.isDeleted = "N";
	    }
	}
	
	// 빌더 패턴(Builder Pattern)으로 객체 생성하기 위한 파라미터 생성자 정의(생성자에 @Builder 어노테이션 추가)
	// => @Id 필드를 제외한 나머지 필드에 대한 정의
	@Builder
	public DepartmentInfo(String departmentCode, String parentCode, String childCode, String childName, 
						  String rankNumber, String isDeleted, String locationIdx, String regId,
						  LocalDateTime regDate, String modId, LocalDateTime modDate) {
	    this.departmentCode = departmentCode;
	    this.parentCode = parentCode;
	    this.childCode = childCode;
	    this.childName = childName;
	    this.rankNumber = rankNumber;
	    this.isDeleted = isDeleted;
	    this.locationIdx = locationIdx;
	    this.regId = regId;
	    this.regDate = regDate;
	    this.modId = modId;
	    this.modDate = modDate;
	   }
	
	// Item(엔티티) -> ItemDTO -> 변환하는 toDTO() 메서드 정의
	public DepartmentInfoDTO toDto() {
	    return DepartmentInfoDTO.builder()
	            .idx(idx)
	            .departmentCode(departmentCode)
	            .parentCode(parentCode != null ? parentCode : "0") // ✅ null이면 "0"으로 처리
	            .childCode(childCode)
	            .childName(childName != null ? childName : "이름 없음") // ✅ null이면 "이름 없음"으로 처리
	            .rankNumber(rankNumber != null ? rankNumber : "0") // ✅ null이면 "0"으로 처리
	            .isDeleted(isDeleted)
	            .locationIdx(locationIdx != null ? locationIdx : "0") // ✅ null이면 "0"으로 처리
	            .regId(regId)
	            .regDate(regDate != null ? regDate : LocalDateTime.now()) // ✅ null이면 현재 시간으로 처리
	            .modId(modId)
	            .modDate(modDate != null ? modDate : LocalDateTime.now()) // ✅ null이면 현재 시간으로 처리
	            .build();
	}

	// 엔티티를 업데이트 하는 메서드
	public void updateFromDTO(DepartmentInfoDTO departmentInfoDTO) {
		this.departmentCode = departmentInfoDTO.getDepartmentCode();
	    this.parentCode = departmentInfoDTO.getParentCode();
	    this.childCode = departmentInfoDTO.getChildCode();
	    this.childName = departmentInfoDTO.getChildName();
	    this.rankNumber = departmentInfoDTO.getRankNumber();
	    this.isDeleted = departmentInfoDTO.getIsDeleted();
	    this.locationIdx = departmentInfoDTO.getLocationIdx();
	    if (departmentInfoDTO.getRegId() != null) {
	        this.regId = departmentInfoDTO.getRegId();
	    }
	    if (departmentInfoDTO.getRegDate() != null) {
	        this.regDate = departmentInfoDTO.getRegDate();
	    }
	    this.modId = departmentInfoDTO.getModId();
	    this.modDate = LocalDateTime.now();
	   }
}
