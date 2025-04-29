package kr.co.itwillbs.de.mes.controller;

import java.math.BigDecimal;
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

	// 마스터 자재 등록 폼 페이지
	@GetMapping("/new")
	public String rawMaterialRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 공통코드 + BOM
		model.addAttribute("materialType", commonCodeUtil.getCodeItems("MATERIAL_TYPE"));
		model.addAttribute("rawMaterialDTO", new RawMaterialDTO());

		return PATH + "/rawMaterial_form";
	}

	// 마스터 자재 등록 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<Map<String, Object>> rawMaterialRegister(@RequestBody @Valid RawMaterialDTO rawMaterialDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(rawMaterialDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			rawMaterialService.registerMasterMaterial(rawMaterialDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 마스터 자재 목록 조회 (검색)
	@GetMapping("")
	public String getRawMaterialList(@ModelAttribute RawMaterialSearchDTO searchDTO, Model model) {
		log.info("getRawMaterialList --- start");

		// 페이징 정보 조회
		searchDTO.getPageDTO().setTotalCount(rawMaterialService.searchMasterMaterialCount(searchDTO));

		// 마스터 자재 목록 조회
		List<RawMaterialDTO> rawMaterialList = rawMaterialService.getMasterMaterialList(searchDTO);
		model.addAttribute("materialType", commonCodeUtil.getCodeItems("MATERIAL_TYPE"));
		model.addAttribute("rawMaterialList", rawMaterialList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return PATH + "/rawmaterial_list";
	}

	// 마스터 자재 상세 조회
	@GetMapping("/{idx}")
	public String getRawMaterial(@PathVariable("idx") Long idx, Model model) {
		log.info("getRawMaterial --- start | idx={}", idx);

		// 마스터 자재 상세정보 조회
		RawMaterialDTO rawMaterialDTO = rawMaterialService.getMasterMaterial(idx);
		model.addAttribute("rawMaterialDTO", rawMaterialDTO);

		// 부속 자재 목록 조회
		List<RawMaterialDTO> subMaterialList = rawMaterialService.getSubMaterialsByIdx(idx);
		model.addAttribute("subMaterialList", subMaterialList);

		// 공통코드 + BOM
		model.addAttribute("itemUnit", commonCodeUtil.getCodeItems("ITEM_UNIT"));
		model.addAttribute("materialType", commonCodeUtil.getCodeItems("MATERIAL_TYPE"));
		model.addAttribute("clientList", clientService.getClientList());
		model.addAttribute("bomList", bomService.getBomList());

		return PATH + "/rawmaterial_detail";
	}

	// 마스터 자재 수정
	@PutMapping("/master/update")
	public ResponseEntity<Map<String, Object>> updateRawMaterial(@RequestBody RawMaterialDTO rawMaterialDTO,
			Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		model.addAttribute("itemUnit", commonCodeUtil.getCodeItems("ITEM_UNIT"));
		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			rawMaterialService.updateMasterMaterial(rawMaterialDTO);
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

	// 부속 자재 등록 (AJAX)
	@PostMapping("/sub/new")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> insertSubMaterial(@RequestBody List<RawMaterialDTO> rawMaterialDTOs) {

		Map<String, Object> response = new HashMap<>();
		try {
			for (RawMaterialDTO dto : rawMaterialDTOs) {
				// 부모 인덱스 값만 설정하고, 유효한 데이터만 등록하도록 처리
				if (dto.getIdx() != null) {
					dto.setParentsIdx(String.valueOf(dto.getIdx())); // 부모 인덱스 설정
				}
				if (dto.getType() == null) {
					dto.setType("기본값"); // 기본값 설정 (필요한 경우)
				}

				// 유효한 필드들만 등록되도록 필터링
				if (isValidSubMaterial(dto)) {
					rawMaterialService.insertSubMaterial(dto);
				}
			}
			response.put("status", "success");
			response.put("message", "등록되었습니다.");
		} catch (Exception e) {
			log.error("부속 자재 등록 실패", e);
			response.put("status", "fail");
			response.put("message", "등록에 실패했습니다.");
		}
		return ResponseEntity.ok(response);
	}

	// 유효한 자재만 처리하는 메서드
	private boolean isValidSubMaterial(RawMaterialDTO dto) {
		return dto.getName() != null && !dto.getName().trim().isEmpty() && dto.getQuantity() != null
				&& dto.getQuantity().compareTo(BigDecimal.ZERO) > 0 && dto.getPrice() != null;
//				&& dto.getPrice().compareTo(BigDecimal.ZERO) > 0;
	}

	// 부속 자재 수정 (AJAX)
	@PutMapping("/sub/update")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateSubMaterial(@RequestBody RawMaterialDTO rawMaterialDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			rawMaterialService.updateSubMaterial(rawMaterialDTO);
			response.put("status", "success");
			response.put("message", "수정되었습니다.");
		} catch (Exception e) {
			log.error("부속 자재 수정 실패", e);
			response.put("status", "fail");
			response.put("message", "수정에 실패했습니다.");
		}
		return ResponseEntity.ok(response);
	}
}
