package kr.co.itwillbs.de.orders.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/client")
	public String orderMain(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<ClientDTO> clientDTOList = orderService.getClientList();
		
		model.addAttribute("clientDTOList", clientDTOList);
		model.addAttribute("clientDTO", new ClientDTO());
		
		return "orders/client_main.html";
	}
	
	@PostMapping("/client")
	public String insertClient(@ModelAttribute ClientDTO clientDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("input params : {}",clientDTO.toString());
		
		if(orderService.insertClient(clientDTO) > 0) {
			return "redirect:/orders/client";
		} else {
			//	TODO 에러페이지 생성 시 수정 필요
			return "redirect:/orders/client";
		} 
		
		
		
	}
	
	
	
}
