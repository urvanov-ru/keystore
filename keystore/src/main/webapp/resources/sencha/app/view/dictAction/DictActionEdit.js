Ext.define('KeyStore.view.dictAction.DictActionEdit', {
    extend : 'Ext.window.Window',
    xtype : 'dictActionEdit',
    requires : [
            'KeyStore.view.dictAction.DictActionEditController',
            'KeyStore.view.dictAction.DictActionEditModel',
            'Ext.window.Window', 'Ext.form.field.Text', 'Ext.form.field.Number',
            'Ext.form.field.ComboBox', 'Ext.form.field.Date', 
            'Ext.form.field.TextArea'],
    controller : 'dictActionEdit',
    viewModel : {
        type : 'dictActionEdit'
    },
    layout : 'vbox',
    width : 500,
    height : 450,
    title : 'Специальная акция',
    autoShow : true,
    modal : true,
    items : [{
        xtype : 'form',
        width : '100%',
        height : 350,
        layout : {
            xtype : 'vbox',
            labelWidth : 150
        },
        reference : 'form',
        items : [ {
            xtype : 'panel',
            width : '100%',
            height : 130,
            layout : 'form',
            items : [{
                xtype : 'hidden',
                reference : 'id'
            },{
                xtype : 'textfield',
                fieldLabel : 'Название',
                reference : 'name',
                maxLength :  100,
                allowBlank : false
            }, {
                xtype : 'textfield',
                fieldLabel : 'Описание',
                reference : 'description',
                maxLength : 1024,
                allowBlank : false
            }, {
                xtype : 'datefield',
                fieldLabel : 'Дата начала акции',
                format : 'd.m.Y',
                reference : 'dateBegin',
                allowBlank : false,
                disabled : true
            }, {
                xtype : 'datefield',
                fieldLabel : 'Дата окончания акции',
                format : 'd.m.Y',
                reference : 'dateEnd',
                allowBlank : false,
                disabled : true
            }]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 480,
            height : 30,
            padding : '2 5 2 5',
            items : [{
                xtype : 'label',
                width : 150,
                text : 'Пакет услуг',
                autoWidth : false
            }, {
                xtype : 'hidden',
                reference : 'dictServiceTypeId'
            }, {
                xtype : 'label',
                width : 200,
                autoWidth : false,
                reference : 'dictServiceTypeName',
                text : '...'
            }, {
                xtype : 'button',
                width : 100,
                text : 'Выбрать',
                handler : 'onDictServiceTypeSelectClick'
            }]
        }, {
            xtype : 'panel',
            width : '100%',
            height : 200,
            layout : 'form',
            items : [{
                xtype : 'combo',
                fieldLabel : 'Тип акции',
                mode : 'local',
                triggerAction : 'all',
                editable : false,
                displayField : 'name',
                valueField : 'code',
                bind : {
                    store : '{dictActionTypeStore}'
                },
                reference : 'dictActionType',
                allowBlank : false
            }, {
                xtype : 'checkbox',
                fieldLabel : 'Для новых клиентов',
                reference : 'forNewClients'
            }]
        }]
    }, {
        xtype : 'label',
        fieldLabel : 'Сообщение об ошибке',
        reference : 'infoLabel'
    }],
    buttons : [{
        text : 'Сохранить',
        handler : 'onSaveClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }//, {
     //   xtype : 'label',
     //   text : '+ нужно как-то назначать акции клиентам. Возможно в форме редактирования.'
//    }
    ]
});
