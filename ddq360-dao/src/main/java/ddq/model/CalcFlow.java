package ddq.model;

import java.io.Serializable;
import java.util.*;


public class CalcFlow implements Serializable {
	private static final long serialVersionUID = 5138984773718311915L;
	/*id int(11) NOT NULL AUTO_INCREMENT,
	   Calcid int,
	   Cashflowid int,
	   interest double,
	   currency int(2) COMMENT '币种',*/
	Integer id=0;
	Integer calcid=0;
	Integer cashflowid=0;
	Double interest=0.;
	Integer currency=0;
	Date timestamp=new Date();
	Integer status=0;
	Integer operation=0;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCalcid() {
		return calcid;
	}
	public void setCalcid(Integer calcid) {
		this.calcid = calcid;
	}
	public Integer getCashflowid() {
		return cashflowid;
	}
	public void setCashflowid(Integer cashflowid) {
		this.cashflowid = cashflowid;
	}
	public Double getInterest() {
		return interest;
	}
	public void setInterest(Double interest) {
		this.interest = interest;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getOperation() {
		return operation;
	}
	public void setOperation(Integer operation) {
		this.operation = operation;
	}
	
	
}
