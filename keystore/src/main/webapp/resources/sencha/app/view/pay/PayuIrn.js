Ext.define('KeyStore.view.pay.PayuIrn', {
    extend : 'Ext.window.Window',
    xtype : 'payuIrn',
    requires : [ 'KeyStore.view.pay.PayuIrnController',
            'KeyStore.view.pay.PayuIrnModel' ],
    controller : 'payuIrn',
    viewModel : {
        type : 'payuIrn'
    },
    autoShow : true,
    width : 600,
    height : 400,
    title : 'Возврат',
    layout : 'fit',
    modal : true,
    items : [ {
        xtype : 'form',
        layout : 'vbox',
        width : '100%',
        height : 400,
        reference : 'form',
        items : [ {
            xtype : 'hidden',
            reference : 'merchant',
            name : 'MERCHANT'
        }, {
            xtype : 'hidden',
            reference : 'irnUrl'
        },{
            xtype : 'hidden',
            reference : 'orderRef',
            name : 'ORDER_REF'
        }, {
            xtype : 'hidden',
            reference : 'orderAmount',
            name : 'ORDER_AMOUNT'
        }, {
            xtype : 'hidden',
            reference : 'orderCurrency',
            name : 'ORDER_CURRENCY'
        }, {
            xtype : 'hidden',
            reference : 'irnDate',
            name : 'IRN_DATE'
        }, {
            xtype : 'hidden',
            reference : 'orderHash',
            name : 'ORDER_HASH'
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        } ]
    } ],
    buttons : [{
        text : 'Возврат средств',
        handler : 'onRefundClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});