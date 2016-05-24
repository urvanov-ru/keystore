Ext.define('KeyStore.view.order.OrderEdit', {
    extend : 'Ext.window.Window',
    xtype : 'orderEdit',
    requires : [ 'KeyStore.view.order.OrderEditController',
            'KeyStore.view.order.OrderEditModel' ],
    controller : 'orderEdit',
    viewModel : {
        type : 'orderEdit'
    },
    autoShow : true,
    modal : true,
    width : 600,
    height : 200,
    title : 'Заказ',
    layout : 'fit',
    items : [ {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [ {
            xtype : 'hidden',
            reference : 'id'
        }, {
            xtype : 'panel',
            width : 600,
            height : 75,
            layout : 'vbox',
            items : [
            // {
            // xtype : 'combo',
            // queryMode : 'remote',
            // displayField : 'name',
            // valueField : 'id',
            // forceSelection : true,
            // typeAhead : true,
            // fieldLabel : 'Пакет услуг',
            // bind : {
            // store : '{dictServiceTypeStore}'
            // },
            // reference : 'dictServiceType',
            // allowBlank : false
            // },
            {
                xtype : 'panel',
                layout : 'hbox',
                width : 600,
                height : 30,
                items : [ {
                    xtype : 'label',
                    text : 'Пакет услуг',
                    padding : 5,
                    width : 160
                }, {
                    xtype : 'hidden',
                    reference : 'dictServiceTypeId'
                }, {
                    xtype : 'label',
                    text : '...',
                    width : 300,
                    reference : 'dictServiceTypeName'
                }, {
                    xtype : 'button',
                    text : 'Выбрать',
                    width : 100,
                    height : 25,
                    handler : 'onDictServiceTypeSelectClick'
                } ]
            }, {
                xtype : 'panel',
                layout : 'form',
                defaults : {
                    labelWidth : 150
                },
                width : 600,
                height : 30,
                items : [ {
                    xtype : 'combo',
                    queryMode : 'local',
                    displayField : 'name',
                    valueField : 'code',
                    editable : false,
                    triggerAction : 'all',
                    fieldLabel : 'Режим активации ключа',
                    bind : {
                        store : '{keyActivationModeStore}'
                    },
                    reference : 'keyActivationMode',
                    allowBlank : false
                } ]
            } ]
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        } ]
    } ],
    buttons : [ {
        text : 'Сохранить',
        handler : 'onSaveClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    } ]
});