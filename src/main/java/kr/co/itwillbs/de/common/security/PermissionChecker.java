package kr.co.itwillbs.de.common.security;

import org.springframework.stereotype.Component;

import kr.co.itwillbs.de.admin.mapper.MenuPermissionMapper;
import kr.co.itwillbs.de.common.constant.PermissionType;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.MenuPermissionVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PermissionChecker {

	private final MenuPermissionMapper menuPermissionMapper;

	public PermissionChecker(MenuPermissionMapper menuPermissionMapper) {
		this.menuPermissionMapper = menuPermissionMapper;
	}

	public boolean isOwner(CustomUserDetails user, Long menuIdx) {
		LogUtil.logStart(log);
		
		MenuPermissionVO permission = menuPermissionMapper.findByMenuIdx(menuIdx);
		if (permission == null || permission.getOwnerUserId() == null) {
			return false;
		}
		return permission.getOwnerUserId().equals(user.getUsername());
	}

	public boolean isGroup(CustomUserDetails user, Long menuIdx) {
		LogUtil.logStart(log);
		
		MenuPermissionVO permission = menuPermissionMapper.findByMenuIdx(menuIdx);
		if (permission == null || permission.getGroupId() == null) {
			return false;
		}
		return permission.getGroupId().equals(user.getGroupId());
	}

	public boolean isOthers(CustomUserDetails user, Long menuSeq) {
		LogUtil.logStart(log);
		
		return !isOwner(user, menuSeq) && !isGroup(user, menuSeq);
	}

	public boolean hasPermission(CustomUserDetails user, Long menuIdx, PermissionType type) {
		LogUtil.logStart(log);
		
		// Admin 일 경우 무조건 통과
		if (user.getAuthorities().stream().anyMatch(auth -> "ADMIN".equals(auth.getAuthority()))) {
			return true;
		}
		
		MenuPermissionVO permission = menuPermissionMapper.findByMenuIdx(menuIdx);
		if (permission == null || permission.getPermissionCode() == null) {
			return false;
		}

		char permissionChar;
		if (isOwner(user, menuIdx)) {
			permissionChar = permission.getPermissionCode().charAt(0); // 첫번째 자리
		} else if (isGroup(user, menuIdx)) {
			permissionChar = permission.getPermissionCode().charAt(1); // 두번째 자리
		} else {
			permissionChar = permission.getPermissionCode().charAt(2); // 세번째 자리
		}

		int permissionDigit = permissionChar - '0'; // 문자 to 숫자 변환

		switch (type) {
		case READ:
			return (permissionDigit & 4) != 0; // 읽기 (100)
		case WRITE:
			return (permissionDigit & 2) != 0; // 쓰기 (010)
		case EXECUTE:
			return (permissionDigit & 1) != 0; // 실행 (001)
		default:
			return false;
		}
	}
}
