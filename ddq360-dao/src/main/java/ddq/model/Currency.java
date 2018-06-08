package ddq.model;

import java.io.Serializable;

public class Currency implements Serializable {
	private static final long serialVersionUID = 5138987773768311904L;
	/*
	 *  
	 id int NOT NULL AUTO_INCREMENT,
   name varchar(20),
   exchange double,
   symbol varchar(20),
   isocode varchar(20),
	 */
	Integer id=0;
    String name;
    Double exchange=0.;
    String symbol;
    String isocode;
    Integer market=0;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getExchange() {
		return exchange;
	}
	public void setExchange(Double exchange) {
		this.exchange = exchange;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getIsocode() {
		return isocode;
	}
	public void setIsocode(String isocode) {
		this.isocode = isocode;
	}
	public Integer getMarket() {
		return market;
	}
	public void setMarket(Integer market) {
		this.market = market;
	}
    
    

}
