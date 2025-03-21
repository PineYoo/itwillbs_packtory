package kr.co.itwillbs.de.orders.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
	
	@GetMapping("/client")
	public String correspondentMain() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return "orders/client_main.html";
	}
}
