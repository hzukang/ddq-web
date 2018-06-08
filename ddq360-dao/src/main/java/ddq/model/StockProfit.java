package ddq.model;

import java.io.Serializable;
import java.util.*;

public class StockProfit implements Serializable{
	private static final long serialVersionUID = 5138984773718311005L;
	
	/*
	 *   id Integer NOT NULL AUTO_INCREMENT,
timestamp timestamp,
uid Integer ,
stockid varchar(10),
market Integer,
    profit Double,
flowid Integer COMMENT '股票流水id',
status Integer,
ctype Integer  1 平仓盈亏 2 日盈亏  3累计盈亏,
	 */
	
	Integer id=0;
	Date timestamp=new Date();
	Integer uid=0;
	String stockid;
	Integer market=0;
	Double profit=0.;
	Integer flowid=0;
	Integer status=0;
	Integer ctype=0; //1 平仓盈亏 2 股票日盈亏  3 股票累计盈亏 4 人日盈亏  5 人累计盈亏 6 市场日盈亏  7 市场累计盈亏 
	Integer currency=0;
	Double total=0.;
	Integer ytdposition=0;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getStockid() {
		return stockid==null?"":stockid;
	}
	public void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public Integer getMarket() {
		return market;
	}
	public void setMarket(Integer market) {
		this.market = market;
	}
	public Double getProfit() {
		return profit;
	}
	public void setProfit(Double profit) {
		this.profit = profit;
	}
	public Integer getFlowid() {
		return flowid==null?0:flowid;
	}
	public void setFlowid(Integer flowid) {
		this.flowid = flowid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCtype() {
		return ctype;
	}
	public void setCtype(Integer ctype) {
		this.ctype = ctype;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	
	public Integer getYtdposition() {
		return ytdposition==null?0:ytdposition;
	}
	public void setYtdposition(Integer ytdposition) {
		this.ytdposition = ytdposition;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
}
