package kr.co.itwillbs.de.employee.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.itwillbs.de.commute.constant.WorkStatusCode;
import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_employee")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Employee {

	@Id
	@Column(name = "idx")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idx; // 테이블 인덱스(컬럼명: idx)
	private String id;	// 사원번호
	private String name;	// 이름
	private String department_code;	// 부서코드
	private String sub_department_code;	// 하위부서코드
	private String position_code;	// 직급코드
	private String phone_number;	// 전화번호
	private String email;	// 이메일
	private String gender;	// 성별
	private String ssn;	// 주민등록번호
	private String address1;	// 주소1
	private String address2;	// 주소2
	
	@DateTimeFormat(pattern="yyyy-MM-dd") // 출력 시 사용할 포멧 지정
	private LocalDate hire_date;	// 입사일

	@DateTimeFormat(pattern="yyyy-MM-dd") // 출력 시 사용할 포멧 지정
	private LocalDate resignation_date;	// 퇴사일
	
	private String work_experience;	// 경력
	private String file_idxs;	// 프로필사진
	private String salary_bank_code;	// 급여은행코드
	private String salary_bank_name;	// 급여은행이름
	private String salary_account_number;	// 급여계좌번호
	private String salary_account_holder;	// 급여계좌예금주
	private String has_vehicle;	// 직원차량유무
	private String employee_status_code;	// 직원상태코드

	@CreatedDate	// 엔티티 생성 시점에 생성 시간 자동 기록
	@DateTimeFormat(pattern="yyyy-MM-dd") // 출력 시 사용할 포멧 지정
	private LocalDate status_start_date;	// 직원상태시작일자

	@DateTimeFormat(pattern="yyyy-MM-dd") // 출력 시 사용할 포멧 지정
	private LocalDate status_end_date;	// 직원상태종료일자


	// 빌더 패턴(Builder Pattern)으로 객체 생성하기 위한 파라미터 생성자 정의(생성자에 @Builder 어노테이션 추가)
	// => @Id 필드를 제외한 나머지 필드에 대한 정의
	@Builder
	public Employee(String id, String name, String department_code, String sub_department_code, String position_code,
			String phone_number, String email, String gender, String ssn, String address1, String address2,
			LocalDate hire_date, LocalDate resignation_date, String work_experience, String file_idxs,
			String salary_bank_code, String salary_bank_name, String salary_account_number,
			String salary_account_holder, String has_vehicle, String employee_status_code, LocalDate status_start_date,
			LocalDate status_end_date) {
		this.id = id;
		this.name = name;
		this.department_code = department_code;
		this.sub_department_code = sub_department_code;
		this.position_code = position_code;
		this.phone_number = phone_number;
		this.email = email;
		this.gender = gender;
		this.ssn = ssn;
		this.address1 = address1;
		this.address2 = address2;
		this.hire_date = hire_date;
		this.resignation_date = resignation_date;
		this.work_experience = work_experience;
		this.file_idxs = file_idxs;
		this.salary_bank_code = salary_bank_code;
		this.salary_bank_name = salary_bank_name;
		this.salary_account_number = salary_account_number;
		this.salary_account_holder = salary_account_holder;
		this.has_vehicle = has_vehicle;
		this.employee_status_code = employee_status_code;
		this.status_start_date = status_start_date;
		this.status_end_date = status_end_date;
	}
	
	
}
