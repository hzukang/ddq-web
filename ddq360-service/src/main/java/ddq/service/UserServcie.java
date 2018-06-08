package ddq.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ddq.mapper.UserMapper;
import ddq.mapper.WeixinUserMapper;
import ddq.model.User;
import ddq.model.WeixinUser;

@Service
@Transactional
public class UserServcie {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private WeixinUserMapper weinxinUserMapper;

	public User getUserByOpenId(String openid) {
		Assert.notNull(openid, "openid is null");

		WeixinUser user = new WeixinUser();
		user.setOpenid(openid);
		List<WeixinUser> lst = weinxinUserMapper.find(user);
		if (lst.isEmpty())
			return null;
		return userMapper.get(lst.get(0).getUid());
	}

	public WeixinUser getWeixinUser(String openid) {
		Assert.notNull(openid, "openid is null");
		WeixinUser query = new WeixinUser();
		query.setOpenid(openid);
		List<WeixinUser> lst = weinxinUserMapper.find(query);
		if (lst.isEmpty())
			return null;
		return lst.get(0);
	}

	public void insertUser(User user) {
		userMapper.insert(user);
	}

	public void insertWeixinUser(WeixinUser weixinUser) {
		if (weinxinUserMapper.find(weixinUser).size() > 0) {
			return;
		}
		User user = new User();
		user.setHeadimgurl(weixinUser.getHeadimgurl());
		user.setName(weixinUser.getNickname());
		this.insertUser(user);
		weixinUser.setUid(user.getUid());
		weinxinUserMapper.insert(weixinUser);
	}
	

}
