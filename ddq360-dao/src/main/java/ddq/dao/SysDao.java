package ddq.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import ddq.model.Calendar;
import ddq.model.Currency;
import ddq.model.User;
import ddq.model.UserLog;
@Repository
public class SysDao   {
	
	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;

	//currency
	public List<Currency> selectAllCurrency() throws Exception {  

		List<Currency> List = null;  
		try {  
			List = sqlMapClientTemplate.queryForList("selectAllCurrency");  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return List;  
	}
	
	public int updateCurrencyByCode(Currency currency)throws Exception {  
	
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateCurrencyByCode", currency);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}

	//user
	public List<User> selectAllUser() throws Exception { 
		List<User> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllUserLog");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	} 
	
	public List<User> selectUserByOpenId(String openid) throws Exception { 
		List<User> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectUserByOpenId",openid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	} 
	
	public List<User> selectUserByOpenIdOrWid(User user) throws Exception { 
		List<User> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectUserByOpenIdOrWid",user);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	} 
	
	public int insertUser(User user) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertUser",user);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}  

	
	
	

	//userlog
	public List<UserLog> selectAllUserLog() throws Exception { 
		List<UserLog> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllUserLog");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public List<UserLog> selectUserLogBuyUid(int uid) throws Exception { 
		List<UserLog> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectUserLogBuyUid",uid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public int insertUserLog(UserLog userlog) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertUserLog",userlog);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}  
	
	public int deleteUserLogById(Integer id) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("deleteUserLogById",id);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}


	//Calendar
	public List<Calendar> selectCalendarByDate(Calendar calendar) throws Exception { 
		List<Calendar> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectCalendarByDate",calendar);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

}
