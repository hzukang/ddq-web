package ddq.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddq.dao.SysDao;
import ddq.model.Currency;
import ddq.model.Global;
import ddq.model.User;
import ddq.model.UserLog;

@Service
@Transactional
public class SysService  {

	@Autowired
	private SysDao sysDao;

	public List<Currency> selectAllCurrency() throws Exception { 

		return sysDao.selectAllCurrency();
	}

	public int updateCurrencyByCode(Currency currency) throws Exception { 

		return sysDao.updateCurrencyByCode(currency);
	}

	

	public int addUserLog(UserLog userlog) throws Exception
	{ 
		int key=0;
		try{
			key=sysDao.insertUserLog(userlog);
			Global.userlogid.set(key);
		}
		catch(Exception e){
			e.printStackTrace();
			key=0;
			throw e;

		}
		return key;
	}

	public int deleteUserLog(Integer id) throws Exception { 
		id=id==null?0:id;
		return sysDao.deleteUserLogById(id);
	}

	public List<ddq.model.Calendar> selectCalendarByDate(ddq.model.Calendar temp) throws Exception { 

		return sysDao.selectCalendarByDate(temp);
	}

}
