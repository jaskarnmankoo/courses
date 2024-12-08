<?php
require_once "lib/lib.php";

// Returns an array
function getProfile($user) {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}

	$query = "SELECT * FROM appuser WHERE userid=$1;";
	$result = pg_prepare($dbconn, "", $query);
	$result = pg_execute($dbconn, "", array(
		$user
	));
	return pg_fetch_row($result);
}

// Loads the request parameters for a given user from the database
function loadProfile($user) {
	$row = getProfile($user);
	if ($row == "Can't connect to db") {
		return;
	}
	if ($row) {
		$_REQUEST['user'] = !empty($_REQUEST['user']) ? $_REQUEST['user'] : $row[0];
		$_REQUEST['password'] = !empty($_REQUEST['password']) ? $_REQUEST['password'] : $row[1];
		$_REQUEST['birthday'] = !empty($_REQUEST['birthday']) ? $_REQUEST['birthday'] : $row[2];
		$_REQUEST['favcolor'] = !empty($_REQUEST['favcolor']) ? $_REQUEST['favcolor'] : $row[3];
		$_REQUEST['year'] = !empty($_REQUEST['year']) ? $_REQUEST['year'] : $row[4];
		$_REQUEST['csc108'] = !empty($_REQUEST['csc108']) ? $_REQUEST['csc108'] : $row[5];
		$_REQUEST['csc236'] = !empty($_REQUEST['csc236']) ? $_REQUEST['csc236'] : $row[6];
		$_REQUEST['csc301'] = !empty($_REQUEST['csc301']) ? $_REQUEST['csc301'] : $row[7];
		$_REQUEST['csc309'] = !empty($_REQUEST['csc309']) ? $_REQUEST['csc309'] : $row[8];
		$_REQUEST['csc384'] = !empty($_REQUEST['csc384']) ? $_REQUEST['csc384'] : $row[9];
		$_REQUEST['csc411'] = !empty($_REQUEST['csc411']) ? $_REQUEST['csc411'] : $row[10];
		$_REQUEST['lecture'] = !empty($_REQUEST['lecture']) ? $_REQUEST['lecture'] : $row[11];
		$_REQUEST['sig'] = !empty($_REQUEST['sig']) ? $_REQUEST['sig'] : $row[12];

		$year1Selected = ($_REQUEST['year'] == '1st Year') ? 'selected' : '';
		$year2Selected = ($_REQUEST['year'] == '2nd Year') ? 'selected' : '';
		$year3Selected = ($_REQUEST['year'] == '3rd Year') ? 'selected' : '';
		$year4Selected = ($_REQUEST['year'] == '4th Year') ? 'selected' : '';
		$year5Selected = ($_REQUEST['year'] == 'Other') ? 'selected' : '';

		$csc108 = ($_REQUEST['csc108'] == 't') ? 'CHECKED' : '';
		$csc236 = ($_REQUEST['csc236'] == 't') ? 'CHECKED' : '';
		$csc301 = ($_REQUEST['csc301'] == 't') ? 'CHECKED' : '';
		$csc309 = ($_REQUEST['csc309'] == 't') ? 'CHECKED' : '';
		$csc384 = ($_REQUEST['csc384'] == 't') ? 'CHECKED' : '';
		$csc411 = ($_REQUEST['csc411'] == 't') ? 'CHECKED' : '';

		$lec101 = ($_REQUEST['lecture'] == '101') ? 'checked' : '';
		$lec102 = ($_REQUEST['lecture'] == '102') ? 'checked' : '';

		return array(
			$year1Selected,
			$year2Selected,
			$year3Selected,
			$year4Selected,
			$year5Selected,
			$csc108,
			$csc236,
			$csc301,
			$csc309,
			$csc384,
			$csc411,
			$lec101,
			$lec102
		);
	}
	return false;
}

// Updates the profile for a given user, with a given array of profile attributes
function updateProfile($user, $args) {
	$currentProfile = getProfile($user);
	if ($currentProfile) {
		$dbconn = db_connect();
		if (!$dbconn) {
			return false;
		}
		$query = "UPDATE appuser SET
			userid=$1,
			password=$2,
			birthday=$3,
			favcolor=$4,
			year=$5,
			csc108=$6,
			csc236=$7,
			csc301=$8,
			csc309=$9,
			csc384=$10,
			csc411=$11,
			lecture=$12,
			signature=$13 WHERE userid=$14;";
		$result = pg_prepare($dbconn, "", $query);
		$result = pg_execute($dbconn, "", $args);
		if (!$result) {
			return false;
		}
		// Update game statistics tables if the username was changed
		if ($currentProfile[0] != $args[0]) {
			$gamesArray = array(
				'guessgame',
				'pegsolitaire',
				'puzzlegame',
				'mastermind'
			);
			foreach ($gamesArray as $game) {
				$query = "UPDATE $game SET userid=$1 WHERE userid=$2;";
				$result = pg_prepare($dbconn, "", $query);
				$result = pg_execute($dbconn, "", array(
					$args[0],
					$currentProfile[0]
				));
			}
		}
		return true;
	}
	return false;
}

