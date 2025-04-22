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
	
	// 돌아가기 버튼(리스트로 이동)
	$("#cancelBtn").on("click", function() {
		location.href = "/groupware/approval"
	});
	
	// ---------------------------------------------------
	// +++++++++++++ 최짐니 ++++++++++++++++++++++++++++++
	// 결제유형 - 레시피결재 클릭 시 이벤트
	$('#approvalType').on('change', function() {
        var selectedValue = $(this).val();
        if (selectedValue === '7') {	// 레시피결재 일 경우
            console.log("레시피결재 선택됨");
			
            $('#recipeApprovalSection').show(); // 섹션 보이기
        } else {
            $('#recipeApprovalSection').hide();
        }
    });
	
	// 기간별 날짜 검색(datepicker) 함수 활용 - (공통 페이지에 있음)
	// 유효기간 적용
	initDateRangePickerMinToday('validFrom', 'validTo');
	
	
}); // DOMContentLoaded 끝

//	결재라인 자식창에서 호출 될 함수
function setApprover(index, id, name) {
	let approverGroup = $(`#approver-${index}`);
	
	approverGroup.find(".approver-id").val(id);
	approverGroup.find(".approver-input").val(name);
	
}

// -------------------------------------------------------------
// 상품 검색 함수(새 창 띄우기) - 단일 선택
function openProductSearch() {
	window.open('/product/search-popup', 'productSearch', 'width=800,height=600,location=no,scrollbars=yes');
	window.isSingleSelect = true; // 상품 하나만 선택할 수 있음
}

// 팝업창에서 가져온 상품 정보 넣는 함수
function setProductInfo(product) {
	$("#productIdx").val(product.idx);     // value 저장용
	$("#productName").val(product.name);   // 사용자에게 보이는 이름
}		



// 결재라인 검색 함수(새 창 띄우기)
function openEmployeeSearch() {
	window.open('/employee/search-popup', 'approvalEmployeeSearch', 'width=800,height=600,location=no,scrollbars=yes');
	window.isSingleSelect = false;
}

// 팝업창에서 가져온 결재라인 정보 넣는 함수
function setSelectedEmployees(employeeList) {
	// 1. positionCode 기준으로 내림차순 정렬
	const sortedList = employeeList.sort((a, b) => {
	    return parseInt(b.positionCode) - parseInt(a.positionCode);
	});

    // 2. 총 선택된 인원 수 확인
    const total = sortedList.length;

    // 3. 결재자 영역 초기화
    $('#approver1').val('');
    $('#approver2').val('');
    $('#approver3').val('');

    // 4. 인원 수에 따라 위치에 채워넣기
    if (total === 2) {
        // 2명인 경우: 1번과 3번에 넣고 2번은 비움
        $('#approver1').val(formatApproverInfo(sortedList[0]));
        $('#approver3').val(formatApproverInfo(sortedList[1]));
    } else {
        // 그 외엔 1, 2, 3 순서대로 채움
        for (let i = 0; i < total && i < 3; i++) {
            $('#approver' + (i + 1)).val(formatApproverInfo(sortedList[i]));
        }
    }
}

// 사원 정보를 문자열로 포맷팅하는 함수
function formatApproverInfo(emp) {
    return emp.name + '(' + emp.departmentName + ', ' + emp.positionName + ')';
}
