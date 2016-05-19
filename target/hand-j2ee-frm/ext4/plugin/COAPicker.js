Ext.define('COAPicker', {
	extend : 'Ext.form.field.Trigger',
	
	xtype : 'COAPicker',

	pickerWidth : 480,
	
	pickerHeight : 430,
	
	multiSelect : false,
	
	matchFieldWidth : false,
	
	flex_count : 0,
	
	org_id_item_id: null,
	
	org_id : null,
	
	coaForm : null,
	
	coaFormId : null,
	
	picker : {},
	
	ccid_field:null,
	
	// 对于行类型的支持
	grid:null,
	
	gridId:null,

	inGrid:false,
	
	oldValue:null,
	
	coa_field:null,
	
	desc_field:null,
	
	initValue:null,
	// 当前操作的数据行
	gridCurrentRecord:null,
	
	grid_event_editor:null,
	grid_event_e :null,
	grid_event_obj :null,
	
	callback:function(data){
		if(this.inGrid){
			this.getGridEditingContext();
			this.gridCurrentRecord.data[this.coa_field] = data.segments;
			this.gridCurrentRecord.data[this.ccid_field]= data.ccid;
			if(this.desc_field != null){
				this.gridCurrentRecord.data[this.desc_field]= data.segmentsDesc;
			}
		} else{
			Ext.getCmp(this.ccid_field).setValue(data.ccid);
			this.setValue(data.segments);
			if(this.desc_field != null){
				Ext.getCmp(this.desc_field).setValue(data.segmentsDesc);
			}
		}
		this.oldValue = data.segments;
		// 刷新表格
		if(this.inGrid){
			this.grid.getView().refresh();
			this.grid.fireEvent("edit",this.grid_event_editor,this.grid_event_e,this.grid_event_obj);
		}
	},
	clearMappings:function(){
		if(this.inGrid){
			this.getGridEditingContext();
			this.gridCurrentRecord.data[this.coa_field] = null;
			this.gridCurrentRecord.data[this.ccid_field]= null;
			if(this.desc_field != null){
				this.gridCurrentRecord.data[this.desc_field]= null;
			}
		} else{
			Ext.getCmp(this.ccid_field).setValue(null);
			this.setValue(null);
			if(this.desc_field != null){
				Ext.getCmp(this.desc_field).setValue(null);
			}
		}
		this.oldValue = null;
		// 刷新表格
		if(this.inGrid){
			this.grid.getView().refresh();
			this.grid.fireEvent("edit",this.grid_event_editor,this.grid_event_e,this.grid_event_obj);
		}
	},
	
	initField : function() {
		var me = this;

		var tmpId = Math.random()*10000;
		
		me.coaFormId = "COAForm"+tmpId;
		
		var basePath = Ext.getBasePath();
		
		var orgId = me.org_id_item_id != null?Ext.getCmp(me.org_id_item_id).getValue():me.org_id;
		
		
		if(orgId == null || orgId == '' || orgId == undefined){
			return;
		}
		
		me.org_id = orgId;
		
		Ext.Ajax.request({
			url : basePath+'/coa/getCOAs?orgId=' + orgId,
			method : 'GET',
			success : function(response, options) {

						var items = [];
				
						var data = eval("(" + response.responseText + ")");
						
						for(var i=0;i<data.length;i++){
							var segmentNum = data[i].segmentNum;
							var prompt = data[i].prompt;
							var itemId = me.coaFormId+"segment"+segmentNum;
							var descId = me.coaFormId+"segmentDesc"+segmentNum;
							items.push({
								xtype : "container",
								layout : "hbox",
								items : [{
										xtype : "LovComboBox",
										id : itemId,
										name : itemId,
										fieldLabel : prompt,
										allowBlank : false,
										lovId : "hand.framework.coa.Segments",
										mappings : [{
													field : "flex_value",
													returnItem : itemId
												},{
													field:"description",
													returnItem:descId
												}],
										othersParams:[{
											field:"org_id",
											value:orgId,
										},{
											field:"segment_num",
											value:segmentNum,
										}]
							
							},{xtype:'textfield',
								   id:descId,
								   name:descId,
								   readOnly:true,
								   labelWidth:0,
								   fieldStyle:{'border':0,background:'none'},
							}]
							});
							

							me.flex_count = me.flex_count + 1;
						}
						
						me.coaForm = Ext.create('Ext.form.Panel', {
							width : me.pickerWidth - 10,
							height : me.pickerHeight - 50,
							title : "",
							layout: 'form',
							id:me.coaFormId,
							items:items,
							fieldDefaults : {
								labelWidth : 80,
								margin : 5
							},
							buttons:[{  xtype : "button",
										text : "确定",
										handler:function(){
											var segments ='';
											var segmentsDesc = '';
											var values = me.coaForm.getForm().getValues();
											
											for(var i = 1;i<=me.flex_count;i++){
												var itemId = me.coaFormId+"segment"+i;
												var descId = me.coaFormId+"segmentDesc"+i;
												segments = segments + values[itemId];
												segmentsDesc = segmentsDesc + values[descId];
												if(i != me.flex_count){
													segments = segments +'.';
													segmentsDesc = segmentsDesc +".";
												}
												
											}
											
											params = {'orgId':orgId,'segments':segments};
											
											me.coaForm.mask("数据加载中，请稍等..");
											Ext.Ajax.request({
												url : basePath+'/coa/getCCID',
												method : 'POST',
												params : params,
												success : function(response, action) {
													me.coaForm.unmask();
													var result = Ext.JSON
															.decode(response.responseText);
													if (result.status == 'S') {
														me.picker.hide();
														me.callback({'segments':segments,
																	 'ccid':result.data.ccid,
																	 'segmentsDesc':segmentsDesc});
													} else {
														me.coaForm.unmask();
														Ext.MessageBox.alert('失败',
																'获取CCID失败：' + result.msg);
													}
												}
											});
											
											
											
										}}
							         ]
						});
						
						// 支持Grid
						if(me.gridId != null){
							me.grid = Ext.getCmp(me.gridId);
							me.inGrid = true;
						}
						
						var picker = Ext.create('Ext.window.Window', {
							title : '科目',
							height : me.pickerHeight,
							width : me.pickerWidth,
							items : [me.coaForm],
							draggable : true,
							modal : true,
							closable:true,
							closeAction:function(){
								me.picker.hide();
							}
						});
						me.picker = picker;
						
						me.on('blur',function(){
							if(me.picker.isHidden() && me.getValue() != me.oldValue){
								me.validateCoaValue(me.getValue());
							} else if(me.getValue() == null || me.getValue() == null){
								me.clearMappings();
							}
						},me);
						
					  }
			});
	},

	onTriggerClick : function() {
		var me = this;
		
		var basePath = Ext.getBasePath();
		
		var orgId = me.org_id == null?Ext.getCmp(me.org_id_item_id).getValue():me.org_id;
		
		if(orgId == null || orgId == '' || orgId == undefined){
			return;
		}
		
		if(orgId != me.org_id){
		me.org_id  = orgId;
		Ext.Ajax.request({
			url : basePath+'/coa/getCOAs?orgId=' + orgId,
			method : 'GET',
			success : function(response, options) {

						var items = [];
				
						var data = eval("(" + response.responseText + ")");
						
						for(var i=0;i<data.length;i++){
							var segmentNum = data[i].segmentNum;
							var prompt = data[i].prompt;
							var itemId = me.coaFormId+"segment"+segmentNum;
							var descId = me.coaFormId+"segmentDesc"+segmentNum;
							items.push({
								xtype : "container",
								layout : "hbox",
								items : [{
										xtype : "LovComboBox",
										id : itemId,
										name : itemId,
										fieldLabel : prompt,
										allowBlank : false,
										lovId : "hand.framework.coa.Segments",
										mappings : [{
													field : "flex_value",
													returnItem : itemId
												},{
													field:"description",
													returnItem:descId
												}],
										othersParams:[{
											field:"org_id",
											value:orgId,
										},{
											field:"segment_num",
											value:segmentNum,
										}]
							
							},{xtype:'textfield',
							   id:descId,
							   name:descId,
							   readOnly:true,
							   labelWidth:0,
							   fieldStyle:{'border':0,background:'none'},
							}]
							});

							me.flex_count = me.flex_count + 1;
						}
						
						me.coaForm = Ext.create('Ext.form.Panel', {
							width : me.pickerWidth - 10,
							height : me.pickerHeight - 50,
							title : "",
							layout:'form',
							id:me.coaFormId,
							items:items,
							fieldDefaults : {
								labelWidth : 80,
								margin : 5
							},
							buttons:[{  xtype : "button",
										text : "确定",
										handler:function(){
											var segments ='';
											var segmentsDesc = '';
											var values = me.coaForm.getForm().getValues();
											
											for(var i = 1;i<=me.flex_count;i++){
												var itemId = me.coaFormId+"segment"+i;
												var descId = me.coaFormId+"segmentDesc"+i;
												segments = segments + values[itemId];
												segmentsDesc = segmentsDesc + values[descId];
												if(i != me.flex_count){
													segments = segments +'.';
													segmentsDesc = segmentsDesc +".";
												}
												
											}
											params = {'orgId':orgId,'segments':segments};
											
											me.coaForm.mask("数据加载中，请稍等..");
											Ext.Ajax.request({
												url : basePath+'/coa/getCCID',
												method : 'POST',
												params : params,
												success : function(response, action) {
													me.coaForm.unmask();
													var result = Ext.JSON
															.decode(response.responseText);
													if (result.status == 'S') {
														me.picker.hide();
														me.callback({'segments':segments,
																	 'ccid':result.data.ccid,
																	 'segmentsDesc':segmentsDesc});
													} else {
														me.coaForm.unmask();
														Ext.MessageBox.alert('失败',
																'获取CCID失败：' + result.msg);
													}
												}
											});
											
											
											
										}}
							         ]
						});
						
						// 支持Grid
						if(me.gridId != null){
							me.grid = Ext.getCmp(me.gridId);
							me.inGrid = true;
						}
						
						var picker = Ext.create('Ext.window.Window', {
							title : '科目',
							height : me.pickerHeight,
							width : me.pickerWidth,
							items : [me.coaForm],
							draggable : true,
							modal : true,
							closable:true,
							closeAction:function(){
								me.picker.hide();
							}
						});
						me.picker = picker;
						
						me.on('blur',function(){
							if(me.picker.isHidden() && me.getValue() != me.oldValue){
								me.validateCoaValue(me.getValue());
							} else if(me.getValue() == null || me.getValue() == null){
								me.clearMappings();
							}
						},me);
						me.setSegmentsValues();
						me.picker.show();
					  }
			});
		}else{
			this.setSegmentsValues();
			this.picker.show();

		}
	},
	validateCoaValue :function(value,callback){
		var field = null;
		
		var me = this;
	    
		if(value == null || value == '' || value == undefined){
			me.clearMappings();
			return;
		}
		
		var basePath = Ext.getBasePath();
		
		var orgId = me.org_id == null?Ext.getCmp(me.org_id_item_id).getValue():me.org_id;
		var segments = value;
		params = {'orgId':orgId,'segments':segments};
		Ext.getBody().mask("数据验证中，请稍等..");
		Ext.Ajax.request({
			url : basePath+'/coa/getCCID',
			method : 'POST',
			params : params,
			success : function(response, action) {
				var result = Ext.JSON.decode(response.responseText);
				if (result.status == 'S') {
					me.callback({'segments':segments,'ccid':result.data.ccid,'segmentsDesc':result.data.segmentsDesc});
				} else {
					me.clearMappings();
				}
				Ext.getBody().unmask();
				if(callback != undefined){
					callback();
				}
			},
			failure:function(){
				Ext.getBody().unmask();
			}
		});
	},
	getGridEditingContext:function(){
		if(this.inGrid){
			var me = this;
			var context = me.grid.getPlugin().context;
			me.gridCurrentRecord = context.record;
			me.coa_field = context.field;
			me.grid_event_e = context;
		}
	},
	setSegmentsValues:function(){
		var me = this;
		if(me.inGrid){
			me.getGridEditingContext();
			var segmentsStr = me.gridCurrentRecord.data[me.coa_field];
			var segmentsDescStr = me.gridCurrentRecord.data[me.desc_field];
			if(segmentsStr != null && segmentsStr != ''){
				var segments = segmentsStr.split('.');
				var segmentsDesc = segmentsDescStr.split('.');
				for(var i = 0;i < segments.length;i++){
				    var itemId = me.coaFormId+"segment"+(i+1);
				    var descId = me.coaFormId+"segmentDesc"+(i+1);
				    Ext.getCmp(itemId).setValue(segments[i]);
				    Ext.getCmp(descId).setValue(segmentsDesc[i]);
				}
			} else{
				me.clearSegmentsValue();
			}
		} else{
			var segmentsStr = me.getValue();
			var segmentsDescStr =Ext.getCmp(me.desc_field).getValue();
			if(segmentsStr != null && segmentsStr != ''){
				var segments = segmentsStr.split('.');
				var segmentsDesc = segmentsDescStr.split('.');
				for(var i = 0;i < segments.length;i++){
				    var itemId = me.coaFormId+"segment"+(i+1);
				    var descId = me.coaFormId+"segmentDesc"+(i+1);
				    Ext.getCmp(itemId).setValue(segments[i]);
				    Ext.getCmp(descId).setValue(segmentsDesc[i]);
				}
			} else{
				me.clearSegmentsValue();
			}
		}
		
	},
	clearSegmentsValue:function(){
		var me = this;
		for(var i = i;i <= me.flex_count;i++){
		    var itemId = me.coaFormId+"segment"+i;
		    var descId = me.coaFormId+"segmentDesc"+(i+1);
		    Ext.getCmp(itemId).setValue();
		    Ext.getCmp(descId).setValue();
		}
	}
});