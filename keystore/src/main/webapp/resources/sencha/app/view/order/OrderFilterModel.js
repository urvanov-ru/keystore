Ext.define('KeyStore.view.order.OrderFilterModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.orderFilter',
    data : {

    },
    stores : {
        orderStatusesStore : {

            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/order/orderStatuses'),
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
        }
    }
});