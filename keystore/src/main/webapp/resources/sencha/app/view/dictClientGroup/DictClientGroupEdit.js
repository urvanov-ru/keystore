Ext.define('KeyStore.view.dictClientGroup.DictClientGroupEdit', {
    extend : 'Ext.window.Window',
    xtype : 'dictClientGroupEdit',
    requires : [ 'KeyStore.view.dictClientGroup.DictClientGroupEditController',
            'KeyStore.view.dictClientGroup.DictClientGroupEditModel',
            'Ext.window.Window', 'Ext.button.Button'],
    controller : 'dictClientGroupEdit',
    viewModel : {
        type : 'dictClientGroupEdit'
    },
    autoShow : true,
    modal : true,
    width : 600,
    height : 200,
    title : 'Группа клиентов',
    layout : 'fit',
    items : [{
        xtype : 'form',
        layout : 'vbox',
        width: '100%',
        height : 150,
        reference : 'form',
        labelWidth : 200,
        items : [{
            xtype : 'panel',
            width : '100%',
            height : 30,
            layout : 'form',
            items : [{
                xtype : 'hidden',
                reference : 'id'
            },{
                xtype : 'textfield',
                fieldLabel : 'Название',
                reference : 'name'
            }]
        }, {
            xtype : 'panel',
            layout : 'hbox',
            width : 600,
            height : 30,
            padding : '2 2 2 2',
            items : [{
                xtype : 'hidden',
                reference : 'dictClientGroupId'
            }, {
                xtype : 'label',
                width : 200,
                text : 'Родительская группа',
                autoWidth : false
            }, {
                xtype : 'label',
                width : 300,
                autoWidth : false,
                text : '...',
                reference : 'dictClientGroupName'
            }, {
                xtype : 'button',
                text : 'Выбрать',
                handler : 'onDictClientGroupSelectClick'
            }]
        }, {
            xtype : 'label',
            reference : 'infoLabel',
            text : ''
        }]
    }],
    buttons : [{
        xtype : 'button',
        text : 'Сохранить',
        handler : 'onSaveClick'
    }, {
        xtype : 'button',
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});