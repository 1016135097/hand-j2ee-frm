package hand.framework.lookup.controller;

import hand.framework.lookup.entity.LookupOtherParams;
import hand.framework.lookup.service.GeneralLookupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/WEB-INF/lookup/")
public class GeneralLookupCO {

	@Resource(name="generalLookupService")
	private GeneralLookupService generalLookupService;
	
	@ResponseBody
	@RequestMapping(value="getLookups", produces = "application/json; charset=utf-8")
	public String getLoookups(String lookupId,LookupOtherParams params){
//		if("SET_SETTLEMENT_TYPE".equals(type)){
//			ccriteria.addCriterion(" cux_fin_privilege_pub.check_access_st(lookup_code) = 'Y'");
//		}else if("SET_BUSINESS_TYPE".equals(type)){
//			ccriteria.addCriterion(" cux_fin_privilege_pub.check_access_ot(lookup_code) = 'Y'");
//		}
		
		return generalLookupService.queryLookup(lookupId, params);
	}
}
