Ext.define('KeyStore.view.dictClientGroup.DictClientGroupSelectModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.dictClientGroupSelect',

    data : {

    },
    stores : {
        dictClientGroupSelectStore : Ext.create('Ext.data.TreeStore', {
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/dictClientGroup/list'),
                reader : {
                    type : 'json',
                    rootProperty : 'children'
                }
            },
            data : [],
            listeners : {
                load : 'onTreeStoreLoad'
            },
            sorters : 'name'
        })
    }
});