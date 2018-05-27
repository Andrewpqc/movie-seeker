<%--
  Created by IntelliJ IDEA.
  User: andrew
  Date: 18-1-6
  Time: 下午2:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>搜索</title>
</head>
<body>
<h1>单字段检索</h1>
<p style="color: red">说明:若未选择搜索字段，則默认在电影简介中搜索!</p>
<form action="main_single.jsp" method="GET">
    <select name="select_t">
        <option>--请选择搜索字段--</option>
        <option value="name">搜片名</option>
        <option value="actor">搜主演</option>
        <option value="type">搜类型</option>
        <option value="shortcut">搜简介</option>
    </select>
    <input type="text" name="name">
    <input type="submit" value="GO">
</form>
<h1>多字段检索</h1>
<p style="color: red">说明:若未选择字段之间的逻辑关系，则默认为逻辑或；如果某个字段不填写，则说明对该字段没有要求!</p>
<form action="main_mult.jsp" method="GET">
    <select name="and_or">
        <option>--请选择逻辑关系--</option>
        <option value="and">逻辑与</option>
        <option value="or">逻辑或</option>
    </select><br><br>

    <label>主演:</label><input type="text" name="actor"><br><br>
    <label>类型:</label><input type="text" name="type"><br><br>
    <label>简介:</label><input type="text" name="shortcut"><br><br>
    <label>片名:</label><input type="text" name="name"><br><br>
    <input type="submit" value="GO">
</form>


</body>
</html>
