/**
 * Quản lý lương nhân viên
 * Created by: NPTAN (05/05/2022)
 * Version: 1.0
 */
 
 $(document).ready(function() {
	// Click tính lương:
	$('.handle_payroll').click(handlePayroll.bind(this));
	
	// Click trả lương:
	$('.payment_payroll').click(paymentSalary.bind(this));
})


/*
	Xử lý tính lương
	Created by: NPTAN (05/05/2022)
	Version: 1.0
*/
function handlePayroll(e) {
	// 1. Lấy ra tổng số ngày làm việc:
	let totalDate = Number( $(e.target).next().text() );
	
	// 2. Tính lương: 40.000 * 8 * tổng số ngày làm
	// 40.000: 40k/h
	// 8: một ngày làm 8h
	let salary = 40000 * 8 * totalDate;
	
	// Hiển thị lương:
	$(e.target).parents('td').siblings('.t-salary').html(`<span style="font-size: 2.2rem;"> <strong>Lương:</strong> <strong style="color: red;">40000 x 8 x ${totalDate} = ${salary}</strong>  </span>`);
}


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

















