Ext.define('KeyStore.view.report.ReportPaymentController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.reportPayment',
    

    onGenerateClick : function(self) {
        var controller = this;
        var form = controller.lookupReference('form');
        if (form.getForm().isValid()) {
            controller.lookupReference('infoLabel').setText('');
            var dateBegin = controller.lookupReference('dateBegin').getValue();
            var dateEnd = controller.lookupReference('dateEnd').getValue();
            var reportMode = controller.lookupReference('reportMode')
                    .getValue();
            
            window.open(globalUtils.toUrl('/report/payment?reportMode='
                    + reportMode + "&dateBegin="
                    + Ext.Date.format(dateBegin, 'Y-m-d') + "&dateEnd="
                    + Ext.Date.format(dateEnd, 'Y-m-d')));
        } else {
            controller.lookupReference('infoLabel').setText(
                    'Проверьте правильность заполнения формы.');
        }
    },
    
    onShowChartClick : function(self) {
        var controller = this;
        var form = controller.lookupReference('form');
        if (form.getForm().isValid()) {
            controller.lookupReference('infoLabel').setText('');
            var dateBegin = controller.lookupReference('dateBegin').getValue();
            var dateEnd = controller.lookupReference('dateEnd').getValue();
            var reportMode = controller.lookupReference('reportMode')
                    .getValue();
            
            var widget = Ext.widget('paymentChart');
            
            widget.getController().loadData({
                dateBegin : dateBegin,
                dateEnd : dateEnd,
                reportMode : reportMode
            });
        } else {
            controller.lookupReference('infoLabel').setText(
                    'Проверьте правильность заполнения формы.');
        }
    }
});