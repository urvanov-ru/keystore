Ext.define('KeyStore.view.payment.PaymentCorrect', {
    extend : 'Ext.window.Window',
    xtype : 'paymentCorrect',
    requires : [ 'KeyStore.view.payment.PaymentCorrectController',
            'KeyStore.view.payment.PaymentCorrectModel',
            'Ext.form.field.Number', 'Ext.grid.Panel', 'Ext.layout.container.Form',
            'Ext.form.Panel', 'Ext.form.field.Text', 'Ext.form.field.Hidden',
            'Ext.form.Label'],
    controller : 'paymentCorrect',
    viewModel : {
        type : 'paymentCorrect'
    },
    width : 500,
    height : 400,
    layout : 'fit',
    autoShow : true,
    modal : true,
    title : 'Корректировка',
    items : [{
        xtype : 'form',
        layout : 'vbox',
        reference : 'form',
        items : [{
            xtype : 'label',
            text : 'Введите изменение сумм:'
        },{
            xtype : 'panel',
            layout : 'form',
            width : '100%',
            height : 100,
            items : [{
                xtype : 'hidden',
                reference : 'parentPaymentId'
            }, {
                xtype : 'numberfield',
                reference : 'amountWithoutCommission',
                fieldLabel : 'Корректировка суммы без коммиссии',
                value : 0.0,
                allowBlank : false
            }, {
                xtype : 'numberfield',
                reference : 'amountOfCommission',
                fieldLabel : 'Корректировка коммиссии',
                value : 0.0,
                allowBlank : false
            }, {
                xtype : 'textfield',
                reference : 'comment',
                fieldLabel : 'Комментарий',
                allowBlank : false
            }]
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }],
    buttons : [{
        xtype : 'button',
        text : 'Сохранить',
        handler : 'onSaveClick'
    }, {
        xtype : 'button',
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});