document.addEventListener("DOMContentLoaded", function(){
//	const modifyForm = document.querySelector("#modifyForm");
	const productReport = $('#approvalList').DataTable({
		lengthChange : true, 	// 건수
		searching : true, 		// 검색
		info : true, 			// 정보
		ordering : true, 		// 정렬
		paging : true,			// 페이징
		responsive: true, 		// 반응형
		destroy: true,
		scrollX: true, 
		autoWidth: false,		
		ajax : {
			url: "ApprovalList",
			type: "POST",
			dataType : "JSON",
			data: function(d) {
                d.status = $('input[name="status"]:checked').val();
                d.searchDate = $("#schDate").val();
                d.searchValue = $('input[name="keyword_search"]').val();
            },
			dataSrc: function (res) {
				console.log(res);
				const data = res.approvalList; // faqList 배열만 가져오기
				console.log("data: "+ JSON.stringify(data));
				
				const start = $('#approvalList').DataTable().page.info().start; 
				
				// 테이블 컬럼 번호(No.) 계산(페이징 포함)
				for (let i = 0; i < data.length; i++) {
					data[i].listIndex = start + i + 1;
					data[i].list_status = data[i].list_status || 1; // 기본값 1로 설정
				}
				return data;
			},
		},
//		dom: '<"top"<"left-length"l><"right-buttons"fB>>rt<"bottom"ip>',
//        buttons: [
//			{
//                extend: 'copy',
//                text: '복사',
//            },
//            {
//                extend: 'excel',
//                text: '엑셀 저장',
//                exportOptions: {
//		            columns: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
//		        },
//            },
//		],
		order: [[4, 'desc']], // 최초 조회시 해당번호 컬럼 최신순으로 기본 설정
		columnDefs: [
			 { targets: [0, 10], orderable: false },
		],
		columns: [
			// defaultContent 는 기본값 설정, 데이터 없는 컬럼일 경우 오류나기 때문에 널스트링 처리 해주어야 함
			// 등록시 유효성 체크를 한다면(null값 없다는 가정하에) defaultContent 값 설정 필요 없음!
            { title: "No.", data: "tableIdx", className : "dt-center", width: '30px', },
//			{ 
//				title: "ID", 
//	            data : "REPORTER_ID", 
//	            defaultContent: "",
//	            width: '120px',
//	            render: function (data, type, row) {
//					if (!data) {
//						return "";
//					}
//                	return `<a href="AdmMemberDetailForm?mem_id=${data}" target="_blank" title="새 창 열기">${data.replace(/(.{16})/g, '$1<br>')}</a>`;
//           		}
//            },
            { 
				title: "요청구분", data:"requestType",	defaultContent: "", width: '100px', className : "dt-center",
				render : function(data, type, row) {
					if(!data) return "";
					switch (data) {
				        case "보낸요청": return "<span class='status status-01'>보낸요청</span>";
				        case "받은요청": return "<span class='status status-02'>받은요청</span>";
				        default: return "";
				    }
				}
			},
			{ 
				title: "결재유형", 
				data : "approvalType", 
				defaultContent: "", 
				className : "dt-center", 
				orderable: true,	// 정렬 활성화
//				searchable: false, // 검색 비활성화
//				width: '150px',
				render : function(data, type, row) {
					const categories = {
						1: "기획",
						2: "발령",
						3: "지출결의",
						4: "휴가",
						5: "휴직",
						6: "기타",
					};
					return `<span class='approval-type' status>${categories[data] || ""}</span>`
				},
             },
//            { 
//				title: "결재유형", data : "approvalType", defaultContent: "",  className : "dt-center",
//					render : function(data, type, row) {
//						if(!data) return "";
//						switch (data) {
//					        case "기획": return "<span class='status status-01'>기획</span>";
//					        case "발령": return "<span class='status status-02'>발령</span>";
//					        case "지출결의": return "<span class='status status-03'>지출결의</span>";
//					        case "휴가": return "<span class='status status-04'>휴가</span>";
//					        case "휴직": return "<span class='status status-05'>휴직</span>";
//					        default: return "";
//					    }
//					}
//            },
            { title: "결재기간", data : "approvalDate", defaultContent: "", className : "dt-center",},
            { title: "제목", data : "title", defaultContent: "", className : "dt-center",
//            	render : function(data, type, row) {
//					return `<a href="ProductDetail?PRODUCT_ID=${row.PRODUCT_ID}" target="_blank" title="새 창 열기">${data}</a>`;
//				}
            },
            { title: "내용", data : "content", defaultContent: "", className : "dt-center",
//            	render : function(data, type, row) {
//					return `<a href="ProductDetail?PRODUCT_ID=${row.PRODUCT_ID}" target="_blank" title="새 창 열기">${data}</a>`;
//				}
            },
            { title: "기안자", data : "draftName", defaultContent: "", className : "dt-center",},
            { title: "결재자", data : "approvalName", defaultContent: "", className : "dt-center",},
            { 
				title: "진행상태", data : "progressStatus", defaultContent: "",  className : "dt-center",
					render : function(data, type, row) {
						if(!data) return "";
						switch (data) {
					        case "진행중": return "<span class='status status-01'>진행중</span>";
					        case "반려": return "<span class='status status-02'>반려</span>";
					        case "결재요청": return "<span class='status status-03'>결재요청</span>";
					        case "결재완료": return "<span class='status status-04'>결재완료</span>";
					        default: return "";
					    }
					}
            },
            {
				title : "기안서 확인",
				data: null,
				searchable: false,
				className : "dt-center",
				width: '120px',
				render : function(data, type, row) {
					return `
						<button class="btn btn-primary detail-btn" data-toggle="modal" data-target="#approvalForm"
								data-approval-id="${data.approvalIdx}"
								aria-label="보기 - approval id: ${data.approvalIdx}" aria-controls="approvalForm">
								보기</button>
					`;
				}
			}
        ],
		serverSide : true, // 서버사이드 처리
		processing : true,  // 서버와 통신 시 응답을 받기 전이라는 ui를 띄울 것인지 여부
		language : {
			emptyTable: "데이터가 없습니다.",
			lengthMenu: "_MENU_ 건씩 보기",
			info: "현재 _START_ - _END_ / 총 _TOTAL_건",
			infoEmpty: "데이터 없음",
			search: "검색",
			infoFiltered: "( _MAX_건의 데이터에서 필터링됨 )",
			loadingRecords: "로딩중...",
			processing: "잠시만 기다려 주세요...",
			zeroRecords: "일치하는 데이터가 없습니다.",
            paginate: {
                first:    '처음',
                previous: '이전',
                next:     '다음',
                last:     '마지막'
            },
        },
	});

	//-----------------------------------------------------
	//기안서 확인 팝업 설정
	$('#approvalForm').on('shown.bs.modal', function () {
		// 모달이 열리면 첫 번째 입력 필드에 포커스
		$(this).find('input, button, textarea').first().focus();
	});

	$('#approvalForm').on('hidden.bs.modal', function () {
		// 모달이 닫히면 이전에 포커스가 있었던 요소로 돌아가게 처리
		$('#previousElement').focus();
		// 모달이 닫힐 때 폼 데이터도 초기화
		$(this).find('form').trigger('reset');
	});

	// FAQ 수정하기 버튼 클릭시 폼제출 처리
	// 1) 수정 버튼 클릭시 모달창 팝업(기존값 가져오기)
	faqList.on("click", '.detail-btn', function() {
		const row = $(this).closest('tr');
		const rowData = faqList.row(row).data();
		
		// 수정 모달 화면에 기존 데이터 보이게 셋팅
		$("#faqId").val(rowData.FAQ_ID); 					// 문의아이디(히든속성값)
		$("#updatedFaqSubject").val(rowData.FAQ_SUBJECT);	// 문의제목
		$("#updatedFaqContent").val(rowData.FAQ_CONTENT);	// 문의내용
		$("#updatedFaqCate").val(rowData.FAQ_CATE);			// 문의유형
		$("#updatedListStatus").val(rowData.LIST_STATUS);	// 사용여부 값
		
		const listStatus = rowData.LIST_STATUS == 1 ? true : false;
		const listStatusText = rowData.LIST_STATUS == 1 ? "사용함" : "사용안함";
		$("#updateFlexSwitchCheckDefault").prop("checked", listStatus); // 사용여부 버튼
		$("#updateFlexSwitchCheckDefaultLab").text(listStatusText); 	// 사용여부 텍스트
		
		const contentLength = rowData.FAQ_CONTENT ? rowData.FAQ_CONTENT.length : 0;
		$("#lengthInfo").text(contentLength); 	// 글자수 표시
	});

	// 2) 폼 제출전 변경된 사용여부 상태값 저장하기
	$(document).on("change", "#updateFlexSwitchCheckDefault", function() {
		const isChecked = $(this).is(':checked');  // 체크 여부
		const statusValue = isChecked ? 1 : 2;  // 상태 값 (1: 사용, 2: 사용안함)
		
		// 업데이트된 상태값을 hidden 필드에 저장
		$("#updatedListStatus").val(statusValue);  // hidden 필드에 상태값 저장
		
	});

	// 3) 수정 완료 버튼 클릭 시 폼 제출
	$('#updateFaqSubmitBtn').on('click', function(e) {
		e.preventDefault(); // 폼 기본 제출 방지
		
		const isChecked = $("#updateFlexSwitchCheckDefault").is(':checked');
		$("#updatedListStatus").val(isChecked ? 1 : 2);
		
		// 폼제출
		$("#modifyForm").submit();
	});


	// 내용 작성
	$("#updatedFaqContent").on('keyup', () => {
		fnChkByte($("#updatedFaqContent"), 500);
	});

	// 글자수 제한 함수
	function fnChkByte(item, maxLength){
		const str = item.val();
	    
	     if (str.length > maxLength) {
	        alert("글자수는 " + maxLength + "자를 초과할 수 없습니다.");
	        item.val(str.substring(0, maxLength)); //문자열 자르고 값 넣기
	     }
	     $('#lengthInfo').text(item.val().length);
	}
	
	
	
	// 기안서 상세보기 팝업 셋팅
//	productReport.on("click", '.detail-btn', function() {
//	productReport.on("click", '.edit-btn', function() {
//		const row = $(this).closest('tr');
//		const rowData = productReport.row(row).data();
//
//		const status = rowData.STATUS;
//		const actionReason = rowData.ACTION_REASON != null ? rowData.ACTION_REASON : "";
//		const productId = document.querySelector("#productId");
//		const reportId = document.querySelector("#reportId");
//		const statusSelect = document.querySelector(`#reportStatus option[value="${status}"]`);
//		const reasonTextarea = document.querySelector("#actionReason");
//		
//		reportId.value = rowData.REPORT_ID;
//		productId.value = rowData.PRODUCT_ID;
//		reasonTextarea.value = actionReason;
//	    if (statusSelect) statusSelect.selected = true;
//	    
//	});
	
	// 기존 검색 숨기기
	$("#productReport_filter").attr("hidden", "hidden");
	
	 // 필터 변경 시 데이터 테이블 다시 로드
    $('input[name="status"]').on('change', () => productReport.draw());

    // 검색 버튼 클릭 시 테이블 다시 로드
    $('#searchBtn').on('click', () => productReport.draw());
    
    // 엔터키 입력으로 검색
    $('#searchKeyword').on('keypress', (e) => {
        if (e.which == 13)  productReport.draw();
    });
    
	// 기간별 검색 필터링 제이쿼리
    $('#schDate').daterangepicker({
//        startDate: moment().subtract(29, 'days'),
//        startDate: false,
//        endDate: moment(),
        maxDate: moment(),
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
            '오늘': [moment()], // 오늘
            '이번 달': [moment().startOf('month'), moment().endOf('month')], // 이번 달 전체
            '지난 달': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')], // 지난 달 전체
            '지난 7일': [moment().subtract(6, 'days'), moment()], // 최근 7일
            '지난 14일': [moment().subtract(13, 'days'), moment()], // 최근 14일
            '지난 30일': [moment().subtract(29, 'days'), moment()], // 최근 30일
            '지난 3개월': [moment().subtract(3, 'months').startOf('month'), moment().endOf('month')], // 최근 3개월
        },
    }).on('show.daterangepicker', function (ev, picker) {
        picker.container.addClass('goodbuyCustomPicker');                            
    });
    
    // 날짜 선택 후에도 placeholder 유지
    $('#schDate').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(`${picker.startDate.format('YYYY-MM-DD')} ~ ${picker.endDate.format('YYYY-MM-DD')}`);
    });
    
	// 날짜 검색 초기화
	$("#initDateBtn").on('click', function() {
		$('#schDate').val('');
		productReport.draw();
	});
	
    // 기간별 검색 후 테이블 다시 로드
    $("#searchDateBtn").on('click', function() {
		productReport.draw();
	});
	
	// 조치 사유 작성
	$("#actionReason").on('keydown', () => {
		fnChkByte($("#actionReason"), 500);
	});

});

	
// 글자수 제한 함수
function fnChkByte(item, maxLength){
	const str = item.val();
    const strLength = str.length;
    
     if (strLength > maxLength) {
        alert("글자수는 " + maxLength + "자를 초과할 수 없습니다.");
        $(item).val(str.substr(0, maxLength));      //문자열 자르고 값 넣기
        fnChkByte(item, maxLength);
     }
     $('#lengthInfo').text(strLength);
}