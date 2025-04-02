//package kr.co.itwillbs.de.orders.controller;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
//import kr.co.itwillbs.de.common.util.CommonCodeUtil;
//import kr.co.itwillbs.de.orders.dto.OrderDTO;
//import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
//import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
//import kr.co.itwillbs.de.orders.service.BuyService;
//import kr.co.itwillbs.de.orders.service.SellService;
//import lombok.extern.slf4j.Slf4j;
//
///* 발주관리 */
//@Slf4j
//@RequestMapping("/orders/buy") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
//@Controller
//public class BuyController_Backup {
//	@Autowired
//	private BuyService buyService;
//	@Autowired
//	private SellService sellService;
//	
//	@Autowired
//	private CommonCodeUtil commonCodeUtil;
//	
//	// 발주 관리 목록 조회(SELECT) 요청(GET)
//	@GetMapping(value = {"", "/"})  // "/orders/buy"
//	public String getBuyList(Model model, HttpSession session) {
//	    log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//	    
//	    // 발주 관리 목록 리스트 조회 요청(SELECT)
//	    // 파라미터 : 공통코드에서 '발주' 코드
// 		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems("ORDER_TRADE_CODE");
// 		
// 		String code = null;
// 		// minorName 이 '발주' 인 minorcode 뽑아내서 trade_code 에 넣기
// 		for (CodeItemDTO item : tradeCode) {
// 			if ("발주".equals(item.getMinorName())) {
// 				code = item.getMinorCode();
// 				break;
// 			}
// 		}
//	    
//	    List<HashMap<String, Object>> buyDTOList = buyService.getBuyList(code);
//	    model.addAttribute("buyDTOList", buyDTOList);
//	    System.out.println("buyDTOList : " + buyDTOList);
//	    return "orders/buy_management";
//	}
//	
//	//------------------------------------------------------------------------------------------------
//
//	// 주문서등록(발주) 페이지로 이동(GET)
//	@GetMapping(value={"/regist","/regist/"})	// "/orders/buy/regist"
//	public String getBuyRegisterForm(Model model) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		
//		// 뷰 페이지에서 OrderDTO, OrderDetailDTO 에 기술된 Validation 항목 체크를 위해 빈 DTO 객체를 함께 전달
//		model.addAttribute("orderFormDTO", new OrderFormDTO());
//		return "orders/buy_register_form";
//	}
//	
//	// =============== 여기부터 수주랑 같은 서비스(SellService) 써서 그냥 하나로 합쳐도 될 것 같기도?? ==============
//	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//	
//	// 주문서등록(INSERT) 요청하는 주소 매핑(POST)
//	@PostMapping(value= {"/regist", "/regist/"}) // "/orders/buy/regist"
//	public String buyRegister(@ModelAttribute("orderFormDTO") @Valid OrderFormDTO orderFormDTO, BindingResult bindingResult) {
//	    log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//
//		// 공통코드 가져와서 trade_code, status_code 넣기
//		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems("ORDER_TRADE_CODE");
//		List<CodeItemDTO> statusCode = commonCodeUtil.getCodeItems("ORDER_STATUS_CODE");
//		
//		// minorName 이 '발주' 인 minorcode 뽑아내서 trade_code 에 넣기
//		for (CodeItemDTO item : tradeCode) {
//			if ("발주".equals(item.getMinorName())) {
//				orderFormDTO.getOrderDTO().setTradeCode(item.getMinorCode());
//				break;
//			}
//		}
//		
//		// minorName 이 '미수금' 인 minorcode 뽑아내서 status_code 에 넣기
//		for (CodeItemDTO item : statusCode) {
//			if ("미수금".equals(item.getMinorName())) {
//				orderFormDTO.getOrderDTO().setStatusCode(item.getMinorCode());
//				break;
//			}
//		}
//		
//		System.out.println(">>>>>>>>>" + orderFormDTO.getOrderDTO());
//		
//		// 주문서 등록 요청(INSERT)
//		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
//		sellService.registerOrder(orderFormDTO.getOrderDTO());
//		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
////		sellService.registerOrderDetail(orderFormDTO.getOrderDetailDTO());
//		
//	    // ----- 상품 선택 및 등록은 나중에 -----
//	    
//		// 주문서 등록 후 주문서 상세 페이지로 이동
//	    return "redirect:/orders/buy/" + orderFormDTO.getOrderDTO().getDocumentNumber();
//	}
//
//	// 주문서 상세 및 수정 페이지 매핑(GET)
//	@GetMapping("/{documentNumber}") // "/orders/buy/100001"
//	public String getBuyDetail(@PathVariable("documentNumber") String documentNumber, Model model) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//		System.out.println("documentNumber : "  + documentNumber);
//		
//		// 공통코드 가져오기
//		List<CodeItemDTO> tradeCode = commonCodeUtil.getCodeItems("ORDER_TRADE_CODE");
//		List<CodeItemDTO> statusCode = commonCodeUtil.getCodeItems("ORDER_STATUS_CODE");
//		model.addAttribute("tradeCode", tradeCode);
//		model.addAttribute("statusCode", statusCode);
//		
//		HashMap<String, Object> buyDTO = sellService.getOrder(documentNumber);
//		System.out.println("buyDTO : " + buyDTO);
//		model.addAttribute("buyDTO", buyDTO);
//		return "orders/buy_detail";
//	}
//	
//	// 주문서 수정(UPDATE) 요청하는 주소 매핑(POST)
//	// => 히든메서드 필터에 의해 PUT 으로 변해야하지만 일단 POST 방식 사용
//	@PostMapping("/modify") // "/orders/buy/modify"
//	public String modifyBuyInfo(@ModelAttribute("orderDTO") OrderDTO orderDTO, @ModelAttribute("orderDetailDTO") OrderDetailDTO orderDetailDTO) {
//		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
//
//		System.out.println("orderDTO : " + orderDTO);
//		System.out.println("orderDetailDTO : " + orderDetailDTO);
//		
//		
//		// 주문 정보 수정
//		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
//		sellService.modifyOrder(orderDTO);
//		// 주문 상세 정보 수정
//		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
////		buyService.modifyOrderDetail(orderDetailDTO);
//		
//		return "redirect:/orders/buy/" + orderDTO.getDocumentNumber();
//	}
//	
//	//------------------------------------------------------------------------------------------------
//}
