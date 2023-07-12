$(document).ready(function() {
	onSignIn();
})

function onSignIn(googleUser) {
	let list = $(".abcRioButtonContents").children();
	console.log( $(list[0]).text() );
  //$("#not_signed_inx04exjduth34").text("Đăng nhập với Google");
  
	
  let profile = googleUser.getBasicProfile();
  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
  console.log('Name: ' + profile.getName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
  
  let idToken = googleUser.getAuthResponse().id_token;
  $.ajax({
        type: "POST",
        url: `http://localhost:8088/auth/verify/${idToken}`,
        success: function (response) {
			alert('Thành công.');
			window.location.reload();
        },
        error: function(reject) {
			alert('Không thành công.')
		}
  });
}

