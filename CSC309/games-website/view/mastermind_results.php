<?php
// So I don't have to deal with uninitialized $_REQUEST['guess']
$_GET['clickedPiece'] = !empty($_GET['clickedPiece']) ? $_GET['clickedPiece'] : '';

// Page Token
$postback = mt_rand();
$_SESSION['postback'] = $postback;

function printSolution() {
	$state = $_SESSION['MasterMind']->getState();
	$instance = $_SESSION['MasterMind']->getInstance();
	$solutionPieces = $_SESSION['MasterMind']->getSolutionPieces();
	$defaultPieces = $_SESSION['MasterMind']->getDefaultPieces();

	echo "<tr>";
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
            <?php echo(view_errors($errors)); echo "<br>";  ?>
        </font>
        <h2>Congratulations! You Won!</h2>
        <h3>The winning puzzle is below:</h3>
        <table class="large" border=1>
            <?php printSolution(); ?>
        </table>
        <h4> Click here to play again! <h4>
        <form method="post">
            <input type="hidden" name="postback" value="<?= $postback ?>" />
            <input type="submit" name="clickedButton" value="New Game">
        </form>
    </main>
</body>

</html>
