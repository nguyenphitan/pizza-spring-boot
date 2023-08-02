$(document).ready(function() {
	var filePath = "";
	let inputFile = $('#mailAttachment');
	inputFile.change(() => {
		filePath = uploadFile();
	});
	
	$(".footer__block-mail-send").click(() => {
		sendMailApply(filePath);
	});
})

function uploadFile() {
	let path = "";
	let fileAttachment = $('input#mailAttachment')[0].files[0];
	
	let formData = new FormData();
	formData.append('file', fileAttachment);
	
	$.ajax({
        type: "POST",
        url: `http://localhost:8088/api/v1/file/upload`,
        async: false,
        data: formData,
        contentType: false,
    	processData: false,
        success: function (response) {
			console.log("OK!");
			path = response;
        },
        error: function(reject) {
			alert('Không thành công.');
		}
    });
    
    return path;
}

function sendMailApply(filePath) {
	let mailRequest = {};
	
	let fullName = $($('input#fullName')[0]).val();
	let emailAddress = $($('input#emailAddress')[0]).val();
	let mailTitle = $($('input#mailTitle')[0]).val();
	let phoneNumber = $($('input#phoneNumber')[0]).val();
	// let fileAttachment = $('input#mailAttachment')[0].files[0];
	
	// let formData = new FormData();
	// formData.append('file', fileAttachment);
	
	mailRequest.fullName = fullName;
	mailRequest.emailAddress = emailAddress;
	mailRequest.mailTitle = mailTitle;
	mailRequest.phoneNumber = phoneNumber;
	mailRequest.pathAttachmentFile = filePath;
	
	$.ajax({
        type: "POST",
        url: 'http://localhost:8088/api/v1/mail/mail_attachment',
        // async: false,
        data: JSON.stringify(mailRequest),
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
			alert('Ứng tuyển thành công.');
			window.location.reload();
        },
        error: function(reject) {
			alert('Không thành công.');
		}
    });
}

