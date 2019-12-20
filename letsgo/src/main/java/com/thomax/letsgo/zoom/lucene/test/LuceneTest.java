package com.thomax.letsgo.zoom.lucene.test;

import com.thomax.letsgo.zoom.lucene.manager.LuceneManager;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LuceneTest {
	
	private LuceneManager manager;
	
	@Before
	public void init() {
		manager = new LuceneManager();
	}
	
	@Test
	//测试创建索引库
	public void test01() throws IOException {
		manager.createIndex();
		System.out.println("创建索引库成功！");
	}
	
	@Test
	//测试标准分析器的分词效果
	public void test02() throws IOException {
		manager.testTokenStream();
	}
	
	@Test
	//测试IK分词器
	public void test03() throws IOException {
		manager.topkenStream();
	}
	
	@Test
	//测试添加索引库
	public void test04() throws IOException {
		manager.addIndexLib();
		System.out.println("添加到索引库成功！");
	}
	
	@Test
	//测试修改索引库文档
	public void test05() throws IOException {
		manager.updateIndexLib();
		System.out.println("修改索引库文档成功！");
	}
	
	@Test
	//测试删除索引库文档
	public void test06() throws IOException {
		manager.deleteIndexLib();
		System.out.println("删除索引库文档成功！");
	}
	
	@Test
	//测试删除所有索引库文档
	public void test07() throws IOException {
		manager.deleteAllIndexLib();
		System.out.println("删除所有索引库文档成功！");
	}
	
	@Test
	//测试查询所有
	public void test08() throws IOException {
		manager.FindAllDocs();
	}
	
	@Test
	//测试TermQuery查询
	public void test09() throws IOException {
		manager.findDocsByTermQuery();
	}
	
	@Test
	//测试NumericRangeQuery查询
	public void test10() throws IOException {
		manager.findDocsByNumericRangeQuery();
	}
	
	@Test
	//测试BooleanQuery查询
	public void test11() throws IOException {
		manager.findDocsByBooleanQuery();
	}
	
	@Test
	//测试QueryParser查询
	public void test12() throws IOException, ParseException{
		manager.findDocsByQueryParser();
	}
	
	@Test
	//测试通过QueryParser结合简易语法进行分析查询
	public void test13() throws IOException, ParseException{
		manager.findDocsByQueryParser2();
	}
	
	@Test
	//测试MultiFieldQueryParser查询
	public void test14() throws IOException, ParseException{
		manager.findDocsByMultiFieldQueryParser();
	}
	
}





