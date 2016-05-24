Ext.define('KeyStore.view.emailTemplate.EmailTemplateList', {
    extend : 'Ext.panel.Panel',
    xtype : 'emailTemplateList',
    requires : [ 'KeyStore.view.emailTemplate.EmailTemplateListController',
            'KeyStore.view.emailTemplate.EmailTemplateListModel',
            'Ext.grid.Panel', 'Ext.button.Button' ],
    controller : 'emailTemplateList',
    viewModel : {
        type : 'emailTemplateList'
    },
    layout : 'fit',
    
    items : [ {
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{emailTemplateListStore}'
        },
        features : [ {
            ftype : 'grouping',
            groupHeaderTpl : 'Группировка: {name} ({rows.length})',
            startCollapsed : true
        } ],
        columns : [ {
            text : 'Тип события',
            dataIndex : 'codeName',
            sortable : true,
            width : 400
        }],
        tbar : [{
            text : 'Изменить',
            handler : 'onEditClick',
            reference : 'editButton',
            disabled : true
        }],
        listeners : {
            render : 'onGridRender',
            selectionchange : 'onGridSelectionChange'
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
        } ]
    } ]
});