package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import kr.co.itwillbs.de.admin.dto.MemberDTO;
import kr.co.itwillbs.de.admin.mapper.MemberMapper;
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
	 * @return
	 */
	public List<MemberDTO> getBeforeMembers() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return memberMapper.getBeforeMembers();
	}

	/**
	 * t_employee (직원)를 t_member(사용자)로 입력
	 * <pre>
	 * 1)t_employee 정보를 List로 받아옴
	 * 2)password, role, status, isDeleted 정보를 작성함
	 * 3)t_member 테이블에 다중 인서트함
	 * </pre>
	 * @param memberDTOList
	 */
	public void registerMembers(List<MemberDTO> memberDTOList) {
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
	}

	/**
	 * 사용자 조회 페이지에서 사용할
	 * <br>t_member 에 입력된 사용자 조회
	 * @return
	 */
	public List<MemberDTO> getMembers() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<MemberDTO> memberDTOList = memberMapper.getMembers();
		return memberDTOList;
	}

	/**
	 * 상세(detail)에서 보여줄 사용자 조회
	 * @param memberId
	 * @return
	 */
	public MemberDTO getMember(String memberId) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return memberMapper.getMember(memberId);
	}

	/**
	 * 상세에서 업데이트 하는 것
	 * @param memberDTO
	 */
	public void modifyMember(MemberDTO memberDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//memberDTO 에 password값이 넘어왔을 때 다시 암호화해서 DB에 입력해야한다.
		if(!StringUtils.hasLength(memberDTO.getPassword())) {
			//TODO 패스워드 암호화할 부분
			memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
		}
		
		int affectedRow = memberMapper.modifyMember(memberDTO);
		
		log.info("affectedRow is {}", affectedRow);
		
		if(affectedRow < 1 ) {
			throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. \\n잠시 후 다시 시도해주시기 바랍니다.");
		}
	}
	
	public void resetPassword(MemberDTO memberDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		
	}

}
