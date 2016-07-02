/* updateSysMenu */
update sys_menu set location=10 where path='0001';
/* insertSysMenu1 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('b0edc6861a494b79b97990dc05f0a524','','0002','微信',NULL,'menu','','','','1','0','wx',NULL,'8','1','','1467471229','0');
/* insertSysMenu2 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('e4256d7b0ffc4a02906cf900322b6213','b0edc6861a494b79b97990dc05f0a524','00020001','微信会员',NULL,'menu','','','fa fa-user','1','0','wx.user',NULL,'1','1','','1467471292','0');
/* insertSysMenu3 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('b19b23b0459a4754bf1fb8cb234450f2','e4256d7b0ffc4a02906cf900322b6213','000200010001','会员列表',NULL,'menu','/private/wx/user/index','data-pjax','','1','0','wx.user.list',NULL,'2','0','','1467471357','0');
/* insertSysMenu4 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('4dc997fef71e4862b9db22de8e99a618','b19b23b0459a4754bf1fb8cb234450f2','0002000100010001','同步会员信息',NULL,'data','','','','0','0','wx.user.list.sync',NULL,'0','0','','1467473044','0');
/* insertSysMenu5 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('9f20a757a6bc40ddbb650c70debbf660','b0edc6861a494b79b97990dc05f0a524','00020002','消息管理',NULL,'menu','','','ti-pencil-alt','1','0','wx.msg',NULL,'3','1','','1467471415','0');
/* insertSysMenu6 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('f426468abf714b1599729f8c36ebbb0d','9f20a757a6bc40ddbb650c70debbf660','000200020001','会员消息',NULL,'menu','/private/wx/msg/user','data-pjax','','1','0','wx.msg.user',NULL,'4','1','','1467471478','0');
/* insertSysMenu7 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('1734e586e96941268a4c5248b593cef9','f426468abf714b1599729f8c36ebbb0d','0002000200010001','回复消息',NULL,'data','','','','0','0','wx.msg.user.reply',NULL,'0','0','','1467473127','0');
/* insertSysMenu8 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('6bb17a41f6394ed0a8a6faf5ff781354','9f20a757a6bc40ddbb650c70debbf660','000200020002','群发消息',NULL,'menu','/private/wx/msg/mass','data-pjax','','1','0','wx.msg.mass',NULL,'5','0','','1467471561','0');
/* insertSysMenu9 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('56d0658c5a8848818ac05e8ffa5c0570','6bb17a41f6394ed0a8a6faf5ff781354','0002000200020001','添加图文',NULL,'data','','','','0','0','wx.msg.mass.addNews',NULL,'0','0','','1467473338','0');
/* insertSysMenu10 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('ce709456e867425297955b3c40406d7e','6bb17a41f6394ed0a8a6faf5ff781354','0002000200020002','删除图文',NULL,'data','','','','0','0','wx.msg.mass.delNews',NULL,'0','0','','1467473363','0');
/* insertSysMenu11 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('3099f497480c4b1987bce3f3a26c3fb4','6bb17a41f6394ed0a8a6faf5ff781354','0002000200020003','群发消息',NULL,'data','','','','0','0','wx.msg.mass.pushNews',NULL,'0','0','','1467473400','0');
/* insertSysMenu12 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('4cd8e4e9519e4cff95465194fdcc8d88','b0edc6861a494b79b97990dc05f0a524','00020003','自动回复',NULL,'menu','','','ti-back-left','1','0','wx.reply',NULL,'6','1','','1467471610','0');
/* insertSysMenu13 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('234f8ec3c2bc42bf9f6202aecae36fd6','4cd8e4e9519e4cff95465194fdcc8d88','000200030001','文本内容',NULL,'menu','/private/wx/reply/txt','data-pjax','','1','0','wx.reply.txt',NULL,'7','0','','1467471884','0');
/* insertSysMenu14 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('c3a44b478d3241b899b9c3f4611bc2b6','234f8ec3c2bc42bf9f6202aecae36fd6','0002000300010001','添加文本',NULL,'data','','','','0','0','wx.reply.txt.add',NULL,'0','0','','1467473460','0');
/* insertSysMenu15 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('fd63a8e389e04ff3a86c3cea53a3b9d5','234f8ec3c2bc42bf9f6202aecae36fd6','0002000300010002','修改文本',NULL,'data','','','','0','0','wx.reply.txt.edit',NULL,'0','0','','1467473519','0');
/* insertSysMenu16 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('7c040dfd8db347e5956a3bc1764653dc','234f8ec3c2bc42bf9f6202aecae36fd6','0002000300010003','删除文本',NULL,'data','','','','0','0','wx.reply.txt.delete',NULL,'0','0','','1467473540','0');
/* insertSysMenu17 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('17e1ee23ca1443f1bc886c2f5eb7c24b','4cd8e4e9519e4cff95465194fdcc8d88','000200030002','图文内容',NULL,'menu','/private/wx/reply/news','data-pjax','','1','0','wx.reply.news',NULL,'8','0','','1467471926','0');
/* insertSysMenu18 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('2275cb125710414e91b617dd7c62f12c','17e1ee23ca1443f1bc886c2f5eb7c24b','0002000300020001','添加图文',NULL,'data','','','','0','0','wx.reply.news.add',NULL,'0','0','','1467473585','0');
/* insertSysMenu19 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('0a972ce655cb4c84809d58668b655900','17e1ee23ca1443f1bc886c2f5eb7c24b','0002000300020002','修改图文',NULL,'data','','','','0','0','wx.reply.news.edit',NULL,'0','0','','1467473596','0');
/* insertSysMenu20 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('fc52d5284b8f4522802383c1ef732242','17e1ee23ca1443f1bc886c2f5eb7c24b','0002000300020003','删除图文',NULL,'data','','','','0','0','wx.reply.news.delete',NULL,'0','0','','1467473606','0');
/* insertSysMenu21 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('2cb327ad59b140828fd26eb2a46cb948','4cd8e4e9519e4cff95465194fdcc8d88','000200030003','关注自动回复',NULL,'menu','/private/wx/reply/conf?type=follow','data-pjax','','1','0','wx.reply.follow',NULL,'9','0','','1467472280','0');
/* insertSysMenu22 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('dd965b2c1dfd493fb5efc7e4bcac99d4','2cb327ad59b140828fd26eb2a46cb948','0002000300030001','添加绑定',NULL,'data','','','','0','0','wx.reply.follow.add',NULL,'0','0','','1467474026','0');
/* insertSysMenu23 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('30a5e70a1456447ebf90b5546e9bc321','2cb327ad59b140828fd26eb2a46cb948','0002000300030002','修改绑定',NULL,'data','','','','0','0','wx.reply.follow.edit',NULL,'0','0','','1467474056','0');
/* insertSysMenu24 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('2a63040409094f1e9dc535dd78ce15b7','2cb327ad59b140828fd26eb2a46cb948','0002000300030003','删除绑定',NULL,'data','','','','0','0','wx.reply.follow.delete',NULL,'0','0','','1467474080','0');
/* insertSysMenu25 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('0706112ff5dc46e388064a99bcdb0561','4cd8e4e9519e4cff95465194fdcc8d88','000200030004','关键词回复',NULL,'menu','/private/wx/reply/conf?type=keyword','data-pjax','','1','0','wx.reply.keyword',NULL,'10','0','','1467472362','0');
/* insertSysMenu26 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('e864c78aba63448892cbcb6a3a7f4da7','0706112ff5dc46e388064a99bcdb0561','0002000300040001','添加绑定',NULL,'data','','','','0','0','wx.reply.keyword.add',NULL,'0','0','','1467474113','0');
/* insertSysMenu27 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('ff6cd243a77c4ae98dacf6149c816c75','0706112ff5dc46e388064a99bcdb0561','0002000300040002','修改绑定',NULL,'data','','','','0','0','wx.reply.keyword.edit',NULL,'0','0','','1467474125','0');
/* insertSysMenu28 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('733d3f35d49f45af99ca9220048583ba','0706112ff5dc46e388064a99bcdb0561','0002000300040003','删除绑定',NULL,'data','','','','0','0','wx.reply.keyword.delete',NULL,'0','0','','1467474136','0');
/* insertSysMenu29 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('bcf64d623fdd4519ae345b7a08c071a1','b0edc6861a494b79b97990dc05f0a524','00020004','微信配置',NULL,'menu','','','fa fa-weixin','1','0','wx.conf',NULL,'11','1','','1467472498','0');
/* insertSysMenu30 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('66cc21d7ce104dd6877cbce114c59fb3','bcf64d623fdd4519ae345b7a08c071a1','000200040001','帐号配置',NULL,'menu','/private/wx/conf/account','data-pjax','','1','0','wx.conf.account',NULL,'12','0','','1467472624','0');
/* insertSysMenu31 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('309dc29ad3c34408a68df8f867a5c9ff','66cc21d7ce104dd6877cbce114c59fb3','0002000400010001','添加帐号',NULL,'data','','','','0','0','wx.conf.account.add',NULL,'0','0','','1467474187','0');
/* insertSysMenu32 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('96554b09a2dd4f82bab7546fa59acd35','66cc21d7ce104dd6877cbce114c59fb3','0002000400010002','修改帐号',NULL,'data','','','','0','0','wx.conf.account.edit',NULL,'0','0','','1467474197','0');
/* insertSysMenu33 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('d568f4c2b687404e8aec7b9edcae5767','66cc21d7ce104dd6877cbce114c59fb3','0002000400010003','删除帐号',NULL,'data','','','','0','0','wx.conf.account.delete',NULL,'0','0','','1467474209','0');
/* insertSysMenu34 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('2fab774f8b6d40cb9d7e187babab2d91','bcf64d623fdd4519ae345b7a08c071a1','000200040002','菜单配置',NULL,'menu','/private/wx/conf/menu','data-pjax','','1','0','wx.conf.menu',NULL,'13','0','','1467472649','0');
/* insertSysMenu35 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('45d958ca78304f25b51f6c71cf66f6d8','2fab774f8b6d40cb9d7e187babab2d91','0002000400020001','添加菜单',NULL,'data','','','','0','0','wx.conf.menu.add',NULL,'0','0','','1467474283','0');
/* insertSysMenu36 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('44da90bc76a5419a841f4924333f7a66','2fab774f8b6d40cb9d7e187babab2d91','0002000400020002','修改菜单',NULL,'data','','','','0','0','wx.conf.menu.edit',NULL,'0','0','','1467474294','0');
/* insertSysMenu37 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('9a9557177d334c209cf73c3817fe3b63','2fab774f8b6d40cb9d7e187babab2d91','0002000400020003','删除菜单',NULL,'data','','','','0','0','wx.conf.menu.delete',NULL,'0','0','','1467474304','0');
/* insertSysMenu38 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('0a43d291e0c94ad88c8b690009279e34','2fab774f8b6d40cb9d7e187babab2d91','0002000400020004','保存排序',NULL,'data','','','','0','0','wx.conf.menu.sort',NULL,'0','0','','1467474314','0');
/* insertSysMenu39 */
insert into `sys_menu` (`id`, `parentId`, `path`, `name`, `aliasName`, `type`, `href`, `target`, `icon`, `isShow`, `disabled`, `permission`, `note`, `location`, `hasChildren`, `opBy`, `opAt`, `delFlag`) values('5244f5c38eb24b918e9ad64d456daa38','2fab774f8b6d40cb9d7e187babab2d91','0002000400020005','推送到微信',NULL,'data','','','','0','0','wx.conf.menu.push',NULL,'0','0','','1467474330','0');
/* insertRoleMenu1 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'b0edc6861a494b79b97990dc05f0a524');
/* insertRoleMenu2 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'e4256d7b0ffc4a02906cf900322b6213');
/* insertRoleMenu3 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'b19b23b0459a4754bf1fb8cb234450f2');
/* insertRoleMenu4 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'4dc997fef71e4862b9db22de8e99a618');
/* insertRoleMenu5 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'9f20a757a6bc40ddbb650c70debbf660');
/* insertRoleMenu6 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'f426468abf714b1599729f8c36ebbb0d');
/* insertRoleMenu7 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'1734e586e96941268a4c5248b593cef9');
/* insertRoleMenu8 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'6bb17a41f6394ed0a8a6faf5ff781354');
/* insertRoleMenu9 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'56d0658c5a8848818ac05e8ffa5c0570');
/* insertRoleMenu10 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'ce709456e867425297955b3c40406d7e');
/* insertRoleMenu11 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'3099f497480c4b1987bce3f3a26c3fb4');
/* insertRoleMenu12 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'4cd8e4e9519e4cff95465194fdcc8d88');
/* insertRoleMenu13 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'234f8ec3c2bc42bf9f6202aecae36fd6');
/* insertRoleMenu14 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'c3a44b478d3241b899b9c3f4611bc2b6');
/* insertRoleMenu15 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'fd63a8e389e04ff3a86c3cea53a3b9d5');
/* insertRoleMenu16 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'7c040dfd8db347e5956a3bc1764653dc');
/* insertRoleMenu17 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'17e1ee23ca1443f1bc886c2f5eb7c24b');
/* insertRoleMenu18 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'2275cb125710414e91b617dd7c62f12c');
/* insertRoleMenu19 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'0a972ce655cb4c84809d58668b655900');
/* insertRoleMenu20 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'fc52d5284b8f4522802383c1ef732242');
/* insertRoleMenu21 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'2cb327ad59b140828fd26eb2a46cb948');
/* insertRoleMenu22 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'dd965b2c1dfd493fb5efc7e4bcac99d4');
/* insertRoleMenu23 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'30a5e70a1456447ebf90b5546e9bc321');
/* insertRoleMenu24 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'2a63040409094f1e9dc535dd78ce15b7');
/* insertRoleMenu25 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'0706112ff5dc46e388064a99bcdb0561');
/* insertRoleMenu26 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'e864c78aba63448892cbcb6a3a7f4da7');
/* insertRoleMenu27 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'ff6cd243a77c4ae98dacf6149c816c75');
/* insertRoleMenu28 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'733d3f35d49f45af99ca9220048583ba');
/* insertRoleMenu29 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'bcf64d623fdd4519ae345b7a08c071a1');
/* insertRoleMenu30 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'66cc21d7ce104dd6877cbce114c59fb3');
/* insertRoleMenu31 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'309dc29ad3c34408a68df8f867a5c9ff');
/* insertRoleMenu32 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'96554b09a2dd4f82bab7546fa59acd35');
/* insertRoleMenu33 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'d568f4c2b687404e8aec7b9edcae5767');
/* insertRoleMenu34 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'2fab774f8b6d40cb9d7e187babab2d91');
/* insertRoleMenu35 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'45d958ca78304f25b51f6c71cf66f6d8');
/* insertRoleMen36 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'44da90bc76a5419a841f4924333f7a66');
/* insertRoleMenu37 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'9a9557177d334c209cf73c3817fe3b63');
/* insertRoleMenu38 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'0a43d291e0c94ad88c8b690009279e34');
/* insertRoleMenu39 */
insert into `sys_role_menu` (`roleId`, `menuId`) values(@roleId,'5244f5c38eb24b918e9ad64d456daa38');