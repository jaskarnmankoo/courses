// LOGIN CODE
function login() {
	if (setLoginErrors("client", 0)) {
		$.ajax({
			method: "GET",
			url:
				"/api/login/user/" +
				$("#username").val() +
				"/pass/" +
				$("#password").val(),
		})
			.done(function (data) {
				if (data.login) {
					console.log("THE TOKEN IS: " + data.token)
					if (data.token) {
						sessionStorage.setItem("jwt", JSON.stringify(data))
						sessionStorage.setItem("globalUsername", $("#username").val())
						sessionStorage.setItem("globalPassword", $("#password").val())
						loginToProfile()
					}
				}
				console.log(JSON.stringify(data))
			})
			.fail(function (err) {
				if (err.status == 400) {
					setLoginErrors("server", 400)
				} else if (err.status == 404) {
					setLoginErrors("server", 404)
				}
				console.log(err.status)
				console.log(JSON.stringify(err.responseJSON))
			})
	}
}

function loginToProfile() {
	$("#ui_login").hide()
	$("#ui_profile").show()
	retrieveUserInfo()
	unsetLoginErrors()
	sessionStorage.setItem("currentScreen", "profile")
}

function loginToRegistration() {
	$("#ui_login").hide()
	$("#ui_register").show()
	unsetLoginErrors()
	if (sessionStorage.getItem("currentScreen")) {
		sessionStorage.removeItem("currentScreen")
	}
}

function setLoginErrors(type, errorCode) {
	if (type == "client") {
		if ($("#username").val() == "" && $("#password").val() == "") {
			$("#errorMessage").html("<br>Please specify a username and password!")
			$("#username").css("borderColor", "#FF0000")
			$("#password").css("borderColor", "#FF0000")
			return false
		}

		if ($("#username").val() == "") {
			$("#errorMessage").html("<br>Please specify a username!")
			$("#username").css("borderColor", "#FF0000")
			$("#password").css("borderColor", "")
			return false
		}

		if ($("#password").val() == "") {
			$("#errorMessage").html("<br>Please specify a password!")
			$("#username").css("borderColor", "")
			$("#password").css("borderColor", "#FF0000")
			return false
		}
	} else {
		if (errorCode == 400) {
			$("#errorMessage").html("<br>Your password is incorrect!")
			$("#username").css("borderColor", "")
			$("#password").css("borderColor", "#FF0000")
			return false
		} else if (errorCode == 404) {
			$("#errorMessage").html(
				"<br>The specified user does not exist! Please register!"
			)
			$("#username").css("borderColor", "#FF0000")
			$("#password").css("borderColor", "")
			return false
		}
	}
	return true
}

function unsetLoginErrors() {
	$("#username").css("borderColor", "")
	$("#password").css("borderColor", "")
	$("#username").val("")
	$("#password").val("")
	$("#errorMessage").html("")
}
// LOGIN CODE ENDS

// REGISTRATION CODE
function register() {
	if (setRegistrationErrors("client")) {
		if (setRegistrationErrors("special")) {
			var lecture = setRadioButtons("register")
			$.ajax({
				method: "POST",
				url: "/api/register/",
				data: {
					user: $("#usernameR").val(),
					pass: $("#passwordR").val(),
					email: $("#emailR").val(),
					birth: $("#birthdayR").val(),
					favColor: $("#favColorR").val(),
					year: $("#yearR").val(),
					lecture: lecture,
				},
			})
				.done(function (data) {
					if (data.register) {
						sessionStorage.setItem("globalUsername", $("#usernameR").val())
						sessionStorage.setItem("globalPassword", $("#passwordR").val())
						registrationToProfile()
					}
				})
				.fail(function (err) {
					console.log(err.status)
					console.log(JSON.stringify(err.responseJSON))
					setRegistrationErrors("server")
				})
		}
	}
}

function registrationToProfile() {
	$("#ui_register").hide()
	$("#ui_profile").show()
	retrieveUserInfo()
	unsetRegistrationErrors()
	sessionStorage.setItem("currentScreen", "profile")
}

function registrationToLogin() {
	$("#ui_register").hide()
	$("#ui_login").show()
	unsetRegistrationErrors()
	if (sessionStorage.getItem("currentScreen")) {
		sessionStorage.removeItem("currentScreen")
	}
}

