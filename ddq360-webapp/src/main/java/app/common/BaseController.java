package app.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


public class BaseController {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public AjaxResult runtimeExceptionHandler(Exception e, HttpServletRequest request) {
		if (e instanceof IllegalArgumentException) {
			return AjaxResult.errorResult(e.getMessage(), 400);
		}
		else if (e instanceof RuntimeException) {
			return AjaxResult.errorResult(e.getMessage(), 401);
		}else{
			return AjaxResult.errorResult(e.getMessage(), 409);
		}
	}
}