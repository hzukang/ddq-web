package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.common.AjaxResult;
import app.common.BaseController;
import ddq.model.User;
import ddq.model.WeixinUser;
import ddq.service.UserServcie;

@Controller
@RequestMapping("/json/user")
public class UserController extends BaseController {

	@Autowired
	private UserServcie userServcie;

	@RequestMapping(value = "/addUserInfo.json", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addUserInfo(@RequestBody WeixinUser weixinUser) throws Exception {
		userServcie.insertWeixinUser(weixinUser);
		return AjaxResult.successResult(weixinUser);
	}

	@RequestMapping(value = "/add.json", method = RequestMethod.POST)
	public @ResponseBody AjaxResult add(@RequestBody User user) throws Exception {
		userServcie.insertUser(user);
		return AjaxResult.successResult(user);
	}

	@RequestMapping(value = "/userExists.json", method = RequestMethod.GET)
	public @ResponseBody AjaxResult userExists(@RequestParam("type") String type, @RequestParam("id") String openId)
			throws Exception {
		User user = null;
		if ("weixin".equals(type)) {
			user = userServcie.getUserByOpenId(openId);
			if (user == null) {
				throw new RuntimeException("openid not exists");
			}
		} else {
			throw new RuntimeException(" type error");
		}
		return AjaxResult.successResult(user);
	}
}
