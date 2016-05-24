Ext.define('KeyStore.view.changePassword.ChangePasswordController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.changePassword',
    onChangePasswordClick : function(self) {
        var form = this.lookupReference('form');
        var me = this;
        var errorLabel = this.lookupReference('errorLabel');
        var params = globalUtils.parseGetParams();
        var key = params.key;
        var id = params.id;
        if (form.getForm().isValid()) {
            errorLabel.setText('');
            var newPassword = this.lookupReference('newPassword').getValue();
            globalUtils.ajaxRequest({
                waitMsg : 'Пожалуйста, подождите...',
                url : '/security/changepassword',
                params : {
                    newPassword : newPassword,
                    key : key,
                    id : id
                },
                success : function(xhr) {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Успешно',
                            msg : 'Смена пароля прошла успешно',
                            buttons : Ext.MessageBox.OK,
                            fn : function () {
                                me.getView().destroy();
                                Ext.widget('login');
                            }
                        });
                    } else {
                        errorLabel.setText('Ошибка. ' + answer.message);
                    }
                },
                failure : function(xhr) {
                    if (console) console.log('Ошибка смены пароля.');
                    
                    switch (xhr.status) {
                    case 403:
                        errorLabel.setText('Доступ запрещён.');
                        break;
                    default :
                        errorLabel.setText('Ошибка смены пароля.');
                        break;
                    }
                }
            });
        } else {
            errorLabel.setText('Проверьте правильность заполнения формы.');
        }
    },
    
    onAfterRender : function (self, eOpts) {
        
        
    }
});