package ddq.model;

import java.io.Serializable;

public class JsonData implements Serializable {
	private static final long serialVersionUID = 5136984773718311900L;
	/*"currency": "RMB",
    "unit": "59.9",
    "unitPercent": "17.5%",
    "unitStatus": "up",
    "totalMarket": "234.69万",
    "day": "234.00",
    "dayPercent": "6.08%",
    "dayStatus": "up",
    "totalPercent": "",
    "totalStatus": "down",
    "total": "447.68"*/
	String currency;
	String unit;
	String unitPercent;
	String unitStatus;
	String totalMarket;
	String day;
	String dayPercent;
	String totalPercent;
	String totalStatus;
	String total;
	String time;
	String dayStatus;
	String totalAssets;

	/*
	 *  "unitPrice": "11",      // 当前价
141	 
-       "range": "1",           // 涨跌幅 
142	 
-       "rangeStatus": "up",    // 涨跌状态
143	 
-       "rangePercent": "1222%",  // 涨跌百分比
144	 
-       "day": "6-24",            // 日期
145	 
-       "time": "10:00:00",       // 时间
146	 
-       "todayPrice": "11",       // 今开
147	 
-       "lastPrice": "222",        // 昨收
148	 
-       "topPrice": "2333",       // 最高价
149	 
-       "bottomPrice": "1111",    // 最低价
150	 
-       "dealNum": "1111",          // 成交量
151	 
-       "profitRatio": "444",     // 市盈率
152	 
-       "unitAssets": "1111",    // 每股净资产
153	 
-       "totalMarket": "11111"   // 总市值
	 */
	String type;
	String name;
	String code;
	String unitPrice;
	String range;
	String rangeStatus;
	String rangePercent;
	String todayPrice;
	String lastPrice;
	String topPrice;
	String bottomPrice;
	String dealNum;
	String profitRatio;
	String unitAssets;

	String num;
	String buyPrice;
	String dilutePrice;
	String position;
	String positionStatus;
	String liquidation;
	String liquidationStatus;

	/*
	 * "rmbValue": 10000,    // 人民币金额
                  "rmbPercent": 10,     // 人民币占比
                  "usdValue": 1000,     // 美元金额
                  "usd2rmbValue": 10000,  // 美元转人民币价值
                  "usdPercent": 20,      // 美元占比
                  "hkdValue": 100003,     // 港币金额
                  "hkd2rmbValue": 30,     // 港币转人民币价值
                  "hkdPercent": 20        // 港币占比
	 */
	String rmbValue;
	String rmbPercent;
	String usdValue;
	String usd2rmbValue;
	String usdPercent;
	String hkdValue;
	String hkd2rmbValue;
	String hkdPercent;

	String totalCash;
	String action;
	String totalFinancing;
	String date;
	String value;

	String status;//0 所有,1 自选,2 持仓
	String turnoverRate;//换手利率
	String buyUnit;//每手股数
	String isClose;//是否停牌

	/*
	 *"price": "11111",         // 当前价
    "floatPrice": "-111",     // 浮动值
    "floatPercent": "-100",   // 浮动占比
	 */
	String price;
	String floatPrice;
	String floatPercent;

	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFloatPrice() {
		return floatPrice;
	}
	public void setFloatPrice(String floatPrice) {
		this.floatPrice = floatPrice;
	}
	public String getFloatPercent() {
		return floatPercent;
	}
	public void setFloatPercent(String floatPercent) {
		this.floatPercent = floatPercent;
	}

