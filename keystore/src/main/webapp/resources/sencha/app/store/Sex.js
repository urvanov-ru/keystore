Ext.define('KeyStore.store.Sex', {
    extend : 'Ext.data.Store',
    model : 'KeyStore.model.CodeName',
    requires : [ 'KeyStore.model.CodeName', 'KeyStore.model.Base' ],
    proxy : {
        type : 'ajax',
        url : globalUtils.toUrl('/user/sexes'),
        reader : {
            type : 'json',
            rootProperty : 'info'
        }
    },
    storeId : 'sexGlobalStore',
    alias : 'store.sexGlobalStore'
});