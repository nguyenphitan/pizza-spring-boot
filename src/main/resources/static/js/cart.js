/**
 * Xử lý giỏ hàng
 * Created by: NPTAN (13/05/2022)
 * Version: 1.0
 */
 
$(document).ready(function() {
	// Xử lý nghiệp vụ khi ấn thanh toán:
	$('.product-btn__pay').click(handlePayement.bind(this));
	
	// Add to cart
	$('.add-to-cart').click(addToCart.bind(this));
	
	// Delete from cart
	$('.product-cart__delete').click(deleteProductOutCart.bind(this));
	
	// Dec product
	$('.product-cart__btn_left').click(decProduct.bind(this));
	
	// Inc product
	$('.product-cart__btn_right').click(incProduct.bind(this));
})


/*
	Hàm xử lý thanh toán
	Created by: NPTAN (13/05/2022)
	Version: 1.0
*/
function handlePayement(e) {
	// 1. Lấy ra phương thức thanh toán:
	let payMethod = $('input[name="paymethod"]:checked').attr('id');
	
	// 2. Lấy ra số tiền thành toán:
	let amount = Number($('.t-total-price').text());
	
	// Kiếm tra phương thức thanh toán:
	if(payMethod == 'pay-offline') {
		window.location.href = `http://localhost:8088/cart/check-out`;
	} else {
		window.location.href = `http://localhost:8088/cart/payment/${amount}`;
	}
	
}

/**
	Add to product to cart
*/
function addToCart(e) {
	console.log(e.target);
	let productId = Number($(e.target).prev().text());
	
	$.ajax({
        type: "POST",
        url: `http://localhost:8088/cart/addToCart/${productId}`,
        success: function (response) {
			console.log("OK!");
			window.location.reload();
        },
        error: function(reject) {
			alert('Thêm sản phẩm không thành công.')
		}
    });
}

/**
	Delete product
*/
function deleteProductOutCart(e) {
	let productId = Number($(e.target).prev().text());
	
	$.ajax({
        type: "DELETE",
        url: `http://localhost:8088/cart/removeProductCart/${productId}`,
        success: function (response) {
			console.log("OK!");
			window.location.reload();
        },
        error: function(reject) {
			alert('Không thành công.')
		}
    });
}

/**
	Dec product
*/
function decProduct(e) {
	let productId = Number($(e.target).prev().text());
	
	$.ajax({
        type: "POST",
        url: `http://localhost:8088/cart/quantity-dec/${productId}`,
        success: function (response) {
			console.log("OK!");
			window.location.reload();
        },
        error: function(reject) {
			alert('Không thành công.')
		}
    });
}

/**
	Inc product
*/
function incProduct(e) {
	let productId = Number($(e.target).prev().text());
	
	$.ajax({
        type: "POST",
        url: `http://localhost:8088/cart/quantity-inc/${productId}`,
        success: function (response) {
			console.log("OK!");
			window.location.reload();
        },
        error: function(reject) {
			alert('Không thành công.')
		}
    });
}

