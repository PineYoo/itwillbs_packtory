$(document).ready(function() {
	//	url에서 index 값 가져오기
	let approverIndex = getIndex();
	
	$(document).on("click", "#approverList .btn-success", function() {
		let drafterId = $(this).closest("tr").data("id");	//	결재자 ID
		let name = $(this).closest("tr").find("td:eq(0)").text();	// 결재자 이름
		
		if(window.opener) {
			window.opener.setApprover(approverIndex, drafterId, name);
			window.close();
		}
	});
	//	-------------------------------------------------------------
	//	url에서 / 맨뒤에 있는 인덱스 리턴 메서드
	function getIndex() {
		let urlParts = window.location.pathname.split("/");
		return urlParts[urlParts.length - 1];
	}
	
	$(document).on("input", "#searchKeyword", function() {
		let keyword = $(this).val().trim();	//	입력값 가져오기
		
		$.ajax({
			url: "/groupware/approval/line/search",
			type: "POST",
			data: { keyword : keyword },
			success: function(data) {
				updateList(data);
			},
			error: function() {
				console.error("검색 중 오류 발생");
			}
		});
	});
	
	function updateList(data) {
		let tbody = $("#approverList");
		tbody.empty();
		
		if(data.length === 0) {
			tbody.append(`<tr><td colspan="4" class="text-conter>검색결과가 없습니다.</td></tr>"`)
			return;
		}
		
		data.forEach(item => {
			let row = `
				<tr data-id="${item.drafterId}">
					<td>${item.name}</td>
					<td>${item.departmentName}</td>
					<td>${item.positionName}</td>
					<td>
						<button class="btn btn-success">선택</button>
					</td>
				</tr>
			`;
			tbody.append(row);
		});
			
		
	}
	
	
})



