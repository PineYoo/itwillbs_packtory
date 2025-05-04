package kr.co.itwillbs.de.orders.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.orders.service.MaterialManagerService;
import kr.co.itwillbs.de.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/orders/etc")
@Controller
public class MaterialManagerController {
/**
 * 필요한 페이지
 * 1. 납품 현황
 * 2. 입고 현황
 * 3. 자재 입고 현황
 * 4. 자재 재고 조회
 */
	
	private final MaterialManagerService materialManagerService;
	private final OrderService orderService;
	
	public MaterialManagerController(MaterialManagerService materialManagerService,
								OrderService orderService) {
		this.materialManagerService = materialManagerService;
		this.orderService = orderService;
	}
	
	private final String VIEW_PATH = "/orders/etc";
	
	@GetMapping("")
	public String getShippingInfo() {
		LogUtil.logStart(log);
		return VIEW_PATH;
	}
	
	@PostMapping("")
	public void setShippingInfo() {
		LogUtil.logStart(log);
		
	}
	
	public String getWarehouseResourcesInfo() {
		LogUtil.logStart(log);
		
		return VIEW_PATH;
	}
	
}