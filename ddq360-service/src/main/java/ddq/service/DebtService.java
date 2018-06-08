package ddq.service;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddq.dao.CalcDao;
import ddq.dao.DebtDao;
import ddq.model.Calc;
import ddq.model.CalcFlow;
import ddq.model.CalcLog;
import ddq.model.Cash;
import ddq.model.CashFlow;
import ddq.model.Debt;
import ddq.model.DebtFlow;
import ddq.model.DebtLog;
import ddq.model.DebtSysLog;
import ddq.model.Global;
import ddq.model.JsonClass;

@Service
@Transactional
public class DebtService {

	@Autowired
	private DebtDao debtDao; 

	@Autowired
	private CalcDao calcDao; 

	@Autowired
	private CashService cashService; 
	

	public int addDebtLog(DebtFlow debtflow,int flowid) throws Exception
	{
		int key;

		DebtLog debtlog=new DebtLog();
		debtlog.setCurrency(debtflow.getCurrency());
		debtlog.setDebt(debtflow.getDebt());
		debtlog.setFlowid(flowid);
		debtlog.setOperation(debtflow.getOperation());
		debtlog.setRate(debtflow.getRate());
		debtlog.setUid(debtflow.getUid());
		debtlog.setTimestamp(debtflow.getTimestamp());
		debtlog.setUserlogid(Global.userlogid.get());
		key=debtDao.insertDebtLog(debtlog);
		return key;
	}

	public int addDebtSysLog(DebtFlow debtflow,int flowid,int debtid) throws Exception
	{

		int key;
		DebtSysLog debtsyslog=new DebtSysLog();
		debtsyslog.setCurrency(debtflow.getCurrency());
		debtsyslog.setDebt(debtflow.getDebt());
		debtsyslog.setFlowid(flowid);
		debtsyslog.setOperation(debtflow.getOperation());
		debtsyslog.setRate(debtflow.getRate());
		debtsyslog.setUid(debtflow.getUid());
		debtsyslog.setTimestamp(debtflow.getTimestamp());
		debtsyslog.setDebtid(debtid);
		key=debtDao.insertDebtSysLog(debtsyslog);
		return key;
	}

	//添加融资
	public int addDebt(Debt debt) throws Exception
	{

		int key=0;

		DebtFlow debtflow=new DebtFlow();
		debtflow.setDebt(debt.getDebt());
		debtflow.setCurrency(debt.getCurrency());
		debtflow.setOperation(1);
		debtflow.setRate(debt.getRate());
		debtflow.setStarttime(debt.getStarttime());
		debtflow.setStatus(1);
		debtflow.setTimestamp(new Date());
		debtflow.setUid(debt.getUid());
		try{
			key=debtDao.insertDebtFlow(debtflow);
			debt.setFlowid(key);
			debtDao.insertDebt(debt);

			//添加日志
			addDebtLog(debtflow,key);
			addDebtSysLog(debtflow,key,0);

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}


		return key;
	}


