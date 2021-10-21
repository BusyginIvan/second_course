<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML>
<html lang="ru-RU">
<head>
    <meta charset="utf-8">
    <title>ЛР №2</title>
    <link href="stylesheets/overall.css" rel="stylesheet">
    <link href="stylesheets/main_page.css" rel="stylesheet">
    <link href="stylesheets/check_button.css" rel="stylesheet">
</head>
<body>
    <header class="shaded animated">
        <h1>Лабораторная работа №2, вариант 12027.</h1>
        <h2>Бусыгин Иван Сергеевич, группа P3212.</h2>
    </header>

    <table id="mainTable" class="shaded list">
        <thead><td colspan="3"><h3>Проверка попадания точки в ограниченную область.</h3></td></thead>

        <tbody>
        <tr><td colspan="3"><hr></td></tr>

        <tr>
            <td colspan="2"></td>
            <td rowspan="5"><canvas id="canvas"></canvas></td>
        </tr>

        <tr>
            <td class="first">Выберите X:</td>
            <td class="second"><table><tbody><tr>
                <td class="first"><input type="radio" name="radioX" value="-4" class="illuminated animated"></td>
                <td class="second"> -4 </td> <td></td>
                <td class="first"><input type="radio" name="radioX" value="-3" class="illuminated animated"></td>
                <td class="second"> -3 </td> <td></td>
                <td class="first"><input type="radio" name="radioX" value="-2" class="illuminated animated"></td>
                <td class="second"> -2 </td> <td></td>
            </tr><tr>
                <td class="first"><input type="radio" name="radioX" value="-1" class="illuminated animated"></td>
                <td class="second"> -1 </td> <td></td>
                <td class="first"><input type="radio" name="radioX" value="0" checked class="illuminated animated"></td>
                <td class="second"> 0  </td> <td></td>
                <td class="first"><input type="radio" name="radioX" value="1" class="illuminated animated"></td>
                <td class="second"> 1  </td> <td></td>
            </tr><tr>
                <td class="first"><input type="radio" name="radioX" value="2" class="illuminated animated"></td>
                <td class="second"> 2 </td> <td></td>
                <td class="first"><input type="radio" name="radioX" value="3" class="illuminated animated"></td>
                <td class="second"> 3 </td> <td></td>
                <td class="first"><input type="radio" name="radioX" value="4" class="illuminated animated"></td>
                <td class="second"> 4 </td> <td></td>
            </tr></tbody></table></td>
        </tr>

        <tr>
            <td class="first">Введите Y:</td>
            <td class="second">
                <input required id="textY" class="illuminated" type="text"
                       placeholder="значение в промежутке (-5 до 5)" maxlength="6">
            </td>
        </tr>

        <tr>
            <td class="first">Выберите R:</td>
            <td class="second"><table><tbody><tr>
                <td class="first"><input type="checkbox" name="checkboxR" value="1" checked class="illuminated animated"></td>
                <td class="second"> 1 </td> <td></td>
                <td class="first"><input type="checkbox" name="checkboxR" value="2" class="illuminated animated"></td>
                <td class="second"> 2 </td> <td></td>
                <td class="first"><input type="checkbox" name="checkboxR" value="3" class="illuminated animated"></td>
                <td class="second"> 3 </td> <td></td>
                <td class="first"><input type="checkbox" name="checkboxR" value="4" class="illuminated animated"></td>
                <td class="second"> 4 </td> <td></td>
                <td class="first"><input type="checkbox" name="checkboxR" value="5" class="illuminated animated"></td>
                <td class="second"> 5 </td> <td></td>
            </tr></tbody></table></td>
        </tr>

        <tr><td colspan="2">
            <button id="checkButton"><div></div>Проверить</button>
        </td></tr>

        <tr><td colspan="3"><hr></td></tr>
        </tbody>

        <tfoot><tr><td colspan="3" id="outputContainer">
            <jsp:include page = "/results.jsp" />
        </td></tr></tfoot>
    </table>

    <footer class="shaded animated"><figure>
            <img src="images/pulpit_logo.png" alt="Кафедра вычислительной техники НИУ ИТМО">
            <figcaption>2021</figcaption>
    </figure></footer>

    <script type="text/javascript" src="scripts/jquery.js"></script>
    <script src="scripts/form.js"></script>
    <script src="scripts/canvas.js"></script>
    <script>
        initForm();
        initCanvas();
    </script>
</body>
</html>