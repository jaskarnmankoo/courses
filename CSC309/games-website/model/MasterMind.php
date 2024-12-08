<?php
class Piece {
	public $colour = "black";
	public $path = "images/BlackDot.png";

	public function __construct($index, $possibleColours) {
		$this->colour = $possibleColours[$index];
		$this->setPath();
	}

	public function setPath() {
		if ($this->colour == "black") {
			$this->path = "resources/mastermind/BlackDot.png";
		}
		else if ($this->colour == "white") {
			$this->path = "resources/mastermind/WhiteDot.png";
		}
		else if ($this->colour == "green") {
			$this->path = "resources/mastermind/GreenDot.png";
		}
		else if ($this->colour == "blue") {
			$this->path = "resources/mastermind/BlueDot.png";
		}
		else if ($this->colour == "grey") {
			$this->path = "resources/mastermind/GreyDot.png";
		}
		else if ($this->colour == "orange") {
			$this->path = "resources/mastermind/OrangeDot.png";
		}
		else if ($this->colour == "pink") {
			$this->path = "resources/mastermind/PinkDot.png";
		}
		else if ($this->colour == "red") {
			$this->path = "resources/mastermind/RedDot.png";
		}
		else if ($this->colour == "yellow") {
			$this->path = "resources/mastermind/YellowDot.png";
		}
	}

	public function setColour($colour) {
		$this->colour = $colour;
	}

	public function getPath() {
		return $this->path;
	}

	public function getColour() {
		return $this->colour;
	}
}

class MasterMind {
	public $possibleColours = array(
		"black",
		"white",
		"green",
		"blue",
		"grey",
		"orange",
		"pink",
		"red",
		"yellow"
	);
	public $possiblePieces = array();
	public $defaultPieces = array();
	public $solutionPieces = array();
	public $selectedPieces = array();
	public $allSelectedPieces = array();
	public $feedbackPieces = array();
	public $allFeedbackPieces = array();
	public $state = "lose";
	public $instance = 0;
	public $startTime = 0;

	public function __construct() {
		$this->startTime = time();
		for ($index = 0;$index < 9;$index++) {
			$piece = new Piece($index, $this->possibleColours);

			if ($piece->getColour() == "black" or $piece->getColour() == "white" or $piece->getColour() == "green") {
				array_push($this->defaultPieces, $piece);
			}
			else {
				array_push($this->possiblePieces, $piece);
			}
		}

		while (count($this->solutionPieces) != 4) {
			array_push($this->solutionPieces, $this->possiblePieces[rand(0, 5) ]);
		}

		// create a completely black piece set
		while (count($this->selectedPieces) != 4) {
			array_push($this->selectedPieces, $this->defaultPieces[0]);
			array_push($this->feedbackPieces, $this->defaultPieces[0]);
		}

		// create ten instances of above
		while (count($this->allSelectedPieces) != 10) {
			array_push($this->allSelectedPieces, $this->selectedPieces);
			array_push($this->allFeedbackPieces, $this->feedbackPieces);
		}
	}

	public function getPossibleColours() {
		return $this->possibleColours;
	}

	public function getPossiblePieces() {
		return $this->possiblePieces;
	}

	public function getDefaultPieces() {
		return $this->defaultPieces;
	}

	public function getSolutionPieces() {
		return $this->solutionPieces;
	}

	public function getSelectedPieces() {
		return $this->selectedPieces;
	}

	public function getAllSelectedPieces() {
		return $this->allSelectedPieces;
	}

	public function getfeedbackPieces() {
		return $this->feedbackPieces;
	}

	public function getAllFeedbackPieces() {
		return $this->allFeedbackPieces;
	}

	public function getState() {
		return $this->state;
	}

	public function getInstance() {
		return $this->instance;
	}

	public function addSelectedPiece($colour) {
		$truth = true;
		$index = intval($colour[0]);
		$colour = substr($colour, 1);
		$overwrite = $this->defaultPieces[0];
		foreach ($this->possiblePieces as $piece) {
			if ($piece->getColour() == $colour) {
				$overwrite = $piece;
				break;
			}
		}

		if ($this->allSelectedPieces[$index][0]->getColour() == "black") {
			$this->allSelectedPieces[$index][0] = $overwrite;
		}
		else if ($this->allSelectedPieces[$index][1]->getColour() == "black") {
			$this->allSelectedPieces[$index][1] = $overwrite;
		}
		else if ($this->allSelectedPieces[$index][2]->getColour() == "black") {
			$this->allSelectedPieces[$index][2] = $overwrite;
		}
		else if ($this->allSelectedPieces[$index][3]->getColour() == "black") {
			$this->allSelectedPieces[$index][3] = $overwrite;
		}
		else {
			$truth = false;
		}
		return $truth;
	}

	public function reset() {
		$overwrite = $this->defaultPieces[0];
		$count = 0;

		while ($count < 4) {
			$this->allSelectedPieces[$this->instance][$count] = $overwrite;
			$this->allFeedbackPieces[$this->instance][$count] = $overwrite;
			$count++;
		}
	}

	public function checkFilled() {
		$filled = true;

		foreach ($this->allSelectedPieces[$this
			->instance] as $piece) {
			$colour = $piece->getColour();

			if ($colour != "black") {
				$filled = true;
			}
			else {
				$filled = false;
			}
		}

		return $filled;
	}

	public function feedback() {
		$positionSystem = array(); /* 0 indicates that piece is not in sequence or correct spot
		                                    1 indicates that piece is in sequence but not in correct spot
		                                    2 indicates that piece is in sequence and in correct spot
		*/

		$curr = 0;
		$num2 = 0;
		foreach ($this->solutionPieces as $piece) {
			if (in_array($piece, $this->allSelectedPieces[$this
				->instance])) {
				if ($this->allSelectedPieces[$this->instance][$curr] == $piece) {
					array_push($positionSystem, 2);
					$num2++;
				}
			}
			else {
				array_push($positionSystem, 0);
			}
			$curr++;
		}

		while (count($positionSystem) != 4) {
			array_push($positionSystem, 1);
		}

		$index = 0;
		if ($num2 == 4) {
			$this->state = "win";
			$overwrite = $this->defaultPieces[2];

			while ($index < 4) {
				$this->allFeedbackPieces[$this->instance][$index] = $overwrite;
				$index++;
			}
		}
		else {
			rsort($positionSystem);

			while ($index < 4) {
				if ($positionSystem[$index] == 2) {
					$overwrite = $this->defaultPieces[2];
				}
				else if ($positionSystem[$index] == 1) {
					$overwrite = $this->defaultPieces[1];
				}
				else {
					$overwrite = $this->defaultPieces[0];
				}
				$this->allFeedbackPieces[$this->instance][$index] = $overwrite;
				$index++;
			}
		}
	}
}
?>
