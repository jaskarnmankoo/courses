<?php
class PegSolitaire {

	public $board = array(
		array(1, 1, 1, 1, 1, 1),
		array(1, 1, 1, 1, 1, 1),
		array(1, 1, 1, 1, 1, 1),
		array(1, 1, 1, 1, 1, 1),
		array(1, 1, 1, 1, 1, 1),
		array(1, 1, 1, 1, 1, 1)
	);
	public $totalTurns = 0;
	public $selectedPeg = - 1;
	public $startTime = 0;

	public function __construct() {
		$this->setEmptyPeg(rand(1, 36));
		$this->startTime = time();
	}

	public function setEmptyPeg($peg) {
		$position = $this->getPegPosition($peg);
		$this->board[$position[0]][$position[1]] = 0;
	}

	public function selectPeg($peg) {
		$this->selectedPeg = $peg;
	}

	public function clearSelectedPeg() {
		$this->selectedPeg = - 1;
	}

	public function hasSelectedAPeg() {
		return ($this->selectedPeg != - 1);
	}

	public function getPegPosition($peg) {
		$column = ($peg - 1) % 6;
		$row = ($peg - $column - 1) / 6;
		return array(
			$row,
			$column
		);
	}

	// Returns true if this peg is not occupied
	public function isPegEmpty($peg) {
		$position = $this->getPegPosition($peg);
		return ($this->board[$position[0]][$position[1]] == 0);
	}

	// Returns true if it is legal for peg1 to move into peg2's position
	public function isALegalMove($peg1, $peg2) {

		// No peg should have a value less than 1
		if ($peg1 < 1 or $peg2 < 1 or $peg1 > 36 or $peg2 > 36) {
			return false;
		}

		$position1 = $this->getPegPosition($peg1);
		$position2 = $this->getPegPosition($peg2);

		// peg1 cannot jump into a non-empty space
		if (!$this->isPegEmpty($peg2)) {
			return false;
		}

		$xDistance = abs($position2[0] - $position1[0]);
		$yDistance = abs($position2[1] - $position1[1]);

		// peg1 cannot move into its own position
		if ($xDistance == 0 and $yDistance == 0) {
			return false;
		}
		// peg1 cannot move farther than 2 tiles away, or exactly 1 tile away
		if ($xDistance > 2 or $yDistance > 2 or $xDistance == 1 or $yDistance == 1) {
			return false;
		}
		// Check if there's a peg in between peg1 and peg2
		$betweenPeg = $peg1 + (($peg2 - $peg1) / 2);
		$position3 = $this->getPegPosition($betweenPeg);
		// peg1 cannot move unless another peg is between it and peg2
		if ($this->board[$position3[0]][$position3[1]] != 1) {
			return false;
		}

		// Any other movement is legal
		return true;
	}

	// Returns true if the given peg has any legal moves left
	public function canPegMove($peg) {
		// Peg cannot move if there's no peg at this position
		if ($this->isPegEmpty($peg)) {
			return false;
		}

		// Check all 8 directions
		// Up
		if ($this->isALegalMove($peg, $peg - 12)) {
			return true;
		}
		// Down
		else if ($this->isALegalMove($peg, $peg + 12)) {
			return true;
		}
		// Right
		else if ($this->isALegalMove($peg, $peg + 2)) {
			return true;
		}
		// Left
		else if ($this->isALegalMove($peg, $peg - 2)) {
			return true;
		}
		// Up-Right
		else if ($this->isALegalMove($peg, $peg - 10)) {
			return true;
		}
		// Down-Right
		else if ($this->isALegalMove($peg, $peg + 14)) {
			return true;
		}
		// Up-Left
		else if ($this->isALegalMove($peg, $peg - 14)) {
			return true;
		}
		// Down-Left
		else if ($this->isALegalMove($peg, $peg + 10)) {
			return true;
		}
		return false;
	}

	// Returns true if there are any legal moves to be made on the board
	public function hasLegalMovesLeft() {
		$boardLength = count($this->board);
		for ($x = 0;$x < $boardLength;$x++) {
			$rowLength = count($this->board[$x]);
			for ($y = 0;$y < $rowLength;$y++) {
				$peg = ($x * 6) + $y + 1;
				if ($this->canPegMove($peg)) {
					return true;
				}
			}
		}
		return false;
	}

	// Moves peg1 into the position of peg2
	public function movePeg($peg1, $peg2) {
		$position1 = $this->getPegPosition($peg1);
		$position2 = $this->getPegPosition($peg2);

		// Remove peg1
		$this->board[$position2[0]][$position2[1]] = $this->board[$position1[0]][$position1[1]];
		$this->setEmptyPeg($peg1);
		//$this->board[$position1[0]][$position1[1]] = 0;
		// Remove the in-between Peg
		$betweenPeg = $peg1 + (($peg2 - $peg1) / 2);
		$position3 = $this->getPegPosition($betweenPeg);
		//$this->board[$position3[0]][$position3[1]] = 0;
		$this->setEmptyPeg($betweenPeg);
	}

	// Returns the amount of pegs left on the board
	public function getPegsLeft() {
		$pegsLeft = 0;
		$boardLength = count($this->board);
		for ($x = 0;$x < $boardLength;$x++) {
			$rowLength = count($this->board[$x]);
			for ($y = 0;$y < $rowLength;$y++) {
				if ($this->board[$x][$y] == 1) {
					$pegsLeft++;
				}
			}
		}
		return $pegsLeft;
	}
}
?>
