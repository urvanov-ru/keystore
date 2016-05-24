Ext.define('KeyStore.view.dictAction.DictActionSelectController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.dictActionSelect',
    
    onGridRender : function(self, eOpts) {
        this.getViewModel().getStore('dictActionSelectStore').load();
    },

    setOkListener : function(listener) {
        this.getViewModel().getData().okListener = listener;
    },
    
    setSelectionMode : function(selectionMode) {
        this.lookupReference('grid').setSelectionMode(selectionMode);
    },
    
    setSelectedIds : function (ids) {
        this.getViewModel().getData().selectedIds = ids;
    },
    
    onGridStoreLoad : function (self, records, successful, eOpts ) {
        if (successful) {
            this.lookupReference('infoLabel').setText('');
            var grid = this.lookupReference('grid');
            var selectedIds = this.getViewModel().getData().selectedIds;
            var selected = [];
            if (selectedIds) {
                for (var n = 0; n < selectedIds.length; n++) { 
                    selected.push(self.getById(selectedIds[n]));
                    
                }
            }
            grid.getSelectionModel().select(selected);
        } else {
            this.lookupReference('infoLabel').setText('Ошибка обновления таблицы.');
        }
    },

    onOkClick : function(self) {
        var grid = this.lookupReference('grid');
        var controller = this;
        var selections = grid.getSelection();
        if (selections.length == 0) {
            Ext.MessageBox.show({
                title : 'Внимание',
                msg : 'Выберите запись',
                buttons : Ext.MessageBox.OK,
                icon : Ext.MessageBox.INFO,
                fn : function() {
                    controller.getView().destroy();
                }
            });
            return;
        }
        var okListener = this.getViewModel().getData().okListener;

        if (okListener) {
            var selArray = [];
            for (var n = 0; n < selections.length; n++) {
                selArray.push({
                    id : selections[n].get('id'),
                    name : selections[n].get('name')
                });
            }
            okListener({
                selection : selArray
            });
        }
        controller.getView().destroy();
    },

    onCancelClick : function(self) {
        this.getView().destroy();
    }
});