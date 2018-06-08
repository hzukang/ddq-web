package ddq.model;

import java.io.Serializable;
import java.util.Date;

public class Calendar implements Serializable {
	private static final long serialVersionUID = 5138984773718311945L;
	String tempdate;
	Integer isclose=0;
	Integer id=0;
	public String getTempdate() {
		return tempdate;
	}
	public void setTempdate(String tempdate) {
		this.tempdate = tempdate;
	}
	public Integer getIsclose() {
		return isclose;
	}
	public void setIsclose(Integer isclose) {
		this.isclose = isclose;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
