package org.drools.dialect.mvel;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.Cheese;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.base.ClassFieldExtractor;
import org.drools.base.ClassObjectType;
import org.drools.common.InternalFactHandle;
import org.drools.lang.descr.EvalDescr;
import org.drools.lang.descr.RuleDescr;
import org.drools.reteoo.ReteTuple;
import org.drools.rule.Column;
import org.drools.rule.Declaration;
import org.drools.rule.EvalCondition;
import org.drools.rule.Package;
import org.drools.semantics.java.builder.BuildContext;
import org.drools.spi.DeclarationScopeResolver;
import org.drools.spi.FieldExtractor;

public class MVELEvalBuilderTest extends TestCase {

    public void setUp() {     
    }

    public void test1() {
        Package pkg = new Package( "pkg1" );
        RuleDescr ruleDescr = new RuleDescr( "rule 1" );

        InstrumentedBuildContent context = new InstrumentedBuildContent( pkg,
                                                                         ruleDescr );
        InstrumentedDeclarationScopeResolver declarationResolver = new InstrumentedDeclarationScopeResolver();
        final FieldExtractor extractor = new ClassFieldExtractor( Cheese.class,
                                                                  "price" );
        Column column = new Column( 0,
                                    new ClassObjectType( int.class ) );
        Declaration declaration = new Declaration( "a",
                                                   extractor,
                                                   column );
        Map map = new HashMap();
        map.put( "a",
                 declaration );
        declarationResolver.setDeclarations( map );
        context.setDeclarationResolver( declarationResolver );

        EvalDescr evalDescr = new EvalDescr();
        evalDescr.setText( "a == 10" );

        MVELEvalBuilder builder = new MVELEvalBuilder();
        EvalCondition eval = ( EvalCondition ) builder.build( context,
                                                    null,
                                                    null,
                                                    evalDescr );
        
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        WorkingMemory wm = ruleBase.newWorkingMemory();
        
        Cheese cheddar = new Cheese("cheddar", 10);
        InternalFactHandle f0 = ( InternalFactHandle ) wm.assertObject( cheddar );
        ReteTuple tuple = new ReteTuple( f0 );      
        
        assertTrue( eval.isAllowed( tuple, wm ) );
        
        cheddar.setPrice( 9 );
        wm.modifyObject( f0, cheddar );
        assertFalse( eval.isAllowed( tuple, wm ) );
    }

    public static class InstrumentedDeclarationScopeResolver extends DeclarationScopeResolver {
        private Map declarations;

        public InstrumentedDeclarationScopeResolver() {
            super( null );
        }

        public void setDeclarations(Map map) {
            this.declarations = map;
        }

        public Map getDeclarations() {
            return this.declarations;
        }
    }

    public static class InstrumentedBuildContent extends BuildContext {
        private DeclarationScopeResolver declarationScopeResolver;

        public InstrumentedBuildContent(Package pkg,
                                        RuleDescr ruleDescr) {
            super( pkg,
                   ruleDescr );
        }

        public void setDeclarationResolver(DeclarationScopeResolver declarationScopeResolver) {
            this.declarationScopeResolver = declarationScopeResolver;
        }

        public DeclarationScopeResolver getDeclarationResolver() {
            return this.declarationScopeResolver;
        }

    }

}
