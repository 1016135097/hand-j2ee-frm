package hand.framework.lov.controller;

import hand.framework.json.JsonUtils;
import hand.framework.lov.entity.GeneralLov;
import hand.framework.lov.entity.LovOtherParams;
import hand.framework.lov.utils.LovManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/WEB-INF/lov/")
public class GeneralLovCO {

	private static Logger logger = Logger.getLogger(GeneralLovCO.class);

	@Resource(name = "lovManager")
	private LovManager lovManager;

	@RequestMapping("go")
	public ModelAndView forwardToLovPage(String lovId) {
		GeneralLov lov = lovManager.getLov(lovId);
		if (lov == null) {
			throw new RuntimeException("Can not found lov with ID:" + lovId);
		}
		ModelAndView m = new ModelAndView("listOfValue");
		m.addObject("LovObject", lov);

		return m;
	}

	@RequestMapping(value = "get", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getLovInfoJson(String lovId) {
		GeneralLov lov = lovManager.getLov(lovId);
		if (lov == null) {
			throw new RuntimeException("Can not found lov with ID:" + lovId);
		}
		return JsonUtils.bean2json(lov.getConfig());
	}

	@RequestMapping(value = "query", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String queryLovData(String lovId, String search,
			String searchConditions, LovOtherParams params, Integer start, Integer limit) {
		logger.info("Query Lov:" + lovId);

		GeneralLov lov = lovManager.getLov(lovId);
		if (lov == null) {
			throw new RuntimeException("Can not found lov with ID:" + lovId);
		}
		
		if (params != null && params.getOthersParam() != null) {
			List<String> paramsNames = params.getOtherParamName();
			List<String> paramsValues = params.getOthersParam();

			for (int i = 0; i < paramsNames.size(); i++) {
				if(paramsNames.get(i) != null && !"".equals(paramsNames.get(i))){
					lov.addQueryCondition(paramsNames.get(i));
				}
				lov.addParams(paramsValues.get(i));
			}
		}

		if (search != null && !"".equals(search)) {
			lov.addQueryCondition(search);
			lov.addParams(searchConditions);
		}

		
		if(start != null){
			lov.setPage(start, limit);
		}

		lovManager.queryLov(lov);
		String json = JsonUtils.bean2json(lov.getQueryResult());

		logger.info("Result:" + json);

		return json;

	}
}
