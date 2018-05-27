<%@ page import="org.apache.lucene.search.Query" %>
<%@ page import="org.apache.lucene.document.Document" %>
<%@ page import="org.apache.lucene.search.BooleanClause" %>
<%@ page import="org.apache.lucene.queryparser.classic.MultiFieldQueryParser" %>
<%@ page import="org.apache.lucene.analysis.standard.StandardAnalyzer" %>
<%@ page import="io.github.andrewpqc.main.SearchUtil" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Iterator" %><%--
  Created by IntelliJ IDEA.
  User: andrew
  Date: 18-1-14
  Time: 下午10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>搜索结果</title>
</head>
<body>
<%
    SearchUtil su=new SearchUtil();
    Vector documents=null;
    String and_or=request.getParameter("and_or");
    String name=request.getParameter("name");
    String actor=request.getParameter("actor");
    String type=request.getParameter("type");
    String shortcut=request.getParameter("shortcut");
    //如果未選擇，則默認爲or
    /**
     * BooleanClause.Occur.SHOULD  表示or
     *
     * BooleanClause.Occur.MUST  表示and
     *
     * BooleanClause.Occur.MUST_NOT  表示not
     */
    int total=0;
    if(!name.equals("")){
        total++;
    }
    if(!actor.equals("")) {
        total++;
    }
    if(!type.equals("")){
        total++;
    }
    if(!shortcut.equals("")){
        total++;
    }
    int f=0;
    if(and_or.equals("and")){//用户选择的是逻辑与

        String[] queries = new String[total];
        String[] fields = new String[total];
        BooleanClause.Occur[] clauses = new BooleanClause.Occur[total];
        if(!name.equals("")){
            queries[f]=name;
            fields[f++]="name";
        }

        if(!actor.equals("")){
            queries[f]=actor;
            fields[f++]="actor";
        }

        if(!type.equals("")){
            queries[f]=type;
            fields[f++]="type";
        }

        if(!shortcut.equals("")){
            queries[f]=shortcut;
            fields[f++]="shortcut";
        }
        f=0;
        for(int i=0;i<queries.length;i++)
            clauses[i]= BooleanClause.Occur.MUST;

        Query query= MultiFieldQueryParser.parse(queries,fields,clauses,new StandardAnalyzer());
        documents=su.searchByQueryParse(query,15);
        System.out.println("and and and and ");

    }
//    else if(and_or.equals("or")){
    else{

        String[] queries = new String[total];
        String[] fields = new String[total];
        BooleanClause.Occur[] clauses = new BooleanClause.Occur[total];

        if(!name.equals("")){
            queries[f]=name;
            fields[f++]="name";
        }

        if(!actor.equals("")){
            queries[f]=actor;
            fields[f++]="actor";
        }

        if(!type.equals("")){
            queries[f]=type;
            fields[f++]="type";
        }

        if(!shortcut.equals("")){
            queries[f]=shortcut;
            fields[f++]="shortcut";
        }
        f=0;

        for(int i=0;i<queries.length;i++)
            clauses[i]= BooleanClause.Occur.SHOULD;


        Query query= MultiFieldQueryParser.parse(queries,fields,clauses,new StandardAnalyzer());

        documents=su.searchByQueryParse(query,15);
        System.out.println("orororor,default");
    }
    Iterator<Document> iter=documents.iterator();
    if(documents.size()==0){
        out.println("未找到你需要的內容!");
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
