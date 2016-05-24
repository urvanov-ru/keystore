Ext.define('KeyStore.view.client.ClientSelectModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.clientSelect',

    data : {
        okListener : null,
        filter : {
            active : null,
            dictClientGroupId : null,
            dictClientGroupName : null,
            activeBegin : null,
            activeEnd : null
        }
    },
    stores : {
        clientSelectStore : {

            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/client/list'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            data : [],
            groupField : 'clientGroupName',
            fields : [ {
                name : 'id',
                type : 'string'
            }, {
                name : 'name',
                type : 'string'
            }, {
                name : 'clientType',
                type : 'string'
            }, {
                name : 'uniqueId',
                type : 'string'
            }, {
                name : 'contactPersonName',
                type : 'string'
            }, {
                name : 'contactPersonEmail',
                type : 'string'
            }, {
                name : 'contactPersonPhone',
                type : 'string'
            }, {
                name : 'itn',
                type : 'string'
            }, {
                name : 'iec',
                type : 'string'
            }, {
                name : 'active',
                type : 'string'
            }, {
                name : 'clientGroupName',
                type : 'string'
            }, {
                name : 'juridicalPersonName',
                type : 'string'
            } ],
            sorters : [ 'clientGroupName', 'name' ],
            listeners : {
                load : 'onGridLoad'
            }
        }
    }
});