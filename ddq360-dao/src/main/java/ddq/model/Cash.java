/**
 * 
 */
package ddq.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hzk
 *
 */
public class Cash implements Serializable {
	private static final long serialVersionUID = 5138984773718311902L;
	Integer id=0;
	String remark;
	Double cash=0.;
	Integer uid=0;
	Date updatedate=new Date();
	Date createdate=new Date();
	Integer currency=0;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getCash() {
		return cash;
	}
	public void setCash(Double cash) {
		this.cash = cash;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
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
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	
}
