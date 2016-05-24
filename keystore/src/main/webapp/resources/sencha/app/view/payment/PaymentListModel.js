Ext.define('KeyStore.view.payment.PaymentListModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.paymentList',
    data : {
        filter : {
            clientName : null,
            paymentType : null,
            paymentTypeName : null,
            status : null,
            statusName : null,
            method : null,
            methodName : null,
            createdAtBegin : null,
            createdAtEnd : null
        }
    },
    stores : {
        paymentListStore : {
            proxy : {
                type: 'ajax',
                actionMethods: {
                    create : 'POST',
                    read   : 'POST',
                    update : 'POST',
                    destroy: 'POST'
                },
                url : globalUtils.toUrl('/payment/list'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            groupField : '',
            groupField : '',
            fields : [ {
                name : 'id',
                type : 'string'
            }, {
                name : 'createdAt',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'orderCreatedAt',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'orderDictServiceTypeName',
                type : 'string'
            }, {
                name : 'paymentType',
                type : 'string'
            }, {
                name : 'paymentTypeName',
                type : 'string'
            }, {
                name : 'status',
                type : 'string'
            }, {
                name : 'statusName',
                type : 'string'
            }, {
                name : 'method',
                type : 'string'
            }, {
                name : 'methodName',
                type : 'string'
            }, {
                name : 'clientName',
                type : 'string'
            }, {
                name : 'amountWithCommission',
                type : 'number'
            }, {
                name : 'amountWithoutCommission',
                type : 'number'
            }, {
                name : 'amountOfCommission',
                type : 'number'
            }, {
                name : 'info',
                type : 'string'
            }, {
                name : 'comment',
                type : 'string'
            }],
            sorters : 'createdAt',
            listeners : {
                beforeload : 'onGridBeforeLoad',
                load : 'onGridLoad'
            }
        }
    }
});