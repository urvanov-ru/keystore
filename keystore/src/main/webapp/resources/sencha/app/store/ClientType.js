Ext.define('KeyStore.store.ClientType', {
    extend: 'Ext.data.Store',
    model: 'KeyStore.model.CodeName',
    requires : ['KeyStore.model.CodeName', 'KeyStore.model.Base'],
    proxy : {
        type : 'ajax',
        url : globalUtils.toUrl('/client/clientTypes'),
        reader : {
            type : 'json',
            rootProperty : 'info'
        }
    },
    storeId : 'clientTypeGlobalStore',
    alias : 'store.clientTypeGlobalStore'
});