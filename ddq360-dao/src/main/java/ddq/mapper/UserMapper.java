package ddq.mapper;

import java.util.List;

import ddq.model.User;

public interface UserMapper {

	User get(Long uid);
	
	List<User> find(User user);
	
	void insert(User user);
}
