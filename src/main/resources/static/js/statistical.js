/*
	Thống kê doanh thu
	Created by: NPTAN (02/5/2022)
	Version: 1.0
*/
$(document).ready(function() {
	// Click chọn tháng xem thống kê:
	$('.t-select-month').change(loadStatistical.bind(this));
	
	
})


/*
	Load doanh thu theo tháng được chọn:
	Created by: NPTAN (02/05/2022)
	Version: 1.0
*/
function loadStatistical(e) {
	// Lấy ra tháng được chọn:
	let month = $(e.target).val();
	console.log($(e.target).val());
	
	// Gọi API lấy dữ liệu:
	$.ajax({
        type: "POST",
        url: `http://localhost:8088/admin/statistical/${month}`,
        // async: false,
        data: JSON.stringify(month),
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
			console.log(response);
        	window.location.reload();
        },
        error: function(reject) {
            alert("Không thành công.");
            console.log(reject);
        }
    });	
	
}

