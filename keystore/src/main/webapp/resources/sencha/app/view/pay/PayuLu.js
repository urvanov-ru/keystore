Ext.define('KeyStore.view.pay.PayuLu', {
    extend : 'Ext.window.Window',
    xtype : 'payuLu',
    requires : [ 'KeyStore.view.pay.PayuLuController',
            'KeyStore.view.pay.PayuLuModel' ],
    controller : 'payuLu',
    viewModel : {
        type : 'payuLu'
    },
    width : 600,
    height :480,
    autoShow : true,
    title : 'Оплата',
    layout : 'fit',
    items : [{
        xtype : 'form',
        layout : 'vbox',
        reference : 'form',
        standardSubmit : true,
        width : '100%',
        height : 400,
        items : [{
            xtype : 'hidden',
            name : 'MERCHANT',
            reference : 'merchant'
        }, {
            xtype  :'hidden',
            reference : 'luUrl'
        }, {
            xtype : 'hidden',
            name : 'ORDER_REF',
            reference : 'orderRef'
        }, {
            xtype : 'hidden',
            name : 'ORDER_DATE',
            reference : 'orderDate'
        }, {
            xtype : 'hidden',
            name : 'ORDER_PNAME[]',
            reference : 'orderPName'
        }, {
            xtype : 'hidden',
            name : 'ORDER_PCODE[]',
            reference : 'orderPCode'
        }, {
            xtype : 'hidden',
            name : 'ORDER_PRICE[]',
            reference : 'orderPrice'
        }, {
            xtype : 'hidden',
            name : 'ORDER_QTY[]',
            value : 1,
            reference : 'orderQty'
        }, {
            xtype : 'hidden',
            name : 'ORDER_VAT[]',
            value : 19,
            reference : 'orderVat'
        }, {
            xtype : 'hidden',
            name : 'ORDER_SHIPPING',
            value : 0,
            reference : 'orderShipping'
        }, {
            xtype : 'hidden',
            name : 'PRICES_CURRENCY',
            value : 'RUB',
            reference : 'pricesCurrency'
        }, {
            xtype : 'hidden',
            name : 'PAY_METHOD',
            reference : 'payMethod'
        }, {
            xtype : 'hidden',
            name : 'ORDER_HASH',
            reference : 'orderHash'
        }, {
            xtype : 'hidden',
            name : 'TESTORDER',
            value : 'TRUE',
            reference : 'testOrder'
        }, {
            xtype : 'hidden',
            name : 'DEBUG',
            value : '1',
            reference : 'debug'
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }],
    buttons : [{
        text : 'Оплатить',
        handler : 'onPayClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});