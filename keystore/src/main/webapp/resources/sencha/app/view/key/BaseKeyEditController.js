Ext.define('KeyStore.view.key.BaseKeyEditController', {
    extend : 'Ext.app.ViewController',
    id : 'baseKeyEdit',
    requires : [],
    alias : 'controller.baseKeyEdit',
    onSaveClick : function (self) {
        var controller = this;
        var form = this.lookupReference('form');
        var infoLabel = this.lookupReference('infoLabel');
        
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        } else {
            infoLabel.setText('');
        }
        if (console) console.log('saving key...');
        var dictServiceTypeId = this.lookupReference('dictServiceTypeId').getValue();
        var kind = this.lookupReference('kind').getValue();
        var dateBegin = this.lookupReference('dateBegin').getValue();
        var dateEnd = this.lookupReference('dateEnd').getValue();
        var password = this.lookupReference('password').getValue();
        var ethnosImportEnabled = this.lookupReference('ethnosImportEnabled').getValue();
        var ethnosExportEnabled = this.lookupReference('ethnosExportEnabled').getValue();
        var xlsEnabled = this.lookupReference('xlsEnabled').getValue();
        var officeEnabled = this.lookupReference('officeEnabled').getValue();
        var csvEnabled = this.lookupReference('csvEnabled').getValue();
        var xlsxEnabled = this.lookupReference('xlsxEnabled').getValue();
        var questionnaireLimit = this.lookupReference('questionnaireLimit').getValue();
        var qLimitPerDay = this.lookupReference('qLimitPerDay').getValue();
        var qLimitPerWeek = this.lookupReference('qLimitPerWeek').getValue();
        var qLimitPerMonth = this.lookupReference('qLimitPerMonth').getValue();
        var devicesLimit = this.lookupReference('devicesLimit').getValue();
        
        infoLabel.setText('Сохранение...');
        globalUtils.ajaxRequest({
            url : '/dictServiceType/keySave',
            jsonData : {
                dictServiceTypeId : dictServiceTypeId,
                kind : kind,
                dateBegin : dateBegin,
                dateEnd : dateEnd,
                password : password,
                ethnosImportEnabled : ethnosImportEnabled,
                ethnosExportEnabled : ethnosExportEnabled,
                xlsEnabled : xlsEnabled,
                officeEnabled : officeEnabled,
                csvEnabled : csvEnabled,
                xlsxEnabled : xlsxEnabled,
                questionnaireLimit : questionnaireLimit,
                qLimitPerDay : qLimitPerDay,
                qLimitPerWeek : qLimitPerWeek,
                qLimitPerMonth : qLimitPerMonth,
                devicesLimit : devicesLimit
            },
            success : function (xhr, opts) {
                infoLabel.setText('');
                var answer = Ext.decode(xhr.responseText);
                if (answer.success) {
                    Ext.MessageBox.show({
                        title : 'Внимание',
                        msg : 'Сохранение прошло успешно.',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getView().destroy();
                        }
                    });
                } else {
                    infoLabel.setText(answer.message);
                }
            },
            failure : function (xhr, opts) {
                if (console) console.log('Ошибка сохранения ключа.');
                
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка сохранения ключа.');
                    break;
                }
            }
        });
    },
    
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    
    loadData : function (obj) {
        var controller = this;
        var infoLabel = this.lookupReference('infoLabel');
        var dictServiceTypeId = obj.dictServiceTypeId;
        infoLabel.setText('Загрузка...');
        globalUtils.ajaxRequest({
            url : '/dictServiceType/keyEdit',
            params : {
                dictServiceTypeId : dictServiceTypeId
            },
            success : function (xhr, opts) {
                infoLabel.setText('');
                var answer = Ext.decode(xhr.responseText);
                if (answer.success) {
                    controller.lookupReference('dictServiceTypeId').setValue(dictServiceTypeId);
                    if (answer.keyParams == null) return;
                    if (answer.keyParams.dateBegin)
                        controller.lookupReference('dateBegin').setValue(new Date(answer.keyParams.dateBegin));
                    if (answer.keyParams.dateEnd)
                        controller.lookupReference('dateEnd').setValue(new Date(answer.keyParams.dateEnd));
                    controller.lookupReference('kind').setValue(answer.keyParams.kind);
                    controller.lookupReference('password').setValue(answer.keyParams.password);
                    controller.lookupReference('ethnosImportEnabled').setValue(answer.keyParams.ethnosImportEnabled);
                    controller.lookupReference('ethnosExportEnabled').setValue(answer.keyParams.ethnosExportEnabled);
                    controller.lookupReference('xlsEnabled').setValue(answer.keyParams.xlsEnabled);
                    controller.lookupReference('officeEnabled').setValue(answer.keyParams.officeEnabled);
                    controller.lookupReference('csvEnabled').setValue(answer.keyParams.csvEnabled);
                    controller.lookupReference('xlsxEnabled').setValue(answer.keyParams.xlsxEnabled);
                    controller.lookupReference('questionnaireLimit').setValue(answer.keyParams.questionnaireLimit);
                    controller.lookupReference('qLimitPerDay').setValue(answer.keyParams.qLimitPerDay);
                    controller.lookupReference('qLimitPerWeek').setValue(answer.keyParams.qLimitPerWeek);
                    controller.lookupReference('qLimitPerMonth').setValue(answer.keyParams.qLimitPerMonth);
                    controller.lookupReference('devicesLimit').setValue(answer.keyParams.devicesLimit);
                } else {
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : answer.message,
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                }
            },
            failure : function (xhr, opts) {
                if (console) console.log('Ошибка загрузки ключа для редактирования.');
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
                        msg : 'Ошибка загрузки ключа для редактирования.',
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
    
    dateBeginChanged : function( self, newValue, oldValue, eOpts ) {
        this.updatePeriod();
    },
    
    dateEndChanged :  function( self, newValue, oldValue, eOpts ) {
        this.updatePeriod();
    },
    
    updatePeriod : function () {
        var dateBegin = this.lookupReference('dateBegin').getValue();
        var dateEnd = this.lookupReference('dateEnd').getValue();
        if (dateBegin != null && dateEnd != null) {
            var diffDays = Math.floor((dateEnd - dateBegin) / 1000 / 60 / 60 / 24) + 1;
            this.lookupReference('period').setValue(diffDays);
        }
    },
    
    onRegenPasswordClick : function () {
        var password = this.lookupReference('password');
        globalUtils.ajaxRequest({
            url : '/key/guid',
            success : function(xhr, opts) {
                var answer = null;
                try {
                    answer = Ext.decode(xhr.responseText);
                } catch (exception) {
                    if (console)
                        console.log('Ошибка обработки ответа сервера');
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка разбора ответа сервера.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
                if (answer) {
                    if (answer.success) {
                        password.setValue(answer.info);
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
                        fn : function () {
                            controller.getView().destroy();
                        },
                        icon : Ext.MessageBox.ERROR
                    });
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка загрузки данных.',
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