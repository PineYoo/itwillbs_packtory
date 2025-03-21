package kr.co.itwillbs.de.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.sample.dto.ItemDTO;
import kr.co.itwillbs.de.sample.dto.ItemSearchDTO;
import kr.co.itwillbs.de.sample.service.ItemService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/items") // 반복되는 경로를 미리 매핑(클래스 내부에서는 추가되는 경로만 매핑)
public class ItemController {
/**
 * 수업 시간에 진행한 JPA 게시판!
 */
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 아이템 등록 페이지(view)와 연결
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/new"})
	public String itemRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 뷰 페이지에서 ItemDTO 에 기술된 Validation 항목 체크를 위해 빈 ItemDTO 객체를 함께 전달
		model.addAttribute("itemDto", new ItemDTO());
		
		return "sample/item_register_form.html";
	}
	
	/**
	 * 상품 목록 조회(SELECT)을 요청하는 "/items" 주소 매핑(GET)
	 * @return
	 */
	@GetMapping(value={"","/"}) // 이미 컨트롤러 자체에서 공통 경로 "/items" 를 매핑했으므로 널스트링("")을 지원
	public String getItemList(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<ItemDTO> itemList = itemService.getItemList();
		log.debug("itemList : " + itemList);
		
		model.addAttribute("itemDtoList",itemService.getItemList());
		// 입력값 검증 및 파라미터로 활용할 ItemSearchDTO 객체 생성 후 Model 에 저장
		ItemSearchDTO itemSearchDto = new ItemSearchDTO();
		model.addAttribute("itemSearchDto", itemSearchDto);
		
		return "sample/item_list.html";
	}
	
	/**
	 * 상품 목록 조회(SELECT)을 요청하는 "/items/search" 주소 매핑(GET)
	 * ItemSearchDTO
	 * @return
	 */
	@GetMapping(value={"/search"}) // 이미 컨트롤러 자체에서 공통 경로 "/items" 를 매핑했으므로 널스트링("")을 지원
	public String getItemSearchList(@ModelAttribute ItemSearchDTO itemSearchDto, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.debug("input params : {}", itemSearchDto.toString());
		
		List<ItemDTO> itemList = itemService.getItemSearchList(itemSearchDto);
		log.debug("itemList : " + itemList);
		//model.addAttribute("itemDtoList",itemService.getItemSearchList());
		model.addAttribute("itemDtoList",itemList);
		// 입력값 검증 및 파라미터로 활용할 ItemSearchDTO 객체 생성 후 Model 에 저장
		model.addAttribute("itemSearchDto", itemSearchDto);
		
		return "sample/item_list.html";
	}
	
	/**
	 * 상품 등록(INSERT)을 요청하는 "/items" 주소 매핑(POST)
	 * @return
	 */
	@PostMapping(value={"","/"}) // 이미 컨트롤러 자체에서 공통 경로 "/items" 를 매핑했으므로 널스트링("")을 지원
	public String itemRegister(@ModelAttribute("itemDto") @Valid ItemDTO itemDto, BindingResult bindingResult) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// ItemDTO 객체에 전달되는 파라미터에들에 대한 Validation Check 수행을 위해 변수 선언부 앞에 @Valid 어노테이션을 붙임
		// => 전달되는 파라미터들이 ItemDTO 객체에 바인딩 될 때 체크 수행하고 이 결과를 BindingResult 객체에 전달함
		// => 이 때, 정확한 바인딩을 위해 바인딩 타입 변수 선언 시 @ModelAttribute("객체변수명") 까지 명시하자!
		//	(지정하지 않으면 뷰페이지에서 바인딩 결과 처리 불가)
		//	(지정 시, 별도로 Model 객체에 바인딩 객체 전달하지 않아도 자동으로 전달됨)
		log.debug("requestData : " + itemDto.toString());
		
		log.debug("getAllErrors() : " + bindingResult.getAllErrors()); // 체크 결과 발생한 모든 오류 확인
		log.debug("hasErrors() : " + bindingResult.hasErrors()); // 체크 후 오류 발생 여부 확인(true/flase)
		
		if(bindingResult.hasErrors()) {
			//model.addAttribute("itemDto", itemDto); //이걸 사용하면 벨리데이션 하기 전 값이 전달 됨
			return "sample/item_register_form.html";
		}
		
		itemService.registerItem(itemDto);
		
		return "redirect:/items";
	}
	
	/**
	 * 단일 상품 조회(SELECT)를 요청하는 "/items" 주소 매핑(GET)
	 * <br>단, 목록 조회와 달리 /items/xxx 형식으로 상품 아이디값이 함께 전달됨
	 * <br>따라서, 전달되는 아이디값도 경로로 활용 가능
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/{id}") // 매핑 매서드 파라미터 선언 시 @PathVariable("경로명") 을 지정하여 /{경로명} 바인딩
	public String getItem(@PathVariable("id") Long id, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// ItemService - getItem() 메서드 호출하여 상품 1개 상세정보 조회 요청
		ItemDTO itemDto = itemService.getItem(id);
		log.debug("itemDto : {}", itemDto);
		
		model.addAttribute("itemDto", itemDto);
		
		return "sample/item_detail.html";
	}
	
	/**
	 * 상품 수정(UPDATE)를 요청하는 "items" 주소 매핑(PUT)
	 * <br>요청 시 클라이언트에서는 POST 방식이나, 히든메서드 필터에 의해 PUT 으로 변환됨
	 * @param id
	 * @param itemDto
	 * @param bindingResult
	 * @return
	 */
	@PutMapping("/{id}")
	public String putItem(@PathVariable("id") Long id, @ModelAttribute("itemDto") ItemDTO itemDto, 
							BindingResult bindingResult) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 수정할 데이터들은 ItemDTO 객체에 저장되어 있지만 아이디 값이 없음
		// 따라서, id 파라미터로 전달 받은 값을 ItemDTO 객체에 저장 후 수정 요청

		// 벨리데이트
		if(bindingResult.hasErrors()) {
			return "sample/item_detail.html";
		}
		
		itemDto.setId(id);
		itemService.modifyItem(itemDto);
		
		// 상품 상세정보 조회 페이지 리다이렉트(GET 방식 /items/ 와 id값 결합 필요)
		return "redirect:/items/"+id;
	}
	
	/**
	 * 아이템 삭제(for Ajax)
	 * <br>Http_mothod: delete 로 요청 받음
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@ResponseBody
	public String removeItem(@PathVariable("id") Long id) {
		
		try {
			itemService.removeItem(id);
		} catch (Exception e) {
			e.printStackTrace();
			// 해당 상품 조회 실패 시 삭제가 불가능하므로 AJAX 응답을 실패로 전송
			return e.getMessage();
		}
		
		return "success";
	}
}