function setRegistrationErrors(type) {
	if (type == "client") {
		if (
			$("#usernameR").val() == "" &&
			$("#passwordR").val() == "" &&
			$("#confirmPR").val() == ""
		) {
			$("#errorMessage").html("<br>Please fill out all essential fields!")
			$("#usernameR").css("borderColor", "#FF0000")
			$("#passwordR").css("borderColor", "#FF0000")
			$("#confirmPR").css("borderColor", "#FF0000")
			return false
		}

		if ($("#usernameR").val() == "" && $("#passwordR").val() == "") {
			$("#errorMessage").html("<br>Please fill out username and password!")
			$("#usernameR").css("borderColor", "#FF0000")
			$("#passwordR").css("borderColor", "#FF0000")
			$("#confirmPR").css("borderColor", "")
			return false
		}

		if ($("#usernameR").val() == "" && $("#confirmPR").val() == "") {
			$("#errorMessage").html(
				"<br>Please fill out username and confirm password!"
			)
			$("#usernameR").css("borderColor", "#FF0000")
			$("#passwordR").css("borderColor", "")
			$("#confirmPR").css("borderColor", "#FF0000")
			return false
		}

		if ($("#passwordR").val() == "" && $("#confirmPR").val() == "") {
			$("#errorMessage").html(
				"<br>Please fill out password and confirm password!"
			)
			$("#usernameR").css("borderColor", "")
			$("#passwordR").css("borderColor", "#FF0000")
			$("#confirmPR").css("borderColor", "#FF0000")
			return false
		}

		if ($("#usernameR").val() == "") {
			$("#errorMessage").html("<br>Please fill out username!")
			$("#usernameR").css("borderColor", "#FF0000")
			$("#passwordR").css("borderColor", "")
			$("#confirmPR").css("borderColor", "")
			return false
		}

		if ($("#passwordR").val() == "") {
			$("#errorMessage").html("<br>Please fill out password!")
			$("#usernameR").css("borderColor", "")
			$("#passwordR").css("borderColor", "#FF0000")
			$("#confirmPR").css("borderColor", "")
			return false
		}

		if ($("#confirmPR").val() == "") {
			$("#errorMessage").html("<br>Please fill out confirm password!")
			$("#usernameR").css("borderColor", "")
			$("#passwordR").css("borderColor", "")
			$("#confirmPR").css("borderColor", "#FF0000")
			return false
		}
	} else if (type == "special") {
		if ($("#passwordR").val() != $("#confirmPR").val()) {
			$("#errorMessage").html("<br>The passwords do not match!")
			$("#usernameR").css("borderColor", "")
			$("#passwordR").css("borderColor", "#FF0000")
			$("#confirmPR").css("borderColor", "#FF0000")
			return false
		}
	} else {
		$("#errorMessage").html("<br>The specified user already exists!")
		$("#usernameR").css("borderColor", "#FF0000")
		$("#passwordR").css("borderColor", "")
		$("#confirmPR").css("borderColor", "")
		return false
	}
	return true
}

function unsetRegistrationErrors() {
	$("#usernameR").css("borderColor", "")
	$("#passwordR").css("borderColor", "")
	$("#confirmPR").css("borderColor", "")
	$("#usernameR").val("")
	$("#passwordR").val("")
	$("#confirmPR").val("")
	$("#errorMessage").html("")
}
// REGISTRATION CODE ENDS

// PROFILE CODE
function retrieveUserInfo() {
	$.ajax({
		method: "GET",
		url:
			"/api/information/user/" +
			sessionStorage.getItem("globalUsername") +
			"/password/" +
			sessionStorage.getItem("globalPassword"),
	}).done(function (data) {
		if (data.information && data.global) {
			$("#nameUP").html("Username: " + data.username)
			$("#emailUP").html("Email: " + data.email)
			$("#birthdayUP").html("Birthday: " + data.birthday)
			$("#favColorUP").html("Favourite Color: " + data.favColor)
			$("#yearUP").html("Year: " + data.year)
			$("#lecUP").html("Lecture: " + data.lecture)
			retrieveTopTen()
		} else {
			$("#errorMessage").html(
				"This request was not made by the signed in user!"
			)
		}
	})
}

function retrieveTopTen() {
	$.ajax({
		method: "GET",
		url: "/api/topten",
	}).done(function (data) {
		var highScores = ""
		for (i = 0; i < data["scores"].length; i++) {
			highScores +=
				data["scores"][i].username +
				" : " +
				data["scores"][i].score +
				"<br/><br/>"
		}
		$("#leaderboard").html(highScores)
	})
}

function profileToLogin() {
	$("#ui_profile").hide()
	$("#ui_login").show()
	if (sessionStorage.getItem("currentScreen")) {
		sessionStorage.removeItem("currentScreen")
	}
}

function profileToDelete() {
	$("#ui_profile").hide()
	$("#ui_delete").show()
	sessionStorage.setItem("currentScreen", "delete")
}

function profileToUpdate() {
	$("#ui_profile").hide()
	$("#ui_update").show()
	sessionStorage.setItem("currentScreen", "update")
}

