package org.drools.base.mvel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import org.drools.WorkingMemory;
import org.drools.rule.MVELDialectRuntimeData;
import org.drools.rule.Package;
import org.drools.spi.Action;
import org.drools.spi.ProcessContext;
import org.drools.spi.KnowledgeHelper;
import org.mvel.MVEL;
import org.mvel.compiler.CompiledExpression;
import org.mvel.debug.DebugTools;
import org.mvel.integration.impl.SimpleValueResolver;

public class MVELAction
    implements
    Action,
    MVELCompileable,
    Externalizable {
    private static final long       serialVersionUID = 400L;
    
    private MVELCompilationUnit unit;
    private String id;        

    private Serializable      expr;
    private DroolsMVELFactory prototype;

    public MVELAction() {
    }

    public MVELAction(final MVELCompilationUnit unit,
                              final String id) {
        this.unit = unit;
        this.id = id;
    }    

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readUTF();
        unit = ( MVELCompilationUnit ) in.readObject();
//        expr    = (Serializable)in.readObject();
//        prototype   = (DroolsMVELFactory)in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF( id );
        out.writeObject( unit );
//        out.writeObject(expr);
//        out.writeObject(prototype);
    }
    
    public void compile(ClassLoader classLoader) {
        expr = unit.getCompiledExpression( classLoader );
        prototype = unit.getFactory( );
    } 

    public String getDialect() {
        return id;
    }
    
    public void execute(final KnowledgeHelper knowledgeHelper, final WorkingMemory workingMemory, ProcessContext context) throws Exception {
        // must clone to avoid concurrency problems
        DroolsMVELFactory factory = (DroolsMVELFactory) this.prototype.clone();
        
        factory.addResolver("context", new SimpleValueResolver(context));
        
        factory.setContext( null,
                            knowledgeHelper,
                            null,
                            workingMemory,
                            null );
        
        Package pkg = workingMemory.getRuleBase().getPackage( "MAIN" );
        if ( pkg != null ) {
            MVELDialectRuntimeData data = ( MVELDialectRuntimeData ) pkg.getDialectRuntimeRegistry().getDialectData( id );
            factory.setNextFactory( data.getFunctionFactory() );
        }        
        
        CompiledExpression compexpr = (CompiledExpression) this.expr;

        //Receive breakpoints from debugger
        MVELDebugHandler.prepare();

        if ( MVELDebugHandler.isDebugMode() ) {
            if ( MVELDebugHandler.verbose ) {
                System.out.println( DebugTools.decompile( compexpr ) );
            }
            MVEL.executeDebugger( compexpr,
                                  null,
                                  factory );
        } else {
            MVEL.executeExpression( compexpr,
                                    null,
                                    factory );
        }

    }

    public Serializable getCompExpr() {
        return expr;
    }

}
