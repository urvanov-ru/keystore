Ext.define('KeyStore.view.report.ClientActivityChartController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.clientActivityChart',
    
    loadData : function (obj) {
        var controller = this;
        globalUtils.ajaxRequest({
            url : '/report/activityJson?reportMode='
                + obj.reportMode + "&dateBegin="
                + Ext.Date.format(obj.dateBegin, 'Y-m-d') + "&dateEnd="
                + Ext.Date.format(obj.dateEnd, 'Y-m-d') + "&clientId="+obj.clientId,
            success : function(xhr, opts) {
                try {
                    var answer = Ext.decode(xhr.responseText);

                    if (answer.success) {
                        controller.getViewModel().getStore('activityStore').loadData(answer.info);
                        
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
                } catch (exception) {
                    if (console)
                        console.log('Ошибка обработки ответа сервера');
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Ошибка обработки ответа от сервера',
                        icon : Ext.MessageBox.ERROR,
                        buttons : Ext.MessageBox.OK
                    });
                }
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