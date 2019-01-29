package com.thomax.letsgo.jdbc.dao;

import com.thomax.letsgo.jdbc.domain.PlayerTable;
import com.thomax.letsgo.jdbc.exception.PlayerNotExistException;
import com.thomax.letsgo.jdbc.exception.SalaryException;

import java.sql.SQLException;
import java.util.List;

/**
 * 球员数据访问接口
 * @author _thomas
 */
public interface IPlayerDao {

	/**
	 * 查询所有球员信息
	 * @return 所有球员的集合
	 * @throws Exception
	 * @throws SQLException
	 */
	List<PlayerTable> findAll() throws SQLException;

	/**
	 * 根据球员号码查询球员信息
	 * @param id 球员号码
	 * @return 球员信息
	 * @throws SQLException
	 */
	PlayerTable findPlayerById(int id)  throws SQLException;

	/**
	 * 新增球员
	 * @param player 球员对象
	 * @return 是否新增成功
	 * @throws SQLException
	 */
	boolean add(PlayerTable player)  throws SQLException;

	/**
	 * 更新球员信息
	 * @param player 球员对象
	 * @return 是否更新成功
	 * @throws SQLException
	 */
	boolean update(PlayerTable player)  throws SQLException;

	/**
	 * 根据球员号码删除球员信息
	 * @param id 球员号码
	 * @return 是否删除成功
	 * @throws SQLException
	 */
	boolean delete(int id)  throws SQLException;
	
	/**
	 * 球员加薪
	 * @param id
	 * @param money
	 * @return 是否成功加薪
	 * @throws SQLException
	 * @throws PlayerNotExistException
	 */
	boolean in(int id, int money) throws SQLException, PlayerNotExistException;
	
	/**
	 * 球员降薪
	 * @param id
	 * @param money
	 * @return 是否成功降薪
	 * @throws SQLException
	 * @throws PlayerNotExistException
	 * @throws SalaryException
	 */
	boolean out(int id, int money) throws SQLException, PlayerNotExistException, SalaryException;

}


