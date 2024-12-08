stage = null
ui = null
view = null
interval = null
controlInterval = null
_gameTime = 0
_gameIntervalTime = 20
_gameScore = 0

// Player controls
// Keep track of the keyboard keys the player is pressing down
var keysPressed = {}
// This map is conversion from keys to input values
var controlInput = {}
// This value is how quickly the player snaps from neutral to moving
var controlIntensity = 1
// This value is how quickly the player snaps from moving to neutral
var controlGravity = 1
var mouseEvent = null

// This value is for multiple projectiles
var mouseDown = false

// This value is for game pausing
var gamePaused = false

function setupGame() {
	var stageElement = document.getElementById("stage")

	// save default context
	var context = stageElement.getContext("2d")
	context.save()

	stage = new Stage(stageElement)
	ui = new UI(stageElement, stage)
	ui.updateToStage(stage)

	// https://javascript.info/keyboard-events
	var gameCanvas = document.getElementById("stage")
	document.addEventListener("keydown", getKeyDown)
	document.addEventListener("keyup", getKeyUp)

	// Mouse events should only happen in the game area
	gameCanvas.addEventListener("mousedown", getMouseDown)
	gameCanvas.addEventListener("mouseup", getMouseUp)
	gameCanvas.addEventListener("mousemove", getMouseMove)

	window.addEventListener("beforeunload", function (e) {
		sessionStorage.removeItem("currentScreen")
		e.preventDefault()
		e.returnValue = ""
	})
}
function startGame() {
	_gameScore = 0
	interval = setInterval(function () {
		stage.step()
		stage.draw()
		ui.draw()
		_gameTime += 1
	}, _gameIntervalTime)

	controlInput = {
		horizontal: 0,
		vertical: 0,
		shoot: false,
		release: false,
		swapID: 0,
		willSwap: false,
		interact: false,
		building: false,
	}
	controlInterval = setInterval(function () {
		runPlayerControls()
	}, _gameIntervalTime)
}
function pauseGame() {
	clearInterval(interval)
	interval = null
	gamePaused = true
}
function resumeGame() {
	interval = setInterval(function () {
		stage.step()
		stage.draw()
		ui.draw()
		_gameTime += 1
	}, _gameIntervalTime)
	gamePaused = false
}
function endGame() {
	_gameScore = stage.wave
	restartGame()
	endGameToProfile()
}

function restartGame() {
	stage = null
	ui = null
	view = null
	clearInterval(interval)
	interval = null
	clearInterval(controlInterval)
	controlInterval = null
	_gameTime = 0
	_gameIntervalTime = 20
	var gameCanvas = document.getElementById("stage")
	document.removeEventListener("keydown", getKeyDown)
	document.removeEventListener("keyup", getKeyUp)
	gameCanvas.removeEventListener("mousedown", getMouseDown)
	gameCanvas.removeEventListener("mouseup", getMouseUp)
	gameCanvas.removeEventListener("mousemove", getMouseMove)
}

