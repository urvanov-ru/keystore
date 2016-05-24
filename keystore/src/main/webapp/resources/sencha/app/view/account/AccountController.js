Ext.define('KeyStore.view.account.AccountController', {
    extend : 'Ext.app.ViewController',
    requires : ['Ext.window.MessageBox'],
    alias : 'controller.account',
    
    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var name = this.lookupReference('name').getValue();
        var clientType = this.lookupReference('clientType').getValue();
        var contactPersonName = this.lookupReference('contactPersonName').getValue();
        var contactPersonEmail = this.lookupReference('contactPersonEmail').getValue();
        var contactPersonPhone = this.lookupReference('contactPersonPhone').getValue();
        var juridicalPersonName = this.lookupReference('juridicalPersonName').getValue();
        var itn = this.lookupReference('itn').getValue();
        var iec = this.lookupReference('iec').getValue();
        
        var isServiceUser = false;
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            if (globalData.user.authorities[n] == 'ROLE_SERVICE')
                isServiceUser = true;
        }
        var juridicalPersonAddress = null;
        var bankName = null;
        var account = null;
        var corrAccount = null;
        var bic = null;
        if (isServiceUser) {
            juridicalPersonAddress = this.lookupReference('juridicalPersonAddress').getValue();
            bankName = this.lookupReference('bankName').getValue();
            account = this.lookupReference('account').getValue();
            corrAccount = this.lookupReference('corrAccount').getValue();
            bic = this.lookupReference('bic').getValue();
        }
        if (juridicalPersonName == '') juridicalPersonName = null;
        if (itn == '') itn = null;
        if (iec == '') iec = null;
        if (juridicalPersonAddress == '') juridicalPersonAddress = null;
        if (bankName == '') bankName = null;
        if (account == '') account = null;
        if (corrAccount == '') corrAccount = null;
        if (bic == '') bic = null;

        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/account/save',
            jsonData : {
                name : name,
                clientType : clientType,
                contactPersonName : contactPersonName,
                contactPersonEmail : contactPersonEmail,
                contactPersonPhone : contactPersonPhone,
                juridicalPersonName : juridicalPersonName,
                itn : itn,
                iec : iec,
                juridicalPersonAddress : juridicalPersonAddress,
                bankName : bankName,
                account : account,
                corrAccount : corrAccount,
                bic : bic
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Внимание',
                            msg : 'Сохранение прошло успешно.',
                            buttons : Ext.MessageBox.OK,
                            fn : function() {
                                controller.getView().destroy();
                            }
                        });
                    } else {
                        infoLabel.setText(answer.message);
                    }
                } catch (exception) {
                    if (console) console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка сохранения пакета.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения пакета.');
                    break;
                }
            }
        });
    },
    
    
    loadData : function (obj) {
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/account/edit',
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('name').setValue(answer.info.name);
                        controller.lookupReference('clientType').setValue(answer.info.clientType);
                        controller.lookupReference('contactPersonName').setValue(answer.info.contactPersonName);
                        controller.lookupReference('contactPersonEmail').setValue(answer.info.contactPersonEmail);
                        controller.lookupReference('contactPersonPhone').setValue(answer.info.contactPersonPhone);
                        controller.lookupReference('juridicalPersonName').setValue(answer.info.juridicalPersonName);
                        controller.lookupReference('itn').setValue(answer.info.itn);
                        controller.lookupReference('iec').setValue(answer.info.iec);
                        controller.lookupReference('uniqueId').setValue(answer.info.uniqueId);
                        var isServiceUser = false;
                        for (var n = 0; n < globalData.user.authorities.length; n++) {
                            if (globalData.user.authorities[n] == 'ROLE_SERVICE')
                                isServiceUser = true;
                        }
                        if (isServiceUser) {
                            controller.lookupReference('juridicalPersonAddress').setValue(answer.info.juridicalPersonAddress);
                            controller.lookupReference('bankName').setValue(answer.info.bankName);
                            controller.lookupReference('account').setValue(answer.info.account);
                            controller.lookupReference('corrAccount').setValue(answer.info.corrAccount);
                            controller.lookupReference('bic').setValue(answer.info.bic);
                        }
                    } else {
                        if (console) console.log('Ошибка загрузки данных.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки данных',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK,
                            fn : function () {
                                controller.getView().destroy();
                            }
                        });
                    }
                } catch (exception) {
                    if (console) console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка загрузки данных.');
                switch (xhr.status) {
                case 403:
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Доступ запрещён.',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка загрузки пользователя',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                }
            }
        });
    },
    
    onChangePasswordClick : function (self) {
        var controller = this;
        Ext.Msg.prompt('Смена пароля', 'Введите новый пароль клиента для доступа к API:', function(btn, text){
            if (btn == 'ok'){
                controller.changePassword(text);
            }
        });
    },
    
    changePassword : function (newPassword) {
        globalUtils.ajaxRequest({
            url : '/account/changePassword',
            params : {
                newPassword : newPassword
            },
            success : function(xhr, opts) {
                try {
                    var answer = Ext.decode(xhr.responseText);

                    if (answer.success) {
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : 'Операция выполнена успешно.',
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.INFO
                        });
                    } else {
                        if (console)
                            console.log(message);
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                } catch (exception) {
                    if (console)
                        console.log('Ошибка обработки ответа сервера');
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка обработки ответа от сервера.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Внутренняя ошибка сервера');
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
    
    onRender : function () {
        var controller = this;
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            if (globalData.user.authorities[n] == 'ROLE_SERVICE')
                controller.lookupReference('serviceFormPart').show();
        }
    }
});