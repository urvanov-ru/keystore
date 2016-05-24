Ext.define('KeyStore.view.pay.Onpay', {
    extend : 'Ext.window.Window',
    xtype : 'onpay',
    requires : [ 'KeyStore.view.pay.OnpayController',
            'KeyStore.view.pay.OnpayModel', 'Ext.form.field.Display' ],
    controller : 'onpay',
    viewModel : {
        type : 'onpay'
    },
    width : 600,
    height :480,
    autoShow : true,
    title : 'Оплата',
    layout : 'fit',
    modal : true,
    items : [{
        xtype : 'form',
        layout : 'vbox',
        reference : 'form',
        standardSubmit : true,
        width : '100%',
        height : 400,
        items : [ {
            xtype : 'hidden',
            reference : 'onpayUrl'
        },{
            xtype : 'hidden',
            name : 'pay_mode',
            reference : 'payMode',
            value : 'fix'
        }, {
            xtype : 'hidden',
            name : 'price',
            reference : 'price'
        }, {
            xtype : 'hidden',
            name : 'ticker',
            reference : 'ticker',
            value : 'RUR'
        }, {
            xtype : 'hidden',
            name : 'pay_for',
            reference : 'payFor'
        }, {
            xtype : 'hidden',
            name : 'md5',
            reference : 'md5'
        },{
            xtype : 'hidden',
            name : 'convert',
            reference : 'convert',
            value : 'yes'
        },{
            xtype : 'hidden',
            name : 'url_success',
            reference : 'urlSuccess'
        }, {
            xtype : 'hidden',
            name : 'url_success_enc',
            reference : 'urlSuccessEnc'
        }, {
            xtype : 'hidden',
            name : 'url_fail',
            reference : 'urlFail'
        }, {
            xtype : 'hidden',
            name : 'url_fail_enc',
            reference : 'urlFailEnc'
        }, {
            xtype : 'hidden',
            name : 'user_email',
            reference : 'userEmail'
        }, {
            xtype : 'hidden',
            name : 'user_phone',
            reference : 'userPhone'
        }, {
            xtype : 'hidden',
            name : 'note',
            reference : 'note'
        }, {
            xtype : 'hidden',
            name : 'ln',
            reference : 'ln'
        }, {
            xtype : 'hidden',
            name : 'f',
            value : '7',
            reference : 'f'
        },  {
            xtype : 'hidden',
            name : 'price_final',
            value : 'false',
            reference : 'priceFinal'
        }, {
            xtype : 'panel',
            layout : 'form',
            height : 100,
            width : '100%',
            items : [{
                xtype : 'displayfield',
                reference : 'dictServiceTypeName',
                fieldLabel : 'Пакет услуг'
            }, {
                xtype : 'displayfield',
                reference : 'orderCreatedAt',
                fieldLabel : 'Дата создание заказа'
            }, {
                xtype : 'displayfield',
                reference : 'amount',
                fieldLabel : 'Сумма заказа'
            }
            ]}
        ,{
            xtype : 'label',
            text : 'При нажатии "Оплатить" вы будете перенаправлены на страницу оплаты платёжной системы.'
        },{
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