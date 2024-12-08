<?php
// Load user's profile
$profileInfo = loadProfile($_SESSION['user']);

$year1Selected = $profileInfo[0];
$year2Selected = $profileInfo[1];
$year3Selected = $profileInfo[2];
$year4Selected = $profileInfo[3];
$year5Selected = $profileInfo[4];

$csc108 = $profileInfo[5];
$csc236 = $profileInfo[6];
$csc301 = $profileInfo[7];
$csc309 = $profileInfo[8];
$csc384 = $profileInfo[9];
$csc411 = $profileInfo[10];

$lec101 = $profileInfo[11];
$lec102 = $profileInfo[12];

//Page token
$postback = mt_rand();
$_SESSION['postback'] = $postback;
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

    <?php include 'view/navigationBar.php'; ?>

    <main>
        <h1>Your Profile</h1>
        <h3>Update Your Profile Here</h1>
            <form action="index.php" method="post">
                <!--legend>Login</legend-->
                <table>
                    <tr>
                        <th align="left"><label for="currentUser">Current Username</label></th>
                        <td>
                            <label for="currentUserText">
                                <?php echo($_SESSION['user']); ?>
                            </label>
                        </td>
                    </tr>

                    <tr>
                        <th align="left"><label for="user">New Username</label></th>
                        <td>
                            <input type="text" name="user" maxlength="20" value="<?php echo($_REQUEST['user']); ?>" />
                        </td>
                    </tr>

                    <tr>
                        <th align="left"><label for="password">New Password (Leave Blank for no Change)</label></th>
                        <td>
                            <input type="password" name="password" />
                        </td>
                    </tr>

                    <tr>
                        <th align="left"><label for="passwordConfirm">Confirm New Password (if applicable)</label></th>
                        <td>
                            <input type="password" name="passwordConfirm" />
                        </td>
                    </tr>

                    <tr>
                        <th align="left"><label for="birthday">Birthday</label></th>
                        <td>
                            <input type="date" name="birthday" value="<?php echo($_REQUEST['birthday']); ?>" />
                        </td>
                    </tr>

                    <tr>
                        <th align="left"><label for="favcolor">Favourite Colour</label></th>
                        <td>
                            <input type="color" name="favcolor" value="<?php echo($_REQUEST['favcolor']); ?>" />
                        </td>
                    </tr>

                    <tr>
                        <th align="left"><label for="year">University Year</label></th>
                        <td>
                            <select name="year">
                                <option value="1st Year" <?php echo($year1Selected); ?>>1st Year</option>
                                <option value="2nd Year" <?php echo($year2Selected); ?>>2nd Year</option>
                                <option value="3rd Year" <?php echo($year3Selected); ?>>3rd Year</option>
                                <option value="4th Year" <?php echo($year4Selected); ?>>4th Year</option>
                                <option value="Other" <?php echo($year5Selected); ?>>Other</option>
                            </select></td>
                    </tr>

                    <tr>
                        <th align="left"><label for="courses">CS Courses Taken</label></th>
                        <td>
                            <input type="checkbox" name="csc108" value="t" <?php echo($csc108); ?> />CSC108</br>
                            <input type="checkbox" name="csc236" value="t" <?php echo($csc236); ?> />CSC236</br>
                            <input type="checkbox" name="csc301" value="t" <?php echo($csc301); ?> />CSC301</br>
                            <input type="checkbox" name="csc309" value="t" <?php echo($csc309); ?> />CSC309</br>
                            <input type="checkbox" name="csc384" value="t" <?php echo($csc384); ?> />CSC384</br>
                            <input type="checkbox" name="csc411" value="t" <?php echo($csc411); ?> />CSC411</td>
                    </tr>

                    <tr>
                        <th align="left"><label for="lecture">Lecture Section</label></th>
                        <td>
                            <input type="radio" name="lecture" value="101" <?php echo($lec101); ?> />LEC101</br>
                            <input type="radio" name="lecture" value="102" <?php echo($lec102); ?> />LEC102</td>
                    </tr>

                    <tr>
                        <th align="left"><label for="signature">Signature</label></th>
                        <td>
                            <textarea name="sig" rows="4" cols="32" maxlength="80">
                                <?php echo($_REQUEST['sig']); ?>
                            </textarea>
                        </td>
                    </tr>

                    <tr>
                        <th>&nbsp;</th>
                        <td><input type="submit" name="submit" value="Update Profile" /></td>
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
                <input type="hidden" name="postback" value="<?= $postback ?>" />
            </form>
    </main>
    <footer></footer>
</body>

</html>
