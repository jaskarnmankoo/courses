<?php
// So I don't have to deal with uninitialized $_REQUEST['guess']
$_GET['clickedTile'] = !empty($_GET['clickedTile']) ? $_GET['clickedTile'] : '';

// Page token
$postback = mt_rand();
$_SESSION['postback'] = $postback;
function printTableRow($columnStart, $columnEnd) {
	echo "<tr>";
	$gameTiles = $_SESSION['The15Puzzle']->getTiles();
	for ($columnStart;$columnStart < $columnEnd;$columnStart++) {
		echo "<td>";
		$imgPath = $gameTiles[$columnStart]->getPath();
		echo "<img src='$imgPath'>";
		echo "</td>";
	}
	echo "</tr>";
}

$moves = $_SESSION['The15Puzzle']->getMoves();
$pieces = $_SESSION['The15Puzzle']->getTiles();
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="style.css" />
    <title>The 15 Puzzle</title>

    <style>
        img {
            width: 100px;
            height: 100px;
        }
    </style>
</head>

<body>
    <header>
        <h1>The 15 Puzzle Results</h1>
    </header>

    <?php include 'view/navigationBar.php'; ?>

    <main>
        <font color="red">
            <?php echo(view_errors($errors)); echo "<br>" ?>
        </font>
        <h2> CONGRATULATIONS! YOU WON IN <?php echo $moves; ?> moves!</h2>
        <h2> BELOW IS THE WINNING PUZZLE: </h2>
        <table border="1">
            <?php printTableRow(0, 4); ?>
            <?php printTableRow(4, 8); ?>
            <?php printTableRow(8, 12); ?>
            <?php printTableRow(12, 16); ?>
        </table>
        <br>
        <form action="index.php" method="post">
            <input type="hidden" name="postback" value="<?= $postback ?>" />
            <input type="submit" name="clickedButton" value="New Game">
        </form>
    </main>
</body>

</html>
