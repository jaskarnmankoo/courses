<?php
// So I don't have to deal with unset $_REQUEST['user'] when refilling the form
// You can also take a look at the new ?? operator in PHP7
$_REQUEST['user'] = !empty($_REQUEST['user']) ? $_REQUEST['user'] : '';
$_REQUEST['password'] = !empty($_REQUEST['password']) ? $_REQUEST['password'] : '';
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="style.css" />
    <title>Games</title>
</head>

<body>
    <header>
        <h1>Games</h1>
    </header>
    <nav>
        <ul>
            <li> <a href="?state=register">Register</a>
        </ul>
    </nav>
    <main>
        <h1>Login</h1>
        <form action="index.php" method="post">
            <table>
                <!-- Trick below to re-fill the user form field -->
                <tr>
                    <th align="left"><label for="user">Username</label></th>
                    <td><input type="text" name="user" value="<?php echo($_REQUEST['user']); ?>" /></td>
                </tr>
                <tr>
                    <th align="left"><label for="password">Password</label></th>
                    <td> <input type="password" name="password" /></td>
                </tr>
                <tr>
                    <th>&nbsp;</th>
                    <td><input type="submit" name="submit" value="Login" /></td>
                </tr>
                <tr>
                    <th>&nbsp;</th>
                    <td>
                        <font color="red">
                            <?php echo(view_errors($errors)); ?>
                        </font>
                    </td>
                </tr>
            </table>
        </form>
    </main>
    <footer>
    </footer>
</body>

</html>
