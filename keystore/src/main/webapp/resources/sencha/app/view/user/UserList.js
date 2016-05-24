Ext.define('KeyStore.view.user.UserList', {
    extend : 'Ext.panel.Panel',
    xtype : 'userList',
    requires : [ 'KeyStore.view.user.UserListController',
            'KeyStore.view.user.UserListModel', 'Ext.panel.Panel',
            'Ext.grid.Panel', 'Ext.grid.feature.Grouping' ],
    layout : 'fit',
    viewModel : {
        type : 'userList'
    },
    controller : 'userList',
    items : [ {
        xtype : 'grid',
        reference : 'grid',
        tbar : [{
            text : 'Добавить',
            reference : 'addButton',
            handler : 'onAddClick'
        }, {
            text : 'Изменить',
            reference : 'editButton',
            disabled : true,
            handler : 'onEditClick'
        }, {
            text : 'Блокировать',
            reference : 'blockButton',
            disabled : true,
            handler : 'onBlockClick'
        }, {
            text : 'Права доступа',
            reference : 'accessButton',
            disabled : true,
            handler : 'onAccessClick'
        },{
            text : 'Фильтры',
            handler : 'onFilterClick'
        }],
        bind : {
            store : '{userListStore}'
        },
        listeners : {
            render : 'onGridRender',
            selectionchange : 'onGridSelectionChange'
        },
        features : [ {
            ftype : 'grouping',
            groupHeaderTpl : 'Группировка: {name} ({rows.length})',
            startCollapsed : false
        } ],
        layout : 'fit',
        columns : [ {
            text : 'Логин',
            dataIndex : 'userName',
            sortable : true,
            width : 100
        }, {
            text : 'Полное имя',
            dataIndex : 'fullName',
            sortable : true,
            width : 300
        }, {
            text : 'Пол',
            dataIndex : 'sexName',
            sortable : true,
            width : 100
        }, {
            text : 'Телефон',
            dataIndex : 'phone',
            sortable : true,
            width : 80
        }, {
            text : 'Должность',
            dataIndex : 'post',
            sortable : true,
            width : 100
        }, {
            text : 'Дата рождения',
            dataIndex : 'birthdate',
            sortable : true,
            width : 100
        }, {
            text : 'Клиент',
            dataIndex : 'clientName',
            sortable : true,
            width : 300
        }, {
            text : 'Разрешён вход',
            dataIndex : 'enabled',
            renderer : function (val) {
                if (val) return 'Разрешён'; else return 'Закрыт';
            }
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