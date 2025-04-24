package kr.co.itwillbs.de.mes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialSearchDTO;
import kr.co.itwillbs.de.mes.service.BomService;
import kr.co.itwillbs.de.mes.service.RawMaterialService;
import kr.co.itwillbs.de.orders.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/rawmaterial")
public class RawMaterialController {

	private final RawMaterialService rawMaterialService;
	private final CommonCodeUtil commonCodeUtil;
	private final ClientService clientService;
	private final BomService bomService;
	private final String PATH = "/mes/rawmaterial";

	// 원자재 등록 폼 페이지
	@GetMapping("/new")
	public String rawMaterialRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 공통코드 + BOM
		model.addAttribute("itemUnit", commonCodeUtil.getCodeItems("ITEM_UNIT"));
		model.addAttribute("materialType", commonCodeUtil.getCodeItems("MATERIAL_TYPE"));
		model.addAttribute("bomList", bomService.getBomList());

		model.addAttribute("rawMaterialDTO", new RawMaterialDTO());

		return PATH + "/rawMaterial_form";
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

		// 공통코드 + 거래처 + BOM
		model.addAttribute("materialType", commonCodeUtil.getCodeItems("MATERIAL_TYPE"));
		model.addAttribute("clientList", clientService.getClientList());

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return PATH + "/rawmaterial_list";
	}

	// 원자재 상세 조회
	@GetMapping("/{idx}")
	public String getRawMaterial(@PathVariable("idx") Long idx, Model model) {
		log.info("getRawMaterial --- start");

		// 원자재 상세정보 조회
		RawMaterialDTO rawMaterialDTO = rawMaterialService.getRawMaterial(idx);
		model.addAttribute("rawMaterialDTO", rawMaterialDTO);

		// 공통코드 + BOM
		model.addAttribute("itemUnit", commonCodeUtil.getCodeItems("ITEM_UNIT"));
		model.addAttribute("materialType", commonCodeUtil.getCodeItems("MATERIAL_TYPE"));
		model.addAttribute("bomList", bomService.getBomList());

		return PATH + "/rawmaterial_detail";
	}

	// 원자재 수정
	@PutMapping("/updateRawMaterial")
	public ResponseEntity<Map<String, Object>> updateRawMaterial(@RequestBody RawMaterialDTO rawMaterialDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			rawMaterialService.updateRawMaterial(rawMaterialDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아니었나? 하는 기억에 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
}
