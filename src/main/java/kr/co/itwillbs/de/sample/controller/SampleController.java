package kr.co.itwillbs.de.sample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.itwillbs.de.common.util.FileUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
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
	
	private final SampleService sampleService;
	//@Autowired
	public SampleController(SampleService sampleService) {
		this.sampleService = sampleService;
	}
	
	
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
	 * <br>http.header.Content-Type: 'application/x-www-form-urlencoded (이게 기본 값임)
	 * <br>이 경우 @ModelAttribute 어노테이션으로 받으면 매핑되는 DTO의 필드와 이름이 같고 setter가 존재하면 DTO에 필드 값이 채워 짐
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
		
		log.info("requestData : {} ", sampleSearchDTO.toString());
		
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
		
		log.info("requestData : {} ", idx);
		
		if(StringUtil.isLongValue(idx)) {
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
		
		log.info("requestData : param = {}, dto = {} ", idx, sampleDTO);
		
		try {
			if(StringUtil.isLongValue(idx)) {
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
	 * <br> href(https://api.jquery.com/jQuery.ajax/)
	 * <br> "application/json" || 'application/x-www-form-urlencoded; charset=UTF-8'
	 * @param idx 샘플 테이블 PK
	 * @return
	 */
	@DeleteMapping("/{idx}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> removeSample(@PathVariable(name = "idx") String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {} ", idx);
		
		Map<String, Object> response = new HashMap<>();
		try {
			if(StringUtil.isLongValue(idx)) {
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
	form 일땐 enctype="multipart/form-data"
	"Content-Type": "application/json"'application/x-www-form-urlencoded' -> @ModelAttribute SampleDTO sampleDTO
	*/
	/**
	 * 폼데이터 http.header.Content-Type: 'multipart/form-data' 일 경우 샘플
	 * <br>이 경우 @ModelAttribute 어노테이션으로 받으면 매핑되는 DTO의 필드와 이름이 같고 setter가 존재하면 DTO에 필드 값이 채워 짐
	 * <br>DTO는 table에서 온 데이터와 유사히(동일하게) 만들기로 팀내에서 정하였고, 파일은 공통 테이블을 이용해야함
	 * <br>따라서 MultipartFile 타입 변수가 있는 DTO로 어노테이션을 이용해서 받거나
	 * <br>파일만 단독으로 @RequestParam MultipartFile[] mfils 으로 파일을 받으면 됨 
	 * @param sampleDTO
	 * @return
	 */
	@PostMapping("blabla1")
	public String setMultipartSample1(@ModelAttribute SampleDTO sampleDTO, @RequestParam List<MultipartFile> mfileList) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {} ", sampleDTO);
		
		// 멀티파트 파일 필수 일 경우
		for(MultipartFile mfile: mfileList) {
			if(!FileUtil.isValidateForRequiredFile(mfile)) {
				// 필수 파일 없기에 다시 입력 페이지로 리다이렉트 시켜야 함
				
			}
			
			if(FileUtil.isSpecial(mfile.getOriginalFilename())) {
				// 파일에 특수 문자가 있기 때문에 오류 발생 시켜야 함
			}
		}
		// 기타 인풋 데이터 벨리데이트 (중략)
		
		// 벨리데이트 끝낸 후 서비스 작성 로직 넘김
		
		// DB에 데이터 정상적으로 입력 >> 리턴 타입 int 로 메서드 작성해서 affectedRow 전달 받는 것으로 판별하기
		
		// 데이터 입력이 기대값으로 진행되지 않았을 때 if 분기를 줘서 입력 페이지로
		// 기대값처럼 진행되었을 때 예정된 페이지로 이동
		
		return "";
	}
	
	/**
	 * Ajax 인이나, 파일 업로드 때문에 http.header.Content-Type: 'multipart/form-data' 일 때
	 * <br>이 경우 @ModelAttribute 어노테이션으로 받으면 매핑되는 DTO의 필드와 이름이 같고 setter가 존재하면 DTO에 필드 값이 채워 짐
	 * @param sampleDTO
	 * @return
	 */
	@PostMapping("blabla2")
	@ResponseBody
	public String setMultipartSample2(SampleDTO sampleDTO, @RequestParam List<MultipartFile> mfileList) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {} ", sampleDTO);
		
		// 멀티파트 파일 필수 일 경우
		for(MultipartFile mfile: mfileList) {
			if(!FileUtil.isValidateForRequiredFile(mfile)) {
				// 필수 파일 없기에 다시 입력 페이지로 리다이렉트 시켜야 함
				
			}
			
			if(FileUtil.isSpecial(mfile.getOriginalFilename())) {
				// 파일에 특수 문자가 있기 때문에 오류 발생 시켜야 함
			}
		}
		// 기타 인풋 데이터 벨리데이트 (중략)
		
		// 벨리데이트 끝낸 후 서비스 작성 로직 넘김
		
		// DB에 데이터 정상적으로 입력 >> 리턴 타입 int 로 메서드 작성해서 affectedRow 전달 받는 것으로 판별하기
		
		// 데이터 입력이 기대값으로 진행되지 않았을 때 if 분기를 줘서 입력 페이지로
		// 기대값처럼 진행되었을 때 예정된 페이지로 이동
		
		return "";
	}
	
}
