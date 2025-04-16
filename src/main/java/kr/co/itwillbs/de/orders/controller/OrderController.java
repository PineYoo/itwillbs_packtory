package kr.co.itwillbs.de.orders.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;
import kr.co.itwillbs.de.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/orders") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
@Controller
public class OrderController {
	
	private OrderService orderService;
	private CommonCodeUtil commonCodeUtil;
	
	public OrderController(OrderService orderService, CommonCodeUtil commonCodeUtil) {
		this.orderService = orderService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	private final String COMMON_PATH = "/orders";
	private final String COMMON_MAJOR_CODE_TRADE = "ORDER_TRADE_CODE";
	private final String COMMON_MAJOR_CODE_STATUS = "ORDER_STATUS_CODE";
	private final String SELL_VALUE = "sell";
	private final String BUY_VALUE = "buy";
	private final String MATERIAL_VALUE = "material";
	private final String SELL_NAME_KR = "수주";
	private final String BUY_NAME_KR = "발주";
	private final String MATERIAL_NAME_KR = "구매";
	private final String SELL_DEFAULT_STATUS_CODE = "미출고";
	private final String BUY_DEFAULT_STATUS_CODE = "미수금";
	private final String MATERIAL_DEFAULT_STATUS_CODE = "미입고";
	
	/**
	 * {수주,발주,구매} > tradeName > 거래에 따른 리스트 조회(GET)
	 * @param tradeName : "/orders/sell" || "/orders/buy" || "/orders/material"
	 * @param orderSearchDTO : view(html).searchForm
	 * @param model : orderSearchDTO, orderDTOList
	 * @return viewResolver : orders_management
	 */
	@GetMapping("/{tradeName}")
	public String getOrderList(@PathVariable("tradeName") String tradeName, 
								@ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("tradeName is {}", tradeName);
		// 검색폼.statusCodes 에 사용할 공통코드 조회 결과 셋
		orderSearchDTO.setStatusList(this.getStatusCodesByTradeName(tradeName));
		// view 페이지 통합을 위한 코드 값 추가
		orderSearchDTO.setTradeCode(this.getTradeCodeByTradeNameFromCommonCodeTrade(tradeName));
		log.info("tradeName is {}, statusList is {}", tradeName, orderSearchDTO.getStatusList());
		
		// db 조회시 trade_code 컬럼에 사용할 공통코드 조회 결과 셋
		orderSearchDTO.setTradeCode(this.getTradeCodeByTradeNameFromCommonCodeTrade(tradeName));
		//페이징용 totalCount
		orderSearchDTO.getPageDTO().setTotalCount(orderService.getOrderCountForPaging(orderSearchDTO));
		
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		model.addAttribute("orderDTOList", orderService.getOrderList(orderSearchDTO));
		
		return COMMON_PATH + "/orders_management";
	}
	
	/**
	 * {수주,발주,구매} > tradeName > 거래에 따른 리스트 검색 조건 조회(GET)
	 * @param tradeName : "/orders/sell/search" || "/orders/buy/search" || "/orders/material/search"
	 * @param orderSearchDTO : view(html).searchForm
	 * @param model : orderSearchDTO, orderDTOList
	 * @return viewResolver : orders_management
	 */
	@GetMapping(value = {"/{tradeName}/search"})
	public String getOrderListBySearchDTO(@PathVariable("tradeName") String tradeName, 
											@ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("tradeName is {}, orderSearchDTO : {}", tradeName, StringUtil.objToString(orderSearchDTO));
		
		// 검색폼.statusCodes 에 사용할 공통코드 조회 결과 셋
		orderSearchDTO.setStatusList(this.getStatusCodesByTradeName(tradeName));
		// view 페이지 통합을 위한 코드 값 추가
		orderSearchDTO.setTradeCode(this.getTradeCodeByTradeNameFromCommonCodeTrade(tradeName));
		log.info("tradeName is {}, statusList is {}", tradeName, orderSearchDTO.getStatusList());
		
		// db 조회시 trade_code 컬럼에 사용할 공통코드 조회 결과 셋
		orderSearchDTO.setTradeCode(this.getTradeCodeByTradeNameFromCommonCodeTrade(tradeName));
		//페이징용 totalCount
		orderSearchDTO.getPageDTO().setTotalCount(orderService.getOrderCountForPaging(orderSearchDTO));
		
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		model.addAttribute("orderDTOList", orderService.getOrderList(orderSearchDTO));
		
		return COMMON_PATH + "/orders_management";
	}
	
	/**
	 * {수주,발주,구매} > tradeName > 주문서 등록(GET)
	 * @param tradeName "/orders/sell/regist" || "/orders/buy/regist" || "/orders/material/regist"
	 * @param model : orderFormDTO, clientList
	 * @return viewResolver: ".../sell_register_form" || ".../buy_register_form" || ".../material_register_form"
	 */
	@GetMapping("/{tradeName}/regist")
	public String getOrderRegisterForm(@PathVariable("tradeName") String tradeName, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("tradeName is {}", tradeName);
		
		// 등록 폼페이지에서 사용할 DTO 객체 생성
		OrderFormDTO orderFormDTO = new OrderFormDTO();
		// 등록시 db.trade_code, status_code 컬럼에 사용할 공통코드 조회 결과 셋
		orderFormDTO.setTradeCode(this.getTradeCodeByTradeNameFromCommonCodeTrade(tradeName));
		orderFormDTO.setStatusCode(this.getDefaultStatusCodeByTradeNameFromCommonCodeStatus(tradeName));
		
		model.addAttribute("orderFormDTO", orderFormDTO);
		
		// TODO 25.04.16 상품이 추가 될 경우 t_order_items 테이블에 입력할 것 준비
		
		return COMMON_PATH + "/orders_register_form";
	}
	
	/**
	 * {수주,발주,구매} > 주문서 등록(POST)
	 * <br>tradeName 이 없는 이유는 /{tradeName}/regist 메서드에서 trade_code,status_code 값을 만들었으며 그걸 전달 받음
	 * @param orderFormDTO
	 * @return status : success, fail
	 */
	@PostMapping(value= {"/regist"}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	private ResponseEntity<Map<String, Object>> registerOrderForJson(@RequestBody @Valid OrderFormDTO orderFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(orderFormDTO));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		
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
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * {수주,발주,구매} > tradeName > 주문서 상세 및 수정(GET)
	 * @param tradeName "/orders/sell/100001" || "/orders/buy/100002" || "/orders/material/100003"
	 * @param documentNumber 주문서 번호
	 * @param model : orderDTO, clientList
	 * @return viewResolver : ".../sell_detail" || ".../buy_detail" || ".../material_detail"
	 */
	@GetMapping("/{tradeName}/{documentNumber}")
	public String getOrderDetail(@PathVariable("tradeName") String tradeName, 
								 @PathVariable("documentNumber") String documentNumber, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println("documentNumber : "  + documentNumber);
		
		// 주문정보 가져오기
		model.addAttribute("orderFormDTO", orderService.getOrderByDocumentNumber(documentNumber));
		// statusCodes 에 사용할 공통코드 조회 결과 셋
		model.addAttribute("statusCodes", this.getStatusCodesByTradeName(tradeName));
		
		// TODO 25.04.16 상품이 추가 될 경우 t_order_items 테이블에 입력한 것 조회 하기
		
		return COMMON_PATH + "/orders_detail";
	}
	
	/**
	 * {수주,발주,구매} > 주문서 수정(PUT)
	 * <br>tradeName 이 없는 이유는 /{tradeName}/regist 메서드에서 trade_code,status_code 값을 만들었으며 그걸 전달 받음
	 * @param orderFormDTO
	 * @return status : success, fail
	 */
	@PutMapping("/modify")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyOrderForJson(@RequestBody @Valid OrderFormDTO orderFormDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(orderFormDTO));
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		
		try {
			// 주문 정보 수정
			orderService.modifyOrder(orderFormDTO);
			
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		return ResponseEntity.ok(response);
	}

	//===========================컨트롤러 내부 메서드 시작=====================================//
	/**
	 * <pre>
	 * 공통코드_아이템에서 수주 or 발주 or 구매 관련된 상태값(status_code) 가져오기
	 * COMMON_MAJOR_CODE_STATUS : \"ORDER_STATUS_CODE\"
	 * SELL_NAME_KR : \"수주\", BUY_NAME_KR : \"발주\", MATERIAL_NAME_KR : \"구매\"
	 * | MAJOR_CODE       | MINOR_CODE | MINOR_NAME | DESCRIPTION   |
	 * | ORDER_STATUS_CODE| 1          | 미출고      | 수주_미출고      |
	 * | ORDER_STATUS_CODE| 2          | 출고완료     | 수주_출고완료    |
	 * | ORDER_STATUS_CODE| 3          | 취소        | 수주_취소       |
	 * | ORDER_STATUS_CODE| 4          | 세금계산서발행| 발주_세금계산서발행|
	 * | ORDER_STATUS_CODE| 5          | 수금완료    | 발주_수금완료     |
	 * | ORDER_STATUS_CODE| 6          | 미발행      | 발주_미발행      |
	 * | ORDER_STATUS_CODE| 7          | 미수금      | 발주_미수금      |
	 * | ORDER_STATUS_CODE| 8          | 세금계산서발행| 구매_세금계산서발행|
	 * | ORDER_STATUS_CODE| 9          | 결제완료    | 구매_결제완료     |
	 * | ORDER_STATUS_CODE| 10         | 미입고      | 구매_미입고      |
	 * | ORDER_STATUS_CODE| 11         | 입고완료    | 구매_입고완료     |
	 * 일 때 생성한 메서드, 리스트를 가져와서 DESCRIPTION에 현재 화면의 코드만 남기고 제외시킴 
	 * 공통 코드 값이 바뀔 경우 현재 의도와 다르게 작동할 가능성이 몹시 높음!
	 * </pre>
	 * @param tradeName
	 * @return 수주: [1,2,3], 발주: [4,5,6,7], 구매: [8,9] 
	 */
	private List<CodeItemDTO> getStatusCodesByTradeName(String tradeName) {
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
		
		log.info("return codeItemList is {}", codeItemList);
		return codeItemList;
	} // end of private List<CodeItemDTO> getCodeItemsByTradeName(String tradeName) {
	
	/**
	 * <pre>
	 * 공통코드_아이템에서 수주 || 발주 || 구매 관련된 거래 코드값(trade_code) 가져오기
	 * COMMON_MAJOR_CODE_TRADE : \"ORDER_TRADE_CODE\"
	 * SELL_NAME_KR : \"수주\", BUY_NAME_KR : \"발주\", MATERIAL_NAME_KR : \"구매\"
	 * | MAJOR_CODE      | MINOR_CODE | MINOR_NAME |
	 * | ORDER_TRADE_CODE| 1          | 수주        |
	 * | ORDER_TRADE_CODE| 2          | 발주        |
	 * | ORDER_TRADE_CODE| 3          | 구매        |
	 * 일 때 생성한 메서드, 리스트를 가져와 MINOR_NAME과 ?_NAME_KR이 일치할 경우 그 MINOR_CODE 를 사용하기 위함
	 * 즉, 한글명으로 MINOR_CODE 공통 코드 값을 가져오기 위함
	 * 공통 코드 값이 바뀔 경우 현재 의도와 다르게 작동할 가능성이 몹시 높음!
	 * </pre>
	 * @param tradeName
	 * @return 1: 수주, 2: 발주, 3: 구매
	 */
	private String getTradeCodeByTradeNameFromCommonCodeTrade(String tradeName) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		if(!StringUtils.hasLength(tradeName)) {
			return null;
		}
		// 파라미터 : 공통코드에서 '수주' or '발주' 코드
		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
//		log.info("tradeCode is {}", tradeCode);
		
		String targetName = "";
		String returnValue = "99"; // 얜 무조건 오류임
		switch(tradeName) {
			case SELL_VALUE: targetName = SELL_NAME_KR;
				break;
			case BUY_VALUE: targetName = BUY_NAME_KR;
				break;
			case MATERIAL_VALUE: targetName = MATERIAL_NAME_KR;
				break;
			default: targetName = SELL_NAME_KR;
				break;
		}
		
		for (CodeItemDTO item : tradeCode) {
			if (targetName.equals(item.getMinorName())) {
				returnValue = item.getMinorCode();
				break;
			}
		}
		
		log.info("returnValue is {}", returnValue);
		return returnValue;
	} // end of private String getTradeCodeByTradeNameFromCommonCodeTrade(String tradeName) {
	
	
	/**
	 * <pre>
	 * 공통코드_아이템에서 수주 || 발주 || 구매 관련된 기본 거래 상태 코드값(statusCode) 가져오기
	 * COMMON_MAJOR_CODE_TRADE : \"ORDER_STATUS_CODE\"
	 * SELL_NAME_KR : \"수주\", BUY_NAME_KR : \"발주\", MATERIAL_NAME_KR : \"구매\"
	 * | MAJOR_CODE       | MINOR_CODE | MINOR_NAME | DESCRIPTION   |
	 * | ORDER_STATUS_CODE| 1          | 미출고      | 수주_미출고      |
	 * ...(생략)
	 * | ORDER_STATUS_CODE| 7          | 미수금      | 발주_미수금      |
	 * ...(생략)
	 * | ORDER_STATUS_CODE| 10         | 미입고      | 구매_미입고      |
	 * </pre>
	 * @param tradeName
	 * @return 수주: 1(미출고), 발주: 7(미수금), 구매: 10(미입고) 
	 */
	private String getDefaultStatusCodeByTradeNameFromCommonCodeStatus(String tradeName) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		if(!StringUtils.hasLength(tradeName)) {
			return null;
		}
		List<CodeItemDTO> statusCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
//		log.info("statusCode is {}", statusCode);
		
		String targetName = "";
		String returnValue = "99"; // 얜 무조건 오류임
		switch(tradeName) {
			case SELL_VALUE: targetName = SELL_DEFAULT_STATUS_CODE;
				break;
			case BUY_VALUE: targetName = BUY_DEFAULT_STATUS_CODE;
				break;
			case MATERIAL_VALUE: targetName = MATERIAL_DEFAULT_STATUS_CODE;
				break;
			default: targetName = SELL_DEFAULT_STATUS_CODE;
				break;
		}
		
		for (CodeItemDTO item : statusCode) {
			if (targetName.equals(item.getMinorName())) {
				returnValue = item.getMinorCode();
				break;
			}
		}
		
		log.info("returnValue is {}", returnValue);
		return returnValue;
	} // end of private String getDefaultStatusCodeByTradeNameFromCommonCodeStatus(String tradeName) {
	
}