	//还融资
	public int reduceDebt(Debt debt,boolean usecash) throws Exception
	{
		int key=0;
		try{
			//计算总负债及利息
			double debtall=0;
			List<HashMap<String,Object>> list=debtDao.selectDebtSUMByUid(debt);
			if(list.size()>0)
			{
				HashMap<String,Object> map=list.get(0);
				debtall=Double.parseDouble(map.get("debt").toString())+Double.parseDouble(map.get("interest").toString());
			}
			if(debtall<debt.getDebt())
			{
				return key;
			}

			int cashflowid=0;
			if(usecash)//用现金去还负债
			{
				Cash cash=new Cash();
				cash.setCurrency(debt.getCurrency());
				cash.setCash(-debt.getDebt());
				cash.setCreatedate(new Date());
				cash.setUid(debt.getUid());
				cashflowid=cashService.addCash(cash);
			}

			//插入负债流水
			DebtFlow debtflow=new DebtFlow();
			debtflow.setCurrency(debt.getCurrency());
			debtflow.setDebt(debt.getDebt());
			debtflow.setFlowid(cashflowid);
			debtflow.setOperation(2);
			debtflow.setRate(debt.getRate());
			debtflow.setStarttime(debt.getStarttime());
			debtflow.setStatus(2);
			debtflow.setTimestamp(new Date());
			debtflow.setUid(debt.getUid());
			key=debtDao.insertDebtFlow(debtflow);
			//添加日志
			addDebtLog(debtflow,key);


			//归还负债
			List<Debt> list1=debtDao.selectDebtByUidCur(debt);
			double debtcash=debt.getDebt();
			for(int i=0;i<list1.size();i++)
			{
				Debt temp=list1.get(i);
				double debttemp=temp.getDebt();
				if(debtcash-debttemp>0)
				{
					temp.setDebt(0.0);
					debtDao.updateDebt(temp);
					debtcash=debtcash-debttemp;
					//添加系统日志
					debtflow.setDebt(debttemp);
					addDebtSysLog(debtflow,key,temp.getId());
				}
				else
				{
					temp.setDebt(debttemp-debtcash);
					debtDao.updateDebt(temp);

					//添加系统日志
					debtflow.setDebt(debtcash);
					addDebtSysLog(debtflow,key,temp.getId());

					debtcash=0;
					break;
				}
			}

			//归还利息
			if(debtcash>0)
			{
				//ReduceInterest();
				Map map=new HashMap();  
				map.put("uid", debt.getUid());  
				map.put("currency", debt.getCurrency()); 
				//查找未还利息
				List<Calc> list2=calcDao.selectCalcInterestByUid(map);
				for(int i=0;i<list2.size();i++)
				{
					Calc temp=list2.get(i);
					double calctemp=temp.getInterest()-temp.getRepayment();
					if(debtcash-calctemp>=0)
					{
						temp.setStatus(3);
						temp.setRepayment(temp.getInterest());
						calcDao.updateCalcById(temp);
						debtcash=debtcash-calctemp;
						int calcflowid;
						//添加利息流水
						CalcFlow calcflow=new CalcFlow();
						calcflow.setCalcid(temp.getId());
						calcflow.setCashflowid(cashflowid);
						calcflow.setTimestamp(new Date());
						calcflow.setStatus(1);
						calcflow.setOperation(2);
						calcflow.setInterest(temp.getInterest());
						calcflow.setCurrency(temp.getCurrency());
						calcflowid=calcDao.insertCalcFlow(calcflow);
						//添加利息日志
						addCalcLog(calcflow,calcflowid,cashflowid);
					}else{
						temp.setStatus(2);
						temp.setRepayment(debtcash+temp.getRepayment());
						calcDao.updateCalcById(temp);
						int calcflowid;
						//添加利息流水
						CalcFlow calcflow=new CalcFlow();
						calcflow.setCalcid(temp.getId());
						calcflow.setCashflowid(cashflowid);
						calcflow.setOperation(2);
						calcflow.setTimestamp(new Date());
						calcflow.setStatus(1);
						calcflow.setInterest(debtcash);
						calcflow.setCurrency(temp.getCurrency());
						calcflowid=calcDao.insertCalcFlow(calcflow);
						//添加利息日志
						addCalcLog(calcflow,calcflowid,cashflowid);
						debtcash=0;
						break;
					}
				}

				//更新负债表利息字段
				debtDao.updateDebtInterest(map);

			}


		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}

		return key;
	}

	public int addCalcLog(CalcFlow calcflow,int calcflowid,int cashflowid) throws Exception
	{
		int key=0;
		CalcLog calclog=new CalcLog();
		calclog.setCurrency(calcflow.getCurrency());
		calclog.setCashflowid(cashflowid);
		calclog.setFlowid(calcflowid);
		calclog.setInterest(calcflow.getInterest());
		calclog.setOperation(1);
		calclog.setTime(calcflow.getTimestamp());
		key=calcDao.insertCalcLog(calclog);
		return key;
	}

