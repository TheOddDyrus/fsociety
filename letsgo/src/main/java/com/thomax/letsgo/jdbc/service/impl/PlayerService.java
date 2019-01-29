package com.thomax.letsgo.jdbc.service.impl;

import com.thomax.letsgo.jdbc.dao.impl.PlayerDao2;
import com.thomax.letsgo.jdbc.domain.PlayerTable;
import com.thomax.letsgo.jdbc.exception.PlayerNotExistException;
import com.thomax.letsgo.jdbc.exception.SalaryException;
import com.thomax.letsgo.jdbc.service.IPlayerService;
import com.thomax.letsgo.jdbc.util.DBUtils;

import java.sql.SQLException;
import java.util.List;

public class PlayerService implements IPlayerService {
	
	private static PlayerDao2 playerDao = new PlayerDao2();

	public List<PlayerTable> findAll() {
		try {
			return playerDao.findAll();
		} catch (SQLException se) {
			System.out.println("查找所有球员时发生异常：" + se.getMessage());
		}
		return null;
	}

	public PlayerTable findPlayerById(int id) {
		try {
			return playerDao.findPlayerById(id);
		} catch (SQLException se) {
			System.out.println("根据号码查找球员时发生异常：" + se.getMessage());
		}
		return null;
	}

	public boolean add(PlayerTable player) {
		try {
			DBUtils.beginNoAutoTransaction();
			return playerDao.add(player);
		} catch (SQLException se) {
			System.out.println("新增球员时发生异常：" + se.getMessage());
		} 
		return false;
	}

	public boolean update(PlayerTable player) {
		try {
			DBUtils.beginNoAutoTransaction();
			return playerDao.update(player);
		} catch (SQLException se) {
			System.out.println("更新球员时发生异常：" + se.getMessage());
		}
		return false;
	}

	public boolean delete(int id) {
		try {
			DBUtils.beginNoAutoTransaction();
			return playerDao.delete(id);
		} catch (SQLException se) {
			System.out.println("删除球员时发生异常：" + se.getMessage());
		}
		return false;
	}

	public boolean trans(int outId, int inId, int money) {
		try {
			DBUtils.beginNoAutoTransaction();
			boolean isOut = playerDao.out(outId, money);
			boolean isIn = playerDao.in(inId, money);
			if (isOut && isIn) {
				DBUtils.commit();
				return true;
			} else {
				DBUtils.rollback();
			}
		} catch (SQLException se) {
			System.out.println("薪水转换过程发生异常：" + se.getMessage());
		} catch (PlayerNotExistException pnee) {
			System.out.println(pnee.getMessage());
		} catch (SalaryException se) {
			System.out.println(se.getMessage());
		}
		return false;
	}

}
