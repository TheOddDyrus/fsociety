package com.thomax.letsgo.zoom.spring.service.impl;

import com.thomax.letsgo.zoom.spring.dao.IEmpTransferDao;
import com.thomax.letsgo.zoom.spring.service.IEmpTransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;

@Service("empTransferService2")
@Transactional
//@Transactional在类上作用，在方法上加@Transactional(readOnly=true)不会进行事务判断
public class EmpTransferService implements IEmpTransferService {
	
	@Resource(name="empDao3")
	private IEmpTransferDao empTransferDao;

	@Override
	//默认会增加事务判断
	public boolean transfer(int from, int to, int money) {
		try {
			int out = empTransferDao.outMoney(from, money);
			System.out.println("out:" + out);
			//System.out.println(10 / 0);
			int in = empTransferDao.inMoney(to, money);
			System.out.println("in:" + in);
			if (out == 1 && in == 1) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
