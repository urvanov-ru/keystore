Ext.define('KeyStore.view.client.LinkClientDictServiceTypeListModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.linkClientDictServiceTypeList',

    data : {

    },
    stores : {
        linkClientDictServiceTypeListStore : {

            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictServiceType/linkClientDictServiceType'),
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
            }, {
                name : 'description',
                type : 'string'
            }, {
                name : 'statusName',
                type : 'string'
            }, {
                name : 'amount',
                type : 'number'
            }, {
                name : 'amount30Days',
                type : 'number'
            }],
            data : [],
            sorters : 'name',
            listeners : {
                beforeload : 'onGridBeforeLoad',
                load : 'onGridStoreLoad'
            }
        }
    }
});