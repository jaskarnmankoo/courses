<?php
ini_set('display_errors', 'On');
require_once "lib/lib.php";
require_once "model/something.php";

require_once "lib/profile_helper.php";
require_once "model/GuessGame.php";
require_once "model/The15Puzzle.php";
require_once "model/PegSolitaire.php";
require_once "model/MasterMind.php";

session_save_path("sess");
session_start();

$dbconn = db_connect();

$errors = array();
$view = "";

/* controller code */

function registerUser() {

}

// Handle href links, only if the user is logged in
if (!empty($_SESSION['user']) and isset($_REQUEST['state'])) {
	$newState = $_REQUEST['state'];
	if ($newState == "logout" or $newState == "login") {
		$_SESSION['state'] = "login";
		$view = "login.php";
		$_SESSION['user'] = '';
		unset($_SESSION['GuessGame']);
		unset($_SESSION['PegSolitaire']);
		unset($_SESSION['The15Puzzle']);
		unset($_SESSION['MasterMind']);

	}
	else if ($newState == "profile") {
		$_SESSION['state'] = "profile";
		$view = "profile.php";
	}
	else if ($newState == "gamestats") {
		$_SESSION['state'] = "gamestats";
		$view = "gamestats.php";
	}
	else {
		// We will start a new game
		$currentState = $_SESSION['state'];
		// If we have already started the game, don't do anything
		if ($_SESSION['state'] == $newState) {
			// Do nothing

		}
		else if ($newState == 'playPeg' and $currentState != 'resultsPeg') {
			if (empty($_SESSION['PegSolitaire'])) {
				$_SESSION['PegSolitaire'] = new PegSolitaire();
			}
			else if (!$_SESSION['PegSolitaire']->hasLegalMovesLeft()) {
				$_SESSION['PegSolitaire'] = new PegSolitaire();
			}
			$_SESSION['state'] = "playPeg";
			$view = "pegsolitaire.php";
		}
		else if ($newState == 'playGuessGame' and $currentState != 'resultsGuessGame') {
			if (empty($_SESSION['GuessGame'])) {
				$_SESSION['GuessGame'] = new GuessGame();
			}
			else if ($_SESSION['GuessGame']->getState() == "correct") {
				$_SESSION['GuessGame'] = new GuessGame();
			}
			$_SESSION['state'] = "playGuessGame";
			$view = "guessGame.php";
		}
		else if ($newState == 'play15Puzzle' and $currentState != 'results15Puzzle') {
			if (empty($_SESSION['The15Puzzle'])) {
				$_SESSION['The15Puzzle'] = new The15Puzzle();
			}
			else if ($_SESSION['The15Puzzle']->getState() == "win") {
				$_SESSION['The15Puzzle'] = new The15Puzzle();
			}
			$_SESSION['state'] = "play15Puzzle";
			$view = "15puzzle.php";
		}
		else if ($newState == 'playMastermind' and $currentState != 'resultsMastermind') {
			if (empty($_SESSION['MasterMind'])) {
				$_SESSION['MasterMind'] = new Mastermind();
			}
			else if ($_SESSION['MasterMind']->getState() == "win") {
				$_SESSION['MasterMind'] = new Mastermind();
			}
			$_SESSION['state'] = "playMastermind";
			$view = "mastermind.php";
		}
	}
}
// The user can only go to the login or registration page if they are logged out
else if (empty($_SESSION['user'])) {
	if (!empty($_REQUEST['state']) and $_REQUEST['state'] == 'login') {
		if ($_REQUEST['state'] == 'login') {
			$_SESSION['state'] = "login";
			$view = "login.php";
		}
		else if ($_REQUEST['state'] == 'register') {
			$_SESSION['state'] = "register";
			$view = "register.php";
		}
	}
}

/* local actions, these are state transforms */
if (!isset($_SESSION['state'])) {
	$_SESSION['state'] = 'login';

}

