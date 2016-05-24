Ext.define('KeyStore.view.client.EmailNotificationController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.clientEmailNotification',
    
    onSendClick : function (self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var title  = this.lookupReference('title').getValue();
        var body = this.lookupReference ('body').getValue();
        
        infoLabel.setText('Отправка сообщений...');
        globalUtils.ajaxRequest({
            url : '/client/sendNotification',
            jsonData : {
                ids : controller.getViewModel().getData().idArray,
                title : title,
                body : body
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Внимание',
                            msg : 'Сообщения успешно отправлены.',
                            buttons : Ext.MessageBox.OK,
                            fn : function() {
                                controller.getView().destroy();
                            }
                        });
                    } else {
                        infoLabel.setText(answer.message);
                    }
                } catch (exception) {
                    if (console) console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка отправки сообщений.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка отправки сообщений.');
                    break;
                }
            }
        });
    },
    
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    
    loadData : function (obj) {
        this.getViewModel().getData().idArray = obj.idArray;
    }
});