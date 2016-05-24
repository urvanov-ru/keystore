Ext.define('KeyStore.view.report.PaymentChart', {
    extend : 'Ext.window.Window',
    xtype : 'paymentChart',
    requires : [ 'KeyStore.view.report.PaymentChartController',
            'KeyStore.view.report.PaymentChartModel', 'Ext.chart.CartesianChart',
            'Ext.panel.Panel', 'Ext.layout.container.Fit', 'Ext.button.Button',
            'Ext.chart.series.Series', 'Ext.chart.axis.Time',
            'Ext.chart.series.Line', 'Ext.chart.interactions.ItemHighlight',
            'Ext.chart.Legend', 'Ext.panel.Panel'],
    controller : 'paymentChart',
    viewModel : {
        type : 'paymentChart'
    },
    title : 'График активности клиента',
    autoShow : true,
    width : 640,
    height : 480,
    layout : 'fit',
    modal : true,
    items : [{
        xtype : 'panel',
        layout : 'fit',
        reference : 'panel'
    }]
});