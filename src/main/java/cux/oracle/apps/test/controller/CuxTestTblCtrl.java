package cux.oracle.apps.test.controller;

import java.util.List;

import cux.oracle.apps.test.entity.CuxTestTblEO;
import cux.oracle.apps.test.service.CuxTestTblService;
import hand.framework.json.JsonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class CuxTestTblCtrl {

	@Autowired
	private CuxTestTblService cuxTestTblService;

	@RequestMapping("/save")
	@ResponseBody
	public String save(CuxTestTblEO eo) {
		cuxTestTblService.save(eo);
		return JsonUtils.commonSuccess("Save Success, myId is :"+eo.getMyId(), null);
	}

	@RequestMapping("/getAll")
	@ResponseBody
	public String getAll() {
		List<CuxTestTblEO> results = cuxTestTblService.getAll();
		return JsonUtils.collection2json(results);
	}
}
