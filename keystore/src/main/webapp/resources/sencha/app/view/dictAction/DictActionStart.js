Ext.define('KeyStore.view.dictAction.DictActionStart', {
    extend : 'Ext.window.Window',
    xtype : 'dictActionStart',
    requires : [ 'KeyStore.view.dictAction.DictActionStartController',
            'KeyStore.view.dictAction.DictActionStartModel' ],
    controller : 'dictActionStart',
    viewModel : {
        type : 'dictActionStart'
    },
    items : [],
    autoShow : true,
    modal : true,
    title : 'Начало акции',
    width : 500,
    height : 200,
    layout : 'fit',
    items : [{
        xtype : 'form',
        layout : 'vbox',
        width: '100%',
        height: 200,
        reference : 'form',
        items : [{
            xtype : 'hidden',
            reference : 'id'
        },{
            xtype : 'panel',
            layout : 'form',
            width : '100%',
            height : 30,
            items : [{
                xtype : 'datefield',
                reference : 'dateEnd',
                fieldLabel : 'Дата окончания',
                format : 'd.m.Y',
                allowBlank : false
            }]
        }, {
            xtype : 'label',
            reference : 'infoLabel'
        }]
    }],
    buttons : [{
        text : 'Начать акцию',
        handler : 'onStartDictActionClick'
    }, {
        text : 'Отмена',
        handler : 'onCancelClick'
    }]
});