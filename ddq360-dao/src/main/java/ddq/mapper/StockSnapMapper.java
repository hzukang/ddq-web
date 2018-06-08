package ddq.mapper;

import java.util.List;

import ddq.model.StockSnap;

public interface StockSnapMapper {
	int insertStockSnapByList(List<StockSnap> mapper);
	int deleteStockSnapByTime(String mapper);
}
