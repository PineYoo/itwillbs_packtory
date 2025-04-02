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

/* 수주관리 */
@Slf4j
@RequestMapping("/orders/sell") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
@Controller
public class SellController {
	
	@Autowired
	private SellService sellService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;
	
	private final String SELL_PATH = "/orders/sell";
	private final String COMMON_MAJOR_CODE = "ORDER_STATUS_CODE";
	private final String COMMON_SOOJOO_MAJOR_CODE = "1";
	private final String MENU_NAME_KR = "수주";
	
	// 수주 관리 목록 조회(SELECT) 요청(GET)
	@GetMapping(value = {"", "/"}) // "/orders/sell"
	public String getSellList(@ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 수주만 남은 codeItemList를 화면 검색 DTO 객체에 셋!
		orderSearchDTO.setCodeItemList(this.getSOOJOOItems());
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		
		// 수주 관리 목록 리스트 조회 요청(SELECT)
//		List<HashMap<String, Object>> sellDTOList = sellService.getSellList();
		// 수주 tradeCode 값 셋
		orderSearchDTO.setTradeCode(COMMON_SOOJOO_MAJOR_CODE);
		List<OrderDTO> sellDTOList = sellService.getOrdersInTradeSell(orderSearchDTO);
		model.addAttribute("sellDTOList", sellDTOList);
		log.info("sellDTOList : " + sellDTOList);
		return SELL_PATH+"_management";
	}
	
	// 수주 관리 목록 검색 조건 조회(SELECT) 요청(GET)
	@GetMapping(value = {"/search"}) // "/orders/sell"
	public String getSellListBySearchDTO(@ModelAttribute OrderSearchDTO orderSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("request orderSearchDTO : {}", StringUtil.objToString(orderSearchDTO));
		// 수주만 남은 codeItemList를 화면 검색 DTO 객체에 셋!
		orderSearchDTO.setCodeItemList(this.getSOOJOOItems());
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		
		// 리스트 검색 DTO 뷰에 전달
		model.addAttribute("orderSearchDTO", orderSearchDTO);
		List<OrderDTO> sellDTOList = sellService.getOrdersInTradeSell(orderSearchDTO);
		model.addAttribute("sellDTOList", sellDTOList);
		return SELL_PATH+"_management";
	}
	
	// 검색 조건에 맞는 수주 관리 목록 조회 요청(POST)
/*	@PostMapping(value = {"", "/"})  // "/orders/sell"
	@ResponseBody
	public List<HashMap<String, Object>> getSearchItem(
					@RequestParam("orderStatus") String orderStatus,
			        @RequestParam("searchKeyword") String searchKeyword,
			        @RequestParam("startDate") String startDate,
			        @RequestParam("endDate") String endDate) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info(">>>> 받은 파라미터");
		log.info("orderStatus: " + (orderStatus == null ? "null" : "'" + orderStatus + "'"));
		log.info("startDate: " + (startDate == null ? "null" : "'" + startDate + "'"));
		log.info("endDate: " + (endDate == null ? "null" : "'" + endDate + "'"));
		log.info("searchKeyword: " + (searchKeyword == null ? "null" : "'" + searchKeyword + "'"));		
		
		// 검색 조건에 맞는 수주 관리 목록 리스트 조회 요청(SELECT)
		List<HashMap<String, Object>> sellDTOList = sellService.getSearchSell(orderStatus, searchKeyword, startDate, endDate);
		
		for (HashMap<String, Object> sell : sellDTOList) {
		    Object regDate = sell.get("reg_date");  // reg_date 값 가져오기

		    // 로그로 데이터 타입 확인
		    if (regDate == null) {
		        log.info("reg_date is NULL");
		    } else {
		        log.info("reg_date: " + regDate + ", type: " + regDate.getClass().getName());
		    }

		    // reg_date가 List<Integer> 형태일 경우 변환
		    if (regDate instanceof List) {
		        List<Integer> dateArray = (List<Integer>) regDate;
		        if (dateArray.size() >= 3) {  // 최소한 연, 월, 일이 있어야 함
		            int year = dateArray.get(0);
		            int month = dateArray.get(1);
		            int day = dateArray.get(2);

		            // yyyy-MM-dd 형식으로 변환
		            String formattedDate = String.format("%04d-%02d-%02d", year, month, day);
		            sell.put("reg_date", formattedDate);
		            log.info("Formatted reg_date: " + formattedDate);
		        }
		    }
		}
		
		System.out.println("sellDTOList22 : " + sellDTOList);
		return sellDTOList;
	}
	*/
	//------------------------------------------------------------------------------------------------

