Ext.define('KeyStore.view.pay.OnpayController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.onpay',

    onPayClick : function (self) {
        if (console) console.log('onPay click.');
        var controller = this;
        var form = this.lookupReference('form');
        var url = controller.lookupReference('onpayUrl').getValue();
        if (console) console.log('url='+url);
        url = url + "?pay_mode="+controller.lookupReference('payMode').getValue() + 
                '&price='+controller.lookupReference('price').getValue() + 
                '&ticker='+controller.lookupReference('ticker').getValue() + 
                '&pay_for=' + controller.lookupReference('payFor').getValue() + 
                '&md5='+controller.lookupReference('md5').getValue() + 
                '&convert='+controller.lookupReference('convert').getValue() + 
                '&url_success=' + controller.lookupReference('urlSuccess').getValue() + 
                '&url_fail=' + controller.lookupReference('urlFail').getValue() + 
                '&url_fail_enc=' + controller.lookupReference('urlFailEnc').getValue() + 
                '&user_email=' + controller.lookupReference('userEmail').getValue() + 
                '&user_phone=' + controller.lookupReference('userPhone').getValue() +
                '&note=' + controller.lookupReference('note').getValue() + 
                '&ln=' + controller.lookupReference('ln').getValue() + 
                '&f='+controller.lookupReference('f').getValue() + 
                '&price_final='+controller.lookupReference('priceFinal').getValue();
        if (console) console.log('url='+url);
        window.location.href=url;
    },
    
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    
    loadData : function (obj) {
        var orderId = obj.orderId;
        var payMethod = obj.payMethod;
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/onpay/loadOnPay',
            params : {
                orderId : orderId
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('onpayUrl').setValue(answer.info.onpayUrl);
                        controller.lookupReference('price').setValue(answer.info.price);
                        controller.lookupReference('payFor').setValue(answer.info.payFor);
                        controller.lookupReference('md5').setValue(answer.info.md5);
                        controller.lookupReference('urlSuccess').setValue(answer.info.urlSuccess);
                        controller.lookupReference('urlSuccessEnc').setValue(answer.info.urlSuccessEnc);
                        controller.lookupReference('urlFail').setValue(answer.info.urlFail);
                        controller.lookupReference('urlFailEnc').setValue(answer.info.urlFailEnc);
                        controller.lookupReference('userEmail').setValue(answer.info.userEmail);
                        controller.lookupReference('userPhone').setValue(answer.info.userPhone);
                        controller.lookupReference('note').setValue(answer.info.note);
                        controller.lookupReference('ln').setValue(answer.info.ln);
                        controller.lookupReference('dictServiceTypeName').setValue(answer.info.dictServiceTypeName);
                        controller.lookupReference('orderCreatedAt').setValue(Ext.Date.format(new Date(answer.info.orderCreatedAt), 'd.m.Y H:i:s'));
                        controller.lookupReference('amount').setValue(answer.info.amount);
   
                    } else {
                        if (console) console.log('Ошибка загрузки данных.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки данных',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK,
                            fn : function () {
                                controller.getView().destroy();
                            },
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                } catch (exception) {
                    if (console) console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка загрузки данных.');
                switch (xhr.status) {
                case 403:
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Доступ запрещён.',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка загрузки данных.',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                }
            }
        });
    }
});