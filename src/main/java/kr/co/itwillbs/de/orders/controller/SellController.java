package kr.co.itwillbs.de.orders.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.service.SellService;
import lombok.extern.slf4j.Slf4j;

/* 수주관리 */
@Slf4j
@RequestMapping("/orders/sell") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
@Controller
public class SellController {
	@Autowired
	private SellService sellService;
	
	// 수주 관리 목록 조회(SELECT) 요청(GET)
	@GetMapping(value = {"", "/"})  // "/orders/sell"
	public String getSellList(Model model, HttpSession session) {

	    log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    
	    // 수주 관리 목록 리스트 조회 요청(SELECT)
	    List<HashMap<String, Object>> sellDTOList = sellService.getSellList();
	    model.addAttribute("sellDTOList", sellDTOList);

	    return "orders/sell_management";
	}
	
	
//	@PostMapping(value = {"", "/"})  // "/orders/sell"
//	public String getSearchItem(OrderSearchDTO orderSearchDTO, Model model) {
//		System.out.println("검색 대상 : " + orderSearchDTO);
//		
//		// 수주 관리 목록 리스트 조회 요청(SELECT)
//		// => 메서드명은 동일하나 파라미터를 다르게 수행하도록 오버로딩
//		List<HashMap<String, Object>> sellDTOList = sellService.getSellList(orderSearchDTO);
//		model.addAttribute("sellDTOList", sellDTOList);
//		model.addAttribute("orderSearchDTO", orderSearchDTO);
//		
//		return "item/item_list.html";
//	}
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
		
		
		HashMap<String, Object> sellDTO = sellService.getOrder(documentNumber);
		System.out.println("sellDTO : " + sellDTO);
		model.addAttribute("sellDTO", sellDTO);
		
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
}
