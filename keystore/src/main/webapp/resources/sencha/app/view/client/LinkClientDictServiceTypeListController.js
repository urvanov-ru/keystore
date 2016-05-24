Ext.define('KeyStore.view.client.LinkClientDictServiceTypeListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.linkClientDictServiceTypeList',
    
    setClientId : function (clientId) {
        this.getViewModel().getData().clientId = clientId;
    },
    
    onGridRender : function(self, eOpts) {
    },
    
    onGridBeforeLoad : function(store, operation, eOpts) {

    },

    onGridStoreLoad : function(store, records, successful, eOpts) {
        var infoLabel = this.lookupReference('infoLabel');
        if (successful) {
            infoLabel.setText('');
        } else {
            switch (eOpts.error.status) {
            case 401:
                globalUtils.showSessionLostMessage();
                break;
            default :
                infoLabel.setText('Ошибка обновления таблицы.');
                break;
            }
        }
        this.updateButtonsEnable();
    },
    
    onGridSelectionChange : function( grid, selected, eOpts ) {
        this.updateButtonsEnable();
    },
    
    
    updateButtonsEnable : function () {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        var removeButton = controller.lookupReference('removeButton');
        if (selected.length > 0) {
            removeButton.enable();
        } else {
            removeButton.disable();
        }
    },
    
    onRemoveClick : function (self) {
        var controller = this;
        var grid = controller.lookupReference('grid');
        var selected = grid.getSelectionModel().getSelection();
        if (selected.length > 0) {
            var idsArray = new Array(selected.length);
            for (var n = 0; n < selected.length; n++) {
                idsArray[n] = selected[n].get('id');
            }
            globalUtils.ajaxRequest({
                url : '/client/removeLinkClientDictServiceType',
                jsonData : {
                    clientId : controller.getViewModel().getData().clientId,
                    dictServiceTypeIds : idsArray
                },
                success : function(xhr, opts) {
                    try {
                        var answer = Ext.decode(xhr.responseText);

                        if (answer.success) {
                            Ext.MessageBox.show({
                                title : 'Успешно',
                                msg : 'Операция выполнена успешно.',
                                buttons : Ext.MessageBox.OK,
                                icon : Ext.MessageBox.INFO,
                                fn : function () {
                                    controller.getViewModel().getStore('linkClientDictServiceTypeListStore').reload();
                                }
                            });
                        } else {
                            if (console)
                                console.log(message);
                            Ext.MessageBox.show({
                                title : 'Ошибка',
                                msg : answer.message,
                                buttons : Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR
                            });
                        }
                    } catch (exception) {
                        if (console)
                            console.log('Ошибка обработки ответа сервера');
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : 'Ошибка обработки ответа от сервера.',
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                },
                failure : function(xhr, opts) {
                    if (console)
                        console.log('Ошибка выполнения операции');
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
    },
    
    loadData : function (obj) {
        this.getViewModel().getData().clientId = obj.clientId;
        this.getViewModel().getStore('linkClientDictServiceTypeListStore').load({
            params : {
                clientId : obj.clientId
            }
        });
    }
});