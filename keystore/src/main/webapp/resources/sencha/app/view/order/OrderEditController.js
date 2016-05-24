Ext.define('KeyStore.view.order.OrderEditController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.orderEdit',
    id : 'orderEdit',
    onSaveClick : function(self) {
        var controller = this;
        var form = controller.lookupReference('form');
        var infoLabel = controller.lookupReference('infoLabel');
        var dictServiceTypeId = controller.lookupReference('dictServiceTypeId')
                .getValue();
        if (!form.getForm().isValid() || !dictServiceTypeId) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var id = controller.lookupReference('id').getValue();
        
        var keyActivationMode = controller.lookupReference('keyActivationMode')
                .getValue();
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/order/save',
            jsonData : {
                id : id,
                dictServiceTypeId : dictServiceTypeId,
                keyActivationMode : keyActivationMode
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
                    infoLabel.setText('Ошибка сохранения заказа.');
                    break;
                }
            }
        });
    },

    onCancelClick : function(self) {
        this.getView().destroy();
    },

    onDictServiceTypeSelectClick : function(self) {
        var dictServiceTypeId  = this.lookupReference('dictServiceTypeId').getValue();
        var controller = this;
        var widget = Ext.widget('dictServiceTypeSelect');
        widget.getController().setOkListener(function(arg0) {
            var id = arg0.selection[0].id;
            var name = arg0.selection[0].name;
            controller.lookupReference('dictServiceTypeId').setValue(id);
            controller.lookupReference('dictServiceTypeName').setText(name);
        });
        widget.getController().setSelectedIds([dictServiceTypeId]);
    }
});