Ext.Loader.setConfig({
	enabled : true
});

Ext.Loader.setPath({
	'Ext.ux' : 'ext4/ux',
	'Ext.app' : 'ext4/app'
});

Ext.onReady(function() {

	var addForm = Ext.create('Ext.form.Panel', {
		width : 800,
		height : 200,
		title : "添加",
		layout : {
			type : 'hbox',
			column : 2
		},
		id : "AddForm",
		renderTo : Ext.getBody(),
		defaultType : 'textfield',
		fieldDefaults : {
			labelWidth : 100,
			margin : 5
		},
		items : [ {
			xtype : "textfield",
			name : "myName",
			id : "myName",
			fieldLabel : 'myName'
		}, {
			xtype : "textfield",
			name : "myOtherAttribtues",
			id : "myOtherAttribtues",
			fieldLabel : 'myOtherAttribtues'
		} ],
		buttons : [ {
			xtype : "button",
			text : "保存",
			handler:save
		} ]
	});

	function save() {
		addForm.getForm().submit(
				{
					url : Ext.getBasePath() + '/test/save',
					dataType : 'json',
					success : function(response, action) {
						if (action.result.status == 'S') {

							Ext.MessageBox.alert('提示', action.result.msg);

						} else {
							Ext.getBody().unmask();
							Ext.MessageBox.alert('失败', '保存失败：'
									+ action.result.msg);
						}
					},
					failure : function(response) {
						Ext.getBody().unmask();
						Ext.MessageBox.alert('失败', '请求超时或网络故障,错误编号：'
								+ response.responseText);
					}
				});
	}

	Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [ addForm ]
	})

});