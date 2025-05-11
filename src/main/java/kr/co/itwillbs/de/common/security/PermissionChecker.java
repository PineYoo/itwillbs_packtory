package kr.co.itwillbs.de.common.security;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.itwillbs.de.admin.mapper.MenuPermissionMapper;
import kr.co.itwillbs.de.common.constant.PermissionType;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.vo.MenuPermissionVO;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 메뉴 권한을 체크해서 반환하는 클래스
 * user 객체에 대한 검증이 없음 
 * 이 클래스를 사용하기 먼저 (user != null)검증하고 이 클래스를 사용해야 함
 * </pre>
 */
@Slf4j
@Component
public class PermissionChecker {

	private final MenuPermissionMapper menuPermissionMapper;

	public PermissionChecker(MenuPermissionMapper menuPermissionMapper) {
		this.menuPermissionMapper = menuPermissionMapper;
	}

	/**
	 * 메뉴 권한 확인
	 * @param user @AuthenticationPrincipal CustomUserDetails user 시큐리티 로그인 유저
	 * @param menuIdx t_menu 에 등록된 idx 값
	 * @param type PermissionType.READ, PermissionType.WRITE, PermissionType.EXECUTE
	 * @return true: 권한 있음, false: 권한 없음
	 */
	public boolean hasPermission(CustomUserDetails user, Long menuIdx, PermissionType type) {
		// ※ 로그를 남기지 말지어다. 이 메서드를 호출하는 경우는 보통 menu를 다 가져와서 확인하기에 반복문이 붙어있다.
		//LogUtil.logStart(log); 

		// Admin은 항상 통과
		if (isAdmin(user))
			return true;

		List<MenuPermissionVO> permissions = menuPermissionMapper.findAllByMenuIdx(menuIdx);
		if (permissions.isEmpty())
			return false;

		PermissionScope scope = determineScope(user, permissions);

		return scope.hasPermission(type);
	}

	/**
	 * ADMIN 인지 확인해서 반환함
	 * @param user
	 * @return true: ADMIN 권한자, false: 사용자
	 */
	private boolean isAdmin(CustomUserDetails user) {
		// 개발자의 로망이 한줄 코딩이긴 해.. 따라갈 수 없는 영역이구나..
		return user.getAuthorities().stream().anyMatch(auth -> "ADMIN".equals(auth.getAuthority()));
	}

	/**
	 * <pre>
	 * 스코프를 어떤것을 보아야할지 결정하는 메서드
	 * permission_code = XYZ 의 필요한 부분을 알려준다.
	 * 1) Owner -> X 
	 * 2) Group -> Y
	 * 3) Others -> Z
	 * 순서로 검토 후
	 * </pre> 
	 * @param user
	 * @param permissions
	 * @return
	 */
	private PermissionScope determineScope(CustomUserDetails user, List<MenuPermissionVO> permissions) {
		for (MenuPermissionVO permission : permissions) {
			if (permission.getOwnerMemberId() != null && user.getUsername().equals(String.valueOf(permission.getOwnerMemberId()))) {
				return new PermissionScope(permission.getPermissionCode(), ScopeType.OWNER);
			}
		}

		for (MenuPermissionVO permission : permissions) {
			if (permission.getGroupId() != null && user.getGroupId().equals(String.valueOf(permission.getGroupId()))) {
				return new PermissionScope(permission.getPermissionCode(), ScopeType.GROUP);
			}
		}

		for (MenuPermissionVO permission : permissions) {
			if (permission.getOwnerMemberId() == null && permission.getGroupId() == null) {
				return new PermissionScope(permission.getPermissionCode(), ScopeType.OTHERS);
			}
		}

		return PermissionScope.empty();
	}

	private enum ScopeType {
		OWNER(0), GROUP(1), OTHERS(2);

		final int index;

		ScopeType(int index) {
			this.index = index;
		}
	}

	// Record!? (HREF: https://docs.oracle.com/en/java/javase/17/language/records.html )
	// 그러니까 PermissionScope 라는 이너 클래스고
	// 멤버 필드로 String permissionCode, ScopeType scopeType 이 있는 객체
	// new PermissionScope("740", ScopeType.OWNER) 이런 객체가 튀어나온다는 말이구만.
	// PermissionScope[permissionCode=740, scopeType=OWNER]
	// 우와...? 언제 어떻게 써보지? DTO 이걸로 하자!
	// Spring Framework 5.3부터는 Record 타입을 지원, 
	// Spring Boot 2.4 이상 버전에서는 @ModelAttribute로 Record 타입을 바인딩할 수 있다고 함
	// 게시판 다 만들었는데 뭘 만들어보지?
	/**
	 * @param permissionCode
	 * @param scopeType
	 * @return record?? 뇌정지당함 Java Record는 JDK 16부터 정식으로 도입된 불변(immutable) 데이터 홀더 클래스
	 */
	private record PermissionScope(String permissionCode, ScopeType scopeType) {
		// 네가 기본 생성자 역할이니?
		static PermissionScope empty() {
			return new PermissionScope("000", ScopeType.OTHERS);
		}

		boolean hasPermission(PermissionType type) {
			if (permissionCode == null || permissionCode.length() < 3)
				return false;

			char c = permissionCode.charAt(scopeType.index);
			int value = c - '0';

			// switch Expression (HREF: https://docs.oracle.com/en/java/javase/17/language/switch-expressions-and-statements.html )
			
			return switch (type) {
				case READ -> (value & 4) != 0;
				case WRITE -> (value & 2) != 0;
				case EXECUTE -> (value & 1) != 0;
			};
		}
	}
	
	public static void main(String...args) {
		//PermissionChecker pc = new PermissionChecker(null);
		
		System.out.println(new PermissionScope("740", ScopeType.OWNER));
		// PermissionScope[permissionCode=740, scopeType=OWNER]
		// 싱기하다..
	}
}