function profileToGame() {
	$("#ui_profile").hide()
	$("#ui_game").show()
	sessionStorage.setItem("currentScreen", "game")

	if (gamePaused) {
		resumeGame()
	} else {
		setupGame()
		startGame()
	}
}
// PROFILE CODE ENDS

// DELETE CODE
function deleteAccount() {
	$.ajax({
		method: "DELETE",
		url: "/api/deleteAccount/user/" + sessionStorage.getItem("globalUsername"),
	}).done(function (data) {
		if (data.delete) {
			deleteToLogin()
		}
	})
}

function deleteToLogin() {
	$("#ui_delete").hide()
	$("#ui_login").show()
	if (sessionStorage.getItem("currentScreen")) {
		sessionStorage.removeItem("currentScreen")
	}
}

function deleteToProfile() {
	$("#ui_delete").hide()
	$("#ui_profile").show()
	unsetDeletionErrors()
	retrieveUserInfo()
	sessionStorage.setItem("currentScreen", "profile")
}

function unsetDeletionErrors() {
	$("#passwordD").css("borderColor", "")
	$("#passwordD").val("")
	$("#errorMessage").html("")
}
// DELETE CODE ENDS

// UPDATE CODE
function updateProfile() {
	if (setUpdateErrors()) {
		var values = setUpdateValues()
		$.ajax({
			method: "PUT",
			url: "/api/update/user/" + sessionStorage.getItem("globalUsername"),
			data: {
				pass: values["newPass"],
				email: values["newEmail"],
				birthday: values["newBirthday"],
				favColor: values["newFavColor"],
				year: values["newYear"],
				lecture: values["newLecture"],
			},
		})
			.done(function (data, text_status, jqXHR) {
				console.log(JSON.stringify(data))
				console.log(text_status)
				console.log(jqXHR.status)
				unsetUpdateErrors()
				updateToProfile()
			})
			.fail(function (err) {
				console.log(err.status)
				console.log(JSON.stringify(err.responseJSON))
			})
	}
}

function setUpdateValues() {
	var values = {}

	if ($("#passwordU").val() == "") {
		values["newPass"] = sessionStorage.getItem("globalPassword")
	} else {
		values["newPass"] = $("#passwordU").val()
		sessionStorage.setItem("globalPassword", values["newPass"])
	}

	if ($("#emailU").val() == "") {
		values["newEmail"] = $("#emailUP").html().slice(7)
	} else {
		values["newEmail"] = $("#emailU").val()
	}

	if ($("#birthdayU").val() == $("#birthdayUP").html().slice(10)) {
		values["newBirthday"] = $("#birthdayUP").html().slice(10)
	} else {
		values["newBirthday"] = $("#birthdayU").val()
	}

	if ($("#favcolorU").val() == $("#favColorUP").html().slice(17)) {
		values["newFavColor"] = $("#favColorUP").html().slice(17)
	} else {
		values["newFavColor"] = $("#favColorU").val()
	}

	if ($("#yearU").val() == $("#yearUP").html().slice(6)) {
		values["newYear"] = $("#yearUP").html().slice(6)
	} else {
		values["newYear"] = $("#yearU").val()
	}

	values["newLecture"] = setRadioButtons("profile")
	return values
}

function setUpdateErrors() {
	if ($("#passwordU").val() != $("#confirmU").val()) {
		$("#errorMessage").html("<br>The passwords do not match!")
		$("#passwordU").css("borderColor", "#FF0000")
		$("#confirmU").css("borderColor", "#FF0000")
		return false
	}
	return true
}

function unsetUpdateErrors() {
	$("#passwordU").css("borderColor", "")
	$("#confirmU").css("borderColor", "")
	$("#confirmO").css("borderColor", "")
	$("#passwordU").val("")
	$("#confirmU").val("")
	$("#confirmO").val("")
	$("#emailU").val("")
	$("#errorMessage").html("")
}

function updateToProfile() {
	$("#ui_update").hide()
	$("#ui_profile").show()
	unsetUpdateErrors()
	retrieveUserInfo()
	sessionStorage.setItem("currentScreen", "profile")
}
// UPDATE CODE ENDS

// GAME CODE
function gameToProfile() {
	pauseGame()
	$("#ui_game").hide()
	$("#ui_profile").show()
	retrieveUserInfo()
	sessionStorage.setItem("currentScreen", "profile")
}

function endGameToProfile() {
	$("#ui_game").hide()
	$("#ui_profile").show()
	updateScore()
	retrieveUserInfo()
	sessionStorage.setItem("currentScreen", "profile")
}

function pauseFortnite() {
	$("#pauseG").hide()
	$("#resumeG").show()
	pauseGame()
}

function resumeFortnite() {
	$("#resumeG").hide()
	$("#pauseG").show()
	resumeGame()
}

function restartFortnite() {
	restartGame()
	setupGame()
	startGame()
}

