package app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Element;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.ModelMap;  
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import ddq.dao.*;
import ddq.common.Cn2Spell;
import ddq.common.ZHConverter;
import ddq.service.DebtService;
import ddq.common.HttpUtil;
import ddq.service.StockService;
import ddq.common.StringUtil;
import ddq.mapper.StockFlowMapper;
import ddq.mapper.StockMapper;
import ddq.mapper.UserMapper;
import ddq.model.Global;
import ddq.model.JsonData;
import ddq.model.Stock;
import ddq.model.StockFlow;


@Controller  
@RequestMapping("/test")  
public class HelloWorldController  {
	private static Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

	ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private DebtService debtService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private StockDao stockDao;

	@Autowired
	private StockMapper stockMapper;
	
	@Autowired
	private StockFlowMapper stockflowMapper;

	@Autowired
	private StockService stockService;
	Gson gg=new Gson();

	@SuppressWarnings("deprecation")
	@RequestMapping(value="/hello",method = RequestMethod.GET)  
	public @ResponseBody String printWelcome(ModelMap model) throws Exception { 
		logger.debug("TEST");
		System.out.println("gg");
		model.addAttribute("message", "Spring 3 MVC Hello World");
		System.out.println(req.getParameter("id"));

		double v1=20060;
		double v2=30.86;


		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -7);
		
		String result=HttpUtil.sendGet("http://www.baidu.com/","q=sh600399,sh603018,usNKE,usNK,hk00686,hk00222","utf-8");
		/*String result=HttpUtil.sendGet("http://qt.gtimg.cn/", "q=sh600399,sh603018,usNKE,usNK,hk00686,usNK,hk00222");*/
		//System.out.println(result);

