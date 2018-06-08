package app;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.ModelMap;  
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import ddq.model.*;
import ddq.service.*;

@Controller  
@RequestMapping("/cash")  
public class CashController {
	private static Logger logger = LoggerFactory.getLogger(CashController.class);

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private CashService cashService;

	@RequestMapping(value="/cashTran2", method = RequestMethod.GET)
	public @ResponseBody String cashTran2() throws Exception {
		try
		{
			Cash ent=new Cash();
            ent.setCash(10000.);
            ent.setUid(9802);
			ent.setRemark("18gg12");
			ent.setCurrency(1);
			ent.setCreatedate(new Date());
			ent.setUpdatedate(new Date());
			int ident=cashService.addCash(ent);
			Gson gson=new Gson();
			return ent.getCash()+"gg"+ident+"hh";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
		

	}

	@RequestMapping(value="/cashflow", method = RequestMethod.GET)
	public @ResponseBody String cashflow() throws Exception {
		try
		{
			CashFlow ent=new CashFlow();
			ent.setCash(-10.7);
			ent.setId(17);
			ent.setUid(9802);
			ent.setStatus(1);
			ent.setCurrency(1);
			ent.setOperation(2);
			ent.setTimestamp(new Date());

			int ident=cashService.updateCash(ent,-13.7);
			Gson gson=new Gson();
			return ent.getCash()+"gg"+ident+"hh";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}

	}

	@RequestMapping(value="/delcashflow", method = RequestMethod.GET)
	public @ResponseBody String delcashflow() throws Exception {
		try
		{
			CashFlow ent=new CashFlow();
			ent.setCash(-10.7);
			ent.setId(17);
			ent.setUid(9802);
			ent.setStatus(0);
			ent.setCurrency(1);
			ent.setOperation(3);
			ent.setTimestamp(new Date());

			int ident=cashService.deleteCashFlowById(17);
			Gson gson=new Gson();
			return ent.getCash()+"gg"+ident+"hh";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}

	}


} 

