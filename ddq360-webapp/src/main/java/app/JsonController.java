package app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ddq.common.ArithmeticUtil;
import ddq.common.Cn2Spell;
import ddq.common.HttpUtil;
import ddq.model.Cash;
import ddq.model.Debt;
import ddq.model.DebtFlow;
import ddq.model.Global;
import ddq.model.JsonClass;
import ddq.model.JsonData;
import ddq.model.Stock;
import ddq.model.StockFlow;
import ddq.model.StockPrice;
import ddq.model.UserLog;
import ddq.service.AppCache;
import ddq.service.CashService;
import ddq.service.DebtService;
import ddq.service.StockService;
import ddq.service.SysService;

@Controller  
@RequestMapping("/json")
public class JsonController {

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private StockService stockService;

	@Autowired
	private CashService cashService;

	@Autowired
	private DebtService debtService;

	@Autowired
	private SysService sysService;

	@Autowired
	private AppCache appCache;

	Map mapr=new HashMap();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfall=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Random ran=new Random();
	Gson gg=new Gson();

	@RequestMapping(value="/getTotalAssetsInfo.json",method = RequestMethod.GET)  
	public @ResponseBody String getTotalAssetsInfo() throws Exception { 

		Integer uid=0;
		JsonClass temp1=new JsonClass();
		try{

			uid=Integer.parseInt(req.getParameter("uid"));
			String day=req.getParameter("day");
			day=day==null||day==""?"7":day;

			Map map=appCache.getStockInfo(uid,Integer.parseInt(day));
			Map map1=appCache.getCurrency();

			if((Integer)map.get("day")!=Integer.parseInt(day))
			{
				map=appCache.updateStockInfo(uid,Integer.parseInt(day));
			}
			List<Map> datelist=(List<Map>)map.get("date");
			List<Map> stocklist=(List<Map>)map.get("stock");

			//取股票实时数据
			mapr=stockService.getStockData(map.get("stockids").toString());

			Double dayprofit=0.0;
			Double daycash=0.0;
			Double totalprofit=0.0;		
			Double totalcash=0.0;
			Double sumcash=0.0;

			//计算总金额 今日盈亏及总盈亏
			for(int i=0;i<stocklist.size();i++)
			{

				Map stockMap=stocklist.get(i);
				Integer position=(Integer)stockMap.get("position");
				Integer jbuynum=(Integer)stockMap.get("jbuynum");
				Integer jsellnum=(Integer)stockMap.get("jsellnum");
				Double ytdprice=(Double)stockMap.get("ytdprice");
				Double jbuycash=(Double)stockMap.get("jbuycash");
				Double jsellcash=(Double)stockMap.get("jsellcash");
				Double buycash=(Double)stockMap.get("buycash");
				Double sellcash=(Double)stockMap.get("sellcash");

				StockPrice stockprice=(StockPrice)mapr.get(stockMap.get("id"));
				ytdprice=stockprice==null?ytdprice:Double.parseDouble(stockprice.getZrspj());
				Double qqjg=stockprice==null||Double.parseDouble(stockprice.getDqjg())==0?ytdprice:Double.parseDouble(stockprice.getDqjg());

				Integer zrccs=position-jbuynum+jsellnum;
				Double exchange=(Double)map1.get(stockMap.get("market"));
				exchange=exchange==null?1:exchange;

				dayprofit=dayprofit+((zrccs-jsellnum)*(qqjg-ytdprice)+
						jbuynum*qqjg-jsellnum*ytdprice-jbuycash+jsellcash)*exchange;
				daycash=daycash+(jbuycash+ytdprice*zrccs)*exchange;
				totalprofit=totalprofit+(position*qqjg-buycash+sellcash)*exchange;
				totalcash=totalcash+(buycash)*exchange;
				sumcash=sumcash+(position*qqjg)*exchange;
			}
			JsonData date=new JsonData();
			date.setTime(sdf.format(new Date()));
			date.setDay(ArithmeticUtil.round(dayprofit,3)+"");
			date.setDayPercent(ArithmeticUtil.round(dayprofit/daycash*100,3)+"");

			date.setTotal(ArithmeticUtil.round(totalprofit,3)+"");

			date.setTotalPercent(ArithmeticUtil.round(totalprofit/totalcash*100,3)+"");
			date.setTotalAssets(ArithmeticUtil.round(sumcash,3)+"");
			temp1.getTotalAssets().getModel().add(0, date);

			for(int i=0;i<datelist.size();i++)
			{
				Map datemap=datelist.get(i);
				if(!datemap.get("time").toString().equals(sdf.format(new Date())))
				{
					date=new JsonData();

					date.setTime(datemap.get("time").toString());
					date.setDay(datemap.get("day")+"");
					date.setDayPercent(ArithmeticUtil.round((Double)datemap.get("dayPercent")*100,3)+"");

					date.setTotal(ArithmeticUtil.round((Double)datemap.get("total"),3)+"");

					date.setTotalPercent(ArithmeticUtil.round((Double)datemap.get("totalPercent")*100,3)+"");
					date.setTotalAssets(ArithmeticUtil.round((Double)datemap.get("totalstock"),3)+"");

					temp1.getTotalAssets().getModel().add(0,date);
				}
			}
		}
		catch(Exception e)
		{
			temp1.getTotalAssets().setResult("error");
			temp1.getTotalAssets().setCode("103");
			temp1.getTotalAssets().setMsg(e.getMessage());
		}


		String callback=req.getParameter("callback");
		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(temp1.getTotalAssets());
		}
		else
		{
			result=callback+"("+gg.toJson(temp1.getTotalAssets())+")";
		}

