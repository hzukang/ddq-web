package ddq.mapper;

import java.util.List;

import ddq.model.StockFlow;


public interface StockFlowMapper {
   List<StockFlow> selectStockFlowByDate(String date,String date1);

}
