package ddq.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import ddq.dao.StockDao;
import ddq.mapper.StockMapper;
import ddq.model.Stock;

public class JsoupUtil {
	@Autowired
	private StockDao stockDao;

	@Autowired
	private StockMapper stockMapper;

	ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);

	/**
	 * 抓取港股股票代码  https://www.hkex.com.hk/chi/market/sec_tradinfo/stockcode/eisdeqty_c.htm
	 */
	public int JsoupHKStockid(String url) throws Exception 
	{
		int result=0;

		Document doc=null;  
		try{  

			doc = Jsoup.connect(url).timeout(5000).maxBodySize(0).get();  
			Elements elems = doc.getElementsByClass("tr_normal");
			String tdhtml="";
			for(int i=0;i<elems.size();i++)
			{
				org.jsoup.nodes.Element elem=elems.get(i);
				Elements cli= elem.children();
				String name=StringUtil.ToDBC(converter.convert(cli.get(1).text()));

				Stock stock=new Stock();
				stock.setCloseprice(1.0);
				stock.setName(name);
				stock.setStockid("hk"+cli.get(0).text());
				stock.setType("hk");
				stock.setMarket("2");
				stock.setUpdatedate(new Date());
				stock.setCreatedate(new Date());
				stock.setTradingunit(Integer.parseInt(cli.get(2).text()));
				stock.setRemark(cli.get(3).text().replace("&nbsp;", "")+cli.get(4).text().replace("&nbsp;", "")+cli.get(5).text().replace("&nbsp;", "")+cli.get(6).text().replace("&nbsp;", ""));
				stock.setFirstspell(Cn2Spell.converterToFirstSpell(name).toLowerCase());
				stock.setSpell(Cn2Spell.converterToSpell(name).toLowerCase());
				stockDao.insertStock(stock);
			}
			result=1;
		}  
		catch(Exception e){  
			e.printStackTrace();
			result=0;
			throw e;
		}  
		return result;
	}


	/**
	 * 上证交易所股票代码  http://www.sse.com.cn/js/common/ssesuggestdata.js;pv19c35b1ba797d96f
	 */
	public int JsoupSHStockid(String url) throws Exception
	{
		int key=0;
		try{  
			String result=HttpUtil.sendGet(url,"","utf-8");
			//解析A股代码
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
				stock.setCloseprice(2.0);
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
				key=1;
			}
		}catch(Exception e){  
			e.printStackTrace();
			key=0;
			throw e;
		}  
		return key;

	}


	/**
	 * 深证交易所股票代码 
	 */
	public int JsoupSZStockid(String url) throws Exception
	{
		int key=0;
		try{  
			
			
		}catch(Exception e){  
			e.printStackTrace();
			key=0;
			throw e;
		}  
		return key;

	}
	


	/**
	 * 美股股票代码 http://www.nasdaq.com/screening/companies-by-industry.aspx?industry=ALL&pagesize=8000
	 */
	public int JsoupUSStockid(String url) throws Exception
	{
		int key=0;
		try{
			Document doc = Jsoup.connect(url).timeout(60000).maxBodySize(0).get();  
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
			key=stockMapper.insertStockList(list);
		}catch(Exception e)
		{
			e.printStackTrace();
			key=0;
			throw e;
		}
		return key;
	}
	
	/**
	 * 美股ETF代码 http://www.nasdaq.com/screening/companies-by-industry.aspx?industry=ALL&pagesize=8000
	 */

}
