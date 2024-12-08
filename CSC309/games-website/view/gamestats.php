<?php
function printTableRow($num) {
	for ($x = 1;$x <= 4;$x++) {
		echo "<td>#$num: </td>";
	}
}

$resultsGuessGame = getGuessGameResults();
$resultsPegSolitaire = getPegSolitaireResults();
$resultsPuzzleGame = getPuzzleGameResults();
$resultsMastermind = getMastermindResults();

$userGuessGame = getUserResults($_SESSION['user'], "guessgame");
$userPegSolitaire = getUserResults($_SESSION['user'], "pegsolitaire");
$userPuzzleGame = getUserResults($_SESSION['user'], "puzzlegame");
$userMastermind = getUserResults($_SESSION['user'], "mastermind");
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="style.css" />
    <title>Game Statistics</title>
</head>

<body>
    <header>
        <h1>Game Statistics</h1>
    </header>

    <?php include 'view/navigationBar.php'; ?>

    <main>
        <h2>Top 5 Scores</h2>

        <table border=1 style="float: left; margin-bottom:8px;">
            <caption><b>Guess Game</b></caption>
            <tr>
                <th>Rank</th>
                <th>Username</th>
                <th>Attempts</th>
                <th>Time</th>

                <?php for($x = 1; $x < 6; $x++){ ?>
            <tr>
                <?php
					echo "<td>$x</td>";

					if($row = pg_fetch_row($resultsGuessGame)){
						echo "<td>$row[0]</td>";
                        echo "<td>$row[1]</td>";
                        echo "<td>$row[2] seconds</td>";
					} else {
						echo "<td colspan='3'></td>";
					}
				?>
            </tr>
            <?php } ?>
            </tr>
        </table>

        <table border=1 style="float: left; margin-left:8px; margin-bottom:8px;">
            <caption><b>Peg Solitaire</b></caption>
            <tr>
                <th>Rank</th>
                <th>Username</th>
                <th>Pegs Left</th>
                <th>Time</th>
                <?php for($x = 1; $x < 6; $x++){ ?>
            <tr>
                <?php
                    echo "<td>$x</td>";
                    if($row = pg_fetch_row($resultsPegSolitaire)){
						echo "<td>$row[0]</td>";
						echo "<td>$row[1]</td>";
                        echo "<td>$row[2] seconds</td>";
                    } else {
                        echo "<td colspan='3'></td>";
                    }
                ?>
            </tr>
            <?php } ?>
            </tr>
        </table>

        <table border=1 style="float: left; margin-left:8px; margin-bottom:8px;">
            <caption><b>15 Puzzle</b></caption>
            <tr>
                <th>Rank</th>
                <th>Username</th>
                <th>Moves</th>
                <th>Time</th>
                <?php for($x = 1; $x < 6; $x++){ ?>
            <tr>
                <?php
                    echo "<td>$x</td>";

                    if($row = pg_fetch_row($resultsPuzzleGame)){
                        echo "<td>$row[0]</td>";
                        echo "<td>$row[1]</td>";
                        echo "<td>$row[2] seconds</td>";
                    } else {
                        echo "<td colspan='3'></td>";
                    }
                ?>
            </tr>
            <?php } ?>
            </tr>
        </table>

        <table border=1 style="float: left; margin-left:8px; margin-bottom:8px;">
            <caption><b>Mastermind</b></caption>
            <tr>
                <th>Rank</th>
                <th>Username</th>
                <th>Attempts</th>
                <th>Time</th>
                <?php for($x = 1; $x < 6; $x++){ ?>
            <tr>
                <?php
                    echo "<td>$x</td>";

                    if($row = pg_fetch_row($resultsMastermind)){
                        echo "<td>$row[0]</td>";
                        echo "<td>$row[1]</td>";
                        echo "<td>$row[2] seconds</td>";
                    } else {
                        echo "<td colspan='3'></td>";
                    }
                ?>
            </tr>
            <?php } ?>
            </tr>
        </table>

        <br>

        <br>

        <h2></h2>
        <h2>Your Best Scores</h2>
        <table border=1 style="float: left; margin-left:8px;">
            <caption><b>Guess Game</b></caption>
            <tr>
                <th>Rank</th>
                <th>Attempts</th>
                <th>Time</th>
            </tr>
            <tr>
                <?php
					if($userGuessGame){
						echo "<td>$userGuessGame[3]</td>";
                        echo "<td>$userGuessGame[1]</td>";
                        echo "<td>$userGuessGame[2] Seconds</td>";
					}
					else{
						echo "<td colspan='3'>You haven't beat this game yet!</td>";
					}
				 ?>
            </tr>
        </table>

        <table border=1 style="float: left; margin-left:8px;">
            <caption><b>Peg Solitaire</b></caption>
            <tr>
                <th>Rank</th>
                <th>Pegs Left</th>
                <th>Time</th>
            </tr>
            <tr>
                <?php
                    if($userPegSolitaire){
                        echo "<td>$userPegSolitaire[3]</td>";
                        echo "<td>$userPegSolitaire[1]</td>";
                        echo "<td>$userPegSolitaire[2] Seconds</td>";
                    } else {
                        echo "<td colspan='3'>You haven't beat this game yet!</td>";
                    }
                ?>
            </tr>
        </table>

        <table border=1 style="float: left; margin-left:8px;">
            <caption><b>15 Puzzle</b></caption>
            <tr>
                <th>Rank</th>
                <th>Moves</th>
                <th>Time</th>
            </tr>
            <tr>
                <?php
                    if($userPuzzleGame){
                        echo "<td>$userPuzzleGame[3]</td>";
                        echo "<td>$userPuzzleGame[1]</td>";
                        echo "<td>$userPuzzleGame[2] Seconds</td>";
                    } else {
                        echo "<td colspan='3'>You haven't beat this game yet!</td>";
                    }
                ?>
            </tr>
        </table>

        <table border=1 style="float: left; margin-left:8px;">
            <caption><b>Mastermind</b></caption>
            <tr>
                <th>Rank</th>
                <th>Attempts</th>
                <th>Time</th>
            </tr>
            <tr>
                <?php
                    if($userMastermind){
                        echo "<td>$userMastermind[3]</td>";
                        echo "<td>$userMastermind[1]</td>";
                        echo "<td>$userMastermind[2] Seconds</td>";
                    } else{
                        echo "<td colspan='3'>You haven't beat this game yet!</td>";
                    }
                ?>
            </tr>
        </table>

    </main>
    <footer>
    </footer>
</body>

</html>
