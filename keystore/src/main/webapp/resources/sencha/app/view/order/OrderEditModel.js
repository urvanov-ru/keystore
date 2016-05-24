Ext.define('KeyStore.view.order.OrderEditModel', {
    extend : 'Ext.app.ViewModel',
    requires : ['KeyStore.model.CodeName', 'KeyStore.store.DictServiceType'],
    alias : 'viewmodel.orderEdit',

    data : {

    },
    stores : {
        dictServiceTypeStore : {
            type : 'dictServiceTypeGlobalStore'
        },
        keyActivationModeStore : {
            type : 'keyActivationModeGlobalStore',
            autoLoad : true
        }
    }
});