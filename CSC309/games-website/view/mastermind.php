<?php
// So I don't have to deal with uninitialized $_REQUEST['guess']
$_GET['clickedPiece'] = !empty($_GET['clickedPiece']) ? $_GET['clickedPiece'] : '';

// Page token
$postback = mt_rand();
$_SESSION['postback'] = $postback;

function printPossiblePieces() {
	global $postback;
	$possiblePieces = $_SESSION['MasterMind']->getPossiblePieces();

	echo "<tr>";
	foreach ($possiblePieces as $piece) {
		$imgPath = $piece->getPath();
		$colour = $piece->getColour();
		$instance = $_SESSION['MasterMind']->getInstance();
		echo "<td>
					  <a href='index.php?clickedPiece=$instance$colour&postback=$postback'>
				      <img src='$imgPath'>
				  </td>";
	}
	echo "</tr>";
}

function printRow($num) {
	echo "<tr>";

	if ($_SESSION['MasterMind']->getInstance() == $num) {
		echo "<td> ACTIVE </td>";
	}
	else {
		echo "<td> INACTIVE </td>";
	}

	$gamePieces = $_SESSION['MasterMind']->getAllSelectedPieces();
	foreach ($gamePieces[$num] as $piece) {
		$imgPath = $piece->getPath();
		echo "<td><img src='$imgPath'></td>";
	}

	echo "<td> <table class='small' border='1'>";
	$allFeedbackPieces = $_SESSION['MasterMind']->getAllFeedbackPieces();
	$currFeedbackPieces = $allFeedbackPieces[$num];
	echo "<tr>";
	$imgPath = $currFeedbackPieces[0]->getPath();
	echo "<td><img src='$imgPath'></td>";
	$imgPath = $currFeedbackPieces[1]->getPath();
	echo "<td><img src='$imgPath'></td>";
	echo "</tr>";
	echo "<tr>";
	$imgPath = $currFeedbackPieces[2]->getPath();
	echo "<td><img src='$imgPath'></td>";
	$imgPath = $currFeedbackPieces[3]->getPath();
	echo "<td><img src='$imgPath'></td>";
	echo "</tr>";
	echo "</table>";
	echo "</td>";
	echo "</tr>";
}

function printSolution() {
	$state = $_SESSION['MasterMind']->getState();
	$instance = $_SESSION['MasterMind']->getInstance();
	$solutionPieces = $_SESSION['MasterMind']->getSolutionPieces();
	$defaultPieces = $_SESSION['MasterMind']->getDefaultPieces();

	echo "<tr>";
	echo "<td>SOLUTION</td>";
	if ($state == "win" or $instance == 10) {
		foreach ($solutionPieces as $piece) {
			$imgPath = $piece->getPath();
			echo "<td><img src='$imgPath'></td>";
		}
	}
	else {
		$imgPath = $defaultPieces[0]->getPath();
		$count = 0;
		while ($count < 4) {
			echo "<td><img src='$imgPath'></td>";
			$count++;
		}
	}
	echo "</tr>";
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>MasterMind</title>
    <link rel="stylesheet" type="text/css" href="style.css" />
    <style>
        .large img {
            width: 50px;
            height: 50px;
        }

        .small img {
            width: 25px;
            height: 25px;
        }
    </style>
</head>

<body>
    <header>
        <h1>MasterMind</h1>
    </header>

    <?php include 'view/navigationBar.php'; ?>

    <main>
        <font color="red">
            <?php echo(view_errors($errors)); echo "<br>" ?>
        </font>
        <form action="index.php" method="post">
            <input type="hidden" name="postback" value="<?= $postback ?>" />
            <table border="1">
                <tr>
                    <td>
                        Click on the corresponding colored peg to pick and place your peg in the row. <br><br>
                        <table class="large">
                            <?php printPossiblePieces(); ?>
                            <tr>
                                <td> <input type="submit" name="clickedButton" value="submit"> </td>
                                <td> <input type="submit" name="clickedButton" value="Reset"> </td>
                                <td> <input type="submit" name="clickedButton" value="New Game"> </td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table class="large" border="1">
                            <?php
								for ($i=0; $i<10; $i++) { printRow($i); }
								printSolution();
							?>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
    </main>
</body>

</html>
