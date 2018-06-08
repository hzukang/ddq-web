package ddq.model;

import java.io.Serializable;
import java.util.*;

public class Debt implements Serializable {
	private static final long serialVersionUID = 5138984773718311914L;
	/*   id int(11) NOT NULL AUTO_INCREMENT,
	   uid int(2),
	   debt double COMMENT '融资金',
	   createdate timestamp ,
	   interest double COMMENT '利息',
	   starttime timestamp COMMENT '开始时间',
	   rate double COMMENT '利率',
	   endtime timestamp COMMENT '结束时间',
	   currency int COMMENT '币种',*/
	
	Integer id=0;
	Integer uid=0;
	Double debt=0.;
	Date createdate=new Date();
	Double interest=0.;
	Date starttime=new Date();
	Double rate=0.;
	Date endtime=new Date();
	Integer currency=0;
	Integer flowid=0;
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
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Double getInterest() {
		return interest;
	}
	public void setInterest(Double interest) {
		this.interest = interest;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
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
	
	
}
