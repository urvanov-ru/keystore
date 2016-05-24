/**
 * This class is the main view for the application. It is specified in app.js as
 * the "autoCreateViewport" property. That setting automatically applies the
 * "viewport" plugin to promote that instance of this class to the body element.
 * 
 * TODO - Replace this content of this view to suite the needs of your
 * application.
 */
Ext.define('KeyStore.view.main.Main', {
    extend : 'Ext.container.Container',
    requires : [ 'KeyStore.view.main.MainController',
            'KeyStore.view.main.MainModel', 'Ext.Img', 'Ext.panel.Panel',
            'Ext.button.Button', 'Ext.menu.Menu', 'Ext.menu.Item',
            'Ext.menu.Separator', 'Ext.plugin.Viewport' ],
    plugins : 'viewport',
    xtype : 'app-main',

    controller : 'main',
    viewModel : {
        type : 'main'
    },

    layout : {
        type : 'border'
    },

    items : [ {
        xtype : 'panel',
        region : 'north',
        layout : 'vbox',
        height : 60,
        items : [ {
            xtype : 'panel',
            layout : 'hbox',
            width : '100%',
            height : 30,
            bodyStyle : 'background-color: #003399;color:#ffffff',
            items : [ {
                xtype : 'image',
                src : globalUtils.toUrl('/resources/image/standard/key.png'),
                width : 30,
                height : 30
            }, {
                xtype : 'label',
                padding : 5,
                text : 'KeyStore',
                flex : 1,
                height : 28,
                style : {
                    fontSize : '18px',
                    fontFamily : 'Arial Narrow',
                    fontWeight : 'bold'
                },
                reference : 'headerLabel'
            }, {
                xtype : 'panel',
                bodyStyle : 'background-color: #003399;color:#ffffff',
                html :'<img  style="border-width : 0px;" src="' + globalUtils.toUrl('/resources/image/standard/logo.png') + '" />',
                width : 169,
                height : 30
            } ]
        }, {
            xtype : 'menu',
            allowOtherMenus : true,
            autoShow : true,
            floating:false,
            plain : true,
            layout : 'hbox',
            width : '100%',
            height : 30,
            listeners : {
                beforehide : function(self, eOpts ) {
                    if (console) console.log('before hide');
                    return false;
                }
            },
            defaults : {
                menuAlign : 'tl-bl?',
                border : true,
                style : {
                    borderColor : '#1E4E79',
                    marginWidth: '0px 0px 0px 0px',
                    outerWidth: '0px 0px 0px 0px'
                }
            },
            border : true,
            items : [ {
                text : 'Клиенты',
                handler : 'onClientListClick',
                reference : 'menuItemClientList',
                hidden : true,
                disabled : true
            }, {
                text : 'Заказы',
                handler : 'onOrderListClick',
                border : true,
                reference : 'menuItemOrderList',
                disabled : true
            }, {
                text : 'Ключи',
                handler : 'onKeyListClick',
                reference : 'menuItemKeyList',
                disabled : true
            }, {
                text : 'Платежи',
                handler : 'onPaymentListClick',
                reference : 'menuItemPaymentList',
                disabled : true
            }, {
                text : 'Кабинет',
                menuExpandDelay : 0,
                menu : {
                    plain : true,
                    defaults : {
                        border : true,
                        style : {
                            borderColor : '#1E4E79',
                            marginWidth: '0px 0px 0px 0px',
                            outerWidth: '0px 0px 0px 0px'
                        }
                    },
                    items : [ {
                        text : 'Кабинет',
                        handler : 'onCabinetClick',
                        reference : 'menuItemCabinet'
                    }, {
                        text : 'Аккаунт',
                        handler : 'onAccountClick',
                        disabled : true,
                        reference : 'menuItemAccount'
                    }, {
                        text : 'Пользователи',
                        handler : 'onUserListClick',
                        disabled : true,
                        reference : 'menuItemUserList'
                    } ]
                }
            }, {
                text : 'Настройки',
                reference : 'menuItemSettings',
                hidden : true,
                menuExpandDelay : 0,
                menu : {
                    plain : true,
                    defaults : {
                        border : true,
                        style : {
                            borderColor : '#1E4E79',
                            marginWidth: '0px 0px 0px 0px',
                            outerWidth: '0px 0px 0px 0px'
                        }
                    },
                    items : [ {
                        text : 'Настройки',
                        handler : 'onGlobalSettingsClick',
                        disabled : true,
                        reference : 'menuItemGlobalSettings'
                    }, {
                        text : 'Пакеты',
                        handler : 'onDictServiceTypeListClick',
                        disabled : true,
                        reference : 'menuItemDictServiceTypeList'
                    }, {
                        text : 'Акции',
                        handler : 'onDictActionListClick',
                        disabled : true,
                        reference : 'menuItemDictActionList'
                    }, {
                        text : 'Группы',
                        handler : 'onDictClientGroupListClick',
                        disabled : true,
                        reference : 'menuItemDictClientGroupList'
                    }, {
                        text : 'Шаблоны',
                        handler : 'onEmailTemplateListClick',
                        disabled : true,
                        reference : 'menuItemEmailTemplateList'
                    } ]
                }
            }, {
                text : 'Анализ активности',
                reference : 'menuItemReports',
                hidden : true,
                menuExpandDelay : 0,
                menu : {
                    plain : true, 
                    defaults : {
                        border : true,
                        style : {
                            borderColor : '#1E4E79',
                            marginWidth: '0px 0px 0px 0px',
                            outerWidth: '0px 0px 0px 0px'
                        }
                    },
                    items : [{
                        text : 'Активность',
                        handler : 'onReportActivityClick',
                        disabled : true,
                        reference : 'menuItemReportActivity'
                    },{
                        text : 'Деньги',
                        handler : 'onReportPaymentClick',
                        disabled : true,
                        reference : 'menuItemReportPayment'
                    }]
                }
            } ]
        } ]
    }, {
        region : 'center',
        xtype : 'panel',
        layout : 'fit',
        reference : 'centerPanel',
        items : [ {
            xtype : 'panel',
            html : '<h2>Content appropriate for the current navigation.</h2>'
        } ]
    }, {
        region : 'south',
        xtype : 'panel',
        layout : 'hbox',
        bodyStyle : 'background-color: #002060;color:#ffffff',
        items : [ {
            xtype : 'label',
            padding : 8,
            text : '© http://urvanov.ru 2016. Все права защищены.',
            style : {
                fontFamily: 'Arial Narrow',
                fontSize : '14px',
                fontWeight : 'bold'
            },
            flex : 1
        }, {
            xtype : 'panel',
            bodyStyle : 'background-color: #002060;color:#ffffff',
            html : '<img style="border-width : 0px;" src="' + globalUtils.toUrl('/resources/image/standard/person.png') + '"  />',
            width : 30,
            height : 30
        }, {
            xtype : 'label',
            text : 'Загрузка...',
            padding : 8,
            reference : 'mainUserFullName',
            width : 200,
            listeners : {
                afterrender : 'onMainUserFullNameAfterRender'
            },
            style : {
                fontFamily: 'Arial Narrow',
                fontSize : '14px',
                fontWeight : 'bold'
            }
        }, {
            xtype : 'button',
            text : 'Выйти',
            handler : 'onExitClick',
            margin : 3,
            height: 24
        } ]
    } ],
    listeners : {
        render : 'onRender'
    }
});
