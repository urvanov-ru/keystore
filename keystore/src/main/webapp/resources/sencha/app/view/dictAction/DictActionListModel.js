Ext.define('KeyStore.view.dictAction.DictActionListModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.dictActionList',

    data : {
        name : 'KeyStore'
    },
    stores : {
        dictActionListStore : {

            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictAction/list'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },

            groupField : '',
            fields : [{
                name : 'id',
                type : 'string'
            }, {
                
                name : 'name',
                type : 'string'
            }, {
                name : 'description',
                type : 'string'
            }, {
                name : 'dateBegin',
                type : 'date',
                dateFormat : 'time'
            }, {
                name : 'dateEnd',
                type : 'date',
                dateFormat : 'time'
            }, {
                name : 'dictServiceTypeName',
                type : 'string'
            }, {
                name : 'dictActionTypeName',
                type : 'string'
            }, {
                name : 'forNewClients',
                type : 'string'
            }],
            data : [],
            sorters : 'name',
            listeners : {
                beforeload : 'onGridBeforeLoad',
                load : 'onGridLoad'
            }
        }
    }
});