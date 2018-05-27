package io.github.andrewpqc.main;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import org.apache.commons.io.ByteOrderMark;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
//import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.BooleanQuery;
//import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.NumericRangeQuery;
//import org.apache.lucene.search.PhraseQuery;
//import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.search.WildcardQuery;
//import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class SearchUtil {
    private Version Lucene_Version = Version.LUCENE_4_10_4;
    private Directory directory = null;
    private DirectoryReader reader = null;

    //构造函数
    public SearchUtil() {
        try {
            directory = FSDirectory.open(new File("/index"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //建立索引
    public void index() {
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(directory, new IndexWriterConfig(Lucene_Version, new StandardAnalyzer()));
            Document doc = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ee) {
                ee.printStackTrace();
            }
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://120.77.220.239:32772/Test", "searchenginedbuser", "searchenginepassword");
                Statement stat = conn.createStatement();
                ResultSet result = stat.executeQuery("select * from movieinfo");
                while (result.next()) {
                    doc = new Document();
                    doc.add(new StringField("url", result.getString("url"), Store.YES));
                    doc.add(new TextField("name", result.getString("name"), Store.YES));
                    doc.add(new TextField("Screenwriter", result.getString("Screenwriter"), Store.YES));
                    doc.add(new TextField("actor", result.getString("actor"), Store.YES));
                    doc.add(new TextField("type", result.getString("type"), Store.YES));
                    doc.add(new TextField("country", result.getString("country"), Store.YES));
                    doc.add(new TextField("displaytime", result.getString("othername"), Store.YES));
                    doc.add(new TextField("score", result.getString("score"), Store.YES));
                    doc.add(new TextField("shortcut", result.getString("shortcut"), Store.YES));
                    indexWriter.addDocument(doc);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (indexWriter != null) {
                    indexWriter.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //获得IndexSearch对象
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


    public Vector searchByTerm(String field, String keyword, int num) {
        Vector documents = new Vector();
        try {
            IndexSearcher searcher = getSearcher();
            QueryParser queryParser = new QueryParser(Lucene_Version, field, new StandardAnalyzer());
            // 创建Query表示搜索域为content包含Darren的文档
            Query query = queryParser.parse(keyword);
            TopDocs tds = searcher.search(query, num);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                documents.add(doc);
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public Vector searchByTermRange(String field, String start, String end, int num) {
        Vector documents = new Vector();
        try {
            IndexSearcher searcher = getSearcher();
            Query query = new TermRangeQuery(field, new BytesRef(start.getBytes()), new BytesRef(end.getBytes()), true,
                    true);
            TopDocs tds = searcher.search(query, num);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                documents.add(doc);
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public Vector searchByQueryParse(Query query, int num) {
        Vector documents = new Vector();
        try {
            IndexSearcher searcher = getSearcher();
            TopDocs tds = searcher.search(query, num);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                documents.add(doc);
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }


    /***如果想要获取未存储到索引中得值，可以根据ID去源文件中进行查找并返回**/
    public Vector searchPage(String query, int pageIndex, int pageSize) {
        Vector documents = new Vector();
        try {
            IndexSearcher searcher = getSearcher();
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query q = null;
            try {
                q = parser.parse(query);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                e.printStackTrace();
            }
            TopDocs tds = searcher.search(q, 500);
            ScoreDoc[] sds = tds.scoreDocs;
            int start = (pageIndex - 1) * pageSize;
            int end = pageIndex * pageSize;
            if (end >= sds.length)
                end = sds.length;
            for (int i = start; i < end; i++) {
                Document doc = searcher.doc(sds[i].doc);
                documents.add(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }

    /**
     * 根据页码和分页大小获取上一次的最后一个ScoreDoc
     */
    private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher) throws IOException {
        if (pageIndex == 1)
            return null;//如果是第一页就返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的数量
        TopDocs tds = searcher.search(query, num);
        return tds.scoreDocs[num - 1];
    }


    public Vector searchPageByAfter(String keyword, int pageIndex, int pageSize, String field) {
        Vector documents = new Vector();
        try {
            IndexSearcher searcher = getSearcher();
            QueryParser parser = new QueryParser(field, new StandardAnalyzer());
            Query q = null;
            try {
                q = parser.parse(keyword);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                e.printStackTrace();
            }
            //先获取上一页的最后一个元素
            ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher);
            //通过最后一个元素搜索下页的pageSize个元素
            TopDocs tds = searcher.searchAfter(lastSd, q, pageSize);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                documents.add(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }
}
