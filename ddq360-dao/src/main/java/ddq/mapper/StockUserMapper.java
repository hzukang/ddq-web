package ddq.mapper;

import java.util.List;

import ddq.model.StockUser;

public interface StockUserMapper {
	
	List<StockUser> selectStockUser(StockUser stockuser);
	
	List<StockUser> selectStockUserByUid(String uid);

}
