package ddq.job;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ddq.service.StockService;

public class test {
	 
	static String path=App.class.getClass().getResource("/")+"applicationContext.xml";  
	static ApplicationContext context = new FileSystemXmlApplicationContext(path);
	
	public static void main( String[] args ) throws Exception
	{
		System.out.println(context);
		StockService stock= (StockService)context.getBean("stockService");
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 0);
		Map map=new HashMap();
		map.put("sh600798", 16.69*Math.random()*2);
		map.put("sh601866", 11.35*Math.random()*2);
		map.put("sh600379", 18.45*Math.random()*2);
		map.put("sh601216", 28.33*Math.random()*2);
		map.put("sh600448", 6.38*Math.random()*2);
		map.put("sh600399", 19.92*Math.random()*2);
		map.put("sh600844", 4.72*Math.random()*2);
		map.put("sh603085", 49.58*Math.random()*2);
		map.put("sh600252", 24.77*Math.random()*2);
		map.put("sh603018", 77.39*Math.random()*2);
		map.put("hk00005", 72.45*Math.random()*2);
		map.put("hk00011", 163.4*Math.random()*2);
		map.put("hk00222", 18.67*Math.random()*2);
		map.put("hk00302", 108.8*Math.random()*2);
		map.put("glbl", 21.79*Math.random()*2);
		map.put("nk", 32.64*Math.random()*2);
		map.put("cyrxw", 2.05*Math.random()*2);
		map.put("wyigu", 13.1*Math.random()*2);
		
		int key= stock.addProfit(map,cal.getTime());
	}

}
