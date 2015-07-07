/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.wizzer.common.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.brickred.socialauth.Profile;

import javax.servlet.http.HttpSession;

public final class OAuthToken implements AuthenticationToken {

	private static final long serialVersionUID = 3376624432421737333L;
	private Profile credential;
	private String userId;
	private String addr;
	private HttpSession session;
	private boolean rname;
	public OAuthToken(Profile credential, String addr,HttpSession session) {
		this.credential = credential;
		this.addr = addr;
		this.session = session;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Object getPrincipal() {
		return userId;
	}

	public Profile getCredentials() {
		return credential;
	}

	public String getAddr() {
		return addr;
	}

	public HttpSession getSession() {
		return session;
	}

	public boolean isRname() {
		return rname;
	}

	public void setRname(boolean rname) {
		this.rname = rname;
	}
}