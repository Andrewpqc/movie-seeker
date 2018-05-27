package io.github.andrewpqc.other;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
//import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

public class SearchTest2 {

    private SearchUtil2 su = null;

    @Before
    public void init() {
        su = new SearchUtil2();
    }

    @Test
    public void testInex() {
        su.index();
    }


    @Test
    public void testSearchByTermRange() {
        //查询name以a开头和s结尾的
        su.searchByTermRange("name", "a", "s", 10);

        //由于attachs是数字类型，使用TermRange无法查询
        System.out.println("------------");
        su.searchByTermRange("attach", "2", "10", 5);
    }

    @Test
    public void testSearchByQueryParse() throws Exception {
        //1、创建QueryParser对象,默认搜索域为content
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        //改变空格的默认操作符，以下可以改成AND
        //parser.setDefaultOperator(Operator.AND);
        //开启第一个字符的通配符匹配，默认关闭因为效率不高
        parser.setAllowLeadingWildcard(true);
        //搜索content中包含有like的
        Query query = parser.parse("like");

        //有basketball或者football的，空格默认就是OR
        //query = parser.parse("basketball football");

        //改变搜索域为name为mike
        //query = parser.parse("content:like");

        //同样可以使用*和?来进行通配符匹配
        // query = parser.parse("name:j*");

        //query = parser.parse("email:*@itat.org");

        //匹配name中没有mike但是content中必须有pingpeng的，+和-要放置到域说明前面
        query = parser.parse("-name:mike +like +pingpeng");

        //匹配一个区间，注意:TO必须是大写
        //query = parser.parse("id:[1 TO 6]");

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
        su.searchByQueryParse(query, 10);
    }


}