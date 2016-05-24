Ext.define('KeyStore.view.register.Register', {
    extend : 'Ext.window.Window',
    xtype : 'register',
    requires : [ 'KeyStore.view.register.RegisterController',
            'KeyStore.view.register.RegisterModel', 'Ext.window.Window',
            'Ext.form.Panel' ],
    width : 470,
    height : 520,
    modal : true,
    resizable : false,
    header : {
        title : 'KeyStore: Регистрация',
        items : [{
            xtype : 'image',
            src : globalUtils.toUrl('/resources/image/standard/logo.png'),
            width : 168,
            height : 28
        }]
    },
    layout : 'vbox',
    autoShow : true,
    viewModel : {
        type : 'register'
    },
    controller : 'register',

    items : [ {
        xtype : 'form',
        reference : 'form',
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
                name : 'clientType',
                hiddenName : 'clientType',
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'Контактное лицо',
                name : 'contactPersonName',
                maxLength : 255,
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'E-mail',
                name : 'contactPersonEmail',
                maxLength : 50,
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'Телефон',
                name : 'contactPersonPhone',
                maxLength : 50,
                allowBlank : false
            }, {
                xtype : 'textfield',
                inputType : 'password',
                name : 'password1',
                fieldLabel : 'Пароль',
                maxLength : 50,
                allowBlank : false
            }, {
                xtype : 'textfield',
                inputType : 'password',
                name : 'password2',
                fieldLabel : 'Ещё раз пароль',
                maxLength : 50,
                allowBlank : false
            } ]
        }, {
            xtype : 'panel',
            width : 350,
            height : 170,
            reference : 'captcha',
            layout : 'fit'
        }, {
            xtype : 'label',
            reference : 'errorLabel'
        }, {
            name : 'recaptcha_response_field',
            xtype : 'hidden',
            reference : 'recaptcha_response_field'
        }, {
            name : 'recaptcha_challenge_field',
            xtype : 'hidden',
            reference : 'recaptcha_challenge_field'
        } ]
    } ],
    buttons : [{
        text : 'Войти',
        handler : 'onEnterClick'
    }, {
        text : 'Зарегистрироваться',
        handler : 'onRegisterClick'
    } ],
    listeners : {
        render : 'onRender'
    }
});