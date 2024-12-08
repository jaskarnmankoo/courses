<?php
class GuessGame {
	public $secretNumber = 5;
	public $numGuesses = 0;
	public $history = array();
	public $state = "";
	public $startTime = 0;

	public function __construct() {
		$this->secretNumber = rand(1, 10);
		$this->startTime = time();
	}

	public function makeGuess($guess) {
		$this->numGuesses++;
		if ($guess > $this->secretNumber) {
			$this->state = "too high";
		}
		else if ($guess < $this->secretNumber) {
			$this->state = "too low";
		}
		else {
			$this->state = "correct";
		}
		$this->history[] = "Guess #$this->numGuesses was $guess and was $this->state.";
	}

	public function getState() {
		return $this->state;
	}
}
?>
