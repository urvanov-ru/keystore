Ext.define('KeyStore.view.dictAction.DictActionStartController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.dictActionStart',
    id : 'dictActionStart',
    
    loadData : function (obj) {
        var controller = this;
        controller.lookupReference('id').setValue(obj.id);
    },
    
    onStartDictActionClick : function(self) {
        var controller = this;
        var id = controller.lookupReference('id').getValue();
        var form = controller.lookupReference('form');
        var infoLabel = controller.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var dateEnd = controller.lookupReference('dateEnd').getValue();
        globalUtils.ajaxRequest({
            url : '/dictAction/start',
            params : {
                id : id,
                dateEnd : dateEnd.getTime()
            },
            success : function(xhr, opts) {
                try {
                    var answer = Ext.decode(xhr.responseText);

                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Успешно',
                            msg : 'Операция выполнена успешно',
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.INFO,
                            fn : function() {
                                controller.fireEvent('start');
                                controller.getView().destroy();
                            }
                        });
                    } else {
                        if (console)
                            console.log(message);
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : 'Ошибка выполнения операции',
                            buttons : Ext.MessageBox.ERROR,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                } catch (exception) {
                    if (console)
                        console.log('Ошибка обработки ответа сервера');
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка обработки ответа от сервера',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Внутренняя ошибка сервера');
                switch (xhr.status) {
                case 403:
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Доступ запрещён.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка выполнения операции.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                }
            }
        });
    }
});