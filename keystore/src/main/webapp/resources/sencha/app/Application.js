

/**
 * The main application class. An instance of this class is created by app.js when it calls
 * Ext.application(). This is the ideal place to handle application launch and initialization
 * details.
 */
Ext.define('KeyStore.Application', {
    extend: 'Ext.app.Application',
    
    name: 'KeyStore',

    models : [
              'KeyStore.model.Base',
              'KeyStore.model.IdName',
              'KeyStore.model.CodeName'
              
              ],
    stores: [
        'KeyStore.store.ClientType',
        'KeyStore.store.DictClientGroup',
        'KeyStore.store.DictServiceType',
        'KeyStore.store.KeyActivationMode',
        'KeyStore.store.Sex'
    ],
    views: [
            'KeyStore.view.login.Login',
            'KeyStore.view.main.Main',
            'KeyStore.view.register.Register',
            'KeyStore.view.client.ClientList',
            'KeyStore.view.order.OrderList',
            'KeyStore.view.payment.PaymentList',
            'KeyStore.view.key.KeyList',
            'KeyStore.view.client.ClientFilter',
            'KeyStore.view.order.OrderFilter',
            'KeyStore.view.payment.PaymentFilter',
            'KeyStore.view.key.KeyFilter',
            'KeyStore.view.client.ClientEdit',
            'KeyStore.view.cabinet.Cabinet',
            'KeyStore.view.account.Account',
            'KeyStore.view.user.UserList',
            'KeyStore.view.settings.GlobalSettings',
            'KeyStore.view.dictServiceType.DictServiceTypeList',
            'KeyStore.view.dictServiceType.DictServiceTypeEdit',
            'KeyStore.view.dictAction.DictActionList',
            'KeyStore.view.dictAction.DictActionEdit',
            'KeyStore.view.dictClientGroup.DictClientGroupList',
            'KeyStore.view.changePassword.ChangePassword',
            'KeyStore.view.accountActivation.AccountActivation',
            'KeyStore.view.key.BaseKeyEdit',
            'KeyStore.view.order.OrderEdit',
            'KeyStore.view.dictServiceType.DictServiceTypeSelect',
            'KeyStore.view.user.UserEdit',
            'KeyStore.view.user.UserAccess',
            'KeyStore.view.dictClientGroup.DictClientGroupEdit',
            'KeyStore.view.dictClientGroup.DictClientGroupSelect',
            'KeyStore.view.emailTemplate.EmailTemplateList',
            'KeyStore.view.emailTemplate.EmailTemplateEdit',
            'KeyStore.view.client.EmailNotification',
            'KeyStore.view.pay.PayuLu',
            'KeyStore.view.pay.PayuIrn',
            'KeyStore.view.client.ClientSelect',
            'KeyStore.view.client.LinkClientDictServiceTypeList',
            'KeyStore.view.dictAction.DictActionSelect',
            'KeyStore.view.client.LinkClientDictActionList',
            'KeyStore.view.dictAction.DictActionStart',
            'KeyStore.view.pay.Onpay',
            'KeyStore.view.payment.PaymentCorrect',
            'KeyStore.view.report.ReportActivity',
            'KeyStore.view.report.ReportPayment',
            'KeyStore.view.report.ActivityChart',
            'KeyStore.view.report.ClientActivityChart',
            'KeyStore.view.report.PaymentChart'
        ],
    launch: function () {
        
        
        
        
        var GETArr = globalUtils.parseGetParams(); 
        var str = JSON.stringify(GETArr);
        var peremen = JSON.parse(str);
        var type=peremen.type;
        if (type == null || type === 'login') {
            Ext.Ajax.request({
                url : globalUtils.toUrl('/main/checkSession'),
                success : function(xhr) {
                    Ext.widget('app-main');
                },
                failure : function(xhr) {
                    Ext.widget('login');
                }
           });
            
        } else if (type === 'changepassword') {
            Ext.widget('changePassword');
        } else if (type === 'accountactivation') {
            Ext.widget('accountActivation');
        } else if (type === 'paySuccess') {
            Ext.MessageBox.show({
                title : 'Успешно',
                msg : 'Оплата прошла успешно. Нажмите "ОК" для возврата в приложение.',
                buttons : Ext.MessageBox.OK,
                icon : Ext.MessageBox.INFO,
                fn : function() {
                    Ext.widget('app-main');
                }
            });
            
        } else if (type === 'payFail') {
            Ext.MessageBox.show({
                title : 'Ошибка',
                msg : 'Оплата НЕ выполнена. Нажмите "ОК" для возврата в приложение.',
                buttons : Ext.MessageBox.OK,
                icon : Ext.MessageBox.ERROR,
                fn : function() {
                    Ext.widget('app-main');
                }
            });
        }

        

    }
});
