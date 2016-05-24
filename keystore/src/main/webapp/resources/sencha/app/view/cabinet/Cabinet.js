Ext.define('KeyStore.view.cabinet.Cabinet', {
    extend : 'Ext.panel.Panel',
    xtype : 'cabinet',
    requires : [ 'KeyStore.view.cabinet.CabinetController',
            'KeyStore.view.cabinet.CabinetModel', 'Ext.panel.Panel',
            'Ext.form.field.Date', 'Ext.form.field.Date',
            'Ext.form.field.Checkbox' ],
    autoScroll : true,
    layout : 'vbox',
    width : '100%',
    viewModel : {
        type : 'cabinet'
    },
    controller : 'cabinet',
    items : [{
        xtype : 'panel',
        height : 30
    }, {
        xtype : 'button',
        text : 'Сохранить',
        reference : 'saveButton',
        handler : 'onSaveClick'
    }, {
        xtype : 'label',
        text : 'Сообщение об ошибке сохранения',
        reference : 'infoLabel',
        height : 30
    }, {
        xtype : 'form',
        width : '100%',
        autoScroll : true,
        reference : 'form',
        items : [{
            xtype : 'panel',
            height : 200,
            width : '100%',
            title : 'Информация',
            layout : 'hbox',
            items : [ {
                xtype : 'panel',
                width : 400,
                height : 200,
                items : [ {
                    xtype : 'hidden',
                    reference : 'id'
                },{
                    xtype : 'textfield',
                    fieldLabel : 'Пользователь',
                    reference : 'fullName',
                    maxLength : 1024
                }, {
                    xtype : 'textfield',
                    fieldLabel : 'E-mail',
                    reference : 'userName',
                    maxLength : 50,
                    disabled : true

                }, {
                    xtype : 'textfield',
                    fieldLabel : 'Телефон',
                    maxLength : 50,
                    reference : 'phone'
                }]
            }, {
                xtype : 'panel',
                width : 400,
                height : 200,
                items : [ {
                    xtype : 'combo',
                    fieldLabel : 'Пол',
                    queryMode : 'local',
                    triggerAction : 'all',
                    reference : 'sex',
                    valueField : 'code',
                    displayField : 'name',
                    forceSelection : true,
                    editable : false,
                    reference : 'sex',
                    bind : {
                        store : '{sexStore}'
                    }
                }, {
                    xtype : 'datefield',
                    fieldLabel : 'День рождения',
                    format : 'd.m.Y',
                    reference : 'birthdate'
                }, {
                    xtype : 'textfield',
                    fieldLabel : 'Должность',
                    maxLength : 50,
                    reference : 'post'
                } ]
            } ]
        }, {
            xtype : 'panel',
            height : 600,
            width : '100%',
            title : 'Настройки',
            items : [ {
                xtype : 'panel',
                title : 'Уведомления',
                reference : 'notificationsPanel',
                layout : 'form',
                autoScroll : true,
                height : 600,
                width : '100%',
                //collapsible : true,
                //collapsed : true,
                items : [ {
                    xtype : 'checkbox',
                    fieldLabel : 'Добавление клиентского аккаунта',
                    reference : 'notificationClientAdded'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Изменение данных аккаунта',
                    reference : 'notificationClientChanged'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Блокировка аккаунта',
                    reference : 'notificationClientBlocked'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Добавление учётной записи пользователя',
                    reference : 'notificationUserAdded'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Изменение данных учётной записи пользователя',
                    reference : 'notificationUserChanged'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Блокировка учётной записи пользователя',
                    reference : 'notificationUserBlocked'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Размещение заказа',
                    reference : 'notificationOrderCreated'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Поступление платежа',
                    reference : 'notificationPayed'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Возврат средств',
                    reference : 'notificationPayBack'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Корректирующей проводке',
                    reference : 'notificationPayCorrected'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Участии в специальной акции',
                    reference : 'notificationAction'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Предложение клиенту пакета услуг',
                    reference : 'notificationService'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Появление обновлений приложения',
                    reference : 'notificationUpdate'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Обращение к клиентам',
                    reference : 'notificationClients'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Обращение к пользователям',
                    reference : 'notificationUsers'
                }, {
                    xtype : 'checkbox',
                    fieldLabel : 'Поздравление с днём рождения',
                    reference : 'notificationBirthday'
                } ]
            } ]

        }
        ]}, {
            xtype : 'panel',
            height : 100
        } ]
});