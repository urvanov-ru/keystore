Ext.define('KeyStore.view.dictServiceType.DictServiceTypeSelectModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.dictServiceTypeSelect',

    data : {
        okListener : null,
        selectedIds : []
    },
    stores : {
        dictServiceTypeSelectStore : {

            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictServiceType/select'),
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
                load : 'onGridStoreLoad'
            }
        }
    }
});