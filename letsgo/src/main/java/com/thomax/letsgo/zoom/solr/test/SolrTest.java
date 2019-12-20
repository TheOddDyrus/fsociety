package com.thomax.letsgo.zoom.solr.test;

import com.thomax.letsgo.zoom.solr.manager.SolrManager;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SolrTest {
	
	private SolrManager solrManager;
	
	@Before
	public void init() {
		solrManager = new SolrManager();
	}
	
	@Test
	//测试新增字段到索引库
	public void test01() throws SolrServerException, IOException {
		solrManager.addIndex();
	}
	
	@Test
	//删除索引库字段：
	public void test02() throws SolrServerException, IOException {
		solrManager.deleteIndex();
	}
	
	@Test
	//测试对索引库进行简单查询：
	public void test03() throws SolrServerException, IOException {
		solrManager.queryIndex1();
	}
	
	@Test
	//测试对索引库进行复杂查询：
	public void test04() throws SolrServerException, IOException {
		solrManager.queryIndex2();
	}

}
