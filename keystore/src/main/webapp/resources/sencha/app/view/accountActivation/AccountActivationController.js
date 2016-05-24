Ext.define('KeyStore.view.accountActivation.AccountActivationController', {
    extend : 'Ext.app.ViewController',
    requires : [],
    alias : 'controller.accountActivation',

    onAfterRender : function(self, eOpts) {
        var infoLabel = this.lookupReference('infoLabel');
        var params = globalUtils.parseGetParams();
        globalUtils.ajaxRequest({
            url : '/security/accountactivation?key=' + params.key + '&id='
                    + params.id,
            method : 'GET',
            success : function(xhr, opts) {
                var answer = Ext.decode(xhr.responseText);
                infoLabel.setText(answer.message);
            },
            failure : function(xhr, opts) {
                switch (xhr.status) {
                case 403:
                    infoLabel.setText('Доступ запрещён.');
                    break;
                default :
                    infoLabel.setText('Ошибка активации аккаунта.');
                    break;
                }
            }
        });
    },

    onOkClick : function(self) {
        this.getView().destroy();
        Ext.widget('login');
    }
});