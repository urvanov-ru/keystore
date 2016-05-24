Ext.define('KeyStore.view.user.UserListModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.userList',
    data : {
        
    },
    stores : {
        userListStore : {
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/user/list'),
                reader : {
                    type : 'json',
                    rootProperty : 'info'
                }
            },
            data : [],
            groupField : 'clientName',
            fields : [ {
                name : 'id',
                type : 'string'
            },  {
                name : 'userName',
                type : 'string'
            }, {
                name : 'fullName',
                type : 'string'
            }, {
                name : 'sexName',
                type : 'string'
            }, {
                name : 'phone',
                type : 'string'
            }, {
                name : 'post',
                type : 'string'
            }, {
                name : 'birthdate',
                type : 'date',
                dateReadFormat : 'time'
            }, {
                name : 'clientName',
                type : 'string'
            }, {
                name : 'enabled',
                type : 'boolean'
            }],
            sorters : 'userName',
            listeners : {
                beforeload : 'onGridBeforeLoad',
                load : 'onGridLoad'
            }
        }
    }
});