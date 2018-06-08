package app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import app.common.AjaxResult;
import app.common.BaseController;
import app.common.StockShowParam;
import ddq.service.StockShowService;

@Controller
@RequestMapping("/json/stockshow")
public class StockShowController extends BaseController {

	@Autowired
	private StockShowService stockShowService;
	
	@RequestMapping(value = "/collectStock.json", method = RequestMethod.POST)
	public @ResponseBody AjaxResult collectStock(@RequestBody StockShowParam p) throws Exception {
		List<String> stockList =Lists.newArrayList(p.getCodes().split(","));

		stockShowService.addStockShow(Long.valueOf(p.getUid()), stockList);
		return AjaxResult.successResult();
	}
	
	@RequestMapping(value = "/unCollectStock.json", method = RequestMethod.POST)
	public @ResponseBody AjaxResult unCollectStock(@RequestBody StockShowParam p) throws Exception {
		List<String> stockList =Lists.newArrayList(p.getCodes().split(","));
		stockShowService.deleteStockShow(Long.valueOf(p.getUid()), stockList);
		return AjaxResult.successResult();
	}
	
	@RequestMapping(value = "/setStockIndex.json", method = RequestMethod.POST)
	public @ResponseBody AjaxResult sortStockShow(@RequestBody StockShowParam p) throws Exception {
		List<String> stockList =Lists.newArrayList(p.getCodes().split(","));
		stockShowService.sortStockShow(Long.valueOf(p.getUid()), stockList);
		return AjaxResult.successResult();
	}
}
