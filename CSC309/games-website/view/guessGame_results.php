<?php
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
        <h1>GuessGame</h1>
    </header>
    <?php include 'view/navigationBar.php'; ?>
    <main>
        <font color="red">
            <?php echo(view_errors($errors)); echo "<br>" ?>
        </font>
        <?php
			foreach($_SESSION['GuessGame']->history as $key=>$value){
				echo("<br/> $value");
			}
		?>
        <form method="post">
            <input type="hidden" name="postback" value="<?= $postback ?>" />
            <input type="submit" name="submit" value="New Game" />
        </form>
    </main>
    <footer></footer>
</body>

</html>
