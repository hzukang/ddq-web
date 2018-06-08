package ddq.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddq.common.Cn2Spell;
import ddq.common.HttpUtil;
import ddq.dao.DebtDao;
import ddq.dao.StockDao;
import ddq.mapper.StockMapper;
import ddq.mapper.StockShowMapper;
import ddq.mapper.StockSnapMapper;
import ddq.mapper.StockUserMapper;
import ddq.model.Cash;
import ddq.model.Currency;
import ddq.model.Debt;
import ddq.model.Global;
import ddq.model.JsonData;
import ddq.model.Stock;
import ddq.model.StockFlow;
import ddq.model.StockLog;
import ddq.model.StockPrice;
import ddq.model.StockProfit;
import ddq.model.StockShow;
import ddq.model.StockSnap;
import ddq.model.StockSysLog;
import ddq.model.StockUser;

@Service
@Transactional
public class StockService {

	@Autowired
	private DebtService debtService;
	@Autowired
	private StockDao stockDao;
	@Autowired
	private AppCache appCache;
	@Autowired
	private CashService cashService;
	@Autowired
	private DebtDao debtDao; 

	@Autowired
	private SysService sysService;

	@Autowired
	private StockMapper stockMapper;

	@Autowired
	private StockSnapMapper stockSnapMapper;

	@Autowired
	private StockUserMapper stockUserMapper;
	@Autowired
	private StockShowMapper stockShowMapper;

	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfall=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfall1=new SimpleDateFormat("yyyyMMddHHmmss");
	private static Logger logger = LoggerFactory.getLogger(StockService.class);

