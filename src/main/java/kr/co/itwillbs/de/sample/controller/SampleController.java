package kr.co.itwillbs.de.sample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.common.util.CommonUtils;
import kr.co.itwillbs.de.sample.dto.SampleDTO;
import kr.co.itwillbs.de.sample.dto.SampleSearchDTO;
import kr.co.itwillbs.de.sample.service.SampleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value={"/sample"})
@Controller
public class SampleController {
/**
 * 마이바티스용 샘플 게시판
 * @return
 */
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private CommonUtils comUtil;
	
	/**
	 * 샘플 등록 페이지(view)를 요청하는 "/sample/new" 연결
	 * @param model
	 * @return
	 */
	@GetMapping(value={"/new"})
	public String sampleRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		model.addAttribute("sampleDTO", new SampleDTO());
		
		return "sample/sample_register_form";
	}

	/**
	 * 샘플 등록(INSERT)을 하는 "/sample" 연결 POST!
	 * @return
	 */
	@PostMapping(value={"","/"})
	public String sampleRegister(@ModelAttribute("sampleDTO") SampleDTO sampleDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : " + sampleDTO.toString());
		
		sampleService.registerSample(sampleDTO);
		
		return "redirect:/sample";
	}
	
	/**
	 * 샘플 목록 조회(SELECT)를 요청하는 "/sample" 연결 GET!
	 * @return
	 */
	@GetMapping(value={"","/"})
	public String getSampleList(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<SampleDTO> sampleDTOlist = sampleService.getSampleList();
		model.addAttribute("sampleDTOlist", sampleDTOlist);
		
		SampleSearchDTO sampleSearchDTO = new SampleSearchDTO();
		model.addAttribute("sampleSearchDTO", sampleSearchDTO);
		
		return "sample/sample_list";
	}
	
	/**
	 * 샘플 검색 조건 조회
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/search")
	public String sampleGetList(@ModelAttribute SampleSearchDTO sampleSearchDTO, Model model) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : " + sampleSearchDTO.toString());
		
		// 조회 결과 값 뷰에 전달
		model.addAttribute("sampleDTOList", sampleService.getSampleSearchList(sampleSearchDTO));
		// 검색 조건 값 뷰에 전달
		model.addAttribute("sampleSearchDTO", sampleSearchDTO);
		
		return "sample/list";
	}
	
	/**
	 * 단일 샘플 조회(SELECT) "/sample/{idx}" 주소 매핑(GET)
	 * @param idx 샘플 테이블 PK값
	 * @param model sampleDTO
	 * @return
	 */
	@GetMapping("/{idx}")
	public String getSample(@PathVariable(name = "idx") String idx, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		if(comUtil.isLongValue(idx)) {
			// 정수일 경우 조회 가능
			model.addAttribute("sampleDTO", sampleService.getSample(idx));
			// 조회 수도 있기 때문에 업데이트 하자!
			SampleDTO sampleDTO = new SampleDTO();
			// Idx만 update 쿼리에 보낼 경우 readCnt를 증가시키게 작성해두었다.
			sampleDTO.setIdx(Long.parseLong(idx));
			try {
				sampleService.modifySample(sampleDTO);
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/sample";
			}
			
		} else {
			// 정수가 아닐 경우 다시 리스트로 리다이렉트
			return "redirect:/sample";
		}
		
		return "sample/sample_detail";
	}
	
	/**
	 * 샘플 수정(UPDATE)를 요청하는 "/sample/{idx}" 주소 매핑(PUT)
	 * <br> 히든 메서드 필터에 의해 PUT으로 변경헤서 서브밋함
	 * @param idx 샘플 테이블 PK값
	 * @param sampleDTO 샘플DTO
	 * @return
	 */
	@PutMapping("/{idx}")
	public String putSample(@PathVariable(name = "idx") String idx, @ModelAttribute("sampleDTO") SampleDTO sampleDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		try {
			if(comUtil.isLongValue(idx)) {
				//정수일 경우 업데이트 가능
				sampleDTO.setIdx(Long.parseLong(idx));
				sampleService.modifySample(sampleDTO);
			} else {
				// 정수가 아닐 경우 다시 정보 페이지로
				return "sample/sample_detail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "sample/sample_detail";
		}
		
		// 샘플 정보 페이지 리다이렉트 (GET)방식 
		return "redirect:/sample/"+idx;
	}
	
	/**
	 * 샘플 삭제 -> 샘플 테이블의 isDeleted = Y 로 변경
	 * <br> Ajax 로 호출하는데 두번씩 호출 되어서 이상한 꼼수로 막은것 같다... 정답을 찾아보자
	 * @param idx 샘플 테이블 PK
	 * @return
	 */
	@DeleteMapping("/{idx}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> removeSample(@PathVariable(name = "idx") String idx) {
		
		Map<String, Object> response = new HashMap<>();
		try {
			if(comUtil.isLongValue(idx)) {
				sampleService.removeItem(Long.parseLong(idx));
				response.put("status", "success");
				response.put("message", "정상적으로 수정 되었습니다.");
			} else {
				throw new Exception("해당 샘플이 존재하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 해당 상품 조회 실패 시 삭제가 불가능하므로 AJAX 응답을 실패로 전송
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	/*
	TODO 멀티파트 작업 완료 후 공유 필!
	*/
	
}
