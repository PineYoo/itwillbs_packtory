package kr.co.itwillbs.de.mes.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.mes.dto.RawMaterialStockDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialStockSearchDTO;
import kr.co.itwillbs.de.mes.service.RawMaterialStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/stock")
public class RawMaterialStockController {

	private final RawMaterialStockService rawMaterialStockService;
	private final CommonCodeUtil commonCodeUtil;
	private final String MATERIAL_TYPE = "MATERIAL_TYPE";
	private final String PATH = "/mes/stock";

	// 마스터 자재 목록 조회 (검색)
	@GetMapping("/material")
	public String getRawMaterialStockList(@ModelAttribute RawMaterialStockSearchDTO searchDTO, Model model) {
		log.info("getRawMaterialList --- start");

		// 페이징 정보 조회
		searchDTO.getPageDTO().setTotalCount(rawMaterialStockService.searchMaterialStockCount(searchDTO));

		// 마스터 자재 목록 조회
		List<RawMaterialStockDTO> rawMaterialStockList = rawMaterialStockService.getMaterialStockList(searchDTO);
		model.addAttribute("rawMaterialStockList", rawMaterialStockList);

		model.addAttribute("materialTypeList", commonCodeUtil.getCodeItems(MATERIAL_TYPE));
		
		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return PATH + "/rawmaterial_stock_list";
	}

	// 마스터 자재 상세 조회
//	@GetMapping("/{idx}")
//	public String getRawMaterial(@PathVariable("idx") Long idx, Model model) {
//		log.info("getRawMaterial --- start | idx={}", idx);
//
//		// 마스터 자재 상세정보 조회
//		RawMaterialDTO rawMaterialDTO = rawMaterialService.getMasterMaterial(idx);
//		model.addAttribute("rawMaterialDTO", rawMaterialDTO);
//
//		// 부속 자재 목록 조회
//		List<RawMaterialDTO> subMaterialList = rawMaterialService.getSubMaterialsByIdx(idx);
//		model.addAttribute("subMaterialList", subMaterialList);
//
//		// 공통코드
//		model.addAttribute("itemUnit", commonCodeUtil.getCodeItems("ITEM_UNIT"));
//		model.addAttribute("materialType", commonCodeUtil.getCodeItems("MATERIAL_TYPE"));
//		model.addAttribute("clientList", clientService.getClientList());
//		model.addAttribute("qcType", commonCodeUtil.getCodeItems("QC_STANDARD"));
//		model.addAttribute("qcType1", qcStandardService.getQcStandardList());
//
//		return PATH + "/rawmaterial_detail";
//	}

}
