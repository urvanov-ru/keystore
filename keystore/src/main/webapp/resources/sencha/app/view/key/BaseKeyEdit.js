Ext.define('KeyStore.view.key.BaseKeyEdit', {
    extend : 'Ext.window.Window',
    xtype : 'baseKeyEdit',
    requires : [ 'KeyStore.view.key.BaseKeyEditController',
            'KeyStore.view.key.BaseKeyEditModel', 'Ext.form.Panel',
            'Ext.panel.Panel' ],
    controller : 'baseKeyEdit',
    viewModel : {
        type : 'baseKeyEdit'
    },
    width : 600,
    height : 400,
    title : 'Ключ',
    layout : 'fit',
    autoShow : true,
    modal : true,
    items : [ {
        reference : 'form',
        xtype : 'form',
        layout : 'vbox',
        items : [ {
            xtype : 'hidden',
            reference : 'dictServiceTypeId'
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 600,
            height : 30,
            padding : 5,
            items : [{
                xtype : 'label',
                text : 'Вид ключа',
                width : 100
            }, {
                xtype : 'textfield',
                width : 480,
                reference : 'kind'
            }]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 600,
            height : 30,
            padding : 5,
            items : [ {
                xtype : 'label',
                text : 'Период с',
                width : 100
            }, {
                xtype : 'datefield',
                width : 120,
                format : 'd.m.Y',
                reference : 'dateBegin',
                allowBlank : false,
                listeners : {
                    change : 'dateBeginChanged'
                }

            }, {
                xtype : 'label',
                text : 'по',
                width : 30
            }, {
                xtype : 'datefield',
                width : 120,
                format : 'd.m.Y',
                reference : 'dateEnd',
                allowBlank : false,
                listeners : {
                    change : 'dateEndChanged'
                }
            }, {
                xtype : 'label',
                text : 'Период',
                width : 100
            }, {
                xtype : 'numberfield',
                width : 100,
                allowBlank : false,
                reference : 'period'
            } ]
        }, {
            xtype : 'panel',
            width : 600,
            height : 30,
            layout : 'hbox',
            padding : 5,
            items : [{
                xtype : 'label',
                width : 100,
                text : 'Ключ актив.'
            }, {
                xtype : 'textfield',
                reference : 'password',
                width : 400
            }, {
                xtype : 'button',
                handler : 'onRegenPasswordClick',
                text : 'Перегенерировать',
                width : 80
            }]
        }, {
            xtype : 'panel',
            title : 'Доступные возможности',
            width : 600,
            height : 210,
            layout : 'vbox',
            items : [ {
                xtype : 'panel',
                width : 600,
                height : 90,
                layout : 'column',
                items : [ {
                    xtype : 'panel',
                    columnWidth : 0.45,
                    height : 200,
                    layout : 'form',
                    items : [ {
                        xtype : 'checkbox',
                        fieldLabel : 'Импорт из Ethnos',
                        reference : 'ethnosImportEnabled',
                        allowBlank : false
                    }, {
                        xtype : 'checkbox',
                        fieldLabel : 'Базы в Ethnos',
                        reference : 'ethnosExportEnabled',
                        allowBlank : false
                    }, {
                        xtype : 'checkbox',
                        fieldLabel : 'Базы в XLS',
                        reference : 'xlsEnabled',
                        allowBlank : false
                    } ]
                }, {
                    xtype : 'panel',
                    columnWidth : 0.45,
                    height : 200,
                    layout : 'form',
                    items : [ {
                        xtype : 'checkbox',
                        fieldLabel : 'Экспорт бумажных форм',
                        reference : 'officeEnabled',
                        allowBlank : false
                    }, {
                        xtype : 'checkbox',
                        fieldLabel : 'Базы в CSV',
                        reference : 'csvEnabled',
                        allowBlank : false
                    }, {
                        xtype : 'checkbox',
                        fieldLabel : 'Базы в XLSX',
                        reference : 'xlsxEnabled',
                        allowBlank : false
                    } ]
                } ]
            }, {
                xtype : 'panel',
                layout : 'hbox',
                height : 30,
                width : 600,
                padding : 5,
                items : [ {
                    xtype : 'label',
                    text : 'Лимит анкет в одной базе',
                    width : 200
                }, {
                    xtype : 'numberfield',
                    width : 100,
                    reference : 'questionnaireLimit',
                    allowBlank : false
                } ]
            }, {
                xtype : 'panel',
                layout : 'hbox',
                height : 30,
                width : 600,
                padding : 5,
                items : [ {
                    xtype : 'label',
                    text : 'Обработка анкет (Д/Н/М)',
                    width : 200
                }, {
                    xtype : 'numberfield',
                    width : 100,
                    reference : 'qLimitPerDay',
                    allowBlank : false
                }, {
                    xtype : 'numberfield',
                    width : 100,
                    reference : 'qLimitPerWeek',
                    allowBlank : false
                }, {
                    xtype : 'numberfield',
                    width : 100,
                    reference : 'qLimitPerMonth',
                    allowBlank : false
                } ]
            }, {
                xtype : 'panel',
                layout : 'hbox',
                width : 600,
                height : 30,
                padding : 5,
                items : [{
                    xtype : 'label',
                    width : 200,
                    text : 'Кол-во устройств'
                }, {
                    xtype : 'numberfield',
                    width : 100,
                    reference : 'devicesLimit'
                }]
            } ]
        }, {
            xtype : 'label',
            text : 'Загрузка...',
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