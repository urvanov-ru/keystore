Ext.define('KeyStore.view.dictServiceType.DictServiceTypeList', {
    extend : 'Ext.panel.Panel',
    xtype : 'dictServiceTypeList',
    requires : ['Ext.grid.Panel',
        'KeyStore.view.dictServiceType.DictServiceTypeListController',
        'KeyStore.view.dictServiceType.DictServiceTypeListModel',
        'Ext.panel.Panel',
        'Ext.grid.feature.Grouping' ],
    layout : 'fit',
    viewModel : {
        type : 'dictServiceTypeList'
    },
    controller : 'dictServiceTypeList',
    items : [{
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{dictServiceTypeListStore}'
        },
        columns : [{
            dataIndex : 'name',
            text : 'Наименование',
            sortable : true,
            width : 150
        }, {
            dataIndex : 'description',
            text : 'Описание',
            sortable : true,
            width : 300
        }, {
            dataIndex : 'statusName',
            text : 'Статус',
            sortable : true,
            width : 100
        }, {
            dataIndex : 'amount',
            text : 'Стоимость',
            sortable : true,
            width : 100
        }, {
            dataIndex : 'amount30Days',
            text : 'Стоимость 30 дней',
            sortable : true,
            width : 100
        }],
        tbar : [{
            text : 'Добавить',
            xtype : 'button',
            handler : 'onAddClick',
            reference : 'addButton'
        }, {
            text : 'Изменить',
            xtype : 'button',
            handler : 'onEditClick',
            reference : 'editButton',
            disabled : true
        }, {
            xtype : 'button',
            text : 'Базовый ключ',
            handler : 'onBaseKeyClick',
            reference : 'baseKeyButton',
            disabled : true
        } ,{
            xtype : 'button',
            text : 'Предоставить клиенту',
            reference : 'giveToClientButton',
            handler : 'onGiveToClientClicked',
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
    }]
});