Ext.define('KeyStore.view.dictAction.DictActionListController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.dictActionList',
    onGridRender : function (self, eOpts) {
        var controller = this;
        if (globalData.user.accesses.SERVICE_WRITE_ACTION)
            controller.lookupReference('addButton').enable();
        if (globalData.user.accesses.SERVICE_EXPORT_ACTION)
            controller.lookupReference('exportButton').enable();
    },
    
    onAddClick : function (self) {
        Ext.widget('dictActionEdit');
    },
    
    onEditClick : function (self) {
        var selectionArray = this.lookupReference('grid').getSelection();
        if (selectionArray.length > 0) {
            var widget = Ext.widget('dictActionEdit');
            var selection = selectionArray[0];
            var id = selection.get('id');
            widget.getController().loadData({
                id : id
            });
        }
    },
    
    loadData : function (obj) {
        this.getViewModel().getStore('dictActionListStore').load();
    },
    
    listen : {
        controller : {
            '#dictActionEdit' : {
                save : 'onSave'
            },
            '#dictActionStart' : {
                start : 'onStart'
            }
        }
    },
    
    onSave : function () {
        this.getViewModel().getStore('dictActionListStore').reload();
    },
    
    onStart : function() {
        this.getViewModel().getStore('dictActionListStore').reload();
    },
    
    onExportClick : function () {
        window.open(globalUtils.toUrl('/dictAction/exportXls/dictActionList'));
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
    
    onGridSelectionChange : function(grid, selected, eOpts) {
        var statusSelected = this.lookupReference('statusSelected');
        statusSelected.setText(selected.length);
        
        this.updateButtonsEnable();
    },
    
    updateButtonsEnable : function () {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
        var editButton = this.lookupReference('editButton');
        //var linkClientDictActionListButton= this.lookupReference('linkClientDictActionListButton');
        var startButton = this.lookupReference('startButton');
        if (selected.length > 0) {
            
            editButton.enable();
            //linkClientDictActionListButton.enable();
            if (selected[0].get('dateBegin') == null) startButton.enable();
        } else {
            editButton.disable();
            //linkClientDictActionListButton.disable();
            startButton.disable();
        }
    },
    
    /*
    onLinkClientDictActionListClick : function (self) {
        var controller =this;
        var selected = controller.lookupReference('grid').getSelectionModel().getSelection();
        if (selected.length > 0) {
            selectedId = selected[0].get('id');
            var widget = Ext.widget('clientSelect');
            widget.getController().loadData({dictActionId : selectedId});
        }
    }*/

    onStartClick : function (self) {
        var controller = this;
        var grid = controller.lookupReference('grid');
        var selected = grid.getSelectionModel().getSelection();
        if (selected.length > 0 ) {
            var selectedId = selected[0].get('id');
            var widget = Ext.widget('dictActionStart');
            widget.getController().loadData({ id : selectedId});
        }
    }
});