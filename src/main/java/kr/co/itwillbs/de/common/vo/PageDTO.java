package kr.co.itwillbs.de.common.vo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageDTO {

	// 페이징
	private int page = 1; // 기본 1페이지
	private int size = 10; // 기본 10개씩
	private int totalCount = 0;

	// groupSize는 화면에 보여줄 페이지 수 (예: 5개씩)
	private final int groupSize = 5;

	// 계산 값
	public int getOffset() {
		return (page - 1) * size;
	}

	public int getTotalPage() {
		return (int) Math.ceil((double) totalCount / size);
	}

	public int getStartPageGroup() {
		return ((page - 1) / groupSize) * groupSize + 1;
	}

	public int getEndPageGroup() {
		int end = getStartPageGroup() + groupSize - 1;
		return Math.min(end, getTotalPage());
	}

	public boolean isHasPrevGroup() {
		return getStartPageGroup() > 1;
	}

	public boolean isHasNextGroup() {
		return getEndPageGroup() < getTotalPage();
	}

	// JPA 페이징 메서드 추가
	public Pageable toPageable(Sort sort) {
		return PageRequest.of(this.page - 1, this.size, sort);
	}
}
