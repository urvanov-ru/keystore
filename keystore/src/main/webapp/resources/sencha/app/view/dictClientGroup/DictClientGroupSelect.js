Ext.define('KeyStore.view.dictClientGroup.DictClientGroupSelect', {
    extend : 'Ext.window.Window',
    xtype : 'dictClientGroupSelect',
    requires : [
            'KeyStore.view.dictClientGroup.DictClientGroupSelectController',
            'KeyStore.view.dictClientGroup.DictClientGroupSelectModel',
            'Ext.tree.Panel', 'Ext.panel.Panel', 'Ext.data.TreeStore' ],
    controller : 'dictClientGroupSelect',
    viewModel : {
        type : 'dictClientGroupSelect'
    },
    autoShow : true,
    width : 600,
    height : 400,
    modal : true,
    title : 'Выберите группу клиентов',
    layout : 'fit',
    items : [ {
        reference : 'tree',
        rootVisible : false,
        xtype : 'treepanel',
        bind : {
            store : '{dictClientGroupSelectStore}'
        },
        autoLoad : true,
        listeners : {
            render : 'onTreeRender'
        },
        bbar : [{
            xtype : 'label',
            reference : 'infoLabel'
        }]
    } ],
    buttons : [ {
        text : 'OK',
        handler : 'onOkClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    } ]
});