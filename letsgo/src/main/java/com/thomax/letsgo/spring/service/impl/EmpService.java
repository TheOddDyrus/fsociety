package com.thomax.letsgo.spring.service.impl;

import com.thomax.letsgo.spring.dao.IEmpDao;
import com.thomax.letsgo.spring.dao.impl.EmpDao;
import com.thomax.letsgo.spring.domain.Emp;
import com.thomax.letsgo.spring.service.IEmpService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**spring发明了以上四种将javabean放到容器中的注解，功能目前一样，未来会扩展功能：
	@Component("empService") -> 具有宽泛型，可以通用，但一般不用
	@Controller("empService") -> 一般用在控制器层中，如springMvc中
	@Repository("empService") -> 一般用在dao层
	@Service("empService") -> 用在serice层

  设置当前bean的作用范围，@Scope(scopeName="四个值")：
	singleton(单例,默认值)
	prototype(多例)
	requst(与web中request有同样的作用域，不常用)
	session(与web中session有同样的作用域，不常用)
*/
@Service("empService1")
@Scope(scopeName="prototype")
public class EmpService implements IEmpService {
	
	@Resource(name="empDao1")
	private IEmpDao empDao;
	
	public EmpService() {
		empDao = new EmpDao();
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	@Override
	public List<Emp> findAll() {
		try {
			return empDao.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int save(Emp emp) {
		try {
			return empDao.save(emp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int update(Emp emp) {
		try {
			return empDao.update(emp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delete(int empno) {
		try {
			return empDao.delete(empno);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Emp findEmpByNo(int empno) {
		try {
			return empDao.findEmpByNo(empno);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
