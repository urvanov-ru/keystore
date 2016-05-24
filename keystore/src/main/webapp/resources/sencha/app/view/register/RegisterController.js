Ext.define('KeyStore.view.register.RegisterController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.register',
    onRegisterClick : function (self) {

        var captchaiframe = document.getElementById('registercaptchaiframe');
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
            this.lookupReference('errorLabel').setText('Введите каптчу.');
            return;
        }
        this.submitForm();
    },
    submitForm : function () {
        var form = this.lookupReference('form');
        var errorLabel = this.lookupReference('errorLabel');
        errorLabel.setText('Пожалуйста, подождите...');
        var me = this;
        form.getForm().submit({
                    url : globalUtils.toUrl('/security/register'),
                    method : 'POST',
                    success : function(form, action) {
                        var obj = Ext.util.JSON
                                .decode(action.response.responseText);
                        if (obj.success) {
                            Ext.MessageBox.show({
                                title : 'Сообщение',
                                msg : 'Регистрация прошла успешно. '+
                                    'На ваш e-mail выслано письмо с ' +
                                    'кодами активации.',
                                buttons : Ext.MessageBox.OK,
                                icon : Ext.MessageBox.INFO,
                                fn : function() {
                                    me.getView().destroy();
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title : 'Ошибка',
                                msg : answer.message,
                                icon : Ext.MessageBox.ERROR,
                                buttons : Ext.MessageBox.OK
                            });
                        }
                        

                    },

                    failure : function(form, action) {
                        if (action.failureType == 'server') {
                            var obj = Ext.util.JSON
                                    .decode(action.response.responseText);
                            var reason = obj != null ? obj.errors != null ? obj.errors.reason : '' : '';
                            var code = obj != null ? obj.errors != null ? obj.errors.code : '' : '';
                            if (code == 'captcha') {
                                    reason = 'Неправильно введена каптча.';
                            }
                            if (!reason && obj.message && !obj.success) reason = obj.message; 
                            errorLabel.setText('Ошибка регистрации. ' + reason);
                            
                        } else {
                            errorLabel.setText('Сервер недоступен. ');

                        }
                        form = me.lookupReference('form');
                        me.refreshCaptcha();
                    }
                });
    }, 
    refreshCaptcha : function() {
        var me = this;
        
        var captcha = me.lookupReference('captcha');
        captcha.removeAll(true);
        var pnl = Ext.create('Ext.panel.Panel', {
            html : '<iframe id="registercaptchaiframe" src="'+globalUtils.toUrl('/security/captcha')+'" style="width:'+captcha.width+'px;height:'+captcha.height+'px;" seamless>iframe is not supported in your browser.</iframe>'
        });
        captcha.add(pnl);
    },
    onRender : function (self, eOpts) {
        this.refreshCaptcha();
    },
    onEnterClick : function(self) {
        this.getView().destroy();
    }
});