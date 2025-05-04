package kr.co.itwillbs.de.orders.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MaterialManagerService {
	/**
	 * 1. 수주 -> 미출고 -> 출고완료 가는 프로세스
	 * 2. 구매 -> 미입고 -> 입고완료 가는 프로세스
	 * 9	2	세이프라인	50600	경남 양산시 물금읍 오봉로 76(세이프라인)	1검사장	품질검사장1 (입고)
	 * 10	2	퀄리티라인	06279	서울 강남구 삼성로57길 42(라이프라인)	2검사장	품질검사장2 (생산)
	 * 11	2	클리어체크	14789	경기 부천시 소사구 범안로219번길 95(클리어 빌딩)	3검사장	품질검사장3 (출고)
	 */
	
	/**
	 * <pre>
	 * [입고 신청]
	 * 1. t_order_items 에 구매한 정보를 가져온다
	 * 	-> order_document_number, material_idx, product_number, product_name, product_value
	 *  -> material_idx의 master_idx 를 가져옴
	 * 2. 데이터 개수에 맞게 끔 t_raw_material_quantity 대로 주문했다는 가정하에 / n을 하여
	 * 	-> t_warehouse_transaction 에 입력한다.
	 * 	-> source_location 에는 order_document_number, destination_location은 idx 5을 사용한다.
	 * 까지!
	 * 
	 * [입고 검수 대기]
	 * 1. t_warehouse_transaction 에서 status가 1인 데이터, 나머지는 위의 조건과 동일하게 되어 있을 것이다.
	 * 2. t_warehouse_transaction source가 5 -> 9로 간다.
	 * 3. t_raw_material.qc_type 컬럼과 t_qc_standard.type이 일치하는 조건들을 가져와
	 * 	-> t_qc_log에 데이터를 쌓는다.
	 * 	-> t_qc_log... 아.. lot이시여..
	 * 
	 * [입고 완료]
	 * 1. t_warehouse_transaction 테이블 데이터가
	 * 	-> destination이 9 -> 6으로 변경되어야 한다.
	 * 2. t_orders.order_document_number 의 status_code를 다음 값으로 바꾼다.
	 * 롯은 변화가 없다! 단지 t_warehouse_transaction 에 입출내역만 쌓인다!
	 * 
	 * [출고 신청]
	 * 1. t_order_items 에 수주 정보를 가져온다.
	 * ...
	 * </pre>
	 */
	public void OrderProcessStocking() {
		
	}
}
