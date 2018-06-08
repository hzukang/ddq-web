package ddq.model;

import java.io.Serializable;
import java.util.*;

public class Calc implements Serializable {
	private static final long serialVersionUID = 5138984773718311914L;
	/*id int(11) NOT NULL AUTO_INCREMENT,
	   debt double,
	   flowid int(11) COMMENT '负债流水id',
	   rate double  COMMENT '利率',
	   interest double COMMENT '利息',
	   time timestamp,
	   status int COMMENT '还款状态', 
	   repayment double COMMENT '已还利息',
	   currency int(2) COMMENT '币种',*/
	Integer id=0;
	Double debt=0.;
	Integer debtid=0;
	Double rate=0.;
	Double interest=0.;
	Date time=new Date();
	Integer status=0;
	Double repayment=0.;
	Integer currency=0;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getDebt() {
		return debt;
	}
	public void setDebt(Double debt) {
		this.debt = debt;
	}
	public Integer getDebtid() {
		return debtid;
	}
	public void setDebtid(Integer debtid) {
		this.debtid = debtid;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getRepayment() {
		return repayment;
	}
	public void setRepayment(Double repayment) {
		this.repayment = repayment;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	

}
