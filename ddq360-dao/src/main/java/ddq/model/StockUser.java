package ddq.model;

import java.io.Serializable;
import java.util.*;

public class StockUser implements Serializable{
	private static final long serialVersionUID = 5138984773718311007L;
	
	/*
	 * id Integer NOT NULL AUTO_INCREMENT,
	uid Integer,
	stockid nvarchar(10),
	position Integer COMMENT '持仓',
    costprice Double COMMENT '成本价',
    updatedate datetime COMMENT '更新时间',
    createdate datetime COMMENT '创建时间',
    dilutedprice Double COMMENT '摊薄价', 
	Integer buynum;
	Integer sellnum;
	Double buycash;
	Double sellcash;
	 */
	
	Integer id=0;
	Integer uid=0;
	String  stockid;
	Integer position=0;
	Double costprice=0.;
	Date updatedate=new Date();
	Date createdate=new Date();
	Double dilutedprice=0.;
	Integer buynum=0;
	Integer sellnum=0;
	Double buycash=0.;
	Double sellcash=0.;
	String market;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getStockid() {
		return stockid;
	}
	public void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Double getCostprice() {
		return costprice;
	}
	public void setCostprice(Double costprice) {
		this.costprice = costprice;
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
	public Double getDilutedprice() {
		return dilutedprice;
	}
	public void setDilutedprice(Double dilutedprice) {
		this.dilutedprice = dilutedprice;
	}
		
	public Integer getBuynum() {
		return buynum;
	}
	public void setBuynum(Integer buynum) {
		this.buynum = buynum;
	}
	public Integer getSellnum() {
		return sellnum;
	}
	public void setSellnum(Integer sellnum) {
		this.sellnum = sellnum;
	}
	public Double getBuycash() {
		return buycash;
	}
	public void setBuycash(Double buycash) {
		this.buycash = buycash;
	}
	public Double getSellcash() {
		return sellcash;
	}
	public void setSellcash(Double sellcash) {
		this.sellcash = sellcash;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	
	
	
}
