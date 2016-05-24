Ext.define('KeyStore.view.account.AccountModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.account',
    requires : [],
    data : {},
    stores : {
        clientTypeStore : {
            type : 'clientTypeGlobalStore',
            autoLoad : true
        }
    }
});