/**
 * 巨坑:需要搜索的字段不能用StringField,不然搜索不到,
 * 需要使用TextField
 **/

package io.github.andrewpqc.main;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;

import java.io.IOException;

public class Test{
    static SearchUtil su =new SearchUtil();

    public static void main(String[] a){
        IndexSearcher indexSearcher=su.getSearcher();
        QueryParser queryParser = new QueryParser(Version.LUCENE_4_10_4, "name", new StandardAnalyzer());
        // 创建Query表示搜索域为content包含Darren的文档
        try {
            Query query = queryParser.parse("爱情");
            TopDocs tds = indexSearcher.search(query, 5);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = indexSearcher.doc(sd.doc);
                // 8、根据Document对象获取需要的值
                System.out.println(doc.get("name"));
            }
        }catch (ParseException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}

