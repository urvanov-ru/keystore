Ext.define('KeyStore.view.cabinet.CabinetController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.cabinet',
    
    onSaveClick : function (self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var id = this.lookupReference('id').getValue();
        var fullName = this.lookupReference('fullName').getValue();
        var userName = this.lookupReference('userName').getValue();
        var phone = this.lookupReference('phone').getValue();
        var sex = this.lookupReference('sex').getValue();
        var birthdate = this.lookupReference('birthdate').getValue();
        var post = this.lookupReference('post').getValue();
        
        var notificationClientAdded = this.lookupReference('notificationClientAdded').getValue();
        var notificationClientChanged = this.lookupReference('notificationClientChanged').getValue();
        var notificationClientBlocked = this.lookupReference('notificationClientBlocked').getValue();
        var notificationUserAdded = this.lookupReference('notificationUserAdded').getValue();
        var notificationUserChanged = this.lookupReference('notificationUserChanged').getValue();
        var notificationUserBlocked = this.lookupReference('notificationUserBlocked').getValue();
        var notificationOrderCreated = this.lookupReference('notificationOrderCreated').getValue();
        var notificationPayed = this.lookupReference('notificationPayed').getValue();
        var notificationPayBack = this.lookupReference('notificationPayBack').getValue();
        var notificationPayCorrected = this.lookupReference('notificationPayCorrected').getValue();
        var notificationAction = this.lookupReference('notificationAction').getValue();
        var notificationService = this.lookupReference('notificationService').getValue();
        var notificationUpdate = this.lookupReference('notificationUpdate').getValue();
        var notificationClients = this.lookupReference('notificationClients').getValue();
        var notificationUsers = this.lookupReference('notificationUsers').getValue();
        var notificationBirthday = this.lookupReference('notificationBirthday').getValue();

        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/cabinet/save',
            jsonData : {
                id : id,
                fullName : fullName,
                userName : userName,
                phone : phone,
                sex : sex,
                birthdate : birthdate,
                post : post,
                
                notifications : {
                    CLIENT_ADDED : notificationClientAdded,
                    CLIENT_CHANGED : notificationClientChanged,
                    CLIENT_BLOCKED : notificationClientBlocked,
                    USER_ADDED : notificationUserAdded,
                    USER_CHANGED : notificationUserChanged,
                    USER_BLOCKED : notificationUserBlocked,
                    ORDER_CREATED : notificationOrderCreated,
                    PAYED : notificationPayed,
                    PAY_BACK : notificationPayBack,
                    PAY_CORRECTED : notificationPayCorrected,
                    ACTION : notificationAction,
                    SERVICE : notificationService,
                    UPDATE : notificationUpdate,
                    CLIENTS : notificationClients,
                    USERS : notificationUsers,
                    BIRTHDAY : notificationBirthday
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
                    console.log('Ошибка сохранения.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения.');
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
            url : '/cabinet/edit',
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('id').setValue(answer.info.id);
                        controller.lookupReference('fullName').setValue(answer.info.fullName);
                        controller.lookupReference('userName').setValue(answer.info.userName);
                        controller.lookupReference('phone').setValue(answer.info.phone);
                        controller.lookupReference('sex').setValue(answer.info.sex);
                        if (answer.info.birthdate) controller.lookupReference('birthdate').setValue(new Date(answer.info.birthdate));
                        controller.lookupReference('post').setValue(answer.info.post);
                        
                        if (answer.info.notifications.CLIENT_ADDED) controller.lookupReference('notificationClientAdded').setValue(answer.info.notifications.CLIENT_ADDED);
                        if (answer.info.notifications.CLIENT_CHANGED) controller.lookupReference('notificationClientChanged').setValue(answer.info.notifications.CLIENT_CHANGED);
                        if (answer.info.notifications.CLIENT_BLOCKED) controller.lookupReference('notificationClientBlocked').setValue(answer.info.notifications.CLIENT_BLOCKED);
                        if (answer.info.notifications.USER_ADDED) controller.lookupReference('notificationUserAdded').setValue(answer.info.notifications.USER_ADDED);
                        if (answer.info.notifications.USER_CHANGED) controller.lookupReference('notificationUserChanged').setValue(answer.info.notifications.USER_CHANGED);
                        if (answer.info.notifications.USER_BLOCKED) controller.lookupReference('notificationUserBlocked').setValue(answer.info.notifications.USER_BLOCKED);
                        if (answer.info.notifications.ORDER_CREATED) controller.lookupReference('notificationOrderCreated').setValue(answer.info.notifications.ORDER_CREATED);
                        if (answer.info.notifications.PAYED) controller.lookupReference('notificationPayed').setValue(answer.info.notifications.PAYED);
                        if (answer.info.notifications.PAY_BACK) controller.lookupReference('notificationPayBack').setValue(answer.info.notifications.PAY_BACK);
                        if (answer.info.notifications.PAY_CORRECTED) controller.lookupReference('notificationPayCorrected').setValue(answer.info.notifications.PAY_CORRECTED);
                        if (answer.info.notifications.ACTION) controller.lookupReference('notificationAction').setValue(answer.info.notifications.ACTION);
                        if (answer.info.notifications.SERVICE) controller.lookupReference('notificationService').setValue(answer.info.notifications.SERVICE);
                        if (answer.info.notifications.UPDATE) controller.lookupReference('notificationUpdate').setValue(answer.info.notifications.UPDATE);
                        if (answer.info.notifications.CLIENTS) controller.lookupReference('notificationClients').setValue(answer.info.notifications.CLIENTS);
                        if (answer.info.notifications.USERS) controller.lookupReference('notificationUsers').setValue(answer.info.notifications.USERS);
                        if (answer.info.notifications.BIRTHDAY) controller.lookupReference('notificationBirthday').setValue(answer.info.notifications.BIRTHDAY);
                    } else {
                        if (console) console.log('Ошибка загрузки данных.');
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
                        msg : 'Ошибка загрузки данных',
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