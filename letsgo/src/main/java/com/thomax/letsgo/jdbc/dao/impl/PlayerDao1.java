package com.thomax.letsgo.jdbc.dao.impl;

import com.thomax.letsgo.jdbc.dao.IPlayerDao;
import com.thomax.letsgo.jdbc.domain.PlayerTable;
import com.thomax.letsgo.jdbc.exception.PlayerNotExistException;
import com.thomax.letsgo.jdbc.util.DBUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**使用Statement命令对象
 * 这种方式可能存在被SQL注入式攻击的可能
 * @author _thomas
 */
public class PlayerDao1 implements IPlayerDao {

	public static void main(String[] args) throws Exception {
		//查询所有记录
		IPlayerDao pd = new PlayerDao1();
		List<PlayerTable> players = pd.findAll();
		for (PlayerTable player : players) {
			System.out.println(player);
		}
		System.out.println();
		
		//查询单条记录
		PlayerTable player1 = pd.findPlayerById(5);
		System.out.println(player1);
		System.out.println();
		
		//新增一条记录
		PlayerTable player2 = new PlayerTable(25,
											  "托马斯", 
											  new Date(new SimpleDateFormat("yyyy-MM-dd").parse("1992-07-05").getTime()),
											  new Time(new SimpleDateFormat("HH:mm:ss").parse("06:06:06").getTime()),
											  "替补球员！@#￥",
											  3600);
		boolean isAdd = pd.add(player2);
		System.out.println("是否新增成功：" + isAdd);
		
		//删除一条记录
		boolean isDelete = pd.delete(25);
		System.out.println("是否删除成功：" + isDelete);
		
		//更新一条记录
		player1.setSalary(10000);
		boolean isUpdate = pd.update(player1);
		System.out.println("是否更新成功：" + isUpdate);
	}
	
	public List<PlayerTable> findAll() {
		List<PlayerTable> players = new ArrayList<PlayerTable>();
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sqlSelectAll = "SELECT * FROM player";
		try {
			connection = DBUtils.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlSelectAll);
			
			while (rs != null && rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date transfer = rs.getDate("transfer");
				Time endless = rs.getTime("endless");
				String description = rs.getString("description");
				int salary = rs.getInt("salary");
				PlayerTable player = new PlayerTable(id, name, transfer, endless, description, salary);
				players.add(player);
			}
		} catch (SQLException se) {
			System.out.println("执行查询所有数据时异常：" + se.getMessage());
		} finally {
			DBUtils.close(stmt, connection, rs);
		}
		return players;
	}
	
	public PlayerTable findPlayerById(int id) {
		PlayerTable player = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = DBUtils.getConnection();
			stmt = connection.createStatement();
			String sqlSelect = "SELECT * FROM player WHERE id = " + id;
			rs = stmt.executeQuery(sqlSelect);
			
			if (rs != null && rs.next()) {
				String name = rs.getString("name");
				Date transfer = rs.getDate("transfer");
				Time endless = rs.getTime("endless");
				String description = rs.getString("description");
				int salary = rs.getInt("salary");
				player = new PlayerTable(id, name, transfer, endless, description, salary);
			}
		} catch (SQLException se) {
			System.out.println("执行查询单条数据时异常：" + se.getMessage());
		} finally {
			DBUtils.close(stmt, connection, rs);
		}
		return player;
	}
	
	public boolean add(PlayerTable player) {
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = DBUtils.getConnection();
			stmt = connection.createStatement();
			String sqlAdd = "INSERT INTO player VALUES(" + player.getId() + ", '" +
														player.getName() + "', '" + 
														player.getTransfer() + "', '" + 
														player.getEndless() + "', '" + 
														player.getDescription() + "', " + 
														player.getSalary()+")";
			int row = stmt.executeUpdate(sqlAdd);
			if (row > 0) {
				return true;
			}
		} catch (SQLException se) {
			System.out.println("执行增加数据时异常：" + se.getMessage());
		} finally {
			DBUtils.close(stmt, connection, null);
		}
		return false;
	}
	
	public boolean update(PlayerTable player) {
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = DBUtils.getConnection();
			stmt = connection.createStatement();
			String sqlUpdate = "UPDATE player SET name = '" + player.getName() +
											"', transfer = '" + player.getTransfer() + 
											"', endless = '" + player.getEndless() + 
											"', description = '" + player.getDescription() + 
											"', salary = " + player.getSalary() + 
											" WHERE id = " + player.getId();
			int row = stmt.executeUpdate(sqlUpdate);
			if (row > 0) {
				return true;
			}
		} catch (SQLException se) {
			System.out.println("执行更新数据时异常：" + se.getMessage());
		}  finally {
			DBUtils.close(stmt, connection, null);
		}
		return false;
	}
	
	public boolean delete(int id) {
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = DBUtils.getConnection();
			stmt = connection.createStatement();
			String sqlDelete = "DELETE FROM player WHERE id = " + id;
											
			int row = stmt.executeUpdate(sqlDelete);
			if (row > 0) {
				return true;
			}
		} catch (SQLException se) {
			System.out.println("执行删除数据时异常：" + se.getMessage());
		}  finally {
			DBUtils.close(stmt, connection, null);
		}
		return false;
	}

	@Override
	public boolean in(int id, int money) throws SQLException, PlayerNotExistException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean out(int id, int money) throws SQLException, PlayerNotExistException {
		// TODO Auto-generated method stub
		return false;
	}

}
