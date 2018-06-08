package ddq.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import ddq.model.StockShow;

public interface StockShowMapper {
      
	  void insert(StockShow stackShow);

	  void deleteByStockId(@Param("sotckid")String stockId,@Param("uid")Long uid);
	  
	  void updateSortById(@Param("uid")Long uid,@Param("stockid")String stockid,@Param("sortvalue")Integer sortValue);
	  
	  List<StockShow> find(StockShow stackShow);
}

