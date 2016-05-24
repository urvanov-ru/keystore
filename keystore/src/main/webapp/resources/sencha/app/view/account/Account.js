Ext.define('KeyStore.view.account.Account', {
    extend : 'Ext.panel.Panel',
    requires : [ 'KeyStore.view.account.AccountController',
            'KeyStore.view.account.AccountModel', 'Ext.panel.Panel',
            'Ext.form.field.Text', 'Ext.form.field.ComboBox' ],
    xtype : 'account',
    viewModel : {
        type : 'account'
    },
    controller : 'account',
    autoScroll : true,
    layout : 'vbox',
    width : '100%',
    items : [{
        xtype : 'panel',
        height : 30
    }, {
        xtype : 'button',
        text : 'Сохранить',
        reference : 'saveButton',
        handler : 'onSaveClick'
    }, {
        xtype : 'label',
        reference : 'infoLabel',
        height : 30
    }, {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        width : '100%',
        items : [ {
            xtype : 'panel',
            layout : 'form',

            width : 460,
            defaults : {
                width : 350,
                labelWidth : 100
            },
            items : [ {
                xtype : 'textfield',
                fieldLabel : 'Название клиента',
                name : 'name',
                maxLength : 1024,
                allowBlank : false,
                reference : 'name'
            }, {
                xtype : 'combo',
                fieldLabel : 'Вид клиента',
                mode : 'local',
                editable : false,
                triggerAction : 'all',
                displayField : 'name',
                valueField : 'code',
                bind : {
                    store : '{clientTypeStore}'
                },
                name : 'clientType',
                hiddenName : 'clientType',
                allowBlank : false,
                reference : 'clientType'
            }, {
                xtype : 'textfield',
                fieldLabel : 'Контактное лицо',
                name : 'contactPersonName',
                maxLength : 255,
                allowBlank : false,
                reference : 'contactPersonName'
            }, {
                xtype : 'textfield',
                fieldLabel : 'E-mail',
                name : 'contactPersonEmail',
                maxLength : 50,
                allowBlank : false,
                reference : 'contactPersonEmail'
            }, {
                xtype : 'textfield',
                fieldLabel : 'Телефон',
                name : 'contactPersonPhone',
                maxLength : 50,
                allowBlank : false,
                reference : 'contactPersonPhone'
            }, {
                xtype : 'textfield',
                fieldLabel : 'Название юр. лица',
                name : 'juridicalPersonName',
                reference : 'juridicalPersonName'
            }, {
                xtype : 'textfield',
                fieldLabel : 'ИНН',
                name : 'itn',
                maxLength : 12,
                reference : 'itn'
            }, {
                xtype : 'textfield',
                fieldLabel : 'КПП',
                name : 'iec',
                maxLength : 9,
                reference : 'iec'
            }]
        }, {
            xtype : 'panel',
            layout : 'form',
            reference : 'serviceFormPart',
            width : 460,
            hidden : true,
            defaults : {
                width : 350,
                labelWidth : 100
            },
            items : [{
                xtype : 'textfield',
                fieldLabel : 'Юридический адрес',
                reference : 'juridicalPersonAddress',
                maxLength : 1024
            }, {
                xtype : 'textfield',
                fieldLabel : 'Название банка',
                reference : 'bankName',
                maxLength : 1024
            }, {
                xtype : 'textfield',
                fieldLabel : 'Расчётный счёт',
                reference : 'account',
                maxLength : 20
            }, {
                xtype : 'textfield',
                fieldLabel : 'К/c',
                maxLength : 20,
                reference : 'corrAccount'
            }, {
                xtype : 'textfield',
                fieldLabel  : 'БИК',
                reference : 'bic',
                maxLength : 9
            } ]
        }, {
            xtype : 'panel',
            layout : 'form',
            width : 460,
            defaults : {
                width : 350,
                labelWidth : 100
            },
            items : [{
                xtype : 'textfield',
                disabled : true,
                fieldLabel : 'Уникальный ID',
                reference : 'uniqueId'
            }]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : '100%',
            height : 30,
            items : [{
                xtype : 'button',
                handler : 'onChangePasswordClick',
                reference : 'changePassword',
                text : 'Сменить пароль клиента для доступа к API'
            }]
        }]
    } ],
    listeners : {
        render : 'onRender'
    }
});