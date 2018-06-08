package ddq.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.emay.sdk.client.api.Client;

@Service
@Transactional
public class SmsService {
	
	private static Client client =null;
	
	@Value("${sms.timeout}")  
	private Long timeout;
	
	@Value("${sms.serial}")  
	private   String serialNumber ;
	
	@Value("${sms.passwd}")  
	private  String serialPasswd;
	
	@Value("${sms.regpasswd}")  
	private  String regpasswd;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	Logger log = Logger.getLogger(SmsService.class);

	@PostConstruct
	public void init(){
		try {
			//client =new Client(serialNumber,serialPasswd);
			client.registEx(regpasswd);
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	public boolean send(String phone, String context) {
		Assert.notNull(client,"sms client is null");
		Assert.notNull(phone,"phone number is null");
		Assert.notNull(context,"context is null");
		Assert.isTrue(context.length()<=70,"context length great than 70");
		int retSend = client.sendSMS(new String[] { phone },context, "", 5);
		if (retSend != 0) {
			throw new RuntimeException("send sms fail,errorcode:"+retSend);
		}
		log.info(String.format("phone:%s send %s ", new Object[] { phone, context }));
		return true;
	}
	/**
	 * 随机生成4位长度的数字，并在 timeout时间内有效
	 * @param phone
	 * @return
	 */
	public Integer sendSmsRandomByPhone(String phone) {
		Double d = Math.random() * 10000;
		Integer num = d.intValue() % 10000;
		if (num < 1000) {
			num = 10000 - num;
		}
		
		send(phone, num.toString());
		redisTemplate.opsForValue().set(phone, num.toString(), timeout, TimeUnit.MINUTES);
		return num;
	}
	/**
	 * 验证电话与4位数字是否匹配
	 * @param phone
	 * @param num
	 * @return
	 */
	public boolean verifyPhoneNum(String phone, String num) {
		String val = redisTemplate.opsForValue().get(phone);
		if (val == null)
			return false;
		return val.equals(num);
	}
}
