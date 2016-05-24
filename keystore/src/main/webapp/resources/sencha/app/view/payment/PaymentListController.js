Ext.define('KeyStore.view.payment.PaymentListController',{
    extend : 'Ext.app.ViewController',
    requires : ['KeyStore.view.payment.PaymentFilterController',
                'Ext.window.MessageBox'],
    alias : 'controller.paymentList',
    

    onGridRender : function(self, eOpts) {
        var controller = this;
        controller.getViewModel().getStore('paymentListStore').load();
        if (globalData.user.accesses.SERVICE_EXPORT_PAYMENT ||
                globalData.user.accesses.CLIENT_EXPORT_DATA)
            controller.lookupReference('exportButton').enable();
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            if (globalData.user.authorities[n] == 'ROLE_SERVICE')
                controller.lookupReference('filterButton').enable();
        }
    },

    onFilterClick : function (self) {
        var widget = Ext.widget('paymentFilter');
        widget.getController().loadData(this.getViewModel().getData().filter);
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
        var correctButton = controller.lookupReference('correctButton');
        var completeButton = controller.lookupReference('completeButton');
        var downloadButton = controller.lookupReference('downloadButton');
        if (selected.length > 0) {
            correctButton.enable();
            if (selected[0].get('status') == 'SCHEDULED')
                completeButton.enable();
            else
                completeButton.disable();
            downloadButton.enable();
        } else {
            correctButton.disable();
            completeButton.disable();
            downloadButton.disable();
        }
    },

    
    onRender : function (self, eOpts) {
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            var str = globalData.user.authorities[n];
            if (str == 'ROLE_SERVICE') {
                this.lookupReference('filterButton').show();
                this.lookupReference('clientNameColumn').show();
                this.lookupReference('amountWithCommissionColumn').show();
                this.lookupReference('amountOfCommissionColumn').show();
                this.lookupReference('correctButton').show();
                this.lookupReference('completeButton').show();
                this.lookupReference('downloadButton').show();
                this.lookupReference('exportButton').show();
            }
        }
    },
    
    onFilter : function(filter) {
        var orderListStore = this.getViewModel().getStore('paymentListStore');
        this.getViewModel().getData().filter = filter;
        var filterString = '';
        if (filter.clientName) filterString += ' Название клиента "' + filter.clientName + '"';
        if (filter.paymentType) filterString += ' Тип ' + filter.paymentTypeName;
        if (filter.status) filterString += ' Статус ' + filter.statusName;
        if (filter.method) filterString += ' Метод ' + filter.methodName;
        if (filter.createdAtBegin) filterString += ' Дата от ' + Ext.Date.format(filter.createdAtBegin, 'd.m.Y');
        if (filter.createdAtEnd) filterString += ' Дата по ' + Ext.Date.format(filter.createdAtEnd, 'd.m.Y');
        if (filterString == '') filterString = ' нет ';
        this.lookupReference('statusFilter').setText(filterString);
        orderListStore.load({
            params : {
                clientName : filter.clientName,
                paymentType : filter.paymentType,
                status : filter.status,
                method : filter.method,
                createdAtBegin : filter.createdAtBegin == null ? null
                        : filter.createdAtBegin.getTime(),
                createdAtEnd : filter.createdAtEnd == null ? null
                        : filter.createdAtEnd.getTime()
            }
        });
    },

    listen : {
        controller : {
            '#paymentFilter' : {
                filter : 'onFilter'
            },
            '#paymentCorrect' : {
                save : 'onSave'
            }
        }
    },
    
    onSave : function () {
        this.getViewModel().getStore('paymentListStore').reload();
    },
    
    onCorrectClick : function (self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('paymentCorrect');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                parentPaymentId : id
            });
        }
    },
    
    onCompleteClick : function (self) {
        var controller = this;
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var selection = selectionArray[0];
            var id = selection.get('id');
            Ext.MessageBox.show({
                title : 'Подтвердите',
                msg : 'Вы действительно хотите провести платёж?',
                buttons : Ext.MessageBox.YESNO,
                icon : Ext.MessageBox.QUESTION,
                fn : function (btn) {
                    if (btn == 'yes') controller.complete(id);
                }
            });
        }
    },
    
    complete : function (id) {
        var controller = this;
        globalUtils.ajaxRequest({
            url : '/payment/complete',
            params : {
                paymentId : id
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
                            fn : function() {
                                controller.getViewModel().getStore('paymentListStore').reload();
                            }
                        });
                    } else {
                        if (console)
                            console.log(answer.message);
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
                        icon : Ext.MessageBox.ERROR,
                        buttons : Ext.MessageBox.OK
                    });
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка проведения платежа.');
                
                switch (xhr.status) {
                case 403:
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Доступ запрещён.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR});
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка проведения платежа.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR});
                    break;
                }
            }
        });
    },
    
    onExportClick : function (self) {
        window.open(globalUtils.toUrl('/payment/exportXls/PaymentList'));
    }
});