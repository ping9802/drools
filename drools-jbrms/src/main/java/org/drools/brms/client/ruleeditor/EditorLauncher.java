package org.drools.brms.client.ruleeditor;

import org.drools.brms.client.common.AssetFormats;
import org.drools.brms.client.modeldriven.SuggestionCompletionEngine;
import org.drools.brms.client.modeldriven.brxml.ActionAssertFact;
import org.drools.brms.client.modeldriven.brxml.ActionFieldValue;
import org.drools.brms.client.modeldriven.brxml.ActionRetractFact;
import org.drools.brms.client.modeldriven.brxml.ActionSetField;
import org.drools.brms.client.modeldriven.brxml.CompositeFactPattern;
import org.drools.brms.client.modeldriven.brxml.Constraint;
import org.drools.brms.client.modeldriven.brxml.DSLSentence;
import org.drools.brms.client.modeldriven.brxml.DSLSentenceFragment;
import org.drools.brms.client.modeldriven.brxml.FactPattern;
import org.drools.brms.client.modeldriven.brxml.IAction;
import org.drools.brms.client.modeldriven.brxml.IPattern;
import org.drools.brms.client.modeldriven.brxml.RuleModel;
import org.drools.brms.client.modeldriven.ui.RuleModeller;
import org.drools.brms.client.rpc.RuleAsset;

import com.google.gwt.user.client.ui.Widget;

/**
 * This launches the appropriate editor for the asset type.
 * 
 * @author Michael Neale
 */
public class EditorLauncher {

    /**
     * This will return the appropriate viewer for the asset.
     */
    public static Widget getEditorViewer(RuleAsset asset) {
        //depending on the format, load the appropriate editor
        if ( asset.metaData.format.equals( AssetFormats.BUSINESS_RULE ) ) {
            return new RuleModeller( getSuggestionCompletionEngine(asset), getRuleModel(asset) );
        } else {
            return new DefaultRuleContentWidget( asset );
        }

    }

    private static RuleModel getRuleModel(RuleAsset asset) {
//        RuleModel model = new RuleModel();
//        
//        model.lhs = new IPattern[3];
//        
//        FactPattern p1 = new FactPattern();
//        FactPattern p2 = new FactPattern();
//        CompositeFactPattern p3 = new CompositeFactPattern();
//        
//        
//        model.lhs[0] = p1;
//        model.lhs[1] = p2;
//        model.lhs[2] = p3;
//        
//        DSLSentence dsl = new DSLSentence();
//        dsl.elements = new DSLSentenceFragment[2];
//        dsl.elements[0] = new DSLSentenceFragment("There is a Storm alert of type", false);
//        dsl.elements[1] = new DSLSentenceFragment("(code here)", true);
//        
//        model.addLhsItem( dsl );
//        
//        dsl = new DSLSentence();
//        dsl.elements = new DSLSentenceFragment[2];
//        dsl.elements[0] = new DSLSentenceFragment("- severity rating is not more than", false);
//        dsl.elements[1] = new DSLSentenceFragment("(code here)", true);
//        
//        model.addLhsItem( dsl );
//            
//        
//        
//        
//        p1.factType = "Person";
//        p1.constraints = new Constraint[2];
//        p1.constraints[0] = new Constraint();
//        p1.constraints[1] = new Constraint();
//        p1.constraints[0].fieldName = "age";
//        p1.constraints[0].operator = "<";
//        p1.constraints[0].value = "42";
//
//        p1.constraints[1].fieldName = "name";
//        p1.constraints[1].operator = "==";
//        p1.constraints[1].value = "Bob";
//        p1.constraints[1].fieldBinding = "n";
//  
//        
//        
//        p2.factType = "Vehicle";
//        p2.boundName = "car1";
//        p2.constraints = new Constraint[1];
//        p2.constraints[0] = new Constraint();
//        p2.constraints[0].fieldName = "type";
//        p2.constraints[0].operator = "!=";
//        
//        p3.type = "not";
//        p3.patterns = new FactPattern[1];
//        FactPattern i1 = new FactPattern("Vehicle");
//        i1.constraints = new Constraint[1];
//        i1.constraints[0] = new Constraint();
//        i1.constraints[0].fieldName = "type";
//        i1.constraints[0].operator = "==";
//        
//        p3.patterns[0] = i1;
//        
//        ActionSetField set = new ActionSetField();
//        set.variable = "car1";
//        set.fieldValues = new ActionFieldValue[1];
//        set.fieldValues[0] = new ActionFieldValue();
//        set.fieldValues[0].field = "type";
//        
//        ActionAssertFact fact = new ActionAssertFact();
//        fact.factType = "Person";
//        fact.fieldValues = new ActionFieldValue[2];
//        fact.fieldValues[0] = new ActionFieldValue("name", "Mike");
//        fact.fieldValues[1] = new ActionFieldValue("age", "42");
//        
//        ActionRetractFact retract = new ActionRetractFact("car1");
//        
//        model.rhs = new IAction[3];
//        model.rhs[0] = set;
//        model.rhs[1] = fact;
//        model.rhs[2] = retract;
        
        
        
        return (RuleModel) asset.content;
    }

    private static SuggestionCompletionEngine getSuggestionCompletionEngine(RuleAsset asset) {
        SuggestionCompletionEngine com = new SuggestionCompletionEngine();
        
        com.addFact( "Person", new String[] {"age", "name"}  );
        com.addFact("Vehicle", new String[] {"type", "make"} );
        com.addOperators( "Person", "name", new String[] {"==", "!="});
        com.addOperators( "Person", "age", new String[] {"==", "!=", "<", ">", "<=", ">="});
        com.addOperators( "Vehicle", "age", new String[] {"==", "!=", "<", ">"});
        com.addOperators( "Vehicle", "type", new String[] {"==", "!=", "<", ">"});

        com.addConnectiveOperators( "Person", "name", new String[] {"|=", "!="});

        
        com.addConnectiveOperators( "Vehicle", "make", new String[] {"|="});
        
        DSLSentence sen = new DSLSentence();
        sen.elements = new DSLSentenceFragment[2];
        sen.elements[0] = new DSLSentenceFragment("This is a dsl expression", false);
        sen.elements[1] = new DSLSentenceFragment("(something)", true);
        com.addDSLCondition( sen );

        
        sen = new DSLSentence();
        sen.elements = new DSLSentenceFragment[3];
        sen.elements[0] = new DSLSentenceFragment("Send an email to [", false);
        sen.elements[1] = new DSLSentenceFragment("(someone)", true);
        sen.elements[2] = new DSLSentenceFragment("]", false);
        com.addDSLAction( sen );
        
        sen = new DSLSentence();
        sen.elements = new DSLSentenceFragment[1];
        sen.elements[0] = new DSLSentenceFragment("do nothing", false);        
        com.addDSLAction( sen );
        
        
        
        return com;
    }

}
