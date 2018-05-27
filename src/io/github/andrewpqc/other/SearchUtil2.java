package io.github.andrewpqc.other;



import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.io.ByteOrderMark;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

/**
 * 对于中文来说，Lucene提供的search基本上不能使用，使用中文分词器替换即可
 * @author Johnny
 *
 */
public class SearchUtil2 {
    private Version Lucene_Version = Version.LUCENE_4_10_4;
    private Directory directory;
    private DirectoryReader reader = null;

    private String[] ids = { "1", "2", "3", "4", "5", "6" };
    private String[] emails = { "aa@itat.org", "bb@itat.org", "cc@cc.org", "dd@sina.org", "ee@zttc.edu",
            "ff@itat.org" };
    private String[] contents = { "welcome to visited the space,I like book java", "hello boy, I like pingpeng ball",
            "my name is cc I like game java", "I like football", "I like football and I like basketball too",
            "I like movie and swim java" };
    private Date[] dates = null;
    private int[] attachs = { 2, 3, 1, 4, 5, 5 };
    private String[] names = { "zhangsan", "lisi", "john", "jetty", "mike", "jake" };

    public SearchUtil2() {
        // directory = new RAMDirectory();
        try {
            directory = FSDirectory.open(new File("/home/andrew/IdeaProjects/app1/index1"));
            setDates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dates = new Date[ids.length];
            dates[0] = sdf.parse("2010-02-19");
            dates[1] = sdf.parse("2012-01-11");
            dates[2] = sdf.parse("2011-09-19");
            dates[3] = sdf.parse("2010-12-22");
            dates[4] = sdf.parse("2012-01-01");
            dates[5] = sdf.parse("2011-05-19");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void index() {
        IndexWriter writer = null;
        try {
            writer = new IndexWriter(directory, new IndexWriterConfig(Lucene_Version, new StandardAnalyzer()));
            //writer.deleteAll();
            Document doc = null;
            for (int i = 0; i < ids.length; i++) {
                doc = new Document();
                doc.add(new StringField("id", ids[i], Store.YES));
                doc.add(new StringField("email", emails[i], Store.YES));
                doc.add(new TextField("content", contents[i], Store.NO));
                doc.add(new StringField("name", names[i], Store.YES));
                //存储数字
                doc.add(new IntField("attach", attachs[i], Store.YES));
                //存储日期
                doc.add(new LongField("date", dates[i].getTime(), Store.YES));

                String et = emails[i].substring(emails[i].lastIndexOf("@") + 1);
                System.out.println(et);
                /**
                 * 在Lucene4.x中，只能给域加权，部门给文档加权，如果要提高文档的加权，需要给
                 * 文档的每个域进行加权
                 * **/

                writer.addDocument(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public IndexSearcher getSearcher() {
        try {
            if (reader == null) {
                reader = DirectoryReader.open(directory);
            } else {
                DirectoryReader tr = DirectoryReader.openIfChanged(reader);
                if (tr != null) {
                    reader.close();
                    reader = tr;
                }
            }
            return new IndexSearcher(reader);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void searchByTermRange(String field, String start, String end, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            Query query = new TermRangeQuery(field, new BytesRef(start.getBytes()), new BytesRef(end.getBytes()), true,
                    true);
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了:" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println(doc.get("id") + "---->" + doc.get("name") + "[" + doc.get("email") + "]-->"
                        + doc.get("id") + "," + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByQueryParse(Query query, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了:" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println(doc.get("id") + "---->" + doc.get("name") + "[" + doc.get("email") + "]-->"
                        + doc.get("id") + "," + doc.get("attach") + "," + doc.get("date") + "==" + sd.score);
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}