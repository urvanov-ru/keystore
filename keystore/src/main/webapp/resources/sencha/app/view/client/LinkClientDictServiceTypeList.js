Ext.define('KeyStore.view.client.LinkClientDictServiceTypeList', {
    extend : 'Ext.window.Window',
    xtype : 'linkClientDictServiceTypeList',
    requires : [ 'KeyStore.view.client.LinkClientDictServiceTypeListController',
            'KeyStore.view.client.LinkClientDictServiceTypeListModel' ],
    controller : 'linkClientDictServiceTypeList',
    viewModel : {
        type : 'linkClientDictServiceTypeList'
    },
    autoShow : true,
    width : 600,
    height : 400,
    title : 'Список пакетов услуг, предоставленных клиенту',
    layout : 'fit',
    items : [{
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{linkClientDictServiceTypeListStore}'
        },
        selModel : {
            mode : 'MULTI'
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
        listeners : {
            render : 'onGridRender',
            selectionchange : 'onGridSelectionChange'
        },
        tbar : [{
            text : 'Отобрать пакет услуг у клиента',
            handler : 'onRemoveClick',
            reference : 'removeButton',
            disabled : true
        }],
        bbar : [ {
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }]
});