function getKeyDown(event) {
	var key = event.key
	if (!keysPressed[key]) {
		keysPressed[key] = true
		if (key == "k") {
			// test self-damage
			if (stage.player) {
				stage.player.takeDamage(30)
			}
		} else if (key == "h") {
			// test self-heal
			if (stage.player) {
				stage.player.addHP(25)
			}
		}

		// One-click events
		// interaction
		if (key == " ") {
			controlInput["interact"] = true
		}
		// building
		else if (key == "b") {
			controlInput["building"] = true
		}
		// swap weapons
		else if (key == "1") {
			controlInput["swapID"] = 0
			controlInput["willSwap"] = true
		} else if (key == "2") {
			controlInput["swapID"] = 1
			controlInput["willSwap"] = true
		} else if (key == "3") {
			controlInput["swapID"] = 2
			controlInput["willSwap"] = true
		} else if (key == "4") {
			controlInput["swapID"] = 3
			controlInput["willSwap"] = true
		} else if (key == "5") {
			controlInput["swapID"] = 4
			controlInput["willSwap"] = true
		}
	}

	// Run moving events
}
function getKeyUp(event) {
	var key = event.key
	keysPressed[key] = null
}
function runPlayerControls() {
	var dx = controlInput["horizontal"]
	var dy = controlInput["vertical"]

	// Horizontal Movement
	if (keysPressed["d"]) {
		controlInput["horizontal"] = 1
	} else if (keysPressed["a"]) {
		controlInput["horizontal"] = -1
	}

	// Vertical Movement
	if (keysPressed["s"]) {
		controlInput["vertical"] = 1
	} else if (keysPressed["w"]) {
		controlInput["vertical"] = -1
	}

	// Horizontal Gravity (Return to neutral if not pressed)
	if (dx > 0 && !keysPressed["d"]) {
		controlInput["horizontal"] = 0
	} else if (dx < 0 && !keysPressed["a"]) {
		controlInput["horizontal"] = 0
	}

	// Vertical Gravity (Return to neutral if not pressed)
	if (dy > 0 && !keysPressed["s"]) {
		controlInput["vertical"] = 0
	} else if (dy < 0 && !keysPressed["w"]) {
		controlInput["vertical"] = 0
	}

	var newDx = controlInput["horizontal"]
	var newDy = controlInput["vertical"]
	// Control player if possible
	if (stage.player) {
		// moving player body
		stage.player.move(newDx, newDy)
		// slow down player
		if (newDx == 0) {
			stage.player.stopMovingX()
		}
		if (newDy == 0) {
			stage.player.stopMovingY()
		}

		if (controlInput["shoot"]) {
			// building something
			if (stage.player.isBuilding) {
				playerBuild(mouseEvent)
				controlInput["shoot"] = false
			} else {
				// firing player weapon
				if (controlInput["shoot"]) {
					fire(mouseEvent)
				}
			}
		}

		// release fire
		if (controlInput["release"]) {
			stage.player.releaseTrigger()
			controlInput["release"] = false
		}

		// interact
		if (controlInput["interact"]) {
			stage.player.queueInteract()
		}
		controlInput["interact"] = false

		if (controlInput["building"]) {
			if (stage.player.isBuilding) {
				stage.player.stopBuilding()
			} else {
				stage.player.startBuilding()
			}
		}
		controlInput["building"] = false

		// swap weapon / build
		var swapID = controlInput["swapID"]
		var willSwap = controlInput["willSwap"]
		if (willSwap) {
			if (stage.player.isBuilding) {
				stage.player.queueBlockSwap(swapID)
			} else {
				stage.player.queueWeaponSwap(swapID)
			}
		}
		controlInput["swapID"] = 0
		controlInput["willSwap"] = false
	}
}

function getMouseDown(event) {
	var click = event.button

	if (click == "0") {
		controlInput["shoot"] = true
		mouseEvent = event
		mouseDown = true
		/*fire(event); //ensures at minimum 1 bullet fires*/
		var gameCanvas = document.getElementById("stage")
		gameCanvas.addEventListener("mousemove", getMouseMoveOnDown)
	}
}

function getMouseUp(event) {
	clearInterval(mouseDown)
	mouseDown = false
	controlInput["shoot"] = false
	controlInput["release"] = true
	mouseEvent = null
}

function fire(event) {
	// Only fire if the player is able to
	if (stage.player.canShootWeapon()) {
		// Shoot at the location the player clicked
		var gameCanvas = document.getElementById("stage")
		var rect = gameCanvas.getBoundingClientRect()
		var clickPosx = Math.round(
			event.clientX - rect.left - stage.positionOffset.x
		)
		var clickPosy = Math.round(
			event.clientY - rect.top - stage.positionOffset.y
		)
		stage.player.shootWeapon(new Pair(clickPosx, clickPosy))
	}
}

function playerBuild(event) {
	// Only build if the player is able to
	if (stage.player.canBuild()) {
		// Shoot at the location the player clicked
		var gameCanvas = document.getElementById("stage")
		var rect = gameCanvas.getBoundingClientRect()
		var clickPosx = Math.round(
			event.clientX - rect.left - stage.positionOffset.x
		)
		var clickPosy = Math.round(
			event.clientY - rect.top - stage.positionOffset.y
		)
		stage.player.buildBlock()
	}
}

function getMouseMoveOnDown(event) {
	if (mouseDown) {
		mouseEvent = event
	}
}

function getMouseMove(event) {
	//if (!mouseDown){
	var gameCanvas = document.getElementById("stage")
	var rect = gameCanvas.getBoundingClientRect()

	var x = Math.round(event.clientX - rect.left - stage.positionOffset.x)
	var y = Math.round(event.clientY - rect.top - stage.positionOffset.y)
	if (stage.player) {
		stage.player.aimAt(new Pair(x, y))
	}
	//}
}
