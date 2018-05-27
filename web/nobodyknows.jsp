<%--
  Created by IntelliJ IDEA.
  User: andrew
  Date: 18-5-26
  Time: 下午1:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="io.github.andrewpqc.main.SearchUtil" %>
<html>
<head>
    <title>Nobody Know This File</title>
</head>
<body>
<%
    SearchUtil su=new SearchUtil();
    su.index();
%>
</body>
</html>
