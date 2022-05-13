/**
 * Xử lý thanh toán ví điện tử
 * Created by: NPTAN (25/04/2022)
 * Version: 1.0
 */

$(document).ready(function() {
	// Format lại số tiền:
	let amount = Math.floor( Number($('#t-amount-clone').text()) / 100 );
 	$('#t-amount').text(amount);  	 
	 
	// Nếu responseCode = '00' -> giao dịch thành công -> tạo hóa đơn -> xóa sản phẩm khỏi giỏ hàng:
	let responseCode = $('.responseCode').text();
	if( responseCode === '00' ) {
		// 1. Lấy ra user id:
		let userId = Number($('.t-user-id').text());
		// 2. Lấy ra danh sách id sản phẩm, số lượng đã chọn:
		let productIds = $('.t-product-id')		// ID sản phẩm
		let quantitySelecteds = $('.t-quantitySelected');	// Số lượng đã chọn
		
		// Buil object:
		// 1. Build List ProductRequest:
		let productRequests = [];
		for(let i = 0 ; i < productIds.length ; i++) {
			let proRequest = {};
			proRequest.idProduct = Number( $(productIds[i]).text() );
			proRequest.quantitySelected = Number( $(quantitySelecteds[i]).text() );
			productRequests.push(proRequest);
		}
		
		// 2. Build BillRequest:
		let billRequest = {};
		billRequest.userId = userId;
		billRequest.productRequests = productRequests;
		
	}
	
	// Click quay về trang chủ:
   	$('.t-return-home').click(function () {
   		window.location.href = "http://localhost:8088/cart/check-out";
	});
	
})


     