	/**
	 * 买股票  type-1 用现金 2 用融资 Operation 1
	 * @return 1 成功 0 失败
	 **/
	public int BuyStock(StockFlow stockflow,int type,Double rate) throws Exception 
	{
		int key=0;
		try{

			//计算操作成本
			double feeprice=0;

			int cashflowid=0;
			int debtflowid=0;
			stockflow.setOperation(1);
			//插入流水
			if(type==1)
			{
				Cash cash=new Cash();
				cash.setCurrency(stockflow.getCurrency());
				cash.setCreatedate(stockflow.getTimestamp());
				cash.setUid(stockflow.getUid());
				cash.setCash(-(stockflow.getPosition()*stockflow.getPrice()+feeprice));
				cashflowid=cashService.addCash(cash);
			}else if(type==2)
			{
				Debt debt=new Debt();
				debt.setCurrency(stockflow.getCurrency());
				debt.setCreatedate(stockflow.getTimestamp());
				debt.setUid(stockflow.getUid());
				debt.setDebt(-(stockflow.getPosition()*stockflow.getPrice()+feeprice));
				debt.setStarttime(stockflow.getTimestamp());
				debt.setRate(rate);
				debtflowid=debtService.addDebt(debt);
			}
			stockflow.setCashflowid(cashflowid);
			stockflow.setDebtflowid(debtflowid);
			stockflow.setFeeprice(feeprice);
			key=stockDao.insertStockFlow(stockflow);
			stockflow.setId(key);

			//更新用户持股
			updateStockUser(stockflow);

			//插入日志
			AddStockLog(stockflow);
			AddStockSysLog(stockflow);

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}

	//系统初始化用户股票数据
	public int InitStock(StockFlow stockflow) throws Exception
	{
		int key=0;
		try{

			//计算操作成本
			double feeprice=0;

			int cashflowid=0;
			int debtflowid=0;
			stockflow.setOperation(1);

			stockflow.setCashflowid(cashflowid);
			stockflow.setDebtflowid(debtflowid);
			stockflow.setFeeprice(feeprice);
			key=stockDao.insertStockFlow(stockflow);
			stockflow.setId(key);

			//更新用户持股
			updateStockUser(stockflow);

			//插入日志
			AddStockLog(stockflow);
			AddStockSysLog(stockflow);

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}

	public int AddStockLog(StockFlow stockflow) throws Exception
	{
		StockLog stocklog=new StockLog();
		stocklog.setFeeprice(stockflow.getFeeprice());
		stocklog.setCashflowid(stockflow.getCashflowid());
		stocklog.setDebtflowid(stockflow.getDebtflowid());
		stocklog.setOperation(stockflow.getOperation());
		stocklog.setPosition(stockflow.getPosition());
		stocklog.setPrice(stockflow.getPrice());
		stocklog.setStockid(stockflow.getStockid());
		stocklog.setTimestamp(stockflow.getTimestamp());
		stocklog.setUid(stockflow.getUid());
		stocklog.setUserlogid(Global.userlogid.get());

		int key=stockDao.insertStockLog(stocklog);
		return key;
	}

	public int AddStockSysLog(StockFlow stockflow) throws Exception
	{
		StockSysLog stocksysflow=new StockSysLog();
		stocksysflow.setFeeprice(stockflow.getFeeprice());
		stocksysflow.setCashflowid(stockflow.getCashflowid());
		stocksysflow.setDebtflowid(stockflow.getDebtflowid());
		stocksysflow.setOperation(stockflow.getOperation());
		stocksysflow.setPosition(stockflow.getPosition());
		stocksysflow.setPrice(stockflow.getPrice());
		stocksysflow.setStockid(stockflow.getStockid());
		stocksysflow.setTimestamp(stockflow.getTimestamp());
		stocksysflow.setUid(stockflow.getUid());

		int key=stockDao.insertStockSysLog(stocksysflow);
		return key;
	}

	/**
	 * 得到所有的股票信息
	 * @return
	 * @throws Exception
	 */
	public Map selectStock() throws Exception
	{
		Map result=new HashMap();
		List<Stock> list=stockDao.selectStock();

		for(int i=0;i<list.size();i++)
		{
			Stock temp =list.get(i);
			result.put(temp.getStockid(), temp);
		}

		return result;
	}

	/**
	 * 卖股票  Operation==2  
	 * @isReduceDebt 是否还融资 
	 * @return 1 成功 0 失败
	 **/
	public int SellStock(StockFlow stockflow,boolean isReduceDebt) throws Exception
	{
		int key=0;
		try
		{
			//计算卖出总额
			double feeprice=0;
			double sellcash=stockflow.getPosition()*stockflow.getPrice()-feeprice;
			stockflow.setOperation(2);
			int debtflowid=0;
			int cashflowid=0;
			if(isReduceDebt)
			{
				double debtall=0;
				Debt debt=new Debt();
				debt.setUid(stockflow.getUid());
				debt.setCurrency(stockflow.getCurrency());
				debt.setRate(0.08);

				//查询融资总额
				List<HashMap<String,Object>> list1=debtDao.selectDebtSUMByUid(debt);
				if(list1.size()>0)
				{
					HashMap<String,Object> map1=list1.get(0);
					debtall=Double.parseDouble(map1.get("debt").toString());
				}

				//全部用来还融资
				if(debtall>=sellcash)
				{
					debt.setDebt(sellcash);
					sellcash=0;
					debtflowid=debtService.reduceDebt(debt, false);
				}
				else
				{
					debt.setDebt(debtall);
					sellcash=sellcash-debtall;
					debtflowid=debtService.reduceDebt(debt, false);
				}
			}

			if(sellcash>0)//插入现金
			{
				Cash cash=new Cash();
				cash.setCash(sellcash);
				cash.setUid(stockflow.getUid());
				cash.setCurrency(stockflow.getCurrency());
				cash.setRemark("买股票收益");
				cashflowid=cashService.addCash(cash);
			}

			stockflow.setCashflowid(cashflowid);
			stockflow.setDebtflowid(debtflowid);
			stockflow.setFeeprice(feeprice);
			key=stockDao.insertStockFlow(stockflow);
			stockflow.setId(key);


			//更新用户持股
			updateStockUser(stockflow);


			//插入日志
			AddStockLog(stockflow);
			AddStockSysLog(stockflow);

		}catch(Exception e)
		{
			e.printStackTrace();

			key=0;
			throw e;
		}
		return key;
	}


	/**
	 * 删除买股票  Operation 3
	 * @return 1 成功 0 失败
	 **/
	public int deleteBuyStock(StockFlow stockflow) throws Exception
	{
		int key=0;
		try
		{
			stockflow.setOperation(3);
			List<StockFlow> list=stockDao.selectStockFlowById(stockflow.getId());
			if(list.size()>0)
			{
				stockflow=list.get(0);
				int cashflowid=stockflow.getCashflowid();
				int debtflowid=stockflow.getDebtflowid();

				//回滚现金流水
				if(cashflowid>0)
				{
					cashService.deleteCashFlowById(cashflowid);
				}
				//回滚融资流水
				if(debtflowid>0)
				{
					debtService.deleteDebt(debtflowid);
				}

				key=stockDao.delStockFlow(stockflow.getId());

				//更新用户持股
				updateStockUser(stockflow);


				//插入日志
				AddStockLog(stockflow);
				AddStockSysLog(stockflow);

			}
		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}


	/**
	 * 删除卖股票  Operation 4
	 * @return 1 成功 0 失败
	 **/
	public int deleteSellStock(StockFlow stockflow) throws Exception
	{
		int key=0;
		try
		{
			stockflow.setOperation(4);
			List<StockFlow> list=stockDao.selectStockFlowById(stockflow.getId());
			if(list.size()>0)
			{
				stockflow=list.get(0);
				int cashflowid=stockflow.getCashflowid();
				int debtflowid=stockflow.getDebtflowid();

				//回滚现金流水
				if(cashflowid>0)
				{
					cashService.deleteCashFlowById(cashflowid);
				}
				//回滚融资流水
				if(debtflowid>0)
				{
					debtService.deleteRDebt(debtflowid);
				}

				key=stockDao.delStockFlow(stockflow.getId());

				//更新用户持股
				updateStockUser(stockflow);

				//插入日志
				AddStockLog(stockflow);
				AddStockSysLog(stockflow);

			}
		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}



	/**
	 * 每日计算  map《股票ID,当前价格》
	 * @return 
	 */
	public int addProfit(Map map,Date date) throws Exception
	{
		int key=0;
		try
		{
			//删除单日计算
			stockDao.deleteStockSnapByTime(sdf.format(date));
			stockDao.deleteStockProfitByTime(sdf.format(date));

			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -1);
			List<Stock> list4=stockDao.selectAllStock();
			List<StockFlow> list2=stockDao.selectStockFlowByDate(sdf.format(date)); 
			List<StockSnap> list1=stockDao.selectStockSnapByDate(sdf.format(cal.getTime()));
			List<StockUser> list=stockDao.selectAllStockUser();

			List<StockProfit> insertlist=new ArrayList<StockProfit>();
			List<StockSnap> insertlist1=new ArrayList<StockSnap>();

			List<Cash> cashlist=cashService.seleteAllCash();

			Map map1=new HashMap();//收盘价格 用户持股
			Map map2=new HashMap();
			List<StockUser> list3=new ArrayList<StockUser>();//用户日盈亏
			List<StockUser> list5=new ArrayList<StockUser>();//市场日盈亏
			for(int i=0;i<list1.size();i++)
			{
				StockSnap temp=list1.get(i);
				map1.put(temp.getStockid(), temp);
			}

			for(int i=0;i<list2.size();i++)
			{
				StockFlow temp=list2.get(i);
				map1.put(temp.getStockid()+"_"+temp.getUid()+"_"+temp.getOperation(), temp);
			}


			for(int i=0;i<list4.size();i++)
			{
				Stock temp=list4.get(i);
				StockSnap stocksnap=new StockSnap();
				//stocksnap.setPosition(temp.getPosition());
				if(map.get(temp.getStockid())!=null)
				{
					stocksnap.setPrice((Double)map.get(temp.getStockid()));
					stocksnap.setStockid(temp.getStockid());
					stocksnap.setTimestamp(date);
					insertlist1.add(stocksnap);
				}
			}


			for(int i=0;i<list.size();i++)
			{
				StockUser temp=list.get(i);

				Double exchange=(Double)map.get("ex_"+temp.getMarket());//对人民币汇率
				exchange=exchange==null?1:exchange;

				StockSnap temp1=(StockSnap)map1.get(temp.getStockid());
				StockFlow temp2=(StockFlow)map1.get(temp.getStockid()+"_"+temp.getUid()+"_1");//买入流水
				StockFlow temp3=(StockFlow)map1.get(temp.getStockid()+"_"+temp.getUid()+"_2");//卖出流水
				temp1=temp1==null?new StockSnap():temp1;
				temp2=temp2==null?new StockFlow():temp2;
				temp3=temp3==null?new StockFlow():temp3;
				int zrccs=temp.getPosition()-temp2.getPosition()+temp3.getPosition();
				Double qqjg=(Double)map.get(temp.getStockid());
				qqjg=qqjg==null?0:qqjg;
				//日盈亏=（昨日持股数−今卖出数）∗（当前股价−昨收盘价）+今买入数∗（当前股价−今买入价）+今卖出数∗（卖出价−昨收盘价）
				double profit=(zrccs-temp3.getPosition())*(qqjg-temp1.getPrice())+
						temp2.getPosition()*qqjg-temp3.getPosition()*temp1.getPrice()-temp2.getPrice()+temp3.getPrice();
				//double percentage=profit/(zrccs*temp1.getPrice()+temp2.getPrice());

				//累计盈亏=当前股价∗持股数−∑买入金额+∑卖出金额
				double profit1=qqjg*temp.getPosition()-temp.getBuycash()+temp.getSellcash();
				//double percentage1=profit1/temp.getBuycash();

				if(!map2.containsKey(temp.getUid()+"profit"))
				{
					map2.put(temp.getUid()+"profit", profit1*exchange);
					map2.put(temp.getUid()+"buycash", temp.getBuycash()*exchange);
					map2.put(temp.getUid()+"day", profit*exchange);
					map2.put(temp.getUid()+"dayc", (zrccs*temp1.getPrice()+temp2.getPrice())*exchange);

					map2.put(temp.getUid()+"total",temp.getPosition()*qqjg*exchange);
					list3.add(temp);
				}
				else
				{
					double profittemp=(Double)map2.get(temp.getUid()+"profit")+profit1*exchange;
					double buycashtemp=(Double)map2.get(temp.getUid()+"buycash")+temp.getBuycash()*exchange;
					double totaltemp=(Double)map2.get(temp.getUid()+"total")+temp.getPosition()*qqjg*exchange;

					map2.remove(temp.getUid()+"profit");
					map2.remove(temp.getUid()+"buycash");
					map2.remove(temp.getUid()+"total");

					map2.put(temp.getUid()+"profit", profittemp);
					map2.put(temp.getUid()+"buycash", buycashtemp);
					map2.put(temp.getUid()+"total",totaltemp);

					double datep=(Double)map2.get(temp.getUid()+"day")+profit*exchange;
					double datec=(Double)map2.get(temp.getUid()+"dayc")+(zrccs*temp1.getPrice()+temp2.getPrice())*exchange;
					map2.remove(temp.getUid()+"day");
					map2.remove(temp.getUid()+"dayc");
					map2.put(temp.getUid()+"day", datep);
					map2.put(temp.getUid()+"dayc", datec);
				}


				String market=temp.getUid()+"_"+temp.getMarket();
				if(!map2.containsKey(market+"day"))
				{
					map2.put(market+"day", profit*exchange);
					map2.put(market+"dayc",(zrccs*temp1.getPrice()+temp2.getPrice())*exchange);

					map2.put(market+"tday", profit1*exchange);
					map2.put(market+"tdayc", temp.getBuycash()*exchange);
					list5.add(temp);
				}
				else
				{
					double tday=(Double)map2.get(market+"tday")+profit1*exchange;
					double tdayc=(Double)map2.get(market+"tdayc")+temp.getBuycash()*exchange;
					map2.remove(market+"tday");
					map2.remove(market+"tdayc");
					map2.put(market+"tday", tday);
					map2.put(market+"tdayc", tdayc);

					double day=(Double)map2.get(market+"day")+profit*exchange;
					double dayc=(Double)map2.get(market+"dayc")+(zrccs*temp1.getPrice()+temp2.getPrice())*exchange;
					map2.remove(market+"day");
					map2.remove(market+"dayc");
					map2.put(market+"day", day);
					map2.put(market+"dayc", dayc);
				}

				StockProfit stockprofit=new StockProfit();
				int markid=Integer.parseInt(temp.getMarket());
				stockprofit.setCtype(2);
				stockprofit.setStatus(1);
				stockprofit.setTotal((zrccs*temp1.getPrice()+temp2.getPrice()));
				stockprofit.setProfit(profit);
				stockprofit.setStockid(temp.getStockid());
				stockprofit.setTimestamp(date);
				stockprofit.setYtdposition(zrccs);
				stockprofit.setUid(temp.getUid());
				stockprofit.setCurrency(markid);

				insertlist.add(stockprofit);//每日盈亏
				StockProfit stockprofit1=new StockProfit();
				stockprofit1.setStatus(1);
				stockprofit1.setStockid(temp.getStockid());
				stockprofit1.setTimestamp(date);
				stockprofit1.setUid(temp.getUid());
				stockprofit1.setCtype(3);
				stockprofit1.setTotal(temp.getBuycash());
				stockprofit1.setProfit(profit1);
				stockprofit1.setCurrency(markid);
				insertlist.add(stockprofit1);//累计盈亏

			}

			//计算用户累计盈亏 日盈亏 
			for(int i=0;i<list3.size();i++)
			{
				StockUser temp=list3.get(i);
				double profittemp=(Double)map2.get(temp.getUid()+"profit");
				double buycashtemp=(Double)map2.get(temp.getUid()+"buycash");

				double datep=(Double)map2.get(temp.getUid()+"day");
				double datec=(Double)map2.get(temp.getUid()+"dayc");

				Double totle=(Double)map2.get(temp.getUid()+"total");

				StockProfit stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(5);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setStockid(temp.getStockid());
				stockprofit.setTotal(buycashtemp);
				stockprofit.setProfit(profittemp);
				stockprofit.setCurrency(1);
				insertlist.add(stockprofit);//累计盈亏

				stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(4);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setStockid(temp.getStockid());
				stockprofit.setTotal(datec);
				stockprofit.setProfit(datep);
				stockprofit.setCurrency(1);
				insertlist.add(stockprofit);//日盈亏

				double cash=getCashByUid(cashlist,temp.getUid(),map);

				stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(8);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setTotal(cash);
				stockprofit.setProfit(totle);
				stockprofit.setCurrency(1);

				insertlist.add(stockprofit);//总资产快照

			}

			//计算市场累计盈亏 日盈亏
			for(int i=0;i<list5.size();i++)
			{
				StockUser temp=list5.get(i);

				String market=temp.getUid()+"_"+temp.getMarket();
				double profittemp=(Double)map2.get(market+"tday");
				double buycashtemp=(Double)map2.get(market+"tdayc");

				double datep=(Double)map2.get(market+"day");
				double datec=(Double)map2.get(market+"dayc");

				StockProfit stockprofit=new StockProfit();
				int marketid=Integer.parseInt(temp.getMarket());
				stockprofit.setStatus(1);
				stockprofit.setCtype(7);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setMarket(marketid);
				stockprofit.setTotal(buycashtemp);
				stockprofit.setProfit(profittemp);
				stockprofit.setCurrency(marketid);
				insertlist.add(stockprofit);//累计盈亏

				stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(6);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setCurrency(marketid);
				stockprofit.setMarket(marketid);
				stockprofit.setTotal(datec);
				stockprofit.setProfit(datep);
				insertlist.add(stockprofit);//日盈亏

			}

			//System.out.println("sizettt:"+insertlist.size()+"tamp:"+insertlist.get(0).getTimestamp());

			key=stockDao.insertStockProfitByList(insertlist);
			stockDao.insertStockSnapByList(insertlist1);

			//插入日志

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}

	/**
	 * 每日计算  map《股票ID,当前价格》
	 * @return 
	 */
	public int addProfit_job(Map map,Date date) throws Exception
	{
		int key=0;
		try
		{
			//删除单日计算
			stockDao.deleteStockProfitByTime(sdf.format(date));

			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -1);

			List<StockFlow> list2=stockDao.selectStockFlowByDate(sdf.format(date)); 
			List<StockSnap> list1=stockDao.selectStockSnapByDate(sdf.format(cal.getTime()));
			List<StockUser> list=stockDao.selectAllStockUser();

			List<StockProfit> insertlist=new ArrayList<StockProfit>();
			List<Cash> cashlist=cashService.seleteAllCash();

			Map map1=new HashMap();//收盘价格 用户持股
			Map map2=new HashMap();
			List<StockUser> list3=new ArrayList<StockUser>();//用户日盈亏
			List<StockUser> list5=new ArrayList<StockUser>();//市场日盈亏
			for(int i=0;i<list1.size();i++)
			{
				StockSnap temp=list1.get(i);
				map1.put(temp.getStockid(), temp);
			}

			for(int i=0;i<list2.size();i++)
			{
				StockFlow temp=list2.get(i);
				map1.put(temp.getStockid()+"_"+temp.getUid()+"_"+temp.getOperation(), temp);
			}


			for(int i=0;i<list.size();i++)
			{
				StockUser temp=list.get(i);

				Double exchange=(Double)map.get("ex_"+temp.getMarket());//对人民币汇率
				exchange=exchange==null?1:exchange;

				StockSnap temp1=(StockSnap)map1.get(temp.getStockid());
				StockFlow temp2=(StockFlow)map1.get(temp.getStockid()+"_"+temp.getUid()+"_1");//买入流水
				StockFlow temp3=(StockFlow)map1.get(temp.getStockid()+"_"+temp.getUid()+"_2");//卖出流水
				temp1=temp1==null?new StockSnap():temp1;
				temp2=temp2==null?new StockFlow():temp2;
				temp3=temp3==null?new StockFlow():temp3;
				int zrccs=temp.getPosition()-temp2.getPosition()+temp3.getPosition();
				Double qqjg=(Double)map.get(temp.getStockid());
				qqjg=qqjg==null?0:qqjg;
				//日盈亏=（昨日持股数−今卖出数）∗（当前股价−昨收盘价）+今买入数∗（当前股价−今买入价）+今卖出数∗（卖出价−昨收盘价）
				double profit=(zrccs-temp3.getPosition())*(qqjg-temp1.getPrice())+
						temp2.getPosition()*qqjg-temp3.getPosition()*temp1.getPrice()-temp2.getPrice()+temp3.getPrice();
				//double percentage=profit/(zrccs*temp1.getPrice()+temp2.getPrice());

				//累计盈亏=当前股价∗持股数−∑买入金额+∑卖出金额
				double profit1=qqjg*temp.getPosition()-temp.getBuycash()+temp.getSellcash();
				//double percentage1=profit1/temp.getBuycash();

				if(!map2.containsKey(temp.getUid()+"profit"))
				{
					map2.put(temp.getUid()+"profit", profit1*exchange);
					map2.put(temp.getUid()+"buycash", temp.getBuycash()*exchange);
					map2.put(temp.getUid()+"day", profit*exchange);
					map2.put(temp.getUid()+"dayc", (zrccs*temp1.getPrice()+temp2.getPrice())*exchange);

					map2.put(temp.getUid()+"total",temp.getPosition()*qqjg*exchange);
					list3.add(temp);
				}
				else
				{
					double profittemp=(Double)map2.get(temp.getUid()+"profit")+profit1*exchange;
					double buycashtemp=(Double)map2.get(temp.getUid()+"buycash")+temp.getBuycash()*exchange;
					double totaltemp=(Double)map2.get(temp.getUid()+"total")+temp.getPosition()*qqjg*exchange;

					map2.remove(temp.getUid()+"profit");
					map2.remove(temp.getUid()+"buycash");
					map2.remove(temp.getUid()+"total");

					map2.put(temp.getUid()+"profit", profittemp);
					map2.put(temp.getUid()+"buycash", buycashtemp);
					map2.put(temp.getUid()+"total",totaltemp);

					double datep=(Double)map2.get(temp.getUid()+"day")+profit*exchange;
					double datec=(Double)map2.get(temp.getUid()+"dayc")+(zrccs*temp1.getPrice()+temp2.getPrice())*exchange;
					map2.remove(temp.getUid()+"day");
					map2.remove(temp.getUid()+"dayc");
					map2.put(temp.getUid()+"day", datep);
					map2.put(temp.getUid()+"dayc", datec);
				}


				String market=temp.getUid()+"_"+temp.getMarket();
				if(!map2.containsKey(market+"day"))
				{
					map2.put(market+"day", profit*exchange);
					map2.put(market+"dayc",(zrccs*temp1.getPrice()+temp2.getPrice())*exchange);

					map2.put(market+"tday", profit1*exchange);
					map2.put(market+"tdayc", temp.getBuycash()*exchange);
					list5.add(temp);
				}
				else
				{
					double tday=(Double)map2.get(market+"tday")+profit1*exchange;
					double tdayc=(Double)map2.get(market+"tdayc")+temp.getBuycash()*exchange;
					map2.remove(market+"tday");
					map2.remove(market+"tdayc");
					map2.put(market+"tday", tday);
					map2.put(market+"tdayc", tdayc);

					double day=(Double)map2.get(market+"day")+profit*exchange;
					double dayc=(Double)map2.get(market+"dayc")+(zrccs*temp1.getPrice()+temp2.getPrice())*exchange;
					map2.remove(market+"day");
					map2.remove(market+"dayc");
					map2.put(market+"day", day);
					map2.put(market+"dayc", dayc);
				}

				StockProfit stockprofit=new StockProfit();
				int marketid=Integer.parseInt(temp.getMarket());
				stockprofit.setCtype(2);
				stockprofit.setStatus(1);
				stockprofit.setTotal((zrccs*temp1.getPrice()+temp2.getPrice()));
				stockprofit.setProfit(profit);
				stockprofit.setStockid(temp.getStockid());
				stockprofit.setTimestamp(date);
				stockprofit.setYtdposition(zrccs);
				stockprofit.setUid(temp.getUid());
				stockprofit.setCurrency(marketid);

				insertlist.add(stockprofit);//每日盈亏
				StockProfit stockprofit1=new StockProfit();
				stockprofit1.setStatus(1);
				stockprofit1.setStockid(temp.getStockid());
				stockprofit1.setTimestamp(date);
				stockprofit1.setUid(temp.getUid());
				stockprofit1.setCtype(3);
				stockprofit1.setTotal(temp.getBuycash());
				stockprofit1.setProfit(profit1);
				stockprofit1.setCurrency(marketid);
				insertlist.add(stockprofit1);//累计盈亏

			}

			//计算用户累计盈亏 日盈亏 
			for(int i=0;i<list3.size();i++)
			{
				StockUser temp=list3.get(i);
				double profittemp=(Double)map2.get(temp.getUid()+"profit");
				double buycashtemp=(Double)map2.get(temp.getUid()+"buycash");

				double datep=(Double)map2.get(temp.getUid()+"day");
				double datec=(Double)map2.get(temp.getUid()+"dayc");

				Double totle=(Double)map2.get(temp.getUid()+"total");

				StockProfit stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(5);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setStockid(temp.getStockid());
				stockprofit.setTotal(buycashtemp);
				stockprofit.setProfit(profittemp);
				stockprofit.setCurrency(1);
				insertlist.add(stockprofit);//累计盈亏

				stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(4);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setStockid(temp.getStockid());
				stockprofit.setTotal(datec);
				stockprofit.setProfit(datep);
				stockprofit.setCurrency(1);
				insertlist.add(stockprofit);//日盈亏

				double cash=getCashByUid(cashlist,temp.getUid(),map);

				stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(8);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setTotal(cash); //现金总额
				stockprofit.setProfit(totle);// 股票总额
				stockprofit.setCurrency(1);

				insertlist.add(stockprofit);//总资产快照

			}

			//计算市场累计盈亏 日盈亏
			for(int i=0;i<list5.size();i++)
			{
				StockUser temp=list5.get(i);
				int markid=Integer.parseInt(temp.getMarket());

				String market=temp.getUid()+"_"+temp.getMarket();
				double profittemp=(Double)map2.get(market+"tday");
				double buycashtemp=(Double)map2.get(market+"tdayc");

				double datep=(Double)map2.get(market+"day");
				double datec=(Double)map2.get(market+"dayc");

				StockProfit stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(7);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setMarket(markid);
				stockprofit.setTotal(buycashtemp);
				stockprofit.setProfit(profittemp);
				stockprofit.setCurrency(markid);
				insertlist.add(stockprofit);//累计盈亏

				stockprofit=new StockProfit();
				stockprofit.setStatus(1);
				stockprofit.setCtype(6);
				stockprofit.setTimestamp(date);
				stockprofit.setUid(temp.getUid());
				stockprofit.setCurrency(markid);
				stockprofit.setMarket(markid);
				stockprofit.setTotal(datec);
				stockprofit.setProfit(datep);
				insertlist.add(stockprofit);//日盈亏

			}

			for(int i=0;i<insertlist.size();i++)
			{
				StockProfit stockprofit=insertlist.get(i);
				key=stockDao.insertStockProfit(stockprofit);
			}

			//插入日志

		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}

	/**
	 * 记录收盘价
	 */
	public int addSnap(Map map,Date date) throws Exception
	{
		List<StockSnap> insertlist=new ArrayList<StockSnap>();
		stockSnapMapper.deleteStockSnapByTime(sdf.format(date));
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {  
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Double value = (Double)entry.getValue();
			StockSnap stocksnap=new StockSnap();
			stocksnap.setPrice(value);
			stocksnap.setStockid(key);
			stocksnap.setTimestamp(date);
			insertlist.add(stocksnap);
		}
		return stockSnapMapper.insertStockSnapByList(insertlist);
	}

	private Double getCashByUid(List<Cash> list,int uid,Map map)
	{
		Double result=0.0;
		for(int i=0;i<list.size();i++)
		{
			Cash temp=list.get(i);
			Double exchange=(Double)map.get("ex_"+temp.getCurrency());//对人民币汇率
			exchange=exchange==null?1:exchange;

			if(temp.getUid()==uid)
			{
				result=result+exchange*temp.getCash();
			}
		}

		return result;
	}

	/**
	 * 用户股票信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map selectProfit(Map map) throws Exception
	{
		Map result=new HashMap();
		try{
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -1);

			List<StockUser> list=stockDao.selectStockUserByUid((Integer)map.get("uid"));
			List<StockProfit> list1=stockDao.selectStockProfitByTime(map);
			List<StockFlow> list4=stockDao.selectStockFlowByDate(sdf.format(new Date()));
			List<StockSnap> list5=stockDao.selectStockSnapByDate(sdf.format(cal.getTime()));
			Integer uid=(Integer)map.get("uid");
			StockProfit model=new StockProfit();
			model.setUid(uid);
			model.setCtype(1);
			List<StockProfit> list6=stockDao.selectStockProfitByCtype(model);

			List<Map> list3=new ArrayList<Map>();
			Map map1=new HashMap();


			for(int i=0;i<list1.size();i++)//股票盈利
			{
				StockProfit temp=list1.get(i);
				if(temp.getCtype()==2||temp.getCtype()==3)
				{
					map1.put(temp.getStockid()+"_"+sdf.format(temp.getTimestamp())+"_"+temp.getCtype(), temp);
				}
				else if(temp.getCtype()==4||temp.getCtype()==5||temp.getCtype()==8)
				{
					map1.put(sdf.format(temp.getTimestamp())+"_"+temp.getCtype(), temp);
				}
				else if(temp.getCtype()==6||temp.getCtype()==7)
				{
					map1.put(temp.getMarket()+"_"+sdf.format(temp.getTimestamp())+"_"+temp.getCtype(), temp);
				}

			}

			for(int i=0;i<list4.size();i++)//操作流水
			{
				StockFlow temp=list4.get(i);
				map1.put(temp.getStockid()+"_"+temp.getOperation(), temp);
			}

			for(int i=0;i<list5.size();i++)//昨日收盘价
			{
				StockSnap temp=list5.get(i);
				map1.put(temp.getStockid()+"ytd", temp);
			}

			for(int i=0;i<list6.size();i++)//平仓盈亏
			{
				StockProfit temp=list6.get(i);
				map1.put(temp.getStockid()+"buy", temp.getProfit());
			}

			List<Map> datelist=new ArrayList<Map>();
			List<Map> stocklist=new ArrayList<Map>();

			Integer date=(Integer)map.get("date");
			date=date==null?0:date;


			Double cash=0.0;
			//当日现金计算
			List<Cash> cashlist=cashService.seleteCashByUid((Integer)map.get("uid"));
			cash=getCashByUid(cashlist,uid,getExChangeRate(map));

			//组装date
			for(int i=0;i<date;i++)
			{
				cal.setTime(new Date());
				cal.add(Calendar.DATE, -i);
				if(IsMarketClose(cal.getTime())==0)
				{
					continue;
				}
				String d=sdf.format(cal.getTime());
				Map temp=new HashMap();

				StockProfit temp1=(StockProfit)map1.get(d+"_4");
				StockProfit temp2=(StockProfit)map1.get(d+"_5");
				StockProfit temp3=(StockProfit)map1.get(d+"_8");
				temp1=temp1==null?new StockProfit():temp1;
				temp2=temp2==null?new StockProfit():temp2;
				temp3=temp3==null?new StockProfit():temp3;
				temp.put("time",d);
				temp.put("day",temp1.getProfit());
				temp.put("dayPercent",temp1.getProfit()/(temp1.getTotal()==0?1:temp1.getTotal()));
				temp.put("dayStatus", temp1.getProfit()>=0?"up":"down");
				temp.put("totalstock",temp3.getProfit());
				temp.put("totalcash",temp3.getTotal());
				temp.put("total",temp2.getProfit());
				temp.put("totalPercent",temp2.getProfit()/(temp2.getTotal()==0?1:temp2.getTotal()));
				temp.put("totalStatus", temp2.getProfit()>=0?"up":"down");

				datelist.add(temp);
			}

			String stockids="";
			for(int i=0;i<list.size();i++)//股票
			{
				Map temp=new HashMap();
				StockUser temp1=list.get(i);

				temp.put("buycash", temp1.getBuycash());
				temp.put("sellcash", temp1.getSellcash());

				temp.put("market", temp1.getMarket());
				temp.put("position", temp1.getPosition());
				temp.put("costprice", temp1.getCostprice());
				temp.put("dilutedprice", temp1.getDilutedprice());
				temp.put("id", temp1.getStockid().toLowerCase());
				temp.put("liquidation",map1.get(temp1.getStockid()+"buy"));

				StockSnap temp2=(StockSnap)map1.get(temp1.getStockid()+"ytd");
				temp2=temp2==null?new StockSnap():temp2;
				temp.put("ytdprice", temp2.getPrice());

				StockFlow temp3=(StockFlow)map1.get(temp1.getStockid()+"_1");//买入流水
				StockFlow temp4=(StockFlow)map1.get(temp1.getStockid()+"_2");//卖出流水

				temp3=temp3==null?new StockFlow():temp3;
				temp4=temp4==null?new StockFlow():temp4;
				temp.put("jbuycash", temp3.getPrice());
				temp.put("jbuynum", temp3.getPosition());
				temp.put("jsellcash", temp4.getPrice());
				temp.put("jsellnum", temp4.getPosition());
				stocklist.add(temp);
				if(temp1.getMarket().equals("1"))
				{
					stockids=stockids+","+temp1.getStockid().toLowerCase();
				}else if(temp1.getMarket().equals("2"))
				{
					stockids=stockids+","+temp1.getStockid().toLowerCase();
				}else if(temp1.getMarket().equals("3"))
				{
					stockids=stockids+",us"+temp1.getStockid().toUpperCase();
				}

			}


			result.put("date", datelist);
			result.put("stock", stocklist);
			result.put("day", map.get("datenum"));
			result.put("cash",cash);
			result.put("stockids",stockids);
			//map1.keySet()
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=null;
			throw e;
		}
		return result;
	}

	/**
	 * Operation 1 买股票 2 卖出股票 3 删除买股票  4  删除卖出股票
	 * @return 1 成功 0 失败
	 **/
	public int updateStockUser(StockFlow stockflow) throws Exception
	{
		int key=0;
		Map map=new HashMap();
		map.put("uid", stockflow.getUid());
		map.put("stockid",stockflow.getStockid());
		StockUser stockuser=new StockUser();
		//查询股票持仓数
		List<StockUser> list=stockDao.selectStockUser(map);
		if(list.size()>0)
		{
			stockuser=list.get(0);
		}

		double costprice=0;
		Double dilutedprice=0.0;
		//计算持股成本及摊薄成本
		if(stockflow.getOperation()==1)
		{

			costprice=(stockuser.getBuycash()+stockflow.getPosition()*stockflow.getPrice())
					/(stockuser.getBuynum()+stockflow.getPosition());

			dilutedprice=(stockflow.getPosition()*stockflow.getPrice()+stockuser.getBuycash()-stockuser.getSellcash())/(stockuser.getPosition()+stockflow.getPosition());

			stockuser.setCostprice(costprice);
			stockuser.setBuycash(stockuser.getBuycash()+stockflow.getPosition()*stockflow.getPrice());
			stockuser.setBuynum(stockuser.getBuynum()+stockflow.getPosition());
			stockuser.setSellcash(stockuser.getSellcash());
			stockuser.setSellnum(stockuser.getSellnum());
			stockuser.setDilutedprice(dilutedprice);
			stockuser.setPosition(stockuser.getPosition()+stockflow.getPosition());
			stockuser.setUid(stockflow.getUid());
			stockuser.setStockid(stockflow.getStockid());
		}else if(stockflow.getOperation()==2)
		{
			costprice=(stockuser.getBuycash())
					/(stockuser.getBuynum());

			if(stockuser.getPosition()-stockflow.getPosition()==0)
			{
				dilutedprice=null;
			}
			else
			{
				dilutedprice=(stockuser.getBuycash()-stockuser.getSellcash()-stockflow.getPosition()*stockflow.getPrice())
						/(stockuser.getPosition()-stockflow.getPosition());
			}

			stockuser.setCostprice(costprice);
			stockuser.setBuycash(stockuser.getBuycash());
			stockuser.setBuynum(stockuser.getBuynum());
			stockuser.setSellcash(stockuser.getSellcash()+stockflow.getPosition()*stockflow.getPrice());
			stockuser.setSellnum(stockuser.getSellnum()+stockflow.getPosition());
			stockuser.setDilutedprice(dilutedprice);
			stockuser.setPosition(stockuser.getPosition()-stockflow.getPosition());
			stockuser.setUid(stockflow.getUid());
			stockuser.setStockid(stockflow.getStockid());
		}else if(stockflow.getOperation()==3)
		{
			costprice=(stockuser.getBuycash()-stockflow.getPosition()*stockflow.getPrice())
					/(stockuser.getBuynum()-stockflow.getPosition());

			dilutedprice=(stockuser.getBuycash()-stockuser.getSellcash()-stockflow.getPosition()*stockflow.getPrice())
					/(stockuser.getPosition()-stockflow.getPosition());

			stockuser.setCostprice(costprice);
			stockuser.setBuycash(stockuser.getBuycash()-stockflow.getPosition()*stockflow.getPrice());
			stockuser.setBuynum(stockuser.getBuynum()-stockflow.getPosition());
			stockuser.setSellcash(stockuser.getSellcash());
			stockuser.setSellnum(stockuser.getSellnum());
			stockuser.setDilutedprice(dilutedprice);
			stockuser.setPosition(stockuser.getPosition()-stockflow.getPosition());
			stockuser.setUid(stockflow.getUid());
			stockuser.setStockid(stockflow.getStockid());
		}else if(stockflow.getOperation()==4)
		{
			costprice=(stockuser.getBuycash())
					/(stockuser.getBuynum());

			dilutedprice=(stockuser.getBuycash()-stockuser.getSellcash()+stockflow.getPosition()*stockflow.getPrice())
					/(stockuser.getPosition()+stockflow.getPosition());

			stockuser.setCostprice(costprice);
			stockuser.setBuycash(stockuser.getBuycash());
			stockuser.setBuynum(stockuser.getBuynum());
			stockuser.setSellcash(stockuser.getSellcash()-stockflow.getPosition()*stockflow.getPrice());
			stockuser.setSellnum(stockuser.getSellnum()-stockflow.getPosition());
			stockuser.setDilutedprice(dilutedprice);
			stockuser.setPosition(stockuser.getPosition()+stockflow.getPosition());
			stockuser.setUid(stockflow.getUid());
			stockuser.setStockid(stockflow.getStockid());
		}
		else
		{
			return 0;
		}

		//卖出股票 及删除卖出股票流水  更新平仓盈亏
		if(stockflow.getOperation()==2)
		{
			double profit=(stockflow.getPrice()-stockuser.getCostprice())*stockflow.getPosition();
			//double percentage=profit/(stockflow.getPrice()*stockflow.getPosition());

			StockProfit stockprofit=new StockProfit();
			stockprofit.setFlowid(stockflow.getId());
			stockprofit.setCurrency(stockflow.getCurrency());
			stockprofit.setMarket(stockflow.getCurrency());
			stockprofit.setProfit(profit);
			stockprofit.setStockid(stockflow.getStockid());
			stockprofit.setStatus(stockflow.getStatus());
			stockprofit.setTimestamp(stockflow.getTimestamp());
			stockprofit.setUid(stockflow.getUid());
			stockprofit.setCtype(1);
			stockprofit.setTotal((stockflow.getPrice()*stockflow.getPosition()));
			stockDao.insertStockProfit(stockprofit);
		}

		if(stockflow.getOperation()==4)
		{
			stockDao.delStockProfit(stockflow.getId());
		}

		//更新用户持股
		if(list.size()>0)
		{
			key=stockDao.updateStockUser(stockuser);
		}else
		{
			key=stockDao.insertStockUser(stockuser);
		}
		return key;
	}


	/**
	 * 得到股票的实时数据
	 * @param stockids
	 * @param market
	 * @return
	 * @throws Exception
	 */
	public Map getStockData(String stockids) throws Exception {

		boolean isclose=true;//是否休市
		Date newdate=new Date();
		String date=sdf.format(new Date());

		if((newdate.getTime()>sdfall.parse(date+" 09:30:00").getTime()&&newdate.getTime()<sdfall.parse(date+" 11:30:00").getTime())|| 
				(newdate.getTime()>sdfall.parse(date+" 13:00:00").getTime()&&newdate.getTime()<sdfall.parse(date+" 15:00:00").getTime()))
		{
			isclose=false;
		}
		Map map=appCache.getStockData();

		String[] ids=stockids.split(",");
		String strArray="";
		for(int i=0;i<ids.length;i++)
		{
			String arr=ids[i];
			if(map.containsKey(arr))
			{
				StockPrice temp=(StockPrice)map.get(arr);
				long d=newdate.getTime()-temp.getTime().getTime();
				long secs=d/(1000);
				System.out.println(secs+"_"+arr+sdfall.format(temp.getTime()));
				if(Math.abs(secs)>=10)
				{
					strArray=strArray+","+arr;
				}

				/*if(IsMarketClose(new Date())==0)
				{
					isclose=false;
				}*/

			}
			else
			{
				strArray=strArray+","+arr;
				isclose=false;
			}
		}


		if(strArray.replace(",", "").isEmpty()||isclose)
		{
			System.out.println("ff");
			return map;
		}

		//发送http 请求
		String result=HttpUtil.sendGet("http://qt.gtimg.cn/", "q="+strArray);

		/**
		 * 腾讯财经接口解析
		 * P@#%$#%@@#$ 【{0-->v_sh600399="1},{1-->抚顺特钢},{2-->600399},{3-->11.55 当前价格},{4-->11.99 昨收},{5-->11.62 今开},{6-->658600 成交量},{7-->305912},{8-->352688},{9-->11.58},{10-->85},
		 * {11-->11.57},{12-->8},{13-->11.56},{14-->15},{15-->11.55},{16-->1055},{17-->11.54},{18-->134},{19-->11.60},{20-->178},{21-->11.61},{22-->860},{23-->11.62},{24-->24},{25-->11.63},{26-->45},
		 * {27-->11.65},{28-->194},{29-->14:59:58/11.55/115/S/133204/9307|14:59:53/11.58/454/M/524520/9304|14:59:48/11.55/216/S/249736/9301|14:59:43/11.58/264/B/305217/9297|14:59:38/11.55/707/S/818944/9294|14:59:33/11.55/166/S/191771/9290},
		 * {30-->20150821150349 time},{31-->-0.44 涨跌额},{32-->-3.67 涨跌福},{33-->12.30 最高},{34-->11.17 最低},{35-->11.52/654368/772407567},{36-->658600},{37-->77730 成交额 万元},{38-->5.83 换手率},{39-->91.94 市盈率},{40--> 是否停牌},{41-->12.30 最高},{42-->11.17 最低},{43-->9.42 振幅},
		 * {44-->130.47 流通市值 亿元 },{45-->150.15 总市值},{46-->8.10 市净率},{47-->13.19 },{48-->10.79},{49-->"}】

           P@#%$#%@@#$ 【{0-->v_usNKE="200},{1-->耐克},{2-->NKE.N},{3-->106.87 当前价格},{4-->112.30 昨收},{5-->111.47 今开},{6-->7933083 成交量},{7-->0},{8-->0},{9-->107.00},{10-->1.00},{11-->0},{12-->0},{13-->0},{14-->0},
           {15-->0},{16-->0},{17-->0},{18-->0},{19-->107.29},{20-->1.00},{21-->0},{22-->0},{23-->0},{24-->0},{25-->0},{26-->0},{27-->0},{28-->0},{29-->371350},{30-->2015-08-21 15:59:37 time},{31-->-5.43 涨跌额},{32-->-4.84 涨跌福},{33-->111.66 最高价},{34-->106.84 最低价},{35-->green},{36-->7933083 成交量},{37-->0},{38-->0},
           {39-->33.50 市盈率},{40-->},{41-->0},{42-->0},{43-->0},{44-->0},{45-->920.70  总市值},{46-->Nike Inc},{47-->3.19 每股收益},{48-->117.72 52周最高价},{49-->76.68 52周最低价},{50-->0},{51-->"}】 

           P@#%$#%@@#$ 【{0-->v_hk00686="100},{1-->联合光伏},{2-->00686},{3-->0.72 当前价格},{4-->0.80 昨收},{5-->0.77 今开},{6-->43248000.0 成交量},{7-->0},{8-->0},{9-->0.72 成交价},{10-->0},{11-->0},{12-->0},{13-->0},
           {14-->0},{15-->0},{16-->0},{17-->0},{18-->0},{19-->0.72},{20-->0},{21-->0},{22-->0},{23-->0},{24-->0},{25-->0},{26-->0},{27-->0},{28-->0},{29-->43248000.0},{30-->2015/08/21 16:01:00 time},{31-->-0.08 涨跌额},{32-->-10.00 涨跌福},
           {33-->0.79 最高价},{34-->0.70 最低价},{35-->0.72 成交价},{36-->43248000.0 成交量},{37-->31285300.00 成交额},{38-->0},{39-->8.03 市盈率},{40-->},{41-->0},{42-->0},{43-->11.25 振幅},{44-->34.14},{45-->34.14 港股市值 亿元},{46-->UNITED PV},{47-->0.00},{48-->1.61 52周最高},{49-->0.58 52周最低},{50-->0},{51-->"}】 
		 */
		String[] beans=result.split(";");
		for(int i=0;i<beans.length;i++)
		{
			String temp= beans[i];
			Date time=new Date();
			String stockid=temp.substring(temp.lastIndexOf("_")+1,temp.lastIndexOf("="));
			String[] data=temp.split("~");

			if(data.length<10)//无数据
			{
				continue;
			}
			String isClose=data[40].length()==0?"false":"true";
			StockPrice tempdate=new StockPrice();
			String turnoverRate="--";
			if(temp.indexOf("v_hk")>-1)//港股
			{
				if(data[30].length()!=0)
				{
					time=sdfall.parse(data[30].replace('/', '-'));
				}

				tempdate.setJrkpj(data[5]);
				tempdate.setZrspj(data[4]);
				tempdate.setDqjg(data[3]);
				tempdate.setJrjgj(data[33]);
				tempdate.setJrjdj(data[34]);
				tempdate.setCjjps(data[36]);
				tempdate.setZde(data[31]);
				tempdate.setZdf(data[32]);
				tempdate.setCjje(data[37]);
				tempdate.setTime(time);
				tempdate.setTurnoverRate(data[38]);
				tempdate.setProfitRatio(data[39]);
				tempdate.setName(data[1]);
				tempdate.setIsClose(isClose);
				tempdate.setTotalMarket(Double.parseDouble(data[45].length()==0?"0.0":data[45])*Math.pow(10, 8)+"");
				//tempdate.setUnitAssets(data[45]);
			}else if(temp.indexOf("v_us")>-1)//美股
			{
				if(data[30].length()!=0)
				{
					time=sdfall.parse(data[30].replace('/', '-'));
				}
				tempdate.setJrkpj(data[5]);
				tempdate.setZrspj(data[4]);
				tempdate.setDqjg(data[3]);
				tempdate.setJrjgj(data[33]);
				tempdate.setJrjdj(data[34]);
				tempdate.setZde(data[31]);
				tempdate.setZdf(data[32]);
				tempdate.setCjjps(data[36]);
				tempdate.setCjje(data[37]);
				tempdate.setTurnoverRate(data[38]);
				tempdate.setTime(time);
				tempdate.setProfitRatio(data[39]);
				tempdate.setName(data[1]);
				tempdate.setIsClose(isClose);
				tempdate.setTotalMarket(Double.parseDouble(data[45].length()==0?"0.0":data[45])*Math.pow(10, 8)+"");
			}else //A股
			{
				if(data[30].length()!=0)
				{
					time=sdfall1.parse(data[30]);
				}
				tempdate.setJrkpj(data[5]);
				tempdate.setZrspj(data[4]);
				tempdate.setDqjg(data[3]);
				tempdate.setZde(data[31]);
				tempdate.setZdf(data[32]);
				tempdate.setJrjgj(data[33]);
				tempdate.setJrjdj(data[34]);
				tempdate.setCjjps(data[36]);
				tempdate.setCjje(Double.parseDouble(data[37].length()==0?"0.0":data[37])*10000+"");
				tempdate.setTime(time);
				tempdate.setTurnoverRate(data[38]);
				tempdate.setProfitRatio(data[39]);
				tempdate.setName(data[1]);
				tempdate.setIsClose(isClose);
				tempdate.setTotalMarket(Double.parseDouble(data[45].length()==0?"0.0":data[45])*Math.pow(10, 8)+"");
			}
			map.remove(stockid.replace("us", "").toLowerCase());
			map.put(stockid.replace("us", "").toLowerCase(),tempdate);
		}
		appCache.updateStockData(map);
		return map;
		/**Map map=appCache.getStockData();
		String[] ids=stockids.split(",");
		String strArray="";
		for(int i=0;i<ids.length;i++)
		{
			String arr=ids[i];
			if(map.containsKey(arr))
			{
				StockPrice temp=(StockPrice)map.get(arr);
				long d=new Date().getTime()-temp.getTime().getTime();
				long secs=d/(1000);
				if(secs>=5)
				{
					strArray=strArray+","+arr;
				}
			}
			else
			{
				strArray=strArray+","+arr;
			}
		}

		if(strArray.isEmpty())
		{
			return map;
		}

		//发送http 请求
		String result=HttpUtil.sendGet("http://hq.sinajs.cn/", "list="+strArray);

		/**
		 * 
		 * 新浪财经接口解析
		 * var hq_str_sh601001="大同煤业,7.40,7.52,7.45,7.60,7.31,7.44,7.45,16094558,120275236,78100,7.44,27700,7.43,
		 * 30000,7.42,22100,7.41,56100,7.40,800,7.45,11500,7.47,22200,7.48,28600,7.49,10600,7.50,2015-08-20,13:35:55,00";
		 *  0：”大秦铁路”，股票名字；
			1：”27.55″，今日开盘价；
			2：”27.25″，昨日收盘价；
			3：”26.91″，当前价格；
			4：”27.55″，今日最高价；
			5：”26.20″，今日最低价；
			6：”26.91″，竞买价，即“买一”报价；
			7：”26.92″，竞卖价，即“卖一”报价；
			8：”22114263″，成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百；
			9：”589824680″，成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
			30：”2008-01-11″，日期；
			31：”15:05:32″，时间；

			var hq_str_rt_hk00686=" 0 UNITED PV ,1 联合光伏 , 2今开盘0.790, 3昨收盘0.810,4最高价0.810,5最低价0.760,6当前价格0.800, 7 -0.010, 8 -1.235,
			9 价位0.790,10 均价0.800,11成交额9185790.000,12成交量11664000,13市盈率10.390,14周息率0.000,15 52周最高1.610,16 52周最低0.580,17 2015/08/20, 18 16:01:06";
			var hq_str_gb_nk="0 NK,1 当前价格25.77,2 价格BFB 9.01, 3 2015-08-20 08:36:46,4 价格C 2.14,5 今开盘23.25,6 最高价26.13,7 最低价22.74,
			8 52周区间 38.48,9 52周区间 20.78,10 成交量635928,
			11 10日均量669683,12 市值2065988917,13 每股收益-1.66,14 --,15 贝塔系数0.00, 16 0.00, 17 0.00,18 0.00,19 股本80170000,20 0.00,21 0.00,22 0.00,23 0.00,24 ,25 Aug 19 03:59PM EDT,26 昨收盘23.74,27 0.00";

		//System.println(result);
		String[] beans=result.split(";");
		for(int i=0;i<beans.length;i++)
		{
			String temp= beans[i];

			String stockid=temp.substring(temp.lastIndexOf("_")+1,temp.lastIndexOf("="));
			//temp=temp.substring(temp.indexOf("\""),temp.lastIndexOf("\""));
			String[] data=temp.split(",");
			if(data.length<10)//无数据
			{
				continue;
			}
			StockPrice tempdate=new StockPrice();
			if(temp.indexOf("hq_str_sh")>1)//A股
			{
				tempdate.setJrkpj(data[1]);
				tempdate.setZrspj(data[2]);
				tempdate.setDqjg(data[3]);
				tempdate.setJrjgj(data[4]);
				tempdate.setJrjdj(data[5]);
				tempdate.setCjjps(data[8]);
				tempdate.setCjje(data[9]);
				tempdate.setTime(sdfall.parse(data[30]+" "+data[31]));
			}else if(temp.indexOf("hq_str_rt_hk")>1)//港股
			{
				tempdate.setJrkpj(data[2]);
				tempdate.setZrspj(data[3]);
				tempdate.setDqjg(data[6]);
				tempdate.setJrjgj(data[4]);
				tempdate.setJrjdj(data[5]);
				tempdate.setCjjps(data[12]);
				tempdate.setCjje(data[11]);
				tempdate.setTime(sdfall.parse(data[17].replace('/', '-')+" "+data[18]));
			}
			/*var hq_str_rt_hk00686=" 0 UNITED PV ,1 联合光伏 , 2今开盘0.790, 3昨收盘0.810,4最高价0.810,5最低价0.760,6当前价格0.800, 7 -0.010, 8 -1.235,
					9 价位0.790,10 均价0.800,11成交额9185790.000,12成交量11664000,13市盈率10.390,14周息率0.000,15 52周最高1.610,16 52周最低0.580,17 2015/08/20, 18 16:01:06";
			  var hq_str_gb_nk="0 NK,1 当前价格25.77,2 价格BFB 9.01, 3 2015-08-20 08:36:46,4 价格C 2.14,5 今开盘23.25,6 最高价26.13,7 最低价22.74,
					8 52周区间 38.48,9 52周区间 20.78,10 成交量635928,
					11 10日均量669683,12 市值2065988917,13 每股收益-1.66,14 --,15 贝塔系数0.00, 16 0.00, 17 0.00,18 0.00,19 股本80170000,20 0.00,
					21 0.00,22 0.00,23 0.00,24 ,25 Aug 19 03:59PM EDT,26 昨收盘23.74,27 0.00";

			else if(temp.indexOf("hq_str_gb_")>1)//美股
			{
				tempdate.setJrkpj(data[5]);
				tempdate.setZrspj(data[26]);
				tempdate.setDqjg(data[1]);
				tempdate.setJrjgj(data[6]);
				tempdate.setJrjdj(data[7]);
				tempdate.setCjjps(data[10]);
				//tempdate.setCjje(data[11]);
				tempdate.setTime(sdfall.parse(data[3]));
			}
			map.remove(stockid);
			map.put(stockid,tempdate);
		}

		appCache.updateStockData(map);
		return map;*/
	}

	/**
	 * 得到股票的实时数据(未用缓存)
	 * @param stockids
	 * @param market
	 * @return
	 * @throws Exception
	 */
	public Map getStockData(String stockids,Map map) throws Exception {

		//发送http 请求
		String result=HttpUtil.sendGet("http://qt.gtimg.cn/", "q="+stockids);
		logger.debug(stockids);
		logger.debug(result);
		String[] beans=result.split(";");
		for(int i=0;i<beans.length;i++)
		{
			String temp= beans[i];
			Date time=new Date();

			String stockid=temp.substring(temp.lastIndexOf("_")+1,temp.lastIndexOf("="));
			String[] data=temp.split("~");
			if(data.length<10)//无数据
			{
				continue;
			}

			StockPrice tempdate=new StockPrice();
			if(temp.indexOf("v_hk")>-1)//港股
			{
				if(data[30].length()!=0)
				{
					time=sdfall.parse(data[30].replace('/', '-'));
				}
				tempdate.setJrkpj(data[5]);
				tempdate.setZrspj(data[4]);
				tempdate.setDqjg(data[3]);
				tempdate.setJrjgj(data[33]);
				tempdate.setJrjdj(data[34]);
				tempdate.setCjjps(data[36]);
				tempdate.setZde(data[31]);
				tempdate.setZdf(data[32]);
				tempdate.setCjje(data[37]);
				tempdate.setTime(time);

				tempdate.setProfitRatio(data[39]);
				tempdate.setName(data[1]);
				tempdate.setTotalMarket(data[45]);
				//tempdate.setUnitAssets(data[45]);
			}else if(temp.indexOf("v_us")>-1)//美股
			{
				if(data[30].length()!=0)
				{
					time=sdfall.parse(data[30].replace('/', '-'));
				}
				tempdate.setJrkpj(data[5]);
				tempdate.setZrspj(data[4]);
				tempdate.setDqjg(data[3]);
				tempdate.setJrjgj(data[33]);
				tempdate.setJrjdj(data[34]);
				tempdate.setZde(data[31]);
				tempdate.setZdf(data[32]);
				tempdate.setCjjps(data[36]);
				tempdate.setCjje(data[37]);
				tempdate.setTime(time);
				tempdate.setProfitRatio(data[39]);
				tempdate.setName(data[1]);
				tempdate.setTotalMarket(data[45]);
			}else //A股
			{
				if(data[30].length()!=0)
				{
					time=sdfall1.parse(data[30]);
				}
				tempdate.setJrkpj(data[5]);
				tempdate.setZrspj(data[4]);
				tempdate.setDqjg(data[3]);
				tempdate.setZde(data[31]);
				tempdate.setZdf(data[32]);
				tempdate.setJrjgj(data[33]);
				tempdate.setJrjdj(data[34]);
				tempdate.setCjjps(data[36]);
				tempdate.setCjje(data[37]);
				tempdate.setTime(sdfall1.parse(data[30]));

				tempdate.setProfitRatio(data[39]);
				tempdate.setName(data[1]);
				tempdate.setTotalMarket(data[45]);
			}
			map.remove(stockid.replace("us", "").toLowerCase());
			map.put(stockid.replace("us", "").toLowerCase(),tempdate);
		}
		return map;
	}


	/**
	 * 得到所有的股票id
	 * @return
	 * @throws Exception
	 */
	public String getStockIds()throws Exception
	{

		String result="";
		try{
			List<Stock> list=stockDao.selectAllStock();
			for(int i=0;i<list.size();i++)
			{
				Stock temp=list.get(i);
				if(temp.getMarket().equals("1"))
				{
					result=result+","+temp.getStockid().toLowerCase();
				}else if(temp.getMarket().equals("2"))
				{
					result=result+","+temp.getStockid().toLowerCase();
				}else if(temp.getMarket().equals("3"))
				{
					result=result+",us"+temp.getStockid().toUpperCase();
				}
			}

		}catch(Exception e)
		{
			e.printStackTrace();
			result="";
			throw e;
		}

		return result;
	}

	/**
	 * 得到用户所有的股票id
	 * @return
	 * @throws Exception
	 */
	public String getUserStockIds() throws Exception
	{

		String result="";
		try{
			List<StockUser> list=stockDao.selectStockIds();
			for(int i=0;i<list.size();i++)
			{
				StockUser temp=list.get(i);
				if(temp.getMarket().equals("1"))
				{
					result=result+","+temp.getStockid().toLowerCase();
				}else if(temp.getMarket().equals("2"))
				{
					result=result+","+temp.getStockid().toLowerCase();
				}else if(temp.getMarket().equals("3"))
				{
					result=result+",us"+temp.getStockid().toUpperCase();
				}
			}

		}catch(Exception e)
		{
			e.printStackTrace();
			result="";
			throw e;
		}

		return result;
	}

	/**
	 * 得到及更新实时汇率
	 * @return
	 * @throws Exception
	 */
	public Map getExChangeRate(Map map) throws Exception
	{

		//发送http 请求
		String result=HttpUtil.sendGet("http://push3.gtimg.cn/", "q=whUSDCNY,whHKDCNY");
		/**
		 * v_whUSDCNY="310~美元人民币~USDCNY~6.4100~10.0000~20150827091450~6.4110~6.4100~6.4110~6.4100~6.4100~6.4125";
           v_whHKDCNY="310~港元人民币~HKDCNY~0.8268~5.0000~20150827090800~0.8264~0.8263~0.8268~0.8263~0.8268~0.8278";
		 */

		String[] rate=result.split(";");
		for(int i=0;i<rate.length;i++)
		{
			Currency temp=new Currency();
			String[] data=rate[i].split("~");
			if(data.length<5)
			{
				continue;
			}
			if(rate[i].contains("v_whUSD"))
			{
				map.remove("ex_3");
				map.put("ex_3",Double.parseDouble(data[3]));

				temp.setIsocode("USD");
				temp.setExchange(Double.parseDouble(data[3]));
				sysService.updateCurrencyByCode(temp);
			}
			if(rate[i].contains("v_whHKD"))
			{
				map.remove("ex_2");
				map.put("ex_2",Double.parseDouble(data[3]));

				temp.setIsocode("HKD");
				temp.setExchange(Double.parseDouble(data[3]));
				sysService.updateCurrencyByCode(temp);
			}

		}

		return map;
	}

	/** 
	 * 获取是否休市 
	 * @param request 
	 * @return 
	 */ 
	public int IsMarketClose(Date time) throws Exception
	{
		int result=0;
		/*ddq.model.Calendar temp=new ddq.model.Calendar();
		temp.setTempdate(sdf.format(time));
		List<ddq.model.Calendar> list=sysService.selectCalendarByDate(temp);

		if(list.size()>0)
		{
			ddq.model.Calendar retemp=list.get(0);
			result=retemp.getIsclose();

		}*/
		Calendar cal=Calendar.getInstance();
		cal.setTime(time);
		int week=cal.get(Calendar.DAY_OF_WEEK);
		if(week==1||week==7)
		{
			result=0;
		}else
		{
			result=1;
		}
		return result;
	}


	public List<StockFlow> selectStockFlowByUid(int uid,String code) throws Exception
	{
		StockFlow stockflow=new StockFlow();
		stockflow.setUid(uid);
		if(code!=null&&code.length()>0)
		{
			stockflow.setStockid(code);
		}
		return stockDao.selectStockFlowByUid(stockflow);
	}


	/**
	 * 获取股票状态值	
	 * @param code
	 * @param uid
	 * @return
	 */
	public String getStockStatus(String code,String uid)
	{
		StockUser stockuser=new StockUser();
		stockuser.setStockid(code);
		stockuser.setUid(Integer.parseInt(uid));
		if(stockUserMapper.selectStockUser(stockuser).size()>0)
		{
			return "2";
		}

		StockShow stockshow=new StockShow();
		stockshow.setUid(Long.parseLong(uid));
		stockshow.setStockid(code);
		stockshow.setStatus(1);
		if(stockShowMapper.find(stockshow).size()>0)
		{
			return "1";
		}
		return "0";
	}

	public List<JsonData> getStockList(String query,String status,String type,String uid) throws Exception 
	{
		List<JsonData> list=new ArrayList<JsonData>();
		String searchresult="";
		String searchcode="";
		Map<String,Stock> mapall=appCache.getStock();
		Map<String,Stock> mapuser=new HashMap<String,Stock>();
		Map<String,Stock> mapshow=new HashMap<String,Stock>();
		Map<String,String> mapstatus=new HashMap<String,String>();
		Map<String,Stock> map=new HashMap<String,Stock>();
		List<StockUser> listuser=stockUserMapper.selectStockUserByUid(uid);
		StockShow stockshow=new StockShow();
		stockshow.setUid(Long.parseLong(uid));
		stockshow.setStatus(1);
		List<StockShow> listshow=stockShowMapper.find(stockshow);

		//组装map
		for(int i=0;i<listshow.size();i++)
		{
			StockUser stockuser=listuser.get(i);
			mapshow.put(stockuser.getStockid(), mapall.get(stockuser.getStockid()));
			mapstatus.remove(stockuser.getStockid());
			mapstatus.put(stockuser.getStockid(), "1");
		}
		
		for(int i=0;i<listuser.size();i++)
		{
			StockUser stockuser=listuser.get(i);
			mapuser.put(stockuser.getStockid(), mapall.get(stockuser.getStockid()));
			mapshow.put(stockuser.getStockid(), mapall.get(stockuser.getStockid()));
			mapstatus.remove(stockuser.getStockid());
			mapstatus.put(stockuser.getStockid(), "2");
		}

        if(status.equals("1"))
        {
        	map=mapshow;
        }else if(status.equals("2"))
        {
        	map=mapuser;
        }else
        {
        	map=mapall;
        }
		
        if(type.endsWith("a"))
        {
        	Iterator entries = map.entrySet().iterator();
    		while (entries.hasNext()) { 
    			Map.Entry entry = (Map.Entry) entries.next();
    			Stock value = (Stock)entry.getValue();
    			String key = (String)entry.getKey();
    			if(!value.getMarket().equals("1"))
    			{
    				entries.remove();
    			}
    		}
        }
        
        if(type.endsWith("hk"))
        {
        	Iterator entries = map.entrySet().iterator();
    		while (entries.hasNext()) { 
    			Map.Entry entry = (Map.Entry) entries.next();
    			Stock value = (Stock)entry.getValue();
    			String key = (String)entry.getKey();
    			if(!value.getMarket().equals("2"))
    			{
    				entries.remove();
    			}
    		}
        }
        
        if(type.endsWith("us"))
        {
        	Iterator entries = map.entrySet().iterator();
    		while (entries.hasNext()) { 
    			Map.Entry entry = (Map.Entry) entries.next();
    			Stock value = (Stock)entry.getValue();
    			String key = (String)entry.getKey();
    			if(!value.getMarket().equals("3"))
    			{
    				entries.remove();
    			}
    		}
        }

		int i=0;
		//A股代码完全匹配 
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}

			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}

			if(key.equals(query)&&value.getMarket().equals("1"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}

		}

		//A股首字母代码完全匹配 
		entries = map.entrySet().iterator(); 
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(Cn2Spell.converterToFirstSpell(value.getName()).equals(query)&&value.getMarket().equals("1"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}

		}

		//港股首字母代码完全匹配 
		entries = map.entrySet().iterator(); 
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(Cn2Spell.converterToFirstSpell(value.getName()).equals(query)&&value.getMarket().equals("2"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}

		}

		//美股代码完全匹配 
		entries = map.entrySet().iterator(); 
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(key.equals(query)&&value.getMarket().equals("3"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+",us"+value.getStockid().toUpperCase();
				i++;
			}

		}

		//A股模糊匹配

		entries = map.entrySet().iterator(); 
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(key.contains(query)&&value.getMarket().equals("1"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}

		}

		entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(value.getName().contains(query)&&value.getMarket().equals("1"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}
		}

		entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(Cn2Spell.converterToFirstSpell(value.getName()).contains(query)&&value.getMarket().equals("1"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}
		}

		entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(Cn2Spell.converterToSpell(value.getName()).contains(query)&&value.getMarket().equals("1"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}
		}

		//港股模糊匹配

		entries = map.entrySet().iterator(); 
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(key.contains(query)&&value.getMarket().equals("2"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}

		}

		entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(value.getName().contains(query)&&value.getMarket().equals("2"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				list.add(jsondata);
				i++;
			}
		}

		entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(Cn2Spell.converterToFirstSpell(value.getName()).contains(query)&&value.getMarket().equals("2"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}
		}

		entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(Cn2Spell.converterToSpell(value.getName()).contains(query)&&value.getMarket().equals("2"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+","+value.getStockid();
				i++;
			}
		}

		//美股模糊匹配
		entries = map.entrySet().iterator(); 
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(key.toLowerCase().toLowerCase().contains(query)&&value.getMarket().equals("3"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+",us"+value.getStockid().toUpperCase();
				i++;
			}

		}

		entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			if(i>=8)
			{
				break;
			}
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String)entry.getKey();
			Stock value = (Stock)entry.getValue();
			if(searchresult.contains(key))
			{
				continue;
			}
			if(value.getName().toLowerCase().contains(query)&&value.getMarket().equals("3"))
			{
				JsonData jsondata=new JsonData();
				jsondata.setName(value.getName());
				jsondata.setType(value.getMarketname());
				jsondata.setCode(value.getStockid());
				list.add(jsondata);
				searchresult=searchresult+","+value.getStockid();
				searchcode=searchcode+",us"+value.getStockid().toUpperCase();
				i++;
			}
		}

		//获取实时数据
	    Map<String,StockPrice> mapstockprice=getStockData(searchcode);
		for(int j=0;j<list.size();j++)
		{
			JsonData jsondate=list.get(j);
			
			StockPrice stockprice=mapstockprice.get(jsondate.getCode())==null?new StockPrice():mapstockprice.get(jsondate.getCode());
			jsondate.setStatus(mapstatus.get(jsondate.getCode())==null?"0":(String)mapstatus.get(jsondate.getCode()));
			jsondate.setPrice(stockprice.getDqjg());
			jsondate.setFloatPrice(stockprice.getZde());
			jsondate.setFloatPercent(stockprice.getZdf());
			System.out.println(searchresult+stockprice.getZdf()+jsondate.getCode()+stockprice.getDqjg());
		}
	    
		return list;
	}

