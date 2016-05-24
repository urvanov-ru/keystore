Ext.define('KeyStore.view.order.OrderList', {
    extend : 'Ext.panel.Panel',
    xtype : 'orderList',
    requires : [ 'KeyStore.view.order.OrderListController',
            'KeyStore.view.order.OrderListModel', 'Ext.panel.Panel',
            'Ext.grid.Panel', 'Ext.grid.feature.Grouping' ],
    layout : 'fit',
    viewModel : {
        type : 'orderList'
    },
    controller : 'orderList',
    items : [ {
        xtype : 'panel',
        layout : {
            type : 'border'
        },
        items : [ {
            xtype : 'grid',
            region : 'center',
            tbar : [ {
                text : 'Новый заказ',
                handler : 'onNewOrderClick',
                reference : 'newButton',
                disabled : true
            }, {
                text : 'Ключи',
                handler : 'onKeyListClick',
                reference : 'keyListButton',
                disabled : true
            }, {
                text : 'Оплатить',
                reference : 'payButton',
                disabled : true,
                menu : {
                    items : [{
                        text : 'Переводом на счёт',
                        handler : 'onPayByTransferToAccountClick'
                    }, {
                        text : 'Банковской картой',
                        handler : 'onPayByCardClick'
                    }, {
                        text : 'Яндекс.Деньги',
                        handler : 'onPayByYandexMoneyClick'
                    },{
                        text : 'Web Money',
                        handler : 'onPayByWebMoneyClick'
                    }, {
                        text : 'QIWI',
                        handler : 'onPayByQiwiClick'
                    }]
                }
            }, {
                text : 'Отменить',
                handler : 'onCancelOrderClick',
                disabled : true,
                reference : 'cancelButton'
            }, {
                text : 'Возврат средств',
                handler : 'onPayBackClick',
                disabled : true,
                reference : 'payBackButton'
            }, {
                text : 'Экспорт',
                handler : 'onExportClick',
                reference : 'exportButton',
                disabled : true
            }, {
                text : 'Фильтры',
                handler : 'onFilterClick',
                hidden : true,
                reference : 'filterButton',
                disabled : true
            } ],
            bind : {
                store : '{orderListStore}'
            },
            listeners : {
                render : 'onGridRender'
            },
            features : [ {
                ftype : 'grouping',
                groupHeaderTpl : 'Группировка: {name} ({rows.length})',
                startCollapsed : true
            } ],
            columns : [ {
                text : 'Создан',
                dataIndex : 'createdAt',
                xtype : 'datecolumn',
                format : 'd.m.Y H:i:s',
                sortable : true,
                width : 150
            }, {
                text : 'Название',
                dataIndex : 'dictServiceTypeName',
                sortable : true,
                width : 300
            }, {
                text : 'Статус',
                dataIndex : 'statusName',
                sortable : true,
                width : 300
            }, {
                text : 'Оплачен',
                dataIndex : 'payDateTime',
                sortable : true,
                width : 150,
                xtype : 'datecolumn',
                format : 'd.m.Y'
            }, {
                text : 'Завершён',
                dataIndex : 'completedDateTime',
                sortable : true,
                width : 150,
                xtype : 'datecolumn',
                format : 'd.m.Y H:i:s'
            }, {
                text : 'Клиент',
                dataIndex : 'clientName',
                width : 300,
                sortable : true
            }, {
                text : 'Создал',
                dataIndex : 'createdByName',
                width : 300,
                sortable : true
            } ],
            listeners : {
                render : 'onGridRender',
                selectionchange : 'onGridSelectionChange'
            },
            bbar : [ {
                xtype : 'label',
                reference : 'infoLabel'
            }, {
                xtype : 'tbseparator'
            }, {
                xtype : 'label',
                text : 'Показано '
            }, {
                xtype : 'label',
                reference : 'statusShowRecords',
                text : '0'
            }, {
                xtype : 'label',
                text : ' из'
            }, {
                xtype : 'label',
                reference : 'statusTotalRecords',
                text : '0'
            }, {
                xtype : 'label',
                text : ', выбрано: '
            }, {
                xtype : 'label',
                reference : 'statusSelected',
                text : '0'
            }, {
                xtype : 'tbseparator'
            }, {
                xtype : 'label',
                text : '    Фильтрация: '
            }, {
                xtype : 'label',
                reference : 'statusFilter',
                text : ' нет '
            } ],
            reference : 'grid'
        } ]
    } ],
    
    listeners : {
        render : 'onRender'
    }
});