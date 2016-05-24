Ext.define('KeyStore.view.changePassword.ChangePassword', {
    extend : 'Ext.window.Window',
    xtype : 'changePassword',
    requires : [ 'KeyStore.view.changePassword.ChangePasswordController',
            'KeyStore.view.changePassword.ChangePasswordModel' ],
    controller : 'changePassword',
    viewModel : {
        type : 'changePassword'
    },
    width : 320,
    height : 240,
    layout : 'vbox',
    autoShow : true,
    items : [{
        xtype : 'form',
        width :'100%',
        reference : 'form',
        defaults : {
            labelWidth : 100
        },
        items : [{
            xtype : 'textfield',
            fieldLabel : 'Новый пароль',
            reference : 'newPassword',
            inputType : 'password',
            allowBlank : false
        }]
    }, {
        xtype : 'label',
        reference : 'errorLabel'
    }],
    buttons : [{
        text : 'Сменить пароль',
        handler : 'onChangePasswordClick'
    }],
    listeners : {
        afterrender : 'onAfterRender'
    }
});