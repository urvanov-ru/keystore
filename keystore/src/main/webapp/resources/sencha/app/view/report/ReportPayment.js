Ext.define('KeyStore.view.report.ReportPayment', {
    extend : 'Ext.panel.Panel',
    xtype : 'reportPayment',
    requires : [ 'KeyStore.view.report.ReportPaymentController',
            'KeyStore.view.report.ReportPaymentModel','Ext.panel.Panel',
            'Ext.form.field.Date', 'Ext.form.Label',
            'Ext.form.field.ComboBox', 'Ext.button.Button', 
            'Ext.form.field.Display', 'Ext.form.field.Hidden' ],
    controller : 'reportPayment',
    viewModel : {
        type : 'reportPayment'
    },
    layout : 'fit',
    
    items : [{
        xtype : 'form',
        layout : 'vbox',
        reference : 'form',
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
            xtype : 'label',
            reference : 'infoLabel'
        }, {
            xtype : 'panel',
            width : '100%',
            height : 30,
            layout : 'hbox',
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