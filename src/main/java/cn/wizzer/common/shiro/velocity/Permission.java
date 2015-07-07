package cn.wizzer.common.shiro.velocity;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * =================================================================================================
 *
 * This software consists of voluntary contributions made by many individuals on behalf of the
 * Apache Software Foundation. For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * +------------------------------------------------------------------------------------------------+
 * | License: http://shiro-velocity-support.buession.com.cn/LICENSE 								|
 * | Author: Yong.Teng <webmaster@buession.com> 													|
 * | Copyright @ 2013-2014 Buession.com Inc.														|
 * +------------------------------------------------------------------------------------------------+
 */

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Velocity 模板引擎，Shiro 权限标签
 *
 * @author Yong.Teng <webmaster@buession.com>
 */
@IocBean
public class Permission {

    private static final String ROLE_NAMES_DELIMETER = ",";
    private static final String PERMISSION_NAMES_DELIMETER = ",";

    private static final Logger logger = LoggerFactory.getLogger(Permission.class);

    /**
     * 验证是否为已认证通过的用户，不包含已记住的用户，这是与 isUser 标签方法的区别所在。
     *
     * @return 用户是否已通过认证
     */
    public boolean isAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.isAuthenticated() == true;
    }

    /**
     * 验证是否为未认证通过用户，与 isAuthenticated 标签相对应，与 isGuest 标签的区别是，该标签包含已记住用户。
     *
     * @return 用户是否未通过认证
     */
    public boolean isNotAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject == null || subject.isAuthenticated() == false;
    }

    /**
     * 验证用户是否为 <访客>，即未认证（包含未记住）的用户。
     *
     * @return 用户是否为 <访客>
     */
    public boolean isGuest() {
        Subject subject = SecurityUtils.getSubject();
        return subject == null || subject.getPrincipal() == null;
    }

    /**
     * 验证用户是否认证通过或已记住的用户。
     *
     * @return 用户是否认证通过或已记住的用户
     */
    public boolean isUser() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.getPrincipal() != null;
    }

    /**
     * 返回用户 Principal。
     *
     * @return 用户 Principal
     */
    public Object getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null ? subject.getPrincipal() : null;
    }

    /**
     * 返回用户属性。
     *
     * @param property 属性名称
     * @return 用户属性
     */
    public Object getPrincipalProperty(String property) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null) {
            Object principal = subject.getPrincipal();

            try {
                BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

                for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                    if (pd.getName().equals(property) == true) {
                        return pd.getReadMethod().invoke(principal, (Object[]) null);
                    }
                }

                logger.trace("Property [{}] not found in principal of type [{}]", property,
                        principal.getClass().getName());
            } catch (Exception e) {
                logger.trace("Error reading property [{}] from principal of type [{}]", property,
                        principal.getClass().getName());
            }
        }

        return null;
    }

    /**
     * 验证用户是否具备某角色。
     *
     * @param role 角色名称
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.hasRole(role) == true;
    }

    /**
     * 验证用户是否不具备某角色，与 hasRole 逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role) {
        return hasRole(role) != true;
    }

    /**
     * 验证用户是否具有以下任意一个角色。
     *
     * @param roleNames 以 delimeter 为分隔符的角色列表
     * @param delimeter 角色列表分隔符
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roleNames, String delimeter) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            if (delimeter == null || delimeter.length() == 0) {
                delimeter = ROLE_NAMES_DELIMETER;
            }

            for (String role : roleNames.split(delimeter)) {
                if (subject.hasRole(role.trim()) == true) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 验证用户是否具有以下任意一个角色。
     *
     * @param roleNames 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roleNames) {
        return hasAnyRoles(roleNames, ROLE_NAMES_DELIMETER);
    }

    /**
     * 验证用户是否具有以下任意一个角色。
     *
     * @param roleNames 角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(Collection<String> roleNames) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && roleNames != null) {
            for (String role : roleNames) {
                if (role != null && subject.hasRole(role.trim()) == true) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 验证用户是否具有以下任意一个角色。
     *
     * @param roleNames 角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String[] roleNames) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && roleNames != null) {
            for (int i = 0; i < roleNames.length; i++) {
                String role = roleNames[i];
                if (role != null && subject.hasRole(role.trim()) == true) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 验证用户是否具备某权限。
     *
     * @param permission 权限名称
     * @return 用户是否具备某权限
     */
    public boolean hasPermission(String permission) {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.isPermitted(permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermission 逻辑相反。
     *
     * @param permission 权限名称
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermission(String permission) {
        return hasPermission(permission) != true;
    }

    /**
     * 验证用户是否具有以下任意一个权限。
     *
     * @param permissions 以 delimeter 为分隔符的权限列表
     * @param delimeter   权限列表分隔符
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermissions(String permissions, String delimeter) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null) {
            if (delimeter == null || delimeter.length() == 0) {
                delimeter = PERMISSION_NAMES_DELIMETER;
            }

            for (String permission : permissions.split(delimeter)) {
                if (permission != null && subject.isPermitted(permission.trim()) == true) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 验证用户是否具有以下任意一个权限。
     *
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermissions(String permissions) {
        return hasAnyPermissions(permissions, PERMISSION_NAMES_DELIMETER);
    }

    /**
     * 验证用户是否具有以下任意一个权限。
     *
     * @param permissions 权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermissions(Collection<String> permissions) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && permissions != null) {
            for (String permission : permissions) {
                if (permission != null && subject.isPermitted(permission.trim()) == true) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 验证用户是否具有以下任意一个权限。
     *
     * @param permissions 权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermissions(String[] permissions) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (permission != null && subject.isPermitted(permission.trim()) == true) {
                    return true;
                }
            }
        }

        return false;
    }

}
