Ext.define('KeyStore.view.user.UserListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.userList',

    onGridRender : function(self, eOpts) {

    },
    onFilterClick : function(self) {
        Ext.widget('userFilter');
    },
    onAddClick : function(self) {
        Ext.widget('userEdit');
    },
    onEditClick : function(self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('userEdit');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                id : id
            });
        }
    },
    
    onBlockClick :function (self) {
        var controller = this;
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var selection = selectionArray[0];
            var id = selection.get('id');
            globalUtils.ajaxRequest({
                url : '/user/block',
                params : {
                    id : id
                },
                success : function(xhr, opts) {
                    try {
                        var answer = Ext.decode(xhr.responseText);

                        if (answer.success) {
                            Ext.MessageBox.show({
                                title : 'Успешно',
                                msg : 'Пользователь успешно заблокирован',
                                buttons : Ext.MessageBox.OK,
                                icon : Ext.MessageBox.INFO,
                                fn : function () {
                                    controller.loadData();
                                }
                            });
                        } else {
                            if (console)
                                console.log(message);
                            Ext.MessageBox.show({
                                title : 'Ошибка',
                                msg : 'Внутренняя ошибка сервера.',
                                buttons : Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR
                            });
                        }
                    } catch (exception) {
                        if (console)
                            console.log('Ошибка обработки ответа сервера');
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : 'Ошибка обработки ответа сервера.',
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                },
                failure : function(xhr, opts) {
                    if (console)
                        console.log('Ошибка блокировки пользователя');
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
                            msg : 'Ошибка блокировки пользователя.',
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                        break;
                    }
                }
            });
        }
    },

    loadData : function(obj) {
        this.getViewModel().getStore('userListStore').load();
    },

    listen : {
        controller : {
            '#userEdit' : {
                save : 'onSave'
            }
        }
    },

    onSave : function() {
        this.loadData();
    },

    onGridBeforeLoad : function(store, operation, eOpts) {

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
    
    onGridSelectionChange : function(grid, selected, eOpts) {
        var statusSelected = this.lookupReference('statusSelected');
        statusSelected.setText(selected.length);
        this.updateButtonsEnable();
    },
    
    updateButtonsEnable : function () {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        var editButton = this.lookupReference('editButton');
        var blockButton = this.lookupReference('blockButton');
        var accessButton = this.lookupReference('accessButton');
        if (selected.length > 0) {
            editButton.setDisabled(false);
            var enabled = selected[0].get('enabled');
            blockButton.setDisabled(!enabled);
            accessButton.setDisabled(false);
        } else {
            editButton.setDisabled(true);
            blockButton.setDisabled(true);
            accessButton.setDisabled(true);
        }
    },
    
    onAccessClick : function (self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('userAccess');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({id : id});
        }
    }
});