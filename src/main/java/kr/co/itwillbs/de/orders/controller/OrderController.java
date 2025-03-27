package kr.co.itwillbs.de.orders.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientInfoDTO;
import kr.co.itwillbs.de.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	//	거래처관리 메인화면(거래처 전체 리스트 목록)
	@GetMapping("/client")
	public String orderMain(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<ClientDTO> clientDTOList = orderService.getClientList();
		
		model.addAttribute("clientDTOList", clientDTOList);
		model.addAttribute("clientDTO", new ClientDTO());
		
		return "orders/client_main.html";
	}
	
	//	거래처 등록 요청
	@PostMapping("/client")
	@ResponseBody
	public String insertClient(@ModelAttribute ClientDTO clientDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("input params : {}",clientDTO.toString());
		
		
		if(orderService.insertClient(clientDTO) > 0) {
			return "success";
		} else {
			//	TODO 에러페이지 생성 시 수정 필요
			return "fail";
		} 
		
	}
	
	//	거래처 상세보기 페이지
	@GetMapping("/clientDetail")
	public String clientDetail(@RequestParam(value = "businessNumber") String businessNumber, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//	businessNumber(거래처코드)로 거래처 정보 가져오기
		Map<String, Object> client = orderService.getClient(businessNumber);
		model.addAttribute("client", client);
		
		return "orders/client_detail.html";
	}
	
	@PostMapping("/updateClient")
	public String updateClient(@ModelAttribute ClientDTO clientDTO,
							   @ModelAttribute ClientInfoDTO clientInfoDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//	거래처 정보 수정
		orderService.updateClient(clientDTO);
		//	거래처_부가정보 수정
		orderService.updateClientInfo(clientInfoDTO);
		
		return "redirect:/orders/client";
	}
	
	
	
	
}
