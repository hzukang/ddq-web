package ddq.model;

import java.util.*;

public class JsonClass {

	TotalAssets totalAssets =new TotalAssets();
	Markets markets = new Markets();
	Persons persons=new Persons();
	public TotalAssets getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(TotalAssets totalAssets) {
		this.totalAssets = totalAssets;
	}
	public Markets getMarkets() {
		return markets;
	}
	public void setMarkets(Markets markets) {
		this.markets = markets;
	}
	public Persons getPersons() {
		return persons;
	}
	public void setPersons(Persons persons) {
		this.persons = persons;
	}

	public class TotalAssets
	{
		String result;
		ArrayList<JsonData> model =new ArrayList<JsonData>(); 
		String code;
		String msg;
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public ArrayList<JsonData> getModel() {
			return model;
		}
		public void setModel(ArrayList<JsonData> model) {
			this.model = model;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	



	public class Markets
	{
		String result;
		ArrayList<Market> model =new ArrayList<Market>(); 
		String code;
		String msg;
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public ArrayList<Market> getModel() {
			return model;
		}
		public void setModel(ArrayList<Market> model) {
			this.model = model;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	public class Market
	{
		String name;
		MarketDate data=new MarketDate();
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public MarketDate getData() {
			return data;
		}
		public void setData(MarketDate data) {
			this.data = data;
		}
	}

	public class MarketDate
	{
		JsonData rmb;
		JsonData original;
		public JsonData getRmb() {
			return rmb;
		}
		public void setRmb(JsonData rmb) {
			this.rmb = rmb;
		}
		public JsonData getOriginal() {
			return original;
		}
		public void setOriginal(JsonData original) {
			this.original = original;
		}
	}



	public class Persons
	{
		String result;
		ArrayList<Person> model=new ArrayList<Person>(); 
		String code;
		String msg;
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public List<Person> getModel() {
			return model;
		}
		public void setModel(ArrayList<Person> model) {
			this.model = model;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	public class Person
	{
		String name;
		String code;
		String type;
		MarketDate data=new MarketDate();
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public MarketDate getData() {
			return data;
		}
		public void setData(MarketDate data) {
			this.data = data;
		}
	}
	
	public class StockDetail
	{
		String result;
		JsonData model=new JsonData();
		String code;
		String msg;
		
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public JsonData getModel() {
			return model;
		}
		public void setModel(JsonData model) {
			this.model = model;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	
	public class PersonStockData
	{
		MarketDate model=new MarketDate();
		String result;
		String code;
		String msg;
		public MarketDate getModel() {
			return model;
		}
		public void setModel(MarketDate model) {
			this.model = model;
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
	}

	public class StocksHistory
	{
		String result;
		String code;
		String msg;
		List<StocksHistoryModel> model= new ArrayList<StocksHistoryModel>();
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public List<StocksHistoryModel> getModel() {
			return model;
		}
		public void setModel(List<StocksHistoryModel> model) {
			this.model = model;
		}
	
	}
	
	public class StocksHistoryModel
	{
		String name;
		String code;
		String totalNum;
		String totalValue;
		List<StocksHistoryData> data=new ArrayList<StocksHistoryData>();
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getTotalNum() {
			return totalNum;
		}
		public void setTotalNum(String totalNum) {
			this.totalNum = totalNum;
		}
		public String getTotalValue() {
			return totalValue;
		}
		public void setTotalValue(String totalValue) {
			this.totalValue = totalValue;
		}
		public List<StocksHistoryData> getData() {
			return data;
		}
		public void setData(List<StocksHistoryData> data) {
			this.data = data;
		}
	}
	public class StocksHistoryData
	{
		String action;
		String date;
		String num;
		MarketDate data=new MarketDate();
		String source;
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public MarketDate getData() {
			return data;
		}
		public void setData(MarketDate data) {
			this.data = data;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
	}
	
	public class Assets
	{
		String result;
		String code;
		String msg;
		AssetsModel model=new AssetsModel();
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public AssetsModel getModel() {
			return model;
		}
		public void setModel(AssetsModel model) {
			this.model = model;
		}
	}
	
	public class AssetsModel
	{
		JsonData cash;
		JsonData financing;
		public JsonData getCash() {
			return cash;
		}
		public void setCash(JsonData cash) {
			this.cash = cash;
		}
		public JsonData getFinancing() {
			return financing;
		}
		public void setFinancing(JsonData financing) {
			this.financing = financing;
		}
	}
	
	public class AssetsHis
	{
		String result;
		String code;
		String msg;
		AssetsHisModel model=new AssetsHisModel();
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public AssetsHisModel getModel() {
			return model;
		}
		public void setModel(AssetsHisModel model) {
			this.model = model;
		}
	}
	
	public class AssetsHisModel
	{
		List<JsonData> rmb=new ArrayList<JsonData>();
		List<JsonData> usd=new ArrayList<JsonData>();
		List<JsonData> hkd=new ArrayList<JsonData>();
		public List<JsonData> getRmb() {
			return rmb;
		}
		public void setRmb(List<JsonData> rmb) {
			this.rmb = rmb;
		}
		public List<JsonData> getUsd() {
			return usd;
		}
		public void setUsd(List<JsonData> usd) {
			this.usd = usd;
		}
		public List<JsonData> getHkd() {
			return hkd;
		}
		public void setHkd(List<JsonData> hkd) {
			this.hkd = hkd;
		}
		
	}
	
	public class addStock
	{
		List<StockData> list=new ArrayList<StockData>();

		public List<StockData> getList() {
			return list;
		}

		public void setList(List<StockData> list) {
			this.list = list;
		} 
	}
	
	public class StockData
	{
		 /*"code": "xxxx",  // 股票代号
	      "num":  100,     // 数量
	      "price": 111.00, // 价格*/
		String code;
		String num;
		String price;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		//[{"code":"sz300262","num":100,"price":"11"},{"code":"sz300262","num":100,"price":"11"}]
	}
	
	public class addAssets
	{
		AssetsData cash;
		AssetsData financing;
		public AssetsData getCash() {
			return cash;
		}
		public void setCash(AssetsData cash) {
			this.cash = cash;
		}
		public AssetsData getFinancing() {
			return financing;
		}
		public void setFinancing(AssetsData financing) {
			this.financing = financing;
		}
	}
	
	public class AssetsData
	{
		String rmb;
		String hkd;
		String usd;
		public String getRmb() {
			return rmb;
		}
		public void setRmb(String rmb) {
			this.rmb = rmb;
		}
		public String getHkd() {
			return hkd;
		}
		public void setHkd(String hkd) {
			this.hkd = hkd;
		}
		public String getUsd() {
			return usd;
		}
		public void setUsd(String usd) {
			this.usd = usd;
		}
	}
	
	public class searchStock
	{
		String result;
		String code;
		String msg;
		List<JsonData> model= new ArrayList<JsonData>();
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public List<JsonData> getModel() {
			return model;
		}
		public void setModel(List<JsonData> model) {
			this.model = model;
		}
	}
	
}

