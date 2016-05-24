Ext.define('KeyStore.view.emailTemplate.EmailTemplateEditController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.emailTemplateEdit',

    onCancelClick : function(self) {
        this.getView().destroy();
    },

    loadData : function(obj) {
        var id = obj.id;
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        infoLabel.setText('Загрузка');
        globalUtils.ajaxRequest({
            url : '/emailTemplate/edit',
            params : {
                id : id
            },
            success : function(xhr, opts) {
                infoLabel.setText('');
                try {
                    var answer = Ext.decode(xhr.responseText);
                    if (answer.success) {
                        controller.lookupReference('id').setValue(id);
                        controller.lookupReference('codeName').setValue(
                                answer.info.codeName);
                        controller.lookupReference('title').setValue(
                                answer.info.title);
                        controller.lookupReference('body').setValue(
                                answer.info.body);
                    } else {
                        if (console)
                            console.log('Ошибка загрузки шаблона письма.');
                        Ext.MessageBox.show({
                            title : 'Ошибка загрузки шаблона письма.',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK,
                            fn : function() {
                                controller.getView().destroy();
                            }
                        });
                    }
                } catch (exception) {
                    if (console)
                        console.log(exception);
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка обработки ответа от сервера.',
                        buttons : Ext.MessageBox.OK,
                        fn : function() {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка загрузки шаблона письма.');
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
                        msg : 'Ошибка загрузки шаблона письма.',
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

    onSaveClick : function(self) {
        var form = this.lookupReference('form');
        var controller = this;
        infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var id = controller.lookupReference('id').getValue();
        var title = controller.lookupReference('title').getValue();
        var body = controller.lookupReference('body').getValue();

        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/emailTemplate/save',
            jsonData : {
                id : id,
                title : title,
                body : body
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
                    if (console)
                        console.log(exception);
                    infoLabel.setText('Ошибка обработки ответа от сервера.');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка сохранения шаблона письма.');
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения шаблона письма.');
                    break;
                }
            }
        });
    }
});