package ddq.model;

import java.io.Serializable;
import java.util.*;

public class StockFlow implements Serializable{
	private static final long serialVersionUID = 5138984773718311002L;

	/*
	id int NOT NULL AUTO_INCREMENT,
	timestamp timestamp,
	position int,
    price double,
    uid int,
	stockid nvarchar(30),
    flowid int COMMENT '现金流水ID及融资流水',
    flowtype int,
    lastprice double COMMENT '预留字段',
    ctype int COMMENT '预留字段',
    currency int,
    status int, 
	 */
	Integer operation=0;
	Integer id=0;
	Date timestamp;
	Integer position=0;
	Double price=.0;
	Integer uid=0;
	String stockid;
	Integer cashflowid=0;
	Integer debtflowid=0;
	Double feeprice=.0;
	Integer ctype=0;
	Integer currency=0;
	Integer status=0;
	String remark;
	Integer userlogid=0;
	public Integer getOperation() {
		return operation;
	}
	public void setOperation(Integer operation) {
		this.operation = operation;
	}
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
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	public Integer getCashflowid() {
		return cashflowid;
	}
	public void setCashflowid(Integer cashflowid) {
		this.cashflowid = cashflowid;
	}
	public Integer getDebtflowid() {
		return debtflowid;
	}
	public void setDebtflowid(Integer debtflowid) {
		this.debtflowid = debtflowid;
	}
	public Double getFeeprice() {
		return feeprice;
	}
	public void setFeeprice(Double feeprice) {
		this.feeprice = feeprice;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getUserlogid() {
		return userlogid;
	}
	public void setUserlogid(Integer userlogid) {
		this.userlogid = userlogid;
	}
	
	
}