	//回滚已还利息
	public void rollInterest(Debt debt) throws Exception
	{

		List<Map> list1=calcDao.selectInterest(debt.getId());
		for(int i=0;i<list1.size();i++)
		{
			Map map=list1.get(i);
			//map.get(arg0)
			double interest=Double.parseDouble(map.get("interest").toString());
			double cash=Double.parseDouble(map.get("cash").toString());
			int cashflowid=(Integer)map.get("cashflowid");

			if(cashflowid!=0)
			{
				CashFlow cashflow=new CashFlow();
				cashflow.setCash(cash+interest);
				cashflow.setId(cashflowid);
				cashflow.setOperation(2);
				cashflow.setCurrency(debt.getCurrency());
				cashflow.setUid(debt.getUid());
				cashflow.setTimestamp(new Date());
				cashService.updateCash(cashflow, cash);
			}
			else
			{
				Cash temp=new Cash();
				temp.setCash(cash+interest);
				temp.setCurrency(debt.getCurrency());
				temp.setUid(debt.getUid());
				temp.setCreatedate(new Date());
				temp.setUpdatedate(new Date());
				cashService.addCash(temp);
			}
		}

		Calc calc=new Calc();
		calc.setDebtid(debt.getId());
		calc.setStatus(4);
		calcDao.updateCalcByDebtId(calc);
		calcDao.updateCalcFlow(debt.getId());
	}

	//重新计算利息
	public double caculateInterest(Debt debt,DebtFlow debtflow) throws Exception
	{
		double debtnew=debtflow.getDebt();
		double interestnum=0;
		List<DebtSysLog> list2=debtDao.selectDebtSysLogByDebtid(debt.getId());
		Date starttime=debtflow.getStarttime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(starttime);
		cal.add(Calendar.DATE, -1);
		starttime=cal.getTime();

		for(int i=0;i<list2.size();i++)
		{
			DebtSysLog temp=list2.get(i);
			debt.setDebt(debtnew);
			debt.setStarttime(starttime);
			debt.setEndtime(temp.getTimestamp());
			debt.setInterest(debtnew*debt.getRate()/360);
			//calcDao.insertCalcFlowByTime(debt);
			//calcDao.insertCalcByTime(debt);

			//计算总利息
			long d=temp.getTimestamp().getTime()-starttime.getTime();
			long days=d/(1000*60*60*24);
			interestnum=interestnum+debtnew*debt.getRate()/360*days;

			starttime=temp.getTimestamp();
			debtnew=debtnew-temp.getDebt();
			if(debtnew<=0)
			{
				break;
			}
		}


		debt.setDebt(debtnew);
		debt.setStarttime(starttime);
		debt.setEndtime(new Date());
		debt.setInterest(debtnew*debt.getRate()/360);
		//calcDao.insertCalcByTime(debt);

		//计算总利息
		long d=new Date().getTime()-starttime.getTime();
		long days=d/(1000*60*60*24);
		interestnum=interestnum+debtnew*debt.getRate()/360*days;

		//插入总利息
		Calc calc=new Calc();
		calc.setDebtid(debt.getId());
		calc.setStatus(1);
		calc.setCurrency(debt.getCurrency());
		calc.setDebt(debt.getDebt());
		calc.setInterest(interestnum);
		calc.setTime(new Date());
		calc.setRate(debt.getRate());
		calcDao.insertCalc(calc);
		int calcid=calcDao.insertCalc(calc);
		//插入利息流水
		CalcFlow calcflow=new CalcFlow();
		calcflow.setCalcid(calcid);
		calcflow.setInterest(calc.getInterest());
		calcflow.setOperation(1);
		calcflow.setStatus(1);
		calcflow.setTimestamp(new Date());
		calcflow.setCurrency(debt.getCurrency());
		int calcflowid=calcDao.insertCalcFlow(calcflow);

		return interestnum;
	}

