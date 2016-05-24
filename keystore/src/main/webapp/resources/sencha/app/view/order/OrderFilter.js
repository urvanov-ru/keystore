Ext.define('KeyStore.view.order.OrderFilter', {
    extend : 'Ext.window.Window',
    xtype : 'orderFilter',
    requires : [ 'KeyStore.view.order.OrderFilterController',
            'Ext.window.Window', 'KeyStore.view.order.OrderFilterModel',
            'Ext.form.field.Date' ],
    controller : 'orderFilter',
    viewModel : {
        type : 'orderFilter'
    },
    autoShow : true,
    width : 500,
    height : 300,
    title : 'Фильтр заказов',
    layout : 'fit',
    modal : true,
    resizable : false,
    items : [ {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        width : 500,
        height : 300,
        items : [ {
            xtype : 'panel',
            layout : 'form',
            width : 490,
            items : [ {
                xtype : 'textfield',
                fieldLabel : 'Название клиента',
                reference : 'clientName'
            }, {
                xtype : 'combo',
                fieldLabel : 'Статус',
                mode : 'local',
                bind : {
                    store : '{orderStatusesStore}'
                },
                triggerAction : 'all',
                displayField : 'name',
                valueField : 'code',
                reference : 'status'
            } ]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 490,
            items : [ {
                xtype : 'datefield',
                fieldLabel : 'Дата заказа с',
                reference : 'createdAtBegin',
                format : 'd.m.Y'
            }, {
                xtype : 'datefield',
                fieldLabel : 'по',
                labelWidth : 30,
                reference : 'createdAtEnd',
                format : 'd.m.Y'
            } ]
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        } ]
    } ],
    buttons : [ {
        text : 'Применить',
        handler : 'onAcceptClick'
    }, {
        text : 'Сбросить',
        handler : 'onResetClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    } ]
});