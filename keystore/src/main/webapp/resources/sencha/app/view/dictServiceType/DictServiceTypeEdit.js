Ext.define('KeyStore.view.dictServiceType.DictServiceTypeEdit', {
    extend : 'Ext.window.Window',
    xtype : 'dictServiceTypeEdit',
    requires : [
            'KeyStore.view.dictServiceType.DictServiceTypeEditController',
            'KeyStore.view.dictServiceType.DictServiceTypeEditModel',
            'Ext.window.Window', 'Ext.form.field.Text',
            'Ext.form.field.Number', 'Ext.form.field.ComboBox',
            'Ext.form.field.Date', 'Ext.layout.container.HBox',
            'Ext.layout.container.Column' ],
    controller : 'dictServiceTypeEdit',
    viewModel : {
        type : 'dictServiceTypeEdit'
    },
    layout : 'fit',
    width : 620,
    height : 400,
    title : 'Пакет услуг',
    autoShow : true,
    modal : true,
    resizable:false,
    items : [ {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [{
            xtype : 'hidden',
            reference : 'id'
        }, {
            xtype : 'panel',
            layout : 'form',
            defaults : {
                labelWidth : 100,
                width : 280
            },
            width : 600,
            height : 180,
            items : [ {
                xtype : 'textfield',
                fieldLabel : 'Название',
                maxLength : 100,
                reference : 'name',
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'Описание',
                maxLength : 1024,
                reference : 'description',
                allowBlank : false
            }, {
                xtype : 'combo',
                fieldLabel : 'Статус',
                mode : 'local',
                valueField : 'code',
                displayField : 'name',
                triggerAction : 'all',
                editable : false,
                bind : {
                    store : '{dictServiceTypeStatusStore}'
                },
                reference : 'status',
                allowBlank : false
            }, {
                xtype : 'numberfield',
                fieldLabel : 'Полная стоимость услуг',
                allowBlank : false,
                reference : 'amount'
            }, {
                xtype : 'numberfield',
                fieldLabel : 'Стоимость 30 дней',
                allowBlank : false,
                reference : 'amount30Days'
            }]
        }, {
            xtype : 'label',
            text : '',
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