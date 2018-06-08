package ddq.service;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddq.dao.CashDao;
import ddq.model.Cash;
import ddq.model.CashFlow;
import ddq.model.CashLog;
import ddq.model.CashSysLog;
import ddq.model.Global;

@Service
@Transactional
public class CashService  {


	@Autowired
	private CashDao cashDao; 
	

	/*
	 * Cach
	 */
	public int addCash(Cash cash) throws Exception{
		int ok=0;
		int flowid;
		try{
			//插入流水
			CashFlow cashflow =new CashFlow();
			cashflow.setCash(cash.getCash());
			cashflow.setCurrency(cash.getCurrency());
			cashflow.setOperation(1);
			cashflow.setStatus(1);
			cashflow.setUid(cash.getUid());
			cashflow.setTimestamp(cash.getCreatedate());
			flowid=cashDao.addCashFlow(cashflow);

			//更新现金
			ok=cashDao.updateCashByUid(cash);
			if(ok==0)//如果更新不成功就插入
			{
				ok=cashDao.addCash(cash);
			}

			//插入日志
			addLog(cashflow,flowid);
		}
		catch (Exception e) {  
			e.printStackTrace(); 
			ok=0;
			flowid=0;
			//cashDao.endTransaction();
			throw e; 
		} 
		return flowid;
	} 

	public void addLog(CashFlow cashflow,int flowid) throws Exception { 
		//插入日志
		CashLog cashlog=new CashLog();
		cashlog.setCash(cashflow.getCash());
		cashlog.setCurrency(cashflow.getCurrency());
		cashlog.setOperation(cashflow.getOperation());
		cashlog.setUid(cashflow.getUid());
		cashlog.setTimestamp(new Date());
		cashlog.setFlowid(flowid);
		cashlog.setUserlogid(Global.userlogid.get());
		cashDao.addCashLog(cashlog);

		CashSysLog cashsyslog=new CashSysLog();
		cashsyslog.setCash(cashflow.getCash());
		cashsyslog.setCurrency(cashflow.getCurrency());
		cashsyslog.setOperation(cashflow.getOperation());
		cashsyslog.setUid(cashflow.getUid());
		cashsyslog.setFlowid(flowid);
		cashsyslog.setTimestamp(new Date());
		cashDao.addCashSysLog(cashsyslog);
	}

	public int updateCash(CashFlow cashflow,double Originalcash) throws Exception {  
		int key=0;  
		try { 
			//更新现金流水
			key=cashDao.updateCashFlowById(cashflow);
			//更新现金账户
			Cash cash=new Cash();
			cash.setUid(cashflow.getUid());
			cash.setCurrency(cashflow.getCurrency());
			cash.setCash(cashflow.getCash()-Originalcash);
			cashDao.updateCashByUid(cash);

			//插入日志
			addLog(cashflow,cashflow.getId());

		} catch (Exception e) {  
			e.printStackTrace(); 
			key=0;
			throw e;
		}  
		return key;  
	}




	public List<Cash> seleteAllCash() throws Exception{
		List<Cash> list=null;
		try { 

			list=cashDao.queryAllCash();
		} catch (Exception e) {  
			e.printStackTrace(); 
		}  
		return list;
	}


	public List<Cash> seleteCashByUid(int id) throws Exception{
		List<Cash> list=null;
		try { 

			list=cashDao.queryCashByUid(id);
		} catch (Exception e) {  
			e.printStackTrace(); 
		}  
		return list;
	}




	/*
	 * Cachflow
	 */

	public List<CashFlow> seleteCashFlow() throws Exception{
		List<CashFlow> list=null;
		try { 

			list=cashDao.selectAllCashFlow();
		} catch (Exception e) {  
			e.printStackTrace(); 
		}  
		return list;
	}

	public List<CashFlow> seleteCashFlowByUid(CashFlow cashflow) throws Exception{
		List<CashFlow> list=null;
		try { 
			list=cashDao.selectCashFlowByUid(cashflow);
		} catch (Exception e) {  
			e.printStackTrace(); 
		}  
		return list;
	}

	public int deleteCashFlowById(int id) throws Exception
	{
		List<CashFlow> list=cashDao.seleteCashFlowById(id);
		return deleteCashFlow(list.get(0));
	}

	public int deleteCashFlow(CashFlow cashflow) throws Exception{
		int key=0;  
		try {
			//更新现金流水
			key=cashDao.SetCashFlowById(cashflow.getId());
			//更新现金账户
			Cash cash=new Cash();
			cash.setUid(cashflow.getUid());
			cash.setCurrency(cashflow.getCurrency());
			cash.setCash(0-cashflow.getCash());

			cashDao.updateCashByUid(cash);

			//插入日志
			addLog(cashflow,cashflow.getId());

		} catch (Exception e) {  
			e.printStackTrace(); 
			key=0;
			throw e;
		}

		return key;
	}
}
