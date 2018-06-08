package ddq.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

import ddq.mapper.StockShowMapper;
import ddq.model.StockShow;

@Service
@Transactional
public class StockShowService {

	@Autowired
	private StockShowMapper stockShowMapper;

	public void addStockShow(Long uid, List<String> stockIds) {
		Assert.notNull(uid, "uid is null");
		Assert.notEmpty(stockIds, "stockIds is empty");

		StockShow stackShow = new StockShow();
		stackShow.setUid(uid);
		List<StockShow> lst = stockShowMapper.find(stackShow);
		Set<String> set = Sets.newHashSet(stockIds);
		for (StockShow s : lst) {
			set.add(s.getStockid());
		}
		for (String s : stockIds) {
			if (set.contains(s))
				continue;
			StockShow ss = new StockShow();
			ss.setSortvalue(1);
			ss.setStatus(1);
			ss.setStockid(s);
			ss.setUid(uid);
			stockShowMapper.insert(ss);
		}
	}
	public void deleteStockShow(Long uid, List<String> stockIds) {
		Assert.notNull(uid, "uid is null");
		Assert.notEmpty(stockIds, "stockIds is empty");
		for (String s : stockIds) {
			stockShowMapper.deleteByStockId(s, uid);
		}
	}

	public void sortStockShow(Long uid, List<String> stockIds) {
		Assert.notNull(uid, "uid is null");
		Assert.notEmpty(stockIds, "stockIds is empty");
		for (int i = 0; i < stockIds.size(); i++) {
			stockShowMapper.updateSortById(uid, stockIds.get(0), i + 1);
		}
	}
}
