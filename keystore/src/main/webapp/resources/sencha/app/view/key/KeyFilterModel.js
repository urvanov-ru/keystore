Ext.define('KeyStore.view.key.KeyFilterModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.keyFilter',
    data : {

    },
    stores : {
        keyStatusStore : {
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/key/keyStatuses'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            fields : [ {
                name : 'code',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            } ],
            autoLoad : true
        }
    }
});