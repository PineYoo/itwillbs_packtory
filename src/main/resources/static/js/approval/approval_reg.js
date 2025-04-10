document.addEventListener("DOMContentLoaded", function(){
	//---------------------------------------------------
	//	결재라인 추가 시 사용할 변수 선언
	let approverCount = 0;
	const maxApprovers = 3;
	//	detail과 reg에 공용으로 쓰기 위해 count값 초기화
	reorderApprovers();
	//---------------------------------------------------
	// 기간별 검색 필터링 제이쿼리 (datepicker)
    $('#dueDate').daterangepicker({
        minDate: moment(),		// 오늘 이전 날짜 선택 불가
        locale: {
			start: '시작일시',
			end: '종료일시',
		    separator: " ~ ", // 시작일시와 종료일시 구분자
		    format: 'YYYY-MM-DD', // 일시 노출 포맷
		    applyLabel: "확인", // 확인 버튼 텍스트
		    cancelLabel: "취소", // 취소 버튼 텍스트
		    daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
		    monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			customRangeLabel: '기간 지정',
		 },
		autoUpdateInput: false,
	    timePicker: false, // 시간 노출 여부
	    showDropdowns: true, // 년월 수동 설정 여부
	    autoApply: false, // 확인/취소 버튼 사용여부
		ranges: {
            '오늘': [moment()],
			'일주일' : [moment(), moment().add(7, 'days')],
			'한달' : [moment(),  moment().add(1, 'months')]
        },
    }).on('show.daterangepicker', function (ev, picker) {
        picker.container.addClass('packtoryCustomPicker');  // common.css                          
    });
    
    // 날짜 선택 후에도 placeholder 유지
    $('#dueDate').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(`${picker.startDate.format('YYYY-MM-DD')} ~ ${picker.endDate.format('YYYY-MM-DD')}`);
    });
    
	// 날짜 검색 초기화 버튼
	$("#initDateBtn").on('click', function() {
		$('#dueDate').val('');
		productReport.draw();
	});
	
	//---------------------------------------------------
	//	결재라인
	$("#addApproverLineBtn").on("click", function() {
		if(approverCount >= maxApprovers) {
			alert("최대" + maxApprovers + "명까지 추가할 수 있습니다.")
			return;
		}
		
		approverCount++;
		
		let approverInput = `
			<div class="approver-group" id="approver-${approverCount}">
				<input type="hidden" class="approver-id" name="approver${approverCount}" value="">
                <input type="text"
						class="form-control approver-input"
						data-index="${approverCount}"
						placeholder="결재자 ${approverCount}"
						value=""
						readonly>
				<button type="button" class="btn btn-primary search-approver"
						id="searchApproverBtn${approverCount}" data-index="${approverCount}">
						검색 <i class="fa-solid fa-magnifying-glass"></i>
				</button>
                <button type="button" class="btn btn-danger remove-approver"
						data-id="${approverCount}" data-index="${approverCount}">
						삭제
				</button>
            </div>
		`
		//	th:field는 JavaScript에서 변경해도 반영되지 않음
		//	approver1, approver2, approver3이 있는 상황에서 결재자2를 지웠을때
		//	approver1, approver3 으로 남아버림
		//	이 문제를 해결하기 위해 th:field대신 name 으로 설정함
		$(".approver-line").append(approverInput);
	});
	
	
	$(document).on("click", "[id^=searchApproverBtn]", function() {  // id가 searchApproverBtn로 시작하는것들 모두 선택
	    let approverIndex = $(this).attr("id").replace("searchApproverBtn", ""); // ID에서 숫자 부분 추출
	    let url = `/groupware/approval/line/${approverIndex}`; 
	    window.open(url, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
	});
	
	$(document).on("click", ".remove-approver", function() {
		$(this).parent().remove();  // 해당 결재자 입력칸 삭제
	    reorderApprovers();         // 삭제 후 번호 재정렬
    });
	
	
	function reorderApprovers() {
		approverCount = 0;
		
	    $(".approver-group").each(function() {
			//	1부터 다시 카운트
	        approverCount++;
			
			//	div의 id 값 변경
			$(this).attr("id", `approver-${approverCount}`);
			
			//	해당 그룹 안의 input 태그 번호 재설정
			let input = $(this).find(".approver-input");
			let inputHidden = $(this).find(".approver-id");
	        input.attr("data-index", approverCount);
	        input.attr("placeholder", "결재자 " + approverCount);
	        inputHidden.attr("name", `approver${approverCount}`);
			
			//	검색 및 삭제 버튼의 data-index 값 변경
			$(this).find(".search-approver").attr("id", `searchApproverBtn${approverCount}`).attr("data-index", approverCount);
	        $(this).find(".remove-approver").attr("data-id", approverCount).attr("data-index", approverCount);
	    });
	}
	//---------------------------------------------------
	
	$(".fileDeleteButton").on("click", function() {
		let idx = $(this).data("id");
		let listItem = $("#file-" + idx);

		if (confirm("삭제하시겠습니까?")) {
			$.ajax({
				url: `/groupware/approval/fileDelete/${idx}`,
				type: "POST",
				success: function(result) {
					alert(result.message);
					listItem.fadeOut(300, function() {
						$(this).remove();
					})
				},
				fail: function(response) {
					alert(response.message);
				}

			});
		}
	});
	
	
	$("#cancelBtn").on("click", function() {
		location.href = "/groupware/approval"
	});
	
	
	
	
}); // DOMContentLoaded 끝

//	결재라인 자식창에서 호출 될 함수
function setApprover(index, id, name) {
	let approverGroup = $(`#approver-${index}`);
	
	approverGroup.find(".approver-id").val(id);
	approverGroup.find(".approver-input").val(name);
	
}





