package ddq.model;

import java.io.Serializable;
import java.util.Date;

public class StockShow implements Serializable{
	private static final long serialVersionUID = 1L;
	Integer id;
	Long uid;
	Date timestamp;
	Integer status;
	String stockid;
	Integer sortvalue;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
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
	public String getStockid() {
		return stockid;
	}
	public void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public Integer getSortvalue() {
		return sortvalue;
	}
	public void setSortvalue(Integer sortvalue) {
		this.sortvalue = sortvalue;
	}
    
}
