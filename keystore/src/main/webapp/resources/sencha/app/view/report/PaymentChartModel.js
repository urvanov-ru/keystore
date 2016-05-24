Ext.define('KeyStore.view.report.PaymentChartModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.paymentChart',

    data : {

    },
    stores : {
        paymentStore : {

            fields : [{
                name : 'dateBegin',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'dateEnd',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'clients',
                type : 'number'
            }, {
                name : 'activeClients',
                type : 'number'
            }, {
                name : 'notActiveClients',
                type : 'number'
            }, {
                name : 'connections',
                type : 'number'
            }, {
                name : 'clientConnections',
                type : 'number'
            }, {
                name : 'serviceConnections',
                type : 'number'
            }, {
                name : 'sessionsTime',
                type : 'number'
            }, {
                name : 'clientSessionsTime',
                type : 'number'
            }, {
                name : 'serviceSessionsTime',
                type : 'number'
            }],
            data : []
            
        }
    }
});