// Saves guess game stats for a given user
function saveGuessGame($user, $attempts, $time) {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM guessgame WHERE userid=$1;";
	$result = pg_prepare($dbconn, "", $query);
	$result = pg_execute($dbconn, "", array(
		$user
	));

	// If the user has recorded stats already, update if the given stats are better
	if ($row = pg_fetch_row($result)) {
		if (((int)$row[1] > $attempts) or ((int)$row[1] == $attempts and (int)$row[2] > $time)) {
			$query = "UPDATE guessgame SET attempts=$1, time=$2 WHERE userid=$3;";
			$result = pg_prepare($dbconn, "", $query);
			$result = pg_execute($dbconn, "", array(
				$attempts,
				$time,
				$user
			));
			if ($result) {
				return true;
			}
		}
		else {
			return false;
		}
	}
	else {
		$query = "INSERT INTO guessgame VALUES($1,$2,$3);";
		$result = pg_prepare($dbconn, "", $query);
		$result = pg_execute($dbconn, "", array(
			$user,
			$attempts,
			$time
		));
		if ($result) {
			return true;
		}
	}
	return false;
}

// Saves peg solitaire stats for a given user
function savePegSolitaire($user, $pegsLeft, $time) {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM pegsolitaire WHERE userid=$1;";
	$result = pg_prepare($dbconn, "", $query);
	$result = pg_execute($dbconn, "", array(
		$user
	));

	// If the user has recorded stats already, update if the given stats are better
	if ($row = pg_fetch_row($result)) {
		if (((int)$row[1] > $pegsLeft) or ((int)$row[1] == $pegsLeft and (int)$row[2] > $time)) {
			$query = "UPDATE guessgame SET attempts=$1, pegsleft=$2 WHERE userid=$3;";
			$result = pg_prepare($dbconn, "", $query);
			$result = pg_execute($dbconn, "", array(
				$time,
				$user
			));
			if ($result) {
				return true;
			}
		}
		else {
			return false;
		}
	}
	else {
		$query = "INSERT INTO pegsolitaire VALUES($1,$2,$3);";
		$result = pg_prepare($dbconn, "", $query);
		$result = pg_execute($dbconn, "", array(
			$user,
			$pegsLeft,
			$time
		));
		if ($result) {
			return true;
		}
	}
	return false;
}

// Saves 15 puzzle game stats for a given user
function savePuzzleGame($user, $moves, $time) {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM puzzlegame WHERE userid=$1;";
	$result = pg_prepare($dbconn, "", $query);
	$result = pg_execute($dbconn, "", array(
		$user
	));

	// If the user has recorded stats already, update if the given stats are better
	if ($row = pg_fetch_row($result)) {
		if (((int)$row[1] > $moves) or ((int)$row[1] == $moves and (int)$row[2] > $time)) {
			$query = "UPDATE puzzlegame SET moves=$1, time=$2 WHERE userid=$3;";
			$result = pg_prepare($dbconn, "", $query);
			$result = pg_execute($dbconn, "", array(
				$moves,
				$time,
				$user
			));
			if ($result) {
				return true;
			}
		}
		else {
			return false;
		}
	}
	else {
		$query = "INSERT INTO puzzlegame VALUES($1,$2,$3);";
		$result = pg_prepare($dbconn, "", $query);
		$result = pg_execute($dbconn, "", array(
			$user,
			$moves,
			$time
		));
		if ($result) {
			return true;
		}
	}
	return false;
}

// Saves mastermind stats for a given user
function saveMastermind($user, $attempts, $time) {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM mastermind WHERE userid=$1;";
	$result = pg_prepare($dbconn, "", $query);
	$result = pg_execute($dbconn, "", array(
		$user
	));

	// If the user has recorded stats already, update if the given stats are better
	if ($row = pg_fetch_row($result)) {
		if (((int)$row[1] > $attempts) or ((int)$row[1] == $attempts and (int)$row[2] > $time)) {
			$query = "UPDATE mastermind SET attempts=$1, time=$2 WHERE userid=$3;";
			$result = pg_prepare($dbconn, "", $query);
			$result = pg_execute($dbconn, "", array(
				$attempts,
				$time,
				$user
			));
			if ($result) {
				return true;
			}
		}
		else {
			return false;
		}
	}
	else {
		$query = "INSERT INTO mastermind VALUES($1,$2,$3);";
		$result = pg_prepare($dbconn, "", $query);
		$result = pg_execute($dbconn, "", array(
			$user,
			$attempts,
			$time
		));
		if ($result) {
			return true;
		}
	}
	return false;
}

// Returns guess game scores for all players
function getGuessGameResults() {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM guessgame ORDER BY attempts ASC, time ASC;";
	$result = pg_query($dbconn, $query);
	return $result;
}

// Returns peg solitaire scores for all players
function getPegSolitaireResults() {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM pegsolitaire ORDER BY pegsleft ASC, time ASC;";
	$result = pg_query($dbconn, $query);
	return $result;
}

// Returns puzzle game scores for all players
function getPuzzleGameResults() {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM puzzlegame ORDER BY moves ASC, time ASC;";
	$result = pg_query($dbconn, $query);
	return $result;
}

// Returns mastermind scores for all players
function getMastermindResults() {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}
	$query = "SELECT * FROM mastermind ORDER BY attempts ASC, time ASC;";
	$result = pg_query($dbconn, $query);
	return $result;
}

// Returns the given user's best score for the given game, with their rank attached at the end
function getUserResults($user, $game) {
	$dbconn = db_connect();
	if (!$dbconn) {
		return false;
	}

	$results = false;
	if ($game == "guessgame") {
		$results = getGuessGameResults();
	}
	else if ($game == "pegsolitaire") {
		$results = getPegSolitaireResults();
	}
	else if ($game == "puzzlegame") {
		$results = getPuzzleGameResults();
	}
	else if ($game == "mastermind") {
		$results = getMastermindResults();
	}
	if ($results) {
		$rank = 1;
		while ($row = pg_fetch_array($results)) {
			if ($row[0] == $user) {
				$row[] = $rank;
				return $row;
			}
			$rank++;
		}
	}
	return false;
}
?>
