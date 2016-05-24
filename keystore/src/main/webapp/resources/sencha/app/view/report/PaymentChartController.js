Ext.define('KeyStore.view.report.PaymentChartController', {
    extend : 'Ext.app.ViewController',
    requires : [ 'Ext.chart.CartesianChart',
                'Ext.panel.Panel', 'Ext.layout.container.Fit', 'Ext.button.Button',
                'Ext.chart.series.Series', 'Ext.chart.axis.Time',
                'Ext.chart.series.Line', 'Ext.chart.interactions.ItemHighlight',
                'Ext.chart.Legend'],
    alias : 'controller.paymentChart',
    
    loadData : function (obj) {
        var controller = this;
        globalUtils.ajaxRequest({
            url : '/report/paymentJson?reportMode='
                + obj.reportMode + "&dateBegin="
                + Ext.Date.format(obj.dateBegin, 'Y-m-d') + "&dateEnd="
                + Ext.Date.format(obj.dateEnd, 'Y-m-d'),
            success : function(xhr, opts) {
                //try {
                    var answer = Ext.decode(xhr.responseText);

                    if (answer.success) {
                        
                        

                        var lastDictServiceTypeId = null;
                        var preparedData = [];
                        var seriesArray = [];
                        var storeFields = [{name : 'dateBegin',type : 'date', dateReadFormat : 'time'},
                                           {name : 'dateEnd',type : 'date', dateReadFormat : 'time'}];
                        var index = 0;
                        for (var n = 0; n < answer.info.length; n++) {
                            var dictServiceTypeId = answer.info[n].dictServiceTypeId;
                            var dictServiceTypeName = answer.info[n].dictServiceTypeName;
                            if (dictServiceTypeId == null) dictServiceTypeId = 0;
                            if (dictServiceTypeName == null) dictServiceTypeName = 'Общий';
                            if (dictServiceTypeId != lastDictServiceTypeId) {
                                index = 0;
                                lastDictServiceTypeId = dictServiceTypeId;
                                var series ={
                                    type: 'line',
                                    xField: 'dateBegin',
                                    yField: 'profit'+dictServiceTypeId,
                                    title : 'Доход по пакету ' + dictServiceTypeName,
                                    hidden : true,
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
                                };
                                seriesArray.push(series);
                                series ={
                                    type: 'line',
                                    xField: 'dateBegin',
                                    yField: 'orderPaymentAmount'+dictServiceTypeId,
                                    title : 'Поступления по пакету ' + dictServiceTypeName,
                                    hidden : true,
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
                                };
                                seriesArray.push(series);
                                
                                series ={
                                        type: 'line',
                                        xField: 'dateBegin',
                                        yField: 'payBackAmount'+dictServiceTypeId,
                                        title : 'Возвраты по пакету ' + dictServiceTypeName,
                                        hidden : true,
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
                                    };
                                    seriesArray.push(series);
                                    
                                    series ={
                                            type: 'line',
                                            xField: 'dateBegin',
                                            yField: 'activeKeys'+dictServiceTypeId,
                                            title : 'Активные ключи по пакету ' + dictServiceTypeName,
                                            hidden : true,
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
                                        };
                                        seriesArray.push(series);
                                        series ={
                                                type: 'line',
                                                xField: 'dateBegin',
                                                yField: 'newKeys'+dictServiceTypeId,
                                                title : 'Новые ключи ' + dictServiceTypeName,
                                                hidden : true,
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
                                            };
                                            seriesArray.push(series);
                                            series ={
                                                    type: 'line',
                                                    xField: 'dateBegin',
                                                    yField: 'canceledKeys'+dictServiceTypeId,
                                                    title : 'Отменённые ключи ' + dictServiceTypeName,
                                                    hidden : true,
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
                                                };
                                                seriesArray.push(series);
                                                
                                                
                                                
                                storeFields.push({name : 'profit'+dictServiceTypeId, type : 'number'});
                                storeFields.push({name : 'orderPaymentAmount'+dictServiceTypeId, type : 'number'});
                                storeFields.push({name : 'payBackAmount'+dictServiceTypeId, type : 'number'});
                                storeFields.push({name : 'activeKeys'+dictServiceTypeId, type : 'number'});
                                storeFields.push({name : 'newKeys'+dictServiceTypeId, type : 'number'});
                                storeFields.push({name : 'canceledKeys'+dictServiceTypeId, type : 'number'});
                            }
                            
                            if (preparedData.length == index) {
                                preparedData.length = index + 1;
                                preparedData[index] = {};
                            }
                            preparedData[index]["dateBegin"] = answer.info[n].dateBegin;
                            preparedData[index]["profit" + dictServiceTypeId] = answer.info[n].profit;
                            preparedData[index]["orderPaymentAmount" + dictServiceTypeId] = answer.info[n].orderPaymentAmount;
                            preparedData[index]["payBackAmount" + dictServiceTypeId] = answer.info[n].payBackAmount;
                            preparedData[index]["activeKeys" + dictServiceTypeId] = answer.info[n].activeKeys;
                            preparedData[index]["newKeys" + dictServiceTypeId] = answer.info[n].newKeys;
                            preparedData[index]["canceledKeys" + dictServiceTypeId] = answer.info[n].canceledKeys;
                            index++;
                        }
                        var store = Ext.create('Ext.data.Store', {
                            autoDestroy : true,
                            fields : storeFields,
                            data : preparedData
                        });
                        var chart = Ext.create('Ext.chart.CartesianChart', {
                                xtype : 'cartesian',
                                store : store,
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
                                series : seriesArray
                            });
                        var panel = controller.lookupReference('panel');
                        panel.removeAll();
                        panel.add(chart);
                    } else {
                        if (console)
                            console.log(answer.message);
                        Ext.MessageBox.show({
                            title : 'Ошибка',
                            msg : answer.message,
                            buttons : Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                //} catch (exception) {
                //    if (console)
                //        console.log('Ошибка обработки ответа сервера ' + exception);
                //    Ext.MessageBox.show({
                //        title : 'Ошибка',
                //        msg : 'Ошибка обработки ответа от сервера',
                //        icon : Ext.MessageBox.ERROR,
                //        buttons : Ext.MessageBox.OK
                //    });
                //}
            },
            failure : function(xhr, opts) {
                if (console)
                    console.log('Ошибка загрузки данных.');
                
                switch (xhr.status) {
                case 403:
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Доступ запрещён.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR});
                    break;
                default :
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка загрузки данных.',
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR});
                    break;
                }
            }
        });
    }
    

});