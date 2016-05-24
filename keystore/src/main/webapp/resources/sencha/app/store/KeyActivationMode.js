Ext.define('KeyStore.store.KeyActivationMode', {
    extend: 'Ext.data.Store',
    model: 'KeyStore.model.CodeName',
    requires : ['KeyStore.model.CodeName', 'KeyStore.model.Base'],
    proxy : {
        type : 'ajax',
        url : globalUtils.toUrl('/order/keyActivationModes'),
        reader : {
            type : 'json',
            rootProperty : 'info'
        }
    },
    storeId : 'keyActivationModeGlobalStore',
    alias : 'store.keyActivationModeGlobalStore'
});