function updateScore() {
	$.ajax({
		method: "PUT",
		url: "/api/updateScore/user/" + sessionStorage.getItem("globalUsername"),
		data: {
			pass: sessionStorage.getItem("globalPassword"),
			score: _gameScore,
		},
	})
		.done(function (data, text_status, jqXHR) {
			console.log(JSON.stringify(data))
			console.log(text_status)
			console.log(jqXHR.status)
		})
		.fail(function (err) {
			console.log(err.status)
			console.log(JSON.stringify(err.responseJSON))
		})
}
// GAME CODE ENDS

// OTHER CODE
function setRadioButtons(from) {
	var lecture = ""
	if (from == "register") {
		if ($("#LEC101").prop("checked")) {
			lecture = "101"
		}
		if ($("#LEC102").prop("checked")) {
			lecture = "102"
		}
	} else {
		if ($("#LEC101U").prop("checked")) {
			lecture = "101"
		}
		if ($("#LEC102U").prop("checked")) {
			lecture = "102"
		}
	}
	return lecture
}

function verifyPassword(type) {
	if (type == "delete") {
		if ($("#passwordD").val() == "") {
			$("#errorMessage").html("<br>Please specify a password!")
			$("#passwordD").css("borderColor", "#FF0000")
		} else {
			$.ajax({
				method: "GET",
				url:
					"/api/verifyPassword/user/" +
					sessionStorage.getItem("globalUsername") +
					"/pass/" +
					$("#passwordD").val(),
			})
				.done(function (data) {
					if (data.same) {
						deleteAccount()
						unsetDeletionErrors()
					}
				})
				.fail(function (err) {
					$("#errorMessage").html("<br>The password is wrong!")
					$("#passwordD").css("borderColor", "#FF0000")
					console.log(err.status)
					console.log(JSON.stringify(err.responseJSON))
				})
		}
	} else {
		if ($("#confirmO").val() == "") {
			$("#errorMessage").html("<br>Please specify a password!")
			$("#confirmO").css("borderColor", "#FF0000")
		} else {
			$.ajax({
				method: "GET",
				url:
					"/api/verifyPassword/user/" +
					sessionStorage.getItem("globalUsername") +
					"/pass/" +
					$("#confirmO").val(),
			})
				.done(function (data) {
					if (data.same) {
						updateProfile()
					}
				})
				.fail(function (err) {
					$("#errorMessage").html("<br>The password is wrong!")
					$("#confirmO").css("borderColor", "#FF0000")
					console.log(err.status)
					console.log(JSON.stringify(err.responseJSON))
				})
		}
	}
}

function loadScreen() {
	var curr = sessionStorage.getItem("currentScreen")
	if (curr) {
		if (curr == "profile") {
			$("#ui_login").hide()
			$("#ui_register").hide()
			$("#ui_profile").show()
			$("#ui_update").hide()
			$("#ui_delete").hide()
			$("#ui_game").hide()
			retrieveUserInfo()
		}

		if (curr == "delete") {
			$("#ui_login").hide()
			$("#ui_register").hide()
			$("#ui_profile").hide()
			$("#ui_update").hide()
			$("#ui_delete").show()
			$("#ui_game").hide()
		}

		if (curr == "update") {
			$("#ui_login").hide()
			$("#ui_register").hide()
			$("#ui_profile").hide()
			$("#ui_update").show()
			$("#ui_delete").hide()
			$("#ui_game").hide()
		}
	}
}
// OTHER CODE ENDS

// This is executed when the document is ready (the DOM for this document is loaded)
$(function () {
	// Setup all events here and display the appropriate UI
	$("#loginSubmit").on("click", function () {
		login()
	})
	$("#newUser").on("click", function () {
		loginToRegistration()
	})
	$("#backR").on("click", function () {
		registrationToLogin()
	})
	$("#registerSubmit").on("click", function () {
		register()
	})
	$("#logoutUP").on("click", function () {
		profileToLogin()
	})
	$("#deleteUP").on("click", function () {
		profileToDelete()
	})
	$("#confirmD").on("click", function () {
		verifyPassword("delete")
	})
	$("#backD").on("click", function () {
		deleteToProfile()
	})
	$("#updateProfileUP").on("click", function () {
		profileToUpdate()
	})
	$("#backU").on("click", function () {
		updateToProfile()
	})
	$("#updateSubmit").on("click", function () {
		verifyPassword("update")
	})
	$("#newGame").on("click", function () {
		profileToGame()
	})
	$("#backG").on("click", function () {
		gameToProfile()
	})
	$("#restartG").on("click", function () {
		restartFortnite()
	})
	$("#pauseG").on("click", function () {
		pauseFortnite()
	})
	$("#resumeG").on("click", function () {
		resumeFortnite()
	})
})
