Ext.define('KeyStore.view.order.OrderListModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.orderList',
    data : {
        filter : {
            clientName : null,
            status : null,
            statusName : null,
            createdAtBegin : null,
            createdAtEnd : null
        }
    },
    stores : {
        
        orderListStore : {
            proxy : {
                type: 'ajax',
                actionMethods: {
                    create : 'POST',
                    read   : 'POST',
                    update : 'POST',
                    destroy: 'POST'
                },
                url : globalUtils.toUrl('/order/list'),
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
                name : 'dictServiceTypeName',
                type : 'string'
            }, {
                name : 'status',
                type : 'string'
            },{
                name : 'statusName',
                type : 'string'
            }, {
                name : 'createdByName',
                type : 'string'
            }, {
                name : 'createdAt',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'payDateTime',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'completedDateTime',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'clientName',
                type : 'string'
            } ],
            data : [],
            sorters : 'createdAt',
            listeners : {
                beforeload : 'onGridBeforeLoad',
                load : 'onGridLoad'
            }
        }
    }
});