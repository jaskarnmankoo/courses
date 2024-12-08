<?php
// So I don't have to deal with uninitialized $_GET['clickedTile']
$_GET['clickedTile'] = !empty($_GET['clickedTile']) ? $_GET['clickedTile'] : '';

// Page token
$postback = mt_rand();
$_SESSION['postback'] = $postback;

function printTableRow($columnStart, $columnEnd) {
	global $postback;
	echo "<tr>";
	$gameTiles = $_SESSION['The15Puzzle']->getTiles();
	for ($columnStart;$columnStart < $columnEnd;$columnStart++) {
		echo "<td>";
		if ($gameTiles[$columnStart]->getValue() != 0) {
			echo "<a href='index.php?clickedTile=$columnStart&postback=$postback'>";
		}
		$imgPath = $gameTiles[$columnStart]->getPath();
		echo "<img src='$imgPath'>";
		echo "</td>";
	}
	echo "</tr>";
}
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
        <h1>The 15 Puzzle</h1>
    </header>

    <?php
		include 'view/navigationBar.php';
		 $pieces = $_SESSION['The15Puzzle']->getTiles();
	?>

    <main>
        <font color="red">
            <?php echo(view_errors($errors)); echo "<br>" ?>
        </font>
        <form method="post">
            <table border="1">
                <?php printTableRow(0, 4); ?>
                <?php printTableRow(4, 8); ?>
                <?php printTableRow(8, 12); ?>
                <?php printTableRow(12, 16); ?>
            </table>
            <br>
            <input type="hidden" name="postback" value="<?= $postback ?>" />
            <input type="submit" name="clickedButton" value="New Game">
        </form>
    </main>
</body>

</html>
