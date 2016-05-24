Ext.define('KeyStore.view.client.ClientEdit', {
    extend : 'Ext.window.Window',
    xtype : 'clientEdit',
    requires : [ 'KeyStore.view.client.ClientEditController',
            'KeyStore.view.client.ClientEditModel', 'Ext.window.Window',
            'Ext.panel.Panel' ],
    width : 600,
    height : 550,
    modal : true,
    title : 'Карточка клиента',
    layout : 'fit',
    autoShow : true,
    viewModel : {
        type : 'clientEdit'
    },
    controller : 'clientEdit',

    items : [ {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [ {
            xtype : 'panel',
            layout : 'form',
            width : '100%',
            defaults : {
                width : 350,
                labelWidth : 100
            },
            items : [ {
                xtype : 'hidden',
                reference : 'id'
            }, {
                xtype : 'textfield',
                fieldLabel : 'Название клиента',
                reference : 'name',
                maxLength : 1024,
                allowBlank : false
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
                reference : 'clientType',
                hiddenName : 'clientType',
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'Контактное лицо',
                reference : 'contactPersonName',
                maxLength : 255,
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'E-mail',
                reference : 'contactPersonEmail',
                maxLength : 50,
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'Телефон',
                reference : 'contactPersonPhone',
                maxLength : 50,
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'Название юр. лица',
                reference : 'juridicalPersonName',
                maxLength : 1024
            }, {
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
            }, {
                xtype : 'textfield',
                fieldLabel : 'ИНН',
                reference : 'itn',
                maxLength : 12
            }, {
                xtype : 'textfield',
                fieldLabel : 'КПП',
                reference : 'iec',
                maxLength : 9
            }]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 600,
            height : 30,
            padding : '2 2 2 2',
            items : [{
                xtype : 'hidden',
                reference : 'dictClientGroupId'
            }, {
                xtype : 'label',
                width : 200,
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
            xtype : 'label',
            reference : 'infoLabel',
            text : ''
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