	public String getCurrency() {
		return currency==""?"0":currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUnit() {
		return unit==""?"0":unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnitPercent() {
		return unitPercent==""?"0":unitPercent;
	}
	public void setUnitPercent(String unitPercent) {
		this.unitPercent = unitPercent;
	}
	public String getUnitStatus() {
		return unitStatus==""?"0":unitStatus;
	}
	public void setUnitStatus(String unitStatus) {
		this.unitStatus = unitStatus;
	}
	public String getTotalMarket() {
		return totalMarket==""?"0":totalMarket;
	}
	public void setTotalMarket(String totalMarket) {
		this.totalMarket = totalMarket;
	}
	public String getDay() {
		return day==""?"0":day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDayPercent() {
		return dayPercent==""?"0":dayPercent;
	}
	public void setDayPercent(String dayPercent) {
		this.dayPercent = dayPercent;
	}
	public String getTotalPercent() {
		return totalPercent==""?"0":totalPercent;
	}
	public void setTotalPercent(String totalPercent) {
		this.totalPercent = totalPercent;
	}
	public String getTotalStatus() {
		return totalStatus==""?"0":totalStatus;
	}
	public void setTotalStatus(String totalStatus) {
		this.totalStatus = totalStatus;
	}
	public String getTotal() {
		return total==""?"0":total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTime() {
		return time==""?"0":time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDayStatus() {
		return dayStatus==""?"0":dayStatus;
	}
	public void setDayStatus(String dayStatus) {
		this.dayStatus = dayStatus;
	}
	public String getTotalAssets() {
		return totalAssets==""?"0":totalAssets;
	}
	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}
	public String getType() {
		return type==""?"0":type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name==""?"0":name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code==""?"0":code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUnitPrice() {
		return unitPrice==""?"0":unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getRange() {
		return range==""?"0":range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getRangeStatus() {
		return rangeStatus==""?"0":rangeStatus;
	}
	public void setRangeStatus(String rangeStatus) {
		this.rangeStatus = rangeStatus;
	}
	public String getRangePercent() {
		return rangePercent==""?"0":rangePercent;
	}
	public void setRangePercent(String rangePercent) {
		this.rangePercent = rangePercent;
	}
	public String getTodayPrice() {
		return todayPrice==""?"0":todayPrice;
	}
	public void setTodayPrice(String todayPrice) {
		this.todayPrice = todayPrice;
	}
	public String getLastPrice() {
		return lastPrice==""?"0":lastPrice;
	}
	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}
	public String getTopPrice() {
		return topPrice==""?"0":topPrice;
	}
	public void setTopPrice(String topPrice) {
		this.topPrice = topPrice;
	}
	public String getBottomPrice() {
		return bottomPrice==""?"0":bottomPrice;
	}
	public void setBottomPrice(String bottomPrice) {
		this.bottomPrice = bottomPrice;
	}
	public String getDealNum() {
		return dealNum==""?"0":dealNum;
	}
	public void setDealNum(String dealNum) {
		this.dealNum = dealNum;
	}
	public String getProfitRatio() {
		return profitRatio==""?"0":profitRatio;
	}
	public void setProfitRatio(String profitRatio) {
		this.profitRatio = profitRatio;
	}
	public String getUnitAssets() {
		return unitAssets==""?"0":unitAssets;
	}
	public void setUnitAssets(String unitAssets) {
		this.unitAssets = unitAssets;
	}
	public String getNum() {
		return num==""?"0":num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getBuyPrice() {
		return buyPrice==""?"0":buyPrice;
	}
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}
	public String getDilutePrice() {
		return dilutePrice==""?"0":dilutePrice;
	}
	public void setDilutePrice(String dilutePrice) {
		this.dilutePrice = dilutePrice;
	}
	public String getPosition() {
		return position==""?"0":position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getLiquidation() {
		return liquidation==""?"0":liquidation;
	}
	public void setLiquidation(String liquidation) {
		this.liquidation = liquidation;
	}
	public String getPositionStatus() {
		return positionStatus;
	}
	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}
	public String getLiquidationStatus() {
		return liquidationStatus;
	}
	public void setLiquidationStatus(String liquidationStatus) {
		this.liquidationStatus = liquidationStatus;
	}
	public String getRmbValue() {
		return rmbValue;
	}
	public void setRmbValue(String rmbValue) {
		this.rmbValue = rmbValue;
	}
	public String getRmbPercent() {
		return rmbPercent;
	}
	public void setRmbPercent(String rmbPercent) {
		this.rmbPercent = rmbPercent;
	}
	public String getUsdValue() {
		return usdValue;
	}
	public void setUsdValue(String usdValue) {
		this.usdValue = usdValue;
	}
	public String getUsd2rmbValue() {
		return usd2rmbValue;
	}
	public void setUsd2rmbValue(String usd2rmbValue) {
		this.usd2rmbValue = usd2rmbValue;
	}
	public String getUsdPercent() {
		return usdPercent;
	}
	public void setUsdPercent(String usdPercent) {
		this.usdPercent = usdPercent;
	}
	public String getHkdValue() {
		return hkdValue;
	}
	public void setHkdValue(String hkdValue) {
		this.hkdValue = hkdValue;
	}
	public String getHkd2rmbValue() {
		return hkd2rmbValue;
	}
	public void setHkd2rmbValue(String hkd2rmbValue) {
		this.hkd2rmbValue = hkd2rmbValue;
	}
	public String getHkdPercent() {
		return hkdPercent;
	}
	public void setHkdPercent(String hkdPercent) {
		this.hkdPercent = hkdPercent;
	}
	public String getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTotalFinancing() {
		return totalFinancing;
	}
	public void setTotalFinancing(String totalFinancing) {
		this.totalFinancing = totalFinancing;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTurnoverRate() {
		return turnoverRate;
	}
	public void setTurnoverRate(String turnoverRate) {
		this.turnoverRate = turnoverRate;
	}
	public String getBuyUnit() {
		return buyUnit;
	}
	public void setBuyUnit(String buyUnit) {
		this.buyUnit = buyUnit;
	}
	public String getIsClose() {
		return isClose;
	}
	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}

}
