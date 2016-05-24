Ext.define('KeyStore.view.dictServiceType.DictServiceTypeEditController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    id : 'dictServiceTypeEdit',
    alias : 'controller.dictServiceTypeEdit',

    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var id = this.lookupReference('id').getValue();
        var name = this.lookupReference('name').getValue();
        var description = this.lookupReference('description').getValue();
        var status = this.lookupReference('status').getValue();
        var amount = this.lookupReference('amount').getValue();
        var amount30Days = this.lookupReference('amount30Days').getValue();
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/dictServiceType/save',
            jsonData : {
                id : id,
                name : name,
                description : description,
                status : status,
                amount : amount,
                amount30Days : amount30Days
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
                    if (console) console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка сохранения пакета.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения пакета.');
                    break;
                }
            }
        });
    },

    onCancelClick : function(self) {
        this.getView().destroy();
    },

    loadData : function(obj) {
        var id = obj.id;
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/dictServiceType/edit',
            params : {
                id : id
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('id').setValue(id);
                        controller.lookupReference('name').setValue(answer.dictServiceType.name);
                        controller.lookupReference('description').setValue(answer.dictServiceType.description);
                        controller.lookupReference('status').setValue(answer.dictServiceType.status);
                        controller.lookupReference('amount').setValue(answer.dictServiceType.amount);
                        controller.lookupReference('amount30Days').setValue(answer.dictServiceType.amount30Days);
                        
                    } else {
                        if (console) console.log('Ошибка загрузки данных.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки пакета для редактирования',
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
                    console.log('Ошибка загрузки пакета услуг.');
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
                        msg : 'Ошибка загрузки пакета услуг для редактирования.',
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