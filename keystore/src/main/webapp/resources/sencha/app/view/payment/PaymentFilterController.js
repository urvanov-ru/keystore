Ext.define('KeyStore.view.payment.PaymentFilterController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.paymentFilter',
    id : 'paymentFilter',
    
    onAcceptClick : function(self) {
        var  form = this.lookupReference('form');
        var infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var controller = this;
        var clientName = controller.lookupReference('clientName').getValue();
        var paymentType = controller.lookupReference('paymentType').getValue();
        var paymentTypeName = null;
        if (paymentType) {
            var paymentTypesStore = controller.getViewModel().getStore('paymentTypesStore');
            var paymentTypeIndex = paymentTypesStore.find('code', paymentType);
            paymentTypeName = paymentTypesStore.getAt(paymentTypeIndex).get('name'); 
        }
        var status = controller.lookupReference('status').getValue();
        var statusName = null;
        if (status) {
            var paymentStatusesStore = controller.getViewModel().getStore('paymentStatusesStore');
            var statusIndex = paymentStatusesStore.find('code', status);
            statusName = paymentStatusesStore.getAt(statusIndex).get('name');
        }
        var method = controller.lookupReference('method').getValue();
        var methodName = null;
        if (method) {
            var paymentMethodsStore = controller.getViewModel().getStore('paymentMethodsStore');
            var paymentMethodIndex = paymentMethodsStore.find('code', method);
            methodName = paymentMethodsStore.getAt(paymentMethodIndex).get('name');
        }
        var createdAtBegin = controller.lookupReference('createdAtBegin').getValue();
        var createdAtEnd = controller.lookupReference('createdAtEnd').getValue();
        controller.fireEvent('filter',{
            clientName : clientName,
            paymentType : paymentType,
            paymentTypeName : paymentTypeName,
            status : status,
            statusName : statusName,
            method : method,
            methodName : methodName,
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
            paymentType : null,
            status : null,
            method : null,
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
        var paymentType = controller.lookupReference('paymentType');
        var status = controller.lookupReference('status');
        var method = controller.lookupReference('method');
        var createdAtBegin = controller.lookupReference('createdAtBegin');
        var createdAtEnd = controller.lookupReference('createdAtEnd');
        clientName.setValue(data.clientName);
        paymentType.setValue(data.paymentType);
        status.setValue(data.status);
        method.setValue(data.method);
        createdAtBegin.setValue(data.createdAtBegin);
        createdAtEnd.setValue(data.createdAtEnd);
    }
});