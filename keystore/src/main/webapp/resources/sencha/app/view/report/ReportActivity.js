Ext.define('KeyStore.view.report.ReportActivity', {
    extend : 'Ext.panel.Panel',
    xtype : 'reportActivity',
    requires : [ 'KeyStore.view.report.ReportActivityController',
            'KeyStore.view.report.ReportActivityModel','Ext.panel.Panel',
            'Ext.form.field.Date', 'Ext.form.Label',
            'Ext.form.field.ComboBox', 'Ext.button.Button', 
            'Ext.form.field.Display', 'Ext.form.field.Hidden',
            'Ext.chart.CartesianChart'],
    controller : 'reportActivity',
    viewModel : {
        type : 'reportActivity'
    },
    layout : 'fit',
    items : [{
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [{
            xtype : 'panel',
            layout : 'form',
            width : 300,
            height : 100,
            defaults : {
                labelWidth : 100
            },
            items : [{
                xtype : 'datefield',
                fieldLabel : 'Период с',
                format : 'd.m.Y',
                reference : 'dateBegin',
                allowBlank : false,
                width : 100
            }, {
                xtype : 'datefield',
                fieldLabel : 'по',
                format : 'd.m.Y',
                reference : 'dateEnd',
                allowBlank : false,
                width : 100
            }, {
                xtype : 'combobox',
                reference : 'reportMode',
                mode : 'local',
                valueField : 'id',
                displayField : 'name',
                triggerAction : 'all',
                bind : {
                    store : '{reportModesStore}'
                },
                fieldLabel : 'Группировка',
                editable : false,
                allowBlank : false,
                width : 200
            }]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 500,
            height : 25,
            padding : '0 5 0 5',
            items : [{
                xtype : 'hidden',
                reference : 'clientId'
            }, {
                xtype : 'label',
                width : 90,
                text : 'Клиент'
            },{
                xtype : 'displayfield',
                reference : 'clientName',
                width : 300,
                value : '...'
            }, {
                xtype : 'button',
                text : 'Выбрать',
                width : 100,
                handler : 'onSelectClientClick'
            }]
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        }, {
            xtype : 'panel',
            width : '100%',
            layout : 'hbox',
            height : 30,
            padding : '0 5 0 5',
            items : [{
                xtype : 'button',
                text : 'Генерировать отчёт в Excel',
                reference : 'generateButton',
                handler : 'onGenerateClick',
                margin : '0 5 0 5'
            }, {
                xtype : 'button',
                text : 'Показать график',
                reference : 'showChartButton',
                handler : 'onShowChartClick',
                margin : '0 5 0 5'
            }]
        }]
    }]
});