<?php
// So I don't have to deal with uninitialized requests
if (!isset($_REQUEST['pegSubmit'])) $_REQUEST['pegSubmit'] = - 1;
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
    <header>
        <h1>Peg Solitaire</h1>
    </header>

    <?php
        include 'view/navigationBar.php';
        $pegsLeft=$game->getPegsLeft();
    ?>

    <main>
        <h3>Pegs Left: <?php echo $pegsLeft; ?></h2>
            <h3>Your next move is...</h2>
                <font color="red">
                    <?php echo(view_errors($errors)); echo "<br>" ?>
                </font>
                <label for="description">
                    <?php
                        if ($game->hasSelectedAPeg()){
                            echo "Your peg is selected and highlighted: ";
                        } else {
                            echo "Select the open peg slot you want to move it to: ";
                        }
                    ?>
                </label>
                <img width="12" height="12" src=<?php
			                                         if ($game->hasSelectedAPeg()) {
                                                         echo "resources/pegsolitaire/2.png";
                                                     } else {
                                                         echo "resources/pegsolitaire/1.png";
                                                     }
                                                ?>
                <br>
                <label for="description">Select the open peg slot you want to move it to: </label>
                <img width="12" height="12" src="resources/pegsolitaire/0.png">
                <br>
                <form action="index.php" method="post">
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
                                for($y = 0; $y < $rowLength; $y++){
                            ?>
                            <td>
                                <?php
                                    $peg = $game->board[$x][$y];
                                    if($pegNumber == $game->selectedPeg){
                                        $peg = 2;
                                    }
                                ?>
                                <a href='?pegSubmit=<?php echo $pegNumber; ?>&postback=<?php echo $postback; ?>'>
                                <img width="48" height="48" src="resources/pegsolitaire/<?php echo $peg; ?>.png">
                                <?php $pegNumber++; ?>
                            </td>
                            <?php } ?>
                        </tr>
                        <?php } ?>
                    </table><br>
                    <input type="hidden" name="postback" value="<?= $postback ?>" />
                    <input type="submit" name="submit" value="New Game" />
                    </tr>
                </form>
            <br>
    </main>
</body>

</html>
