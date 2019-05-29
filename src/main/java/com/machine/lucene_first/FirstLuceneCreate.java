package com.machine.lucene_first;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;

public class FirstLuceneCreate {

    public static void main(String[] args) throws Exception {

        // 创建文档
        Document document = new Document();

        // 添加文档属性，Field.Store.YES表示存储到文档列表，后续需要做展示用，StringField会创建索引不会分词
        document.add(new StringField("id", "1", Field.Store.YES));

        // TextField 即创建索引也会分词
        document.add(new TextField("title", "初识lucene，使用lucene创建第一个demo", Field.Store.YES));

        // 创建索引目录
        Directory directory = FSDirectory.open(Paths.get("out", "index_dir"));

        // 创建标准分词器
        // Analyzer analyzer = new StandardAnalyzer();

        // 创建IK分词器
        Analyzer analyzer = new IKAnalyzer();

        //4 索引写出工具的配置对象
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //5 创建索引的写出工具类。参数：索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, conf);

        //6 把文档交给IndexWriter
        indexWriter.addDocument(document);

        //7 提交
        indexWriter.commit();

        //8 关闭
        indexWriter.close();


    }

}
