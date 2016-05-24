Ext.define('KeyStore.view.client.LinkClientDictActionListModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.linkClientDictActionList',

    data : {

    },
    stores : {
        linkClientDictActionListStore : {

        proxy : {
            type : 'ajax',
            url : globalUtils.toUrl('/dictAction/linkClientDictAction'),
            reader : {
                type : 'json',
                rootProperty : 'info'
            }
        },
        fields : [{
            name : 'id',
            type : 'string'
        }, {
            
            name : 'name',
            type : 'string'
        }, {
            name : 'description',
            type : 'string'
        }, {
            name : 'dateBegin',
            type : 'date',
            dateFormat : 'time'
        }, {
            name : 'dateEnd',
            type : 'date',
            dateFormat : 'time'
        }, {
            name : 'dictServiceTypeName',
            type : 'string'
        }, {
            name : 'dictActionTypeName',
            type : 'string'
        }, {
            name : 'forNewClients',
            type : 'string'
        }],
        data : [],
        sorters : 'name',
        listeners : {
            beforeload : 'onGridBeforeLoad',
            load : 'onGridStoreLoad'
        }
    }}
});