switch ($_SESSION['state']) {
	case "unavailable":
		$view = "unavailable.php";
	break;

	case "login":
		// the view we display by default
		$view = "login.php";

		// Switch to registration if the link was clicked
		if (isset($_REQUEST['state'])) {
			if ($_REQUEST['state'] == "register") {
				$_SESSION['state'] = 'register';
				$view = "register.php";
				break;
			}
		}

		// check if submit or not
		if (empty($_REQUEST['submit']) or $_REQUEST['submit'] != "Login") {
			break;
		}

		// validate and set errors
		if (empty($_REQUEST['user'])) $errors[] = 'username is required';
		if (empty($_REQUEST['password'])) $errors[] = 'password is required';
		if (!empty($errors)) break;

		// perform operation, switching state and view if necessary
		if (!$dbconn) {
			$errors[] = "Can't connect to db";
			break;
		}

		$profileInfo = getProfile($_REQUEST['user']);
		if ($profileInfo) {
			$hashPassword = $profileInfo[1];
			if (password_verify($_REQUEST['password'], $hashPassword)) {
				$_SESSION['user'] = $_REQUEST['user'];
				$_SESSION['state'] = "gamestats";
				$view = "gamestats.php";
			}
			else {
				$errors[] = "Invalid login";
			}
		}
		else {
			$errors[] = "Invalid login";
		}

		break;

	case "register":
		$view = "register.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		// check if submit or not
		if (empty($_REQUEST['submit'])) {
			break;
		}

		// Switch to login page if the button was clicked
		if ($_REQUEST['submit'] == "Login") {
			$_SESSION['state'] = 'login';
			$view = "login.php";
			break;
		}
		else if ($_REQUEST['submit'] != "Register") {
			break;
		}

		// validate and set errors
		if (empty($_REQUEST['user'])) $errors[] = 'A Username is required';
		if (empty($_REQUEST['password'])) $errors[] = 'A Password is required';
		if (empty($_REQUEST['passwordConfirm'])) $errors[] = 'Password confirmation is required';
		if (empty($_REQUEST['birthday'])) $errors[] = 'A valid birthday is required';
		if (!empty($errors)) break;

		// Check if the passwords match
		if ($_REQUEST['password'] != $_REQUEST['passwordConfirm']) {
			$errors[] = 'Passwords do not match';
			break;
		}

		// perform operation, switching state and view if necessary
		if (!$dbconn) {
			$errors[] = "Can't connect to db";
			break;
		}

		// Check if the username is already taken
		$profileInfo = getProfile($_REQUEST['user']);
		if ($profileInfo) {
			$user = $_REQUEST['user'];
			$errors[] = "The username $user is already taken";
			break;
		}

		// Add the user to the database
		$query = "INSERT INTO appuser VALUES($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13);";
		$result = pg_prepare($dbconn, "", $query);
		$hashPassword = password_hash($_REQUEST['password'], PASSWORD_BCRYPT);

		$result = pg_execute($dbconn, "", array(
			$_REQUEST['user'],
			$hashPassword,
			$_REQUEST['birthday'],
			$_REQUEST['favcolor'],
			$_REQUEST['year'],
			(empty($_REQUEST['csc108'])) ? 'f' : 't',
			(empty($_REQUEST['csc236'])) ? 'f' : 't',
			(empty($_REQUEST['csc301'])) ? 'f' : 't',
			(empty($_REQUEST['csc309'])) ? 'f' : 't',
			(empty($_REQUEST['csc384'])) ? 'f' : 't',
			(empty($_REQUEST['csc411'])) ? 'f' : 't',
			$_REQUEST['lecture'],
			$_REQUEST['sig']
		));
		if ($result) {
			$_SESSION['state'] = 'login';
			$view = "login.php";
		}
		else {
			$errors[] = "Could not execute query";
		}

		break;

	case "profile":
		$view = "profile.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		// check if submit or not
		if (empty($_REQUEST['submit'])) {
			break;
		}

		// Switch to login page if the button was clicked
		if ($_REQUEST['submit'] == "Logout") {
			$_SESSION['state'] = 'login';
			$_SESSION['user'] = '';
			$view = "login.php";
			break;
		}
		else if ($_REQUEST['submit'] != "Update Profile") {
			break;
		}

		// validate and set errors
		if (empty($_REQUEST['user'])) $errors[] = 'A username is required';
		if (!empty($errors)) break;

		// Check if the passwords match
		if ($_REQUEST['password'] != $_REQUEST['passwordConfirm']) {
			$errors[] = 'Passwords do not match';
			break;
		}
		// Create the password hash
		$hashPassword = password_hash($_REQUEST['password'], PASSWORD_BCRYPT);

		// Check if the desired username exists in the database
		$profileInfo = getProfile($_REQUEST['user']);
		if ($profileInfo) {
			// Cannot use a username taken by somebody else
			if ($profileInfo[0] != $_SESSION['user']) {
				$errors[] = "The username $profileInfo[0] is already taken by another user";
				break;
			}
		}

		// Update the profile
		$userInfo = getProfile($_SESSION['user']);
		if ($userInfo) {
			// Keep the same password if the user didn't update it
			if (empty($_REQUEST['password'])) {
				$hashPassword = $userInfo[1];
			}
		}

		// Update the user's information
		$result = updateProfile($_SESSION['user'], array(
			$_REQUEST['user'],
			$hashPassword,
			$_REQUEST['birthday'],
			$_REQUEST['favcolor'],
			$_REQUEST['year'],
			(empty($_REQUEST['csc108'])) ? 'f' : 't',
			(empty($_REQUEST['csc236'])) ? 'f' : 't',
			(empty($_REQUEST['csc301'])) ? 'f' : 't',
			(empty($_REQUEST['csc309'])) ? 'f' : 't',
			(empty($_REQUEST['csc384'])) ? 'f' : 't',
			(empty($_REQUEST['csc411'])) ? 'f' : 't',
			$_REQUEST['lecture'],
			$_REQUEST['sig'],
			$_SESSION['user']
		));
		if ($result) {
			$_SESSION['user'] = $_REQUEST['user'];
		}
		else {
			$errors[] = "Could not execute query";
		}

		break;

	case "gamestats":
		$view = "gamestats.php";

		break;

	case "playGuessGame":
		$view = "guessGame.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		// check if submit or not
		if (empty($_REQUEST['submit'])) {
			break;
		}
		if ($_REQUEST['submit'] == "New Game") {
			$_SESSION['GuessGame'] = new GuessGame();
			break;
		}
		else if ($_REQUEST['submit'] != "guess") {
			break;
		}

		// validate and set errors
		if (!is_numeric($_REQUEST["guess"])) $errors[] = "Guess must be numeric.";
		if (!empty($errors)) break;

		// perform operation, switching state and view if necessary
		$_SESSION["GuessGame"]->makeGuess($_REQUEST['guess']);
		if ($_SESSION["GuessGame"]->getState() == "correct") {
			$_SESSION['state'] = "resultsGuessGame";
			$view = "guessGame_results.php";

			// Save game data
			saveGuessGame($_SESSION['user'], $_SESSION["GuessGame"]->numGuesses, time() - $_SESSION["GuessGame"]->startTime);
		}
		$_REQUEST['guess'] = "";

		break;

	case "resultsGuessGame":
		$view = "guessGame_results.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		// check if submit or not
		if (empty($_REQUEST['submit']) || $_REQUEST['submit'] != "New Game") {
			$errors[] = "Invalid request";
			$view = "guessGame_results.php";
		}

		// validate and set errors
		if (!empty($errors)) break;

		// perform operation, switching state and view if necessary
		$_SESSION["GuessGame"] = new GuessGame();
		$_SESSION['state'] = "playGuessGame";
		$view = "guessGame.php";

		break;

	case "playPeg":
		// change the view to peg solitaire
		$view = "pegsolitaire.php";
		$game = $_SESSION['PegSolitaire'];

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		// Check if new game was clicked
		if (!empty($_REQUEST['submit']) and $_REQUEST['submit'] == "New Game") {
			$_SESSION['PegSolitaire'] = new PegSolitaire();
			$_REQUEST['state'] = 'playPeg';
			break;
		}

		// Check if a peg was selected
		if (isset($_REQUEST['pegSubmit'])) {
			if (!is_numeric($_GET['pegSubmit'])) {
				$errors[] = 'Move must be numeric.';
			}
			$peg = $_REQUEST['pegSubmit'];
			//$peg = -1;
			// If the peg's value is the defualt -1, we just ignore this case
			if ($peg == - 1) {
				break;
			}
			// If we haven't selected a peg yet, set a new selected peg
			else if (!$game->hasSelectedAPeg()) {
				// Can't select an empty peg slot
				if ($game->isPegEmpty($peg)) {
					$errors[] = 'Cannot select an empty peg slot';
					break;
				}
				$game->selectPeg($peg);
			}
			// Else we're trying to move an already selected peg
			else {
				$selectedPeg = $game->selectedPeg;
				// Verify if movement is possible
				if ($game->isALegalMove($selectedPeg, $peg)) {
					$game->movePeg($selectedPeg, $peg);
					$game->clearSelectedPeg();
				}
				// If the user re-selects the peg, unselect it
				else if ($selectedPeg == $peg) {
					$game->clearSelectedPeg();
				}
				// Illegal movement
				else {
					$errors[] = 'Illegal peg movement';
				}
			}
		}
		// End the game if there are no legal moves left
		if (!$game->hasLegalMovesLeft()) {
			$pegsLeft = $game->getPegsLeft();
			$_SESSION['state'] = 'resultsPeg';
			$view = "pegsolitaire_results.php";

			// Save game data
			savePegSolitaire($_SESSION['user'], $pegsLeft, time() - $game->startTime);
			break;
		}

		break;

	case "resultsPeg":
		$view = "pegsolitaire_results.php";
		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['submit'])) {
			$_SESSION['PegSolitaire'] = new PegSolitaire();
			$_SESSION['state'] = "playPeg";
			$view = "pegsolitaire.php";
		}
		break;

	case "play15Puzzle":
		$view = "15puzzle.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		if (isset($_REQUEST['clickedButton'])) {
			if ($_REQUEST['clickedButton'] == "New Game") {
				$_SESSION['The15Puzzle'] = new The15Puzzle();
				break;
			}
		}

		if (isset($_GET['clickedTile'])) {
			if (is_numeric($_GET['clickedTile'])) {
				if (!$_SESSION['The15Puzzle']->canMove($_GET['clickedTile'])) {
					$errors[] = "Illegal tile movement.";
					break;
				}
				$_SESSION['The15Puzzle']->makeMove($_GET['clickedTile']);
			}
			else {
				$errors[] = "Move must be numeric.";
			}
		}

		// perform operation, switching state and view if necessary
		if ($_SESSION['The15Puzzle']->getState() == "win") {
			$_SESSION['state'] = "results15puzzle";
			$view = "15puzzle_results.php";

			// Save game data
			savePuzzleGame($_SESSION['user'], $_SESSION['The15Puzzle']->numMoves, time() - $_SESSION['The15Puzzle']->startTime);
		}

		$_GET['clickedTile'] = "";
		$_REQUEST['clickedButton'] = "";
		break;

	case "results15puzzle":
		$view = "15puzzle_results.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		if (!empty($_REQUEST['clickedButton']) and $_REQUEST['clickedButton'] == "New Game") {
			$_SESSION['The15Puzzle'] = new The15Puzzle();
			$_SESSION['state'] = 'play15Puzzle';
			$view = "15puzzle.php";
		}

		$_GET['clickedTile'] = "";
		$_REQUEST['clickedButton'] = "";

		break;

	case "playMastermind":
		$view = "mastermind.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		if (isset($_REQUEST['clickedButton'])) {
			if ($_REQUEST['clickedButton'] == "New Game") {
				$_SESSION['MasterMind'] = new MasterMind();
				break;
			}

			if ($_SESSION['MasterMind']->getInstance() != 10) {
				if ($_REQUEST['clickedButton'] == "submit") {
					if ($_SESSION['MasterMind']->checkFilled()) {
						$_SESSION['MasterMind']->feedback();
						$_SESSION['MasterMind']->instance++;

						if ($_SESSION['MasterMind']->instance == 10) {
							$_SESSION['MasterMind']->state = "lose";
							echo "<h3>YOU LOST! TRY AGAIN!</h3>";
						}
					}
					else {
						$errors[] = "Invalid move. You must fill the row.";
					}
				}

				if ($_REQUEST['clickedButton'] == "Reset") {
					$_SESSION['MasterMind']->reset();
				}
			}
			else {
				$errors[] = "The game is over. Start a new one.";
			}
			unset($_GET['clickedPiece']);
		}

		if (isset($_GET['clickedPiece'])) {
			if ($_SESSION['MasterMind']->getInstance() != 10) {
				if (intval($_GET['clickedPiece']) == $_SESSION['MasterMind']->getInstance()) {
					if (in_array(substr($_GET['clickedPiece'], 1) , $_SESSION['MasterMind']->getPossibleColours())) {
						if (!$_SESSION['MasterMind']->addSelectedPiece($_GET['clickedPiece'])) {
							$errors[] = "Invalid move. All places of the row are occupied.";
						}
					}
					else {
						$errors[] = "Invalid move. You must play with the given colours.";
					}
				}
				else {
					$errors[] = "Invalid move. You must play with the ACTIVE row.";
				}
			}
			else {
				$errors[] = "The game is over. Start a new one.";
			}
		}
		if (!empty($errors)) break;

		if ($_SESSION["MasterMind"]->getState() == "win") {
			$_SESSION['state'] = "resultsMastermind";
			$view = "mastermind_results.php";

			// Save game data
			saveMastermind($_SESSION['user'], $_SESSION["MasterMind"]->instance, time() - $_SESSION["MasterMind"]->startTime);
		}

		break;

	case "resultsMastermind":
		$view = "mastermind_results.php";

		// If we clicked reload or back, don't do anything
		if (!empty($_REQUEST['postback']) and $_REQUEST['postback'] != $_SESSION['postback']) {
			break;
		}

		if ($_REQUEST['clickedButton'] == "New Game") {
			$_SESSION['MasterMind'] = new MasterMind();
			$_SESSION['state'] = 'playMastermind';
			$view = "mastermind.php";
			unset($_GET['clickedPiece']);
		}

		break;

	}
	require_once "view/$view";
?>
