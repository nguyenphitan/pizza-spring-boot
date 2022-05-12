/**
 * Quản lý lương nhân viên
 * Created by: NPTAN (05/05/2022)
 * Version: 1.0
 */
 
 $(document).ready(function() {
	// Click đánh dấu đã trả lương:
	$('.payment_payroll').click(paymentSalary.bind(this));
})


/*
	Xử lý trả lương nhân viên
	Created by: NPTAN (05/05/2022)
	Version: 1.0
*/
function paymentSalary(e) {
	// 1. Lấy ra id nhân viên:
	let sellerId = Number( $(e.target).prev().text() );
	
	// 2. Gọi API chấm công -> xóa các ngày chấm công:
	$.ajax({
        type: "DELETE",
        url: `http://localhost:8088/admin/timekeeping/${sellerId}`,
        success: function (response) {
            alert("Thành công.");
            window.location.reload();
        },
        error: function(reject) {
			alert('Không thành công.')
		}
    });
	
}

















