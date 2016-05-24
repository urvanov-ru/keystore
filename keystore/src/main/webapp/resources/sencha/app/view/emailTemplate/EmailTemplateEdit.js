Ext.define('KeyStore.view.emailTemplate.EmailTemplateEdit', {
    extend : 'Ext.window.Window',
    xtype : 'emailTemplateEdit',
    requires : [ 'KeyStore.view.emailTemplate.EmailTemplateEditController',
            'KeyStore.view.emailTemplate.EmailTemplateEditModel',
            'Ext.button.Button', 'Ext.form.field.Text',
            'Ext.form.field.TextArea',
            'Ext.panel.Panel', 'Ext.form.Label'],
    controller : 'emailTemplateEdit',
    viewModel : {
        type : 'emailTemplateEdit'
    },
    layout : 'fit',
    autoShow : true,
    modal : true,
    width : 600,
    height : 400,
    title : 'Шаблон письма',
    items : [ {
        xtype : 'form',
        reference : 'form',
        layout : 'vbox',
        items : [ {
            xtype : 'hidden',
            reference : 'id'
        }, {
            xtype : 'panel',
            layout : 'form',
            width : '100%',
            height : 60,
            items : [{
                xtype : 'textfield',
                disabled : true,
                fieldLabel : 'Событие',
                reference : 'codeName'
            }, {
                xtype : 'textfield',
                reference : 'title',
                fieldLabel : 'Заголовок',
                maxLength : 1024
            }]
        }, {
            xtype : 'label',
            text : 'Текст письма'
        }, {
            xtype : 'textarea',
            width : '100%',
            height : 200,
            reference : 'body'
        }, {
            xtype : 'label',
            reference : 'infoLabel',
            text : 'Сообщение об ошибке сохранения'
        } ]
    } ],
    buttons : [ {
        xtype : 'button',
        text : 'Сохранить',
        handler : 'onSaveClick'
    }, {
        xtype : 'button',
        text : 'Отмена',
        handler : 'onCancelClick'
    } ]
});