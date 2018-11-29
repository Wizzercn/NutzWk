/* wx_menu_01 */
update sys_menu set location=10 where path='0001'
/* wx_menu_02 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('b0edc6861a494b79b97990dc05f0a524','','0002','微信','Wechat','menu','','','','1','0','wx',NULL,'8','1','','1467471229','0')
/* wx_menu_03 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('e4256d7b0ffc4a02906cf900322b6213','b0edc6861a494b79b97990dc05f0a524','00020001','微信会员','Member','menu','','','fa fa-user','1','0','wx.user',NULL,'1','1','','1467471292','0')
/* wx_menu_04 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('b19b23b0459a4754bf1fb8cb234450f2','e4256d7b0ffc4a02906cf900322b6213','000200010001','会员列表','List','menu','/platform/wx/user/index','data-pjax','','1','0','wx.user.list',NULL,'2','0','','1467471357','0')
/* wx_menu_05 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('4dc997fef71e4862b9db22de8e99a618','b19b23b0459a4754bf1fb8cb234450f2','0002000100010001','同步会员信息','Sync','data','','','','0','0','wx.user.list.sync',NULL,'0','0','','1467473044','0')
/* wx_menu_06 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('9f20a757a6bc40ddbb650c70debbf660','b0edc6861a494b79b97990dc05f0a524','00020002','消息管理','Message','menu','','','ti-pencil-alt','1','0','wx.msg',NULL,'3','1','','1467471415','0')
/* wx_menu_07 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('f426468abf714b1599729f8c36ebbb0d','9f20a757a6bc40ddbb650c70debbf660','000200020001','会员消息','Msg','menu','/platform/wx/msg/user','data-pjax','','1','0','wx.msg.user',NULL,'4','1','','1467471478','0')
/* wx_menu_08 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('1734e586e96941268a4c5248b593cef9','f426468abf714b1599729f8c36ebbb0d','0002000200010001','回复消息','Reply','data','','','','0','0','wx.msg.user.reply',NULL,'0','0','','1467473127','0')
/* wx_menu_09 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('6bb17a41f6394ed0a8a6faf5ff781354','9f20a757a6bc40ddbb650c70debbf660','000200020002','群发消息','Mass','menu','/platform/wx/msg/mass','data-pjax','','1','0','wx.msg.mass',NULL,'5','0','','1467471561','0')
/* wx_menu_10 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('56d0658c5a8848818ac05e8ffa5c0570','6bb17a41f6394ed0a8a6faf5ff781354','0002000200020001','添加图文','Add','data','','','','0','0','wx.msg.mass.addNews',NULL,'0','0','','1467473338','0')
/* wx_menu_11 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('ce709456e867425297955b3c40406d7e','6bb17a41f6394ed0a8a6faf5ff781354','0002000200020002','删除图文','Delete','data','','','','0','0','wx.msg.mass.delNews',NULL,'0','0','','1467473363','0')
/* wx_menu_12 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('3099f497480c4b1987bce3f3a26c3fb4','6bb17a41f6394ed0a8a6faf5ff781354','0002000200020003','群发消息','Push','data','','','','0','0','wx.msg.mass.pushNews',NULL,'0','0','','1467473400','0')
/* wx_menu_13 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('4cd8e4e9519e4cff95465194fdcc8d88','b0edc6861a494b79b97990dc05f0a524','00020003','自动回复','AutoReply','menu','','','ti-back-left','1','0','wx.reply',NULL,'6','1','','1467471610','0')
/* wx_menu_14 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('234f8ec3c2bc42bf9f6202aecae36fd6','4cd8e4e9519e4cff95465194fdcc8d88','000200030001','文本内容','Txt','menu','/platform/wx/reply/txt','data-pjax','','1','0','wx.reply.txt',NULL,'7','0','','1467471884','0')
/* wx_menu_15 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('c3a44b478d3241b899b9c3f4611bc2b6','234f8ec3c2bc42bf9f6202aecae36fd6','0002000300010001','添加文本','Add','data','','','','0','0','wx.reply.txt.add',NULL,'0','0','','1467473460','0')
/* wx_menu_16 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('fd63a8e389e04ff3a86c3cea53a3b9d5','234f8ec3c2bc42bf9f6202aecae36fd6','0002000300010002','修改文本','Edit','data','','','','0','0','wx.reply.txt.edit',NULL,'0','0','','1467473519','0')
/* wx_menu_17 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('7c040dfd8db347e5956a3bc1764653dc','234f8ec3c2bc42bf9f6202aecae36fd6','0002000300010003','删除文本','Delete','data','','','','0','0','wx.reply.txt.delete',NULL,'0','0','','1467473540','0')
/* wx_menu_18 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('234f8ec3c2bc42bf9f6202aecae36f99','4cd8e4e9519e4cff95465194fdcc8d88','000200030005','图片内容','Img','menu','/platform/wx/reply/img','data-pjax','','1','0','wx.reply.img',NULL,'8','0','','1467471884','0')
/* wx_menu_19 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('c3a44b478d3241b899b9c3f4611bc216','234f8ec3c2bc42bf9f6202aecae36f99','0002000300050001','添加图片','Add','data','','','','0','0','wx.reply.img.add',NULL,'0','0','','1467473460','0')
/* wx_menu_20 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('fd63a8e389e04ff3a86c3cea53a3b925','234f8ec3c2bc42bf9f6202aecae36f99','0002000300050002','修改图片','Edit','data','','','','0','0','wx.reply.img.edit',NULL,'0','0','','1467473519','0')
/* wx_menu_21 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('7c040dfd8db347e5956a3bc17646533c','234f8ec3c2bc42bf9f6202aecae36f99','0002000300050003','删除图片','Delete','data','','','','0','0','wx.reply.img.delete',NULL,'0','0','','1467473540','0')
/* wx_menu_22 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('17e1ee23ca1443f1bc886c2f5eb7c24b','4cd8e4e9519e4cff95465194fdcc8d88','000200030002','图文内容','News','menu','/platform/wx/reply/news','data-pjax','','1','0','wx.reply.news',NULL,'9','0','','1467471926','0')
/* wx_menu_23 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('2275cb125710414e91b617dd7c62f12c','17e1ee23ca1443f1bc886c2f5eb7c24b','0002000300020001','添加图文','add','data','','','','0','0','wx.reply.news.add',NULL,'0','0','','1467473585','0')
/* wx_menu_24 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('0a972ce655cb4c84809d58668b655900','17e1ee23ca1443f1bc886c2f5eb7c24b','0002000300020002','修改图文','Edit','data','','','','0','0','wx.reply.news.edit',NULL,'0','0','','1467473596','0')
/* wx_menu_25 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('fc52d5284b8f4522802383c1ef732242','17e1ee23ca1443f1bc886c2f5eb7c24b','0002000300020003','删除图文','Delete','data','','','','0','0','wx.reply.news.delete',NULL,'0','0','','1467473606','0')
/* wx_menu_26 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('2cb327ad59b140828fd26eb2a46cb948','4cd8e4e9519e4cff95465194fdcc8d88','000200030003','关注自动回复','Follow','menu','/platform/wx/reply/conf/follow','data-pjax','','1','0','wx.reply.follow',NULL,'10','0','','1467472280','0')
/* wx_menu_27 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('dd965b2c1dfd493fb5efc7e4bcac99d4','2cb327ad59b140828fd26eb2a46cb948','0002000300030001','添加绑定','Add','data','','','','0','0','wx.reply.follow.add',NULL,'0','0','','1467474026','0')
/* wx_menu_28 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('30a5e70a1456447ebf90b5546e9bc321','2cb327ad59b140828fd26eb2a46cb948','0002000300030002','修改绑定','Edit','data','','','','0','0','wx.reply.follow.edit',NULL,'0','0','','1467474056','0')
/* wx_menu_29 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('2a63040409094f1e9dc535dd78ce15b7','2cb327ad59b140828fd26eb2a46cb948','0002000300030003','删除绑定','Delete','data','','','','0','0','wx.reply.follow.delete',NULL,'0','0','','1467474080','0')
/* wx_menu_30 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('0706112ff5dc46e388064a99bcdb0561','4cd8e4e9519e4cff95465194fdcc8d88','000200030004','关键词回复','Keyword','menu','/platform/wx/reply/conf/keyword','data-pjax','','1','0','wx.reply.keyword',NULL,'11','0','','1467472362','0')
/* wx_menu_31 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('e864c78aba63448892cbcb6a3a7f4da7','0706112ff5dc46e388064a99bcdb0561','0002000300040001','添加绑定','Add','data','','','','0','0','wx.reply.keyword.add',NULL,'0','0','','1467474113','0')
/* wx_menu_32 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('ff6cd243a77c4ae98dacf6149c816c75','0706112ff5dc46e388064a99bcdb0561','0002000300040002','修改绑定','Edit','data','','','','0','0','wx.reply.keyword.edit',NULL,'0','0','','1467474125','0')
/* wx_menu_33 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('733d3f35d49f45af99ca9220048583ba','0706112ff5dc46e388064a99bcdb0561','0002000300040003','删除绑定','Delete','data','','','','0','0','wx.reply.keyword.delete',NULL,'0','0','','1467474136','0')
/* wx_menu_34 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('bcf64d623fdd4519ae345b7a08c071a1','b0edc6861a494b79b97990dc05f0a524','00020004','微信配置','Config','menu','','','fa fa-weixin','1','0','wx.conf',NULL,'12','1','','1467472498','0')
/* wx_menu_35 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('66cc21d7ce104dd6877cbce114c59fb3','bcf64d623fdd4519ae345b7a08c071a1','000200040001','帐号配置','Account','menu','/platform/wx/conf/account','data-pjax','','1','0','wx.conf.account',NULL,'13','0','','1467472624','0')
/* wx_menu_36 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('309dc29ad3c34408a68df8f867a5c9ff','66cc21d7ce104dd6877cbce114c59fb3','0002000400010001','添加帐号','Add','data','','','','0','0','wx.conf.account.add',NULL,'0','0','','1467474187','0')
/* wx_menu_37 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('96554b09a2dd4f82bab7546fa59acd35','66cc21d7ce104dd6877cbce114c59fb3','0002000400010002','修改帐号','Edit','data','','','','0','0','wx.conf.account.edit',NULL,'0','0','','1467474197','0')
/* wx_menu_38 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('d568f4c2b687404e8aec7b9edcae5767','66cc21d7ce104dd6877cbce114c59fb3','0002000400010003','删除帐号','Delete','data','','','','0','0','wx.conf.account.delete',NULL,'0','0','','1467474209','0')
/* wx_menu_39 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('2fab774f8b6d40cb9d7e187babab2d91','bcf64d623fdd4519ae345b7a08c071a1','000200040002','菜单配置','Menu','menu','/platform/wx/conf/menu','data-pjax','','1','0','wx.conf.menu',NULL,'14','0','','1467472649','0')
/* wx_menu_40 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('45d958ca78304f25b51f6c71cf66f6d8','2fab774f8b6d40cb9d7e187babab2d91','0002000400020001','添加菜单','Add','data','','','','0','0','wx.conf.menu.add',NULL,'0','0','','1467474283','0')
/* wx_menu_41 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('44da90bc76a5419a841f4924333f7a66','2fab774f8b6d40cb9d7e187babab2d91','0002000400020002','修改菜单','Edit','data','','','','0','0','wx.conf.menu.edit',NULL,'0','0','','1467474294','0')
/* wx_menu_42 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('9a9557177d334c209cf73c3817fe3b63','2fab774f8b6d40cb9d7e187babab2d91','0002000400020003','删除菜单','Delete','data','','','','0','0','wx.conf.menu.delete',NULL,'0','0','','1467474304','0')
/* wx_menu_43 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('0a43d291e0c94ad88c8b690009279e34','2fab774f8b6d40cb9d7e187babab2d91','0002000400020004','保存排序','Save','data','','','','0','0','wx.conf.menu.sort',NULL,'0','0','','1467474314','0')
/* wx_menu_44 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('5244f5c38eb24b918e9ad64d456daa38','2fab774f8b6d40cb9d7e187babab2d91','0002000400020005','推送到微信','Push','data','','','','0','0','wx.conf.menu.push',NULL,'0','0','','1467474330','0')
/* wx_menu_45 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('6afc5075913d4df4b44a6476080e35a0','b0edc6861a494b79b97990dc05f0a524','00020005','模板消息','Template','menu','','','ti-notepad','1','0','wx.tpl',NULL,'50','1','','1470406797','0')
/* wx_menu_46 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('1385ae887e5c4b8aa33fbf228be7f907','6afc5075913d4df4b44a6476080e35a0','000200050001','模板编号','Id','menu','/platform/wx/tpl/id','data-pjax','','1','0','wx.tpl.id',NULL,'51','0','','1470406854','0')
/* wx_menu_47 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('e6b6224617b04090a76e46a4b048fb96','1385ae887e5c4b8aa33fbf228be7f907','0002000500010001','添加编号','Add','data','','','','0','0','wx.tpl.id.add',NULL,'54','0','','1470407055','0')
/* wx_menu_48 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('3888f05aa4064f788ba7ec51c495ce7c','1385ae887e5c4b8aa33fbf228be7f907','0002000500010002','删除编号','Delete','data','','','','0','0','wx.tpl.id.delete',NULL,'55','0','','1470407068','0')
/* wx_menu_49 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('cabbe834a7474675b899e8442b5c2604','6afc5075913d4df4b44a6476080e35a0','000200050002','模板列表','List','menu','/platform/wx/tpl/list','data-pjax','','1','0','wx.tpl.list',NULL,'52','0','','1470406883','0')
/* wx_menu_50 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('a11163584dfe456cbfd6fb2d4b74391b','cabbe834a7474675b899e8442b5c2604','0002000500020001','获取列表','Get','data','','','','0','0','wx.tpl.list.get',NULL,'56','0','','1470407390','0')
/* wx_menu_51 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('c76a84f871d047db955dd1465c845ac1','6afc5075913d4df4b44a6476080e35a0','000200050003','发送记录','Log','menu','/platform/wx/tpl/log','data-pjax','','1','0','wx.tpl.log',NULL,'53','0','','1470406926','0')

