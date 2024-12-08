<?php
$game = $_SESSION['PegSolitaire'];

// Page token
$postback = mt_rand();
$_SESSION['postback'] = $postback;
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="style.css" />
    <title>Peg Solitaire</title>
</head>

<body>
    <?php
        $pegsLeft = $game->getPegsLeft();
        $timeTaken = 0;
    ?>

    <header>
        <h1>
            <?php
                if ($pegsLeft <= 1){
                    echo "You Win!";
                } else if ($pegsLeft == 2){
                    echo "You almost won!";
                } else {
                    echo "Game Over!";
                }
            ?>
        </h1>
    </header>

    <?php include 'view/navigationBar.php'; ?>

    <main>
        <font color="red">
            <?php echo(view_errors($errors)); echo "<br>"; ?>
        </font>
        <h2>End of Game Result</h2>

        <label for="pegsLeft">Pegs Left: <?php echo $pegsLeft; ?> </label></br>
        <label for="timeTaken">Total Time: <?php echo $timeTaken; ?> </label></br>

        <hr />

        <h3>Final Board</h3>

        <table border="border">
            <?php
                // Set up the board view in table format
     	        $boardLength = count($game->board);
                $pegNumber=1;

  	            for($x = 0; $x < $boardLength; $x++){
            ?>
            <tr>
                <?php
                    $rowLength = count($game->board[$x]);
                    for($y = 0; $y < $rowLength; $y++) {
                ?>
                <td>
                    <?php $peg = $game->board[$x][$y]; ?>
                    <img width="48" height="48" src="resources/pegsolitaire/<?php echo $peg; ?>.png">
                    <?php $pegNumber++; ?>
                </td>
                <?php } ?>
            </tr>
            <?php } ?>
        </table></br>
        <form action="index.php" method="post">
            <input type="hidden" name="postback" value="<?= $postback ?>" />
            <input type="submit" name="submit" value="New Game">
        </form>
        </tr>
    </main>
    <footer></footer>
</body>

</html>
