Ext.define('KeyStore.view.user.UserEditController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.userEdit',
    id : 'userEdit',

    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var id = controller.lookupReference('id').getValue();
        var fullName = controller.lookupReference('fullName').getValue();
        var sex = controller.lookupReference('sex').getValue();
        var userName = controller.lookupReference('userName').getValue();
        var phone = controller.lookupReference('phone').getValue();
        var post = controller.lookupReference('post').getValue();
        var birthdate = controller.lookupReference('birthdate').getValue();
        
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/user/save',
            jsonData : {
                id : id,
                fullName : fullName,
                sex : sex,
                userName : userName,
                phone : phone,
                post : post,
                birthdate : birthdate
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
                    console.log('Ошибка сохранения пользователя.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения пользователя.');
                    break;
                }
            }
        });
    },

    onCancelClick : function(self) {
        this.getView().destroy();
    },

    
    loadData : function (obj) {
        var id = obj.id;
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/user/edit',
            params : {
                id : id
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('id').setValue(answer.info.id);
                        controller.lookupReference('fullName').setValue(answer.info.fullName);
                        controller.lookupReference('sex').setValue(answer.info.sex);
                        controller.lookupReference('userName').setValue(answer.info.userName);
                        controller.lookupReference('phone').setValue(answer.info.phone);
                        controller.lookupReference('post').setValue(answer.info.post);
                        controller.lookupReference('birthdate').setValue(
                                answer.info.birthdate == null ? null :
                                    new Date(answer.info.birthdate));
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
                        msg : 'Ошибка загрузки пользователя.',
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