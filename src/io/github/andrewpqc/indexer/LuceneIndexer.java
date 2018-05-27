package io.github.andrewpqc.indexer;
//连接数据库建立索引
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.sql.*;


public class LuceneIndexer {
    private static Directory directory = null;
    private static IndexWriter indexWrite = null;

    public static void createIndexWriter(){
        //指定索引分词技术，这里使用的是标准分词
        Analyzer analyzer = new StandardAnalyzer();
        //indexwriter 配置信息
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
        //索引的打开方式，没有索引文件就新建，有就打开
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

        try {
            //指定索引硬盘存储路径
            directory = FSDirectory.open(new File("./index"));
            //如果索引处于锁定状态，则解锁
            if (IndexWriter.isLocked(directory)){
                IndexWriter.unlock(directory);
            }
            //指定所以操作对象indexWrite
            indexWrite = new IndexWriter(directory, indexWriterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        createIndexWriter();
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ee){
            ee.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://120.77.220.239:32772/Test", "searchenginedbuser","searchenginepassword");
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("select * from movieinfo limit 300");
            while (result.next()) {
                Document doc = new Document();
                doc.add(new TextField("url", result.getString("url"), Store.YES));
                doc.add(new TextField("name", result.getString("name"), Store.YES));
                doc.add(new TextField("Screenwriter", result.getString("Screenwriter"), Store.YES));
                doc.add(new TextField("actor", result.getString("actor"), Store.YES));
                doc.add(new TextField("type", result.getString("type"), Store.YES));
                doc.add(new TextField("country", result.getString("country"), Store.YES));
                doc.add(new TextField("displaytime", result.getString("othername"), Store.YES));
                doc.add(new TextField("score", result.getString("score"), Store.YES));
                doc.add(new TextField("shortcut", result.getString("shortcut"), Store.YES));
                indexWrite.addDocument(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
        }
        try {
            //这一步很消耗系统资源，所以commit操作需要有一定的策略
            indexWrite.commit();
            //indexWrite.optimize();// no optimize method is found.
            //关闭资源
            indexWrite.close();
            directory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
