Ext.define('KeyStore.view.report.ReportActivityController', {
    extend : 'Ext.app.ViewController',
    requires : [ 'Ext.Date' ],
    alias : 'controller.reportActivity',

    onGenerateClick : function(self) {
        var controller = this;
        var form = controller.lookupReference('form');
        if (form.getForm().isValid()) {
            controller.lookupReference('infoLabel').setText('');
            var dateBegin = controller.lookupReference('dateBegin').getValue();
            var dateEnd = controller.lookupReference('dateEnd').getValue();
            var reportMode = controller.lookupReference('reportMode')
                    .getValue();
            var clientId = controller.lookupReference('clientId').getValue();
            
            window.open(globalUtils.toUrl('/report/activity?reportMode='
                    + reportMode + "&dateBegin="
                    + Ext.Date.format(dateBegin, 'Y-m-d') + "&dateEnd="
                    + Ext.Date.format(dateEnd, 'Y-m-d') + "&clientId="+clientId));
        } else {
            controller.lookupReference('infoLabel').setText(
                    'Проверьте правильность заполнения формы.');
        }
    },
    
    onSelectClientClick : function (self) {
        var controller = this;
        var widget = Ext.widget('clientSelect');
        widget.getController().setOkListener(function(arg0) {
            var id = arg0.ids[0];
            var name = arg0.names[0];
            controller.lookupReference('clientId').setValue(id);
            controller.lookupReference('clientName').setValue(name);
        });
    },
    
    onShowChartClick : function (self) {
        var controller = this;
        var form = controller.lookupReference('form');
        if (form.getForm().isValid()) {
            controller.lookupReference('infoLabel').setText('');
            var dateBegin = controller.lookupReference('dateBegin').getValue();
            var dateEnd = controller.lookupReference('dateEnd').getValue();
            var reportMode = controller.lookupReference('reportMode')
                    .getValue();
            var clientId = controller.lookupReference('clientId').getValue();
            var widget = null;
            if (clientId)
                widget = Ext.widget('clientActivityChart');
            else
                widget = Ext.widget('activityChart');
            
            widget.getController().loadData({
                dateBegin : dateBegin,
                dateEnd : dateEnd,
                reportMode : reportMode,
                clientId : clientId
            });
            
        } else {
            controller.lookupReference('infoLabel').setText(
                    'Проверьте правильность заполнения формы.');
        } 
    }
});