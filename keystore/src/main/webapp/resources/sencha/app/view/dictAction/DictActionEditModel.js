Ext.define('KeyStore.view.dictAction.DictActionEditModel', {
    extend : 'Ext.app.ViewModel',
    requires : ['KeyStore.model.CodeName', 'KeyStore.store.DictServiceType'],
    alias : 'viewmodel.dictActionEdit',
    data : {

    },
    stores : {
        dictActionTypeStore : {
            model : 'KeyStore.model.CodeName',
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictAction/dictActionTypes'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            autoLoad : true
        }
    }
});