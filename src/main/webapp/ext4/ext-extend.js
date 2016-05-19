// session超时
Ext.Ajax.on('requestcomplete',function(conn,response,options) {     
	   if(response && response.getResponseHeader && response.getResponseHeader('_timeout')){    
	       Ext.Msg.alert('提示', '会话超时，请重新登录!', function(){
	    	   var result = Ext.JSON.decode(response.responseText);
	           window.location = result.url;     
	       });   
	   }});   
// 对Load进行拓展
Ext.define('Ext.form.action.Load', {
			extend : 'Ext.form.action.Action',
			requires : ['Ext.data.Connection'],
			alternateClassName : 'Ext.form.Action.Load',
			alias : 'formaction.load',

			type : 'load',

			run : function() {
				Ext.Ajax.request(Ext.apply(this.createCallback(), {
							method : this.getMethod(),
							url : this.getUrl(),
							headers : this.headers,
							params : this.getParams()
						}));
			},

			onSuccess : function(response) {
				var result = this.processResponse(response), form = this.form;
				if (result.success == null && response.status == 200) {
					result.success = true;
					result.data = result;
				}
				if (result === true || !result.success || !result.data) {
					this.failureType = Ext.form.action.Action.LOAD_FAILURE;
					form.afterAction(this, false);
					return;
				}
				form.clearInvalid();
				form.setValues(result.data);
				form.afterAction(this, true);
			},

			handleResponse : function(response) {
				var reader = this.form.reader, rs, data;
				if (reader) {
					rs = reader.read(response);
					data = rs.records && rs.records[0]
							? rs.records[0].data
							: null;
					return {
						success : rs.success,
						data : data
					};
				}
				return Ext.decode(response.responseText);
			}
		});
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
		// millisecond
	};

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
						- RegExp.$1.length));
	}

	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
							? o[k]
							: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

Ext.data2ArrayParams = function(store, field, prefix) {
	var data = store.data;

	var result = '{';

	for (var index = 0; index < data.length; index++) {
		var record = data.get(index).data;
		var value = record[field];
		result = result + '"' + prefix + '[' + index + ']":' + '"' + value
				+ '",';
	}
	result = result + '}';

	return eval("(" + result + ")");
};

// 将行转化成FORM参数
Ext.data2FormParams = function(store, prefix) {
	var fields = store.model.getFields();
	var data = store.data;

	var result = '{';

	for (var index = 0; index < data.length; index++) {
		var record = data.get(index).data;

		for (var i = 0; i < fields.length; i++) {
			var value = record[fields[i].name];
			if (value instanceof Date) {
				result = result + '"' + prefix + '[' + index + '].'
						+ fields[i].name + '":' + '"'
						+ value.format('yyyy/MM/dd') + '",';
			} else {
				result = result + '"' + prefix + '[' + index + '].'
						+ fields[i].name + '":' + '"' + value + '",';
			}
		}
	}
	result = result + '}';

	return eval("(" + result + ")");
};

Ext.selection2FormParams = function(selectedRecords, prefix) {
	var fields = selectedRecords[0].fields;

	var result = '{';

	for (var index = 0; index < selectedRecords.length; index++) {
		var record = selectedRecords[index].data;

		for (var i = 0; i < fields.length; i++) {
			var value = record[fields.getAt(i).name];
			if (value instanceof Date) {
				result = result + '"' + prefix + '[' + index + '].'
						+ fields.getAt(i).name + '":' + '"'
						+ value.format('yyyy/MM/dd') + '",';
			} else {
				result = result + '"' + prefix + '[' + index + '].'
						+ fields.getAt(i).name + '":' + '"' + value + '",';
			}
		}
	}
	result = result + '}';

	return eval("(" + result + ")");
};
// 合并Json
Ext.extendsJson = function(destination, source) {
	for (var property in source)
		destination[property] = source[property];
	return destination;
};

Ext.openTab = function(tab, id, option) {
	try{
	
		var panel = Ext.getCmp(id);
		if (panel != null) {
			panel.close();
		}
		panel = Ext.create('Ext.panel.Panel', {
					id : id,
					title : option.title,
					closable : true,
					iconCls : 'icon-user',
					autoScroll : true,
					html : '<iframe width="100%" height="100%" frameborder="0" src="'
							+ option.url + '"></iframe>'
				});
		tab.add(panel);
		tab.setActiveTab(panel);
	
	}catch(e){
		alert(e);
	}
};

//清除form的数据，设置为空，例如：当关闭查询窗口时，调用此方法，自动清空之前输入的查询条件
Ext.clearForm = function(form) {
	var formFields = form.getForm().getFields();
	for (var i = 0; i < formFields.length; i++) {
		var fieldId = formFields.items[i].id;
		var fieldXtype = formFields.items[i].xtype;
		console.info("form的所有的fieldId:" + fieldId + ", xtype: " + fieldXtype);
		Ext.getCmp(fieldId).setValue(null);
		if(fieldXtype=='combo'){
			Ext.getCmp(fieldId).setValue("");
		}
		
	}
};
//form中必输字段提示加*号
Ext.markFormRequiredField = function(form) {
	var formFields = form.getForm().getFields();
	for (var i = 0; i < formFields.length; i++) {
		var allowBlank = formFields.items[i].allowBlank;
		var fieldId = formFields.items[i].id;
		var fieldLabel = formFields.items[i].fieldLabel;
		if(allowBlank == false){
			Ext.DomQuery.selectNode('label[id='+fieldId+'-labelEl]').innerHTML = "<font style='color:red'>*</font>"+fieldLabel;
		}
	}
};

