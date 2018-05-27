package io.github.andrewpqc.main;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Vector;

public class SearchTest{
    private SearchUtil su = null;

    @Before
    public void init() {
        su = new SearchUtil();

    }

    @Test
    public void index(){
        su.index();
    }

    @Test
    public void testSearchByTerm(){
        Vector documents;
        documents= su.searchByTerm("name", "肖生克的救赎", 10);
        Iterator<Document> iterator=documents.iterator();
        System.out.println(documents.size());
        while(iterator.hasNext()){
            Document document = iterator.next();
            System.out.println(document.get("name"));
        }
    }

    @Test
    public void testSearchByTermRange() {
        Vector  documents= new Vector();
        //查询name以a开头和s结尾的
        documents=su.searchByTermRange("score", "5", "10", 10);
        System.out.println(documents.size());

//        //由于attachs是数字类型，使用TermRange无法查询
//        System.out.println("------------");
//        su.searchByTermRange("score", "2", "10", 5);
    }

    @Test
    public void testSearchByQueryParse() throws Exception {
        //1、创建QueryParser对象,默认搜索域为content
        QueryParser parser = new QueryParser("score", new StandardAnalyzer());
        //改变空格的默认操作符，以下可以改成AND
//        parser.setDefaultOperator(Operator.AND);
        //开启第一个字符的通配符匹配，默认关闭因为效率不高
        parser.setAllowLeadingWildcard(true);
        //搜索content中包含有like的
//        Query query = parser.parse("爱情");

        //有basketball或者football的，空格默认就是OR
//        query = parser.parse("basketball football");

        //改变搜索域为name为mike
        //query = parser.parse("content:like");

        //同样可以使用*和?来进行通配符匹配
        // query = parser.parse("name:j*");

        //query = parser.parse("email:*@itat.org");

        //匹配name中没有mike但是content中必须有pingpeng的，+和-要放置到域说明前面
//        Query query = parser.parse("-name:mike +like +pingpeng");

        //匹配一个区间，注意:TO必须是大写
        Query query = parser.parse("score:[0 TO 10]");

        //闭区间匹配只会匹配到2
        //query = parser.parse("id:{1 TO 3}");

        //完全匹配I Like Football的
        //query = parser.parse(""I like football"");

        //匹配I 和football之间有一个单词距离的
        //query = parser.parse(""I football"~1");

        //模糊查询
        //query = parser.parse("name:make~");

        //没有办法匹配数字范围（自己扩展Parser）
        //query = parser.parse("attach:[2 TO 10]");

        Vector documents=new Vector();
        documents=su.searchByQueryParse(query, 10);
        Iterator<Document> iterator=documents.iterator();
        System.out.println(documents.size());
        while(iterator.hasNext()) {
            Document document = iterator.next();
            System.out.println(document.get("score"));
        }
    }

    @Test
    public void testSearchPage(){
        su.searchPage("java", 1, 20);
    }

    @Test
    public void testSearchPageByAfter(){
        Vector documents=new Vector();
        documents=su.searchPageByAfter("爱情", 1, 20,"shortcut");
        System.out.println(documents.size());
        Iterator<Document> iterator=documents.iterator();
        while(iterator.hasNext()) {
            Document document = iterator.next();
            System.out.println(document.get("name"));
        }
    }
}
