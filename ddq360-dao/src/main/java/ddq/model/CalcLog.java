package ddq.model;

import java.io.Serializable;
import java.util.*;


public class CalcLog implements Serializable {
	private static final long serialVersionUID = 5138984773718311916L;
	/*
   id int(11) NOT NULL AUTO_INCREMENT,
   flowid int(11) COMMENT '负债流水id',
   interest double COMMENT '利息',
   time timestamp,
   currency int(2) COMMENT '币种',
   Operation int,
   Cashflowid int,*/
	
	Integer id=0;
	Integer flowid=0;
	Double interest=0.;
	Date time=new Date();
	Integer currency=0;
	Integer operation=0;
	Integer cashflowid=0;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFlowid() {
		return flowid;
	}
	public void setFlowid(Integer flowid) {
		this.flowid = flowid;
	}
	public Double getInterest() {
		return interest;
	}
	public void setInterest(Double interest) {
		this.interest = interest;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Integer getOperation() {
		return operation;
	}
	public void setOperation(Integer operation) {
		this.operation = operation;
	}
	public Integer getCashflowid() {
		return cashflowid;
	}
	public void setCashflowid(Integer cashflowid) {
		this.cashflowid = cashflowid;
	}
}
