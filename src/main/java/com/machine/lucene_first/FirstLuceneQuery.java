package com.machine.lucene_first;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;

public class FirstLuceneQuery {

    public static void main(String[] args) throws Exception {
        // 索引目录对象
        Directory directory = FSDirectory.open(Paths.get("out", "index_dir"));

        // 索引读取工具
        IndexReader reader = DirectoryReader.open(directory);

        // 索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);

        // 创建查询解析器,两个参数：默认要查询的字段的名称，分词器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());

        // 创建查询对象
        Query query = parser.parse("第一次lucene");

        // Term(词条)是搜索的最小单位，不可再分词
        TermQuery termQuery = new TermQuery(new Term("title" ,"lucene"));

        // 通配符查询
        Query wildcardQuery = new WildcardQuery(new Term("title", "*lucene*"));

        // 模糊查询 maxEdits 取值0～2
        Query fuzzyQuery = new FuzzyQuery(new Term("title","lucenes"),1);

        /*// 数值范围查询对象，参数：字段名称，最小值、最大值、是否包含最小值、是否包含最大值
        Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);

        // 创建布尔查询的对象
        BooleanQuery booleanQuery = new BooleanQuery();
        // 组合其它查询
        booleanQuery.add(fuzzyQuery, BooleanClause.Occur.MUST_NOT);
        booleanQuery.add(wildcardQuery, BooleanClause.Occur.SHOULD);*/

        // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
        // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
        TopDocs topDocs = searcher.search(fuzzyQuery, 10);

        // 获取总条数
        System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");

        // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取出文档编号
            int docID = scoreDoc.doc;
            // 根据编号去找文档
            Document doc = reader.document(docID);
            System.out.println("id: " + doc.get("id"));
            System.out.println("title: " + doc.get("title"));
            // 取出文档得分
            System.out.println("得分： " + scoreDoc.score);
        }
    }

}
