Ext.define('KeyStore.view.pay.PayuLuController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.payuLu',
    
    onPayClick : function (self) {
        var controller = this;
        var form = this.lookupReference('form');
        form.getForm().submit({
            url: controller.lookupReference('luUrl').getValue(),
            success: function(form, action) {
               if (console) console.log('submit to payU success.');
            },
            failure: function(form, action) {
                switch (action.failureType) {
                    case Ext.form.action.Action.CLIENT_INVALID:
                        Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                        break;
                    case Ext.form.action.Action.CONNECT_FAILURE:
                        Ext.Msg.alert('Failure', 'Ajax communication failed');
                        break;
                    case Ext.form.action.Action.SERVER_INVALID:
                       Ext.Msg.alert('Failure', action.result.msg);
               }
            }
        });
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
            url : '/payu/loadLu',
            params : {
                orderId : orderId,
                payMethod : payMethod
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('merchant').setValue(answer.info.merchant);
                        controller.lookupReference('orderRef').setValue(orderId);
                        controller.lookupReference('orderDate').setValue(answer.info.orderDate);
                        controller.lookupReference('orderPName').setValue(answer.info.orderPName);
                        controller.lookupReference('orderPCode').setValue(answer.info.orderPCode);
                        controller.lookupReference('orderPrice').setValue(answer.info.orderPrice);
                        controller.lookupReference('payMethod').setValue(answer.info.payMethod);
                        controller.lookupReference('orderQty').setValue(answer.info.orderQty);
                        controller.lookupReference('orderVat').setValue(answer.info.orderVat);
                        controller.lookupReference('orderShipping').setValue(answer.info.orderShipping);
                        controller.lookupReference('pricesCurrency').setValue(answer.info.pricesCurrency);
                        controller.lookupReference('orderHash').setValue(answer.info.orderHash);
                        controller.lookupReference('payMethod').setValue(answer.info.payMethod);
                        controller.lookupReference('luUrl').setValue(answer.info.luUrl);
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
    },
    
    updateOrderHash : function (password) {
        var controller = this;
        var merchant = controller.lookupReference('merchant').getValue();
        var orderRef = controller.lookupReference('orderRef').getValue();
        var orderDate = controller.lookupReference('orderDate').getValue();
        var orderPName = controller.lookupReference('orderPName').getValue();
        var orderPCode = controller.lookupReference('orderPCode').getValue();
        var orderPrice = controller.lookupReference('orderPrice').getValue();
        var orderQty = controller.lookupReference('orderQty').getValue();
        var orderVat = controller.lookupReference('orderVat').getValue();
        var orderShipping = controller.lookupReference('orderShipping').getValue();
        var pricesCurrency = controller.lookupReference('orderRef').getValue();
        var payMethod = controller.lookupReference('payMethod').getValue();
        var baseString = merchant.length
                + merchant
                +orderRef.length + orderRef
                + orderDate.length + orderDate
                + orderPName.length + orderPName +
                orderPCode.length + orderPCode
                + orderPrice.length + orderPrice
                + orderQty.length + orderQty
                + orderVat.length + orderVat
                + orderShipping.length + orderShipping
                + pricesCurrency.length + pricesCurrency
                + payMethod.length + payMethod;
        var orderHash = CryptoJS.HmacMD5(baseString, password);
        controller.lookupReference('orderHash').setValue(orderHash);
    }
});