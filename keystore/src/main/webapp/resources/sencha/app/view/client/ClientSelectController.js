Ext.define('KeyStore.view.client.ClientSelectController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.clientSelect',
    id : 'clientSelect',
    
    onGridRender : function (self, eOpts) {
        this.getViewModel().getStore('clientSelectStore').load();
    },
    
    setOkListener : function(listener) {
        this.getViewModel().getData().okListener = listener;
    },
    
    setSelectedIds : function (ids) {
        this.getViewModel().getData().selectedIds = ids;
    },
    
    onOkClick : function (self) {
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

        var ids = [];
        var names = [];
        for (var n = 0; n < selections.length; n++) {
            ids.push(selections[n].get('id'));
            names.push(selections[n].get('name'));
        }
        if (okListener) {
            okListener({
                ids : ids,
                names : names
            });
        }
        controller.getView().destroy();
    },
    
    onCancelClick : function (self) {
        this.getView().destroy();
    },
    
    
    onFilterClick : function (self) {
        var widget = Ext.widget('clientFilter');
        widget.getController().loadData(this.getViewModel().getData().filter);
    },
    
    listen : {
        controller : {
            '#clientFilter' : {
                filter : 'onFilter'
            }
        }
    },
    
    onFilter : function(filter) {
        var clientListStore = this.getViewModel().getStore('clientListStore');
        this.getViewModel().getData().filter = filter;
        var filterString = '';
        if (filter.active) filterString += ' Статус ' + filter.activeName;
        if (filter.dictClientGroupId) filterString += ' Группа ' + filter.dictClientGroupName;
        if (filter.activeBegin) filterString += ' Активность с ' + Ext.Date.format(filter.activeBegin, 'd.m.Y');
        if (filter.activeEnd) filterString += ' Активность по ' + Ext.Date.format(filter.activeEnd, 'd.m.Y');
        if (filterString == '') filterString = ' нет ';
        this.lookupReference('statusFilter').setText(filterString);
        clientListStore.load({
            params : {
                active : filter.active == 'ACTIVE' ? true : filter.active == 'BLOCKED'? false : null,
                dictClientGroupId : filter.dictClientGroupId,
                activeBegin : filter.activeBegin == null ? null
                        : filter.activeBegin.getTime(),
                activeEnd : filter.activeEnd == null ? null
                        : filter.activeEnd.getTime()
            }
        });
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
    
    updateButtonsEnable : function() {
        var controller = this;
        var selected = controller.lookupReference('grid').getSelection();
    }

});