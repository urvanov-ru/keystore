Ext.define('KeyStore.view.client.ClientListController',{
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.clientList',
    
    onGridRender : function (self, eOpts) {
        var controller = this;
        this.getViewModel().getStore('clientListStore').load();
        if (globalData.user.accesses.SERVICE_ADD_CLIENT)
            controller.lookupReference('addButton').enable();
        if (globalData.user.accesses.SERVICE_EXPORT_CLIENT)
            controller.lookupReference('exportButton').enable();
    },
    onFilterClick : function (self) {
        var widget = Ext.widget('clientFilter');
        widget.getController().loadData(this.getViewModel().getData().filter);
    },
    onAddClick : function (self) {
        Ext.widget('clientEdit');
    },
    onEditClick : function (self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('clientEdit');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                id : id
            });
        }
    },
    
    onBlockClick : function (self) {
        var controller = this;
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var idArray = [];
            for (var n= 0; n < selectionArray.length; n++) {
                idArray.push(selectionArray[n].get('id'));
            }
            Ext.MessageBox.show({
                title : 'Вопрос',
                msg : 'Заблокировать ' + idArray.length + ' клиентов?',
                buttons : Ext.MessageBox.YESNO,
                icon : Ext.MessageBox.QUESTION,
                fn : function (buttonId, value, opt) {
                    if (buttonId == 'yes') {
                        controller.blockClients(idArray);
                    }
                }
            });
        } 
    },
    
    blockClients : function(idArray) {
        var controller = this;
        globalUtils.ajaxRequest({
            url : '/client/block',
            jsonData: {
                ids : idArray
            },
            success : function(xhr, opts) {
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Сообщение',
                            msg : 'Операция выполнена успешно',
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR,
                            fn : function () {
                                controller.getStore('clientListStore').reload();
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
                        msg : 'Ошибка обработки ответа от сервера',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка блокировки клиента.');
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
                        msg : 'Ошибка блокировки клиента.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                }
            }
        });
    },
    
    onSendNotificationClick : function (self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var idArray = [];
            for (var n= 0; n < selectionArray.length; n++) {
                idArray.push(selectionArray[n].get('id'));
            }
            var widget = Ext.widget('clientEmailNotification');
            widget.getController().loadData({idArray : idArray});
        } 
    },
    
    listen : {
        controller : {
            '#clientFilter' : {
                filter : 'onFilter'
            },
            '#clientEdit' : {
                save : 'onSave'
            }
        }
    },
    
    onSave : function () {
        this.getViewModel().getStore('clientListStore').reload();
    },
    
    onFilter : function(filter) {
        var clientListStore = this.getViewModel().getStore('clientListStore');
        this.getViewModel().getData().filter = filter;
        var filterString = '';
        if (filter.active) filterString += ' Статус ' + filter.activeName;
        if (filter.dictClientGroupId) filterString += ' Группа ' + filter.dictClientGroupName;
        if (filter.activeBegin) filterString += ' Активность с ' + Ext.Date.format(filter.activeBegin, 'd.m.Y');
        if (filter.activeEnd) filterString += ' Активность по ' + Ext.Date.format(filter.activeEnd, 'd.m.Y');
        if (filter.dictActionId) filterString += ' Учавствует в акции ' + filter.dictActionName;
        if (filterString == '') filterString = ' нет ';
        this.lookupReference('statusFilter').setText(filterString);
        clientListStore.load({
            params : {
                active : filter.active == 'ACTIVE' ? true : filter.active == 'BLOCKED'? false : null,
                dictClientGroupId : filter.dictClientGroupId,
                activeBegin : filter.activeBegin == null ? null
                        : filter.activeBegin.getTime(),
                activeEnd : filter.activeEnd == null ? null
                        : filter.activeEnd.getTime(),
                dictActionId : filter.dictActionId
            }
        });
    },
    

    onGridLoad : function(store, records, successful, eOpts) {
        var statusShowRecords = this.lookupReference('statusShowRecords');
        var statusTotalRecords = this.lookupReference('statusTotalRecords');
        var infoLabel = this.lookupReference('infoLabel');
        if (successful) {
            infoLabel.setText('');
            statusShowRecords.setText(records.length);
            statusTotalRecords.setText(store.getProxy().getReader().rawData.totalRecords);
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
        var statusSelected = this.lookupReference('statusSelected');
        statusSelected.setText(selected.length);
        this.updateButtonsEnable();
    },
    
    updateButtonsEnable : function() {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        var editButton = controller.lookupReference('editButton');
        var blockButton = controller.lookupReference('blockButton');
        var sendNotificationButton = controller.lookupReference('sendNotificationButton');
        var giveDictServiceTypeMenuItem = controller.lookupReference('giveDictServiceTypeMenuItem');
        var linkClientDictServiceTypeListMenuItem = controller.lookupReference('linkClientDictServiceTypeListMenuItem');
        var giveDictActionMenuItem = controller.lookupReference('giveDictActionMenuItem');
        var linkClientDictActionListMenuItem = controller.lookupReference('linkClientDictActionListMenuItem');
        var giveButton = controller.lookupReference('giveButton');
        if (selected.length > 0) {
            editButton.setDisabled(selected.length != 1);
            if (globalData.user.accesses.SERVICE_ADMIN_CLIENT) {
                blockButton.enable();
                sendNotificationButton.enable();
                giveDictServiceTypeMenuItem.enable();
                linkClientDictServiceTypeListMenuItem.setDisabled(selected.length != 1);
                giveDictActionMenuItem.enable();
                linkClientDictActionListMenuItem.setDisabled(selected.length != 1);
                giveButton.enable();
            }
        } else {
            editButton.disable();
            blockButton.disable();
            sendNotificationButton.disable();
            giveDictServiceTypeMenuItem.disable();
            linkClientDictServiceTypeListMenuItem.disable();
            giveDictActionMenuItem.disable();
            linkClientDictActionListMenuItem.disable();
            giveButton.disable();
        }
    },
    
    
    onExportClick : function (self) {
        window.open(globalUtils.toUrl('/client/exportXls/ClientList'));
    },
    
    onGiveDictServiceTypeClick : function (self) {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        if (selected.length > 0) {
            var clientIdsArray = new Array(selected.length);
            for (var n =0; n < selected.length; n++) {
                clientIdsArray[n] = selected[n].get('id');
            }
            var widget = Ext.widget('dictServiceTypeSelect');
            widget.getController().setOkListener(function (arg0) {
                var idsArray = new Array(arg0.selection.length);
                for (var n = 0; n < arg0.selection.length; n++) {
                    idsArray[n] = arg0.selection[n].id;
                }
                globalUtils.ajaxRequest({
                    url : '/client/giveDictServiceType',
                    jsonData : {
                        dictServiceTypeIds : idsArray,
                        clientIds : clientIdsArray
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
                                    fn : function () {
                                        controller.getViewModel().getStore('clientListStore').reload();
                                    }
                                });
                            } else {
                                if (console)
                                    console.log(message);
                                Ext.MessageBox.show({
                                    title : 'Ошибка',
                                    msg : answer.message,
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
                                buttons : Ext.MessageBox.ERROR,
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
            });
        }
    },
    
    onLinkClientDictServiceTypeListClick : function (self) {
        var controller =this;
        var selected = controller.lookupReference('grid').getSelectionModel().getSelection();
        if (selected.length > 0) {
            selectedId = selected[0].get('id');
            var widget = Ext.widget('linkClientDictServiceTypeList');
            widget.getController().loadData({clientId : selectedId});
        }
    },
    
    onGiveDictActionClick : function (self) {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        if (selected.length > 0) {
            var clientIdsArray = new Array(selected.length);
            for (var n =0; n < selected.length; n++) {
                clientIdsArray[n] = selected[n].get('id');
            }
            var widget = Ext.widget('dictActionSelect');
            widget.getController().setOkListener(function (arg0) {
                var idsArray = new Array(arg0.selection.length);
                for (var n = 0; n < arg0.selection.length; n++) {
                    idsArray[n] = arg0.selection[n].id;
                }
                globalUtils.ajaxRequest({
                    url : '/client/giveDictAction',
                    jsonData : {
                        dictActionIds : idsArray,
                        clientIds : clientIdsArray
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
                                    fn : function () {
                                        controller.getViewModel().getStore('clientListStore').reload();
                                    }
                                });
                            } else {
                                if (console)
                                    console.log(message);
                                Ext.MessageBox.show({
                                    title : 'Ошибка',
                                    msg : answer.message,
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
                                buttons : Ext.MessageBox.ERROR,
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
            });
        }
    },
    
    onLinkClientDictActionListClick : function (self) {
        var controller =this;
        var selected = controller.lookupReference('grid').getSelectionModel().getSelection();
        if (selected.length > 0) {
            selectedId = selected[0].get('id');
            var widget = Ext.widget('linkClientDictActionList');
            widget.getController().loadData({clientId : selectedId});
        }
    }
    
});