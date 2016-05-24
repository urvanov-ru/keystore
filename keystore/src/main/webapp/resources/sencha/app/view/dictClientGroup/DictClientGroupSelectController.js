Ext.define('KeyStore.view.dictClientGroup.DictClientGroupSelectController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.dictClientGroupSelect',
    
    onTreeRender : function(self, eOpts) {
        this.getViewModel().getStore('dictClientGroupSelectStore').load();
    },

    setOkListener : function(listener) {
        this.getViewModel().getData().okListener = listener;
    },
    
    setSelectedId : function (id) {
        this.getViewModel().getData().selectedId = id;
    },
    
    onTreeStoreLoad : function (self, records, successful, operation, node, eOpts ) {
        if (successful) {
            this.lookupReference('infoLabel').setText('');
            var tree = this.lookupReference('tree');
            var selectedId = this.getViewModel().getData().selectedId;
            if (selectedId) {
                var selected = self.getById(selectedId);
                if (selected) tree.getSelectionModel().select(selected);
            }
        } else {
            this.lookupReference('infoLabel').setText('Ошибка обновления дерева.');
        }
    },

    onOkClick : function(self) {
        var tree = this.lookupReference('tree');
        var controller = this;
        var selections = tree.getSelection();
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
            okListener({
                id : selections[0].get('id'),
                name : selections[0].get('text')
            });
        }
        controller.getView().destroy();
    },

    onCancelClick : function(self) {
        this.getView().destroy();
    }

   
});