		String result3="";
		String[] temp1=result.split(";");
		System.out.println(temp1.length);
		for(int i=0;i<temp1.length;i++)
		{
			String result2="";
			String[] temp= temp1[i].split("~");
			for(int j=0;j<temp.length;j++)
			{
				result2=result2+",{"+j+"-->"+temp[j]+"}";
			}
			//System.out.println(temp.substring(temp.indexOf("\"")+1,temp.lastIndexOf("\"")));
			//System.out.println(temp.substring(temp.lastIndexOf("_")+1,temp.lastIndexOf("=")));
			result3=result3+"_"+i+"<-->"+result2+"_"+"    P@#%$#%@@#$     ";
		}
		try{
			//int i=7/0;
		}
		catch(Exception e)
		{
			System.out.println(Global.userlogid.get()+"_"+Global.userlogid1.get());
			e.printStackTrace();
		}
		System.out.println(System.getProperty("java.io.tmpdir"));
		return Global.userlogid.get()+"_"+Global.userlogid1.get();  
	}

	@RequestMapping(value="/test",method = RequestMethod.GET)  
	public @ResponseBody String test() throws Exception { 

		String yy=HttpUtil.readTxtFile("C:/Users/hzk/Desktop/stockids.txt");
		String[] arrs=yy.split("<li>");
		for(int i=0;i<arrs.length;i++)
		{
			String temp=arrs[i];
			if(temp==null||temp.length()<8)
			{
				continue;
			}
			String stockid=temp.substring(temp.indexOf(".com/")+5,temp.indexOf(".html"));
			String name=temp.substring(temp.indexOf(">")+1,temp.indexOf("("));
			Stock stock=new Stock();

			stock.setCloseprice(0.0);

			stock.setName(name);
			stock.setStockid(stockid);
			stock.setType(stockid.substring(0,2));
			stock.setMarket("1");
			stock.setUpdatedate(new Date());
			stock.setCreatedate(new Date());
			stock.setFirstspell(Cn2Spell.converterToFirstSpell(name).toLowerCase());
			stock.setSpell(Cn2Spell.converterToSpell(name).toLowerCase());
			stockDao.insertStock(stock);
		}

		return yy;
	}

	@RequestMapping(value="/print",method = RequestMethod.GET)  
	public @ResponseBody String print() throws Exception { 

		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		int week=cal.get(Calendar.DAY_OF_WEEK);
		System.out.println(System.getProperty("java.io.tmpdir"));
		System.out.println(System.getProperty("webapp.root"));
		//String t="<a target=\"_blank\" href=\"http://quote.eastmoney.com/sh166105.html\">(166105)</a></li>";

		//String result=HttpUtil.sendGet("http://xueqiu.com/stock/cata/stocklist.json","page=1&size=90&order=desc&orderby=percent&type=30","utf-8");
		//String result1=HttpUtil.sendGet("http://qt.gtimg.cn/","q=sh600399,sh603018,usNKE,usNK,hk00686,hk00222","gbk");

		//gg.fromJson(json, typeOfT);
		/*Document doc = Jsoup.connect("https://www.hkex.com.hk/chi/market/sec_tradinfo/stockcode/eisdeqty_c.htm").timeout(20000).maxBodySize(0).get();  
		Elements elems = doc.getElementsByClass("tr_normal");//table
        //http://www.nasdaq.com/screening/companies-by-industry.aspx?industry=ALL&pagesize=7000
		List<Stock> list=new ArrayList<Stock>();
		for(int i=0;i<elems.size();i++)
		{
			org.jsoup.nodes.Element elem=elems.get(i);
			Elements cli= elem.children();

			String name=StringUtil.ToDBC(converter.convert(cli.get(1).text().trim()));

			Stock stock=new Stock();
			stock.setCloseprice(1.0);
			stock.setName(name);
			stock.setStockid("hk"+cli.get(0).text());
			stock.setType("hk");
			stock.setMarket("2");
			stock.setUpdatedate(new Date());
			stock.setCreatedate(new Date());
			stock.setTradingunit(Integer.parseInt(cli.get(2).text().replace(",", "")));
			stock.setRemark(cli.get(3).text()+cli.get(4).text()+cli.get(5).text()+cli.get(6).text());
			stock.setFirstspell(Cn2Spell.converterToFirstSpell(name).toLowerCase());
			stock.setSpell(Cn2Spell.converterToSpell(name).toLowerCase());
			list.add(stock);
			
		}
		stockMapper.insertStockList(list);*/
		/*System.out.println(elems.size()+"");
		String result=HttpUtil.sendGet("http://www.sse.com.cn/js/common/ssesuggestdata.js;pv19c35b1ba797d96f","","utf-8");

		String[] codes=result.split(";");
		for(int i=0;i<codes.length;i++)
		{
			String[] arr=codes[i].split(",");
			if(arr.length<3)
			{
				continue;
			}
			String code=arr[0].substring(arr[0].indexOf(":\"")+2,arr[0].lastIndexOf("\""));
			String name=arr[1].substring(arr[1].indexOf(":\"")+2,arr[1].lastIndexOf("\""));
			Stock stock=new Stock();
			stock.setCloseprice(2);
			stock.setName(name);
			stock.setStockid("sh"+code);
			stock.setType("sh");
			stock.setMarket("1");
			stock.setUpdatedate(new Date());
			stock.setCreatedate(new Date());
			stock.setTradingunit(100);
			stock.setRemark("");
			stock.setFirstspell(Cn2Spell.converterToFirstSpell(name).toLowerCase());
			stock.setSpell(Cn2Spell.converterToSpell(name).toLowerCase());
			stockDao.insertStock(stock);	
		}*/

		//ACTIONID=51&type=ZA||EB||CY&dmjcMatchMode=3&resultcount=15&caseSensitive=false&TYPE_AHEAD_TXTVALUE=0&TYPE_AHEAD_MATCHANYWHERE=true
		//String result=HttpUtil.sendPost("http://www.szse.cn/szseWeb/FrontController.szse", "ACTIONID=51&type=ZA||EB||CY&dmjcMatchMode=3&resultcount=1730&caseSensitive=false&TYPE_AHEAD_TXTVALUE=0&TYPE_AHEAD_MATCHANYWHERE=true");
		//String result1=HttpUtil.sendPost("http://www.szse.cn/szseWeb/FrontController.szse", "ACTIONID=7&AJAX=AJAX-TRUE&CATALOGID=1110&TABKEYvtab1&tab1PAGECOUNT=173&tab1RECORDCOUNT=1729&REPORT_ACTION=navigate&tab1PAGENUM=2");

		/*Document doc = Jsoup.connect("http://www.nasdaq.com/screening/companies-by-industry.aspx?industry=ALL&pagesize=8000").timeout(60000).maxBodySize(0).get();  
		org.jsoup.nodes.Element node = doc.getElementById("CompanylistResults");
		Elements elems=node.getElementsByTag("tr");
        //http://www.nasdaq.com/screening/companies-by-industry.aspx?industry=ALL&pagesize=7000
		List<Stock> list=new ArrayList<Stock>();
		for(int i=0;i<elems.size();i++)
		{
			org.jsoup.nodes.Element elem=elems.get(i);
			Elements cli= elem.children();

			if(cli.size()<6)
			{
				continue;
			}
			String name=StringUtil.ToDBC(cli.get(0).text().trim());

			Stock stock=new Stock();
			stock.setCloseprice(4.0);
			stock.setName(name);
			stock.setStockid(cli.get(1).text().trim().toLowerCase());
			stock.setType("us");
			stock.setMarket("3");
			stock.setUpdatedate(new Date());
			stock.setCreatedate(new Date());
			stock.setMarketcap(cli.get(2).text());
			stock.setCountry(cli.get(4).text());
			stock.setIpoyear(cli.get(5).text());
			stock.setSubsector(cli.get(6).text());
			stock.setFirstspell(name);
			stock.setSpell(name);
			list.add(stock);
		}
		stockMapper.insertStockList(list);*/

		/*String stockids=stockService.getStockIds();
		String[] stockid=stockids.split(",");
		Map stockdata=new HashMap();
		String stockidtemp="";
		for(int i=0,j=0;i<stockid.length;i++)
		{
			stockidtemp=stockidtemp+","+stockid[i];
			j++;
			if(j==50)
			{
				stockdata=stockService.getStockData(stockidtemp,stockdata);
				j=0;
				stockidtemp="";
			}
		}
		
		stockdata=stockService.getStockData(stockidtemp,stockdata);

		String result="";
		List<Stock> list=stockDao.selectStock();
		for(int i=0;i<list.size();i++)
		{   
			Stock en=list.get(i);
			if(stockdata.containsKey(en.getStockid()))
			{

			}else
			{
				result=result+","+en.getStockid();
			}
		}*/

		return sdf.format(cal.getTime())+week+"gg_";
	}

	@RequestMapping(value="/print1",method = RequestMethod.GET)
	public @ResponseBody String print1() throws Exception { 
	 List<StockFlow> list=stockflowMapper.selectStockFlowByDate("2015-08-21", "2015-08-20");
		return gg.toJson(list);
	}

} 