	//编辑融资
	public int updateDebt(DebtFlow debtflow,double Originaldebt) throws Exception
	{
		int key=0;

		double interestnum=0;
		try{
			//利息回滚
			List<Debt> list=debtDao.selectDebtByFlowid(debtflow.getId());
			Debt debt=null;
			if(list.size()>0)
			{
				debt=list.get(0);

				//回滚已还利息
				rollInterest(debt);

				//重新计算利息
				interestnum=caculateInterest(debt,debtflow);

				//更新债务流水
				key=debtDao.updateDebtFlow(debtflow);


				debt.setDebt(debt.getDebt()+debtflow.getDebt()-Originaldebt);

				debt.setInterest(interestnum);
				debtDao.updateDebt(debt);

				//计入日志
				debtflow.setOperation(3);
				addDebtLog(debtflow,debtflow.getId());
				addDebtSysLog(debtflow,debtflow.getId(),0);
			}

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}

	//删除融资
	public int deleteDebt(DebtFlow debtflow) throws Exception
	{
		int key=0;
		try{
			//利息回滚
			List<Debt> list=debtDao.selectDebtByFlowid(debtflow.getId());
			Debt debt=null;
			if(list.size()>0)
			{
				debt=list.get(0);

				//回滚已还利息
				rollInterest(debt);

				//更新债务流水
				key=debtDao.updateDebtFlow(debtflow);

				//更新债务
				debt.setDebt(debt.getDebt()-debtflow.getDebt());
				debtDao.updateDebt(debt);

				//插入日志
				debtflow.setOperation(4);
				addDebtLog(debtflow,debtflow.getId());
				addDebtSysLog(debtflow,debtflow.getId(),0);
			}

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;

	}

	public int deleteDebt(int flowid) throws Exception
	{
		List<DebtFlow> list=debtDao.selectDebtFlowById(flowid);
		return deleteDebt(list.get(0));
	}
	//编辑还融资 todo 需要重新计算利息
	public int updateRDebt(DebtFlow debtflow,double Originaldebt) throws Exception
	{   
		int key=0;
		try{

			//得到所还融资
			List<DebtSysLog> list=debtDao.selectDebtSysLogByFlowid(debtflow.getId());
			double debtdiff=debtflow.getDebt()-Originaldebt;
			for(int i=0;i<list.size();i++)
			{
				DebtSysLog debtsyslog=list.get(0); 
				//得到融资
				Debt debt=debtDao.selectDebtById(debtsyslog.getDebtid()).get(0);

				//得到融资流水
				DebtFlow debtflow1=debtDao.selectDebtFlowById(debt.getFlowid()).get(0);

				if(debtdiff<0)
				{
					//更新系统日志
					debtsyslog.setDebt(0.0);
					debtsyslog.setOperation(5);
					debtDao.updateDebtSysLog(debtsyslog);

					rollInterest(debt);
					double interestnum=caculateInterest(debt,debtflow1);
					//更新融资
					debt.setDebt(debt.getDebt()-debtsyslog.getDebt());
					debt.setInterest(interestnum);
					debtDao.updateDebt(debt);
					debtdiff=debtdiff+debtsyslog.getDebt();
				}else if(debtdiff>0)
				{
					//更新系统日志
					debtsyslog.setDebt(debtsyslog.getDebt()+debtdiff);
					debtsyslog.setOperation(5);
					debtDao.updateDebtSysLog(debtsyslog);

					rollInterest(debt);
					double interestnum=caculateInterest(debt,debtflow1);
					//更新融资
					debt.setDebt(debt.getDebt()+debtdiff);

					debt.setInterest(interestnum);
					debtDao.updateDebt(debt);
					break;
				}

			}
			//更新融资流水
			key=debtDao.updateDebtFlow(debtflow);

			//计入系统日志
			debtflow.setOperation(5);
			addDebtLog(debtflow,debtflow.getId());

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}

		return key;
	}

	//删除还融资
	public int deleteRDebt(DebtFlow debtflow) throws Exception
	{
		int key=0;
		try{

			//得到所还融资
			List<DebtSysLog> list=debtDao.selectDebtSysLogByFlowid(debtflow.getId());

			for(int i=0;i<list.size();i++)
			{
				DebtSysLog debtsyslog=list.get(0); 
				//得到融资
				Debt debt=debtDao.selectDebtById(debtsyslog.getDebtid()).get(0);

				//得到融资流水
				DebtFlow debtflow1=debtDao.selectDebtFlowById(debt.getFlowid()).get(0);


				//更新系统日志
				debtsyslog.setDebt(0.0);
				debtsyslog.setOperation(6);
				debtDao.updateDebtSysLog(debtsyslog);
				rollInterest(debt);
				double interestnum=caculateInterest(debt,debtflow1);
				//更新融资
				debt.setDebt(debt.getDebt()-debtsyslog.getDebt());
				debt.setInterest(interestnum);
				debtDao.updateDebt(debt);


			}
			//更新融资流水
			key=debtDao.updateDebtFlow(debtflow);

			//计入系统日志
			debtflow.setOperation(6);
			addDebtLog(debtflow,debtflow.getId());

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}

		return key;
	}

	public int deleteRDebt(int flowid) throws Exception
	{
		List<DebtFlow> list=debtDao.selectDebtFlowById(flowid);
		return deleteRDebt(list.get(0));
	}

	//每日计算利息
	public int addCalc() throws Exception
	{
		//查询所有的负债
		List<Debt> list=debtDao.selectAllDebt();
		System.out.println(list.size());
		for(int i=0;i<list.size();i++)
		{
			Debt temp=list.get(i);
			System.out.println(temp.getDebt());
			if(temp.getDebt()!=0)
			{
				//计算利息				
				Calc calc=new Calc();
				calc.setDebtid(temp.getId());
				calc.setStatus(1);
				calc.setCurrency(temp.getCurrency());
				calc.setDebt(temp.getDebt());
				calc.setInterest(temp.getDebt()*temp.getRate()/360);
				calc.setTime(new Date());
				calc.setRate(temp.getRate());
				int calcid=calcDao.insertCalc(calc);
				//插入利息流水
				CalcFlow calcflow=new CalcFlow();
				calcflow.setCalcid(calcid);
				calcflow.setInterest(calc.getInterest());
				calcflow.setOperation(1);
				calcflow.setStatus(1);
				calcflow.setTimestamp(new Date());
				calcflow.setCurrency(temp.getCurrency());
				int calcflowid=calcDao.insertCalcFlow(calcflow);
				addCalcLog(calcflow,calcflowid,0);
			}
		}

		//更新负债表利息字段
		int ok=debtDao.updateAllDebtInterest();
		return ok;
	}


	//每月还利息
	public void rollCalc() throws Exception
	{

	}

	public List<Debt> selectDebtByUid(int uid) throws Exception
	{
		return debtDao.selectDebtByUid(uid);
	}

	public List<DebtFlow> selectAssetsHistoryByUid(int uid) throws Exception
	{
		return debtDao.selectAssetsHistoryByUid(uid);
	}
	
	/**
	 * 初始化现金及融资账户
	 * @param addAssets
	 * @return
	 * @throws Exception
	 */
	public int addAssets(JsonClass.addAssets addAssets,Integer uid)throws Exception
	{
		int key=0;
		try
		{
			Cash ent=new Cash();
			ent.setCash(Double.parseDouble(addAssets.getCash().getRmb()));
			ent.setUid(uid);
			ent.setRemark("InitCash");
			ent.setCurrency(1);
			ent.setCreatedate(new Date());
			ent.setUpdatedate(new Date());
			cashService.addCash(ent);
			ent=new Cash();
			ent.setCash(Double.parseDouble(addAssets.getCash().getHkd()));
			ent.setUid(uid);
			ent.setRemark("InitCash");
			ent.setCurrency(2);
			ent.setCreatedate(new Date());
			ent.setUpdatedate(new Date());
			cashService.addCash(ent);
			ent=new Cash();
			ent.setCash(Double.parseDouble(addAssets.getCash().getUsd()));
			ent.setUid(uid);
			ent.setRemark("InitCash");
			ent.setCurrency(3);
			ent.setCreatedate(new Date());
			ent.setUpdatedate(new Date());
			cashService.addCash(ent);

			Debt debt=new Debt();
			debt.setDebt(Double.parseDouble(addAssets.getFinancing().getRmb()));
			debt.setUid(uid);
			debt.setCurrency(1);
			debt.setCreatedate(new Date());
			debt.setStarttime(new Date());
			debt.setRate(0.08);
			addDebt(debt);

			debt=new Debt();
			debt.setDebt(Double.parseDouble(addAssets.getFinancing().getHkd()));
			debt.setUid(uid);
			debt.setCurrency(2);
			debt.setCreatedate(new Date());
			debt.setStarttime(new Date());
			debt.setRate(0.08);
			addDebt(debt);

			debt=new Debt();
			debt.setDebt(Double.parseDouble(addAssets.getFinancing().getUsd()));
			debt.setUid(uid);
			debt.setCurrency(3);
			debt.setCreatedate(new Date());
			debt.setStarttime(new Date());
			debt.setRate(0.08);
			addDebt(debt);
			key=1;
		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}
}
