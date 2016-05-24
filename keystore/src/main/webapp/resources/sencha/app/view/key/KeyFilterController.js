Ext.define('KeyStore.view.key.KeyFilterController', {
    id : 'keyFilter',
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.keyFilter',
    onAcceptClick : function (self) {
        var  form = this.lookupReference('form');
        var infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var controller = this;
        var clientName = controller.lookupReference('clientName').getValue();
        var status = controller.lookupReference('status').getValue();
        var statusName = null;
        if (status) {
            var keyStatusStore = controller.getViewModel().getStore('keyStatusStore');
            var statusIndex = keyStatusStore.find('code', status);
            statusName = keyStatusStore.getAt(statusIndex).get('name');
        }
        var activeOnDate = controller.lookupReference('activeOnDate').getValue();
        controller.fireEvent('filter',{
            clientName : clientName,
            status : status,
            statusName : statusName,
            activeOnDate : activeOnDate
        });
        this.getView().destroy();
    },
    
    
    onResetClick : function (self) {
        var  form = this.lookupReference('form');
        form.reset();
        this.fireEvent('filter',{
            clientName : null,
            status : null,
            statusName : null
        });
        this.getView().destroy();
    },
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    
    loadData : function (data) {
        var controller = this;
        var clientName = controller.lookupReference('clientName');
        var status = controller.lookupReference('status');
        var activeOnDate = controller.lookupReference('activeOnDate');
        clientName.setValue(data.clientName);
        status.setValue(data.status);
        activeOnDate.setValue(data.activeOnDate);
    }
});