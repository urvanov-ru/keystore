Ext.define('KeyStore.view.dictClientGroup.DictClientGroupListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.dictClientGroupList',
    onTreeRender : function(self, eOpts) {
        
    },
    
    onRender : function (self, eOpts) {
        this.getViewModel().getStore('dictClientGroupListStore').load();
    },

    onAddClick : function(self) {
        Ext.widget('dictClientGroupEdit');
    },

    onEditClick : function(self) {
        var selectionArray = this.lookupReference('tree').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('dictClientGroupEdit');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                id : id
            });
        }
    },

    listen : {
        controller : {
            '#dictClientGroupEdit' : {
                save : 'onSave'
            }
        }
    },

    onSave : function() {
        this.getViewModel().getStore('dictClientGroupListStore').reload();
    },
    
    onTreeLoad : function(store, records, successful, operation, node, eOpts) {
        var statusShowRecords = this.lookupReference('statusShowRecords');
        var statusTotalRecords = this.lookupReference('statusTotalRecords');
        var infoLabel = this.lookupReference('infoLabel');
        if (successful) {
            infoLabel.setText('');
            statusShowRecords.setText(records.length);
            statusTotalRecords.setText(store.getProxy().getReader().rawData.totalRecords);
        } else {
            infoLabel.setText('Ошибка обновления таблицы.');
        }
    },
    
    onTreeSelectionChange : function( tree, selected, eOpts ) {
        var statusSelected = this.lookupReference('statusSelected');
        statusSelected.setText(selected.length);
    }
    
});