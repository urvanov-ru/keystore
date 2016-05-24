Ext.define('KeyStore.view.dictClientGroup.DictClientGroupListModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.dictClientGroupList',

    requires : ['Ext.data.TreeStore'],
    data : {

    },
    stores : {
        dictClientGroupListStore : Ext.create('Ext.data.TreeStore',{
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
                load : 'onTreeLoad'
            },
            sorters : 'name'
        })
    }
});