package ddq.model;

import java.util.*;
import java.io.Serializable;

public class UserLog implements Serializable {
	private static final long serialVersionUID = 5138984773718311900L;
	
	  /* Id Integer NOT NULL AUTO_INCREMENT,
	   Uid Integer,
	   Ip nvarchar(10),
	   Time timestamp,
	   Operation nvarchar,
	   Remark  nvarchar(250),
	   PRIMARY KEY (`id`)
	   Flowid Integer*/
	
	Integer id=0;
	Integer uid=0;
	String ip;
	Date time=new Date();
    String operation;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
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
