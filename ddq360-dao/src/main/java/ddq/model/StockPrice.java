package ddq.model;

import java.io.Serializable;
import java.util.Date;


//股票行情
public class StockPrice implements Serializable
{
	private static final long serialVersionUID = 7138984773718311915L;
	String jrkpj;
	String zrspj;
	String dqjg;
	String jrzgj;
	String jrzdj;
	String cjjps;
	String cjje;
	
	String zde;//涨跌额
	String zdf;//涨跌福
	Date time;
	
	String name;
	/*"profitRatio": "444",     // 市盈率
    "unitAssets": "1111",    // 每股净资产
    "totalMarket": "11111"   // 总市值*/
	String profitRatio;
	String unitAssets;
	String totalMarket;
	
	/*
	 *    "position": "20001",
          "positionStatus": "up",
          "liquidation": "19911",
          "liquidationStatus": "down"
          "buyPrice": "111",
          "dilutePrice": "11111",
	 */
	String num;
	String position;
	String liquidation;
	String buyPrice;
	String dilutePrice;
	
	String turnoverRate;//换手利率
	
	String isClose;//是否停牌

    /**
	 *今日开盘价 
	 */
	public String getJrkpj() {
		return jrkpj;
	}


	public void setJrkpj(String jrkpj) {
		this.jrkpj = jrkpj;
	}


	/**
	 *昨日收盘价 
	 */
	public String getZrspj() {
		return zrspj==""?"0":zrspj;
	}


	public void setZrspj(String zrspj) {
		this.zrspj = zrspj;
	}


	/**
	 *当前价格 
	 */
	public String getDqjg() {
		return dqjg;
	}


	public void setDqjg(String dqjg) {
		this.dqjg = dqjg;
	}


	/**
	 *今日最高价 
	 */
	public String getJrzgj() {
		return jrzgj;
	}


	public void setJrjgj(String jrzgj) {
		this.jrzgj = jrzgj;
	}

	/**
	 *今日最低价 
	 */
	public String getJrzdj() {
		return jrzdj;
	}


	public void setJrjdj(String jrzdj) {
		this.jrzdj = jrzdj;
	}


	/**
	 *成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百
	 */
	public String getCjjps() {
		return cjjps;
	}


	public void setCjjps(String cjjps) {
		this.cjjps = cjjps;
	}

	/**
	 *成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万
	 */
	public String getCjje() {
		return cjje;
	}


	public void setCjje(String cjje) {
		this.cjje = cjje;
	}

	

	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public String getZde() {
		return zde;
	}


	public void setZde(String zde) {
		this.zde = zde;
	}


	public String getZdf() {
		return zdf;
	}


	public void setZdf(String zdf) {
		this.zdf = zdf;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getProfitRatio() {
		return profitRatio;
	}


	public void setProfitRatio(String profitRatio) {
		this.profitRatio = profitRatio;
	}


	public String getUnitAssets() {
		return unitAssets;
	}


	public void setUnitAssets(String unitAssets) {
		this.unitAssets = unitAssets;
	}


	public String getTotalMarket() {
		return totalMarket;
	}


	public void setTotalMarket(String totalMarket) {
		this.totalMarket = totalMarket;
	}


	public String getNum() {
		return num;
	}


	public void setNum(String num) {
		this.num = num;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getLiquidation() {
		return liquidation;
	}


	public void setLiquidation(String liquidation) {
		this.liquidation = liquidation;
	}


	public String getBuyPrice() {
		return buyPrice;
	}


	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}


	public String getDilutePrice() {
		return dilutePrice;
	}


	public void setDilutePrice(String dilutePrice) {
		this.dilutePrice = dilutePrice;
	}


	public String getTurnoverRate() {
		return turnoverRate;
	}


	public void setTurnoverRate(String turnoverRate) {
		this.turnoverRate = turnoverRate;
	}


	public String getIsClose() {
		return isClose;
	}


	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
}