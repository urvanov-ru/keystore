Ext.define('KeyStore.view.report.ReportActivityModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.reportActivity',

    data : {

    }, 
    stores : {
        reportModesStore : {
            data : [{
                id : '1',
                name : 'По годам'
            }, {
                id : '2',
                name : 'По кварталам'
            }, {
                id : '3',
                name : 'По месяцам'
            }, {
                id : '4',
                name : 'По неделям'
            }, {
                id : '5',
                name : 'По дням'
            }],
            fields : [{
                name : 'id',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            }]
        }
    }
});