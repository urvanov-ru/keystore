Ext.define('KeyStore.view.client.ClientEditController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.clientEdit',
    id : 'clientEdit',

    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        var dictClientGroupId = this.lookupReference('dictClientGroupId').getValue();
        if (!form.getForm().isValid() || !dictClientGroupId) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var id = this.lookupReference('id').getValue();
        var name = this.lookupReference('name').getValue();
        var clientType = this.lookupReference('clientType').getValue();
        var contactPersonName = this.lookupReference('contactPersonName').getValue();
        var contactPersonEmail = this.lookupReference('contactPersonEmail').getValue();
        var contactPersonPhone = this.lookupReference('contactPersonPhone').getValue();
        var juridicalPersonName = this.lookupReference('juridicalPersonName').getValue();
        var itn = this.lookupReference('itn').getValue();
        var iec = this.lookupReference('iec').getValue();
        var juridicalPersonAddress = this.lookupReference('juridicalPersonAddress').getValue();
        var bankName = this.lookupReference('bankName').getValue();
        var account = this.lookupReference('account').getValue();
        var corrAccount = this.lookupReference('corrAccount').getValue();
        var bic = this.lookupReference('bic').getValue();
        if (itn == '') itn = null;
        if (iec == '') iec = null;
        if (juridicalPersonAddress == '') juridicalPersonAddress = null;
        if (bankName == '') bankName = null;
        if (account == '') account = null;
        if (corrAccount == '') corrAccount = null;
        if (bic == '') bic = null;
        
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/client/save',
            jsonData : {
                id : id,
                name : name,
                clientType : clientType,
                contactPersonName : contactPersonName,
                contactPersonEmail : contactPersonEmail,
                contactPersonPhone : contactPersonPhone,
                juridicalPersonName : juridicalPersonName,
                itn : itn,
                iec : iec,
                dictClientGroupId : dictClientGroupId,
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
                                controller.fireEvent('save');
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
                    console.log('Ошибка сохранения клиента.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения клиента.');
                    break;
                }
            }
        });
    },

    onCancelClick : function(self) {
        this.getView().destroy();
    },

    loadData : function(obj) {
        var id = obj.id;
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/client/edit',
            params : {
                id : id
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('id').setValue(id);
                        controller.lookupReference('name').setValue(answer.info.name);
                        controller.lookupReference('clientType').setValue(answer.info.clientType);
                        controller.lookupReference('contactPersonName').setValue(answer.info.contactPersonName);
                        controller.lookupReference('contactPersonEmail').setValue(answer.info.contactPersonEmail);
                        controller.lookupReference('contactPersonPhone').setValue(answer.info.contactPersonPhone);
                        controller.lookupReference('juridicalPersonName').setValue(answer.info.juridicalPersonName);
                        controller.lookupReference('itn').setValue(answer.info.itn);
                        controller.lookupReference('iec').setValue(answer.info.iec);
                        controller.lookupReference('dictClientGroupId').setValue(answer.info.dictClientGroupId);
                        controller.lookupReference('dictClientGroupName').setText(answer.info.dictClientGroupName);
                        controller.lookupReference('juridicalPersonAddress').setValue(answer.info.juridicalPersonAddress);
                        controller.lookupReference('bankName').setValue(answer.info.bankName);
                        controller.lookupReference('account').setValue(answer.info.account);
                        controller.lookupReference('corrAccount').setValue(answer.info.corrAccount);
                        controller.lookupReference('bic').setValue(answer.info.bic);
                    } else {
                        if (console) console.log('Ошибка загрузки клиента.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки клиента',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK,
                            fn : function () {
                                controller.getView().destroy();
                            },
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                } catch (exception) {
                    if (console) console.log(exception);
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка обработки ответа от сервера',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка загрузки клиента.');
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
                        msg : 'Ошибка загрузки клиента.',
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
    
    
    onDictClientGroupSelectClick : function(self) {
        var dictClientGroupId  = this.lookupReference('dictClientGroupId').getValue();
        var controller = this;
        var widget = Ext.widget('dictClientGroupSelect');
        widget.getController().setOkListener(function(arg0) {
            var id = arg0.id;
            var name = arg0.name;
            controller.lookupReference('dictClientGroupId').setValue(id);
            controller.lookupReference('dictClientGroupName').setText(name);
        });
        widget.getController().setSelectedId(dictClientGroupId);
    }
});