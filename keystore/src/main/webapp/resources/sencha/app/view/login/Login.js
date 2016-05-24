Ext.define('KeyStore.view.login.Login', {
    extend : 'Ext.window.Window',
    msgUsername : 'Логин',
    msgPassword : 'Пароль',
    msgEnter : 'Войти',
    msgSendPassword : 'Забыл',
    msgRegister : 'Регистрация',
    msgTitle : 'KeyStore: Вход в сервис',
    msgTypeUsername : 'Введите логин (e-mail)',
    msgTypePassword : 'Введите пароль',
    xtype : 'login',
    requires : [ 'KeyStore.view.login.LoginController', 'Ext.form.Panel',
                 'Ext.Img', 'Ext.panel.Panel' ],
    controller : 'login',
    initComponent : function(config) {
        Ext.apply(this, {closable : false,
                resizable : false,
                autoShow : true,
                standardSubmit : true,
                width :450,
                height : 350,
                header : {
                    title : this.msgTitle,
                    items : [{
                        xtype : 'image',
                        src : globalUtils.toUrl('/resources/image/standard/logo.png'),
                        width : 168,
                        height : 28
                    }]
                },
                layout : 'fit',

                items : [ {
                    xtype : 'form',
                    reference : 'form',
                    layout : 'vbox',
                    items : [ {
                        xtype : 'panel',
                        layout : 'hbox',
                        width : 450,
                        height : 30,
                        bodyPadding : 5,
                        items : [ {
                            xtype : 'label',
                            width : 80,
                            text : this.msgUsername + ':'

                        }, {
                            xtype : 'textfield',
                            allowBlank : false,
                            name : 'j_username',
                            reference : 'j_username',
                            emptyText : this.msgTypeUsername,
                            width : 350
                        } ]
                    }

                    , {
                        xtype : 'panel',
                        bodyPadding : 5,
                        layout : {
                            type : 'hbox'
                        },
                        width : 440,
                        items : [ {
                            xtype : 'label',
                            text : this.msgPassword + ':',
                            width : 80
                        }, {
                            xtype : 'textfield',
                            allowBlank : false,
                            inputType : 'password',
                            name : 'j_password',
                            emptyText : this.msgTypePassword,
                            width : 270
                        }, {
                            xtype : 'button',
                            text : this.msgSendPassword,
                            width : 80,
                            handler : 'onSendPassword'
                        } ]
                    }, {
                        xtype : 'panel',
                        width : 450,
                        height : 170,
                        reference : 'captcha',
                        layout : 'fit'
                    },
                    {
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
                    }  ]
                } ],
                buttons : [ {
                    text : this.msgRegister,
                    width : 100,
                    handler : 'onRegisterClick'
                }, {
                    xtype : 'component',
                    width : 220
                }, {
                    text : this.msgEnter,
                    width : 100,
                    formBind : true,
                    listeners : {
                        click : 'onLoginClick'
                    }
                // handler : function(self) {
                // loginFormPanel.getForm().submit({
                // url : document.getElementById('loginurlspan').innerHTML
                // });
                // }
                } ]});
        this.callParent(arguments);
    }
    
    
});