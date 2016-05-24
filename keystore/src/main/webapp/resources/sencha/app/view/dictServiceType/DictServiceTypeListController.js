Ext.define('KeyStore.view.dictServiceType.DictServiceTypeListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.dictServiceTypeList',

    onGridRender : function(self, eOpts) {
        this.getViewModel().getStore('dictServiceTypeListStore').load();
    },

    onAddClick : function(self) {
        var widget = Ext.widget('dictServiceTypeEdit');
    },

    onEditClick : function(self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('dictServiceTypeEdit');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                id : id
            });
        }
    },

    onBaseKeyClick : function(self) {
        var widget = Ext.widget('baseKeyEdit');
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                dictServiceTypeId : id
            });
        }
    },
    
    listen : {
        controller : {
            '#dictServiceTypeEdit' : {
                save : 'onSave'
            }
        }
    },

    onGridBeforeLoad : function(store, operation, eOpts) {

    },

    onGridLoad : function(store, records, successful, eOpts) {
        var statusShowRecords = this.lookupReference('statusShowRecords');
        var statusTotalRecords = this.lookupReference('statusTotalRecords');
        var infoLabel = this.lookupReference('infoLabel');
        if (successful) {
            infoLabel.setText('');
            statusShowRecords.setText(records.length);
            statusTotalRecords.setText(store.getProxy().getReader().rawData.totalRecords);
        } else {
            switch (eOpts.error.status) {
            case 401:
                globalUtils.showSessionLostMessage();
                break;
            default :
                infoLabel.setText('Ошибка обновления таблицы.');
                break;
            }
        }
        this.updateButtonsEnable();
    },
    
    onGridSelectionChange : function( grid, selected, eOpts ) {
        var statusSelected = this.lookupReference('statusSelected');
        statusSelected.setText(selected.length);
        this.updateButtonsEnable();
    },
    
    
    updateButtonsEnable : function () {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        var editButton = controller.lookupReference('editButton');
        var baseKeyButton = controller.lookupReference('baseKeyButton');
        var giveToClientButton = controller.lookupReference('giveToClientButton');
        if (selected.length > 0) {
            editButton.enable();
            baseKeyButton.enable();
            giveToClientButton.enable();
        } else {
            editButton.disable();
            baseKeyButton.disable();
            giveToClientButton.disable();
        }
    },

    
    onSave : function () {
        this.getViewModel().getStore('dictServiceTypeListStore').reload();
    },
    
    onGiveToClientClicked : function (self) {
        Ext.widget('clientSelect');
    }
});