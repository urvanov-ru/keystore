Ext.define('KeyStore.view.user.UserEdit', {
    extend : 'Ext.window.Window',
    xtype : 'userEdit',
    requires : [ 'KeyStore.view.user.UserEditController',
            'KeyStore.view.user.UserEditModel', 'Ext.window.Window',
            'Ext.form.field.ComboBox', 'Ext.button.Button', 'Ext.form.Label',
            'Ext.form.field.Text', 'Ext.window.MessageBox'],
    controller : 'userEdit',
    viewModel : {
        type : 'userEdit'
    },
    modal : true,
    autoShow : true,
    width : 600,
    height : 300,
    title : 'Пользователь',
    layout : 'fit',
    items : [{
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [{
            xtype : 'panel',
            layout : 'form',
            width : '100%',
            items : [{
                xtype : 'hidden',
                reference : 'id'
            },{
                xtype : 'textfield',
                fieldLabel : 'ФИО',
                reference : 'fullName'
            }, {
                xtype : 'combo',
                valueField : 'code',
                displayField : 'name',
                fieldLabel : 'Пол',
                bind : {
                    store : '{sexStore}'
                },
                editable : false,
                forceSelection : true,
                queryMode : 'local',
                triggerAction : 'all',
                reference : 'sex'
            }, {
                xtype : 'textfield',
                fieldLabel : 'e-mail',
                reference : 'userName'
            }, {
                xtype : 'textfield',
                fieldLabel : 'Телефон',
                reference : 'phone'
            }, {
                xtype : 'textfield',
                fieldLabel : 'Должность',
                reference : 'post'
            }, {
                xtype : 'datefield',
                fieldLabel : 'День рождения',
                format : 'd.m.Y',
                reference : 'birthdate'
            }]
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }],
    buttons : [{
        xtype : 'button',
        text : 'Сохранить',
        handler : 'onSaveClick'
    }, {
        xtype : 'button',
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});