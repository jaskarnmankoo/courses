<?php
//So I don't have to deal with unsetting paramaters when refilling the form
$_REQUEST['user'] = !empty($_REQUEST['user']) ? $_REQUEST['user'] : '';
$_REQUEST['password'] = !empty($_REQUEST['password']) ? $_REQUEST['password'] : '';
$_REQUEST['birthday'] = !empty($_REQUEST['birthday']) ? $_REQUEST['birthday'] : '2000-01-01';
$_REQUEST['favcolor'] = !empty($_REQUEST['favcolor']) ? $_REQUEST['favcolor'] : '#ff0000';
$_REQUEST['year'] = !empty($_REQUEST['year']) ? $_REQUEST['year'] : '1st Year';

$year1Selected = ($_REQUEST['year'] == '1st Year') ? 'selected' : '';
$year2Selected = ($_REQUEST['year'] == '2nd Year') ? 'selected' : '';
$year3Selected = ($_REQUEST['year'] == '3rd Year') ? 'selected' : '';
$year4Selected = ($_REQUEST['year'] == '4th Year') ? 'selected' : '';
$year5Selected = ($_REQUEST['year'] == 'Other') ? 'selected' : '';

$_REQUEST['csc108'] = !empty($_REQUEST['csc108']) ? $_REQUEST['csc108'] : 'f';
$_REQUEST['csc236'] = !empty($_REQUEST['csc236']) ? $_REQUEST['csc236'] : 'f';
$_REQUEST['csc301'] = !empty($_REQUEST['csc301']) ? $_REQUEST['csc301'] : 'f';
$_REQUEST['csc309'] = !empty($_REQUEST['csc309']) ? $_REQUEST['csc309'] : 'f';
$_REQUEST['csc384'] = !empty($_REQUEST['csc384']) ? $_REQUEST['csc384'] : 'f';
$_REQUEST['csc411'] = !empty($_REQUEST['csc411']) ? $_REQUEST['csc411'] : 'f';

$csc108 = ($_REQUEST['csc108'] == 't') ? 'CHECKED' : '';
$csc236 = ($_REQUEST['csc236'] == 't') ? 'CHECKED' : '';
$csc301 = ($_REQUEST['csc301'] == 't') ? 'CHECKED' : '';
$csc309 = ($_REQUEST['csc309'] == 't') ? 'CHECKED' : '';
$csc384 = ($_REQUEST['csc384'] == 't') ? 'CHECKED' : '';
$csc411 = ($_REQUEST['csc411'] == 't') ? 'CHECKED' : '';

$_REQUEST['lecture'] = !empty($_REQUEST['lecture']) ? $_REQUEST['lecture'] : '101';
$lec101 = ($_REQUEST['lecture'] == '101') ? 'checked' : '';
$lec102 = ($_REQUEST['lecture'] == '102') ? 'checked' : '';

$_REQUEST['sig'] = !empty($_REQUEST['sig']) ? $_REQUEST['sig'] : '';
?>
