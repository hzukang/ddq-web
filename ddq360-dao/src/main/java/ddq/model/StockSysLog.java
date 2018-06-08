package ddq.model;

import java.io.Serializable;
import java.util.Date;

public class StockSysLog implements Serializable{
	private static final long serialVersionUID = 5138984773718311004L;
	 /*id Integer NOT NULL AUTO_INCREMENT,
	 timestamp timestamp, 
	 uid Integer,
	 stockid varchar(10),
	 operation Integer,
	 price Double,
cashflowid Integer COMMENT '现金流水ID',
debtflowid Integer COMMENT '融资流水ID',
	 feeprice Double,*/
	
	Integer id=0;
	Date timestamp=new Date();
	Integer operation=0;
	Double price=0.;
	Integer uid=0;
	String stockid;
	Integer debtflowid=0;
	Integer cashflowid=0;
	Double feeprice=0.;
	Integer position=0;
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
	public Integer getOperation() {
		return operation;
	}
	public void setOperation(Integer operation) {
		this.operation = operation;
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

	public Double getFeeprice() {
		return feeprice;
	}
	public void setFeeprice(Double feeprice) {
		this.feeprice = feeprice;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getDebtflowid() {
		return debtflowid;
	}
	public void setDebtflowid(Integer debtflowid) {
		this.debtflowid = debtflowid;
	}
	public Integer getCashflowid() {
		return cashflowid;
	}
	public void setCashflowid(Integer cashflowid) {
		this.cashflowid = cashflowid;
	}


	
}

