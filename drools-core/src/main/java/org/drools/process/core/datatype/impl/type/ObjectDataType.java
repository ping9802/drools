package org.drools.process.core.datatype.impl.type;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.process.core.datatype.DataType;

import com.thoughtworks.xstream.XStream;

/**
 * Representation of an object datatype.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ObjectDataType implements DataType {

    private static final long serialVersionUID = 4L;

    private String className;
    
    public ObjectDataType() {
    }

    public ObjectDataType(String className) {
    	setClassName(className);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    	className = in.readUTF();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
    	out.writeUTF(className);
    }

    public boolean verifyDataType(final Object value) {
        try {
            Class<?> clazz = ObjectDataType.class.forName(className);
            if (clazz.isInstance(value)) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                "Could not find data type " + className);
        }
        return false;
    }

	public Object readValue(String value) {
		XStream xstream = new XStream();
		return xstream.fromXML(value);
	}

	public String writeValue(Object value) {
		XStream xstream = new XStream();
		return xstream.toXML(value);
	}
}
