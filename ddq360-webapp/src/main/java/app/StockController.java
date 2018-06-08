package app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import ddq.common.HttpUtil;
import ddq.model.Global;
import ddq.model.StockFlow;
import ddq.model.StockPrice;
import ddq.model.UserLog;
import ddq.service.AppCache;
import ddq.service.StockService;
import ddq.service.SysService;


@Controller  
@RequestMapping("/stock")
public class StockController {

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private StockService stockService;

	@Autowired
	private AppCache appCache;
	
	@Autowired
	private SysService sysService;

	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Gson gson=new Gson();
	@RequestMapping(value="/buy", method = RequestMethod.GET)
	public @ResponseBody String cashTran2() throws Exception {
		try
		{
			String stockid=req.getParameter("stockid");
			Double price=Double.parseDouble(req.getParameter("price"));
			Integer position=Integer.parseInt(req.getParameter("position"));
			Integer currency=Integer.parseInt(req.getParameter("currency"));
			Integer day=Integer.parseInt(req.getParameter("day"));
			Integer uid=Integer.parseInt(req.getParameter("uid"));
			
			UserLog userlog=new UserLog();
			userlog.setIp(HttpUtil.getIpAddr(req));
			System.out.println(userlog.getIp());
			userlog.setOperation("buystock");
			userlog.setTime(new Date());
			userlog.setUid(uid);
			sysService.addUserLog(userlog);
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -day);

			StockFlow stockflow=new StockFlow();
			stockflow.setUid(uid);
			stockflow.setCurrency(currency);
			stockflow.setOperation(1);
			stockflow.setPosition(position);
			stockflow.setPrice(price);
			stockflow.setStatus(1);
			stockflow.setTimestamp(cal.getTime());
			stockflow.setStockid(stockid);
			int ident=stockService.BuyStock(stockflow, 1,.0);

			return stockflow.getCashflowid()+"gg"+ident+"hh";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sysService.deleteUserLog(Global.userlogid.get());
			return "";
		}

	}

	@RequestMapping(value="/sell", method = RequestMethod.GET)
	public @ResponseBody String Sell() throws Exception {
		try
		{
			
			
			String stockid=req.getParameter("stockid");
			Double price=Double.parseDouble(req.getParameter("price"));
			Integer position=Integer.parseInt(req.getParameter("position"));
			Integer currency=Integer.parseInt(req.getParameter("currency"));
			Integer day=Integer.parseInt(req.getParameter("day"));
			Integer uid=Integer.parseInt(req.getParameter("uid"));
			
			UserLog userlog=new UserLog();
			userlog.setIp(HttpUtil.getIpAddr(req));
			userlog.setOperation("sellstock");
			userlog.setTime(new Date());
			userlog.setUid(uid);
			sysService.addUserLog(userlog);
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -day);
			
			StockFlow stockflow=new StockFlow();
			stockflow.setUid(uid);
			stockflow.setCurrency(currency);
			stockflow.setOperation(2);
			stockflow.setPosition(position);
			stockflow.setPrice(price);
			stockflow.setStatus(1);
			stockflow.setTimestamp(cal.getTime());
			stockflow.setStockid(stockid);
			int ident=stockService.SellStock(stockflow, false);
			
			return stockflow.getCashflowid()+"gg"+ident+"hh";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sysService.deleteUserLog(Global.userlogid.get());
			return "";
		}
	}
	/*
	 * 
sh600798	8.09
sh601866	7.35
sh600379	17.45
sh603018	74.39
sh601216	12.33
sh600448	8.38
sh600399	11.92
sh600844	8.72
sh603085	43.58
sh600252	27.77
00005	69.45
00011	154.4
00023	31.5
00222	5.67
00302	121.8
GLBL	23.79
NK	34.64
CYRXW	1.05
WYIGU	10.1
LOB	19.8*/

	@RequestMapping(value="/addProfit", method = RequestMethod.GET)
	public @ResponseBody String addProfit() throws Exception {
		
		Integer day=Integer.parseInt(req.getParameter("day"));
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -day);
		
		String stockids=stockService.getUserStockIds();
		
		Map stockdata=new HashMap();
		String[] stockid=stockids.split(",");
		String stockidtemp="";
		for(int i=0,j=0;i<stockid.length;i++)
		{
			stockidtemp=stockidtemp+","+stockid[i];
			j++;
			if(j==50)
			{
				stockdata=stockService.getStockData(stockidtemp);
				j=0;
				stockidtemp="";
			}
		}
		
		stockdata=stockService.getStockData(stockidtemp);
		
		Map map=new HashMap();
		
		Iterator entries = stockdata.entrySet().iterator(); 
		while (entries.hasNext()) {  
			Map.Entry entry = (Map.Entry) entries.next();  
		    String key = (String)entry.getKey();  
		    StockPrice value = (StockPrice)entry.getValue();  
		    if(value==null)
		    {
		    	continue;
		    }
		    double dqjg=Double.parseDouble(value.getZrspj());
		  
		    map.put(key.replace("us", "").toLowerCase(),dqjg);
		} 
				
		stockService.addProfit(map, cal.getTime());
		int key= stockService.addProfit(map,cal.getTime());
		//appCache.deleteAllCache();
		return key+"h";
	}

	@RequestMapping(value="/selectProfit", method = RequestMethod.GET)
	public @ResponseBody String selectProfit() throws Exception {
		Map map=new HashMap();
		map.put("starttime", "2015-08-01 15:00:00");
		map.put("endtime", "2015-08-07 15:00:00");
		map.put("date",7);
		map.put("uid", 9802);

		Map key=appCache.getStockInfo(9802, 7);//stockService.selectProfit(map);

		return gson.toJson(key);
	}


}
