Ext.define('KeyStore.view.user.UserAccess', {
    extend : 'Ext.window.Window',
    xtype : 'userAccess',
    requires : [ 'KeyStore.view.user.UserAccessController',
            'KeyStore.view.user.UserAccessModel' ],
    controller : 'userAccess',
    viewModel : {
        type : 'userAccess'
    },
    width : 600,
    height : 400,
    autoShow : true,
    modal : true,
    layout : 'fit',
    title : 'Права доступа',
    items : [{
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        width : '100%',
        scrollable : 'y',
        items : [{
            xtype : 'hidden',
            reference : 'id'
        },{
            xtype : 'panel',
            reference : 'serviceAccess',
            layout : 'form',
            width : '100%',
            hidden : true,
            collapsible : true,
            defaults : {
                xtype : 'checkbox'
            },
            items : [{
                fieldLabel : 'Администрирование аккаунта (данных аккаунта и пользователей)',
                reference : 'serviceAdminAccount'
            }, {
                fieldLabel : 'Администрирование клиентских аккаунтов (данных аккаунта и пользователей)',
                reference : 'serviceAdminClient'
            }, {
                fieldLabel : 'Просмотр клиентских аккаунтов',
                reference : 'serviceReadClient'
            }, {
                fieldLabel : 'Получение оповещений о регистрации клиентов',
                reference : 'serviceNewClientNotification'
            }, {
                fieldLabel : 'Добавление клиентских аккаунтов',
                reference : 'serviceAddClient'
            }, {
                fieldLabel : 'Экспорт данных о клиентских аккаунтах',
                reference : 'serviceExportClient'
            }, {
                fieldLabel : 'Просмотр заказов',
                reference : 'serviceReadOrder'
            }, {
                fieldLabel : 'Экспорт заказов',
                reference : 'serviceExportOrder'
            }, {
                fieldLabel : 'Просмотр платежей',
                reference : 'serviceReadPayment'
            }, {
                fieldLabel : 'Проведение платежей',
                reference : 'serviceExecutePayment'
            }, {
                fieldLabel : 'Экспорт данных о платежах',
                reference : 'serviceExportPayment'
            }, {
                fieldLabel : 'Просмотр специальных акций',
                reference : 'serviceReadAction'
            }, {
                fieldLabel : 'Изменение специальных акций',
                reference : 'serviceWriteAction'
            }, {
                fieldLabel : 'Экспорт специальных акций',
                reference : 'serviceExportAction'
            }, {
                fieldLabel : 'Рассылка уведомлений',
                reference : 'serviceSendNotification'
            }, {
                fieldLabel : 'Настройка групп клиентов',
                reference : 'serviceSettingClientGroup'
            }, {
                fieldLabel : 'Настройка пакетов услуг',
                reference : 'serviceSettingDictServiceType'
            }, {
                fieldLabel : 'Настройка шаблонов писем',
                reference : 'serviceSettingEmailTemplate'
            }]
        }, {
            xtype : 'panel',
            reference : 'clientAccess',
            layout : 'form',
            width : '100%',
            hidden : true,
            collapsible : true,
            defaults : {
                xtype : 'checkbox'
            },
            items : [{
                fieldLabel : 'Администрирование аккаунта',
                reference : 'clientAdminClientAccount'
            }, {
                fieldLabel : 'Просмотр заказов',
                reference : 'clientReadOrder'
            }, {
                fieldLabel : 'Размещение заказов',
                reference : 'clientAddOrder'
            }, {
                fieldLabel : 'Просмотр платежей',
                reference : 'clientReadPayment'
            }, {
                fieldLabel : 'Просмотр ключей',
                reference : 'clientReadKey'
            }, {
                fieldLabel : 'Активация ключей',
                reference : 'clientActivateKey'
            }, {
                fieldLabel : 'Получение ключей',
                reference : 'clientGetKey'
            }, {
                fieldLabel : 'Получение оповещений',
                reference : 'clientReceiveNotifications'
            }, {
                fieldLabel : 'Экспорт данных',
                reference : 'clientExportData'
            }]
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }],
    buttons : [{
        text : 'Сохранить',
        handler : 'onSaveClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});