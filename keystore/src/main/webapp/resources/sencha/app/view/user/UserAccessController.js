Ext.define('KeyStore.view.user.UserAccessController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.userAccess',

    onCancelClick : function(self) {
        this.getView().destroy();
    },


    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        
        
        var id = controller.lookupReference('id').getValue();
        var serviceAdminAccount  = controller.lookupReference('serviceAdminAccount').getValue();
        var serviceAdminClient  = controller.lookupReference('serviceAdminClient').getValue();
        var serviceReadClient  = controller.lookupReference('serviceReadClient').getValue();
        var serviceNewClientNotification  = controller.lookupReference('serviceNewClientNotification').getValue();
        var serviceAddClient  = controller.lookupReference('serviceAddClient').getValue();
        var serviceExportClient  = controller.lookupReference('serviceExportClient').getValue();
        var serviceReadOrder  = controller.lookupReference('serviceReadOrder').getValue();
        var serviceExportOrder  = controller.lookupReference('serviceExportOrder').getValue();
        var serviceReadPayment  = controller.lookupReference('serviceReadPayment').getValue();
        var serviceExecutePayment  = controller.lookupReference('serviceExecutePayment').getValue();
        var serviceExportPayment  = controller.lookupReference('serviceExportPayment').getValue();
        var serviceReadAction  = controller.lookupReference('serviceReadAction').getValue();
        var serviceWriteAction  = controller.lookupReference('serviceWriteAction').getValue();
        var serviceExportAction  = controller.lookupReference('serviceExportAction').getValue();
        var serviceSendNotification  = controller.lookupReference('serviceSendNotification').getValue();
        var serviceSettingClientGroup  = controller.lookupReference('serviceSettingClientGroup').getValue();
        var serviceSettingDictServiceType  = controller.lookupReference('serviceSettingDictServiceType').getValue();
        var serviceSettingEmailTemplate  = controller.lookupReference('serviceSettingEmailTemplate').getValue();
        
        var clientAdminClientAccount  = controller.lookupReference('clientAdminClientAccount').getValue();
        var clientReadOrder  = controller.lookupReference('clientReadOrder').getValue();
        var clientAddOrder  = controller.lookupReference('clientAddOrder').getValue();
        var clientReadPayment  = controller.lookupReference('clientReadPayment').getValue();
        var clientReadKey  = controller.lookupReference('clientReadKey').getValue();
        var clientActivateKey  = controller.lookupReference('clientActivateKey').getValue();
        var clientGetKey  = controller.lookupReference('clientGetKey').getValue();
        var clientReceiveNotifications  = controller.lookupReference('clientReceiveNotifications').getValue();
        var clientExportData  = controller.lookupReference('clientExportData').getValue();
        
        var serviceAccess = controller.lookupReference('serviceAccess');
        var clientAccess = controller.lookupReference('clientAccess');
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/user/saveAccess',
            jsonData : {
                id : id,
                info : {
                    SERVICE_ADMIN_ACCOUNT : serviceAdminAccount,
                    SERVICE_ADMIN_CLIENT : serviceAdminClient,
                    SERVICE_READ_CLIENT : serviceReadClient,
                    SERVICE_NEW_CLIENT_NOTIFICATION : serviceNewClientNotification,
                    SERVICE_ADD_CLIENT : serviceAddClient,
                    SERVICE_EXPORT_CLIENT : serviceExportClient,
                    SERVICE_READ_ORDER : serviceReadOrder,
                    SERVICE_EXPORT_ORDER : serviceExportOrder,
                    SERVICE_READ_PAYMENT : serviceReadPayment,
                    SERVICE_EXECUTE_PAYMENT : serviceExecutePayment,
                    SERVICE_EXPORT_PAYMENT : serviceExportPayment,
                    SERVICE_READ_ACTION : serviceReadAction,
                    SERVICE_WRITE_ACTION : serviceWriteAction,
                    SERVICE_EXPORT_ACTION : serviceExportAction,
                    SERVICE_SEND_NOTIFICATION : serviceSendNotification,
                    SERVICE_SETTING_CLIENT_GROUP : serviceSettingClientGroup,
                    SERVICE_SETTING_DICT_SERVICE_TYPE : serviceSettingDictServiceType,
                    SERVICE_SETTING_EMAIL_TEMPLATE : serviceSettingEmailTemplate,
                    
                    CLIENT_ADMIN_CLIENT_ACCOUNT : clientAdminClientAccount,
                    CLIENT_READ_ORDER : clientReadOrder,
                    CLIENT_ADD_ORDER : clientAddOrder,
                    CLIENT_READ_PAYMENT : clientReadPayment,
                    CLIENT_READ_KEY : clientReadKey,
                    CLIENT_ACTIVATE_KEY : clientActivateKey,
                    CLIENT_GET_KEY : clientGetKey,
                    CLIENT_RECEIVE_NOTIFICATIONS : clientReceiveNotifications,
                    CLIENT_EXPORT_DATA : clientExportData
                }
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
                    console.log('Ошибка сохранения пользователя.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения прав доступа.');
                    break;
                }
            }
        });
    },
    
    loadData : function (obj) {
        var id = obj.id;
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/user/access',
            params : {
                id : id
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        var admin = false;
                        var clientAdmin = false;
                        for (var n = 0; n < answer.authorities.length; n++) {
                            var str = answer.authorities[n];
                            if (str == 'ROLE_SERVICE') {
                                admin = true;
                            }
                            if (str == 'ROLE_CLIENT') {
                                clientAdmin = true;
                            }
                        }
                        if (admin) {
                            controller.lookupReference('serviceAccess').show();
                            
                        } else if (!admin && clientAdmin) {
                            controller.lookupReference('clientAccess').show();
                        }
                        controller.lookupReference('id').setValue(id);
                        if (answer.info.SERVICE_ADMIN_ACCOUNT) controller.lookupReference('serviceAdminAccount').setValue(answer.info.SERVICE_ADMIN_ACCOUNT);
                        if (answer.info.SERVICE_ADMIN_CLIENT) controller.lookupReference('serviceAdminClient').setValue(answer.info.SERVICE_ADMIN_CLIENT);
                        if (answer.info.SERVICE_READ_CLIENT) controller.lookupReference('serviceReadClient').setValue(answer.info.SERVICE_READ_CLIENT);
                        if (answer.info.SERVICE_NEW_CLIENT_NOTIFICATION) controller.lookupReference('serviceNewClientNotification').setValue(answer.info.SERVICE_NEW_CLIENT_NOTIFICATION);
                        if (answer.info.SERVICE_ADD_CLIENT) controller.lookupReference('serviceAddClient').setValue(answer.info.SERVICE_ADD_CLIENT);
                        if (answer.info.SERVICE_EXPORT_CLIENT) controller.lookupReference('serviceExportClient').setValue(answer.info.SERVICE_EXPORT_CLIENT);
                        if (answer.info.SERVICE_READ_ORDER) controller.lookupReference('serviceReadOrderAdmin').setValue(answer.info.SERVICE_READ_ORDER);
                        if (answer.info.SERVICE_EXPORT_CLIENT) controller.lookupReference('serviceExportOrder').setValue(answer.info.SERVICE_EXPORT_CLIENT);
                        if (answer.info.SERVICE_READ_PAYMENT) controller.lookupReference('serviceReadPaymentAdmin').setValue(answer.info.SERVICE_READ_PAYMENT);
                        if (answer.info.SERVICE_EXECUTE_PAYMENT) controller.lookupReference('serviceExecutePayment').setValue(answer.info.SERVICE_EXECUTE_PAYMENT);
                        if (answer.info.SERVICE_EXPORT_PAYMENT) controller.lookupReference('serviceExportPayment').setValue(answer.info.SERVICE_EXPORT_PAYMENT);
                        if (answer.info.SERVICE_READ_ACTION) controller.lookupReference('serviceReadAction').setValue(answer.info.SERVICE_READ_ACTION);
                        if (answer.info.SERVICE_WRITE_ACTION) controller.lookupReference('serviceWriteAction').setValue(answer.info.SERVICE_WRITE_ACTION);
                        if (answer.info.SERVICE_EXPORT_ACTION) controller.lookupReference('serviceExportAction').setValue(answer.info.SERVICE_EXPORT_ACTION);
                        if (answer.info.SERVICE_SEND_NOTIFICATION) controller.lookupReference('serviceSendNotification').setValue(answer.info.SERVICE_SEND_NOTIFICATION);
                        if (answer.info.SERVICE_SETTING_CLIENT_GROUP) controller.lookupReference('serviceSettingClientGroup').setValue(answer.info.SERVICE_SETTING_CLIENT_GROUP);
                        if (answer.info.SERVICE_SETTING_DICT_SERVICE_TYPE) controller.lookupReference('serviceSettingDictServiceType').setValue(answer.info.SERVICE_SETTING_DICT_SERVICE_TYPE);
                        if (answer.info.SERVICE_SETTING_EMAIL_TEMPLATE) controller.lookupReference('serviceSettingEmailTemplate').setValue(anser.info.SERVICE_SETTING_EMAIL_TEMPLATE);
                        
                        if (answer.info.CLIENT_ADMIN_CLIENT_ACCOUNT) controller.lookupReference('clientAdminClientAccount').setValue(answer.info.CLIENT_ADMIN_CLIENT_ACCOUNT);
                        if (answer.info.CLIENT_READ_ORDER) controller.lookupReference('clientReadOrder').setValue(answer.info.CLIENT_READ_ORDER);
                        if (answer.info.CLIENT_ADD_ORDER) controller.lookupReference('clientAddOrder').setValue(answer.info.CLIENT_ADD_ORDER);
                        if (answer.info.CLIENT_READ_PAYMENT) controller.lookupReference('clientReadPayment').setValue(answer.info.CLIENT_READ_PAYMENT);
                        if (answer.info.CLIENT_READ_KEY) controller.lookupReference('clientReadKey').setValue(answer.info.CLIENT_READ_KEY);
                        if (answer.info.CLIENT_ACTIVATE_KEY) controller.lookupReference('clientActivateKey').setValue(answer.info.CLIENT_ACTIVATE_KEY);
                        if (answer.info.CLIENT_GET_KEY) controller.lookupReference('clientGetKey').setValue(answer.info.CLIENT_GET_KEY);
                        if (answer.info.CLIENT_RECEIVE_NOTIFICATIONS) controller.lookupReference('clientReceiveNotifications').setValue(answer.info.CLIENT_RECEIVE_NOTIFICATIONS);
                        if (answer.info.CLIENT_EXPORT_DATA) controller.lookupReference('clientExportData').setValue(answer.info.CLIENT_EXPORT_DATA);
                    } else {
                        if (console) console.log('Ошибка загрузки пользователя.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки данных',
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
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка загрузки пользователя.');
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
    }
});