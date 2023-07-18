$(document).ready(function() {
	$(".footer__block-mail-send").click(sendMailApply.bind(this));
})

function sendMailApply() {
	let mailRequest = {};
	
	let fullName = $($('input#fullName')[0]).val();
	let emailAddress = $($('input#email')[0]).val();
	let mailTitle = $($('input#mailTitle')[0]).val();
	let phoneNumber = $($('input#phoneNumber')[0]).val();
	let fileAttachment = $('input#mailAttachment')[0].files[0];
	
	let formData = new FormData();
	formData.append('file', fileAttachment);
	
	mailRequest.fullName = fullName;
	mailRequest.emailAddress = emailAddress
	mailRequest.mailTitle = mailTitle
	mailRequest.phoneNumber = phoneNumber
	mailRequest.fileAttachment = fileAttachment
	
	/*console.log(fullName);
	console.log(emailAddress);
	console.log(mailTitle);
	console.log(phoneNumber);
	console.log(formData);*/
	
	console.log(mailRequest);
	
	$.ajax({
        type: "POST",
        url: `http://localhost:8088/api/v1/mail/mail_attachment`,
        // async: false,
        data: JSON.stringify(mailRequest),
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
			console.log("OK!");
			// window.location.reload();
        },
        error: function(reject) {
			alert('Không thành công.')
		}
    });
}

