Ext.define('KeyStore.store.DictClientGroup', {
    extend: 'Ext.data.Store',
    model: 'KeyStore.model.IdName',
    requires : ['KeyStore.model.IdName', 'KeyStore.model.Base'],
    proxy : {
        type : 'ajax',
        url : globalUtils.toUrl('/client/dictClientGroups'),
        reader : {
            type : 'json',
            rootProperty : 'info'
        }
    },
    storeId : 'dictClientGroupGlobalStore',
    alias : 'store.dictClientGroupGlobalStore'
});