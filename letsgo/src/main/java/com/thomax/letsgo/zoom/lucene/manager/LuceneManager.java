package com.thomax.letsgo.zoom.lucene.manager;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class LuceneManager {
	
	private static String myIndex;
	private String searchDocument;
	
	public LuceneManager() {
		String workPath = System.getProperty("user.dir");
		workPath = workPath.substring(0, workPath.lastIndexOf(File.separator));
		myIndex = workPath + File.separator + "resource" + File.separator + "myIndex";
		searchDocument = workPath + File.separator + "resource" + File.separator + "search_document";
	}
	
	//创建索引
	public void createIndex() throws IOException {
		IndexWriter writer = LuceneTool.getIndexWriter();
		//读取search_document下的文件并遍历成File 对象(原始文档)
		File searchDocs = new File(searchDocument);
		File[] files = searchDocs.listFiles();
		
		for (File file : files) {
			String fileName = file.getName();
			String filePath = file.getAbsolutePath();
			String fileContext = FileUtils.readFileToString(file);
			long fileSize = FileUtils.sizeOf(file);
			
			//定义文件名域(分析、索引、存储)
			Field fileNameField = new TextField("fileNameField", fileName, Store.YES);
			//定义文件路径域(不分析，不索引，必须存储)
			Field filePathField = new StoredField("filePathField", filePath);
			//定义文件内容域（分析、索引、存储）
			Field fileContextField = new TextField("fileContextField", fileContext, Store.YES);
			//定义文件大小域（分析、索引、存储）
			//Field fileSizeField = new LongField("fileSizeField", fileSize, Store.YES);
			
			Document document = new Document();
			document.add(fileNameField);
			document.add(filePathField);
			document.add(fileContextField);
			//document.add(fileSizeField);
			writer.addDocument(document);
		}
		writer.close();
	}
	
	//查看标准分析器的分词效果
	public void testTokenStream() throws IOException {
		//创建一个标准分析器对象
		Analyzer analyzer = new StandardAnalyzer();
		//获得tokenStream 对象。第一个参数：域名，可以随便给一个；第二个参数：要分析的文本内容
		TokenStream tokenStream = analyzer.tokenStream("test03", "The Spring Framework provides a comprehensive programming and configuration model.");
		//添加一个引用，可以获得每个关键词
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		//添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		//将指针调整到列表的头部
		tokenStream.reset();
		//遍历关键词列表，通过incrementToken 方法判断列表是否结束
		while(tokenStream.incrementToken()) {
			//关键词的起始位置
			System.out.println("start->" + offsetAttribute.startOffset());
			//取关键词
			System.out.println(charTermAttribute);
			//结束位置
			System.out.println("end->" + offsetAttribute.endOffset());
			System.out.println();
			}
		tokenStream.close();
		analyzer.close();
	}
	
	//使用IK分词器
	public void topkenStream() throws IOException {
		Analyzer analyzer = new IKAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("test04", "张学友可以用二维表结构来逻辑表达实现的数据");
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			System.out.println("start->" + offsetAttribute.startOffset());
			System.out.println(charTermAttribute);
			System.out.println("end->" + offsetAttribute.endOffset());
			System.out.println();
		}
		tokenStream.close();
		analyzer.close();
	}
	
	//添加索引库
	public void addIndexLib() throws IOException {
		IndexWriter writer = LuceneTool.getIndexWriter();
		Document document = new Document();
		document.add(new TextField("fnField", "新添加的文件名域", Store.YES));
		document.add(new TextField("fcField", "新添加的文件内容域", Store.YES));
		writer.addDocument(document);
		writer.close();
	}
	
	//修改索引库文档（先删除再添加,在删除库中索引的序号仍保留）
	public void updateIndexLib() throws IOException {
		IndexWriter writer = LuceneTool.getIndexWriter();
		Term term = new Term("fileNameField", "apache");
		Document document = new Document();
		document.add(new TextField("AA_Field", "修改文档时的AA域的内容", Store.YES));
		document.add(new TextField("BB_Field", "修改文档时的BB域的内容", Store.YES));
		writer.updateDocument(term, document);
		writer.close();
	}
	
	//删除满足条件的索引库文档
	public void deleteIndexLib() throws IOException {
		IndexWriter writer = LuceneTool.getIndexWriter();
		Term term = new Term("fileContextField", "mybatis");
		writer.deleteDocuments(term);
		writer.close();
	}
	
	//删除所有索引库文档
	public void deleteAllIndexLib() throws IOException {
		IndexWriter writer = LuceneTool.getIndexWriter();
		writer.deleteAll();
		writer.close();
	}
	
	//查询所有
	public void FindAllDocs() throws IOException {
		IndexSearcher searcher = LuceneTool.getIndexSearcher();
		Query query = new MatchAllDocsQuery();
		LuceneTool.printRS(searcher, query);
		searcher.getIndexReader().close();
	}
	
	//TermQuery查询
	public void findDocsByTermQuery() throws IOException {
		IndexSearcher searcher = LuceneTool.getIndexSearcher();
		TermQuery query = new TermQuery(new Term("fileNameField", "apache"));
		LuceneTool.printRS(searcher, query);
	}
	
	//NumericRangeQuery查询
	public void findDocsByNumericRangeQuery() throws IOException {
		IndexSearcher searcher = LuceneTool.getIndexSearcher();
		//参数1：代表查询的域名，参数2：代表查询的最小值，参数3：代表查询的最大值，参数4：代表有无包含最小值，参数5：有无包含最大值
		/* 5.0版本以后取消掉了NumericRangeQuery
		NumericRangeQuery<Long> query = NumericRangeQuery.newLongRange("fileSizeField", 50L, 100L, true, false);
		LuceneTool.printRS(searcher, query);
		*/
		searcher.getIndexReader().close();
	}
	
	//BooleanQuery查询
	public void findDocsByBooleanQuery() throws IOException {
		IndexSearcher searcher = LuceneTool.getIndexSearcher();
		TermQuery termQuery1 = new TermQuery(new Term("fileNameField", "lucene"));
		TermQuery termQuery2 = new TermQuery(new Term("fileContextField", "lucene"));

		BooleanClause bc1 = new BooleanClause(termQuery1, Occur.SHOULD); //Occur.SHOULD：应该满足，但是不满足也可以，相当于or
		BooleanClause bc2 = new BooleanClause(termQuery2, Occur.MUST); //Occur.MUST：必须满足此条件，相当于and； Occur.MUST_NOT 查询条件不能满足，相当于not 非
		BooleanQuery boolQuery = new BooleanQuery.Builder().add(bc1).add(bc2).build();
		LuceneTool.printRS(searcher, boolQuery);
		searcher.getIndexReader().close();
	}
	
	//QueryParser查询
	public void findDocsByQueryParser() throws IOException, ParseException {
		IndexSearcher searcher = LuceneTool.getIndexSearcher();
		QueryParser parser = new QueryParser("fileContextField", new IKAnalyzer());
		//通过parser 对象得到query 对象(此例会对下面这句话进行分词解析，其语汇单元为：lucene java development)
		Query query = parser.parse("lucene is Java development");
		LuceneTool.printRS(searcher, query);
		searcher.getIndexReader().close();
	}
	
	/*通过QueryParser结合简易语法进行分析查询：
	1、基础的查询语法，关键词查询：域名+“:”+搜索的关键字
	 	例如：content:java
	2、范围查询域名+“:”+[最小值TO 最大值]
		例如：size:[1 TO 1000] 范围查询在lucene 中支持数值类型，不支持字符串类型。在solr 中支持字符串类型
	3、组合条件查询
		1）+条件1 +条件2：两个条件之间是并且的关系and
		  例如：+filename:apache +content:apache
		2）+条件1 条件2：必须满足第一个条件，应该满足第二个条件
		  例如：+filename:apache content:apache
		3）条件1 条件2：两个条件满足其一即可
		  例如：filename:apache content:apache
		4）-条件1 条件2：必须不满足条件1
		
		第二种写法：
		  条件1 AND 条件2
		  条件1 OR 条件2
		  条件1 NOT 条件2
	*/
	public void findDocsByQueryParser2() throws IOException, ParseException {
		IndexSearcher searcher = LuceneTool.getIndexSearcher();
		QueryParser parser = new QueryParser("fileContextField", new IKAnalyzer());
		//通过parser 对象得到query 对象(此例会对下面这句话进行分词解析，其语汇单元为：lucene java development)
		Query query = parser.parse("fileNameField:lucene fileContextField:lucene");
		LuceneTool.printRS(searcher, query);
		searcher.getIndexReader().close();
	}
	
	//MultiFieldQueryParser查询
	public void findDocsByMultiFieldQueryParser() throws IOException, ParseException {
		IndexSearcher searcher = LuceneTool.getIndexSearcher();
		String[] fields = {"fileNameField", "fileContextField"};
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new IKAnalyzer());
		Query query = parser.parse("lucene is Java");
		LuceneTool.printRS(searcher, query);
		searcher.getIndexReader().close();
	}
	
    /*-------------------------------------------
    |                  T O O L                  |
    ============================================*/
	private static class LuceneTool {
		
		//根据索引查询器及query对象打印查询到的内容
		private static void printRS(IndexSearcher searcher, Query query) throws IOException {
			//根据搜索器对象及查询对象得到前10条记录(其中TopDocs中存放的是文档的id),这里的10代表只显示的记录条数
			TopDocs docs = searcher.search(query , 10);
			//测试查询的条数（总共有n条）
			long totalHits = docs.totalHits;
			System.out.println("满足条件的有：" + totalHits + "条!\n");
			//6.遍历前10条记录
			for(ScoreDoc sd : docs.scoreDocs) {
				//得到文档的id
				int docID = sd.doc;
				//利用搜索器，传递文档的id，查询出对应的文档对象
				Document doc = searcher.doc(docID);
				//文件名域
				IndexableField fileNameField = doc.getField("fileNameField");
				//文件内容域
				//IndexableField fileContentField = doc.getField("fileContextField");
				//文件路径域
				IndexableField filePathField = doc.getField("filePathField");
				//文件大小域
				//IndexableField fileSizeField = doc.getField("fileSizeField");
				//输出内容
				System.out.println("文件名：" + fileNameField.stringValue());
				//System.out.println("文件内容：" + fileContentField.stringValue());
				System.out.println("文件路径：" + filePathField.stringValue());
				System.out.println("文件大小：" + doc.get("fileSizeField"));  //可以传递key(field的名称)得到值
				System.out.println("-------------------------------------------------");
			}
		}
		
		private static IndexSearcher getIndexSearcher() throws IOException {
			//获取索引库所在的目录对象
			Directory directory = FSDirectory.open(Paths.get(myIndex));
			//根据目录对象得到相应的输入流
			IndexReader reader = DirectoryReader.open(directory );
			//根据输入流对象构建索引搜索器对象
			return new IndexSearcher(reader );
		}
		
		private static IndexWriter getIndexWriter() throws IOException {
			//定义索引库的存放目录
			Directory directory = FSDirectory.open(Paths.get(myIndex));
			//构建分词器(分析器) ,StandardAnalyzer 是标准分词器，官方建议使用的
			Analyzer analyzer = new StandardAnalyzer();
			//创建索引输出流的配置
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			//构造IndexWriter 输出流对象
			IndexWriter writer = new IndexWriter(directory, config);
			
			return writer;
		}
		
	}

}







