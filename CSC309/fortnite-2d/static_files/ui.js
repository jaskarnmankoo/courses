class UI {
	constructor(canvas, stage) {
		this.canvas = canvas
		this.stage = stage

		// the logical width and height of the ui should match the stage's
		this.width = stage.width
		this.height = stage.height
		this.updateToStage(stage)

		// the dimensions of the canvas
		this.canvasWidth = canvas.width
		this.canvasHeight = canvas.height

		// some ui values
		this.HUDPosition = new Pair(24, 24)
		this.minimapDimensions = new Pair(150, 150)
		this.inventoryPos = new Pair(24 + 80 + 16, this.canvasHeight - 24)
		this.waveUIPosition = new Pair(
			this.canvasWidth / 2,
			this.canvasHeight / 2 - 60
		)
		this.defaultFontStyle = "16px sans-serif"
		this.bigFontStyle = "24px sans-serif"
		this.smallFontStyle = "12px sans-serif"

		// game logic
		this.waveStartTime = 0

		// player values
		this.playerMaxHP = 1
		this.playerHP = 0
		this.playerAmmo = 0
		this.playerScore = 0
	}

	updateToStage(stage) {
		this.width = stage.width
		this.height = stage.height
	}

	drawWaveTimer(context, time) {
		context.restore()
		context.save()

		var floorTime = Math.floor(time)
		var timeDifference = time - floorTime
		context.font = this.bigFontStyle
		context.fillStyle = "rgba(0,0,0,1)"
		context.fillText(
			"Wave incoming in ",
			this.waveUIPosition.x - 120,
			this.waveUIPosition.y
		)
		context.fillStyle = "rgba(0,0,0," + timeDifference + ")"
		context.fillText(
			floorTime,
			this.waveUIPosition.x + 80,
			this.waveUIPosition.y
		)
	}

	drawHPBar(context, actor, colour) {
		var currentHP = actor.getCurrentHP()
		var maxHP = actor.getMaxHP()
		var HPPercent = currentHP / maxHP

		context.fillStyle = "rgba(64,64,64,0.75)"
		context.fillRect(
			actor.x + this.stage.positionOffset.x - 35,
			actor.y + this.stage.positionOffset.y + actor.getHeight() / 2 + 8,
			70,
			6
		)
		context.fillStyle = colour
		context.fillRect(
			actor.x + this.stage.positionOffset.x - 35,
			actor.y + this.stage.positionOffset.y + actor.getHeight() / 2 + 8,
			70 * HPPercent,
			6
		)
		context.strokeStyle = "rgba(0,0,0,0.75)"
		context.lineWidth = 2
		context.strokeRect(
			actor.x + this.stage.positionOffset.x - 35,
			actor.y + this.stage.positionOffset.y + actor.getHeight() / 2 + 8,
			70,
			6
		)
	}

	convertToMinimapPos(position) {
		return new Pair(
			this.minimapDimensions.x * (position.x / this.stage.width),
			this.minimapDimensions.y * (position.y / this.stage.height)
		)
	}

	drawMinimap(context) {
		context.restore()
		context.save()

		var HUDx = this.HUDPosition.x
		var HUDy = this.HUDPosition.y

		// map bg
		context.fillStyle = "rgba(128,128,128,0.5)"
		context.fillRect(
			HUDx,
			HUDy,
			this.minimapDimensions.x,
			this.minimapDimensions.y
		)
		context.lineWidth = 4
		context.strokeStyle = "rgba(0,0,0,0.5)"
		context.strokeRect(
			HUDx,
			HUDy,
			this.minimapDimensions.x,
			this.minimapDimensions.y
		)

		// items
		var items = this.stage.getAllItems()
		var itemStyle = new ColourCStyle(255, 255, 128, 0.75)
		itemStyle.outline = true
		itemStyle.outlineWidth = 2
		itemStyle.setOutlineColorRGBA(160, 160, 64, 0.75)
		for (var i = 0; i < items.length; i++) {
			if (items[i].enabled) {
				var shape = new Square(4, itemStyle)
				var minimapPos = this.convertToMinimapPos(items[i].position)
				shape.draw(context, minimapPos.plus(this.HUDPosition), 0)
			}
		}

		// player
		if (this.stage.player) {
			var playerStyle = new ColourCStyle(255, 255, 255, 0.75)
			playerStyle.outline = true
			playerStyle.outlineWidth = 2
			playerStyle.setOutlineColorRGBA(128, 128, 128, 0.75)
			var shape = new Triangle(6, 8, playerStyle)
			var minimapPos = this.convertToMinimapPos(this.stage.player.position)
			shape.draw(
				context,
				minimapPos.plus(this.HUDPosition),
				this.stage.player.rotationAngle
			)
		}

		// enemies
		var enemies = this.stage.getAllEnemies()
		var enemyStyle = new ColourCStyle(255, 96, 96, 0.75)
		enemyStyle.outline = true
		enemyStyle.outlineWidth = 2
		enemyStyle.setOutlineColorRGBA(196, 48, 48, 0.75)
		for (var i = 0; i < enemies.length; i++) {
			if (enemies[i].enabled) {
				var shape = new Triangle(6, 8, enemyStyle)
				var minimapPos = this.convertToMinimapPos(enemies[i].position)
				shape.draw(
					context,
					minimapPos.plus(this.HUDPosition),
					enemies[i].rotationAngle
				)
			}
		}
	}

	drawHUD(context) {
		context.restore()
		context.save()

		var player = this.stage.player

		// draw wave #
		context.fillStyle = "rgba(0,0,0,1)"
		context.font = this.bigFontStyle
		context.textAlign = "center"
		context.fillText("Wave " + this.stage.wave, this.canvasWidth / 2, 40)

		// draw HP
		if (player) {
			this.playerHP = player.getCurrentHP()
			this.playerMaxHP = player.getMaxHP()
		} else {
			this.playerHP = 0
		}
		var HPPercent = this.playerHP / this.playerMaxHP
		context.fillStyle = "rgba(64,64,64,1)"
		context.fillRect(
			this.HUDPosition.x + this.minimapDimensions.x + 16,
			this.HUDPosition.y + 32,
			200,
			16
		)
		context.fillStyle = "rgba(128,255,128,1)"
		context.fillRect(
			this.HUDPosition.x + this.minimapDimensions.x + 16,
			this.HUDPosition.y + 32,
			200 * HPPercent,
			16
		)
		context.lineWidth = 2
		context.strokeStyle = "rgba(0,0,0,1)"
		context.strokeRect(
			this.HUDPosition.x + this.minimapDimensions.x + 16,
			this.HUDPosition.y + 32,
			200,
			16
		)

		context.font = this.defaultFontStyle
		context.fillStyle = "rgba(0,0,0,1)"
		context.fillText(
			"HP: " + this.playerHP + " / " + this.playerMaxHP,
			this.HUDPosition.x + 100 + this.minimapDimensions.x + 16,
			this.HUDPosition.y + 64
		)
		if (player) {
			this.drawHPBar(context, player, "rgba(128,255,128,0.75)")
		}

		// draw player score
		if (player) {
			this.playerScore = player.getScore()
		}
		context.fillStyle = "rgba(0,0,0,1)"
		context.fillText(
			"$" + this.playerScore,
			this.HUDPosition.x + 100 + this.minimapDimensions.x + 16,
			this.HUDPosition.y + 108
		)

		// draw Ammo
		if (player) {
			this.playerAmmo = player.getAmmo()
		}
		var ammoText = this.playerAmmo < 0 ? "Unlimited" : this.playerAmmo
		context.fillText(
			"Ammo: " + ammoText,
			this.HUDPosition.x + 100 + this.minimapDimensions.x + 16,
			this.HUDPosition.y + 90
		)

		// draw crosshairs
		if (player) {
			if (player.weapon) {
				if (player.weapon.crosshairs && player.aimLocation) {
					context.translate(stage.positionOffset.x, stage.positionOffset.y)
					player.weapon.crosshairs.draw(context)
					context.translate(-stage.positionOffset.x, -stage.positionOffset.y)
				}
			}
		}

		// draw inventory
		if (player) {
			var iPos = this.inventoryPos
			var currentObj = player.isBuilding
				? player.buildingID
				: player.weaponSwapID
			for (var i = 0; i < 5; i++) {
				context.fillStyle =
					currentObj == i ? "rgba(196,196,196,0.5)" : "rgba(128,128,128,0.5)"
				context.fillRect(iPos.x + i * 120, iPos.y - 100, 100, 100)
				context.lineWidth = 4
				context.strokeStyle = "rgba(0,0,0,0.5)"
				context.strokeRect(iPos.x + i * 120, iPos.y - 100, 100, 100)
			}
			if (player.isBuilding) {
				for (var i = 0; i < player.blocks.length; i++) {
					var drawPos = player.blocks[i].position.clone()
					context.translate(
						iPos.x + 50 + i * 120 - drawPos.x,
						iPos.y - 50 - drawPos.y
					)
					player.blocks[i].draw(context)
					context.translate(
						-(iPos.x + 50 + i * 120 - drawPos.x),
						-(iPos.y - 50 - drawPos.y)
					)

					context.translate(iPos.x + 50 + i * 120, iPos.y - 50)
					context.fillStyle = "rgba(255,255,255,0.75)"
					context.textAlign = "center"
					context.fillText(i, 40, -34)

					context.fillStyle =
						player.score >= player.blocks[i].getCost()
							? "rgba(255,255,255,0.75)"
							: "rgba(255,0,0,0.75)"
					context.textAlign = "left"
					context.fillText("$" + player.blocks[i].getCost(), -40, 44)
					context.translate(-(iPos.x + 50 + i * 120), -(iPos.y - 50))
				}
			} else {
				for (var i = 0; i < player.backpackWeapons.length; i++) {
					var drawPos = player.backpackWeapons[i].position.clone()
					context.translate(
						iPos.x + 50 + i * 120 - drawPos.x,
						iPos.y - 50 - drawPos.y
					)
					player.backpackWeapons[i].draw(context)
					context.translate(
						-(iPos.x + 50 + i * 120 - drawPos.x),
						-(iPos.y - 50 - drawPos.y)
					)

					context.translate(iPos.x + 50 + i * 120, iPos.y - 50)
					context.fillStyle = "rgba(255,255,255,0.75)"
					context.textAlign = "center"
					context.fillText(i, 40, -34)
					context.textAlign = "left"
					context.fillText(player.backpackWeapons[i].name, -40, 44)
					context.translate(-(iPos.x + 50 + i * 120), -(iPos.y - 50))
				}
			}
		}
	}

	drawEnemyStats(context) {
		context.restore()
		context.save()

		var enemyActors = this.stage.getAllEnemies()
		for (var i = 0; i < enemyActors.length; i++) {
			var actor = enemyActors[i]
			if (actor.enabled) {
				this.drawHPBar(context, actor, "rgba(255,128,128,0.75)")
			}
		}
	}

	drawItemStats(context) {
		context.restore()
		context.save()

		var items = this.stage.getAllItems()
		for (var i = 0; i < items.length; i++) {
			var item = items[i]
			if (item.enabled) {
			}
		}
	}

	drawBlockStats(context) {
		context.restore()
		context.save()

		var actors = this.stage.getAllBlocks()
		for (var i = 0; i < actors.length; i++) {
			var actor = actors[i]
			if (actor.enabled) {
				var currentHP = actor.getCurrentHP()
				var maxHP = actor.getMaxHP()
				var HPPercent = currentHP / maxHP
				this.drawHPBar(context, actor, "rgba(212,212,128,0.75)")

				/*context.fillStyle = 'rgba(64,64,64,0.75)';
				context.fillRect(actor.x + this.stage.positionOffset.x - 30, actor.y + this.stage.positionOffset.y - actor.getHeight()/2 - 8, 60, 4);
				context.fillStyle = 'rgba(212,212,128,0.75)';
				context.fillRect(actor.x + this.stage.positionOffset.x - 30, actor.y + this.stage.positionOffset.y - actor.getHeight()/2 - 8, 60 * HPPercent, 4);*/
			}
		}
	}

	draw() {
		var context = this.canvas.getContext("2d")

		// draw wave timer if possible
		var waveTimer = this.stage.getWaveTimer()
		if (waveTimer > 0) {
			this.drawWaveTimer(context, waveTimer)
		}

		// draw minimap
		this.drawMinimap(context)

		// draw player's stats
		this.drawHUD(context)

		// draw other stats
		this.drawEnemyStats(context)
		this.drawItemStats(context)
		this.drawBlockStats(context)
	}
} // End UI class
