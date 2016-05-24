Ext.define('KeyStore.view.register.RegisterModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.register',
    requires : ['KeyStore.store.ClientType'],
    data : {
    },
    stores : {
        clientTypeStore : {
            type : 'clientTypeGlobalStore',
            autoLoad : true
        }
    }
});