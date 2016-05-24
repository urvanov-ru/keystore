Ext.define('KeyStore.view.dictAction.DictActionEditController',{
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.dictActionEdit',
    id : 'dictActionEdit',
    
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    
    loadData : function (obj) {
        var id = obj.id;
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/dictAction/edit',
            params : {
                id : id
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('id').setValue(id);
                        controller.lookupReference('name').setValue(answer.info.name);
                        controller.lookupReference('description').setValue(answer.info.description);
                        if (answer.info.dateBegin) controller.lookupReference('dateBegin').setValue(new Date(answer.info.dateBegin));
                        if (answer.info.dateEnd) controller.lookupReference('dateEnd').setValue(new Date(answer.info.dateEnd));
                        controller.lookupReference('dictServiceTypeId').setValue(answer.info.dictServiceTypeId);
                        controller.lookupReference('dictServiceTypeName').setText(answer.info.dictServiceTypeName);
                        controller.lookupReference('dictActionType').setValue(answer.info.dictActionType);
                        controller.lookupReference('forNewClients').setValue(answer.info.forNewClients);
                    } else {
                        if (console) console.log('Ошибка загрузки данных об акции.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки данных об акции.',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK,
                            fn : function () {
                                controller.getView().destroy();
                            }
                        });
                    }
                } catch (exception) {
                    if (console) console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка загрузки данных об акции.');
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
                        msg : 'Ошибка загрузки данных об акции.',
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
    
    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var id = controller.lookupReference('id').getValue();
        var name = controller.lookupReference('name').getValue();
        var description = controller.lookupReference('description').getValue();
        var dictServiceTypeId = controller.lookupReference('dictServiceTypeId').getValue();
        var dictActionType = controller.lookupReference('dictActionType').getValue();
        var forNewClients = controller.lookupReference('forNewClients').getValue();
        
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/dictAction/save',
            jsonData : {
                id : id,
                name : name,
                description : description,
                dictServiceTypeId : dictServiceTypeId,
                dictActionType : dictActionType,
                forNewClients : forNewClients
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
                    infoLabel.setText('Ошибка сохранения акции.');
                    break;
                }
            }
        });
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
        if (dictServiceTypeId) {
            widget.getController().setSelectedIds([dictServiceTypeId]);
        }
    }
});