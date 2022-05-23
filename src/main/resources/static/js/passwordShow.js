const eyeOpens = document.querySelectorAll(".eye-open")
const eyeCloses = document.querySelectorAll(".eye-close")
const eyeOpenActives = document.querySelectorAll(".eye-open.active")
const eyeCloseActives = document.querySelectorAll(".eye-close.active")

const inputPasswords = document.querySelectorAll(".inputPassword")

inputPasswords.forEach((inputPassword, index) => {
	const eyeOpen = eyeOpens[index]
	const eyeClose = eyeCloses[index]
	const eyeCloseActive = eyeCloseActives[index]

	eyeClose.onclick = () => {
		eyeCloseActive.classList.remove('active')
		inputPassword.setAttribute('type', 'text')
		eyeOpen.classList.add('active')
		console.log(index)
	}

	eyeOpen.onclick = () => {
		inputPassword.setAttribute('type', 'password')
		eyeClose.classList.add('active')
		console.log(index)
	}
})