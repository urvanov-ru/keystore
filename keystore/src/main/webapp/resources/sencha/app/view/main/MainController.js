/**
 * This class is the main view for the application. It is specified in app.js as
 * the "autoCreateViewport" property. That setting automatically applies the
 * "viewport" plugin to promote that instance of this class to the body element.
 * 
 * TODO - Replace this content of this view to suite the needs of your
 * application.
 */
Ext.define('KeyStore.view.main.MainController', {
    extend : 'Ext.app.ViewController',

    requires : [ 'Ext.window.MessageBox' ],

    alias : 'controller.main',

    onClickButton : function() {
        Ext.Msg.confirm('Confirm', 'Are you sure?', 'onConfirm', this);
    },

    onConfirm : function(choice) {
        if (choice === 'yes') {
            //
        }
    },

    onClientListClick : function(self) {
        this.showPage(Ext.widget('clientList'), 'Клиенты');
    },

    onOrderListClick : function(self) {
        this.showPage(Ext.widget('orderList'), 'Заказы');
    },

    onPaymentListClick : function(self) {
        this.showPage(Ext.widget('paymentList'), 'Платежи');
    },

    onKeyListClick : function(self) {
        this.showPage(Ext.widget('keyList'), 'Ключи');
    },

    onCabinetClick : function(self) {
        this.showPage(Ext.widget('cabinet'), 'Кабинет');
    },

    onExitClick : function(self) {
        Ext.Msg.confirm('Подтверждение', 'Точно выйти?', function (buttonId) {
            if (buttonId == 'yes') {
                
                Ext.Ajax.request({
                    url : globalUtils.toRootUrl('/j_spring_security_logout'),
                    success : function(xhr, opts) {
                        window.location = globalUtils.toUrl('/resources/sencha/index.html');
                    },
                    failure : function(xhr, opts) {
                        if (console)
                            console.log('Внутренняя ошибка сервера');
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : 'Внутренняя ошибка сервера',
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                });
            }
        }, this);
    },

    onAccountClick : function(self) {
        this.showPage(Ext.widget('account'), 'Аккаунт');
    },

    onUserListClick : function(self) {
        this.showPage(Ext.widget('userList'), 'Пользователи');
    },

    onGlobalSettingsClick : function(self) {
        this.showPage(Ext.widget('globalSettings'), 'Настройки');
    },

    onDictServiceTypeListClick : function(self) {
        this.showPage(Ext.widget('dictServiceTypeList'), 'Пакеты');
    },

    onDictActionListClick : function(self) {
        this.showPage(Ext.widget('dictActionList'), 'Акции');
    },

    onDictClientGroupListClick : function(self) {
        this.showPage(Ext.widget('dictClientGroupList'), 'Группы');
    },
    
    onEmailTemplateListClick : function (self) {
        this.showPage(Ext.widget('emailTemplateList'), 'Шаблоны');
    },
    
    onReportActivityClick : function (self) {
        this.showPage(Ext.widget('reportActivity'), 'Активность пользователей');
    },
    
    onReportPaymentClick : function (self) {
        this.showPage(Ext.widget('reportPayment'), 'Деньги');
    },
    
    listen : {
        controller : {
            '#orderList' : {
                'keyListClick' : 'onKeyListByOrderClick'
            }
        }
    },
    
    onKeyListByOrderClick : function (arg) {
        this.showPage(Ext.widget('keyList'), 'Ключи', {orderId : arg.orderId});
    },

    showPage : function(widget, headerText, arg) {
        var centerPanel = this.lookupReference('centerPanel');
        centerPanel.removeAll(true);
        var lastWidget = this.getViewModel().getData().pageWidget;
        if (lastWidget != null) lastWidget.destroy();
        this.getViewModel().getData().pageWidget = widget;
        centerPanel.add(widget);
        this.setHeaderText(headerText);
        if (widget.getController().loadData) widget.getController().loadData(arg);
    },
    
    onMainUserFullNameAfterRender : function (self, eOpts) {
        var controller = this;
        globalUtils.ajaxRequest({
            url : '/main/userInfo',
            success : function(xhr, opts) {
                try {
                    var answer = Ext.decode(xhr.responseText);

                    if (answer.success) {
                        self.setText(answer.fullName);
                        globalData.user.fullName = answer.fullName;
                        globalData.user.authorities = answer.authorities;
                        globalData.user.accesses = answer.accesses;
                        controller.processAuthorities();
                        controller.processAccesses();
                    } else {
                        if (console)
                            console.log(message);
                        self.setText('Ошибка');
                    }
                } catch (exception) {
                    if (console)
                        console.log('Ошибка обработки ответа сервера' + exception);
                    self.setText('Ошибка');
                }
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Внутренняя ошибка сервера');
                
                switch (xhr.status) {
                case 403:
                    self.setText('Доступ запрещён');
                    break;
                default :
                    self.setText('Ошибка');
                    break;
                }
            }
        });
    },
    
    processAuthorities : function () {
        var controller = this;
        for (var n = 0; n < globalData.user.authorities.length; n++) {
            var str = globalData.user.authorities[n];
            if (str == 'ROLE_SERVICE') {
                controller.lookupReference('menuItemClientList').show();
                controller.lookupReference('menuItemSettings').show();
                controller.lookupReference('menuItemReports').show();
                controller.lookupReference('menuItemReportActivity').enable();
                controller.lookupReference('menuItemReportPayment').enable();
            }
        }
    },
    
    processAccesses : function () {
        var controller = this;
        controller.lookupReference('menuItemGlobalSettings').enable();
        if (globalData.user.accesses.SERVICE_ADMIN_CLIENT ||
                globalData.user.accesses.SERVICE_READ_CLIENT ||
                globalData.user.accesses.SERVICE_NEW_CLIENT_NOTIFICATION || 
                globalData.user.accesses.SERVICE_ADD_CLIENT || 
                globalData.user.accesses.SERVICE_EXPORT_CLIENT) {
            controller.lookupReference('menuItemClientList').enable();
        }
        if (globalData.user.accesses.SERVICE_READ_ORDER ||
                globalData.user.accesses.SERVICE_EXPORT_ORDER ||
                globalData.user.accesses.SERVICE_READ_PAYMENT ||
                globalData.user.accesses.SERVICE_EXECUTE_PAYMENT ||
                globalData.user.accesses.SERVICE_EXPORT_PAYMENT ||
                globalData.user.accesses.CLIENT_READ_ORDER ||
                globalData.user.accesses.CLIENT_ADD_ORDER) {
            controller.lookupReference('menuItemOrderList').enable();
        }
        if (globalData.user.accesses.SERVICE_READ_PAYMENT ||
                globalData.user.accesses.SERVICE_EXECUTE_PAYMENT ||
                globalData.user.accesses.SERVICE_EXPORT_PAYMENT ||
                globalData.user.accesses.CLIENT_ADD_ORDER ||
                globalData.user.accesses.CLIENT_READ_PAYMENT
                ) {
            controller.lookupReference('menuItemPaymentList').enable();
        }
        if (globalData.user.accesses.CLIENT_READ_KEY ||
               globalData.user.accesses.CLIENT_ACTIVATE_KEY ||
               globalData.user.accesses.CLIENT_GET_KEY) {
            controller.lookupReference('menuItemKeyList').enable();
        }
        if (globalData.user.accesses.SERVICE_ADMIN_ACCOUNT ||
                globalData.user.accesses.CLIENT_ADMIN_CLIENT_ACCOUNT) {
            controller.lookupReference('menuItemAccount').enable();
        }
        if (globalData.user.accesses.SERVICE_ADMIN_ACCOUNT ||
                globalData.user.accesses.CLIENT_ADMIN_CLIENT_ACCOUNT) {
            controller.lookupReference('menuItemUserList').enable();
        }
        if (globalData.user.accesses.SERVICE_SETTING_DICT_SERVICE_TYPE) {
            controller.lookupReference('menuItemDictServiceTypeList').enable();
        }
        if (globalData.user.accesses.SERVICE_READ_ACTION ||
                globalData.user.accesses.SERVICE_WRITE_ACTION ||
                globalData.user.accesses.SERVICE_EXPORT_ACTION) {
            controller.lookupReference('menuItemDictActionList').enable();
        }
        if (globalData.user.accesses.SERVICE_SETTING_CLIENT_GROUP ) {
            controller.lookupReference('menuItemDictClientGroupList').enable();
        }
        if (globalData.user.accesses.SERVICE_SETTING_EMAIL_TEMPLATE) {
            controller.lookupReference('menuItemEmailTemplateList').enable();
        }
    },
    
    setHeaderText : function (text) {
        this.lookupReference('headerLabel').setText('KeyStore: ' + text);
    },
    
    onRender : function (self, eOpts) {
        this.onCabinetClick();
    }
});
