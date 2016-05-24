Ext.define('KeyStore.view.report.ActivityChart', {
    extend : 'Ext.window.Window',
    xtype : 'activityChart',
    requires : [ 'KeyStore.view.report.ActivityChartController',
            'KeyStore.view.report.ActivityChartModel', 'Ext.chart.CartesianChart',
            'Ext.panel.Panel', 'Ext.layout.container.Fit', 'Ext.button.Button',
            'Ext.chart.series.Series', 'Ext.chart.axis.Time',
            'Ext.chart.series.Line', 'Ext.chart.interactions.ItemHighlight',
            'Ext.chart.Legend'],
    controller : 'activityChart',
    viewModel : {
        type : 'activityChart'
    },
    title : 'График активности',
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
            fields: ['clients', 'activeClients', 'notActiveClients',
                     'connections', 'clientConnections', 'serviceConnections',
                     'sessionsTime', 'clientSessionsTime',
                     'serviceSessionsTime'],
            position: 'left',
            grid: true,
            title : 'единиц / секунд'
        }],
        series : [{
            type: 'line',
            xField: 'dateBegin',
            yField: 'clients',
            title : 'Число клиентов',
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
        },{
            type: 'line',
            xField: 'dateBegin',
            yField: 'activeClients',
            title : 'Число активных клиентов',
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
            yField: 'notActiveClients',
            title : 'Число не активных клиентов',
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
            yField: 'clientConnections',
            //hidden : true,
            title : 'Число клиентских подключений',
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
        }, 
        {
            type: 'line',
            xField: 'dateBegin',
            yField: 'serviceConnections',
            //hidden : true,
            title : 'Число сервисных подключений',
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
        }, {
            type: 'line',
            xField: 'dateBegin',
            yField: 'connections',
            //hidden : true,
            title : 'Длительность клиентских сессий',
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
            yField: 'connections',
            //hidden : true,
            title : 'Длительность сервисных сессий',
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