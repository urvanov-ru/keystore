Ext.define('KeyStore.model.CodeName', {
    extend: 'KeyStore.model.Base',
    requires :  [ 'KeyStore.model.Base'],
    fields: [{
        name: 'code',
        type: 'string'
    }, {
        name : 'name',
        type : 'string'
    }]
});
