/* cms_menu_01 */
update sys_menu set location=10 where path='0001'
/* cms_menu_02 */
update sys_menu set location=8 where path='0002'
/* cms_menu_03 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('02e86a61e99746bea34236ea73dd52a5','','0003','CMS','CMS','menu','','','','1','0','cms',NULL,'9','1','1a19ef09b12344b4a797d6e6dfe7fb29','1468895671','0')
/* cms_menu_04 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('077cb6be4c7c41cc8955ee045a4f0286','68cdbf694f71445c8587a20234d6fe31','0003000300020001','添加链接','Add','data','','','','0','0','cms.link.link.add',NULL,'47','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468897043','0')
/* cms_menu_05 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('17500ef3a9e44b4fabb240162a164fcb','6075fc0cf0ef441b9d93cc3cab3445bf','0003000200020003','删除文章','Delete','data','','','','0','0','cms.content.article.delete',NULL,'40','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896170','0')
/* cms_menu_06 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('31ed2243077c44448cce26abfd5ae574','9822bafbe3454dfd8e8b974ebc304d03','0003000300010002','修改分类','Edit','data','','','','0','0','cms.link.class.edit',NULL,'44','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896957','0')
/* cms_menu_07 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('33aed9298643424783116e0cf0f7fcbe','6075fc0cf0ef441b9d93cc3cab3445bf','0003000200020001','添加文章','Add','data','','','','0','0','cms.content.article.add',NULL,'38','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896151','0')
/* cms_menu_08 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('36e0faf5062b4f6b95d4167cbb1f8fea','68cdbf694f71445c8587a20234d6fe31','0003000300020002','修改链接','Edit','data','','','','0','0','cms.link.link.edit',NULL,'48','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468897051','0')
/* cms_menu_09 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('3c24111091ad4a70ad2d9cc361311d2f','68cdbf694f71445c8587a20234d6fe31','0003000300020003','删除链接','Delete','data','','','','0','0','cms.link.link.delete',NULL,'49','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468897060','0')
/* cms_menu_10 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('3f330d729ca34dc9825c46122be1bfae','02e86a61e99746bea34236ea73dd52a5','00030003','广告链接','AD','menu','','','ti-link','1','0','cms.link',NULL,'41','1','1a19ef09b12344b4a797d6e6dfe7fb29','1468896230','0')
/* cms_menu_11 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('4781372b00bb4d52b429b58e72b80c68','b2631bbdbf824cc4b74d819c87962c0d','0003000200010001','添加栏目','Add','data','','','','0','0','cms.content.channel.add',NULL,'33','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896049','0')
/* cms_menu_12 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('50ba60ee650e4c739e6abc3ab71e4960','b2631bbdbf824cc4b74d819c87962c0d','0003000200010004','栏目排序','Sort','data','','','','0','0','cms.content.channel.sort',NULL,'36','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896092','0')
/* cms_menu_13 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('6075fc0cf0ef441b9d93cc3cab3445bf','6b6de8c720c645a1808e1c3e9ccbfc90','000300020002','文章管理','Article','menu','/platform/cms/article','data-pjax','','1','0','cms.content.article',NULL,'37','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896141','0')
/* cms_menu_14 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('68cdbf694f71445c8587a20234d6fe31','3f330d729ca34dc9825c46122be1bfae','000300030002','链接管理','Link','menu','/platform/cms/link/link','data-pjax','','1','0','cms.link.link',NULL,'46','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468897031','0')
/* cms_menu_15 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('6b6de8c720c645a1808e1c3e9ccbfc90','02e86a61e99746bea34236ea73dd52a5','00030002','内容管理','Content','menu','','','ti-pencil-alt','1','0','cms.content',NULL,'31','1','1a19ef09b12344b4a797d6e6dfe7fb29','1468895990','0')
/* cms_menu_16 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('7125a72beee34b21ab3df9bf01b7bce6','9822bafbe3454dfd8e8b974ebc304d03','0003000300010003','删除分类','Delete','data','','','','0','0','cms.link.class.delete',NULL,'45','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896968','0')
/* cms_menu_17 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('73a29d3f99224426b5a87c92da122275','d1e991ad38a8424daf9f7eb000ee27f4','0003000100010001','保存配置','Save','data','','','','0','0','cms.site.settings.save',NULL,'30','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468895899','0')
/* cms_menu_18 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('7db6207d0dab4d6e95a7eee4f2efe875','9822bafbe3454dfd8e8b974ebc304d03','0003000300010001','添加分类','Add','data','','','','0','0','cms.link.class.add',NULL,'43','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896947','0')
/* cms_menu_19 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('9822bafbe3454dfd8e8b974ebc304d03','3f330d729ca34dc9825c46122be1bfae','000300030001','链接分类','Class','menu','/platform/cms/link/class','data-pjax','','1','0','cms.link.class',NULL,'42','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896932','0')
/* cms_menu_20 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('b2631bbdbf824cc4b74d819c87962c0d','6b6de8c720c645a1808e1c3e9ccbfc90','000300020001','栏目管理','Channel','menu','/platform/cms/channel','data-pjax','','1','0','cms.content.channel',NULL,'32','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896018','0')
/* cms_menu_21 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('d1e991ad38a8424daf9f7eb000ee27f4','d920314e925c451da6d881e7a29743b7','000300010001','网站配置','Settings','menu','/platform/cms/site','data-pjax','','1','0','cms.site.settings',NULL,'29','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468895881','0')
/* cms_menu_22 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('d920314e925c451da6d881e7a29743b7','02e86a61e99746bea34236ea73dd52a5','00030001','站点管理','Site','menu','','','ti-world','1','0','cms.site',NULL,'28','1','1a19ef09b12344b4a797d6e6dfe7fb29','1468895821','0')
/* cms_menu_23 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('e461c62a1d5441619cd35612f3b40691','b2631bbdbf824cc4b74d819c87962c0d','0003000200010002','修改栏目','Edit','data','','','','0','0','cms.content.channel.edit',NULL,'34','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896060','0')
/* cms_menu_24 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('ef9f436c61654ec09efbfa79a40061cf','6075fc0cf0ef441b9d93cc3cab3445bf','0003000200020002','修改文章','Edit','data','','','','0','0','cms.content.article.edit',NULL,'39','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896159','0')
/* cms_menu_25 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, showit, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('f6fba69c3b704d79834b8bd2cc753729','b2631bbdbf824cc4b74d819c87962c0d','0003000200010003','删除栏目','Delete','data','','','','0','0','cms.content.channel.delete',NULL,'35','0','1a19ef09b12344b4a797d6e6dfe7fb29','1468896072','0')
