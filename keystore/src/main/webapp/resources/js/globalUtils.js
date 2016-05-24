var globalUtils = {
                toUrl : function(s1) {
                    var a = window.location.href.split('/');
                    a.pop();
                    a.pop();
                    a.pop();
                    var str = a.join('/') + s1;
                    return str;
                },
                toRootUrl : function(s1) {
                    var a = window.location.href.split('/');
                    a.pop();
                    a.pop();
                    a.pop();
                    a.pop();
                    var str = a.join('/') + s1;
                    return str;
                },
                ajaxRequest : function (options) {
                    var obj = {};
                    if (options.url) obj.url= this.toUrl(options.url);
                    if (options.params) obj.params=options.params;
                    if (options.method) obj.method=options.method;
                    if (options.jsonData) obj.jsonData = options.jsonData;
                    obj.success = function(response, opts) {
                        if (options.success) {
                            options.success(response, opts);
                        }
                    };
                    obj.failure = function(response, opts) {
                        switch (response.status) {
                        case 401 : 
                            globalUtils.showSessionLostMessage();
                            return;
                        }
                        if (options.failure) {
                            options.failure(response, opts);
                        }
                    };
                    obj.callback = function(opt, success, xhr) {
                        if (options.callback) {
                            options.callback(opt, success, xhr);
                        }
                    };
                    Ext.Ajax.request(obj);
                },
                parseGetParams : function() { 
                    var $_GET = {}; 
                    var __GET = window.location.search.substring(1).split("&"); 
                    for(var i=0; i<__GET.length; i++) { 
                            var getVar = __GET[i].split("="); 
                            $_GET[getVar[0]] = typeof(getVar[1])=="undefined" ? "" : getVar[1]; 
                    } 
                    return $_GET; 
                },
                showSessionLostMessage : function () {
                    Ext.MessageBox.show({
                        title : 'Ошибка',
                        msg : 'Вы не зарегистрированы в системе или Ваша сессия устарела.<br/>Пожалуйста, войдите в систему повторно.',
                        buttons : Ext.MessageBox.OK,
                        fn : function () {
                            window.location = globalUtils.toUrl('/resources/sencha/index.html');
                        }
                    });
                }
        };



var globalData = {
    user : {
        fullName : '',
        authorities : [],
        accesses : {}
    }
};
