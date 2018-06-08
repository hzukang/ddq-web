package ddq.mapper;

import java.util.List;

import ddq.model.WeixinUser;

public interface WeixinUserMapper {
	
	
	
	List<WeixinUser> find(WeixinUser user);
	
	void insert(WeixinUser user);

}
