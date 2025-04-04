package kr.co.itwillbs.de.orders.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.dto.OrderSearchDTO;
import kr.co.itwillbs.de.orders.service.SellService;
import lombok.extern.slf4j.Slf4j;

/* 수주/발주관리 */
@Slf4j
@RequestMapping("/orders") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
@Controller
public class SellController {
	
	@Autowired
	private SellService sellService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;
	
	private final String SELL_PATH = "/orders/sell";
	private final String BUY_PATH = "/orders/buy";
	private final String COMMON_MAJOR_CODE_STATUS = "ORDER_STATUS_CODE";
	private final String COMMON_MAJOR_CODE_TRADE = "ORDER_TRADE_CODE";
	private final String SELL_NAME_KR = "수주";
	private final String BUY_NAME_KR = "발주";
	
	
	// 수주 관리 목록 조회(SELECT) 요청(GET)
	@GetMapping(value = {"/sell", "/sell/"}) // "/orders/sell"
	public String getSellList(@ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 수주만 남은 codeItemList를 화면 검색 DTO 객체에 셋!
		orderSearchDTO.setCodeItemList(this.getSellItems());
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		
		// 수주 관리 목록 리스트 조회 요청(SELECT)
	    // 파라미터 : 공통코드에서 '수주' 코드
 		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
 		
 		// minorName 이 '수주' 인 minorcode 뽑아내서 trade_code 에 넣기
 		for (CodeItemDTO item : tradeCode) {
 			if (SELL_NAME_KR.equals(item.getMinorName())) {
 				orderSearchDTO.setTradeCode(item.getMinorCode());
 				break;
 			}
 		}
		
		// 수주 관리 목록 리스트 조회 요청(SELECT)
		List<OrderDTO> sellDTOList = sellService.getOrderList(orderSearchDTO);
		model.addAttribute("sellDTOList", sellDTOList);
		log.info("sellDTOList : " + sellDTOList);
		return SELL_PATH+"_management";
	}
	
	// 수주 관리 목록 검색 조건 조회(SELECT) 요청(GET)
	@GetMapping(value = {"/sell/search"}) // "/orders/sell/search"
	public String getSellListBySearchDTO(@ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("request orderSearchDTO : {}", StringUtil.objToString(orderSearchDTO));
		// 수주만 남은 codeItemList를 화면 검색 DTO 객체에 셋!
		orderSearchDTO.setCodeItemList(this.getSellItems());
		// 리스트 검색 DTO 뷰에 전달
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		
		// 수주 정보 조건 검색 가져오기(재사용)
		List<OrderDTO> sellDTOList = sellService.getOrderList(orderSearchDTO);
		model.addAttribute("sellDTOList", sellDTOList);
		return SELL_PATH+"_management";
	}
	
	//------------------------------------------------------------------------------------------------
	// 주문서등록(수주) 페이지로 이동(GET)
	@GetMapping(value={"/sell/regist","/sell/regist/"})	// "/orders/sell/regist"
	public String getSellRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 뷰 페이지에서 OrderDTO, OrderDetailDTO 에 기술된 Validation 항목 체크를 위해 빈 DTO 객체를 함께 전달
		model.addAttribute("orderFormDTO", new OrderFormDTO());

