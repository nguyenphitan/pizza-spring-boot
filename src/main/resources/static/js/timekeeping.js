/**
 * Chấm công nhân viên
 * Created by: NPTAN (05/05/2022)
 * Version: 1.0
 */
 
 $(document).ready(function() {
	
	// Admin click vào nút chấm công:
	$('.click_timekeeping').click(timekeeping.bind(this));
	
})


/*
	Hàm xử lí chấm công nhân viên
	Created by: NPTAN (05/05/2022)
	Version: 1.0
*/
function timekeeping(e) {
	// 1. Lấy ra id nhân viên.
	let sellerId = Number($(e.target).prev().text());
	
	// 2. Lấy ra ngày chấm công.
	let date = new Date();
	
	// 3. Build thành object.
	let timekeeping = {};
	timekeeping.accountId = sellerId;
	timekeeping.date = date;
	
	// 4. Gọi API cất dữ liệu:
	$.ajax({
        type: "POST",
        url: 'http://localhost:8088/admin/timekeeping',
        // async: false,
        data: JSON.stringify(timekeeping),
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
			console.log(response);
			alert("Chấm công thành công.");
        	window.location.reload();
        },
        error: function(reject) {
            alert("Không thành công.");
            console.log(reject);
        }
    });	
	
	
	
}
