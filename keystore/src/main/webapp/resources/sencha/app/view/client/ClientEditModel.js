Ext.define('KeyStore.view.client.ClientEditModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.clientEdit',
    requires : ['KeyStore.store.ClientType', 'KeyStore.model.CodeName', 'KeyStore.model.Base'],
    data : {
    },
    stores : {
        clientTypeStore : {
            type : 'clientTypeGlobalStore',
            autoLoad : true
        }
    }
});