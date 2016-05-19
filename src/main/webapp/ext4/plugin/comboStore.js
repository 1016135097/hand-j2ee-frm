Ext.define('LookupCodeModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'lookupCode',
		type : 'string'
	}, {
		name : 'meaning',
		type : 'string'
	} ]
});

function getAjaxComboStore(lookuptype, unlimited) {

	var local = window.location;
	var contextPath = local.pathname.split("/")[1];
	var basePath = local.protocol + "//" + local.host + "/" + contextPath;

	var comboStore = Ext.data.StoreManager.lookup(lookuptype + "Store");

	if (comboStore == null) {
		var comboStore = Ext.create('Ext.data.Store',
						{
							storeId : lookuptype + "Store",
							model : 'LookupCodeModel',
							proxy : {
								type : 'ajax',
								url : basePath
										+ '/lookup/getLookups?lookupId=hand.common.lookup&otherParamName[0]=lookup_type&othersParam[0]='
										+ lookuptype,
								reader : {
									type : 'json',
									rootProperty : ''
								}
							},
							autoLoad : false
						});
	}
	// 缓存
//	var data = Ext.getFromCache(lookuptype);
//	if(data != null){
//		comboStore.loadData(data);
//		if (unlimited == true) {
//			var rs =  { lookupCode : '', meaning:'--无限制--' };
//			comboStore.insert(0,rs);
//		}
//		return comboStore;
//	}
	
	// 在头部添加一个空的表示[无限制的]记录
//	if (unlimited == true) {
//		comboStore.addListener({'load': function(store, records, options){
//										var rs =  {
//											lookupCode : '',
//											meaning:'--无限制--'
//										};
//										store.insert(0,rs);
//								
//									}
//		});
//	}
	comboStore.addListener({'load': function(store, records, options){
		var data = store.proxy.getReader().jsonData;
		Ext.putIntoCache(lookuptype,data);
		
	}
	});
	comboStore.load();
	
	return comboStore;
}

function getAjaxComboStoreById(lookupId, fields, params, autoLoad) {

	if (autoLoad == null || autoLoad == undefined) {
		autoLoad = true;
	}

	if (fields == null || fields == undefined) {
		fields = [ {
			name : 'lookupCode',
			type : 'string'
		}, {
			name : 'meaning',
			type : 'string'
		} ];
	}

	var local = window.location;
	var contextPath = local.pathname.split("/")[1];
	var basePath = local.protocol + "//" + local.host + "/" + contextPath;

	var comboStore = Ext.data.StoreManager.lookup(lookupId + "Store");

	var otherParam = "{";

	if (params != null && params != undefined) {
		for ( var i = 0; i < params.length; i++) {
			if (params[i].field != null && params[i].field != undefined
					&& params[i].field != '') {
				otherParam += "'otherParamName[" + i + "]':" + "'"
						+ params[i].field + "',";
			}

			if (params[i].value != null && params[i].value != undefined
					&& params[i].value != '') {
				otherParam += "'othersParam[" + i + "]':" + "'"
						+ params[i].value + "',";
			}

		}
	}
	otherParam += "}";
	otherParam = eval("(" + otherParam + ")");

	if (comboStore == null) {

		comboStore = Ext.create('Ext.data.Store', {
			storeId : lookupId + "Store",
			// model: 'LookupCode',
			fields : fields,
			proxy : {
				type : 'ajax',
				url : basePath + '/lookup/getLookups?lookupId=' + lookupId,
				actionMethods : {
					read : 'POST' // by default GET
				},
				reader : {
					type : 'json',
					rootProperty : ''
				}
			},
			autoLoad : false
		});

		Ext.apply(comboStore.proxy.extraParams, otherParam);
		
	} else {
		Ext.apply(comboStore.proxy.extraParams, otherParam);
	}
	
	comboStore.addListener({'load': function(store, records, options){
		var data = store.proxy.getReader().jsonData;
		var params = JSON.stringify(comboStore.proxy.extraParams);
		Ext.putIntoCache(lookupId+"@"+params,data);
		}
	});
	
	if (autoLoad) {
		// 缓存
		var params = JSON.stringify(comboStore.proxy.extraParams);
		var data = Ext.getFromCache(lookupId+"@"+params,data);
		if(data != null){
			comboStore.loadData(data);
		} else {
			comboStore.load();
		}
	}
	return comboStore;
}