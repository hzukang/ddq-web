package ddq.dao;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import ddq.model.*;


@Repository
public class CalcDao  {


	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	/*
	 * calcflow */
	
	public List<Calc> selectAllCalc() throws Exception {  

		List<Calc> List = null;  
		try {  
			List = sqlMapClientTemplate.queryForList("selectAllCalc");  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return List;  
	}  

	public List<Calc> selectCalcByFLowid(int flowid) throws Exception {  

		List<Calc> List = null;  
		try {  
			List = sqlMapClientTemplate.queryForList("selectCalcByFLowid",flowid);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return List;  
	} 
	
	public List<Calc> selectCalcInterestByUid(Map map)throws Exception { 
		List<Calc> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectCalcInterestByUid",map);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}


	public int insertCalc(Calc calc) throws Exception {  
		int key;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertCalc",calc);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}  

	public int updateCalcById(Calc calc) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateCalcById",calc);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 
	
	public int updateCalcByDebtId(Calc calc) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateCalcByDebtId",calc);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 
	
	public int insertCalcByTime(Debt debt) throws Exception 
	{  
		int key=0;  
		try { 
			key=(Integer) sqlMapClientTemplate.insert("insertCalcByTime",debt);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key; 
	}



	/*
	 * calcflow */

	public List<CalcFlow> selectAllCalcFlow() throws Exception {
		List<CalcFlow> list=null;  
		try {  
			list = sqlMapClientTemplate.queryForList("selectAllCalcFlow");  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return list;  
	}

	public int insertCalcFlow(CalcFlow calcflow) throws Exception {  
		int key;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertCalcFlow",calcflow);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}  

	public int updateCalcFlow(int debtid) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateCalcFlow",debtid);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	public List<Map> selectInterest(int debtid) throws Exception
	{
		List<Map> list=null;  
		try {  
			list = sqlMapClientTemplate.queryForList("selectInterest",debtid);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return list; 
	}
	

	public int insertCalcFlowByTime(Debt debt) throws Exception
	{  
		int key=0;  
		try { 
			key=(Integer) sqlMapClientTemplate.insert("insertCalcFlowByTime",debt);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key; 
	}

	/*
	 * calclog*/
	public int insertCalcLog(CalcLog calclog) throws Exception {  
		int key;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertCalcLog",calclog);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}


	/*
	 * calcsyslog*/
	public int insertCalcSysLog(CalcSysLog calcsyslog) throws Exception {  
		int key;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertCalcSysLog",calcsyslog);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}
}
