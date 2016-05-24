Ext.define('KeyStore.view.login.LoginController', {
    extend : 'Ext.app.ViewController',

    requires : [ 'Ext.window.MessageBox' ],

    alias : 'controller.login',
    onLoginClick : function() {

        var form = this.lookupReference('form');
        var errorLabel = this.lookupReference('errorLabel');
        var me = this;
        var controller = this;
        var captchaiframe = document.getElementById('captchaiframe');
        if (captchaiframe) {
            var recaptcha_response_field = captchaiframe.contentWindow.document.getElementById('recaptcha_response_field').value;
            var recaptcha_challenge_field = captchaiframe.contentWindow.document.getElementById('recaptcha_challenge_field').value;
            if (recaptcha_response_field && recaptcha_challenge_field) {
                this.lookupReference("recaptcha_response_field").setValue(recaptcha_response_field);
                this.lookupReference("recaptcha_challenge_field").setValue(recaptcha_challenge_field);
            } else {
                this.lookupReference('errorLabel').setText('Введите каптчу.');
                return;
            }
        } else {
            this.lookupReference("recaptcha_response_field").setValue('');
            this.lookupReference("recaptcha_challenge_field").setValue('');
        }
        form.getForm().submit(
                {
                    url : globalUtils.toRootUrl('/j_spring_security_check'),
                    method : 'POST',
                    success : function() {
                        me.getView().destroy();
                        Ext.widget('app-main');

                    },

                    failure : function(form, action) {
                        var code = '';
                        if (action.failureType == 'server') {
                            obj = Ext.util.JSON
                                    .decode(action.response.responseText);
                            var reason = obj != null ? obj.errors != null ? obj.errors.reason : '' : '';
                            code = obj != null ? obj.errors != null ? obj.errors.code : '' : '';
                            if (code == 'usernamepassword') {
                                reason = 'E-mail или пароль введены неверно.';
                            } else if (code == 'captcha') {
                                    reason = 'Неправильно введена каптча.';
                            } else if (code == 'notactivated') {
                                reason = "Ваша учётная запись не активирована.";
                            } else if (code == "disabled") {
                                reason = "Доступ для этого пользователя закрыт.";
                            } else if (code == 'internalerror') {
                                reason = "Внутренняя ошибка сервера.";
                            } else {
                                reason = "Неопознанная ошибка.";
                            }
                            errorLabel.setText('Ошибка входа. ' + reason);
                        } else {
                            errorLabel.setText('Сервер недоступен.');

                        }
                        
                        var userName = controller.lookupReference('j_username').getValue();
                        if (code == 'notactivated') {
                            Ext.MessageBox.show({
                                title : 'Внимание',
                                msg : 'Ваша учётная запись не активирована. Выслать письмо активации повторно?',
                                buttons : Ext.MessageBox.YESNO,
                                fn : function (buttonId) {
                                    if (buttonId == "yes") {
                                        controller.sendActivationLetter(userName);
                                    }
                                }
                            });
                        }
                        form = me.lookupReference('form');
                        form.getForm().reset();
                        me.refreshCaptcha();
                    }
                });
    },
    onRegisterClick : function (self) {
        Ext.widget('register');
    },
    refreshCaptcha : function() {
        var me = this;
        
        var captcha = me.lookupReference('captcha');
        captcha.removeAll(true);
        var pnl = Ext.create('Ext.panel.Panel', {
            html : '<iframe id="captchaiframe" src="'+globalUtils.toUrl('/security/captcha')+'" style="width:'+captcha.getWidth()+'px;height:'+captcha.getHeight()+'px;" seamless>iframe is not supported in your browser.</iframe>'
        });
        captcha.add(pnl);
    },
    
    onSendPassword : function (self) {
        var j_username = this.lookupReference('j_username');
        var userName = j_username.getValue();
        if (!userName)
        {
            Ext.MessageBox.show({
                title : 'Внимание',
                msg : 'Введите e-mail.',
                buttons : Ext.MessageBox.OK,
                icon : Ext.MessageBox.ERROR
            });
            return;
        }
        globalUtils.ajaxRequest({
            url : '/security/sendpassword',
            params : {
                userName : userName
            },
            success : function(xhr) {
                var answer = Ext.decode(xhr.responseText);
                if (answer.failure) {
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Внутрення ошибка сервера',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                } else {
                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Сообщение',
                            buttons : Ext.MessageBox.OK,
                            msg : 'На e-mail ' + userName + ' отправлено письмо' +
                            ' с инструкциями.'
                        });
                    } else {
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            buttons : Ext.MessageBox.OK,
                            msg : answer.message,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                }
            },
            failure : function(xhr) {
                switch (xhr.status) {
                case 403:
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Доступ запрещён.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка выполнения операции.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                }
            }
        });
    },
    sendActivationLetter : function(userName) {
        globalUtils.ajaxRequest({
            url : '/security/repeatActivationLetter',
            params : {
                userName : userName
            },
            success : function(xhr, opts) {
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Сообщение',
                            msg : 'Письмо с инструкциями по активации выслано повторно на e-mail '
                                + userName + '. Дождитесь его получения.',
                            buttons : Ext.MessageBox.OK
                        });
                    } else {
                        if (console) console.log('Ошибка отправки письма с инструкциями активации.');
                        Ext.MessageBox.show({
                            title : 'Ошибка отправки письма с инструкциями активации.',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK
                        });
                    }
                } catch (exception) {
                    if (console) console.log(exception);
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка отправки письма с инструкциями активации.',
                        buttons : Ext.MessageBox.OK
                    });
                }
            },
            failure : function(xhr, opts) {
                if (console) console.log('Ошибка отправки письма с инструкциями по активации.');
                switch (xhr.status) {
                case 403:
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Доступ запрещён.',
                        buttons : Ext.MessageBox.OK
                    });
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка отправки письма с инструкциями по активации.',
                        buttons : Ext.MessageBox.OK
                    });
                    break;
                }
            }
        });
    }
});
