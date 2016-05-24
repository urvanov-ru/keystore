Ext.define('KeyStore.view.dictServiceType.DictServiceTypeListModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.dictServiceTypeList',

    data : {
        name : 'KeyStore'
    },
    stores : {
        dictServiceTypeListStore : {

            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictServiceType/list'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },

            groupField : '',
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
                load : 'onGridLoad'
            }
        }
    }
});