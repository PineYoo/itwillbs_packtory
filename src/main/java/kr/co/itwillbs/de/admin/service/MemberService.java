package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import kr.co.itwillbs.de.admin.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.admin.dto.MemberDTO;
import kr.co.itwillbs.de.admin.dto.MemberSearchDTO;
import kr.co.itwillbs.de.admin.mapper.MemberMapper;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {
	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
		this.memberMapper = memberMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	/**
	 * 사용자 등록용 직원 조회하기 -> t_employee 정보를 가져옴
	 * <br>t_member.status IS NULL 조건으로 가져오고 IFNULL||NVL('NOT_YET')으로 화면에 보여줌 
	 * @return List<MemberDTO>
	 */
	@Transactional(readOnly = true)
	public List<MemberDTO> getBeforeMembers() {
		LogUtil.logStart(log);
		
		return memberMapper.getBeforeMembers();
	}

	/**
	 * 사용자 등록용 직원 검색 조건 조회 카운트 - 페이징용
	 * @param employeeSearchDTO
	 * @return List<MemberDTO>
	 */
	@Transactional(readOnly = true)
	public int getBeforeMembersCountByEmployeeSearch(EmployeeSearchDTO employeeSearchDTO) {
		LogUtil.logStart(log);
		
		return memberMapper.getBeforeMembersCountByEmployeeSearch(employeeSearchDTO);
	}
	
	/**
	 * 사용자 등록용 직원 검색 조건 조회
	 * @param employeeSearchDTO
	 * @return List<MemberDTO>
	 */
	@Transactional(readOnly = true)
	public List<MemberDTO> getBeforeMembersByEmployeeSearch(EmployeeSearchDTO employeeSearchDTO) {
		LogUtil.logStart(log);
		
		return memberMapper.getBeforeMembersByEmployeeSearch(employeeSearchDTO);
	}
	
	/*
	public void OldregisterMembers(List<MemberDTO> memberDTOList) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// t_empoyee.id 를 가지고 데이터를 다시 가져온다.
		List<MemberDTO> memberList = memberMapper.getBeforeMembersByIds(memberDTOList);
		log.info("before memberList in {}", memberList);
		if(memberList.size() > 0) {
			for(MemberDTO member : memberList) {
				// 암호 만들기
				if(member.getSsn().length() > 6) {
					//스프링 시큐리티 이후 이걸로 암호 만들어서 넣는다. 기본 암호는 생년월일 6자리이다.
					member.setPassword(passwordEncoder.encode(member.getSsn().substring(0, 6)));
				}
				// 기본 값 셋
				member.setRole("4");	// 1: Admin, 2: Manger, 3: employee, 4: user, 5: visitor
				member.setStatus("1");	// 사용중
				member.setIsDeleted("N"); //삭제되지 않음
				member.setRegId("superUser");
			}
		}
		log.info("after memberList in {}", memberList);
		
		int affectedRow = memberMapper.registerMembers(memberList);
		log.info("memberDTOList.size(): {}, memberList.size(): {}, affectedRow: {}", memberDTOList.size(), memberList.size(), affectedRow);
		if(memberDTOList.size() != affectedRow) {
			log.error("dtoList.size() {} , affectedRow {}", memberDTOList.size(), affectedRow);
			throw new IllegalStateException("요청한 입력 개수와 처리된 개수가 같지 않습니다.");
		}
	}*/
	/**
	 * t_employee (직원)를 t_member(사용자)로 입력
	 * <pre>
	 * 1)t_employee 정보를 List로 받아옴 -> registerMembersPartOne
	 * 2)password, role, status, isDeleted 정보를 작성함 -> registerMembersPartTwo
	 * 3)t_member 테이블에 다중 인서트함
	 * </pre>
	 * @param memberDTOList
	 */
	@Transactional
	public List<MemberDTO> registerMembersPartOne(List<MemberDTO> memberDTOList) {
		LogUtil.logStart(log);
		
		// t_empoyee.id 를 가지고 데이터를 다시 가져온다.
		List<MemberDTO> memberList = memberMapper.getBeforeMembersByIds(memberDTOList);
//		log.info("before memberList in {}", memberList);
		return memberList;
	}
	
	private final String ROLE_USER = "4";
	private final String STATUS_USED = "1";
	private final String IS_DELETED_N = "N";
	
	/**
	 * AOP 작업을 위해 INSERT 작업 메서드를 별도로 처리
	 * @param memberDTOList
	 * @return
	 */
	@LogExecution // 로그 남길 서비스
	@Transactional
	public void registerMembersPartTwo(List<MemberDTO> memberDTOList) {
		LogUtil.logStart(log);
		if(memberDTOList.size() > 0) {
			for(MemberDTO member : memberDTOList) {
				// 암호 만들기
				if(member.getSsn().length() > 6) {
					//스프링 시큐리티 이후 이걸로 암호 만들어서 넣는다. 기본 암호는 생년월일 6자리이다.
					member.setPassword(passwordEncoder.encode(member.getSsn().substring(0, 6)));
				}
				// 기본 값 셋
				member.setRole(ROLE_USER);	// 1: Admin, 2: Manger, 3: employee, 4: user, 5: visitor
				member.setStatus(STATUS_USED);	// 사용중
				member.setIsDeleted(IS_DELETED_N); //삭제되지 않음
				//member.setRegId("superUser");
			}
		}
//		log.info("after memberList in {}", memberDTOList);
		
		int affectedRow = memberMapper.registerMembers(memberDTOList);;
		
//		log.info("memberDTOList.size(): {}, affectedRow: {}", memberDTOList.size(), affectedRow);
		if(memberDTOList.size() != affectedRow) {
//			log.error("dtoList.size() {} , affectedRow {}", memberDTOList.size(), affectedRow);
			throw new IllegalStateException("요청한 입력 개수와 처리된 개수가 같지 않습니다.");
		}
	}
	
	/**
	 * 사용자 조회 페이지에서 사용할
	 * <br>t_member 에 입력된 사용자 조회
	 * @return List<MemberDTO>
	 */
	@Transactional(readOnly = true)
	public List<MemberDTO> getMembers() {
		LogUtil.logStart(log);
		
		List<MemberDTO> memberDTOList = memberMapper.getMembers();
		return memberDTOList;
	}
	
	/**
	 * 사용자 조회 검색 조건 조회 카운트 - 페이징용
	 * @param memberSearchDTO
	 * @return List<MemberDTO>
	 */
	@Transactional(readOnly = true)
	public int getMembersCountBySearchDTO(MemberSearchDTO memberSearchDTO) {
		LogUtil.logStart(log);
		
		return memberMapper.getMembersCountBySearchDTO(memberSearchDTO);
	}
	
	/**
	 * 사용자 조회 검색 조건 조회
	 * @param memberSearchDTO
	 * @return List<MemberDTO>
	 */
	@Transactional(readOnly = true)
	public List<MemberDTO> getMembersBySearchDTO(MemberSearchDTO memberSearchDTO) {
		LogUtil.logStart(log);
		
		List<MemberDTO> memberDTOList = memberMapper.getMembersBySearchDTO(memberSearchDTO);
		return memberDTOList;
	}

	/**
	 * 상세(detail)에서 보여줄 사용자 조회
	 * @param memberId
	 * @return
	 */
	@Transactional(readOnly = true)
	public MemberDTO getMember(String memberId) {
		LogUtil.logStart(log);
		
		return memberMapper.getMember(memberId);
	}

	/**
	 * 상세에서 업데이트 하는 것
	 * 너도 AOP가 적용되어야 한다.
	 * @param memberDTO
	 */
	@LogExecution // 로그 남길 서비스
	@Transactional
	public void modifyMember(MemberDTO memberDTO) {
		LogUtil.logStart(log);
		
//		log.info("password is {}", memberDTO.getPassword());
		//memberDTO 에 password값이 넘어왔을 때 다시 암호화해서 DB에 입력해야한다.
		if(StringUtils.hasLength(memberDTO.getPassword())) {
//			log.info("패스워드가 있데?");
			memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
		} else {
//			log.info("패스워드가 없어야하는데?");
			// 그렇지 않으면 마이 바티스 업데이트를 하지 않기 위한 처리
			// <if test="password !=null and !(''.equals(password.trim()))">password = #{password},</if>
			memberDTO.setPassword(null);
		}
		
		int affectedRow = memberMapper.modifyMember(memberDTO);
		
//		log.info("affectedRow is {}", affectedRow);
		
		if(affectedRow < 1 ) {
			throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. \\n잠시 후 다시 시도해주시기 바랍니다.");
		}
	}
	
	/**
	 * <pre>
	 * 어라? 패스워드 리셋을 구현 안했네?
	 * 1. 사번 또는 이름으로 검색해서 사번을 찾는 걸 만들어야하나? 여튼 사번을 입력받음
	 * 2. 사번으로 회원정보를 조회함
	 * 3. 기본 암호로 회원정보를 업데이트함
	 * </pre>
	 * @param memberDTO
	 */
	@LogExecution // 로그 남길 서비스
	@Transactional
	public void resetPassword(MemberDTO memberDTO) {
		LogUtil.logStart(log);
		
		/*
		if(member.getSsn().length() > 6) {
			//스프링 시큐리티 이후 이걸로 암호 만들어서 넣는다. 기본 암호는 생년월일 6자리이다.
			member.setPassword(passwordEncoder.encode(member.getSsn().substring(0, 6)));
		}
		 * */
	}


}
