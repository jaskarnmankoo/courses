<?php
class Tile {
	public $value = 5;
	public $position = 1;
	public $imgPath = '';

	public function __construct($value, $row, $column) {
		$this->value = $value;

		if ($row == 1) {
			$this->position = - 1 + $column;
		}
		else if ($row == 2) {
			$this->position = 3 + $column;
		}
		else if ($row == 3) {
			$this->position = 7 + $column;
		}
		else {
			$this->position = 11 + $column;
		}

		$this->updatePath($value);
	}

	public function getPath() {
		return $this->imgPath;
	}

	public function getValue() {
		return $this->value;
	}

	public function getPosition() {
		return $this->position;
	}

	public function updateValue($value) {
		$this->value = $value;
	}

	public function updatePath($value) {
		if ($this->value == 0) {
			$this->imgPath = "resources/puzzlegame/blank.png";
		}
		else {
			$this->imgPath = "resources/puzzlegame/number$value.jpg";
		}
	}
}

class The15Puzzle {
	public $tiles = array();
	public $state = "lose";
	public $numMoves = 0;
	public $startTime = 0;

	public function __construct() {
		$values = range(0, 15);
		shuffle($values);
		$index = 0;

		for ($row = 1;$row < 5;$row++) {
			for ($column = 1;$column < 5;$column++) {
				$tile = new Tile($values[$index], $row, $column);
				$index++;
				array_push($this->tiles, $tile);
			}
		}
		$this->startTime = time();
	}

	public function canMove($fromPosition) {
		$leftValue = $fromPosition - 1;
		$rightValue = $fromPosition + 1;
		$downValue = $fromPosition + 4;
		$upValue = $fromPosition - 4;

		if ($leftValue >= 0 and $leftValue <= 15) {
			if ($this->tiles[$leftValue]->getValue() == 0) {
				return true;
			}
		}

		if ($rightValue >= 0 and $rightValue <= 15) {
			if ($this->tiles[$rightValue]->getValue() == 0) {
				return true;
			}
		}

		if ($upValue >= 0 and $upValue <= 15) {
			if ($this->tiles[$upValue]->getValue() == 0) {
				return true;
			}
		}
		if ($downValue >= 0 and $downValue <= 15) {
			if ($this->tiles[$downValue]->getValue() == 0) {
				return true;
			}
		}
		return false;
	}

	public function makeMove($fromPosition) {
		$leftValue = $fromPosition - 1;
		$rightValue = $fromPosition + 1;
		$downValue = $fromPosition + 4;
		$upValue = $fromPosition - 4;

		if ($leftValue >= 0 and $leftValue <= 15) {
			if ($this->tiles[$leftValue]->getValue() == 0) {
				$this->switch($leftValue, $fromPosition);
			}
		}

		if ($rightValue >= 0 and $rightValue <= 15) {
			if ($this->tiles[$rightValue]->getValue() == 0) {
				$this->switch($rightValue, $fromPosition);
			}
		}

		if ($upValue >= 0 and $upValue <= 15) {
			if ($this->tiles[$upValue]->getValue() == 0) {
				$this->switch($upValue, $fromPosition);
			}
		}

		if ($downValue >= 0 and $downValue <= 15) {
			if ($this->tiles[$downValue]->getValue() == 0) {
				$this->switch($downValue, $fromPosition);
			}
		}

		if ($this->won()) {
			$this->state = "win";
		}

		$this->numMoves++;
	}

	public function switch ($toPosition, $fromPosition) {
			$this->tiles[$toPosition]->updateValue($this->tiles[$fromPosition]->getValue());
			$this->tiles[$toPosition]->updatePath($this->tiles[$fromPosition]->getValue());
			$this->tiles[$fromPosition]->updateValue(0);
			$this->tiles[$fromPosition]->updatePath(0);
	}

	public function won() {
		$won = false;

		foreach ($this->tiles as $tile) {
			if ($tile->getValue() != $tile->getPosition()) {
				return false;
			}
		}
		return true;
	}

	public function getState() {
		return $this->state;
	}

	public function getTiles() {
		return $this->tiles;
	}

	public function getMoves() {
		return $this->numMoves;
	}
}
?>
