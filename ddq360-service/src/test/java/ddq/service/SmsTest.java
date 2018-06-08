package ddq.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class SmsTest {

	@Autowired
	SmsService smsService;

	@Test
	public void testSendSms() {
		String phone = "18069784322";

		Integer code = smsService.sendSmsRandomByPhone(phone);
		System.out.println(code);
		boolean b = smsService.verifyPhoneNum(phone, code.toString());
		Assert.isTrue(b);

	}
}
