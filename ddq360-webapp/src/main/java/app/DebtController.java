package app;

import java.text.*;
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
@RequestMapping("/debt")  
public class DebtController {
	private static Logger logger = LoggerFactory.getLogger(DebtController.class);

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private DebtService debtService;

	@RequestMapping(value="/add", method = RequestMethod.GET)
	public @ResponseBody String cashTran2() throws Exception {
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Debt ent=new Debt();
			ent.setDebt(2013.7);
			ent.setUid(9802);
			ent.setCurrency(1);
			ent.setCreatedate(new Date());
			ent.setStarttime(sdf.parse("2015-01-12"));
			ent.setRate(0.08);
			int ident=debtService.addDebt(ent);
			Gson gson=new Gson();
			return ent.getFlowid()+"gg"+ident+"hh";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}

	}

	@RequestMapping(value="/roll", method = RequestMethod.GET)
	public @ResponseBody String rolldebt() throws Exception {
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Debt ent=new Debt();
			ent.setDebt(2013.7);
			ent.setUid(9802);
			ent.setCurrency(1);
			ent.setCreatedate(new Date());
			ent.setStarttime(sdf.parse("2015-01-12"));
			ent.setRate(0.08);
			int ident=debtService.reduceDebt(ent, true);

			Gson gson=new Gson();
			return ent.getFlowid()+"gg"+ident+"hh";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}

	}


}
