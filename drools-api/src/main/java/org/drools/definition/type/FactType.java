/**
 * Copyright 2010 JBoss Inc
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

package org.drools.definition.type;

import java.util.List;
import java.util.Map;

/**
 * FactType declarations are fact definitions (like classes) that
 * are managed alongside the rules.
 * 
 * You then communicate with the rulebase/knowledge base by using 
 * instances created by this.
 * 
 * There are utility set and get methods on this, as well as in the FactFields.
 *
 * The Object that is used is a javabean (which was generated by the engine). 
 * You can also use reflection on it as normal.
 *
 * @author etirelli
 */
public interface FactType
    extends
    java.io.Externalizable {

    /**
     * Returns the name of the FactType.
     * 
     * @return the name of the fact type.
     */
    public String getName();

    /**
     * Returns the list of FactField in this FactType
     *  
     * @return the list of the fields in this FactType
     */
    public List<FactField> getFields();

    /**
     * Returns the FactField identified by the given name.
     * 
     * @param name the name of the FactField
     * 
     * @return the FactField instance identified by the given name or null if none is found.
     */
    public FactField getField(String name);

    /**
     * Returns the Class<?> for this FactType. The class object can be used through reflection.
     * 
     * @return the generated class for this FactType
     */
    public Class< ? > getFactClass();

    /**
     * Create a new fact based on the declared fact type.
     * This object will usually be a javabean.
     */
    public Object newInstance() throws InstantiationException,
                               IllegalAccessException;

    /**
     * Sets the value of the field on a fact.
     * 
     * @param bean the object on which the field will be set.
     * @param field the name of the field to set.
     * @param value the value to be set on the field.
     */
    public void set(Object bean,
                    String field,
                    Object value);

    /**
     * Gets the value of the specified field on the dynamic fact.
     * 
     * @param bean the fact to read the field value from.
     * @param field the name of the field to read.
     * 
     * @return the value of the field read from the fact
     */
    public Object get(Object bean,
                      String field);

    /**
     * Get a map of the fields and their values for the bean.
     * 
     * @param the bean to read the fields from.
     * 
     * @return a map of field names and corresponding values on the bean.
     */
    public Map<String, Object> getAsMap(Object bean);

    /**
     * Sets the values of the bean from a map.
     * 
     * @param bean the fact to set the values of the fields on
     * @param values a map of field names and values to set
     */
    public void setFromMap(Object bean,
                           Map<String, Object> values);
}
