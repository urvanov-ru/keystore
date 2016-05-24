Ext.define('KeyStore.view.order.OrderFilterController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.orderFilter',
    id : 'orderFilter',
    
    onAcceptClick : function(self) {
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
            var orderStatusesStore = controller.getViewModel().getStore('orderStatusesStore');
            var statusIndex = orderStatusesStore.find('code', status);
            statusName = orderStatusesStore.getAt(statusIndex).get('name');
        }
        var createdAtBegin = controller.lookupReference('createdAtBegin').getValue();
        var createdAtEnd = controller.lookupReference('createdAtEnd').getValue();
        controller.fireEvent('filter',{
            clientName : clientName,
            status : status,
            statusName : statusName,
            createdAtBegin : createdAtBegin,
            createdAtEnd : createdAtEnd
        });
        this.getView().destroy();
    },
    
    onResetClick : function(self) {
        var  form = this.lookupReference('form');
        form.reset();
        this.fireEvent('filter',{
            clientName : null,
            status : null,
            createdAtBegin : null,
            createdAtEnd : null
        });
        this.getView().destroy();
    },
    onCancelClick : function(self) {
        this.getView().destroy();
    },
    
    loadData : function (data) {
        var controller = this;
        var clientName = controller.lookupReference('clientName');
        var status = controller.lookupReference('status');
        var createdAtBegin = controller.lookupReference('createdAtBegin');
        var createdAtEnd = controller.lookupReference('createdAtEnd');
        clientName.setValue(data.clientName);
        status.setValue(data.status);
        createdAtBegin.setValue(data.createdAtBegin);
        createdAtEnd.setValue(data.createdAtEnd);
    }
});