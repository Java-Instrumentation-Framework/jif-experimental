package binding.table;
/*
 * Copyright (c) 2007 Component House (Hugo Vidal Teixeira). All Rights Reserved.
 * Visit: http://www.componenthouse.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Component House (Hugo Vidal Teixeira) nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.jgoodies.binding.beans.Model;

/**
 * Model that represents a user in the system.
 *
 * @author Hugo Teixeira
 * @since September/2007
 */
public class User extends Model {

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_EMAIL = "email";
	public static final String PROPERTY_WEIGHT = "weight";
	
	private String name;
	private String email;
	private int weight;

	public User() {}

	public User(String name, String email, int weight) {
		this.name = name;
		this.email = email;
		this.weight = weight;
	}

	public String getName() { return name; }
	public void setName(String name) {
		String old = this.name;
		this.name = name;
		this.firePropertyChange(PROPERTY_NAME, old, this.name);
	}

	public String getEmail() { return email; }
	public void setEmail(String email) {
		String old = this.email;
		this.email = email;
		this.firePropertyChange(PROPERTY_EMAIL, old, this.email);
	}

	public int getWeight() { return weight; }
	public void setWeight(int weight) {
		int old = this.weight;
		this.weight = weight;
		this.firePropertyChange(PROPERTY_WEIGHT, old, this.weight);
	}

	public void copyTo(User user)  {
		user.setName(this.name);
		user.setEmail(this.email);
		user.setWeight(this.weight);
	}
}
