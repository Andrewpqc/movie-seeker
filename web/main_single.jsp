<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="org.apache.lucene.document.Document" %>
<%@ page import="io.github.andrewpqc.main.SearchUtil" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>搜索结果</title>
</head>
<body>
    <%
        SearchUtil su=new SearchUtil();
        String keyword=request.getParameter("name");
        String option=request.getParameter("select_t");
        System.out.println(option);
        if(option.equals("--请选择搜索字段--")){//默認在電影簡介中搜索
            option="shortcut";
        }
        Vector documents=new Vector();
        try {
            documents= su.searchByTerm(option,keyword,100);
        }catch(Exception e){
            e.printStackTrace();
        }
        Iterator<Document> iter=documents.iterator();
        if(documents.size()==0){
            out.println("没有找到你想要的内容!");
        }
        while(iter.hasNext()){
            Document document=iter.next();
    %>
<ul>
    <h3><li><%=document.get("name")%></li></h3>
    <li><label>编剧:</label><%=document.get("screenwriter")%></li>
    <li><label>主演:</label><%=document.get("actor")%></li>
    <li><label>类型:</label><%=document.get("type")%></li>
    <li><label>国家/地区:</label><%=document.get("country")%></li>
    <li><label>上映时间:</label><%=document.get("displaytime")%></li>
    <li><label>概要:</label><%=document.get("shortcut")%></li>
    <li><label>豆瓣评分:</label><%=document.get("score")%></li>
    <a href="<%=document.get("url")%>" target="_blank">去豆瓣看详情</a>
</ul>
    <%}%>

</body>
</html>