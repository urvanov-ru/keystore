Ext.define('KeyStore.view.client.ClientFilterModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.clientFilter',
    data : {

    },
    stores : {
        dictClientGroupStore : {
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/client/group'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            fields : [ {
                name : 'id',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            } ]
        },

        activeStore : {
            data : [ {
                code : 'ALL',
                name : 'Все'
            }, {
                code : 'ACTIVE',
                name : 'Активен'
            }, {
                code : 'BLOCKED',
                name : 'Заблокирован'
            } ],
            fields : [ {
                name : 'code',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            } ]
        }
    }
});