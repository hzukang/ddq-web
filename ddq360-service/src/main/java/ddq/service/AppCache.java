package ddq.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddq.model.Currency;

@Service
@Transactional
public class AppCache {

	@Autowired
	private StockService stockService;

	@Autowired
	private SysService sysService;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 缓存用户股票信息
	 * @param uid
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@Cacheable(value="cacheManager", key="#uid")
	public Map getStockInfo(Integer uid,Integer date) throws Exception{  

		Integer _date=0;

		//排除周末
		for(int i=0;i<date*2;i++)
		{
			Calendar caltemp=Calendar.getInstance();
			caltemp.setTime(new Date());
			caltemp.add(Calendar.DATE, -i);
			if(stockService.IsMarketClose(caltemp.getTime())==1)
			{
				_date++;
			}
			if(_date==date)
			{
				_date=i+1;
				break;
			}
		}

		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -_date);

		Map map=new HashMap();
		map.put("uid", uid);
		map.put("date", _date);
		map.put("datenum", date);
		map.put("starttime", cal.getTime());
		map.put("endtime", new Date());
		System.out.println(_date+""+cal.getTime().toString()+"+iiii_"+(Integer)map.get("date"));
		return stockService.selectProfit(map);
	} 


	/**
	 * 更新缓存里的用户股票信息
	 * @param uid
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@CachePut(value="cacheManager", key="#uid")
	public Map updateStockInfo(Integer uid,Integer date) throws Exception{  

		Integer _date=0;

		//排除周末
		for(int i=0;i<date*2;i++)
		{
			Calendar caltemp=Calendar.getInstance();
			caltemp.setTime(new Date());
			caltemp.add(Calendar.DATE, -i);
			if(stockService.IsMarketClose(caltemp.getTime())==1)
			{
				_date++;
			}
			if(_date==date)
			{
				_date=i+1;
				break;
			}
		}


		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -_date);

		Map map=new HashMap();
		map.put("uid", uid);
		map.put("date", _date);
		map.put("datenum", date);
		map.put("starttime", cal.getTime());
		map.put("endtime", new Date());

		return stockService.selectProfit(map);
	} 

	/**
	 * 更新缓存里的用户股票信息(直接更新)
	 * @param uid
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@CachePut(value="cacheManager", key="#uid")
	public Map updateStockInfo(Map map,Integer uid) throws Exception{  
		return map;
	} 

	/**
	 * 清空指定key的缓存
	 * @param uid
	 * @throws Exception
	 */
	@CacheEvict(value="cacheManager", key="#key",beforeInvocation=true)
	public void deleteCacheByKey(final String key) throws Exception{  
		//写入日志
		redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
                result = connection.del(key.getBytes());
				return result;
			}
		});
	} 

	/**
	 * 清空所有缓存
	 * @param uid
	 * @throws Exception
	 */
	@CacheEvict(value="cacheManager",allEntries=true,beforeInvocation=true)
	public void deleteAllCache() throws Exception{  
		//写入日志
		redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	} 

	/**
	 * 缓存汇率
	 * @return
	 * @throws Exception
	 */
	@Cacheable(value="cacheManager", key="'currency'") 
	public Map getCurrency() throws Exception{
		Map map=new HashMap();
		List<Currency> list=sysService.selectAllCurrency();
		for(int i=0;i<list.size();i++)
		{
			Currency temp=list.get(i);
			map.put(temp.getMarket(), temp.getExchange());
		}

		return map;
	}

	/**
	 * 缓存股票信息
	 * @return
	 * @throws Exception
	 */
	@Cacheable(value="cacheManager", key="'stock'") 
	public Map getStock() throws Exception{
		return stockService.selectStock();
	}


	/**
	 * 缓存股票实时数据
	 * @return
	 * @throws Exception
	 */
	@Cacheable(value="cacheManager", key="'stock_hq'") 
	public Map getStockData() throws Exception{
		Map map=new HashMap();
		return map;
	}

	/**
	 * 更新缓存里的股票实时数据
	 * @return
	 * @throws Exception
	 */
	@CachePut(value="cacheManager", key="'stock_hq'")
	public Map updateStockData(Map map) throws Exception{  
		return map;
	}
}
