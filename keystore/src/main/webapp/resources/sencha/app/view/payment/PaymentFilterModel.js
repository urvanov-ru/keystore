Ext.define('KeyStore.view.payment.PaymentFilterModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.paymentFilter',
    data : {

    },
    stores : {
        paymentTypesStore : {
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/payment/paymentTypes'),
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
        },
        paymentStatusesStore : {
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/payment/paymentStatuses'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            fields : [{
                name : 'code',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            }],
            autoLoad : true
        },
        paymentMethodsStore : {
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/payment/paymentMethods'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            fields : [{
                name : 'code',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            }],
            autoLoad : true
        }
    }
});