package com.thomax.letsgo.jdbc.dao.impl;

import com.thomax.letsgo.jdbc.dao.IPlayerDao;
import com.thomax.letsgo.jdbc.domain.PlayerTable;
import com.thomax.letsgo.jdbc.exception.PlayerNotExistException;
import com.thomax.letsgo.jdbc.exception.SalaryException;
import com.thomax.letsgo.jdbc.util.DBUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**使用PreparedStatement命令对象
 * PreparedStatement继承于Statement，可以写动态参数化的查询，比Statement更快，可以防止SQL注入式攻击
 * @author _thomas
 */
public class PlayerDao2 implements IPlayerDao {
	
	public List<PlayerTable> findAll() throws SQLException {
		List<PlayerTable> players = new ArrayList<PlayerTable>();
		Connection connection = DBUtils.getConnection();
		String sqlSelectAll = "SELECT * FROM player";
		PreparedStatement ps = connection.prepareStatement(sqlSelectAll);
		ResultSet rs = ps.executeQuery();
		
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

		DBUtils.close(ps, connection, rs);
		return players;
	}
	
	public PlayerTable findPlayerById(int id) throws SQLException {
		PlayerTable player = null;
		Connection connection = DBUtils.getConnection();
		String sqlSelect = "SELECT * FROM player WHERE id = ?";
		PreparedStatement ps = connection.prepareStatement(sqlSelect);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		if (rs != null && rs.next()) {
			String name = rs.getString("name");
			Date transfer = rs.getDate("transfer");
			Time endless = rs.getTime("endless");
			String description = rs.getString("description");
			int salary = rs.getInt("salary");
			player = new PlayerTable(id, name, transfer, endless, description, salary);
		}

		DBUtils.close(ps, connection, rs);
		return player;
	}
	
	public boolean add(PlayerTable player) throws SQLException {
		Connection connection = DBUtils.getLocalConnection();
		String sqlAdd = "INSERT INTO player VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(sqlAdd);
		ps.setInt(1, player.getId());
		ps.setString(2, player.getName());
		ps.setDate(3, player.getTransfer());
		ps.setTime(4, player.getEndless());
		ps.setString(5, player.getDescription());
		ps.setInt(6, player.getSalary());
		int row = ps.executeUpdate();
		
		if (row > 0) {
			DBUtils.commit();
		} else {
			DBUtils.rollback();
		}
		DBUtils.close(ps, null, null);
		if (row > 0) {
			return true;
		}
		return false;
	}
	
	public boolean update(PlayerTable player) throws SQLException {
		Connection connection = DBUtils.getLocalConnection();
		String sqlUpdate = "UPDATE player SET name=?, transfer=?, endless=?, description=?, salary=? where id=?";
		PreparedStatement ps = connection.prepareStatement(sqlUpdate);
		ps.setString(1, player.getName());
		ps.setDate(2, player.getTransfer());
		ps.setTime(3, player.getEndless());
		ps.setString(4, player.getDescription());
		ps.setInt(5, player.getSalary());
		ps.setInt(6, player.getId());
		int row = ps.executeUpdate();
		
		if (row > 0) {
			DBUtils.commit();
		} else {
			DBUtils.rollback();
		}
		DBUtils.close(ps, null, null);
		if (row > 0) {
			return true;
		}
		return false;
	}
	
	public boolean delete(int id) throws SQLException {
		Connection connection = DBUtils.getLocalConnection();
		String sqlDelete = "DELETE FROM player WHERE id = ?";
		PreparedStatement ps = connection.prepareStatement(sqlDelete);
		ps.setInt(1, id);
		int row = ps.executeUpdate();
		
		if (row > 0) {
			DBUtils.commit();
		} else {
			DBUtils.rollback();
		}
		DBUtils.close(ps, null, null);
		if (row > 0) {
			return true;
		}
		return false;
	}

	public boolean in(int id, int money) throws SQLException, PlayerNotExistException {
		Connection connection = DBUtils.getLocalConnection();
		if (this.findPlayerById(id) == null) {
			throw new PlayerNotExistException(id + "号球员不存在！");
		}
		String sqlIn = "Update player SET salary = salary + ? where id = ?";
		PreparedStatement ps = connection.prepareStatement(sqlIn);
		ps.setInt(1, money);
		ps.setInt(2, id);
		int row = ps.executeUpdate();
		
		DBUtils.close(ps, null, null);
		if (row > 0) {
			return true;
		}
		return false;
	}

	public boolean out(int id, int money) throws SQLException, PlayerNotExistException, SalaryException {
		Connection connection = DBUtils.getLocalConnection();
		PlayerTable player = null;
		if ((player = this.findPlayerById(id)) == null) {
			throw new PlayerNotExistException(id + "号球员不存在！");
		}
		if (player.getSalary() - money < 1) {
			throw new SalaryException("此球员降薪" + money + "€后工资小于1€，不能执行！");
		}
		String sqlOut = "Update player SET salary = salary - ? where id = ?";
		PreparedStatement ps = connection.prepareStatement(sqlOut);
		ps.setInt(1, money);
		ps.setInt(2, id);
		int row = ps.executeUpdate();
		
		DBUtils.close(ps, null, null);
		if (row > 0) {
			return true;
		}
		return false;
	}
	
}



