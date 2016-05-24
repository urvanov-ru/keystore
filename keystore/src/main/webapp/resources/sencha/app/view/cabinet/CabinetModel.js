Ext.define('KeyStore.view.cabinet.CabinetModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.cabinet',
    requires : [],
    data : {},
    stores : {
        sexStore : {
            type : 'sexGlobalStore',
            autoLoad : true
        }
    }
});