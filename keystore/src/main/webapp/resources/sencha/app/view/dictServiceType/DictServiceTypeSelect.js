Ext.define('KeyStore.view.dictServiceType.DictServiceTypeSelect', {
    extend : 'Ext.window.Window',
    xtype : 'dictServiceTypeSelect',
    requires : [ 'KeyStore.view.dictServiceType.DictServiceTypeSelectController',
            'KeyStore.view.dictServiceType.DictServiceTypeSelectModel',
            'Ext.window.Window'],
    controller : 'dictServiceTypeSelect',
    viewModel : {
        type : 'dictServiceTypeSelect'
    },
    autoShow : true,
    width : 600,
    height : 400,
    title : 'Выбор пакета услуг',
    layout : 'fit',
    modal : true,
    items : [{
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{dictServiceTypeSelectStore}'
        },
        columns : [
                   {
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
            render : 'onGridRender'
        },
        bbar : [{
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }],
    buttons : [{
        text : 'ОК',
        handler : 'onOkClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});