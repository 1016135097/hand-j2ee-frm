package hand.framework.coa.controller;

import hand.framework.coa.entity.COAConfig;
import hand.framework.coa.service.COAService;
import hand.framework.ebs.controller.EbsBaseController;
import hand.framework.json.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/coa/")
public class COAController extends EbsBaseController{

	@Resource(name="coaService")
	private COAService coaService;
	
	@RequestMapping(value="getCOAs", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getCOAConfigs(BigDecimal orgId){
		List<COAConfig> coas = coaService.getCoaConfigs(orgId);
		return JsonUtils.collection2json(coas);
	}
	
	@RequestMapping(value="getCCID", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getCCID(BigDecimal orgId,String segments){
		Integer ccid = coaService.getCCID(orgId, segments);
		String coaDesc = "";
		if(ccid > 0){
			coaDesc = coaService.getCOADesc(orgId, new BigDecimal(ccid));
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ccid", ccid);
		params.put("segmentsDesc", coaDesc);
		return JsonUtils.commonSuccess(null, params);
	}
	
}
