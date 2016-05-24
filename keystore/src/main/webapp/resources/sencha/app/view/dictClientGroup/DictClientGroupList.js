Ext.define('KeyStore.view.dictClientGroup.DictClientGroupList', {
    extend : 'Ext.panel.Panel',
    xtype : 'dictClientGroupList',
    requires : [
            'KeyStore.view.dictClientGroup.DictClientGroupListController',
            'KeyStore.view.dictClientGroup.DictClientGroupListModel',
            'Ext.tree.Panel', 'Ext.panel.Panel', 'Ext.data.TreeStore' ],
    controller : 'dictClientGroupList',
    viewModel : {
        type : 'dictClientGroupList'
    },
    layout : 'fit',
    items : [ {
        rootVisible : false,
        reference : 'tree',
        xtype : 'treepanel',
        bind : {
            store : '{dictClientGroupListStore}'
        },
        autoLoad : true,
        tbar : [ {
            text : 'Добавить',
            handler : 'onAddClick'
        }, {
            text : 'Изменить',
            handler : 'onEditClick'
        } ],
        listeners : {
            render : 'onTreeRender',
            selectionchange : 'onTreeSelectionChange'
        },
        bbar : [ {
            xtype : 'label',
            reference : 'infoLabel'
        }, {
            xtype : 'tbseparator'
        }, {
            xtype : 'label',
            text : 'Показано '
        }, {
            xtype : 'label',
            reference : 'statusShowRecords',
            text : '0'
        }, {
            xtype : 'label',
            text : ' из'
        }, {
            xtype : 'label',
            reference : 'statusTotalRecords',
            text : '0'
        }, {
            xtype : 'label',
            text : ', выбрано: '
        }, {
            xtype : 'label',
            reference : 'statusSelected',
            text : '0'
        }, {
            xtype : 'tbseparator'
        }, {
            xtype : 'label',
            text : '    Фильтрация: '
        }, {
            xtype : 'label',
            reference : 'statusFilter',
            text : ' нет '
        }]
    } ],
    listeners : {
        render : 'onRender'
    }
});