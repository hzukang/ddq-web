package ddq.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import ddq.model.Calc;
import ddq.model.Debt;
import ddq.model.DebtFlow;
import ddq.model.DebtLog;
import ddq.model.DebtSysLog;
@Repository
public class DebtDao  {

	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;


	public List<Debt> selectAllDebt() throws Exception { 
		List<Debt> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllDebt");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public List<Debt> selectDebtByUidCur(Debt debt) throws Exception { 
		List<Debt> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectDebtByUidCur",debt);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<Debt> selectDebtByUid(int uid) throws Exception { 
		List<Debt> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectDebtByUid",uid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<Debt> selectDebtById(int id) throws Exception { 
		List<Debt> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectDebtById",id);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<Debt> selectDebtByFlowid(int flowid) throws Exception
	{
		List<Debt> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectDebtByFlowid",flowid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<HashMap<String,Object>> selectDebtSUMByUid(Debt debt) throws Exception {

		List<HashMap<String,Object>> list=null;
		try{
			list= sqlMapClientTemplate.queryForList("selectDebtSUMByUid",debt);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public int insertDebt(Debt debt) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertDebt",debt);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}  

	public int updateDebt(Debt debt) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateDebt", debt);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}  
	
	public int updateDebtInterest(Map map) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateDebtInterest", map);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key; 
	}
	
	public int updateAllDebtInterest() throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateAllDebtInterest");
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key; 
	}

	public int deleteDebtById(int debtid) throws Exception
	{
		int key=0;  
		try { 
			key=sqlMapClientTemplate.delete("deleteDebtById", debtid);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}


	// debtflow
	public List<DebtFlow> selectAllDebtFlow() throws Exception { 
		List<DebtFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllDebtFlow");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public List<DebtFlow> selectDebtFlowByUid(DebtFlow debtflow) throws Exception { 
		List<DebtFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectDebtFlowByUid",debtflow);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<DebtFlow> selectDebtFlowById(int flowid) throws Exception { 
		List<DebtFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectDebtFlowById",flowid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<DebtFlow> selectAssetsHistoryByUid(int uid) throws Exception { 
		List<DebtFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAssetsHistoryByUid",uid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public int insertDebtFlow(DebtFlow debtflow) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertDebtFlow",debtflow);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}  

	public int updateDebtFlow(DebtFlow debtflow) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateDebtFlow", debtflow);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}  

	public int delDebtFlowById(int id) throws Exception
	{
		int key=0;  
		try { 
			key=sqlMapClientTemplate.delete("delDebtFlowById", id);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}



	//<!-- debtlog -->
	public int insertDebtLog(DebtLog debtlog) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertDebtLog",debtlog);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	} 



	//<!-- debtsyslog -->
	public int insertDebtSysLog(DebtSysLog debtsyslog) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertDebtSysLog",debtsyslog);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	} 

	public List<DebtSysLog> selectDebtSysLogByDebtid(int debtid) throws Exception
	{
		List<DebtSysLog> list=null;  
		try {  
			list = sqlMapClientTemplate.queryForList("selectDebtSysLogByDebtid",debtid);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return list; 
	}

	public List<DebtSysLog> selectDebtSysLogByFlowid(int flowid) throws Exception
	{
		List<DebtSysLog> list=null;  
		try {  
			list = sqlMapClientTemplate.queryForList("selectDebtSysLogByFlowid",flowid);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return list; 
	}
	
	public int updateDebtSysLog(DebtSysLog debtsyslog) throws Exception
	{
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.update("updateDebtSysLog",debtsyslog);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}
	
	
	//<!-- debtcalc -->
	public int insertDebtCalc(Calc calc) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertDebtCalc",calc);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}

	public int updateDebtCalc(Calc calc) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateDebtCalc", calc);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 


}
