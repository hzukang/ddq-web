package ddq.model;

import java.io.Serializable;
import java.util.*;

public class DebtFlow implements Serializable {
	private static final long serialVersionUID = 5138984773718311911L;
	
	/* id int(11) NOT NULL AUTO_INCREMENT,
	   uid int(2),
	   debt double,
	   timestamp  timestamp ,
	   operation int(2) COMMENT '操作',
	   rate double COMMENT '利率',
	   starttime timestamp COMMENT '开始时间',
	   status int(2) COMMENT '是否有效',
	   currency int(2) COMMENT '币种',
	   flowid int(11) COMMENT '现金流水id',*/
	
	Integer id=0;
	Integer uid=0;
	Double debt=0.;
	Date timestamp=new Date();
	Integer operation=0;
	Double rate=0.;
	Date starttime=new Date();
	Integer status=0;
	Integer currency=0;
	Integer flowid=0;
	String remark;
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
	public Double getDebt() {
		return debt;
	}
	public void setDebt(Double debt) {
		this.debt = debt;
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
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Integer getFlowid() {
		return flowid;
	}
	public void setFlowid(Integer flowid) {
		this.flowid = flowid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
