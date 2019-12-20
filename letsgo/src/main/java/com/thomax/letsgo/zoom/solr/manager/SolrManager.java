package com.thomax.letsgo.zoom.solr.manager;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SolrManager {
	
	private SolrServer solrServer;
	
	public SolrManager() {
		solrServer = new HttpSolrServer("http://localhost:8080/solr");
	}
	
	//添加到索引库：
	public void addIndex() throws SolrServerException, IOException {
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id", "10001");
		document.setField("title", "java编程实践");
		document.setField("name", "java is good!");
		solrServer.add(document);
		solrServer.commit();
	}
	
	//删除索引库：
	public void deleteIndex() throws SolrServerException, IOException {
		solrServer.deleteById("10001");
		solrServer.deleteByQuery("product_name:幸福");
		solrServer.commit();
	}
	
	//对索引库进行简单查询：
	public void queryIndex1() throws SolrServerException, IOException {
		SolrQuery params = new SolrQuery();
		params.setQuery("*:*"); //设置查询条件，相当于：params.set("q", "*:*");
		params.set("fq", "product_catalog_name:幽默杂货"); //过滤查询
		params.addSort("product_price", ORDER.desc); //排序
		QueryResponse query = solrServer.query(params);
		SolrDocumentList results = query.getResults();
		long numFound = results.getNumFound();
		System.out.println("总记录数：" + numFound);
		for (SolrDocument doc : results) {
			String id = (String) doc.get("id");
			String product_name = (String) doc.get("product_name");
			String product_catalog_name = (String) doc.get("product_catalog_name");
			float product_price = (float) doc.get("product_price");
			System.out.println("id：" + id);
			System.out.println("product_name：" + product_name);
			System.out.println("product_catalog_name：" + product_catalog_name);
			System.out.println("product_price：" + product_price);
			System.out.println("--------------------------------------------------");
		}
		solrServer.commit();
	}
	
	//对索引库进行复杂查询：
	public void queryIndex2() throws SolrServerException, IOException {
		SolrQuery params = new SolrQuery();
		params.set("q", "钻石"); //相当于：params.setQuery("钻石");
		params.set("fq", "product_catalog_name:幽默杂货"); //相当于：params.setFilterQueries("product_catalog_name:幽默杂货");
		params.setSort("product_price", ORDER.desc); //相当于：params.set("sort", "product_price");
		params.setStart(0); //分也开始的记录号
		params.setRows(5); //分页中每页显示的记录数
		//显示域的列表
		params.setFields("id", "product_name", "product_catalog_name", "product_price");  //fl
		//显示默认搜索域
		params.set("df", "product_name", "product_catalog_name");
		//设置高度显示
		params.setHighlight(true);
		//设置高度显示域
		params.addHighlightField("product_name"); 
		//设置高度显示的前缀
		params.setHighlightSimplePre("<span style='color:red'>"); 
		//设置高度显示的后缀
		params.setHighlightSimplePost("</span>"); 
		QueryResponse query = solrServer.query(params);
		SolrDocumentList results = query.getResults();
		Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
		for (SolrDocument doc : results) {
			String id = (String) doc.get("id");
			String product_name = (String) doc.get("product_name");
			String product_catalog_name = (String) doc.get("product_catalog_name");
			float product_price = (float) doc.get("product_price");
			System.out.println("id：" + id);
			System.out.println("product_name：" + product_name);
			System.out.println("product_catalog_name：" + product_catalog_name);
			System.out.println("product_price：" + product_price);
			System.out.println("--------------------------------------------------");
			//遍历出高度数据(根据外层循环中的id 查询出高度数据的某一条记录就是下面的这个Map 集合)
			Map<String, List<String>> map = highlighting.get(id);
			List<String> pname = map.get("product_name");
			List<String> pdname = map.get("product_catalog_name");
			System.out.println(pname.get(0));
			System.out.println(pdname);
		}
		solrServer.commit();
	}

}






