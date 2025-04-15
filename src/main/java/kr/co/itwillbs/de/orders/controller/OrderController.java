package kr.co.itwillbs.de.orders.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.OrderCodeDTO;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;
import kr.co.itwillbs.de.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;

/* 수주 및 발주관리 */
@Slf4j
@RequestMapping("/orders") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;
	
	private final String COMMON_PATH = "/orders";
	private final String SELL_VALUE = "sell";
	private final String BUY_VALUE = "buy";
	private final String MATERIAL_VALUE = "material";
	private final String COMMON_MAJOR_CODE_TRADE = "ORDER_TRADE_CODE";
	private final String COMMON_MAJOR_CODE_STATUS = "ORDER_STATUS_CODE";
	private final String SELL_NAME_KR = "수주";
	private final String BUY_NAME_KR = "발주";
	private final String MATERIAL_NAME_KR = "구매";
	
	
	// 수주/발주 관리 목록 조회(SELECT) 요청(GET)
	@GetMapping("/{tradeName}") // "/orders/sell" or "/orders/buy"
	public String getOrderList(@PathVariable("tradeName") String tradeName, 
							   @ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 수주 혹은 발주만 남은 codeItemList를 orderSearchDTO에 set
		orderSearchDTO.setCodeItemList(this.getCodeItemsByTradeName(tradeName));
		System.out.println(">>>>>>>>>>>>>>tradeName : " + tradeName);
		System.out.println(">>>>>>>>>>>>>>getCodeItemList() : " + orderSearchDTO.getCodeItemList());
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		
		// 수주/발주 관리 목록 리스트 조회 요청(SELECT)
	    // 파라미터 : 공통코드에서 '수주' or '발주' 코드
 		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
 		System.out.println(">>>>>>>>>>>>>>tradeCode : " + tradeCode);
 		
 		// 각 minorName 이 일치하는 minorcode 뽑아내서 trade_code 에 넣기
 		if (SELL_VALUE.equals(tradeName)) {	// sell 일 때
 			orderSearchDTO.setTradeCode(getMinorCodeByMinorName(tradeCode, SELL_NAME_KR));	// '수주(1)'
 		} else if (BUY_VALUE.equals(tradeName)) {	// buy 일 때
 			orderSearchDTO.setTradeCode(getMinorCodeByMinorName(tradeCode, BUY_NAME_KR));	// '발주(2)'
 		} else if (MATERIAL_VALUE.equals(tradeName)) {	// material 일 때
 			orderSearchDTO.setTradeCode(getMinorCodeByMinorName(tradeCode, MATERIAL_NAME_KR));	// '구매(3)'
 		}
		
		// 수주/발주 관리 목록 리스트 조회 요청(SELECT)
		List<OrderDTO> orderDTOList = orderService.getOrderList(orderSearchDTO);
		model.addAttribute("orderDTOList", orderDTOList);
		log.info("orderDTOList : " + orderDTOList);
		
		//페이징용 totalCount
		orderSearchDTO.getPageDTO().setTotalCount(orderService.getOrderCountForPaging(orderSearchDTO));

		
		return COMMON_PATH + "/" + tradeName +"_management";	// "orders/sell_management" or "orders/buy_management"
//		return COMMON_PATH + "/order_management";
	}
	
	// 수주/발주 관리 목록 조회(SELECT) 요청(GET) - 검색 조건
	@GetMapping(value = {"/{tradeName}/search"}) // "/orders/sell/search" or "/orders/buy/search"
	public String getOrderListBySearchDTO(@PathVariable("tradeName") String tradeName, 
									  	  @ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("orderSearchDTO : {}", StringUtil.objToString(orderSearchDTO));
		
		// 수주 혹은 발주만 남은 codeItemList를 orderSearchDTO에 set
		orderSearchDTO.setCodeItemList(this.getCodeItemsByTradeName(tradeName));
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		
		// 수주/발주 관리 목록 리스트 조회 요청(SELECT)
		// 파라미터 : 공통코드에서 '수주' or '발주' 코드
		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		
		// 각 minorName 이 일치하는 minorcode 뽑아내서 trade_code 에 넣기
		if (SELL_VALUE.equals(tradeName)) {	// sell 일 때
			orderSearchDTO.setTradeCode(getMinorCodeByMinorName(tradeCode, SELL_NAME_KR));	// '수주(1)'
		} else if (BUY_VALUE.equals(tradeName)) {	// buy 일 때
			orderSearchDTO.setTradeCode(getMinorCodeByMinorName(tradeCode, BUY_NAME_KR));	// '발주(2)'
		}
		
		// 수주/발주 조건 검색 리스트 조회 요청(SELECT) - 재사용
		List<OrderDTO> orderDTOList = orderService.getOrderList(orderSearchDTO);
		model.addAttribute("orderDTOList", orderDTOList);
		log.info("orderDTOList : " + orderDTOList);
		
		//페이징용 totalCount
		orderSearchDTO.getPageDTO().setTotalCount(orderService.getOrderCountForPaging(orderSearchDTO));
		
		return COMMON_PATH + "/" + tradeName +"_management";	// "orders/sell_management" or "orders/buy_management"
//		return COMMON_PATH + "/order_management";
	}
	
	//------------------------------------------------------------------------------------------------
	// 주문서등록 페이지로 이동(GET)
	@GetMapping("/{tradeName}/regist")	// "/orders/sell/regist" or "/orders/buy/regist"
	public String getOrderRegisterForm(@PathVariable("tradeName") String tradeName, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 뷰 페이지에서 OrderDTO, OrderDetailDTO 에 기술된 Validation 항목 체크를 위해 빈 DTO 객체를 함께 전달
		model.addAttribute("orderFormDTO", new OrderFormDTO());
		
		// 거래처 정보 가져오기
		List<ClientDTO> clientList = orderService.getClientList();
		model.addAttribute("clientList", clientList);
		
		// 아이템 정보 가져오기 (공통으로 가져와라?!)
		// sell, buy 일 경우 상품 정보 | material(얘도 buy 라서 일단 자재(material) 라고 이름 지음) 일 경우 자재 정보 가져오기 ?? 이거 맞나
		// 발주 : 반제품같은거 발주하는거고, 구매 : 자재 발주 ??
		
		
		return COMMON_PATH + "/" + tradeName +"_register_form";	// "orders/sell_register_form" or "orders/buy_register_form"
	}
	
	@PostMapping(value= {"/{tradeName}/regist"}, consumes= {MediaType.APPLICATION_JSON_VALUE}) // "/orders/sell/regist" or "/orders/buy/regist"
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerOrderForJson(@PathVariable("tradeName") String tradeName, 
																	 @RequestBody @Valid OrderFormDTO orderFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(orderFormDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();

		// 공통코드 가져와서 trade_code, status_code 넣기
		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		List<CodeItemDTO> statusCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
		
		// 각 minorName 이 일치하는 minorcode 뽑아내서 trade_code 에 넣기
		if (SELL_VALUE.equals(tradeName)) {	// sell 일 때
			orderFormDTO.getOrderDTO().setTradeCode(getMinorCodeByMinorName(tradeCode, SELL_NAME_KR));  // '수주(1)'
			orderFormDTO.getOrderDTO().setStatusCode(getMinorCodeByMinorName(statusCode, "미출고"));	// 1
		} else if (BUY_VALUE.equals(tradeName)) {	// buy 일 때
			orderFormDTO.getOrderDTO().setTradeCode(getMinorCodeByMinorName(tradeCode, BUY_NAME_KR));   // '발주(2)'
			orderFormDTO.getOrderDTO().setStatusCode(getMinorCodeByMinorName(statusCode, "미수금"));	// 7
		}
		
		try {
			// 주문서 등록 요청(INSERT)
			orderService.registerOrder(orderFormDTO);
			
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아니었나? 하는 기억에 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
	
	// 주문서등록(INSERT) 요청하는 주소 매핑(POST)
//	@PostMapping("/{tradeName}/regist") // "/orders/sell/regist" or "/orders/buy/regist"
//	public String OrderRegister(@PathVariable("tradeName") String tradeName, 
//							    @ModelAttribute("orderFormDTO") @Valid OrderFormDTO orderFormDTO, 
//							    BindingResult bindingResult) {	// BindingResult : 폼 데이터를 검증할 때, 그 결과(오류들)를 담는 데 쓰임
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//
//		// 공통코드 가져와서 trade_code, status_code 넣기
//		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
//		List<CodeItemDTO> statusCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
//		
//		// 각 minorName 이 일치하는 minorcode 뽑아내서 trade_code 에 넣기
//		if (SELL_VALUE.equals(tradeName)) {	// sell 일 때
//			orderFormDTO.getOrderDTO().setTradeCode(getMinorCodeByMinorName(tradeCode, SELL_NAME_KR));  // '수주(1)'
//			orderFormDTO.getOrderDTO().setStatusCode(getMinorCodeByMinorName(statusCode, "미출고"));	// 1
//		} else if (BUY_VALUE.equals(tradeName)) {	// buy 일 때
//			orderFormDTO.getOrderDTO().setTradeCode(getMinorCodeByMinorName(tradeCode, BUY_NAME_KR));   // '발주(2)'
//			orderFormDTO.getOrderDTO().setStatusCode(getMinorCodeByMinorName(statusCode, "미수금"));	// 7
//		}
//		
//		// 주문서 등록 요청(INSERT)
//		orderService.registerOrder(orderFormDTO);
//		
//		// >>>>>>>>>> 상품 선택 및 등록은 나중에  <<<<<<<<<<
//
//		// 주문서 등록 후 주문서 상세 페이지로 이동
//		return "redirect:" + COMMON_PATH + "/" + tradeName + "/" + orderFormDTO.getOrderDTO().getDocumentNumber();	// "orders/sell/100001" or "orders/buy/100002"
//	}

	//------------------------------------------------------------------------------------------------
	// 주문서 상세 및 수정 페이지 매핑(GET)
	@GetMapping("/{tradeName}/{documentNumber}")	// "orders/sell/100001" or "orders/buy/100002"
	public String getOrderDetail(@PathVariable("tradeName") String tradeName, 
								 @PathVariable("documentNumber") String documentNumber, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println("documentNumber : "  + documentNumber);
		
		// 거래처 정보 가져오기
		List<ClientDTO> clientList = orderService.getClientList();
		model.addAttribute("clientList", clientList);
		
		OrderDTO orderDTO = orderService.getOrderByDocumentNumber(documentNumber);
		// 수주 혹은 발주만 남은 codeItemList를 orderDTO에 set
		orderDTO.setCodeItemList(this.getCodeItemsByTradeName(tradeName));
		
		log.info("orderDTO : " + orderDTO);
		model.addAttribute("orderDTO", orderDTO);
		
		return COMMON_PATH + "/" + tradeName + "_detail";	// "orders/sell_detail" or "orders/buy_detail"
	}
	
	// 주문서 수정(UPDATE) 요청하는 주소 매핑(POST)
	// => 히든메서드 필터에 의해 PUT 으로 변해야하지만 일단 POST 방식 사용
	@PostMapping("/{tradeName}/modify")		// "/orders/sell/modify" or "/orders/buy/modify"
	public String modifyOrder(@PathVariable("tradeName") String tradeName, 
							   @ModelAttribute("orderDTO") OrderDTO orderDTO, 
							   @ModelAttribute("orderDetailDTO") OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		System.out.println("orderDTO : " + orderDTO);
		System.out.println("orderDetailDTO : " + orderDetailDTO);

		// 주문 정보 수정
		orderService.modifyOrder(orderDTO, orderDetailDTO);
		
		return "redirect:" + COMMON_PATH + "/" + tradeName + "/" + orderDTO.getDocumentNumber();
	}

	// ========================================================================================
	// 주문서 내에 담당자, 담당자 전화번호 넣기 위함
	// 대분류 부서 목록
	@GetMapping("/api/departments/main")
	@ResponseBody
	public List<OrderCodeDTO> getDepartmentList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    return orderService.getDepartmentList(); // major_code 기준
	}

	// 하위 부서 목록
	@GetMapping("/api/departments/sub")
	@ResponseBody
	public List<OrderCodeDTO> getSubDepartmentList(@RequestParam("departmentCode") String departmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    return orderService.getSubDepartmentList(departmentCode); // 하위 minor_code
	}

	// 부서별 직원 목록
	@GetMapping("/api/employees/by-sub-dept")
	@ResponseBody
	public List<OrderCodeDTO> getEmployeeList(@RequestParam("departmentCode") String departmentCode,
											  @RequestParam("subDepartmentCode") String subDepartmentCode) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    return orderService.getEmployeeList(departmentCode, subDepartmentCode);
	}

	// 직원 전화번호 조회
	@GetMapping("/api/employees/info")
	@ResponseBody
	public OrderCodeDTO getEmployeeInfo(@RequestParam("employeeId") String employeeId) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    return orderService.getEmployeeInfo(employeeId);
	}
	
	
	// ========================================================================================
	/**
	 * 공통코드_아이템에서 수주 or 발주 관련된 상태값(status_code) 가져오기
	 * <br>COMMON_MAJOR_CODE_STATUS : \"ORDER_STATUS_CODE\"
	 * <br>SELL_NAME_KR : \"수주\"
	 * <br>BUY_NAME_KR : \"발주\"
	 * <br>MATERIAL_NAME_KR : \"구매\"
	 * @param tradeName
	 * @return
	 */
	private List<CodeItemDTO> getCodeItemsByTradeName(String tradeName) {
		//TODO 공통코드 가져오기 근데 고민해보자. 문제가 좀 있네? 지금 이 방법이 맞냐?
//		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
		// 공통코드 원본 리스트
		List<CodeItemDTO> originList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
		// 방어적 복사 (원본 건드리지 않도록)
		List<CodeItemDTO> codeItemList = new ArrayList<>(originList);
		
		for (Iterator<CodeItemDTO> it = codeItemList.iterator(); it.hasNext();) {
			CodeItemDTO item = it.next();
			// 수주가 아니면 -1이니까 삭제하라!
			if (SELL_VALUE.equals(tradeName) && item.getDescription().indexOf(SELL_NAME_KR) < 0) it.remove(); 
			// 발주가 아니면 -1이니까 삭제하라!
			if (BUY_VALUE.equals(tradeName) && item.getDescription().indexOf(BUY_NAME_KR) < 0) it.remove(); 
			// 구매가 아니면 -1이니까 삭제하라!
			if (MATERIAL_VALUE.equals(tradeName) && item.getDescription().indexOf(MATERIAL_NAME_KR) < 0) it.remove(); 
		}
		return codeItemList;
	}
	
	/**
	 * 특정 minorName을 가진 minorCode를 반환하는 유틸 메서드
	 * @param codeList
	 * @param targetName
	 * @return
	 */
	private String getMinorCodeByMinorName(List<CodeItemDTO> codeList, String targetName) {
		for (CodeItemDTO item : codeList) {
			if (targetName.equals(item.getMinorName())) {
				return item.getMinorCode();
			}
		}
		return null;
	}
	
}
