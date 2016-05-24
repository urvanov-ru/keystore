Ext.define('KeyStore.view.settings.GlobalSettingsController',{
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.globalSettings',
    
    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var sessionStoreDays = this.lookupReference('sessionStoreDays').getValue();
        var dictClientGroupId = this.lookupReference('dictClientGroupId').getValue();
        
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/globalSettings/save',
            jsonData : {
                sessionStoreDays : sessionStoreDays,
                dictClientGroupId : dictClientGroupId
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
                    console.log('Ошибка сохранения настроек.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения настроек.');
                    break;
                }
            }
        });
    },

    loadData : function(obj) {
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/globalSettings/edit',
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('sessionStoreDays').setValue(answer.info.sessionStoreDays);
                        controller.lookupReference('dictClientGroupId').setValue(answer.info.dictClientGroupId);
                        controller.lookupReference('dictClientGroupName').setText(answer.info.dictClientGroupName);
                    } else {
                        if (console) console.log('Ошибка загрузки настроек.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки настроек.',
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
                    console.log('Ошибка загрузки настроек.');
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
                        msg : 'Ошибка загрузки настроек.',
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
    
    
    onDictClientGroupSelectClick : function(self) {
        var dictClientGroupId  = this.lookupReference('dictClientGroupId').getValue();
        var controller = this;
        var widget = Ext.widget('dictClientGroupSelect');
        widget.getController().setOkListener(function(arg0) {
            var id = arg0.id;
            var name = arg0.name;
            controller.lookupReference('dictClientGroupId').setValue(id);
            controller.lookupReference('dictClientGroupName').setText(name);
        });
        widget.getController().setSelectedId(dictClientGroupId);
    }
});