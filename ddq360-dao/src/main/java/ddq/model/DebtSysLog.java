package ddq.model;

import java.io.Serializable;
import java.util.*;

public class DebtSysLog implements Serializable{
	private static final long serialVersionUID = 5138984773718311913L;
	Integer id=0;
	Date timestamp=new Date();
	Integer uid=0;
	Integer operation=0;
	Double debt=0.;
	Integer currency=0;
	Double rate=0.;
	Integer flowid=0;
	Integer debtid=0;
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
	public Integer getOperation() {
		return operation;
	}
	public void setOperation(Integer operation) {
		this.operation = operation;
	}
	public Double getDebt() {
		return debt;
	}
	public void setDebt(Double debt) {
		this.debt = debt;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getFlowid() {
		return flowid;
	}
	public void setFlowid(Integer flowid) {
		this.flowid = flowid;
	}
	public Integer getDebtid() {
		return debtid;
	}
	public void setDebtid(Integer debtid) {
		this.debtid = debtid;
	}

}
