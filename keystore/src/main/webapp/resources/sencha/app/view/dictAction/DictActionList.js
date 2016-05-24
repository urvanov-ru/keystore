Ext.define('KeyStore.view.dictAction.DictActionList', {
    extend : 'Ext.panel.Panel',
    xtype : 'dictActionList',
    requires : [ 'KeyStore.view.dictAction.DictActionListController',
            'KeyStore.view.dictAction.DictActionListModel',
            'Ext.panel.Panel', 'Ext.grid.Panel', 'Ext.grid.feature.Grouping' ],
    controller : 'dictActionList',
    viewModel : {
        type : 'dictActionList'
    },
    layout : 'fit',
    items : [ {
        xtype : 'grid',
        reference : 'grid',
        bind : {
            store : '{dictActionListStore}'
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
            text : 'Описание',
            dataIndex : 'description',
            sortable : true,
            width : 300
        }, {
            text : 'Дата начала',
            dataIndex : 'dateBegin',
            width : 100,
            sortable : true,
            format : 'd.m.Y',
            xtype : 'datecolumn'
        }, {
            text : 'Дата окончания',
            dataIndex : 'dateEnd',
            sortable : true,
            width : 100,
            format : 'd.m.Y',
            xtype : 'datecolumn'
        }, {
            text : 'Пакет предоставляемых услуг',
            dataIndex : 'dictServiceTypeName',
            sortable : true,
            width : 300
        }, {
            text : 'Тип акции',
            dataIndex : 'dictActionTypeName',
            sortable : true,
            width : 100
        }, {
            text : 'Для новых клиентов',
            dataIndex : 'forNewClients',
            sortable : true,
            width : 100,
            renderer : function (val) {
                if (val) return 'Да';
                return 'Нет';
            }
        }],
        tbar : [{
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
            text : 'Начать акцию',
            handler : 'onStartClick',
            disabled : true,
            reference : 'startButton'
        },/*{
            text : 'Список клиентов, учавствующих в акции',
            disabled : true,
            reference : 'linkClientDictActionListButton',
            handler : 'onLinkClientDictActionListClick'
        }, */{
            text : 'Экспорт',
            handler : 'onExportClick',
            disabled : true,
            reference : 'exportButton'
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
    } ]
});