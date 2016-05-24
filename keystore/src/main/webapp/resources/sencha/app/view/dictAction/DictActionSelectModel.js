Ext.define('KeyStore.view.dictAction.DictActionSelectModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.dictActionSelect',


    data : {
        okListener : null,
        selectedIds : []
    },
    stores : {
        dictActionSelectStore : {

            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictAction/select'),
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
                load : 'onGridStoreLoad'
            }
        }
    }
});