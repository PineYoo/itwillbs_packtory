package kr.co.itwillbs.de.orders.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.dto.ClientInfoDTO;
import kr.co.itwillbs.de.orders.dto.ClientSearchDTO;
import kr.co.itwillbs.de.orders.service.ClientService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders/client")
public class ClientController {
	
	@Autowired
	private ClientService clientService;
	
	//	거래처관리 메인화면(거래처 전체 리스트 목록)
	@GetMapping("")
	public String clientMain(@ModelAttribute ClientSearchDTO clientSearchDTO ,Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		//	거래처 모든 목록 요청
		List<ClientDTO> clientDTOList = clientService.getClientList(clientSearchDTO);
		
		model.addAttribute("clientDTOList", clientDTOList);
		model.addAttribute("searchDTO", clientSearchDTO);
		
		return "orders/client_main.html";
	}
	
	@GetMapping("/regist")
	public String registClient(@ModelAttribute ClientDTO clientDTO ,Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("clientDTO", clientDTO);
		return "orders/client_register";
	}
	
	//	거래처 등록 요청
	@PostMapping("")
	public String insertClient(@ModelAttribute ClientDTO clientDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("input params : {}",clientDTO.toString());
		
		
		if(clientService.insertClient(clientDTO) > 0) {
			return "redirect:/orders/client";
		} else {
			return "orders/client_register";
		} 
		
	}
	
	//	거래처 상세보기 페이지
	@GetMapping("/detail/{companyNumber}")
	public String clientDetail(@PathVariable("companyNumber") String companyNumber, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//	companyNumber(거래처코드)로 거래처 정보 가져오기
		ClientInfoDTO client = clientService.getClient(companyNumber);
		log.info(client.toString());
		model.addAttribute("clientInfoDTO", client);
		
		return "orders/client_detail.html";
	}
	
	@PutMapping("/update")
	public String updateClient(@ModelAttribute ClientInfoDTO clientInfoDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//	거래처 정보 수정
		clientService.updateClient(clientInfoDTO);
		//	거래처_부가정보 수정
		clientService.updateClientInfo(clientInfoDTO);
		
		return "redirect:/orders/client";
	}
	
	
	
	
	
}
