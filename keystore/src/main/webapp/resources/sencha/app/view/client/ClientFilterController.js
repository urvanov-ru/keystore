Ext.define('KeyStore.view.client.ClientFilterController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.clientFilter',
    id : 'clientFilter',
    
    onAcceptClick : function (self) {
        var  form = this.lookupReference('form');
        var infoLabel = this.lookupReference('infoLabel');
        if (!form.getForm().isValid()) {
            infoLabel.setText('Проверьте правильность заполнения формы.');
            return;
        }
        var controller = this;
        var active = controller.lookupReference('active').getValue();
        var activeName = null;
        if (active) {
            var activeStore = controller.getViewModel().getStore('activeStore');
            var activeIndex = activeStore.find('code', active);
            activeName = activeStore.getAt(activeIndex).get('name');
        }
        var activeBegin = controller.lookupReference('activeBegin').getValue();
        var activeEnd = controller.lookupReference('activeEnd').getValue();
        var dictClientGroupId = controller.lookupReference('dictClientGroupId').getValue();
        var dictClientGroupName = controller.lookupReference('dictClientGroupName').text;
        var dictActionId = controller.lookupReference('dictActionId').getValue();
        var dictActionName = controller.lookupReference('dictActionName').text;
        controller.fireEvent('filter',{
            active : active,
            activeName : activeName,
            activeBegin : activeBegin,
            activeEnd : activeEnd,
            dictClientGroupId : dictClientGroupId,
            dictClientGroupName : dictClientGroupName,
            dictActionId : dictActionId,
            dictActionName : dictActionName
        });
        this.getView().destroy();
    },
    
    onResetClick : function (self) {
        var  form = this.lookupReference('form');
        form.reset();
        this.fireEvent('filter',{
            active : null,
            activeName : null,
            activeBegin : null,
            activeEnd : null,
            dictClientGroupId : null,
            dictClientGroupName : null,
            dictActionId : null,
            dictActionName : null
        });
        this.getView().destroy();
    },
    
    
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    

    onDictClientGroupSelectClick : function(self) {
        var dictClientGroupId  = this.lookupReference('dictClientGroupId').getValue();
        var controller = this;
        var widget = Ext.widget('dictClientGroupSelect');
        widget.getController().setOkListener(function(arg0) {
            var id = arg0.id;
            var name = arg0.name;
            controller.lookupReference('dictClientGroupId').setValue(id);
            controller.lookupReference('dictClientGroupName').setText(name);
        });
        widget.getController().setSelectedId(dictClientGroupId);
    },
    
    loadData : function (data) {
        var controller = this;
        var active = controller.lookupReference('active');
        var activeBegin = controller.lookupReference('activeBegin');
        var activeEnd = controller.lookupReference('activeEnd');
        var dictClientGroupId = controller.lookupReference('dictClientGroupId');
        var dictClientGroupName = controller.lookupReference('dictClientGroupName');
        var dictActionId = controller.lookupReference('dictActionId');
        var dictActionName = controller.lookupReference('dictActionName');
        active.setValue(data.active);
        activeBegin.setValue(data.activeBegin);
        activeEnd.setValue(data.activeEnd);
        dictClientGroupId.setValue(data.dictClientGroupId);
        dictClientGroupName.setText(data.dictClientGroupName);
        dictActionId.setValue(data.dictActionId);
        dictActionName.setText(data.dictActionName);
    },
    
    onDictActionSelectClick : function (self) {
        var dictActionId  = this.lookupReference('dictActionId').getValue();
        var controller = this;
        var widget = Ext.widget('dictActionSelect');
        widget.getController().setOkListener(function(arg0) {
            var id = arg0.selection[0].id;
            var name = arg0.selection[0].name;
            controller.lookupReference('dictActionId').setValue(id);
            controller.lookupReference('dictActionName').setText(name);
        });
        widget.getController().setSelectedId(dictActionId);
    }
});