Ext.markRequiredField = function(itemId) {
		var item = Ext.getCmp(itemId);
		var allowBlank = item.allowBlank;
		var fieldLabel = item.fieldLabel;
		if(allowBlank == false){
			Ext.DomQuery.selectNode('label[id='+itemId+'-labelEl]').innerHTML = "<font style='color:red'>*</font>"+fieldLabel;
		} else {
			Ext.DomQuery.selectNode('label[id='+itemId+'-labelEl]').innerHTML = fieldLabel;
		}
};


// 设置指定form的所有field为不可编辑状态，进入view查看模式
Ext.disableAllFormFields = function(form) {
	var formFields = form.getForm().getFields();
	for (var i = 0; i < formFields.length; i++) {
		var fieldId = formFields.items[i].id;
		// console.info("form的所有的fieldId:" + fieldId);
		if(Ext.getCmp(fieldId) != null){
			Ext.getCmp(fieldId).setReadOnly(true);
		}
	}
};

// 设置指定grid的所有column为不可编辑状态，进入view查看模式
Ext.disableAllGridColumns = function(grid) {
	grid.on('beforeedit', function() {
		return false;
	});
};

// 重新加载当前页面
Ext.reloadPage = function() {
	window.location.reload();
};

// 重新加载指定页面
Ext.loadPage = function(url) {
	window.location.assign(url);
};

// 给combo增加一个监听事件，当使用BackSpace或Delete键时，可清空field;
// 参数为combo field的id，参数可传递多个，不限制个数
Ext.clearComboListener = function() {
	var length = arguments.length;
	if (length > 0) {
		for (var i = 0; i < length; i++) {
			var comboField = arguments[i];
			Ext.getCmp(comboField).on(
				'specialkey', function(field, e) {
					if (e.getKey() == 8 || e.getKey() == 46) {
						field.clearValue();
						field.value = '';
					}
				}
			);
		}
		Ext.getCmp()
	}
}

Ext.validateGrid = function(grid){
	var store = grid.store;
	
	var getColumnIndexes = function (grid) {
        var self = this;
        var columnIndexes = [];
        var getIndex = function(column) {
            if (Ext.isDefined(column.getEditor())) {
                columnIndexes.push(column.dataIndex);
            } else {
                columnIndexes.push(undefined);
            }
        }
        
        if (grid) {
        
            Ext.each(grid.columns, function(column) {
                // # only validate column with editor - with support to grouped headers
                if (column.isGroupHeader) {
                    Ext.each(column.items.items, function(subcolumn) {
                        getIndex(subcolumn);
                    }); 
                } else {
                    getIndex(column);
                }
            });        
            return columnIndexes;
        }
    };
    
    var validateRow = function (grid,columnIndexes, record, y) {
        var me, view, errors;

        me = grid;
        view = me.getView();
        
        me.errorMsgs = '';
        
        errors = record.validate();
        if (errors.isValid()) {
            return true;
        }
        
        Ext.each(columnIndexes, function (columnIndex, x) {
            var cellErrors, cell, messages;

            cellErrors = errors.getByField(columnIndex);
            if (!Ext.isEmpty(cellErrors)) {
                cell = view.getCellByPosition({row: y, column: x});
                messages = [];
                Ext.each(cellErrors, function (cellError) {
                    messages.push(cellError.message);
                });
                me.errorMsgs += "行"+(y+1)+":"+me.columns[x].text + messages +'</br>';
                
                cell.addCls("x-form-invalid-field");
                // set error tooltip attribute
                cell.set({'data-errorqtip': Ext.String.format('<ul><li class="last">{0}</li></ul>', messages.join('<br/>'))});
            }
        });

        return false;
    };
    
    var isValid, error, columnIndexes;

    isValid = true;
    error = undefined;
    columnIndexes = getColumnIndexes(grid);

    Ext.each(grid.getView().getNodes(), function (row, y) {
        var record = grid.getView().getRecord(row);

        isValid = (validateRow(grid,columnIndexes, record, y) && isValid);
    });

    return isValid;
	
}
Ext.getBasePath = function(){
	 var local = window.location;  
	 var contextPath = local.pathname.split("/")[1];  
	 var basePath = local.protocol+"//"+local.host+"/"+contextPath;  
	 return basePath;
}
Ext.requestParam = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
Ext.getFormErrorMsg = function(form){
	var fields = form.getForm().getFields();
	
	var errorMsgs = '';
	
	for(var i = 0;i <fields.length;i++){
		var item = fields.getAt(i);
		var label = item.fieldLabel;
		if(!item.isValid()){
			var errorMsg = item.getErrors()[0];
			errorMsgs += label +":"+errorMsg+"<br/>";
		}
	}
	return errorMsgs;
}
// 缓存
var localCache = new Ext.util.HashMap();
Ext.putIntoCache = function(key,value){
	if(window.sessionStorage){ // 支持本地存储
		sessionStorage.setItem(key,JSON.stringify(value));
	} else {
		localCache.put(key,value);
	}
}
Ext.getFromCache = function(key){
	if(window.sessionStorage){ // 支持本地存储
		if(sessionStorage.getItem(key) != "undefined"){
			return JSON.parse(sessionStorage.getItem(key));
		}
	} else {
		return localCache.get(key);
	}
	return null;
}
