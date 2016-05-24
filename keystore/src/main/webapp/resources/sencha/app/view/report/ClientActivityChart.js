Ext.define('KeyStore.view.report.ClientActivityChart', {
    extend : 'Ext.window.Window',
    xtype : 'clientActivityChart',
    requires : [ 'KeyStore.view.report.ClientActivityChartController',
            'KeyStore.view.report.ClientActivityChartModel', 'Ext.chart.CartesianChart',
            'Ext.panel.Panel', 'Ext.layout.container.Fit', 'Ext.button.Button',
            'Ext.chart.series.Series', 'Ext.chart.axis.Time',
            'Ext.chart.series.Line', 'Ext.chart.interactions.ItemHighlight',
            'Ext.chart.Legend'],
    controller : 'clientActivityChart',
    viewModel : {
        type : 'clientActivityChart'
    },
    title : 'График активности клиента',
    autoShow : true,
    width : 640,
    height : 480,
    layout : 'fit',
    modal : true,
    items : [{
        xtype : 'cartesian',
        bind : {
            store : '{activityStore}'
        },
        reference : 'chart',
        insetPadding: 40,
        legend : {
            xtype : 'legend',
            docked : 'right',
            border : 1
        },
        axes: [{
            type: 'time',
            fields: 'dateBegin',
            position: 'bottom',
            grid: true,
            label: {
                rotate: {
                    degrees: -90
                }
            }
            //minimum: 0,
            //maximum: 24,
            //renderer: function (v, layoutContext) {
                // Custom renderer overrides the native axis label renderer.
                // Since we don't want to do anything fancy with the value
                // ourselves except appending a '%' sign, but at the same time
                // don't want to loose the formatting done by the native renderer,
                // we let the native renderer process the value first.
            //    return layoutContext.renderer(v) + '%';
            //}
        }, {
            type: 'numeric',
            miminum : 0,
            fields: [
                     'connections', 
                     'sessionsTime'],
            position: 'left',
            grid: true,
            title : 'единиц / секунд'
        }],
        series : [{
            type: 'line',
            xField: 'dateBegin',
            yField: 'connections',
            //hidden : true,
            title : 'Общее число подключений',
            marker: true,
            highlightCfg: {
                scaling: 2
            },
            tooltip: {
                trackMouse: true,
                style: 'background: #fff',
                renderer: function(storeItem, item) {
                    var title = item.series.getTitle();
                    this.setHtml(title + ': ' + storeItem.get(item.series.getYField()));
                }
            }
        }, {
            type: 'line',
            xField: 'dateBegin',
            yField: 'sessionsTime',
            //hidden : true,
            title : 'Общая длительность сессий',
            marker: true,
            highlightCfg: {
                scaling: 2
            },
            tooltip: {
                trackMouse: true,
                style: 'background: #fff',
                renderer: function(storeItem, item) {
                    var title = item.series.getTitle();
                    this.setHtml(title + ': ' + storeItem.get(item.series.getYField()));
                }
            }
        }
        ]
    }]
});