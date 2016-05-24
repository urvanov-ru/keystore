Ext.define('KeyStore.view.key.KeyFilter', {
    extend : 'Ext.window.Window',
    xtype : 'keyFilter',
    requires : [ 'Ext.window.Window',
            'KeyStore.view.key.KeyFilterController',
            'KeyStore.view.key.KeyFilterModel' ],
    controller : 'keyFilter',
    viewModel : {
        type : 'keyFilter'
    },
    autoShow : true,
    modal : true,
    width : 500,
    height : 240,
    title : 'Фильтр ключей',
    layout : 'fit',
    closable : true,
    items : [ {
        xtype : 'form',
        reference : 'form',
        items : [ {
            xtype : 'panel',
            layout : 'vbox',
            items : [ {
                xtype : 'panel',
                layout : 'form',
                width : 490,
                items : [ {
                    xtype : 'textfield',
                    fieldLabel : 'Название клиента',
                    reference : 'clientName'
                }, {
                    xtype : 'combo',
                    bind : {
                        store : '{keyStatusStore}'
                    },
                    mode : 'local',
                    triggerAction : 'all',
                    fieldLabel : 'Статус',
                    displayField : 'name',
                    valueField : 'code',
                    reference : 'status'
                }, {
                    xtype : 'datefield',
                    fieldLabel : 'Действует на дату',
                    reference : 'activeOnDate',
                    format : 'd.m.Y'
                } ]
            }]
        } ]
    } ],
    buttons : [ {
        text : 'Применить',
        handler : 'onAcceptClick'
    }, {
        text : 'Сбросить',
        handler : 'onResetClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    } ]
});