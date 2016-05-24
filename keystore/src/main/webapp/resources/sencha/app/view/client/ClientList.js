Ext.define('KeyStore.view.client.ClientList', {
    extend : 'Ext.panel.Panel',
    xtype : 'clientList',
    requires : [ 'KeyStore.view.client.ClientListController',
            'KeyStore.view.client.ClientListModel', 'Ext.panel.Panel',
            'Ext.grid.Panel', 'Ext.grid.feature.Grouping' ],
    layout : 'fit',
    viewModel : {
        type : 'clientList'
    },
    controller : 'clientList',
    items : [ {
        xtype : 'grid',
        reference : 'grid',
        selModel : {
            mode : 'MULTI'
        },
        tbar : [ {
            text : 'Добавить',
            handler : 'onAddClick',
            disabled : true,
            reference : 'addButton'
        }, {
            text : 'Изменить',
            handler : 'onEditClick',
            disabled : true,
            reference : 'editButton'
        }, {
            text : 'Блокировать',
            handler : 'onBlockClick',
            disabled : true,
            reference : 'blockButton'
        }, {
            text : 'Оповестить',
            handler : 'onSendNotificationClick',
            disabled : true,
            reference : 'sendNotificationButton'
        }, {
            text : 'Предоставить...',
            reference : 'giveButton',
            disabled : true,
            menu : [ {
                text : 'Предоставить пакет',
                handler : 'onGiveDictServiceTypeClick',
                disabled : true,
                reference : 'giveDictServiceTypeMenuItem'
            }, {
                text : 'Список предоставленных пакетов',
                handler : 'onLinkClientDictServiceTypeListClick',
                disabled : true,
                reference : 'linkClientDictServiceTypeListMenuItem'
            }, {
                xtype : 'menuseparator'
            }, {
                text : 'Предоставить акцию',
                handler : 'onGiveDictActionClick',
                disabled : true,
                reference : 'giveDictActionMenuItem'
            }, {
                text : 'Список предоставленных акций',
                handler : 'onLinkClientDictActionListClick',
                disabled : true,
                reference : 'linkClientDictActionListMenuItem'
            }]
        },{
            text : 'Экспорт',
            handler : 'onExportClick',
            disabled : true,
            reference : 'exportButton'
        }, {
            text : 'Фильтры',
            handler : 'onFilterClick',
            reference : 'filterButton'
        } ],
        bind : {
            store : '{clientListStore}'
        },
        features : [ {
            ftype : 'grouping',
            groupHeaderTpl : 'Группировка: {name} ({rows.length})',
            startCollapsed : false
        } ],
        layout : 'fit',
        columns : [ {
            text : 'Название',
            dataIndex : 'name',
            sortable : true,
            width : 300
        }, {
            text : 'Тип',
            dataIndex : 'clientType',
            sortable : true,
            width : 300
        }, {
            text : 'Уникальный код',
            dataIndex : 'uniqueId',
            sortable : true
        }, {
            text : 'Контактное лицо',
            dataIndex : 'contactPersonName',
            sortable : true,
            width : 300
        }, {
            text : 'E-mail контактного лица',
            dataIndex : 'contactPersonEmail',
            sortable : true,
            width : 100
        }, {
            text : 'Телефон контактного лица',
            dataIndex : 'contactPersonPhone',
            sortable : true,
            width : 100
        }, {
            text : 'ИНН',
            dataIndex : 'itn',
            sortable : true,
            width : 50
        }, {
            text : 'КПП',
            dataIndex : 'iec',
            sortable : true,
            width : 50
        }, {
            text : 'Активен',
            dataIndex : 'active',
            sortable : true,
            width : 70
        }, {
            text : 'Группа клиентов',
            dataIndex : 'clientGroupName',
            sortable : true,
            width : 200
        }, {
            text : 'Название организации',
            dataIndex : 'juridicalPersonName',
            sortable : true,
            width : 300
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
        } ]
    } ]
});