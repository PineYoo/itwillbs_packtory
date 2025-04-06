package kr.co.itwillbs.de.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.admin.dto.MemberDTO;

@Mapper
public interface MemberMapper {
	
	/**
	 * SELECT FROM T_EMPLOYEE LEFT JOIN T_MEMBER ... WHERE T_MEMBER.STATUS IS NULL
	 * @return
	 */
	List<MemberDTO> getBeforeMembers();
	
	/**
	 * SELECT FROM T_EMPLOYEE LEFT JOIN T_MEMBER ... WHERE T_MEMBER.STATUS IS NULL AND EMP.ID in #{memberDTOList.id}
	 * @return
	 */
	List<MemberDTO> getBeforeMembersByIds(List<MemberDTO> memberDTOList);

	/**
	 * INSERT INTO T_MEMBER values MemberDTO
	 * @param memberDTOList
	 * @return
	 */
	int registerMembers(List<MemberDTO> memberDTOList);

	/**
	 * SELECT FROM T_EMPLOYEE LEFT JOIN T_MEMBER ... WHERE T_MEMBER.STATUS IS NOT NULL;
	 * @return
	 */
	List<MemberDTO> getMembers();

	/**
	 * SELECT FROM T_MEMBER
	 * @param memberId
	 * @return
	 */
	MemberDTO getMember(String memberId);

	/**
	 * UPDATE T_MEMBER SET memberDTO where idx = #{memberDTO.idx}
	 * @param memberDTO
	 */
	int modifyMember(MemberDTO memberDTO);
}
