Ext.define('KeyStore.view.payment.PaymentCorrectController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.paymentCorrect',
    id : 'paymentCorrect',
    onSaveClick : function (self) {
        var controller = this;
        var form = controller.lookupReference('form');
        var infoLabel = controller.lookupReference('infoLabel');

        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var parentPaymentId = controller.lookupReference('parentPaymentId').getValue();
        var amountWithoutCommission = controller.lookupReference('amountWithoutCommission').getValue();
        var amountOfCommission = controller.lookupReference('amountOfCommission').getValue();
        var comment = controller.lookupReference('comment').getValue();

        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/payment/correct',
            jsonData : {
                parentPaymentId : parentPaymentId,
                amountWithoutCommission : amountWithoutCommission,
                amountOfCommission : amountOfCommission,
                comment : comment
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Внимание',
                            msg : 'Сохранение прошло успешно.',
                            buttons : Ext.MessageBox.OK,
                            fn : function() {
                                controller.getView().destroy();
                                controller.fireEvent('save');
                            }
                        });
                    } else {
                        infoLabel.setText(answer.message);
                    }
                } catch (exception) {
                    if (console)
                        console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка сохранения заказа.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения корректирующего платежа.');
                    break;
                }
            }
        });
    },
    
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    
    loadData : function (obj) {
        var parentPaymentId = obj.parentPaymentId;
        var controller = this;
        controller.lookupReference('parentPaymentId').setValue(parentPaymentId);
    }
});