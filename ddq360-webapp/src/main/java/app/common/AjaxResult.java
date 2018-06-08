package app.common;

public class AjaxResult {
	
	private String success;
	
	private Object model;
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private Integer code;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}

	public static AjaxResult errorResult(String message,Integer code){
		AjaxResult ajax =new AjaxResult();
		ajax.setSuccess("error");
		ajax.setMessage(message);
		ajax.setCode(code);
		return ajax;
	}
	
	public static AjaxResult successResult(Object model){
		AjaxResult ajax =new AjaxResult();
		ajax.setSuccess("success");
		ajax.setModel(model);
		return ajax;
	}
	
	public static AjaxResult successResult(){
		AjaxResult ajax =new AjaxResult();
		ajax.setSuccess("success");
		return ajax;
	}
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public Object getModel() {
		return model;
	}
	public void setModel(Object model) {
		this.model = model;
	}
}
