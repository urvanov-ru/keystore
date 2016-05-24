Ext.define('KeyStore.view.settings.GlobalSettings', {
    extend : 'Ext.panel.Panel',
    xtype : 'globalSettings',
    requires : [ 'KeyStore.view.settings.GlobalSettingsController',
            'KeyStore.view.settings.GlobalSettingsModel', 'Ext.panel.Panel',
            'Ext.grid.Panel', 'Ext.grid.feature.Grouping',
            'Ext.form.field.Text', 'Ext.form.field.ComboBox' ],
    layout : 'fit',
    viewModel : {
        type : 'globalSettings'
    },
    controller : 'globalSettings',
    layout : 'fit',
    items : [ {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [ {
            xtype : 'panel',
            height : 30
        }, {
            xtype : 'button',
            text : 'Сохранить',
            handler : 'onSaveClick'
        }, {
            xtype : 'label',
            reference : 'infoLabel',
            text : 'Сообщение об ошибке сохранения',
            height : 30
        }, {
            xtype : 'panel',
            layout : 'form',
            width : '100%',
            height : 100,
            defaults : {
                labelWidth : 400,
                width : 200
            },
            items : [{
                xtype : 'panel',
                layout : 'hbox',
                width : 600,
                height : 30,
                padding : '2 2 2 2',
                items : [ {
                    xtype : 'hidden',
                    reference : 'dictClientGroupId'
                }, {
                    xtype : 'label',
                    width : 200,
                    text : 'Родительская группа',
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
                } ]
            },
            {
                xtype : 'numberfield',
                fieldLabel : 'Количество дней, в течении '
                    + 'которых хранить информацию о '
                    + 'сессиях пользователей',
                    reference : 'sessionStoreDays'
            } ]
        } ]
    } ]
});