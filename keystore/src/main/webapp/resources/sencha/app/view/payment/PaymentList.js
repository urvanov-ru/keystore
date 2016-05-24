Ext.define('KeyStore.view.payment.PaymentList', {
    extend : 'Ext.panel.Panel',
    xtype : 'paymentList',
    requires : [ 'KeyStore.view.payment.PaymentListController',
            'KeyStore.view.payment.PaymentListModel', 'Ext.panel.Panel',
            'Ext.grid.Panel', 'Ext.grid.feature.Grouping' ],
    layout : 'fit',
    viewModel : {
        type : 'paymentList'
    },
    controller : 'paymentList',
    items : [ {
        xtype : 'panel',
        layout : {
            type : 'border'
        },
        items : [ {
            xtype : 'grid',
            region : 'center',
            bind : {
                store : '{paymentListStore}'
            },
            tbar : [ {
                text : 'Создать корректировку',
                reference : 'correctButton',
                handler : 'onCorrectClick',
                hidden : true,
                disabled : true
            }, {
                text : 'Провести',
                reference : 'completeButton',
                handler : 'onCompleteClick',
                hidden : true,
                disabled : true
            },{
                text : 'Скачать',
                reference : 'downloadButton',
                hidden : true,
                disabled : true
            }, {
                text : 'Выгрузить',
                reference : 'exportButton',
                hidden : true,
                handler : 'onExportClick'
            }, {
                text : 'Фильтры',
                handler : 'onFilterClick',
                reference : 'filterButton',
                hidden : true
            } ],
            features : [ {
                ftype : 'grouping',
                groupHeaderTpl : 'Группировка: {name} ({rows.length})',
                startCollapsed : true
            } ],
            layout : 'fit',
            columns : [ {
                text : 'Создан',
                dataIndex : 'createdAt',
                xtype : 'datecolumn',
                format : 'd.m.Y H:i:s',
                sortable : true,
                width : 150
            }, {
                text : 'Заказ создан',
                dataIndex : 'orderCreatedAt',
                xtype : 'datecolumn',
                format : 'd.m.Y H:i:s',
                sortable : true,
                width : 150
            }, {
                text : 'Заказанный пакет услуг',
                dataIndex : 'orderDictServiceTypeName',
                sortable : true,
                width : 300
            },  {
                text : 'Тип',
                dataIndex : 'paymentTypeName',
                sortable : true,
                width : 150
            }, {
                text : 'Статус',
                dataIndex : 'statusName',
                sortable : true,
                width : 100
            }, {
                text : 'Способ платежа',
                dataIndex : 'methodName',
                sortable : true,
                width : 150
            },  {
                text : 'ФИО клиента',
                dataIndex : 'clientName',
                sortable : true,
                width : 300,
                reference : 'clientNameColumn',
                hidden : true
            }, {
                text : 'Полная сумма',
                dataIndex : 'amountWithCommission',
                sortable : true,
                width : 100,
                reference  : 'amountWithCommissionColumn',
                hidden : true
            }, {
                text : 'Без коммиссии',
                dataIndex : 'amountWithoutCommission',
                sortable : true,
                width : 100
            }, {
                text : 'Коммиссия',
                dataIndex : 'amountOfCommission',
                sortable : true,
                width : 100,
                reference : 'amountOfCommissionColumn',
                hidden : true
            }],
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