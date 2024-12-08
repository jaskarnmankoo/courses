<?php
// So I don't have to deal with uninitialized $_REQUEST['guess']
$_REQUEST['guess'] = !empty($_REQUEST['guess']) ? $_REQUEST['guess'] : '';

// Page token
$postback = mt_rand();
$_SESSION['postback'] = $postback;
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="style.css" />
    <title>Guess Game</title>
</head>

<body>
    <header>
        <h1>Guess Game</h1>
    </header>

    <?php include 'view/navigationBar.php'; ?>

    <main>
        <?php if($_SESSION["GuessGame"]->getState()!="correct"){ ?>

        <label for="description">Guess a number between 0 and 10</label><br>
        <form method="post">
            <input type="text" name="guess" value="<?php echo($_REQUEST['guess']); ?>" /> <input type="submit" name="submit" value="guess" />
        </form>

        <?php } ?>

        <font color="red">
            <?php echo(view_errors($errors)); ?>
        </font>

        <?php
			foreach($_SESSION['GuessGame']->history as $key=>$value){
				echo("<br/> $value");
			}
		  	if($_SESSION["GuessGame"]->getState()=="correct"){
		?>

        <form method="post">
            <br><input type="submit" name="submit" value="New Game" />
        </form>

        <?php } ?>

        <form method="post">
            <input type="hidden" name="postback" value="<?= $postback ?>" />
            <input type="submit" name="submit" value="New Game" />
        </form>
    </main>
</body>

</html>
