Ext.define('KeyStore.view.key.KeyList', {
    extend : 'Ext.panel.Panel',
    xtype : 'keyList',
    requires : [ 'KeyStore.view.key.KeyListController',
            'KeyStore.view.key.KeyListModel', 'Ext.panel.Panel',
            'Ext.grid.Panel', 'Ext.grid.feature.Grouping' ],
    layout : {type:'fit'},
    viewModel : {
        type : 'keyList'
    },
    controller : 'keyList',
    items : [ {
        xtype : 'panel',
        layout : {
            type : 'border'
        },
        items : [ {
            xtype : 'grid',
            region : 'center',
            reference : 'grid',
            bind : {
                store : '{keyListStore}'
            },
            tbar : [{
                text : 'Активировать',
                handler : 'onActivateClick',
                reference : 'activateButton',
                disabled : true
            }, {
                text : 'Скачать',
                handler : 'onDownloadClick',
                reference : 'downloadButton',
                disabled : true
            }, {
                text : 'Удалить',
                disabled : true,
                reference : 'deleteButton',
                handler : 'onDeleteClick'
            }, {
                text : 'Фильтры',
                hidden : true,
                handler : 'onFilterClick',
                reference : 'filterButton'
            } ],
            features : [ {
                ftype : 'grouping',
                groupHeaderTpl : 'Группировка: {name} ({rows.length})',
                startCollapsed : true
            } ],
            layout : 'fit',
            columns : [ {
                text : 'Пакет услуг',
                dataIndex : 'dictServiceTypeName',
                sortable : true,
                width : 300
            }, {
                text : 'Основание',
                dataIndex : 'basis',
                sortable : true,
                width : 300
            }, {
                text : 'Статус ключа',
                dataIndex : 'statusName',
                sortable : true
            }, {
                text : 'Действителен с',
                dataIndex : 'dateBegin',
                xtype : 'datecolumn',
                sortable : true,
                format : 'd.m.Y',
                width : 100
            }, {
                text : 'Действителен по',
                dataIndex : 'dateEnd',
                sortable : true,
                xtype : 'datecolumn',
                format : 'd.m.Y',
                width : 100
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
            } ]
        }  ]
    }],
    listeners : {
        render : 'onRender'
    }
});