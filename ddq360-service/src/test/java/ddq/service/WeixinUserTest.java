package ddq.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import ddq.mapper.WeixinUserMapper;
import ddq.model.WeixinUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class WeixinUserTest {

	@Autowired
	WeixinUserMapper weixinUserMapper;

	@Autowired
	SmsService smsService;

	@Test
	public void insertAndQuery() {
		String openid = "test_" + System.currentTimeMillis();
		WeixinUser u = new WeixinUser();
		u.setOpenid(openid);
		u.setUid(1001L);
		u.setNickname("中文昵称");
		weixinUserMapper.insert(u);
		WeixinUser query = new WeixinUser();
		query.setOpenid(openid);
		List<WeixinUser> l = weixinUserMapper.find(query);
		Assert.isTrue(l.size() == 1);
	}
}
