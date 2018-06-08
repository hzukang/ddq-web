package ddq.model;

import java.io.Serializable;
import java.util.*;

public class StockSnap implements Serializable{
	private static final long serialVersionUID = 5138984773718311006L;

    /*
     * Id Integer NOT NULL AUTO_INCREMENT,
stockid varchar(10),
price Double COMMENT '当前价格',
timestamp  timestamp COMMENT '时间',
     */
	
	Integer id=0;
	String stockid;
	Double price=0.;
	Date timestamp=new Date();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStockid() {
		return stockid;
	}
	public void setStockid(String stockid) {
		this.stockid = stockid;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
