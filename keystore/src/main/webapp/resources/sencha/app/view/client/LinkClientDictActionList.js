Ext.define('KeyStore.view.client.LinkClientDictActionList', {
    extend : 'Ext.window.Window',
    xtype : 'linkClientDictActionList',
    requires : [ 'KeyStore.view.client.LinkClientDictActionListController',
            'KeyStore.view.client.LinkClientDictActionListModel' ],
    controller : 'linkClientDictActionList',
    viewModel : {
        type : 'linkClientDictActionList'
    },
    autoShow : true,
    modal : true,
    width : 600,
    height : 400,
    title : 'Список акций, в который учавтсвует клиент',
    layout : 'fit',
    items : [{
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{linkClientDictActionListStore}'
        },
        selModel : {
            mode : 'MULTI'
        },
        columns : [{
            text : 'Название',
            dataIndex : 'name',
            sortable : true,
            width : 300
        }, {
            text : 'Описание',
            dataIndex : 'description',
            sortable : true,
            width : 300
        }, {
            text : 'Дата начала',
            dataIndex : 'dateBegin',
            width : 100,
            sortable : true,
            format : 'd.m.Y',
            xtype : 'datecolumn'
        }, {
            text : 'Дата окончания',
            dataIndex : 'dateEnd',
            sortable : true,
            width : 100,
            format : 'd.m.Y',
            xtype : 'datecolumn'
        }, {
            text : 'Пакет предоставляемых услуг',
            dataIndex : 'dictServiceTypeName',
            sortable : true,
            width : 300
        }, {
            text : 'Тип акции',
            dataIndex : 'dictActionTypeName',
            sortable : true,
            width : 100
        }, {
            text : 'Для новых клиентов',
            dataIndex : 'forNewClients',
            sortable : true,
            width : 100,
            renderer : function (val) {
                if (val) return 'Да';
                return 'Нет';
            }
        }],
        listeners : {
            render : 'onGridRender',
            selectionchange : 'onGridSelectionChange'
        },
        tbar : [{
            text : 'Убрать акцию',
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