	public List<JsonData> getIndexData(String codes,String uid) throws Exception
	{
		List<JsonData> list=new ArrayList<JsonData>();
		Map<String,Stock> mapall=appCache.getStock();
	    Map<String,String> mapstatus=new HashMap<String,String>();
		Map<String,Stock> map=new HashMap<String,Stock>();
		List<StockUser> listuser=stockUserMapper.selectStockUserByUid(uid);
		StockShow stockshow=new StockShow();
		stockshow.setUid(Long.parseLong(uid));
		stockshow.setStatus(1);
		List<StockShow> listshow=stockShowMapper.find(stockshow);

		//组装map
		for(int i=0;i<listshow.size();i++)
		{
			StockUser stockuser=listuser.get(i);
			mapstatus.remove(stockuser.getStockid());
			mapstatus.put(stockuser.getStockid(), "1");
		}
		
		for(int i=0;i<listuser.size();i++)
		{
			StockUser stockuser=listuser.get(i);
			mapstatus.remove(stockuser.getStockid());
			mapstatus.put(stockuser.getStockid(), "2");
		}
		
		
		
		//获取实时数据
	    Map<String,StockPrice> mapstockprice=getStockData(codes);
	    
	    String[] s=codes.split(",");
		for(int i=0;i<s.length;i++)
		{
			JsonData jsondate=new JsonData();
			StockPrice stockprice=mapstockprice.get(s[i].replace("us", "").toLowerCase());
			if(stockprice==null)
			{
				continue;
			}
			Stock stock = mapall.get(s[i]);
			if(stock!=null)
			{
				jsondate.setType(stock.getMarket().equals("1")?"a":(stock.getMarket().equals("2")?"hk":"us"));
			}
			jsondate.setStatus(mapstatus.get(jsondate.getCode())==null?"0":(String)mapstatus.get(jsondate.getCode()));
			jsondate.setPrice(stockprice.getDqjg());
			jsondate.setFloatPrice(stockprice.getZde());
			jsondate.setFloatPercent(stockprice.getZdf());
			jsondate.setName(stockprice.getName());
			jsondate.setCode(s[i]);
			
			list.add(jsondate);
		}
		
		return list;
	}
}
