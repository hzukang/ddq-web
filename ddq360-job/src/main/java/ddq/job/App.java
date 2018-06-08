package ddq.job;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import ddq.model.StockPrice;
import ddq.model.UserLog;
import ddq.service.DebtService;
import ddq.service.StockService;
import ddq.service.SysService;

/**
 * Hello world!
 *
 */

public class App 
{

	static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	static String path=App.class.getClass().getResource("/")+"applicationContext.xml";  
	static ApplicationContext context = new FileSystemXmlApplicationContext(path);
	
	/*static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	static ApplicationContext context = new FileSystemXmlApplicationContext(StockService.class.getClass().getResource("/applicationContext.xml").getFile());
	*/
	
	public static void main( String[] args ) throws Exception
	{

		Log4jConfigurer.initLogging(App.class.getClass().getResource("/")+"/log4j.xml");
		long oneDay = 24 * 60 * 60 * 1000;  

		long AinitDelay  = getTimeMillis("16:00:00") - System.currentTimeMillis();  
		AinitDelay = AinitDelay > 0 ? AinitDelay : oneDay + AinitDelay;  

		long hkinitDelay  = getTimeMillis("17:00:00") - System.currentTimeMillis();  
		hkinitDelay = hkinitDelay > 0 ? hkinitDelay : oneDay + hkinitDelay;

		long usinitDelay  = getTimeMillis("05:00:00") - System.currentTimeMillis();  
		usinitDelay = usinitDelay > 0 ? usinitDelay : oneDay + usinitDelay;

		long calcinitDelay  = getTimeMillis("00:00:00") - System.currentTimeMillis();  
		calcinitDelay = calcinitDelay > 0 ? calcinitDelay : oneDay + calcinitDelay;

		
		long initDelay  = getTimeMillis("20:00:00") - System.currentTimeMillis();  
		initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;

		Runnable Arunnable = new Runnable() {  
			public void run() { 
				try{

				    //System.out.println(context.toString());
					StockService stock= (StockService)context.getBean("stockService");
					SysService sysService=(SysService)context.getBean("sysService");
					
					if(stock.IsMarketClose(new Date())==0)
					{
						System.out.println("IsMarketClose:0");
						return;
					}
					//闁告劖鐟ラ崣鍡涙偨閵婏箑鐓曢柡鍐﹀劚缁伙拷
					UserLog userlog=new UserLog();
					userlog.setIp("127.0.0.1");
					userlog.setOperation("addProfit");
					userlog.setTime(new Date());
					userlog.setUid(0);
					userlog.setRemark("test");
					sysService.addUserLog(userlog);
					
					String stockids=stock.getUserStockIds();
					
					Map stockdata=new HashMap();
					String[] stockid=stockids.split(",");
					String stockidtemp="";
					for(int i=0,j=0;i<stockid.length;i++)
					{
						stockidtemp=stockidtemp+","+stockid[i];
						j++;
						if(j==50)
						{
							stockdata=stock.getStockData(stockidtemp,stockdata);
							j=0;
							stockidtemp="";
						}
					}
					
					stockdata=stock.getStockData(stockidtemp,stockdata);
					
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
					    double dqjg=Double.parseDouble(value.getDqjg());
					    if(dqjg==0)
					    {
					    	dqjg=Double.parseDouble(value.getZrspj());
					    }
					   
					    //System.out.println("Key = " + key + ", Value = " + value);  
					    map.put(key.replace("us", "").toLowerCase(),dqjg);
					} 
					
					map=stock.getExChangeRate(map);
					stock.addSnap(map, new Date());
					int ok=stock.addProfit_job(map, new Date());
					
					System.out.println(sdf.format(new Date())+"path:"+stockids+",ok:"+ok);  
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}  
		};
		
		Runnable calcrunnable=new Runnable() {  
			public void run() { 
				try{
					
					SysService sysService=(SysService)context.getBean("sysService");
					DebtService debtService=(DebtService)context.getBean("debtService");
					
					//闁告劖鐟ラ崣鍡涙偨閵婏箑鐓曢柡鍐﹀劚缁伙拷
					UserLog userlog=new UserLog();
					userlog.setIp("127.0.0.1");
					userlog.setOperation("addCalc");
					userlog.setTime(new Date());
					userlog.setUid(0);
					userlog.setRemark("test");
					sysService.addUserLog(userlog);
					
					int ok=debtService.addCalc();
					
					System.out.println(sdf.format(new Date())+",ok:"+ok);  
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
		};

		System.out.println(context);
		//service.scheduleAtFixedRate(Arunnable,0,oneDay, TimeUnit.MILLISECONDS);
		service.scheduleAtFixedRate(Arunnable,initDelay,oneDay, TimeUnit.MILLISECONDS);
		service.scheduleAtFixedRate(calcrunnable,calcinitDelay,oneDay, TimeUnit.MILLISECONDS);
	}

	/** 
	 * 闁兼儳鍢茶ぐ鍥箰閸パ呮毎jar闁告牕鎳愬▓鎴︽儎缁嬫鍤犻悹渚灠缁讹拷
	 * @param time "HH:mm:ss" 
	 * @return 
	 */ 
	public static String getProjectPath() {
		java.net.URL url = App.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar"))
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		java.io.File file = new java.io.File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}

	/** 
	 * 闁兼儳鍢茶ぐ鍥箰閸パ呮毎闁哄啫鐖煎Λ璺拷鐢垫嚀缁ㄦ煡鎯冮崟顒夊殤缂佸甯楅弳锟� 
	 * @param time "HH:mm:ss" 
	 * @return 
	 */  
	private static long getTimeMillis(String time) {  
		try {  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");  
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);  
			return curDate.getTime();  
		} catch (ParseException e) {  
			e.printStackTrace();  
		}  
		return 0;  
	}

}
