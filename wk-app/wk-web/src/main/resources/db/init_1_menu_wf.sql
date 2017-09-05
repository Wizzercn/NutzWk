/* wf01 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('d49a7990416c455cb342a0b4dbdabb2d','','0004','工作流',NULL,'menu','','','','1','0','wf',NULL,'40','1','','1504600562','0')
/* wf02 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('41c9dbb82f074f2e9938625ec304b38c','d49a7990416c455cb342a0b4dbdabb2d','00040001','流程配置',NULL,'menu','','','','1','0','wf.cfg',NULL,'41','1','','1504601825','0')
/* wf03 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('b4ccefa459d04250b4453334a33cc750','41c9dbb82f074f2e9938625ec304b38c','000400010001','流程定义',NULL,'menu','/platform/wf/cfg/model','data-pjax','','1','0','wf.cfg.model',NULL,'42','0','','1504601534','0')
/* wf04 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('b0fa810799884dcb862abab16d2cc50e','41c9dbb82f074f2e9938625ec304b38c','000400010004','流程部署',NULL,'menu','/platform/wf/cfg/deploy','data-pjax','','1','0','wf.cfg.deploy',NULL,'43','0','','1504601620','0')
/* wf05 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('af05ce13d20344c5a9dccd18d42fce6e','41c9dbb82f074f2e9938625ec304b38c','000400010002','流程实例',NULL,'menu','/platform/wf/cfg/flow','data-pjax','','1','0','wf.cfg.flow',NULL,'44','0','','1504601561','0')
/* wf06 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('a417ef10992648239776d9f1d0ef1bda','41c9dbb82f074f2e9938625ec304b38c','000400010003','流程任务',NULL,'menu','/platform/wf/cfg/task','data-pjax','','1','0','wf.cfg.task',NULL,'45','0','','1504601596','0')
/* wf07 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('16bd0d5f786448368d9668a7aca019ca','d49a7990416c455cb342a0b4dbdabb2d','00040002','流程历史',NULL,'menu','','','','1','0','wf.hi',NULL,'46','1','','1504601696','0')
/* wf08 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('7ddd92dfd5a843609184c647779ace13','16bd0d5f786448368d9668a7aca019ca','000400020001','历史实例',NULL,'menu','/platform/wf/hi/flow','data-pjax','','1','0','wf.hi.flow',NULL,'47','0','','1504601734','0')
/* wf09 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('d2098984feea411eb927dc9cf60c2269','16bd0d5f786448368d9668a7aca019ca','000400020002','历史节点',NULL,'menu','/platform/wf/hi/node','data-pjax','','1','0','wf.hi.node',NULL,'48','0','','1504601764','0')
/* wf10 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('0611fbe227e847fea5a1e7a12e8e1345','16bd0d5f786448368d9668a7aca019ca','000400020003','历史任务',NULL,'menu','/platform/wf/hi/task','data-pjax','','1','0','wf.hi.task',NULL,'49','0','','1504601794','0')
/* wf11 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('bb9480e1157f4c929345c908bee1a779','d49a7990416c455cb342a0b4dbdabb2d','00040003','我的流程',NULL,'menu','','','','1','0','wf.my',NULL,'50','1','','1504601811','0')
/* wf12 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('0c3c57275c1d45c08856b3f003cfacf5','bb9480e1157f4c929345c908bee1a779','000400030001','我的待办',NULL,'menu','/platform/wf/my/todo','data-pjax','','1','0','wf.my.todo',NULL,'51','0','','1504601924','0')
/* wf13 */
insert into sys_menu (id, parentId, path, name, aliasName, type, href, target, icon, isShow, disabled, permission, note, location, hasChildren, opBy, opAt, delFlag) values('91693f03a39540cfb7be66722d6d21a7','bb9480e1157f4c929345c908bee1a779','000400030002','我的已办',NULL,'menu','/platform/wf/my/done','data-pjax','','1','0','wf.my.done',NULL,'52','0','','1504601950','0')