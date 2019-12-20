package com.thomax.letsgo.zoom.jdbc.test;

import com.thomax.letsgo.zoom.jdbc.domain.PlayerTable;
import com.thomax.letsgo.zoom.jdbc.service.impl.PlayerService;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

public class TestPlayerService {
	
	public PlayerService ps;
	
	@Before
	public void beforePlayer() throws Exception {
		ps = new PlayerService();
	}

	@Test
	public void testPlayer() throws Exception {
		//查询所有记录
		List<PlayerTable> players = ps.findAll();
		for (PlayerTable player : players) {
			System.out.println(player);
		}
		System.out.println();
		
		//查询单条记录
		PlayerTable player1 = ps.findPlayerById(8);
		System.out.println(player1);
		System.out.println();
		
		//新增一条记录
		PlayerTable player2 = new PlayerTable(25,
											  "托马斯", 
											  new Date(new SimpleDateFormat("yyyy-MM-dd").parse("1992-07-05").getTime()),
											  new Time(new SimpleDateFormat("HH:mm:ss").parse("06:06:06").getTime()),
											  "替补球员！@#￥",
											  3600);
		boolean isAdd = ps.add(player2);
		System.out.println("是否新增成功：" + isAdd);
		
		//删除一条记录
		boolean isDelete = ps.delete(25);
		System.out.println("是否删除成功：" + isDelete);
		
		//更新一条记录
		player1.setSalary(8888);
		boolean isUpdate = ps.update(player1);
		System.out.println("是否更新成功：" + isUpdate);
		
		//球员之间薪水转换
		boolean isTrans = ps.trans(5, 7, 1);
		System.out.println("是否转换：" + isTrans);
	}

}
