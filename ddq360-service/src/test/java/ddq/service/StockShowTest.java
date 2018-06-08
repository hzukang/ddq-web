package ddq.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

import ddq.mapper.StockShowMapper;
import ddq.model.StockShow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class StockShowTest {

	@Autowired
	private StockShowService stockShowService;

	@Autowired
	StockShowMapper stockShowMapper;

	@Test
	public void testAll() {
		Long uid = 1001L;

		List<String> stockIds = new ArrayList<String>();
		stockIds.add("a");
		stockIds.add("b");
		stockIds.add("c");
		stockShowService.addStockShow(uid, stockIds);

		StockShow stackShow = new StockShow();
		stackShow.setUid(uid);
		{
			List<StockShow> lst = stockShowMapper.find(stackShow);
			for (StockShow s : lst) {
				System.out.println(new Gson().toJson(s));
			}
		}
		stockIds.clear();
		stockIds.add("a");
		stockShowService.deleteStockShow(uid, stockIds);
		stockIds.clear();
		stockIds.add("c");
		stockIds.add("b");
		stockShowService.sortStockShow(uid, stockIds);
		{
			List<StockShow> lst = stockShowMapper.find(stackShow);
			for (StockShow s : lst) {
				System.out.println(new Gson().toJson(s));
			}
		}
	}
}
