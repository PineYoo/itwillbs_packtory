package kr.co.itwillbs.de.mes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.mes.dto.BomDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialSearchDTO;
import kr.co.itwillbs.de.mes.service.BomService;
import kr.co.itwillbs.de.mes.service.RawMaterialService;
import kr.co.itwillbs.de.orders.dto.ClientDTO;
import kr.co.itwillbs.de.orders.service.ClientService;
import kr.co.itwillbs.de.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/rawmaterial")
public class RawMaterialController {

	private final RawMaterialService rawMaterialService;
	private final ClientService clientService;
	private final OrderService orderService;
	private final BomService bomService;

	// 원자재 등록 폼 페이지
	@GetMapping("/new")
	public String rawMaterialRegisterForm(Model model) {

		// 로그인 유저 정보 세팅
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			LoginVO loginVO = userDetails.getLoginVO();
			model.addAttribute("userDetails", userDetails);
			model.addAttribute("loginVO", loginVO);
		}

//		// 거래처 목록 조회
//		List<ClientDTO> clientList = clientService.getClientList();
//		model.addAttribute("clientList", clientList);

		// 거래처 정보 가져오기
		List<ClientDTO> clientList = orderService.getClientList();
		model.addAttribute("clientList", clientList);

		// BOM 목록 조회
		List<BomDTO> bomList = bomService.getBomList();
		model.addAttribute("bomList", bomList);

		model.addAttribute("rawMaterialDTO", new RawMaterialDTO());

		return "mes/rawMaterial/rawMaterial_form";
	}

	// 원자재 등록 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> rawMaterialRegister(@RequestBody @Valid RawMaterialDTO rawMaterialDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(rawMaterialDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			if (rawMaterialDTO.getClientIdx() != null) {
				rawMaterialDTO.setClientIdx(String.valueOf(Long.parseLong(rawMaterialDTO.getClientIdx())));
			}

			if (rawMaterialDTO.getBomIdx() != null) {
				rawMaterialDTO.setBomIdx(String.valueOf(Long.parseLong(rawMaterialDTO.getBomIdx())));
			}

			rawMaterialService.registerRawMaterial(rawMaterialDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 원자재 목록 조회 (검색)
	@GetMapping("")
	public String getRawMaterialList(@ModelAttribute RawMaterialSearchDTO searchDTO, Model model) {
		log.info("getRawMaterialList --- start");

		// 페이징 정보 조회
		searchDTO.getPageDTO().setTotalCount(rawMaterialService.searchRawMaterialCount(searchDTO));

		// 원자재 목록 조회
		List<RawMaterialDTO> rawMaterialList = rawMaterialService.getRawMaterialList(searchDTO);
		model.addAttribute("rawMaterialList", rawMaterialList);

		// 거래처 목록 조회
		List<ClientDTO> clientList = clientService.getClientList();
		model.addAttribute("clientList", clientList);

		// BOM 목록 조회
		List<BomDTO> bomList = bomService.getBomList();
		model.addAttribute("bomList", bomList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return "mes/rawmaterial/rawmaterial_list";
	}

	// 원자재 상세 조회
	@GetMapping("/{idx}")
	public String getRawMaterial(@PathVariable("idx") Long idx, Model model) {
		log.info("getRawMaterial --- start");

		// 원자재 상세정보 조회
		RawMaterialDTO rawMaterialDTO = rawMaterialService.getRawMaterial(idx);
		model.addAttribute("rawMaterialDTO", rawMaterialDTO);

		// 거래처 정보 가져오기
		List<ClientDTO> clientList = orderService.getClientList();
		model.addAttribute("clientList", clientList);

		// BOM 목록 조회
		List<BomDTO> bomList = bomService.getBomList();
		model.addAttribute("bomList", bomList);

		return "mes/rawmaterial/rawmaterial_detail";
	}

	// 원자재 수정
	@PutMapping("/{idx}")
	public ResponseEntity<Map<String, Object>> updateRawMaterial(@PathVariable("idx") Long idx,
			@RequestBody RawMaterialDTO rawMaterialDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			rawMaterialService.updateRawMaterial(rawMaterialDTO);
			response.put("status", "success");
			response.put("message", "원자재 수정이 완료되었습니다.");
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			log.error("원자재 수정 실패: {}", e.getMessage());
			response.put("status", "error");
			response.put("message", "원자재을 찾을 수 없습니다.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (Exception e) {
			log.error("원자재 수정 실패: {}", e.getMessage());
			response.put("status", "error");
			response.put("message", "원자재 수정 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 원자재 삭제 (Soft Delete)
	@DeleteMapping("/{idx}")
	@ResponseBody
	public String deleteRawMaterial(@PathVariable("idx") Long idx) {
		try {
			rawMaterialService.deleteRawMaterial(idx);
			return "success";
		} catch (Exception e) {
			log.error("원자재 삭제 실패: {}", e.getMessage());
			return "error";
		}
	}
}
