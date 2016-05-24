Ext.define('KeyStore.view.client.EmailNotification', {
    extend : 'Ext.window.Window',
    xtype : 'clientEmailNotification',
    requires : [ 'KeyStore.view.client.EmailNotificationController',
            'KeyStore.view.client.EmailNotificationModel' ],
    controller : 'clientEmailNotification',
    viewModel : {
        type : 'clientEmailNotification'
    },
    width : 600,
    height : 400,
    title : 'Отправка e-mail',
    autoShow : true,
    modal : true,
    layout : 'fit',
    items : [{
        xtype : 'form',
        layout : 'vbox',
        reference : 'form',
        items : [{
            xtype : 'panel',
            width : '100%',
            height : 30,
            layout : 'form',
            items : [{
                xtype : 'textfield',
                reference : 'title',
                fieldLabel : 'Заголовок',
                allowBlank : false
            }]
        }, {
            xtype : 'label',
            height : 30,
            text : 'Сообщение:'
        }, {
            xtype : 'textarea',
            height : 200,
            width : '100%',
            reference : 'body',
            allowBlank : false
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }],
    buttons : [{
        text : 'Отправить',
        handler : 'onSendClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});