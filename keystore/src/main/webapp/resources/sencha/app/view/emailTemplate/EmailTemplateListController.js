Ext.define('KeyStore.view.emailTemplate.EmailTemplateListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.emailTemplateList',

    onGridRender : function(self, eOpts) {
    },

    loadData : function(obj) {
        this.getViewModel().getStore('emailTemplateListStore').load();
    },

    onEditClick : function(self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('emailTemplateEdit');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                id : id
            });
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
        if (selected.length > 0) {
            editButton.enable();
        } else {
            editButton.disable();
        }
    }
});