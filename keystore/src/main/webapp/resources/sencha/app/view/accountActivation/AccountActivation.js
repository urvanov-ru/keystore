Ext.define('KeyStore.view.accountActivation.AccountActivation', {
    extend : 'Ext.window.Window',
    xtype : 'accountActivation',
    requires : [ 'KeyStore.view.accountActivation.AccountActivationController',
            'KeyStore.view.accountActivation.AccountActivationModel' ],
    controller : 'accountActivation',
    viewModel : {
        type : 'accountActivation'
    },
    autoShow : true,
    width : 320,
    height : 240,
    items : [{
        xtype : 'label',
        reference : 'infoLabel'
    }],
    buttons : [{
        text : 'OK',
        handler : 'onOkClick'
    }],
    listeners : {
        afterrender : 'onAfterRender'
    }
});