		return "orders/sell_register_form";
	}
	
	// 주문서등록(INSERT) 요청하는 주소 매핑(POST)
	@PostMapping(value= {"/sell/regist", "/sell/regist/"}) // "/orders/sell/regist"
	public String sellRegister(@ModelAttribute("orderFormDTO") @Valid OrderFormDTO orderFormDTO, BindingResult bindingResult) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 공통코드 가져와서 trade_code, status_code 넣기
		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		List<CodeItemDTO> statusCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
		
		// minorName 이 '수주' 인 minorcode 뽑아내서 trade_code 에 넣기
		for (CodeItemDTO item : tradeCode) {
			if (SELL_NAME_KR.equals(item.getMinorName())) {
				orderFormDTO.getOrderDTO().setTradeCode(item.getMinorCode());
				break;
			}
		}
		
		// minorName 이 '미출고' 인 minorcode 뽑아내서 status_code 에 넣기
		for (CodeItemDTO item : statusCode) {
			if ("미출고".equals(item.getMinorName())) {
				orderFormDTO.getOrderDTO().setStatusCode(item.getMinorCode());
				break;
			}
		}
		
		System.out.println(">>>>>>>>>" + orderFormDTO.getOrderDTO());
		System.out.println(">>>>>>>>>" + orderFormDTO.getOrderDetailDTO());
		
		// 주문서 등록 요청(INSERT)
		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
		sellService.registerOrder(orderFormDTO.getOrderDTO());
		
		// t_order에 insert한 document_number 들고와서 t_order_detail 테이블에도 넣어야함
		// document_number만 들어간 컬럼이라도 만들어놔야 나중에 update만 하면된다 !
		String documentNumber = orderFormDTO.getOrderDTO().getDocumentNumber();
		orderFormDTO.getOrderDetailDTO().setDocumentNumber(documentNumber);
		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
		sellService.registerOrderDetail(orderFormDTO.getOrderDetailDTO());

		// +++++++ 상품 선택 및 등록은 나중에 +++++++

		// 주문서 등록 후 주문서 상세 페이지로 이동
		return "redirect:/orders/sell/" + documentNumber;
	}

	// 주문서 상세 및 수정 페이지 매핑(GET)
	@GetMapping("/sell/{documentNumber}") // "/orders/sell/100001"
	public String getSellDetail(@PathVariable("documentNumber") String documentNumber, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println("documentNumber : "  + documentNumber);
		
		OrderDTO orderDTO = sellService.getOrderByDocumentNumber(documentNumber);
		// 수주만 남은 codeItemList를 화면 DTO 객체에 셋! 진짜 이거 맞아?
		orderDTO.setCodeItemList(this.getSellItems());
		
		log.info("orderDTO : " + orderDTO);
		model.addAttribute("orderDTO", orderDTO);
		
		return "orders/sell_detail";
	}
	
	// 주문서 수정(UPDATE) 요청하는 주소 매핑(POST)
	// => 히든메서드 필터에 의해 PUT 으로 변해야하지만 일단 POST 방식 사용
	@PostMapping("/sell/modify") // "/orders/sell/modify"
	public String updateClient(@ModelAttribute("orderDTO") OrderDTO orderDTO, @ModelAttribute("orderDetailDTO") OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		System.out.println("orderDTO : " + orderDTO);
		System.out.println("orderDetailDTO : " + orderDetailDTO);
		
		// 주문 정보 수정
		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
		sellService.modifyOrder(orderDTO);
		// 주문 상세 정보 수정
		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
		sellService.modifyOrderDetail(orderDetailDTO);
		
		return "redirect:/orders/sell/" + orderDTO.getDocumentNumber();
	}
	
	// ===============================================================================================
	// ----------------------------------- 여기부터 발주 ---------------------------------------------
	// 발주 관리 목록 조회(SELECT) 요청(GET)
	@GetMapping(value = {"/buy", "/buy/"})  // "/orders/buy"
	public String getBuyList(@ModelAttribute OrderSearchDTO orderSearchDTO,Model model, HttpSession session) {
	    log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    
	    // 발주만 남은 codeItemList를 화면 검색 DTO 객체에 셋!
 		orderSearchDTO.setCodeItemList(this.getBuyItems());
 		model.addAttribute("orderSearchDTO", orderSearchDTO);
	 		
	    // 발주 관리 목록 리스트 조회 요청(SELECT)
	    // 파라미터 : 공통코드에서 '발주' 코드
 		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
 		
 		// minorName 이 '발주' 인 minorcode 뽑아내서 trade_code 에 넣기
 		for (CodeItemDTO item : tradeCode) {
 			if (BUY_NAME_KR.equals(item.getMinorName())) {
 				orderSearchDTO.setTradeCode(item.getMinorCode());
 				break;
 			}
 		}
 		
		// 발주 관리 목록 리스트 조회 요청(SELECT) - 재사용
		List<OrderDTO> buyDTOList = sellService.getOrderList(orderSearchDTO);
	    model.addAttribute("buyDTOList", buyDTOList);
	    System.out.println("buyDTOList : " + buyDTOList);
	    return "orders/buy_management";
	}
	
	// 발주 관리 목록 검색 조건 조회(SELECT) 요청(GET)
	@GetMapping(value = {"/buy/search"}) // "/orders/buy/search"
	public String getBuyListBySearchDTO(@ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("request orderSearchDTO : {}", StringUtil.objToString(orderSearchDTO));
		// 발주만 남은 codeItemList를 화면 검색 DTO 객체에 셋!
		orderSearchDTO.setCodeItemList(this.getBuyItems());
		// 리스트 검색 DTO 뷰에 전달
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		
		// 발주 정보 조건 검색 가져오기(재사용)
		List<OrderDTO> buyDTOList = sellService.getOrderList(orderSearchDTO);
		model.addAttribute("buyDTOList", buyDTOList);
		return "orders/buy_management";
	}
	
	//------------------------------------------------------------------------------------------------
	// 주문서등록(발주) 페이지로 이동(GET)
	@GetMapping(value={"/buy/regist","/buy/regist/"})	// "/orders/buy/regist"
	public String getBuyRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 뷰 페이지에서 OrderDTO, OrderDetailDTO 에 기술된 Validation 항목 체크를 위해 빈 DTO 객체를 함께 전달
		model.addAttribute("orderFormDTO", new OrderFormDTO());
		return "orders/buy_register_form";
	}
	
	// 주문서등록(INSERT) 요청하는 주소 매핑(POST)
	@PostMapping(value= {"/buy/regist", "/buy/regist/"}) // "/orders/buy/regist"
	public String buyRegister(@ModelAttribute("orderFormDTO") @Valid OrderFormDTO orderFormDTO, BindingResult bindingResult) {
	    log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 공통코드 가져와서 trade_code, status_code 넣기
		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		List<CodeItemDTO> statusCode = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
		
		// minorName 이 '발주' 인 minorcode 뽑아내서 trade_code 에 넣기
		for (CodeItemDTO item : tradeCode) {
			if (BUY_NAME_KR.equals(item.getMinorName())) {
				orderFormDTO.getOrderDTO().setTradeCode(item.getMinorCode());
				break;
			}
		}
		
		// minorName 이 '미수금' 인 minorcode 뽑아내서 status_code 에 넣기
		for (CodeItemDTO item : statusCode) {
			if ("미수금".equals(item.getMinorName())) {
				orderFormDTO.getOrderDTO().setStatusCode(item.getMinorCode());
				break;
			}
		}
		
		System.out.println(">>>>>>>>>" + orderFormDTO.getOrderDTO());
		
		// 주문서 등록 요청(INSERT)
		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
		sellService.registerOrder(orderFormDTO.getOrderDTO());

		// t_order에 insert한 document_number 들고와서 t_order_detail 테이블에도 넣어야함
		// document_number만 들어간 컬럼이라도 만들어놔야 나중에 update만 하면된다 !
		String documentNumber = orderFormDTO.getOrderDTO().getDocumentNumber();
		orderFormDTO.getOrderDetailDTO().setDocumentNumber(documentNumber);
		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
		sellService.registerOrderDetail(orderFormDTO.getOrderDetailDTO());
		
		// +++++++ 상품 선택 및 등록은 나중에 +++++++
	    
		// 주문서 등록 후 주문서 상세 페이지로 이동
	    return "redirect:/orders/buy/" + orderFormDTO.getOrderDTO().getDocumentNumber();
	}

	// 주문서 상세 및 수정 페이지 매핑(GET)
	@GetMapping("/buy/{documentNumber}") // "/orders/buy/100001"
	public String getBuyDetail(@PathVariable("documentNumber") String documentNumber, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println("documentNumber : "  + documentNumber);
		
		OrderDTO orderDTO = sellService.getOrderByDocumentNumber(documentNumber);
		// 발주만 남은 codeItemList를 화면 DTO 객체에 셋! 진짜 이거 맞아?
		orderDTO.setCodeItemList(this.getBuyItems());
		
		log.info("orderDTO : " + orderDTO);
		model.addAttribute("orderDTO", orderDTO);
		
		return "orders/buy_detail";
	}
	
	// 주문서 수정(UPDATE) 요청하는 주소 매핑(POST)
	// => 히든메서드 필터에 의해 PUT 으로 변해야하지만 일단 POST 방식 사용
	@PostMapping("/buy/modify") // "/orders/buy/modify"
	public String modifyBuyInfo(@ModelAttribute("orderDTO") OrderDTO orderDTO, @ModelAttribute("orderDetailDTO") OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		System.out.println("orderDTO : " + orderDTO);
		System.out.println("orderDetailDTO : " + orderDetailDTO);
		
		// 주문 정보 수정
		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
		sellService.modifyOrder(orderDTO);
		// 주문 상세 정보 수정
		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
		sellService.modifyOrderDetail(orderDetailDTO);
		
		return "redirect:/orders/buy/" + orderDTO.getDocumentNumber();
	}
	
	//------------------------------------------------------------------------------------------------
	
	/**
	 * 공통코드_아이템에서 수주 관련된 상태값 가져오기
	 * <br>COMMON_MAJOR_CODE_STATUS : \"ORDER_STATUS_CODE\"
	 * <br>SELL_NAME_KR : \"수주\"
	 * @return
	 */
	private List<CodeItemDTO> getSellItems() {
		//TODO 공통코드 가져오기 근데 고민해보자. 문제가 좀 있네? 지금 이 방법이 맞냐?
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
		for (Iterator<CodeItemDTO> it = codeItemList.iterator(); it.hasNext();) {
			CodeItemDTO item = it.next();
			if (item.getDescription().indexOf(SELL_NAME_KR) < 0) it.remove(); // 수주가 아니면 -1이니까 삭제하라!
		}
		return codeItemList;
	}
	
	/**
	 * 공통코드_아이템에서 발주 관련된 상태값 가져오기
	 * <br>COMMON_MAJOR_CODE_STATUS : \"ORDER_STATUS_CODE\"
	 * <br>BUY_NAME_KR : \"발주\"
	 * @return
	 */
	private List<CodeItemDTO> getBuyItems() {
		//TODO 공통코드 가져오기 근데 고민해보자. 문제가 좀 있네? 지금 이 방법이 맞냐?
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_STATUS);
		for (Iterator<CodeItemDTO> it = codeItemList.iterator(); it.hasNext();) {
			CodeItemDTO item = it.next();
			if (item.getDescription().indexOf(BUY_NAME_KR) < 0) it.remove(); // 발주가 아니면 -1이니까 삭제하라!
		}
		return codeItemList;
	}
	
}
