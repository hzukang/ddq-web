package ddq.model;

import java.io.Serializable;
import java.util.*;

public class Stock implements Serializable{
	private static final long serialVersionUID = 5138984773718311001L;
	/*
	 * 
	Id Integer NOT NULL AUTO_INCREMENT,
	stockid varchar(10),
    closeprice Double COMMENT '当前价格',
	market varchar(10) COMMENT '市场',
	updatedate timestamp COMMENT '更新时间',
	createdate timestamp COMMENT '创建时间',
    company varchar(10) COMMENT '上市公司',
    name varchar(10)
    
    marketcap varchar(50),
country varchar(50),
ipoyear varchar(50),
Subsector varchar(50),

	 */
	
	Integer id=0;
	String stockid;
	Double closeprice=0.;
	String market;
	Date updatedate=new Date();
	Date createdate=new Date();
	String company;
	String name;
	String marketname;
	String firstspell;
	String spell;
	String type;
	String remark;
	Integer tradingunit=0;
	String marketcap;
	String country;
	String ipoyear;
	String subsector;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStockid() {
		return stockid;
	}
	public void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public Double getCloseprice() {
		return closeprice;
	}
	public void setCloseprice(Double closeprice) {
		this.closeprice = closeprice;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMarketname() {
		return marketname;
	}
	public void setMarketname(String marketname) {
		this.marketname = marketname;
	}
	public String getFirstspell() {
		return firstspell;
	}
	public void setFirstspell(String firstspell) {
		this.firstspell = firstspell;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getTradingunit() {
		return tradingunit;
	}
	public void setTradingunit(Integer tradingunit) {
		this.tradingunit = tradingunit;
	}
	public String getMarketcap() {
		return marketcap;
	}
	public void setMarketcap(String marketcap) {
		this.marketcap = marketcap;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getIpoyear() {
		return ipoyear;
	}
	public void setIpoyear(String ipoyear) {
		this.ipoyear = ipoyear;
	}
	public String getSubsector() {
		return subsector;
	}
	public void setSubsector(String subsector) {
		this.subsector = subsector;
	}
	

}
