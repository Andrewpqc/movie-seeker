package io.github.andrewpqc.retrieval;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.util.Vector;


public class LuceneRetrieval {

    public static Vector search_by_name(String name) throws Exception{
        IndexReader indexReader = null;
        // 1、创建Directory
        Directory directory = FSDirectory.open(new File("./index"));
        // 2、创建IndexReader
        indexReader = IndexReader.open(directory);
        // 3、根据IndexReader创建IndexSearch
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 4、创建搜索的Query
        // 使用默认的标准分词器
        Analyzer analyzer = new StandardAnalyzer();


        // 单字段
        // 在content中搜索重要程度
        // 创建parser来确定要搜索文件的内容，第二个参数为搜索的域
        QueryParser queryParser = new QueryParser(Version.LUCENE_4_10_4, "name", analyzer);
        // 创建Query表示搜索域为content包含Darren的文档
        Query query = queryParser.parse(name);

        // 多字段
        String[] queries = {"余弦值","相似程度"};
        String[] fields = {"name","content"};
        BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
        Query query1 = MultiFieldQueryParser.parse(queries,fields,clauses,new StandardAnalyzer());

        // 5、根据searcher搜索并且返回TopDocs
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        Vector documents=new Vector();
        for (ScoreDoc scoreDoc:scoreDocs) {
            // 7、根据searcher和ScoreDoc对象获取具体的Document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            // 8、根据Document对象获取需要的值
            documents.add(document);
        }
        return documents;
    }


    public static void main(String[] args) throws Exception{
        IndexReader indexReader = null;
        // 1、创建Directory
        Directory directory = FSDirectory.open(new File("/home/andrew/IdeaProjects/app1/index"));
        // 2、创建IndexReader
        indexReader = IndexReader.open(directory);
        // 3、根据IndexReader创建IndexSearch
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 4、创建搜索的Query
        // 使用默认的标准分词器
        Analyzer analyzer = new StandardAnalyzer();


        // 单字段
        // 在content中搜索重要程度
        // 创建parser来确定要搜索文件的内容，第二个参数为搜索的域
        QueryParser queryParser = new QueryParser(Version.LUCENE_4_10_4, "content", analyzer);
        // 创建Query表示搜索域为content包含Darren的文档
        Query query = queryParser.parse("计算");

        // 多字段
        String[] queries = {"余弦值","相似程度"};
        String[] fields = {"name","content"};
        BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
        Query query1 = MultiFieldQueryParser.parse(queries,fields,clauses,new StandardAnalyzer());

        // 5、根据searcher搜索并且返回TopDocs
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 6、根据TopDocs获取ScoreDoc对象
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 7、根据searcher和ScoreDoc对象获取具体的Document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            // 8、根据Document对象获取需要的值
            System.out.println(document.get("name"));
        }
    }
}