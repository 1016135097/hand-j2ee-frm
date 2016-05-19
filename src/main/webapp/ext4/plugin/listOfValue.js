
/**
 * LovComboBox
 * Usage:
 * 	    { xtype: "LovComboBox", id:"currencyCode", name: "currencyCode", fieldLabel: "币种", allowBlank: true, emptyText: "币种",lovId:"hand.demo.lov.xxLov",
		 mappings:[{field:"lookup_code",returnItem:"currencyCode"},{field:"meaning",returnItem:"test"}]
		 }
	id 必须指定
	lovId：list of value的ID
	mappings：返回值和控件映射
	callback：如需要手动处理选择结果时指定
 */

Ext.define('LovComboBox', {
	extend : 'Ext.form.field.Trigger',
	xtype : 'LovComboBox',

	matchFieldWidth : false,

	multiSelect : false,

	pickerWidth : 400,

	pickerHeight : 420,

	lovId : '',

	lovJson : '',

	conditionStore : {},

	lovStore : {},
	
	lovStoreId:'',
	
	conditionModelName : '',

	lovForm : {},
	
	lovFormId:'',

	tbl : {},

	picker : {},
	
	mappings:[{field:'',returnItem:''}],
	
	othersParams:[],
	
//	editable:false,

	// 对于行类型的支持
	grid:null,
	
	gridId:null,

	inGrid:false,
	
	oldValue:null,
	
	lov_field:null,
	
	initValue:null,
	// 当前操作的数据行
	gridCurrentRecord:null,
	
	grid_event_editor:null,
	grid_event_e :null,
	grid_event_obj :null,
	
	listeners:{
		"change":function(field,newValue,oldValue,opts){
			if(newValue == null || newValue == "" || newValue == undefined){
				this.clearMappings();
			}
		}
	},
	
	/*
	 * 默认callback
	 */
	callback:function(data){
		this.getGridEditingContext();
		
		for(var i = 0;i < this.mappings.length;i++){
			var returnItemId = this.mappings[i].returnItem;
			if(this.inGrid){
				this.gridCurrentRecord.data[returnItemId] = data[this.mappings[i].field] || '';
				if(returnItemId == this.lov_field){
					this.oldValue = data[this.mappings[i].field];
				}
			} else{
				Ext.getCmp(returnItemId).setValue(data[this.mappings[i].field]);
				if(returnItemId == this.id){
					this.oldValue = data[this.mappings[i].field];
				}
			}
		}
		if(this.inGrid){
			this.grid.getView().refresh();
			this.grid.fireEvent("edit",this.grid_event_editor,this.grid_event_e,this.grid_event_obj);
		}
	},
	
	clearMappings:function(){
		this.getGridEditingContext();
		this.oldValue = null;
		for(var i = 0;i < this.mappings.length;i++){
			var returnItemId = this.mappings[i].returnItem;
			if(this.inGrid){
				this.gridCurrentRecord.data[returnItemId] = "";
			} else{
				Ext.getCmp(returnItemId).setValue("");
			}
		}
		// 刷新表格
		if(this.inGrid){
			this.grid.getView().refresh();
			this.grid.fireEvent("edit",this.grid_event_editor,this.grid_event_e,this.grid_event_obj);
		}
	},
	
	initField : function() {
	    var local = window.location;  
	    var contextPath = local.pathname.split("/")[1];  
	    var basePath = local.protocol+"//"+local.host+"/"+contextPath;  
		
		var me = this;
		
		var conDesc = Ext.getFromCache(me.lovId+"@conDesc");
		var con = Ext.getFromCache(me.lovId+"@con");
		var alias = Ext.getFromCache(me.lovId+"@alias");
		var resultDesc = Ext.getFromCache(me.lovId+"@resultDesc");
		var result = Ext.getFromCache(me.lovId+"@result");
		if(conDesc == null ){
//			console.info('initField');
			Ext.Ajax.request({
				url : basePath+'/lov/get?lovId=' + me.lovId,
				method : 'GET',
				success : function(response, options) {
					var json = eval("(" + response.responseText + ")");

					var conDesc = json.queryConditionDesc;
					var con = json.queryCondition;
					var alias = json.alias;
					var resultDesc = json.resultDisplayDesc;
					var result = json.resultDisplay;
					
					Ext.putIntoCache(me.lovId+"@conDesc",conDesc);
					Ext.putIntoCache(me.lovId+"@con",con);
					Ext.putIntoCache(me.lovId+"@alias",alias);
					Ext.putIntoCache(me.lovId+"@resultDesc",resultDesc);
					Ext.putIntoCache(me.lovId+"@result",result);
					
					
					me.buildLov(conDesc, con, alias,resultDesc,result);

				}
			});
		} else {
			me.buildLov(conDesc, con, alias,resultDesc,result);
		}
		
		// 支持Grid
		if(me.gridId != null){
			me.grid = Ext.getCmp(me.gridId);
			me.inGrid = true;

		}
		
		me.on('blur',function(){
			if(me.readOnly){
				return;
			}
			
			if(me.picker.isHidden() && me.getValue() != me.oldValue){
				me.validateLovValue(me.getValue());
			}
		},me);
		
		if(me.initValue != null){
			me.validateLovValue(me.initValue);
		}
	},
	
	buildLov:function(conDesc,con,alias,resultDesc,result){
	    var local = window.location;  
	    var contextPath = local.pathname.split("/")[1];  
	    var basePath = local.protocol+"//"+local.host+"/"+contextPath;  
		var tmpId = Math.random()*10000;
		
		var me = this;
		var conData = "[";
		for ( var i = 0; i < conDesc.length; i++) {
			conData = conData + "{";
			conData = conData + "code:" + "'" + con[i] + "',";
			conData = conData + "desc:" + "'" + conDesc[i] + "'";
			conData = conData + "},";
		}
		conData = conData + "]";

		conData = eval(conData);

		me.conditionStore = Ext.create('Ext.data.Store', {
			storeId : 'lovConditionStore'+tmpId,
			fields : [ 'code', 'desc' ],
			data : conData
		});

		
		var aliasData = "[";
		for ( var i = 0; i < alias.length; i++) {
			aliasData = aliasData + "{";
			aliasData = aliasData + "name:" + "'" + alias[i] + "'";
			aliasData = aliasData + "},";
		}
		aliasData = aliasData + "]";
//		console.info(aliasData);
		aliasData = eval(aliasData);

		conditionModelName = me.lovId + "Model";

		var model = Ext.define(conditionModelName, {
			extend : 'Ext.data.Model',
			fields : aliasData
		});

		me.lovStoreId = me.lovId +"Store"+tmpId;
		
		me.lovStore = Ext.create('Ext.data.Store', {
			model : model,
			pageSize : 10,
			storeId:me.lovStoreId ,
			proxy : {
				type : 'ajax',
				url : basePath+'/lov/query?lovId=' + me.lovId,
				actionMethods:{create: "POST", read: "POST", update: "POST", destroy: "POST"},
				reader : {
					type : 'json',
					totalProperty : 'totalCount',
					root:'valueList'
				}
			},
			autoLoad : false
		});
		
		me.lovFormId = me.lovId+"Form"+tmpId;

		me.lovForm = Ext.create('Ext.form.Panel', {
			width : me.pickerWidth - 10,
			height : 80,
			title : "查找",
			region : 'north',
			layout : {
				type : 'hbox',
				column : 3
			},
			id:me.lovFormId,
			// renderTo : me.picker,
			// bodyPadding : 5,
			defaultType : 'textfield',
			fieldDefaults : {
				labelWidth : 80,
				margin : 5
			},
			items : [ {
				xtype : "combo",
				width : 100,
				name : "search",
				displayField : 'desc',
				valueField : 'code',
				fieldLabel : "",
				allowBlank : true,
				autoSelect:true,
				editable:false,
				store : me.conditionStore,
			},

			{
				fieldLabel : '',
				name : 'searchConditions'
			},

			{
				xtype : "button",
				text : "查找",
				margin : 5,
				handler:function(){
					  var queryConditions = me.lovForm.getForm().getValues();
					  var lovDataStore = me.lovStore;
					  var params = me.othersParams;
					  var otherParam = "{";
					  for(var i=0;i<params.length;i++){
						  if(params[i].field != null && params[i].field != undefined){
							  otherParam += "'otherParamName["+i+"]':" +"'"+params[i].field+"',";
						  }
						  if(params[i].value != null && params[i].value != undefined &&params[i].value != ''){
							  otherParam += "'othersParam["+i+"]':"+"'"+params[i].value+"',";
						  } else{
							  var value ;
							  if(me.inGrid){
								   me.getGridEditingContext();
								   value = me.gridCurrentRecord.data[params[i].item];
								   if(value == undefined){
									  value = Ext.getCmp(params[i].item).getValue();
									  if(value instanceof Date){
										  value = value.format('yyyy/MM/dd')
									  }
								   }
							  } else{
								  value = Ext.getCmp(params[i].item).getValue();
								  if(value instanceof Date){
									  value = value.format('yyyy/MM/dd')
								  }
							  }
							  otherParam += "'othersParam["+i+"]':"+"'"+value+"',";
						  }
						  
					  }
					  otherParam += "}";
					  otherParam = eval("("+otherParam+")");
					  queryConditions = Ext.extendsJson(queryConditions,otherParam);
					  // 带参数分页
					  Ext.apply(me.lovStore.proxy.extraParams,queryConditions);
					  
					  lovDataStore.loadPage(1,{params:queryConditions});
					}
			} ]
		});

		var pagebar = new Ext.PagingToolbar({
			pageSize : 10,
			store : me.lovStore,
			displayInfo : true,
			displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : "没有记录"
		});

		var resultData = "[";
		for ( var i = 0; i < resultDesc.length; i++) {
			resultData = resultData + "{";
			resultData = resultData + "dataIndex:" + "'" + result[i]
					+ "',";
			resultData = resultData + "header:" + "'" + resultDesc[i]
					+ "'";
			resultData = resultData + "},";
		}
		resultData = resultData + "]";

		resultData = eval(resultData);

		me.tbl = Ext.create('Ext.grid.Panel', {
			store : '',
			region : 'center',
			columns : resultData,
			height : me.pickerHeight - 100,
			width : me.pickerWidth - 20,
			loadmark:true,
			viewConfig : {
				 forceFit: true
			},
			store : me.lovStore,
			bbar : pagebar,
			multiSelect:false,
			buttons : [ {
				xtype : "button",
				text : "选择",
				handler:function(){
					var selection = me.tbl.getSelectionModel().getSelection();
					if(selection.length == 0){
						Ext.Msg.alert('错误', '请选择一行记录');
					}else{
						me.picker.hide();
						me.callback(selection[0].data);
						
					}
					
				}
			} ],
			listeners:{ 
				itemdblclick:function(grid, rowIndex, e){
					var selection = grid.getSelectionModel().getSelection();  
					me.picker.hide();
					me.callback(selection[0].data);
				}
			}
		});
		
		var loadMarsk = new Ext.LoadMask(me.tbl, {  
		    msg     : '数据加载中，请稍候......', 
		    store   : me.lovStore  
		});  
		
		console.info('create Picker');
		var picker = Ext.create('Ext.window.Window', {
			title : '',
			height : me.pickerHeight + 10,
			width : me.pickerWidth,
			items : [me.lovForm,me.tbl],
			draggable : true,
			modal : true,
			closable:true,
			closeAction:function(){
				me.picker.hide();
			}
		});
		me.picker = picker;
	},
	onTriggerClick : function() {
		if(this.inGrid){
			this.lovStore.removeAll();
		}
		this.picker.show();

	},
	validateLovValue :function(value,callback){
		Ext.getBody().mask("数据验证中，请稍等...");
		var field = null;
		var me = this;
		
	    var local = window.location;  
	    var contextPath = local.pathname.split("/")[1];  
		var basePath = local.protocol+"//"+local.host+"/"+contextPath;  
			
		me.getGridEditingContext();
		for(var i = 0; i < me.mappings.length; i++ ){
			if(me.inGrid){
				if( me.mappings[i].returnItem == me.lov_field){
					field = me.mappings[i].field;
				}
			}else{
				if( me.mappings[i].returnItem == me.id){
					field = me.mappings[i].field;
				}
			}
		}

		if(field != null && value != null && value != '' && value != undefined && me.oldValue != value){
			 
			var queryConditions = eval("({'search':'"+ field+"','searchConditions':'"+value+"'})");

			 var params = me.othersParams;
			 var otherParam = "{";
			  for(var i=0;i<params.length;i++){
				  if(params[i].field != null && params[i].field != undefined){
					  otherParam += "'otherParamName["+i+"]':" +"'"+params[i].field+"',";
				  }
				  if(params[i].value != null && params[i].value != undefined &&params[i].value != ''){
					  otherParam += "'othersParam["+i+"]':"+"'"+params[i].value+"',";
				  } else{
					  var value ;
					  if(me.inGrid){
						   me.getGridEditingContext();
						   value = me.gridCurrentRecord.data[params[i].item];
						   if(value == undefined){
							  value = Ext.getCmp(params[i].item).getValue();
							  if(value instanceof Date){
								  value = value.format('yyyy/MM/dd')
							  }
						   }
					  } else{
						  value = Ext.getCmp(params[i].item).getValue();
						  if(value instanceof Date){
							  value = value.format('yyyy/MM/dd')
						  }
					  }
					  otherParam += "'othersParam["+i+"]':"+"'"+value+"',";
				  }
				  
			  }
			  otherParam += "}";
			  otherParam = eval("("+otherParam+")");
			  queryConditions = Ext.extendsJson(queryConditions,otherParam);
			  Ext.Ajax.request({
					url : basePath+'/lov/query?lovId=' + me.lovId,
					method : 'POST',
					params:queryConditions,
					success : function(response, action) {
						var valueList = eval("("+response.responseText+")").valueList;
						if(valueList.length == 0){
							me.clearMappings();
						} else{
							var record = valueList[0];
							me.callback(record);
						}
						Ext.getBody().unmask();
						if(callback != undefined){
							callback();
						}
					},
					failure : function(form, action) {
						Ext.getBody().unmask();
					}
			  });
		} else{
			Ext.getBody().unmask();
		}
	},
	getGridEditingContext:function(){
		var me = this;
		if(me.inGrid){
			var context = me.grid.getPlugin().context;
			me.gridCurrentRecord = context.record;
			me.lov_field = context.field;
			me.grid_event_e = context;
		}
	}
});