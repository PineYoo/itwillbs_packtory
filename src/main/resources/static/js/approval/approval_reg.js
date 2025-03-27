document.addEventListener("DOMContentLoaded", function(){
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


}); // DOMContentLoaded 끝
