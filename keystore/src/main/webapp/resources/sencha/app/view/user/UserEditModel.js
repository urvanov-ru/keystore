Ext.define('KeyStore.view.user.UserEditModel', {
    extend : 'Ext.app.ViewModel',

    alias : 'viewmodel.userEdit',

    data : {

    },
    stores : {
        sexStore : {
            type : 'sexGlobalStore',
            autoLoad : true
        }
    }
});