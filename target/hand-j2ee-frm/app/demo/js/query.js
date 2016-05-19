Ext.Loader.setConfig({
	enabled : true
});

Ext.Loader.setPath({
	'Ext.ux' : 'ext4/ux',
	'Ext.app' : 'ext4/app'
});

Ext.onReady(function() {
	
	Ext.define('cuxTestTblModel', {
		extend : 'Ext.data.Model',
		fields : [ {
			name : 'myName', // 
			type : 'string'
		}, {
			name : 'myOtherAttribtues', // 
			type : 'string'
		}]
	});

var cuxTestTblStore = Ext.create('Ext.data.Store', {
		model : 'cuxTestTblModel',
		proxy : {
			type : 'ajax',
			url:Ext.getBasePath() + '/test/getAll',
			reader : {
				type : 'json',
				rootProperty : ''
			}
		},
		autoLoad : false
	});

var cuxTestTblColumns =[{header:'myName',width:100,dataIndex: 'myName'},
                         					 {header:'myOtherAttribtues',width:100,dataIndex: 'myOtherAttribtues'}];

var  cuxTestTblHisGrid = Ext.create('Ext.grid.Panel', {
	width : 200,
	height : 200,
	id : "CuxTestTblHisGrid",
	store : cuxTestTblStore,
	columns : cuxTestTblColumns,
	region : 'center',
	renderTo : Ext.getBody(),
	tbar : Ext.create('Ext.Toolbar', {
		items : [ {
			text : '查询',
			id : 'QueryBtn',
			handler : function() {
				cuxTestTblStore.load();
			}
		}]
	})
});

var loadQueryMarsk = new Ext.LoadMask(cuxTestTblHisGrid, {
	msg : '数据加载中，请稍候......',
	store : cuxTestTblStore
});

Ext.create('Ext.container.Viewport', {
	layout : 'border',
	items : [ cuxTestTblHisGrid ]
})

});