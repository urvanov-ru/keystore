Ext.define('KeyStore.view.emailTemplate.EmailTemplateListModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.emailTemplateList',

    data : {

    },
    stores : {
        emailTemplateListStore : {
            
            proxy : {
                type : 'ajax',
                url : globalUtils.toUrl('/emailTemplate/list'),
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
                
                name : 'codeName',
                type : 'string'
            }],
            data : [],
            sorters : 'codeName',
            listeners : {
                beforeload : 'onGridBeforeLoad',
                load : 'onGridLoad'
            }
        }
    }
});