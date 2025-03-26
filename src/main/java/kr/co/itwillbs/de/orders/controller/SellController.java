package kr.co.itwillbs.de.orders.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientInfoDTO;
import kr.co.itwillbs.de.orders.dto.OrderDTO;
import kr.co.itwillbs.de.orders.dto.OrderDetailDTO;
import kr.co.itwillbs.de.orders.dto.OrderFormDTO;
import kr.co.itwillbs.de.orders.service.SellService;
import kr.co.itwillbs.de.sample.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;

/* 수주관리 */
@Slf4j
@RequestMapping("/orders/sell") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
@Controller
public class SellController {
	@Autowired
	private SellService sellService;
	
	// 수주 관리 목록 조회(SELECT) 요청(GET)
	@GetMapping(value={"","/"})	// "/orders/sell"
	public String getSellList(Model model, HttpSession session) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
//		List<OrderDTO> sellDTOList = sellService.getSellList();
		// HashMap으로 가져옴(OrderDTO, OrderDetailDTO 같이 가져와야하므로)
		List<HashMap<String, Object>> sellDTOList = sellService.getSellList();
		log.info(">>>>> sellDTOList : " + sellDTOList);
		model.addAttribute("sellDTOList", sellDTOList);
		
//		
//		SampleSearchDTO sampleSearchDTO = new SampleSearchDTO();
//		model.addAttribute("sampleSearchDTO", sampleSearchDTO);
		
		return "orders/sell_management";
	}
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
	@PostMapping(value= {"", "/"}) // "/orders/sell"
	public String sellRegister(@ModelAttribute("orderFormDTO") @Valid OrderFormDTO orderFormDTO, BindingResult bindingResult) {
	    log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

	    System.out.println("orderDTO : " + orderFormDTO.getOrderDTO());
	    System.out.println("orderDetailDTO : " + orderFormDTO.getOrderDetailDTO());
	    System.out.println("clientDTO : " + orderFormDTO.getClientDTO());
	    System.out.println("clientInfoDTO : " + orderFormDTO.getClientInfoDTO());
	    
	    log.debug("requestData : " + orderFormDTO.toString());
		log.debug("getAllErrors() : " + bindingResult.getAllErrors()); // 체크 결과 발생한 모든 오류 확인
		
		// 주문서 등록 요청(INSERT)
		sellService.registerOrder(orderFormDTO.getOrderDTO());
		sellService.registerOrderDetail(orderFormDTO.getOrderDetailDTO());
		
	    
	    
	    // ----- 상품 선택 및 등록은 나중에 -----
	    
	    return "redirect:/orders/sell";
	}

	
	
	
	
	//------------------------------------------------------------------------------------------------
}
