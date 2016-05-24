Ext.define('KeyStore.view.key.KeyListModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.keyList',
    data : {
        filter : {
            clientName : null,
            status : null,
            statusName : null,
            activeOnDate : null
        }
    },
    stores : {
        keyListStore : {
            proxy : {
                type: 'ajax',
                actionMethods: {
                    create : 'POST',
                    read   : 'POST',
                    update : 'POST',
                    destroy: 'POST'
                },
                url : globalUtils.toUrl('/key/list'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            fields : [ {
                name : 'id',
                type : 'string'
            }, {
                name : 'dictServiceTypeName',
                type : 'string'
            }, {
                name : 'basic',
                type : 'string'
            }, {
                name : 'status',
                type : 'string'
            }, {
                name : 'statusName',
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
                name : 'dictActionId',
                type : 'string'
            } ],
            data : [],
            sorters : 'dictServiceTypeName',
            listeners : {
                beforeload : 'onGridBeforeLoad',
                load : 'onGridLoad'
            }
        }
    }
});