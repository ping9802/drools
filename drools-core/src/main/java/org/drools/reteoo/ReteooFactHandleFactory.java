package org.drools.reteoo;

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

import org.drools.TemporalSession;
import org.drools.common.AbstractFactHandleFactory;
import org.drools.common.DefaultFactHandle;
import org.drools.common.EventFactHandle;
import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.rule.TypeDeclaration;
import org.drools.spi.FactHandleFactory;

public class ReteooFactHandleFactory extends AbstractFactHandleFactory {

    private static final long serialVersionUID = 400L;

    public ReteooFactHandleFactory() {
        super();
    }

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        super.readExternal( in );
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal( out );
    }

    /* (non-Javadoc)
     * @see org.drools.reteoo.FactHandleFactory#newFactHandle(long)
     */
    protected final InternalFactHandle newFactHandle(final int id,
                                                     final Object object,
                                                     final long recency,
                                                     final ObjectTypeConf conf,
                                                     final InternalWorkingMemory workingMemory) {
        if ( conf != null && conf.isEvent() ) {
            TypeDeclaration type = conf.getTypeDeclaration();
            long timestamp = ((TemporalSession) workingMemory).getSessionClock().getCurrentTime();
            long duration = 0;
            if( type.getDurationExtractor() != null ) {
                duration = type.getDurationExtractor().getLongValue( workingMemory, object );
            }
            return new EventFactHandle( id,
                                        object,
                                        recency,
                                        timestamp,
                                        duration ); 
        } else {
            return new DefaultFactHandle( id,
                                          object,
                                          recency );
        }
    }

    /* (non-Javadoc)
     * @see org.drools.reteoo.FactHandleFactory#newInstance()
     */
    public FactHandleFactory newInstance() {
        return new ReteooFactHandleFactory();
    }

    public Class getFactHandleType() {
        return DefaultFactHandle.class;
    }
}
