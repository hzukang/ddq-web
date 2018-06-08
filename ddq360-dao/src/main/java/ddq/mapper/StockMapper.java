package ddq.mapper;

import java.util.List;

import ddq.model.Stock;

public interface StockMapper {
	int insertStock(Stock mapper);
	int insertStockList(List<Stock> mapper);
}

