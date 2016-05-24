Ext.define('KeyStore.view.key.KeyListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.keyList',

    onGridRender : function(self, eOpts) {
        var controller = this;
        
        controller.lookupReference('filterButton').enable();
    },
    
    onActivateClick : function (self) {
        var controller = this;
        var selection = this.lookupReference('grid').getSelection();
        if (selection.length == 0) {
            Ext.MessageBox.show({
                title : 'Сообщение',
                msg : 'Выделите запись.',
                buttons : Ext.MessageBox.OK
            });
            return;
        }
        Ext.MessageBox.show({
            title : 'Подтвердите',
            msg : 'Активировать выбранный ключ?',
            buttons : Ext.MessageBox.YESNO,
            icon : Ext.MessageBox.QUESTION,
            fn : function (btn) {
                var selectedId = selection[0].get('id');
                controller.activateKey(selectedId);
            }
        });
        
    },
    
    activateKey : function (selectedId) {
        var controller = this;
        globalUtils.ajaxRequest({
            url : '/key/activate',
            params : {
                id : selectedId
            },
            success : function (xhr, opts) {
                var answer = Ext.decode(xhr.responseText);
                if (answer.success) {
                    Ext.MessageBox.show({
                        title : 'Внимание',
                        msg : 'Ключ успешно активирован.',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getViewModel().getStore('keyListStore').reload();
                        }
                    });
                } else {
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : answer.message,
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function (xhr, opts) {
                if (console) console.log('Ошибка активации ключа.');
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
                        msg : 'Ошибка активации ключа.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                }
            }
        });
    },
    
    onDownloadClick : function (self) {
        var selection = this.lookupReference('grid').getSelection();
        if (selection.length == 0) {
            Ext.MessageBox.show({
                title : 'Сообщение',
                msg : 'Выделите запись.',
                buttons : Ext.MessageBox.OK
            });
            return;
        }
        var selectedId =selection[0].get('id');
        window.open(globalUtils.toUrl('/key/download?id=' + selectedId));
    },
    
    onFilterClick : function(self) {
        var widget = Ext.widget('keyFilter');
        widget.getController().loadData(this.getViewModel().getData().filter);
    },

    onNewKeyClick : function(self) {
        var widget = Ext.widget('keyEdit');
    },

    onSave : function() {
        this.getViewModel().getStore('keyListStore').reload();
    },

    onFilter : function(filter) {
        var keyListStore = this.getViewModel().getStore('keyListStore');
        this.getViewModel().getData().filter = filter;
        var filterString = '';
        if (filter.clientName) filterString += ' Название клиента "' + filter.clientName + '"';
        if (filter.status) filterString += ' Статус ' + filter.statusName;
        if (filter.activeOnDate) filterString += ' Активен на дату: ' + Ext.Date.format(filter.activeOnDate, 'd.m.Y');
        if (filterString == '') filterString = ' нет ';
        this.lookupReference('statusFilter').setText(filterString);
        keyListStore.load({
            params : {
                clientName : filter.clientName,
                status : filter.status,
                activeOnDate : filter.activeOnDate == null ? null : filter.activeOnDate.getTime()
            }
        });
    },

    listen : {
        controller : {
            '#keyFilter' : {
                filter : 'onFilter'
            },
            '#baseKeyEdit' : {
                save : 'onSave'
            }
        }
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
    
    onGridSelectionChange : function( grid, selected, eOpts ) {
        var statusSelected = this.lookupReference('statusSelected');
        statusSelected.setText(selected.length);
        this.updateButtonsEnable();
    },
    
    updateButtonsEnable : function () {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        var activateButton = this.lookupReference('activateButton');
        var downloadButton = this.lookupReference('downloadButton');
        var deleteButton = this.lookupReference('deleteButton');
        if (selected.length > 0) {
            var status = selected[0].get('status');
            var dictActionId = selected[0].get('dictActionId');
            if (globalData.user.accesses.CLIENT_ACTIVATE_KEY) {
                activateButton.setDisabled(status != 'CREATED');
                deleteButton.setDisabled(status != 'CREATED' || 
                        dictActionId == null || dictActionId == '');
            }
            if (globalData.user.accesses.CLIENT_GET_KEY)
                downloadButton.setDisabled(status != 'ACTIVE');
        } else {
            activateButton.setDisabled(true);
            deleteButton.setDisabled(true);
            downloadButton.setDisabled(true);
        }
    },
    
    
    onRender : function (self, eOpts) {
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            var str = globalData.user.authorities[n];
            if (str == 'ROLE_SERVICE') {
                this.lookupReference('deleteButton').show();
                this.lookupReference('filterButton').show();
            }
        }
    },
    
    loadData : function (arg) {
        var params = {
                orderId : arg == null ? null : arg.orderId
        };
        this.getViewModel().getStore('keyListStore').load({params : params});
    },
    
    onDeleteClick : function (self) {
        var controller = this;
        var selection = this.lookupReference('grid').getSelection();
        if (selection.length == 0) {
            Ext.MessageBox.show({
                title : 'Сообщение',
                msg : 'Выделите запись.',
                buttons : Ext.MessageBox.OK
            });
            return;
        }
        Ext.MessageBox.show({
            title : 'Подтвердите',
            msg : 'Вы действительно хотите удалить выделенный ключ?',
            buttons : Ext.MessageBox.YESNO,
            icon : Ext.MessageBox.QUESTION,
            fn : function (btn) {
                if (btn == 'yes') {
                    var selectedId = selection[0].get('id');
                    controller.deleteKey(selectedId);
                }
            }
        });
        
    },
    
    deleteKey : function (selectedId) {
        var controller = this;
        globalUtils.ajaxRequest({
            url : '/key/delete',
            params : {
                id : selectedId
            },
            success : function (xhr, opts) {
                var answer = Ext.decode(xhr.responseText);
                if (answer.success) {
                    Ext.MessageBox.show({
                        title : 'Внимание',
                        msg : 'Ключ успешно удалён.',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getViewModel().getStore('keyListStore').reload();
                        }
                    });
                } else {
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : answer.message,
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function (xhr, opts) {
                if (console) console.log('Ошибка удаления ключа.');
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
                        msg : 'Ошибка удаления ключа.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                }
            }
        });
    }
});