Ext.define('KeyStore.view.pay.PayuIrnController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.payuIrn',
    
    onRefundClick : function (self) {
        var controller = this;
        var form = this.lookupReference('form');
        form.getForm().submit({
            url: controller.lookupReference('irnUrl').getValue(),
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
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/payu/loadIrn',
            params : {
                orderId : orderId
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('merchant').setValue(answer.info.merchant);
                        controller.lookupReference('orderRef').setValue(answer.info.orderRef);
                        controller.lookupReference('orderAmount').setValue(answer.info.orderAmount);
                        controller.lookupReference('orderCurrency').setValue(answer.info.orderCurrency);
                        controller.lookupReference('irnDate').setValue(answer.info.irnDate);
                        controller.lookupReference('orderHash').setValue(answer.info.orderHash);
                        controller.lookupReference('irnUrl').setValue(answer.info.irnUrl);
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