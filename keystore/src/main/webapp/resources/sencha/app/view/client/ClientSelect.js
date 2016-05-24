Ext.define('KeyStore.view.client.ClientSelect', {
    extend : 'Ext.window.Window',
    xtype : 'clientSelect',
    requires : [ 'KeyStore.view.client.ClientSelectController',
            'KeyStore.view.client.ClientSelectModel' ],
    controller : 'clientSelect',
    viewModel : {
        type : 'clientSelect'
    },
    title : 'Выбор клиентов',
    autoShow : true,
    modal : true,
    width : 600,
    height : 400,
    layout : 'fit',
    items : [ {
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{clientSelectStore}'
        },
        listeners : {
            render : 'onGridRender',
            selectionchange : 'onGridSelectionChange'
        },
        features : [ {
            ftype : 'grouping',
            groupHeaderTpl : 'Группировка: {name} ({rows.length})',
            startCollapsed : true
        } ],
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
        tbar : [{
            text : 'Фильтры',
            handler : 'onFilterClick'
        }],
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
    } ],
    buttons : [{
        text : 'ОК',
        handler : 'onOkClick'
    }, {
        text : 'Отмена',
            handler : 'onCancelClick'
    }]
});