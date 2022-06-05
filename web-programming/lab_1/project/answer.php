<?php
@session_start();
if (!isset($_SESSION["tableRows"])) $_SESSION["tableRows"] = array();
date_default_timezone_set($_GET["timezone"]);
$x = (float) $_GET["x"];
$y = (float) $_GET["y"];
$r = (float) $_GET["r"];
if (checkData($x, $y, $r)) {
    $coordsStatus = checkCoordinates($x, $y, $r);
    $currentTime = date("H : i : s");
    $benchmarkTime = microtime(true) - $_SERVER["REQUEST_TIME_FLOAT"];
    array_push($_SESSION["tableRows"], "<tr>
    <td>$x</td>
    <td>$y</td>
    <td>$r</td>
    <td>$coordsStatus</td>
    <td>$currentTime</td>
    <td>$benchmarkTime</td>
    </tr>");
    echo "<table id='outputTable'>
        <tr>
            <th>x</th>
            <th>y</th>
            <th>r</th>
            <th>Точка входит в ОДЗ</th>
            <th>Текущее время</th>
            <th>Время работы скрипта</th>
        </tr>";
    foreach ($_SESSION["tableRows"] as $tableRow) echo $tableRow;
    echo "</table>";
} else {
    http_response_code(400);
    return;
}

function checkData($x, $y, $r) {
    return is_numeric($x) && ($x >= -5 && $x <= 3) &&
        in_array($y, array(-4, -3, -2, -1, 0, 1, 2, 3, 4)) &&
        in_array($r, array(1, 1.5, 2, 2.5, 3));
}

function checkCoordinates($x, $y, $r) {
    if ((($x >= 0) && ($x <= $r) && ($y >= 0) && ($y <= $r/2)) ||
        (($x <= 0) && ($y >= 0) && ($y <= $r + $x)) ||
        (($x**2 + $y**2 <= $r**2) && ($x >= 0) && ($y <= 0)))
        return "да";
    else return "нет";
}