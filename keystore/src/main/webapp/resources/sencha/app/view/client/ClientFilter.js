Ext.define('KeyStore.view.client.ClientFilter', {
    extend : 'Ext.window.Window',
    xtype : 'clientFilter',
    requires : [ 'Ext.window.Window',
            'KeyStore.view.client.ClientFilterController',
            'KeyStore.view.client.ClientFilterModel' ],
    controller : 'clientFilter',
    viewModel : {
        type : 'clientFilter'
    },
    autoShow : true,
    modal : true,
    width : 500,
    height : 240,
    title : 'Фильтр клиентов',
    layout : 'fit',
    closable : true,
    items : [ {
        xtype : 'form',
        layout : 'vbox',
        reference : 'form',
        items : [ {
            xtype : 'panel',
            layout : 'form',
            width : '100%',
            defaults : {
                labelWidth : 100
            },
            items : [ {
                xtype : 'combo',
                fieldLabel : 'Статус',
                reference : 'active',
                bind : {
                    store : '{activeStore}'
                },
                editable : false,
                forceSelection : true,
                queryMode : 'local',
                triggerAction : 'all',
                valueField : 'code',
                displayField : 'name'
            } ]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 500,
            height : 30,
            padding : '2 2 2 2',
            items : [{
                xtype : 'hidden',
                reference : 'dictClientGroupId'
            }, {
                xtype : 'label',
                width : 100,
                text : 'Группа',
                autoWidth : false
            }, {
                xtype : 'label',
                width : 300,
                autoWidth : false,
                text : '...',
                reference : 'dictClientGroupName'
            }, {
                xtype : 'button',
                text : 'Выбрать',
                handler : 'onDictClientGroupSelectClick'
            }]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 490,
            items : [ {
                xtype : 'datefield',
                fieldLabel : 'Активность с',
                reference : 'activeBegin',
                format : 'd.m.Y'
            }, {
                xtype : 'datefield',
                fieldLabel : 'по',
                labelWidth : 30,
                reference : 'activeEnd',
                format : 'd.m.Y'
            } ]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 500,
            height : 30,
            padding : '2 2 2 2',
            items : [{
                xtype : 'hidden',
                reference : 'dictActionId'
            }, {
                xtype : 'label',
                width : 150,
                text : 'Учавствует в акции',
                autoWidth : false
            }, {
                xtype : 'label',
                width : 250,
                autoWidth : false,
                text : '...',
                reference : 'dictActionName'
            }, {
                xtype : 'button',
                text : 'Выбрать',
                handler : 'onDictActionSelectClick'
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