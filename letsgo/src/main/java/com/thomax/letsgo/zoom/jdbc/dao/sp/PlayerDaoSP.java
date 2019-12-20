package com.thomax.letsgo.zoom.jdbc.dao.sp;

import com.thomax.letsgo.zoom.jdbc.domain.PlayerTable;
import com.thomax.letsgo.zoom.jdbc.util.DBUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**使用CallableStatement命令对象
 * CallableStatement继承于PreparedStatement，常用于调用存储过程
 * @author _thomas
 */
public class PlayerDaoSP {

	public static void main(String[] args) {
		//调用无参存储过程
		PlayerDaoSP pd = new PlayerDaoSP();
		List<PlayerTable> players = pd.findMaxPlayer();
		for (PlayerTable player : players) {
			System.out.println(player);
		}
		System.out.println();
		
		//调用带参数返回值的存储过程
		String popularName = pd.findPopularPlayer(10000);
		System.out.println("最受欢迎的球员是：" + popularName);
	}
	
	public List<PlayerTable> findMaxPlayer() {
		List<PlayerTable> players = new ArrayList<PlayerTable>();
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			connection = DBUtils.getConnection();
			String spMaxPlayer = "call sp_max_player";
			cs = connection.prepareCall(spMaxPlayer);
			rs = cs.executeQuery();
			
			while (rs != null && rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString("name");
				Date transfer = rs.getDate(3);
				Time endless = rs.getTime("endless");
				String description = rs.getString(5);
				int salary = rs.getInt("salary");
				PlayerTable player = new PlayerTable(id, name, transfer, endless, description, salary);
				players.add(player);
			}
		} catch (SQLException se) {
			System.out.println("执行无参存储过程时异常：" + se.getMessage());
		} finally {
			DBUtils.close(cs, connection, rs);
		}
		return players;
	}
	
	public String findPopularPlayer(int money) {
		Connection connection = null;
		CallableStatement cs = null;
		String popularName = null;
		try {
			connection = DBUtils.getConnection();
			String spPopularPlayer = "call sp_popular_player(?, ?)";
			cs = connection.prepareCall(spPopularPlayer);
			cs.setInt(1, money);
			cs.registerOutParameter(2, Types.VARCHAR);  //java.sql.Types 存放数据库字段类型的类
			cs.executeQuery();
			popularName = cs.getString(2);
		} catch (SQLException se) {
			System.out.println("执行带参数返回值的存储过程时异常：" + se.getMessage());
		} finally {
			DBUtils.close(cs, connection, null);
		}
		return popularName;
	}

}



