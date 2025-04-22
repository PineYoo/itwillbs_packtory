document.addEventListener("DOMContentLoaded", function(){
	//---------------------------------------------------
	// 첨부 파일 삭제 버튼 클릭 시
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
	
	// +++++++++++++ 최짐니 ++++++++++++++++++++++++++++++
	// ---------------------------------------------------
	// 페이지 로드 시 확인
	toggleApprovalSection();

	// 변경 이벤트 시 확인
	$('#approvalType').on('change', function() {
	    toggleApprovalSection();
	});
	
	// ---------------------------------------------------
	// 기간별 날짜 검색(datepicker) 함수 활용 - (공통 페이지에 있음)
	// 결재기간 적용
	initDateRangePickerMinToday('draftDate', 'dueDate');
	// 유효기간 적용
	initDateRangePickerMinToday('validFrom', 'validTo');
	// 휴가신청기간 적용
	initDateRangePickerMinToday('eventStartDate', 'eventEndDate');
	
	// ---------------------------------------------------
	// 기안서 등록(submit)
	$('#btnSubmitForm').on("click", function() {
		console.group('click! #btnSubmitForm!');
		// error 내용 삭제
		$("div[id$='Error']").text("")
		
		// 주문서 폼 데이터 수집 (기존 코드)
		const _serializedData = $('#approvalForm').serialize();
		const _pairs = _serializedData.split('&');	// 문자열을 '&' 기준z으로 분리하여 배열로 변환
		const _jsonObj = {};	// 빈 객체를 생성하여 데이터를 담을 준비
		// 각 쌍을 '=' 기준으로 분리하여 JSON 객체로 추가
		_pairs.forEach(pair => {
			const [key, value] = pair.split('=');
			_jsonObj[decodeURIComponent(key)] = decodeURIComponent(value); // URL 디코딩을 적용
		});
	
		// 데이터 확인
		console.log(_jsonObj);
	
		// JSON 데이터를 서버로 전송
		fetch("/groupware/approval/regist", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(_jsonObj)
		})
		.then(response => response.json())
		.then(data => {
			console.log("서버 응답:", data);
			if(data.status === "success") {
				alert(data.message);
				//location.reload(); // 현재 페이지 새로 고침
				location.href="/groupware/approval";
			} else if(data.status === "validFail") {
				console.log("hello validFail!");
				let _errors = data.errors;
				console.log(_errors);
				for (let field in _errors) {
					$("#" + field + "Error").text(_errors[field]); // ex: majorCodeError div에 메시지 출력
				} // end of for (let field in errors) {
			}
		})
		.catch(error => {
			console.error("서버 응답 오류:", error);
			alert("요청 처리 중 문제가 발생했습니다.\n잠시 후 다시 시도해 주시기 바랍니다.");
		});
		
		console.groupEnd();
	}); // end of $('#btnSubmitForm').on("click", function() {
						
	
	
}); // DOMContentLoaded 끝

//	결재라인 자식창에서 호출 될 함수
function setApprover(index, id, name) {
	let approverGroup = $(`#approver-${index}`);
	
	approverGroup.find(".approver-id").val(id);
	approverGroup.find(".approver-input").val(name);
	
}

// -------------------------------------------------------------
// 결재할 레시피 검색 함수(새 창 띄우기) - 단일 선택
function openRecipeSearch() {
	window.open('/recipe/search-popup', 'recipeSearch', 'width=800,height=600,location=no,scrollbars=yes');
	window.isSingleSelect = true; // 하나만 선택할 수 있음
}

// 팝업창에서 가져온 레시피 정보 넣는 함수
function setRecipeInfo(recipe) {
	$("#recipeMasterIdx").val(recipe.idx);     // value 저장용(hidden)
	$("#recipeName").val(recipe.name + '(ver.' + recipe.version + ')');   // 사용자에게 보이는 이름
}		



// 결재라인 검색 함수(새 창 띄우기)
function openEmployeeSearch() {
	window.open('/employee/search-popup', 'approvalEmployeeSearch', 'width=800,height=600,location=no,scrollbars=yes');
	window.isSingleSelect = false;
}

// 팝업창에서 가져온 결재라인 정보 넣는 함수
function setSelectedEmployees(employeeList) {
	// 1. positionCode 기준으로 내림차순 정렬
//	const sortedList = employeeList.sort((a, b) => {
//	    return parseInt(b.positionCode) - parseInt(a.positionCode);
//	});

    // 2. 총 선택된 인원 수 확인
    const total = employeeList.length;

	// 3. 결재자 영역 초기화
	for (let i = 1; i <= 3; i++) {
	    $('#approver' + i).val('');
		$('#approver' + i + 'Name').val('');
	    $('#approver' + i + 'Ifno').val('');
	}
	
    // 4. 인원 수에 따라 위치에 채워넣기
    if (total === 2) {
        // 2명인 경우: 1번과 3번에 넣고 2번은 비움
		$('#approver1').val(employeeList[0].id);
		$('#approver1Name').val(employeeList[0].name);
        $('#approver1Ifno').val(formatApproverInfo(employeeList[0]));

		$('#approver3').val(employeeList[1].id);
		$('#approver3Name').val(employeeList[1].name);
        $('#approver3Ifno').val(formatApproverInfo(employeeList[1]));
    } else {
        // 그 외엔 1, 2, 3 순서대로 채움
        for (let i = 0; i < total && i < 3; i++) {
			$('#approver' + (i + 1)).val(employeeList[i].id);
			$('#approver' + (i + 1) + 'Name').val(employeeList[i].name);
            $('#approver' + (i + 1) + 'Ifno').val(formatApproverInfo(employeeList[i]));
        }
    }
}

// 사원 정보를 문자열로 포맷팅하는 함수
function formatApproverInfo(emp) {
    return emp.name + '(' + emp.departmentName + ', ' + emp.positionName + ')';
}

// 결제유형 선택에 따른 이벤트 함수 - 레시피결재 / 휴가결재
function toggleApprovalSection() {
    var selectedValue = $('#approvalType').val();
    if (selectedValue === '4') {
        $('#vacationApprovalSection').show();
        $('#recipeApprovalSection').hide();
		
		// 레시피결재 정보 지우기
		$("#recipeMasterIdx").val('');
		$("#recipeName").val('');
		
    } else if (selectedValue === '7') {
        $('#vacationApprovalSection').hide();
        $('#recipeApprovalSection').show();
		
		// 휴가결재 정보 지우기
		$("#eventStartDate").val('');
		$("#eventEndDate").val('');
		
    } else {
        $('#vacationApprovalSection').hide();
        $('#recipeApprovalSection').hide();
		
		// 다지워
		$("#eventStartDate").val('');
		$("#eventEndDate").val('');
		$("#recipeMasterIdx").val('');
		$("#recipeName").val('');
    }
}
