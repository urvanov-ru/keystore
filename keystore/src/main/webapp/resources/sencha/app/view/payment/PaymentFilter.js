Ext.define('KeyStore.view.payment.PaymentFilter', {
    extend : 'Ext.window.Window',
    requires : [ 'KeyStore.view.payment.PaymentFilterController',
            'KeyStore.view.payment.PaymentFilterModel',
            'Ext.window.Window' ],
    xtype : 'paymentFilter',
    controller : 'paymentFilter',
    viewModel : {
        type : 'paymentFilter'
    },
    autoShow : true,
    width : 500,
    height : 300,
    title : 'Фильтр платежей',
    layout : 'vbox',
    modal : true,
    items : [  {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [{
            xtype : 'panel',
            layout : 'form',
            width : 490,
            items : [ {
                xtype : 'textfield',
                fieldLabel : 'ФИО клиента',
                reference : 'clientName'
            }, {
                xtype : 'combo',
                fieldLabel : 'Тип',
                mode : 'local',
                bind : {
                    store : '{paymentTypesStore}'
                },
                triggerAction : 'all',
                displayField : 'name',
                valueField : 'code',
                reference : 'paymentType'
            }, {
                xtype : 'combo',
                fieldLabel : 'Статус',
                mode : 'local',
                triggerAction : 'all',
                displayField : 'name',
                valueField : 'code',
                bind : {
                    store : '{paymentStatusesStore}'
                },
                reference : 'status'
            }, {
                xtype : 'combo',
                fieldLabel : 'Способ',
                mode : 'local',
                triggerAction : 'all',
                displayField : 'name',
                valueField : 'code',
                bind : {
                    store : '{paymentMethodsStore}'
                },
                reference : 'method'
            } ]
        }, {
            xtype : 'panel',
            width : 490,
            layout : 'hbox',
            items : [ {
                xtype : 'datefield',
                fieldLabel : 'Дата платежа с',
                format : 'd.m.Y',
                reference : 'createdAtBegin'
            }, {
                xtype : 'datefield',
                fieldLabel : 'по',
                labelWidth : 30,
                format : 'd.m.Y',
                reference : 'createdAtEnd'
            } ]
        } ]
    }],
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