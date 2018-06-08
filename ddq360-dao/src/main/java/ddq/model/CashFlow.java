package ddq.model;

import java.io.Serializable;
import java.util.Date;

public class CashFlow implements Serializable {
	
	private static final long serialVersionUID = 5138984773718311900L;
	Integer id=0;
	Double cash=0.;
	Date timestamp=new Date();
	Integer uid=0;
	Integer status=0;
	Integer operation=0;//1 融资 2 还融资 3 编辑融资 4 删除融资 5 编辑还融资 6  删除还融资
	Integer currency=0;
	String remark;
	Integer userlogid=0;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getCash() {
		return cash;
	}
	public void setCash(Double cash) {
		this.cash = cash;
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
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
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
