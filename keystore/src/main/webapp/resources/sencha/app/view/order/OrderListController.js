Ext.define('KeyStore.view.order.OrderListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.orderList',
    id : 'orderList',
    onGridRender : function(self, eOpts) {
        var controller = this;
        this.getViewModel().getStore('orderListStore').load();
        if (globalData.user.accesses.CLIENT_ADD_ORDER)
            controller.lookupReference('newButton').enable();
        if (globalData.user.accesses.SERVICE_EXPORT_ORDER ||
                globalData.user.accesses.CLIENT_EXPORT_DATA)
            controller.lookupReference('exportButton').enable();
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            if (globalData.user.authorities[n] == 'ROLE_SERVICE')
                controller.lookupReference('filterButton').enable();
        }
    },

    onFilterClick : function(self) {
        var widget = Ext.widget('orderFilter');
        widget.getController().loadData(this.getViewModel().getData().filter);
    },

    onNewOrderClick : function(self) {
        var widget = Ext.widget('orderEdit');
    },

    onSave : function() {
        this.getViewModel().getStore('orderListStore').reload();
    },
    
    onKeyListClick : function () {
        var grid = this.lookupReference('grid');
        var selections = grid.getSelection();
        if (selections.length > 0) {
            this.fireEvent('keyListClick', {orderId : selections[0].get('id')});
        }
    },

    onFilter : function(filter) {
        var orderListStore = this.getViewModel().getStore('orderListStore');
        this.getViewModel().getData().filter = filter;
        var filterString = '';
        if (filter.clientName) filterString += ' Название клиента "' + filter.clientName + '"';
        if (filter.status) filterString += ' Статус ' + filter.statusName;
        if (filter.createdAtBegin) filterString += ' Дата от ' + Ext.Date.format(filter.createdAtBegin, 'd.m.Y');
        if (filter.createdAtEnd) filterString += ' Дата по ' + Ext.Date.format(filter.createdAtEnd, 'd.m.Y');
        if (filterString == '') filterString = ' нет ';
        this.lookupReference('statusFilter').setText(filterString);
        orderListStore.load({
            params : {
                clientName : filter.clientName,
                status : filter.status,
                createdAtBegin : filter.createdAtBegin == null ? null
                        : filter.createdAtBegin.getTime(),
                createdAtEnd : filter.createdAtEnd == null ? null
                        : filter.createdAtEnd.getTime()
            }
        });
    },

    listen : {
        controller : {
            '#orderFilter' : {
                filter : 'onFilter'
            },
            '#orderEdit' : {
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
        var keyListButton = controller.lookupReference('keyListButton');
        var payButton = controller.lookupReference('payButton');
        var cancelButton = controller.lookupReference('cancelButton');
        var payBackButton = controller.lookupReference('payBackButton');
        if (selected.length > 0) {
            if (globalData.user.accesses.CLIENT_READ_KEY ||
                    globalData.user.accesses.CLIENT_ACTIVATE_KEY ||
                    globalData.user.accesses.CLIENT_GET_KEY)
                keyListButton.enable(); 
            else 
                keyListbutton.disable();
            
            if (selected[0].get('status') == 'PENDING_PAYMENT'
                    && globalData.user.accesses.CLIENT_ADD_ORDER) {
                cancelButton.enable();
                payButton.enable();
            } else {
                cancelButton.disable();
                payButton.disable();
            }
            if (selected[0].get('status') == 'PAYED') {
                payBackButton.enable();
            } else {
                payBackButton.disable();
            }
            
        } else {
            keyListButton.disable();
            payButton.disable();
            payBackButton.disable();
            cancelButton.disable();
        }
    },

    
    onRender : function (self, eOpts) {
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            var str = globalData.user.authorities[n];
            if (str == 'ROLE_SERVICE') {
                this.lookupReference('filterButton').show();
            }
        }
    },
    
    onCancelOrderClick : function (self) {
        var controller = this;
        var grid = this.lookupReference('grid');
        var selection = grid.getSelection();
        var id = selection[0].get('id');
        globalUtils.ajaxRequest({
            url : '/order/cancel',
            params : {
                orderId : id
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
                                controller.getViewModel().getStore('orderListStore').reload();
                            }
                        });
                    } else {
                        if (console)
                            console.log(message);
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : 'Ошибка отмены заказа.',
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
                    console.log('Ошибка отмены заказа.');
                
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
                        msg : 'Ошибка отмены заказа.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR});
                    break;
                }
            }
        });
    },
    
    onPayBackClick : function (self) {
        var controller = this;
        var grid = this.lookupReference('grid');
        var selection = grid.getSelection();
        if (selection.length > 0) {
            Ext.MessageBox.show({
                title : 'Подтверждение',
                msg : 'Вы действительно хотите инициировать возврат средств?',
                buttons : Ext.MessageBox.YESNO,
                icon : Ext.MessageBox.QUESTION,
                fn : function (btn) {
                    if (btn == 'yes') {
                        controller.payBack(selection[0].get('id'));
                    }
                }
            });
            //var widget = Ext.widget('payuIrn');
            //widget.getController().loadData({
            //    orderId : selection[0].get('id')
            //});
        }
    },
    
    payBack : function (id) {
        globalUtils.ajaxRequest({
            url : '/order/payBack',
            params : {
                orderId : id
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
                                controller.getViewModel().getStore('orderListStore').reload();
                            }
                        });
                    } else {
                        if (console)
                            console.log(message);
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : 'Ошибка инициализации процедуры возврата средств.',
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
                    console.log('Ошибка инициализации процедуры возврата средств.');
                
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
                        msg : 'Ошибка инициализации процедуры возврата средств.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR});
                    break;
                }
            }
        });
    },
    
    onExportClick : function (self) {
        window.open(globalUtils.toUrl('/order/exportXls/OrderList'));
    },
    
    onPayByTransferToAccountClick : function (self) {
        if (console) console.log('Оплата банковским переводом.');
        var grid = this.lookupReference('grid');
        var selection = grid.getSelection();
        if (selection.length > 0) {
            window.open(globalUtils.toUrl('/order/invoice?orderId='+selection[0].get('id')));
        }
    },
    
    onPayByCardClick : function (self) {
        if (console) console.log('Оплата картой.');
        this.showPayWindow('CARD');
    },
    
    onPayByYandexMoneyClick : function (self) {
        if (console) console.log('Оплата через Яндекс.Деньги.');
        this.showPayWindow('YANDEX');
    },
    
    onPayByWebMoneyClick : function (self) {
        if (console) console.log('Оплата через Web Money.');
        this.showPayWindow('WEBMONEY');
    },
    
    onPayByQiwiClick : function (self) {
        if (console) console.log('Оплата через QIWI.');
        this.showPayWindow('QIWI');
    },
    
    showPayWindow : function (payMethod) {
        var grid = this.lookupReference('grid');
        var selection = grid.getSelection();
        if (selection.length > 0) {
            var widget = Ext.widget('onpay');
            widget.getController().loadData({
                orderId : selection[0].get('id'),
                payMethod : payMethod
            });
        }
    }
});