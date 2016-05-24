Ext.define('KeyStore.view.dictServiceType.DictServiceTypeEditModel', {
    extend : 'Ext.app.ViewModel',
    requires : ['KeyStore.model.Base','KeyStore.model.CodeName'],
    alias : 'viewmodel.dictServiceTypeEdit',
    data : {

    },
    stores : {
        dictServiceTypeStatusStore : {
            model : 'KeyStore.model.CodeName',
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictServiceType/dictServiceTypeStatuses'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            autoLoad : true
        }
    }
});