	// 주문서등록(수주) 페이지로 이동(GET)
	@GetMapping(value={"/regist","/regist/"})	// "/orders/sell/regist"
	public String getSellRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 뷰 페이지에서 OrderDTO, OrderDetailDTO 에 기술된 Validation 항목 체크를 위해 빈 DTO 객체를 함께 전달
//		model.addAttribute("orderDTO", new OrderDTO());
//		model.addAttribute("orderDetailDTO", new OrderDetailDTO());
		model.addAttribute("orderFormDTO", new OrderFormDTO());

		return "orders/sell_register_form";
	}
	
	// 주문서등록(INSERT) 요청하는 주소 매핑(POST)
	@PostMapping(value= {"/regist", "/regist/"}) // "/orders/sell/regist"
	public String sellRegister(@ModelAttribute("orderFormDTO") @Valid OrderFormDTO orderFormDTO, BindingResult bindingResult) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		System.out.println("orderDTO : " + orderFormDTO.getOrderDTO());
		System.out.println("orderDetailDTO : " + orderFormDTO.getOrderDetailDTO());
		System.out.println("clientDTO : " + orderFormDTO.getClientDTO());
		System.out.println("clientInfoDTO : " + orderFormDTO.getClientInfoDTO());

		log.debug("requestData : " + orderFormDTO.toString());
		log.debug("getAllErrors() : " + bindingResult.getAllErrors()); // 체크 결과 발생한 모든 오류 확인

		// 주문서 등록 요청(INSERT)
		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
		sellService.registerOrder(orderFormDTO.getOrderDTO());
		// >>>>>>>>>>> 작성자 등록 위한 id 가져가야함
//		sellService.registerOrderDetail(orderFormDTO.getOrderDetailDTO());

		// ----- 상품 선택 및 등록은 나중에 -----

		// 주문서 등록 후 주문서 상세 페이지로 이동
		return "redirect:/orders/sell/" + orderFormDTO.getOrderDTO().getDocumentNumber();
	}

	// 주문서 상세 및 수정 페이지 매핑(GET)
	@GetMapping("/{documentNumber}") // "/orders/sell/100001"
	public String getSellDetail(@PathVariable("documentNumber") String documentNumber, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println("documentNumber : "  + documentNumber);
		
		/*HashMap<String, Object> sellDTO = sellService.getOrder(documentNumber);
		log.info("sellDTO : " + sellDTO);
		model.addAttribute("sellDTO", sellDTO);
		*/
		OrderDTO orderDTO = sellService.getOrderByDocumentNumber(documentNumber);
		// 수주만 남은 codeItemList를 화면 DTO 객체에 셋! 진짜 이거 맞아?
		orderDTO.setCodeItemList(this.getSOOJOOItems());
		
		log.info("orderDTO : " + orderDTO);
		model.addAttribute("orderDTO", orderDTO);
		
		return "orders/sell_detail";
	}
	
	// 주문서 수정(UPDATE) 요청하는 주소 매핑(POST)
	// => 히든메서드 필터에 의해 PUT 으로 변해야하지만 일단 POST 방식 사용
	@PostMapping("/modify") // "/orders/sell/modify"
	public String updateClient(@ModelAttribute("orderDTO") OrderDTO orderDTO, @ModelAttribute("orderDetailDTO") OrderDetailDTO orderDetailDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		System.out.println("orderDTO : " + orderDTO);
		System.out.println("orderDetailDTO : " + orderDetailDTO);
		
		
		// 주문 정보 수정
		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
		sellService.modifyOrder(orderDTO);
		// 주문 상세 정보 수정
		// >>>>>>>>>>> 최종 작성자 수정 위한 id 가져가야함
//		sellService.modifyOrderDetail(orderDetailDTO);
		
		return "redirect:/orders/sell/" + orderDTO.getDocumentNumber();
	}
	
	//------------------------------------------------------------------------------------------------
	
	/**
	 * 공통코드_아이템에서 수주 관련된 상태값 가져오기
	 * <br>COMMON_MAJOR_CODE : \"ORDER_STATUS_CODE\"
	 * <br>MENU_NAME_KR : \"수주\"
	 * @return
	 */
	private List<CodeItemDTO> getSOOJOOItems() {
		//TODO 공통코드 가져오기 근데 고민해보자. 문제가 좀 있네? 지금 이 방법이 맞냐?
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE);
		for (Iterator<CodeItemDTO> it = codeItemList.iterator(); it.hasNext();) {
			CodeItemDTO item = it.next();
			if (item.getDescription().indexOf(MENU_NAME_KR) < 0) it.remove(); // 수주가 아니면 -1이니까 삭제하라!
		}
		return codeItemList;
	}
}
