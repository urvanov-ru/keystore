Ext.define('KeyStore.view.dictAction.DictActionSelect', {
    extend : 'Ext.window.Window',
    xtype : 'dictActionSelect',
    requires : [ 'KeyStore.view.dictAction.DictActionSelectController',
            'KeyStore.view.dictAction.DictActionSelectModel' ],
    controller : 'dictActionSelect',
    viewModel : {
        type : 'dictActionSelect'
    },
    autoShow : true,
    title : 'Выберите акцию',
    width : 600,
    height : 400,
    layout : 'fit',
    modal : true,
    items : [ {
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{dictActionSelectStore}'
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
            render : 'onGridRender'
        },
        bbar : [{
            xtype : 'label',
            reference : 'infoLabel'
        } ]
    }],
    buttons : [{
        text : 'ОК',
        handler : 'onOkClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});