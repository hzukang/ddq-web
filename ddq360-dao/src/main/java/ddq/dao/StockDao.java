package ddq.dao;

import ddq.model.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockDao  {


	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;

	
	//stock
	public List<Stock> selectAllStock() throws Exception { 
		List<Stock> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllStock");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<Stock> selectStock() throws Exception { 
		List<Stock> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStock");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public int insertStock(Stock stock) throws Exception {  
		int key=0;  
		try {  
			key = (Integer) sqlMapClientTemplate.insert("insertStock",stock);  
		} catch (Exception e) {  
			e.printStackTrace();  
			throw e;  
		}  
		return key;  
	}  

	public int updateStock(Stock stock) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateStock", stock);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	public int deleteStockById(int stockid) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.delete("deleteStockById", stockid);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 


	//stockflow
	public List<StockFlow> selectAllStockFlow() throws Exception { 
		List<StockFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllStockFlow");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockFlow> selectStockFlowById(int id) throws Exception { 
		List<StockFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockFlowById",id);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockFlow> selectStockFlowByUid(StockFlow stockflow) throws Exception { 
		List<StockFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockFlowByUid",stockflow);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}


	
	public List<StockFlow> selectStockFlowByStock(StockFlow stockflow ) throws Exception { 
		List<StockFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockFlowByStock",stockflow);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockFlow> selectStockFlowByDate(String date) throws Exception { 
		List<StockFlow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockFlowByDate",date);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	
	public int updateStockFlow(StockFlow stockflow) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateStock", stockflow);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	public int insertStockFlow(StockFlow stockflow) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockFlow", stockflow);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 
	
	public int delStockFlow(int id) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.update("delStockFlow", id);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	//stocklog
	public int insertStockLog(StockLog stocklog) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockLog", stocklog);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	//stocksyslog
	public int insertStockSysLog(StockSysLog stocksyslog) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockSysLog", stocksyslog);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	//stockprofit
	public List<StockProfit> selectAllStockProfit(StockProfit stockprofit) throws Exception { 
		List<StockProfit> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllStockProfit",stockprofit);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public List<StockProfit> selectStockProfitByStock(String stockid) throws Exception { 
		List<StockProfit> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockProfitByStock",stockid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public List<StockProfit> selectStockProfitByUid(int uid) throws Exception { 
		List<StockProfit> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockProfitByStock",uid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public List<StockProfit> selectStockProfitByTime(Map map) throws Exception { 
		List<StockProfit> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockProfitByTime",map);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockProfit> selectStockProfitByCtype(StockProfit stockprofit) throws Exception { 
		List<StockProfit> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockProfitByCtype",stockprofit);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public int insertStockProfit(StockProfit stockprofit) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockProfit", stockprofit);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 
	
	public int insertStockProfitByList(List<StockProfit> list) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockProfitByList", list);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}

	
	public int updateStockProfit(StockProfit stockprofit) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateStockProfit", stockprofit);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	public int deleteStockProfit(int id) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.delete("deleteStockProfit", id);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 
	
	public int deleteStockProfitByTime(String date) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.delete("deleteStockProfitByTime", date);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 
	
	

	public int delStockProfit(int flowid) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("delStockProfit", flowid);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	//stockuser
	public List<StockUser> selectAllStockUser() throws Exception { 
		List<StockUser> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllStockUser");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockUser> selectStockIds() throws Exception { 
		List<StockUser> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockIds");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public List<StockUser> selectStockUserByUid(int uid) throws Exception { 
		List<StockUser> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockUserByUid",uid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return list;
	}
	
	public List<StockUser> selectStockUser(Map map) throws Exception { 
		List<StockUser> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockUser",map);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	

	public int insertStockUser(StockUser stockuser) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockUser",stockuser);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	public int updateStockUser(StockUser stockuser) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.update("updateStockUser",stockuser);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 

	public int deleteStockUser(int id) throws Exception {  
		int key=0;  
		try { 
			key=sqlMapClientTemplate.delete("deleteStockUser",id);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}


	//stocksnap
	public List<StockSnap> selectAllStockSnap() throws Exception { 
		List<StockSnap> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllStockSnap");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockSnap> selectStockSnapByDate(String date) throws Exception { 
		List<StockSnap> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockSnapByDate",date);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public int insertStockSnapByList(List<StockSnap> list) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockSnapByList", list);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}
	
	public int insertStockSnap(StockSnap stocksnap) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockSnap",stocksnap);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	} 
	
	public int deleteStockSnap(int id) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.delete("deleteStockSnap",id);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}
	
	public int deleteStockSnapByTime(String time) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.delete("deleteStockSnapByTime",time);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}
	
	
	//stockshow
	public List<StockShow> selectAllStockShow() throws Exception {
		List<StockShow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectAllStockShow");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockShow> selectStockShowByUid(int uid) throws Exception {
		List<StockShow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectStockShowByUid",uid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public List<StockShow> selectShowByUid(int uid) throws Exception {
		List<StockShow> list=null;
		try{
			list=sqlMapClientTemplate.queryForList("selectShowByUid",uid);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	public int insertStockShow(StockShow stockshow) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.insert("insertStockShow", stockshow);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}
	
	public int updateStockShow(StockShow stockshow) throws Exception {  
		int key=0;  
		try { 
			key=(Integer)sqlMapClientTemplate.update("updateStockShow", stockshow);
		} catch (Exception e) {  
			e.printStackTrace(); 
			throw e;  
		}  
		return key;  
	}
}
