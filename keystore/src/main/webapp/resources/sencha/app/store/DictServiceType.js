Ext.define('KeyStore.store.DictServiceType', {
    extend: 'Ext.data.Store',
    model: 'KeyStore.model.IdName',
    requires : ['KeyStore.model.IdName', 'KeyStore.model.Base'],
    proxy : {
        type : 'ajax',
        url : globalUtils.toUrl('/dictServiceType/query'),
        reader : {
            type : 'json',
            rootProperty : 'info'
        }
    },
    storeId : 'dictServiceTypeGlobalStore',
    alias : 'store.dictServiceTypeGlobalStore'
});