		return result;  
	}

	@RequestMapping(value="/getMarketInfo.json",method = RequestMethod.GET)  
	public @ResponseBody String getMarketInfo() throws Exception { 


		Integer uid=0;
		JsonClass temp=new JsonClass();
		try{
			uid=Integer.parseInt(req.getParameter("uid"));
			Map map=appCache.getStockInfo(uid,0);
			//取股票实时数据
			mapr=stockService.getStockData(map.get("stockids").toString());

			//rmb,usd,hkd
			JsonClass.Market  temp1=temp.new Market();
			temp1.setName("A股");
			GetMarkData((List<Map>)map.get("stock"),1,mapr,temp1);
			temp.getMarkets().getModel().add(temp1);

			temp1=temp.new Market();
			temp1.setName("港股");
			GetMarkData((List<Map>)map.get("stock"),2,mapr,temp1);
			temp.getMarkets().getModel().add(temp1);

			temp1=temp.new Market();
			temp1.setName("美股");
			GetMarkData((List<Map>)map.get("stock"),3,mapr,temp1);
			temp.getMarkets().getModel().add(temp1);
		}catch(Exception e)
		{
			temp.getMarkets().setResult("error");
			temp.getMarkets().setCode("103");
			temp.getMarkets().setMsg(e.getMessage());
		}

		String callback=req.getParameter("callback");
		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(temp.getMarkets());
		}
		else
		{
			result=callback+"("+gg.toJson(temp.getMarkets())+")";
		}
		return result;  
	}

	@RequestMapping(value="/getPersonInfo.json",method = RequestMethod.GET)  
	public @ResponseBody String getPersonInfo() throws Exception { 

		JsonClass temp=new JsonClass();
		Integer uid=0;
		try{
			uid=Integer.parseInt(req.getParameter("uid"));
			Map map=appCache.getStockInfo(uid,0);
			Map ttmap=appCache.getStock();
			Map map1=appCache.getCurrency();
			//取股票实时数据
			mapr=stockService.getStockData(map.get("stockids").toString());

			List<Map> list=(List<Map>)map.get("stock");
			for(int i=0;i<list.size();i++)
			{
				JsonClass.Person  temp1=temp.new Person();
				Map stockMap=list.get(i);

				Integer position=(Integer)stockMap.get("position");
				Integer jbuynum=(Integer)stockMap.get("jbuynum");
				Integer jsellnum=(Integer)stockMap.get("jsellnum");
				Double ytdprice=(Double)stockMap.get("ytdprice");
				Double jbuycash=(Double)stockMap.get("jbuycash");
				Double jsellcash=(Double)stockMap.get("jsellcash");
				Double buycash=(Double)stockMap.get("buycash");
				Double sellcash=(Double)stockMap.get("sellcash");

				StockPrice stockprice=(StockPrice)mapr.get(stockMap.get("id"));
				ytdprice=stockprice==null?ytdprice:Double.parseDouble(stockprice.getZrspj());
				Double qqjg=stockprice==null||Double.parseDouble(stockprice.getDqjg())==0?ytdprice:Double.parseDouble(stockprice.getDqjg());

				Integer zrccs=position-jbuynum+jsellnum;

				Stock stock=(Stock)ttmap.get(stockMap.get("id"));
				stock=stock==null?new Stock():stock;

				Double exchange=(Double)map1.get(stockMap.get("market"));
				exchange=exchange==null?1:exchange;

				Double dayprofit=((zrccs-jsellnum)*(qqjg-ytdprice)+
						jbuynum*qqjg-jsellnum*ytdprice-jbuycash+jsellcash);
				Double daycash=(ytdprice*zrccs+jbuycash);
				Double totalprofit=(position*qqjg-buycash+sellcash);
				Double totalcash=(buycash);

				temp1.setName(stock.getName());
				temp1.setType(stock.getMarketname());
				temp1.setCode(stock.getStockid());

				JsonData date=new JsonData();
				date.setCurrency(GetCurrencyName(Integer.parseInt((String)stockMap.get("market"))));
				date.setTotalMarket(ArithmeticUtil.round(position*qqjg,3)+"");
				date.setDay(ArithmeticUtil.round(dayprofit,3)+"");
				date.setDayPercent(ArithmeticUtil.round(dayprofit/daycash*100,3)+"");

				date.setTotal(ArithmeticUtil.round(totalprofit,3)+"");
				date.setUnit(ArithmeticUtil.round(qqjg,3)+"");
				date.setUnitPercent(ArithmeticUtil.round((qqjg-ytdprice)/ytdprice*100,3)+"");

				date.setTotalPercent(ArithmeticUtil.round(totalprofit/totalcash*100,3)+"");
				temp1.getData().setOriginal(date);

				date=new JsonData();
				date.setCurrency("rmb");
				date.setTotalMarket(ArithmeticUtil.round(position*qqjg*exchange,3)+"");
				date.setDay(ArithmeticUtil.round(dayprofit*exchange,3)+"");
				date.setDayPercent(ArithmeticUtil.round(dayprofit/daycash*100,3)+"");

				date.setTotal(ArithmeticUtil.round(totalprofit*exchange,3)+"");

				date.setTotalPercent(ArithmeticUtil.round(totalprofit/totalcash*100,3)+"");
				date.setUnit(ArithmeticUtil.round(qqjg*exchange,3)+"");
				date.setUnitPercent(ArithmeticUtil.round((qqjg-ytdprice)/ytdprice*100,3)+"");

				temp1.getData().setRmb(date);
				temp.getPersons().getModel().add(temp1);

			}
		}catch(Exception e)
		{
			temp.getPersons().setResult("error");
			temp.getPersons().setCode("103");
			temp.getPersons().setMsg(e.getMessage());
		}

		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(temp.getPersons());
		}
		else
		{
			result=callback+"("+gg.toJson(temp.getPersons())+")";
		}
		return result;  
	}

	@RequestMapping(value="/clear.json",method = RequestMethod.GET)  
	public @ResponseBody String clear() throws Exception { 
		appCache.deleteAllCache();
		return "ok";
	}

	@RequestMapping(value="/getStockDetail.json",method = RequestMethod.GET)  
	public @ResponseBody String getStockDetail() throws Exception
	{
		JsonClass temp=new JsonClass();
		JsonClass.StockDetail detail=temp.new StockDetail();

		String code=req.getParameter("code");
		String type=req.getParameter("type");
		String uid=req.getParameter("uid");
		System.out.println(code+"_"+type+"_"+uid);
		if(code==null||code.length()==0||uid==""||uid.length()==0)
		{
			detail.setMsg("参数出错");
			detail.setResult("error");
			detail.setCode("101");
		}
		else
		{
			if(type!=null&&type.equals("us"))
			{
				code="us"+code.toUpperCase();
			}
			//code.replace("us", "")
			mapr=stockService.getStockData(code);
			StockPrice stockprice=(StockPrice)mapr.get(code.replace("us", "").toLowerCase());

			if(stockprice==null)
			{
				detail.setMsg("暂无数据");
				detail.setResult("error");
				detail.setCode("102");
			}else
			{
				Map map=appCache.getStock();
				Stock stock=(Stock)map.get(code);
				detail.setResult("success");
				String status=stockService.getStockStatus(code,uid); //0 所有，1 自选，2 持仓

				JsonData jsondata=new JsonData();
				jsondata.setName(stockprice.getName());
				jsondata.setType("");
				jsondata.setCode(code.replace("us", "").toLowerCase());
				jsondata.setCurrency("");
				jsondata.setUnitPrice(stockprice.getDqjg());
				jsondata.setRange(stockprice.getZde());

				jsondata.setRangePercent(stockprice.getZdf());
				jsondata.setDay(sdfall.format(stockprice.getTime()).split(" ")[0]);
				jsondata.setTime(sdfall.format(stockprice.getTime()).split(" ")[1]);
				jsondata.setTodayPrice(stockprice.getJrkpj());
				jsondata.setLastPrice(stockprice.getZrspj());
				jsondata.setTopPrice(stockprice.getJrzgj());
				jsondata.setBottomPrice(stockprice.getJrzdj());
				jsondata.setDealNum(stockprice.getCjjps());
				jsondata.setProfitRatio(stockprice.getProfitRatio());
				jsondata.setUnitAssets("");

				jsondata.setTurnoverRate(stockprice.getTurnoverRate());
				jsondata.setTotalMarket(stockprice.getTotalMarket());
				jsondata.setIsClose(stockprice.getIsClose());
				jsondata.setStatus(status);
				jsondata.setBuyUnit((stock==null?"--":stock.getTradingunit())+"");

				detail.setModel(jsondata);
			}
		}



		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(detail);
		}
		else
		{
			result=callback+"("+gg.toJson(detail)+")";
		}
		return result;  
	}

	@RequestMapping(value="/getPersonStockData.json",method = RequestMethod.GET)  
	public @ResponseBody String getPersonStockData() throws Exception {

		String _uid=req.getParameter("uid");
		JsonClass temp=new JsonClass();
		JsonClass.PersonStockData data=temp.new PersonStockData();

		String code=req.getParameter("code");
		if(code==null||code.length()==0||_uid==null||_uid.length()==0)
		{
			data.setMsg("参数出错");
			data.setResult("error");
			data.setCode("101");
		}
		else
		{
			Integer uid=Integer.parseInt(_uid);
			mapr=stockService.getStockData(code);
			code=code.replace("us", "").toLowerCase();

			StockPrice stockprice=(StockPrice)mapr.get(code);
			stockprice=stockprice==null?new StockPrice():stockprice;

			Map map=appCache.getStockInfo(uid,0);
			Map map1=appCache.getCurrency();
			List<Map> list=(List<Map>)map.get("stock");

			for(int i=0;i<list.size();i++)
			{
				Map stockMap=list.get(i);
				String stockid=(String)stockMap.get("id");
				if(stockid.contains(code))
				{
					data.setResult("success");
					Integer position=(Integer)stockMap.get("position");
					Integer jbuynum=(Integer)stockMap.get("jbuynum");
					Integer jsellnum=(Integer)stockMap.get("jsellnum");
					Double ytdprice=(Double)stockMap.get("ytdprice");
					Double jbuycash=(Double)stockMap.get("jbuycash");
					Double jsellcash=(Double)stockMap.get("jsellcash");
					Double buycash=(Double)stockMap.get("buycash");
					Double sellcash=(Double)stockMap.get("sellcash");
					Double costprice=(Double)stockMap.get("costprice");
					Double dilutedprice=(Double)stockMap.get("dilutedprice");
					Double liquidation=(Double)stockMap.get("liquidation");
					liquidation=liquidation==null?0.0:liquidation;

					//
					ytdprice=stockprice==null||Double.parseDouble(stockprice.getZrspj())==0?ytdprice:Double.parseDouble(stockprice.getZrspj());
					Double qqjg=stockprice==null||Double.parseDouble(stockprice.getDqjg())==0?ytdprice:Double.parseDouble(stockprice.getDqjg());
					/*
					temp.put("position", temp1.getPosition());
					temp.put("costprice", temp1.getCostprice());
					temp.put("dilutedprice", temp1.getDilutedprice());*/


					Double exchange=(Double)map1.get(stockMap.get("market"));
					exchange=exchange==null?1:exchange;
					System.out.println(stockMap.get("market")+"_"+exchange);
					Integer zrccs=position-jbuynum+jsellnum;
					Double dayprofit=((zrccs-jsellnum)*(qqjg-ytdprice)+
							jbuynum*qqjg-jsellnum*ytdprice-jbuycash+jsellcash);
					Double daycash=(ytdprice*position+jbuycash);
					Double totalprofit=(position*qqjg-buycash+sellcash);
					Double totalcash=(buycash);
					Double ccyk=(qqjg-costprice)*position;
					Double ccykp=ccyk/(costprice*position);

					JsonData jsondata=new JsonData();
					jsondata.setNum(position.toString());
					jsondata.setTotalMarket(ArithmeticUtil.round(position*qqjg,3)+"");
					jsondata.setBuyPrice(ArithmeticUtil.round(costprice,3)+"");
					jsondata.setDilutePrice(ArithmeticUtil.round(dilutedprice,3)+"");
					jsondata.setDay(ArithmeticUtil.round(dayprofit,3)+"");
					jsondata.setDayPercent(ArithmeticUtil.round(dayprofit/daycash*100,3)+"");

					jsondata.setTotal(ArithmeticUtil.round(totalprofit,3)+"");
					jsondata.setTotalPercent(ArithmeticUtil.round(totalprofit/totalcash*100,3)+"");

					jsondata.setPosition(ArithmeticUtil.round(ccyk,3)+"");

					jsondata.setLiquidation(ArithmeticUtil.round(liquidation,3)+"");


					data.getModel().setOriginal(jsondata);

					jsondata=new JsonData();
					jsondata.setNum(position.toString());
					jsondata.setTotalMarket(ArithmeticUtil.round(position*qqjg*exchange,3)+"");
					jsondata.setBuyPrice(ArithmeticUtil.round(costprice*exchange,3)+"");
					jsondata.setDilutePrice(ArithmeticUtil.round(dilutedprice*exchange,3)+"");
					jsondata.setDay(ArithmeticUtil.round(dayprofit*exchange,3)+"");
					jsondata.setDayPercent(ArithmeticUtil.round(dayprofit/daycash*100,3)+"");

					jsondata.setTotal(ArithmeticUtil.round(totalprofit*exchange,3)+"");
					jsondata.setTotalPercent(ArithmeticUtil.round(totalprofit/totalcash*100,3)+"");

					jsondata.setPosition(ArithmeticUtil.round(ccyk*exchange,3)+"");

					jsondata.setLiquidation(ArithmeticUtil.round(liquidation*exchange,3)+"");

					data.getModel().setRmb(jsondata); 

				}
			}
		}



		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(data);
		}
		else
		{
			result=callback+"("+gg.toJson(data)+")";
		}
		return result;  
	}

	@RequestMapping(value="/getMyStocksHistory.json",method = RequestMethod.GET)
	public @ResponseBody  String getMyStocksHistory() throws Exception {
		JsonClass jsonclass=new JsonClass();
		JsonClass.StocksHistory stockhis=jsonclass.new StocksHistory();
		try{
			String code=req.getParameter("code");
			Integer uid=Integer.parseInt(req.getParameter("uid"));
			List<StockFlow> list=stockService.selectStockFlowByUid(uid,code);
			Map map=appCache.getStockInfo(uid,0);
			String stockids=(String)map.get("stockids");
			mapr=stockService.getStockData(stockids);
			Map map1=appCache.getCurrency();
			String codes="";
			stockhis.setResult("success");

			for(int i=0;i<list.size();i++)
			{
				StockFlow temp=list.get(i);
				if(!codes.contains(temp.getStockid()))
				{
					JsonClass.StocksHistoryModel stockhisMod=jsonclass.new StocksHistoryModel();
					StockPrice stockprice=(StockPrice)mapr.get(temp.getStockid());
					stockprice=stockprice==null?new StockPrice():stockprice;
					//System.out.println(stockprice.getName());
					Map stockMap=GetStockMap(map,temp.getStockid());

					stockhisMod.setCode(temp.getStockid());
					stockhisMod.setName(stockprice.getName());
					stockhisMod.setTotalNum(stockMap.get("position").toString());
					stockhisMod.setTotalValue(ArithmeticUtil.strMul2(stockMap.get("position").toString(),stockprice.getIsClose().equals("true")?stockprice.getZrspj():stockprice.getDqjg(),3));

					Double exchange=(Double)map1.get(stockMap.get("market"));
					exchange=exchange==null?1:exchange;

					for(int j=0;j<list.size();j++)
					{
						StockFlow data=list.get(j);

						if(temp.getStockid().equals(data.getStockid()))
						{
							JsonClass.StocksHistoryData stockhisdata=jsonclass.new StocksHistoryData(); 
							stockhisdata.setAction((data.getOperation()-1)+"");
							stockhisdata.setDate(sdfall.format(data.getTimestamp()));
							stockhisdata.setNum(data.getPosition()+"");
							String source="";
							if(data.getCashflowid()>0)
							{
								source="0";
							}
							else if(data.getDebtflowid()>0)
							{
								source="1";
							}
							stockhisdata.setSource(source);
							JsonData jsondata=new JsonData();
							jsondata.setUnit(data.getPrice()+"");
							jsondata.setTotal(data.getPrice()*data.getPosition()+"");

							stockhisdata.getData().setOriginal(jsondata);

							jsondata=new JsonData();
							jsondata.setUnit(data.getPrice()*exchange+"");
							jsondata.setTotal(data.getPrice()*data.getPosition()*exchange+"");
							stockhisdata.getData().setRmb(jsondata);

							stockhisMod.getData().add(stockhisdata);
						}
					}

					stockhis.getModel().add(stockhisMod);
					codes=codes+temp.getStockid()+",";
				}
			}



		}catch(Exception e)
		{
			stockhis.setMsg(e.getMessage());
			stockhis.setResult("error");
			stockhis.setCode("101");
			e.printStackTrace();
			throw e;
		}
		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(stockhis);
		}
		else
		{
			result=callback+"("+gg.toJson(stockhis)+")";
		}
		return result;  
	}

	@RequestMapping(value="/stockTrade.json",method = RequestMethod.GET)
	public @ResponseBody String stockTrade() throws Exception {

		JsonClass temp=new JsonClass();
		String action=req.getParameter("action");
		String num=req.getParameter("num");
		String code=req.getParameter("code");
		String price=req.getParameter("price");
		String date=req.getParameter("date");
		String source=req.getParameter("source");
		String purpose=req.getParameter("purpose");
		String rate=req.getParameter("rate");
		String currency=req.getParameter("currency");
		JsonClass.PersonStockData data=temp.new PersonStockData();

		int ok=0;
		try{
			if(action==null||action.length()<=0||num==null||num.length()<=0
					||code==null||code.length()<=0||price==null||price.length()<=0||date==null||date.length()<=0||rate==null||rate.length()<=0	)
			{
				data.setMsg("参数出错");
				data.setResult("error");
				data.setCode("101");

			}
			else
			{
				Integer uid=Integer.parseInt(req.getParameter("uid"));
				StockFlow stockflow=new StockFlow();
				int iscash=source==null||source.length()<=0?2:1;
				if(action.equals("0"))//0:买入，1:卖出 
				{
					UserLog userlog=new UserLog();
					userlog.setIp(HttpUtil.getIpAddr(req));
					userlog.setOperation("buystock");
					userlog.setTime(new Date(Long.parseLong(date)));
					userlog.setUid(uid);
					sysService.addUserLog(userlog);

					stockflow.setUid(uid);
					stockflow.setCurrency(1);
					stockflow.setOperation(1);
					stockflow.setPosition(Integer.parseInt(num));
					stockflow.setPrice(Double.parseDouble(price));
					stockflow.setStatus(1);
					stockflow.setTimestamp(new Date(Long.parseLong(date)));
					stockflow.setStockid(code);
					ok=stockService.BuyStock(stockflow,iscash,Double.parseDouble(rate)/100);

				}
				else if(action.equals("1"))
				{
					UserLog userlog=new UserLog();
					userlog.setIp(HttpUtil.getIpAddr(req));
					System.out.println(userlog.getIp());
					userlog.setOperation("sellstock");
					userlog.setTime(new Date(Long.parseLong(date)));
					userlog.setUid(uid);
					sysService.addUserLog(userlog);

					stockflow.setUid(uid);
					stockflow.setCurrency(1);
					stockflow.setOperation(2);
					stockflow.setPosition(Integer.parseInt(num));
					stockflow.setPrice(Double.parseDouble(price));
					stockflow.setStatus(1);
					stockflow.setTimestamp(new Date(Long.parseLong(date)));
					stockflow.setStockid(code);
					ok=stockService.SellStock(stockflow,false);
				}

				if(ok>0)
				{
					data.setResult("success");

					//更新股票缓存
					appCache.updateStockInfo(uid,0);
				}
				else
				{
					sysService.deleteUserLog(Global.userlogid.get());
					data.setMsg("系统异常");
					data.setResult("error");
					data.setCode("100");
				}

			}
		}catch(Exception e)
		{
			sysService.deleteUserLog(Global.userlogid.get());
			data.setMsg(e.getMessage());
			data.setResult("error");
			data.setCode("103");

		}

		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback.length()<=0)
		{
			result= gg.toJson(data);
		}
		else
		{
			result=callback+"("+gg.toJson(data)+")";
		}
		return result;
	}

	@RequestMapping(value="/getMyAssets.json",method = RequestMethod.GET)
	public @ResponseBody String getMyAssets() throws Exception {

		Integer uid=Integer.parseInt(req.getParameter("uid"));
		JsonClass temp=new JsonClass();
		List<Cash> cashlist=cashService.seleteCashByUid(uid);
		Map map1=appCache.getCurrency();
		Double total=0.;
		Double rmb=0.;
		Double us=0.;
		Double ustormb=0.;
		Double hkd=0.;
		Double hkdtormb=0.;
		JsonClass.Assets assets=temp.new Assets();
		for(int i=0;i<cashlist.size();i++)
		{
			Cash cash=cashlist.get(i);
			int currency=cash.getCurrency();
			Double exchange=(Double)map1.get(currency);
			exchange=exchange==null?1:exchange;
			if(currency==1)
			{
				rmb=rmb+cash.getCash();
			}
			if(currency==2)
			{
				hkd=hkd+cash.getCash();
				hkdtormb=hkdtormb+cash.getCash()*exchange;
			}
			if(currency==3)
			{
				us=us+cash.getCash();
				ustormb=ustormb+cash.getCash()*exchange;
			}

			total=total+cash.getCash()*exchange;
		}
		JsonData jsondata=new JsonData();
		jsondata.setTotal(ArithmeticUtil.round(total, 3)+"");
		jsondata.setRmbValue(ArithmeticUtil.round(rmb, 3)+"");
		jsondata.setRmbPercent(ArithmeticUtil.round(rmb/total*100, 3)+"");
		jsondata.setUsdValue(ArithmeticUtil.round(us, 3)+"");
		jsondata.setUsd2rmbValue(ArithmeticUtil.round(ustormb, 3)+"");
		jsondata.setUsdPercent(ArithmeticUtil.round(ustormb/total*100, 3)+"");
		jsondata.setHkdValue(ArithmeticUtil.round(hkd, 3)+"");
		jsondata.setHkd2rmbValue(ArithmeticUtil.round(hkdtormb, 3)+"");
		jsondata.setHkdPercent(ArithmeticUtil.round(hkdtormb/total*100, 3)+"");
		assets.getModel().setCash(jsondata);

		List<Debt> debtlist=debtService.selectDebtByUid(uid);
		Double dtotal=0.;
		Double drmb=0.;
		Double dus=0.;
		Double dustormb=0.;
		Double dhkd=0.;
		Double dhkdtormb=0.;

		for(int i=0;i<debtlist.size();i++)
		{
			Debt debt=debtlist.get(i);
			int currency=debt.getCurrency();
			Double exchange=(Double)map1.get(currency);
			exchange=exchange==null?1:exchange;
			if(currency==1)
			{
				drmb=drmb+debt.getDebt();
			}
			if(currency==2)
			{
				dhkd=dhkd+debt.getDebt();
				dhkdtormb=dhkdtormb+debt.getDebt()*exchange;
			}
			if(currency==3)
			{
				dus=dus+debt.getDebt();
				dustormb=dustormb+debt.getDebt()*exchange;
			}

			dtotal=dtotal+debt.getDebt()*exchange;
		}

		jsondata=new JsonData();
		jsondata.setTotal(ArithmeticUtil.round(dtotal, 3)+"");
		jsondata.setRmbValue(ArithmeticUtil.round(drmb, 3)+"");
		jsondata.setRmbPercent(ArithmeticUtil.round(drmb/dtotal*100, 3)+"");
		jsondata.setUsdValue(ArithmeticUtil.round(dus, 3)+"");
		jsondata.setUsd2rmbValue(ArithmeticUtil.round(dustormb, 3)+"");
		jsondata.setUsdPercent(ArithmeticUtil.round(dustormb/dtotal*100, 3)+"");
		jsondata.setHkdValue(ArithmeticUtil.round(dhkd, 3)+"");
		jsondata.setHkd2rmbValue(ArithmeticUtil.round(dhkdtormb, 3)+"");
		jsondata.setHkdPercent(ArithmeticUtil.round(dhkdtormb/dtotal*100, 3)+"");
		assets.getModel().setFinancing(jsondata);

		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(assets);
		}
		else
		{
			result=callback+"("+gg.toJson(assets)+")";
		}
		return result;  

	}

	@RequestMapping(value="/getMyAssetsHistory.json",method = RequestMethod.GET)
	public @ResponseBody String getMyAssetsHistory() throws Exception {

		JsonClass temp=new JsonClass();
		JsonClass.AssetsHis assetsHis=temp.new AssetsHis();

		Integer uid=Integer.parseInt(req.getParameter("uid"));
		List<DebtFlow> list=debtService.selectAssetsHistoryByUid(uid);
		Map map1=appCache.getCurrency();
		Double totalcash=.0;
		Double totalFinancing=.0;


		for(int i=0;i<list.size();i++)
		{
			JsonData jsondata=new JsonData();

			DebtFlow debtflow=list.get(i);
			int currency=debtflow.getCurrency();
			Double exchange=(Double)map1.get(currency);
			exchange=exchange==null?1:exchange;

			String action="";
			if(debtflow.getStatus()==0)
			{
				action=debtflow.getDebt()>=0?"0":"1";
				totalcash=totalcash+debtflow.getDebt()*exchange;
			}else if(debtflow.getStatus()==1)
			{
				action=debtflow.getOperation()==1?"0":"1";
				totalFinancing=totalFinancing+debtflow.getDebt()*exchange;
			}
			jsondata.setType(debtflow.getStatus()+"");
			jsondata.setAction(action);
			jsondata.setValue(debtflow.getDebt()+"");
			jsondata.setRmbValue(debtflow.getDebt()*exchange+"");
			jsondata.setTotalCash(totalcash+"");
			jsondata.setTotalFinancing(totalFinancing+"");
			jsondata.setDate(sdfall.format(debtflow.getTimestamp()));
			if(currency==1)
			{
				assetsHis.getModel().getRmb().add(0,jsondata);
			}else if(currency==2)
			{
				assetsHis.getModel().getHkd().add(0,jsondata);
			}else if(currency==3)
			{
				assetsHis.getModel().getUsd().add(0,jsondata);
			}

		}
		assetsHis.setMsg("success");
		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(assetsHis);
		}
		else
		{
			result=callback+"("+gg.toJson(assetsHis)+")";
		}
		return result;  
	}

	@RequestMapping(value="/assetsTrade.json",method = RequestMethod.GET)
	public @ResponseBody String assetsTrade() throws Exception {

		JsonClass temp=new JsonClass();
		String action=req.getParameter("action");
		String type=req.getParameter("type");
		String currency=req.getParameter("currency");
		String value=req.getParameter("value");
		String date=req.getParameter("date");
		JsonClass.PersonStockData data=temp.new PersonStockData();
		int ok=0;
		try{
			if(action==null||action.length()<=0||type==null||type.length()<=0
					||currency==null||currency.length()<=0||value==null||value.length()<=0||date==null||date.length()<=0)
			{
				data.setMsg("参数出错");
				data.setResult("error");
				data.setCode("101");

			}else
			{
				Integer uid=Integer.parseInt(req.getParameter("uid"));
				if(currency.toLowerCase().equals("usd"))
				{
					currency="3";
				}else
					if(currency.toLowerCase().equals("hkd"))
					{
						currency="2";
					}else
					{
						currency="1";
					}

				if(type.equals("0"))
				{
					UserLog userlog=new UserLog();
					userlog.setIp(HttpUtil.getIpAddr(req));
					System.out.println(userlog.getIp());
					userlog.setOperation("addcash");
					userlog.setTime(new Date(Long.parseLong(date)));
					userlog.setUid(uid);
					sysService.addUserLog(userlog);

					Double cash=0.0;
					if(action.equals("0"))
					{
						cash=Double.parseDouble(value);
					}else
					{
						cash=-Double.parseDouble(value);
					}
					Cash ent=new Cash();
					ent.setCash(cash);
					ent.setUid(uid);
					ent.setRemark("addcash");
					ent.setCurrency(Integer.parseInt(currency));
					ent.setCreatedate(new Date(Long.parseLong(date)));
					ent.setUpdatedate(new Date());
					ok=cashService.addCash(ent);
				}else if(type.equals("1"))
				{
					String rate=req.getParameter("rate");
					if(rate==null||rate.length()<=0)
					{
						rate="0.08";
					}
					Debt ent=new Debt();
					ent.setDebt(Double.parseDouble(value));
					ent.setUid(uid);
					ent.setCurrency(Integer.parseInt(currency));
					ent.setCreatedate(new Date());
					ent.setStarttime(new Date(Long.parseLong(date)));
					ent.setRate(Double.parseDouble(rate));
					if(action.equals("0"))
					{
						UserLog userlog=new UserLog();
						userlog.setIp(HttpUtil.getIpAddr(req));
						System.out.println(userlog.getIp());
						userlog.setOperation("adddebt");
						userlog.setTime(new Date(Long.parseLong(date)));
						userlog.setUid(uid);
						sysService.addUserLog(userlog);

						ok=debtService.addDebt(ent);
					}else
					{
						UserLog userlog=new UserLog();
						userlog.setIp(HttpUtil.getIpAddr(req));
						System.out.println(userlog.getIp());
						userlog.setOperation("reduceDebt");
						userlog.setTime(new Date(Long.parseLong(date)));
						userlog.setUid(uid);
						sysService.addUserLog(userlog);

						ok=debtService.reduceDebt(ent, true);
					}
				}
				if(ok>0)
				{
					data.setResult("success");
					//更新股票缓存
					appCache.updateStockInfo(uid,0);
				}
				else
				{
					sysService.deleteUserLog(Global.userlogid.get());
					data.setMsg("系统异常");
					data.setResult("error");
					data.setCode("100");
				}

			}



		}catch(Exception e)
		{
			sysService.deleteUserLog(Global.userlogid.get());
			data.setMsg(e.getMessage());
			data.setResult("error");
			data.setCode("103");

		}


		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback.length()<=0)
		{
			result= gg.toJson(data);
		}
		else
		{
			result=callback+"("+gg.toJson(data)+")";
		}
		return result; 
	}

	@RequestMapping(value="/addStock.json",method = RequestMethod.GET)
	public @ResponseBody  String addStock() throws Exception {

		Global.userlogid.set(0);
		JsonClass temp=new JsonClass();
		JsonClass.PersonStockData stockdata=temp.new PersonStockData();
		JsonClass.addStock addstock=temp.new addStock();
		String data=req.getParameter("data");
		System.out.println(data);
		if(data==null||data.length()<=0)
		{
			stockdata.setMsg("参数出错");
			stockdata.setResult("error");
			stockdata.setCode("101");

		}else{

			try{
				Integer uid=Integer.parseInt(req.getParameter("uid"));
				java.lang.reflect.Type type = new TypeToken<ArrayList<JsonClass.StockData>>(){}.getType();
				ArrayList<JsonClass.StockData> result = (ArrayList<JsonClass.StockData>)gg.fromJson(data, type);

				//初始化股票账户
				for(int i=0;i<result.size();i++)
				{
					JsonClass.StockData sdata=result.get(i);
					StockFlow stockflow=new StockFlow();
					stockflow.setUid(uid);
					stockflow.setCurrency(1);
					stockflow.setOperation(1);
					stockflow.setPosition(Integer.parseInt(sdata.getNum()));
					stockflow.setPrice(Double.parseDouble(sdata.getPrice()));
					stockflow.setStatus(1);
					stockflow.setRemark("InitStock");
					stockflow.setTimestamp(new Date());
					stockflow.setStockid(sdata.getCode());
					stockService.InitStock(stockflow);
				}
				stockdata.setResult("success");
			}
			catch(Exception e)
			{

				stockdata.setMsg(e.getMessage());
				stockdata.setResult("error");
				stockdata.setCode("103");
				throw e;
			}
		}

		String callback=req.getParameter("callback");
		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(stockdata);
		}
		else
		{
			result=callback+"("+gg.toJson(stockdata)+")";
		}
		return result;  
	}

	@RequestMapping(value="/addAssets.json",method = RequestMethod.GET)
	public @ResponseBody  String addAssets() throws Exception {

		Global.userlogid.set(0);
		JsonClass temp=new JsonClass();
		JsonClass.PersonStockData stockdata=temp.new PersonStockData();
		JsonClass.addAssets addAssets=temp.new addAssets();
		String data=req.getParameter("data");
		if(data==null||data.length()<=0)
		{
			stockdata.setMsg("参数出错");
			stockdata.setResult("error");
			stockdata.setCode("101");

		}else{

			try{
				Integer uid=Integer.parseInt(req.getParameter("uid"));
				addAssets=gg.fromJson(data, JsonClass.addAssets.class);

				//插入现金账户
				debtService.addAssets(addAssets,uid);

				stockdata.setResult("success");
			}
			catch(Exception e)
			{
				stockdata.setMsg(e.getMessage());
				stockdata.setResult("error");
				stockdata.setCode("103");
			}
		}

		String callback=req.getParameter("callback");
		String result="";
		if(callback==null||callback=="")
		{
			result= gg.toJson(stockdata);
		}
		else
		{
			result=callback+"("+gg.toJson(stockdata)+")";
		}
		return result;  

	}

	@RequestMapping(value="/getIndexData.json",method = RequestMethod.GET)
	public @ResponseBody  String getIndexData() throws Exception {
		String result="";
		String code=req.getParameter("q");
		mapr=stockService.getStockData(code);
		String[] s=code.split(",");
		result="{\"result\": \"success\",\"model\": {  ";
		for(int i=0;i<s.length;i++)
		{
			StockPrice stockprice=(StockPrice)mapr.get(s[i].replace("us", "").toLowerCase());

			if(stockprice==null)
			{
				continue;
			}
			result=result+"\""+s[i]+"\": { \"value\": "+stockprice.getDqjg()+", \"float\": "+stockprice.getZde()+",\"floatPercent\": "+stockprice.getZdf()+" }" +",";
		}
		result=result.substring(0, result.length()-1);

		result=result+"  }}";
		return result;
	}

	@RequestMapping(value="/searchStock.json",method = RequestMethod.GET)
	public @ResponseBody  String searchStock() throws Exception {
		JsonClass temp=new JsonClass();
		JsonClass.searchStock searStock=temp.new searchStock();
		try{

			String query=java.net.URLDecoder.decode(req.getParameter("query"),"utf-8").toLowerCase();

			Map map=appCache.getStock();
			searStock.setResult("success");
			String searchresult="";

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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
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
					searStock.getModel().add(jsondata);
					searchresult=searchresult+","+value.getStockid();
					i++;
				}
			}

		}catch(Exception e)
		{
			searStock.setMsg(e.getMessage());
			searStock.setResult("error");
			searStock.setCode("103");
		}

		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback.length()<=0)
		{
			result= gg.toJson(searStock);
		}
		else
		{
			result=callback+"("+gg.toJson(searStock)+")";
		}
		return result; 
	}

	@RequestMapping(value="/getStockList.json",method = RequestMethod.GET)
	public @ResponseBody  String getStockList(@RequestParam("status") String status, @RequestParam("type") String type,
			@RequestParam("codes") String codes,@RequestParam("uid") String uid,@RequestParam("q") String q	) throws Exception
	{
	    q=java.net.URLDecoder.decode(q,"utf-8").toLowerCase();
        JsonClass temp=new JsonClass();
		JsonClass.searchStock searStock=temp.new searchStock();
		try{
			List<JsonData> list =new ArrayList<JsonData>();
			if(codes!=null&&codes.length()!=0)
			{
				list=stockService.getIndexData(codes,uid);
			}else
			{
			 list=stockService.getStockList(q,status,type,uid);
			}
			searStock.setModel(list);
			searStock.setResult("success");
		}catch(Exception e)
		{
			e.printStackTrace();
			searStock.setMsg(e.getMessage());
			searStock.setResult("error");
			searStock.setCode("103");
		}
        
		String callback=req.getParameter("callback");

		String result="";
		if(callback==null||callback.length()<=0)
		{
			result= gg.toJson(searStock);
		}
		else
		{
			result=callback+"("+gg.toJson(searStock)+")";
		}
		return result; 
	}

	private void GetMarkData(List<Map> list,Integer markid,Map mapr,JsonClass.Market temp1) throws Exception 
	{

		Double dayprofit=0.0;
		Double daycash=0.0;
		Double totalprofit=0.0;		
		Double totalcash=0.0; 
		Double all=0.0;

		Map map1=appCache.getCurrency();
		Double exchange=(Double)map1.get(markid);
		//System.out.println(exchange+"_"+markid);
		exchange=exchange==null?1:exchange;

		//计算总金额 今日盈亏及总盈亏
		for(int i=0;i<list.size();i++)
		{
			Map stockMap=list.get(i);

			Integer position=(Integer)stockMap.get("position");//持股数
			Integer jbuynum=(Integer)stockMap.get("jbuynum");//今买入数
			Integer jsellnum=(Integer)stockMap.get("jsellnum");//今买出数
			Double ytdprice=(Double)stockMap.get("ytdprice");//昨日收盘价
			Double jbuycash=(Double)stockMap.get("jbuycash");//今买入金额
			Double jsellcash=(Double)stockMap.get("jsellcash");//今买出金额
			Double buycash=(Double)stockMap.get("buycash");//总买入金额
			Double sellcash=(Double)stockMap.get("sellcash");//总买出金额

			StockPrice stockprice=(StockPrice)mapr.get(stockMap.get("id"));
			ytdprice=stockprice==null?ytdprice:Double.parseDouble(stockprice.getZrspj());

			//当前价格
			Double qqjg=stockprice==null||Double.parseDouble(stockprice.getDqjg())==0?ytdprice:Double.parseDouble(stockprice.getDqjg());
			/*Double qqjg=(Double)mapr.get(stockMap.get("id"));
			qqjg=qqjg==null?ytdprice:qqjg;*/

			Integer zrccs=position-jbuynum+jsellnum; //昨日持股数

			if(Integer.parseInt((String)stockMap.get("market"))==markid)
			{
				dayprofit=dayprofit+(zrccs-jsellnum)*(qqjg-ytdprice)+
						jbuynum*qqjg-jsellnum*ytdprice-jbuycash+jsellcash; //日盈亏
				daycash=daycash+jbuycash+ytdprice*zrccs;//(昨收盘市值+今买入金额)
				totalprofit=totalprofit+position*qqjg-buycash+sellcash;//累计盈亏
				totalcash=totalcash+buycash;//∑买入金额
				all=all+position*qqjg;//总资产

			}

		}
		JsonData date=new JsonData();
		date.setCurrency("rmb");
		date.setTotalMarket(ArithmeticUtil.round(all*exchange,3)+"");
		date.setDay(ArithmeticUtil.round(dayprofit*exchange,3)+"");
		date.setDayPercent(daycash==0?"0":ArithmeticUtil.round(dayprofit/daycash*100,3)+"");

		date.setTotal(ArithmeticUtil.round(totalprofit*exchange,3)+"");

		date.setTotalPercent(totalcash==0?"0":ArithmeticUtil.round(totalprofit/totalcash*100,3)+"");
		temp1.getData().setRmb(date);

		date=new JsonData();
		date.setCurrency(GetCurrencyName(markid));
		date.setTotalMarket(ArithmeticUtil.round(all,3)+"");
		date.setDay(ArithmeticUtil.round(dayprofit,3)+"");
		date.setDayPercent(daycash==0?"0":ArithmeticUtil.round(dayprofit/daycash*100,3)+"");

		date.setTotal(ArithmeticUtil.round(totalprofit,3)+"");

		date.setTotalPercent(totalcash==0?"0":ArithmeticUtil.round(totalprofit/totalcash*100,3)+"");
		temp1.getData().setOriginal(date);
	}

	public Map GetStockMap(Map map,String stockid)
	{
		Map result=new HashMap();
		List<Map> stocklist=(List<Map>)map.get("stock");
		for(int i=0;i<stocklist.size();i++)
		{
			Map temp=stocklist.get(i);
			if(stockid.equals((String)temp.get("id")))
			{
				result=temp;
				break;
			}
		}

		return result;
	}

	public String GetCurrencyName(Integer currency)
	{
		String result="";
		if(currency==1)
		{
			result="rmb";
		}else if(currency==2)
		{
			result="hkd";
		}else if(currency==3)
		{
			result="usd";
		}
		return result;
	}

}
