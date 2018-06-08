package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.common.AjaxResult;
import app.common.BaseController;
import ddq.service.SmsService;

@Controller
@RequestMapping("/json/sms")
public class SmsController extends BaseController {


	@Autowired
	private SmsService smsService;


	@RequestMapping(value = "/send.json", method = RequestMethod.POST)
	public @ResponseBody AjaxResult add(@RequestParam("phone") String phone) throws Exception {
		Integer code = smsService.sendSmsRandomByPhone(phone);
		return AjaxResult.successResult(code);
	}

	@RequestMapping(value = "/verify.json", method = RequestMethod.POST)
	public @ResponseBody AjaxResult verify(@RequestParam("phone") String phone, @RequestParam("num") String num)
			throws Exception {
		Boolean b=smsService.verifyPhoneNum(phone, num);
		return b ? AjaxResult.successResult("") :AjaxResult.errorResult("验证失败", 402);
	}
}
