package com.thomax.letsgo.zoom.jdbc.service;

import com.thomax.letsgo.zoom.jdbc.domain.PlayerTable;

import java.util.List;

/**
 * 球员数据服务接口
 * @author _thomas
 */
public interface IPlayerService {

	/**
	 * 查询所有球员信息
	 * @return 所有球员的集合
	 */
	List<PlayerTable> findAll();

	/**
	 * 根据球员号码查询球员信息
	 * @param id 球员号码
	 * @return 球员信息
	 */
	PlayerTable findPlayerById(int id);

	/**
	 * 新增球员
	 * @param player 球员对象
	 * @return 是否新增成功
	 */
	boolean add(PlayerTable player);

	/**
	 * 更新球员信息
	 * @param player 球员对象
	 * @return 是否更新成功
	 */
	boolean update(PlayerTable player);

	/**
	 * 根据球员号码删除球员信息
	 * @param id 球员号码
	 * @return 是否删除成功
	 */
	boolean delete(int id);
	
	/**
	 * 两个球员间转账交易
	 * @param outId 转出者号码
	 * @param inId 存入者号码
	 * @param money 金额
	 * @return 是否转账成功
	 */
	boolean trans(int outId, int inId, int money);
	
}
