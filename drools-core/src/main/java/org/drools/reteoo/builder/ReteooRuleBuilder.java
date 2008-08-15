/*
 * Copyright 2006 JBoss Inc
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

package org.drools.reteoo.builder;

import org.drools.InitialFact;
import org.drools.RuleIntegrationException;
import org.drools.base.ClassObjectType;
import org.drools.common.BaseNode;
import org.drools.common.InternalRuleBase;
import org.drools.reteoo.QueryTerminalNode;
import org.drools.reteoo.ReteooBuilder;
import org.drools.reteoo.RuleTerminalNode;
import org.drools.reteoo.TerminalNode;
import org.drools.rule.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author etirelli
 *
 */
public class ReteooRuleBuilder {

    private BuildUtils utils;

    public ReteooRuleBuilder() {
        this.utils = new BuildUtils();

        this.utils.addBuilder( GroupElement.class,
                               new GroupElementBuilder() );
        this.utils.addBuilder( Pattern.class,
                               new PatternBuilder() );
        this.utils.addBuilder( EvalCondition.class,
                               new EvalBuilder() );
        this.utils.addBuilder( From.class,
                               new FromBuilder() );
        this.utils.addBuilder( Collect.class,
                               new CollectBuilder() );
        this.utils.addBuilder( Accumulate.class,
                               new AccumulateBuilder() );
        this.utils.addBuilder( Forall.class,
                               new ForallBuilder() );
        this.utils.addBuilder( EntryPoint.class,
                               new EntryPointBuilder() );
    }

    /**
     * Creates the corresponting Rete network for the given <code>Rule</code> and adds it to
     * the given rule base.
     * 
     * @param rule
     *            The rule to add.
     * @param rulebase
     *            The rulebase to add the rule to.
     *            
     * @return a List<BaseNode> of terminal nodes for the rule             
     * 
     * @throws RuleIntegrationException
     *             if an error prevents complete construction of the network for
     *             the <code>Rule</code>.
     * @throws InvalidPatternException
     */
    public List addRule(final Rule rule,
                        final InternalRuleBase rulebase,
                        final ReteooBuilder.IdGenerator idGenerator) throws InvalidPatternException {

        // the list of terminal nodes
        final List nodes = new ArrayList();

        // transform rule and gets the array of subrules
        final GroupElement[] subrules = rule.getTransformedLhs();

        for ( int i = 0; i < subrules.length; i++ ) {
            // creates a clean build context for each subrule
            final BuildContext context = new BuildContext( rulebase,
                                                           idGenerator );
           
            if ( rulebase.getConfiguration().isSequential() ) {
                context.setTupleMemoryEnabled( false );
                context.setObjectTypeNodeMemoryEnabled( false );
                context.setTerminalNodeMemoryEnabled( false );
                context.setAlphaNodeMemoryAllowed( false );
            } else {
                context.setTupleMemoryEnabled( true );
                context.setObjectTypeNodeMemoryEnabled( true );
                context.setTerminalNodeMemoryEnabled( true );
                context.setAlphaNodeMemoryAllowed( true );                
            }
            
            // adds subrule
            final TerminalNode node = this.addSubRule( context,
                                                       subrules[i],
                                                       rule );

            // adds the terminal node to the list of terminal nodes
            nodes.add( node );

        }

        return nodes;
    }

    private TerminalNode addSubRule(final BuildContext context,
                                    final GroupElement subrule,
                                    final Rule rule) throws InvalidPatternException {
        // gets the appropriate builder
        final ReteooComponentBuilder builder = this.utils.getBuilderFor( subrule );

        // checks if an initial-fact is needed
        if ( builder.requiresLeftActivation( this.utils,
                                             subrule ) ) {
            this.addInitialFactPattern( context,
                                        subrule,
                                        rule );
        }

        // builds and attach
        builder.build( context,
                       this.utils,
                       subrule );

        TerminalNode terminal = null;

        if ( !(rule instanceof Query) ) {
            terminal = new RuleTerminalNode( context.getNextId(),
                                             context.getTupleSource(),
                                             rule,
                                             subrule,
                                             context );
        } else {
            terminal = new QueryTerminalNode( context.getNextId(),
                                              context.getTupleSource(),
                                              rule,
                                              subrule );
        }
        if ( context.getWorkingMemories().length == 0 ) {
            ((BaseNode) terminal).attach();
        } else {
            ((BaseNode) terminal).attach( context.getWorkingMemories() );
        }

        ((BaseNode) terminal).networkUpdated();
        
        // adds the terminal no to the list of nodes created/added by this sub-rule
        context.getNodes().add((BaseNode) terminal );

        // assigns partition IDs to the new nodes
        assignPartitionId(context);

        return terminal;
    }

    /**
     * Assigns the current partition ID to the list of created nodes
     * 
     * @param context
     */
    private void assignPartitionId(BuildContext context) {
        if( context.getRuleBase().getConfiguration().isPartitionsEnabled() ) {
            org.drools.common.RuleBasePartitionId partitionId = null;
            if( context.getPartitionId() != null ) {
                // it means it shares nodes with an existing partition, so
                // assign the first id to the newly added nodes
                partitionId = context.getPartitionId();
            } else {
                // nodes are independent of existing nodes, so create a new
                // partition ID for them
                partitionId = context.getRuleBase().createNewPartitionId();
            }
            for( BaseNode node : context.getNodes() ) {
                node.setPartitionId( partitionId );
            }
        }
    }

    /**
     * Adds a query pattern to the given subrule
     * 
     * @param context
     * @param subrule
     * @param rule
     */
    private void addInitialFactPattern(final BuildContext context,
                                       final GroupElement subrule,
                                       final Rule rule) {
        
        // creates a pattern for initial fact
        final Pattern pattern = new Pattern( 0,
                                             new ClassObjectType( InitialFact.class ) );

        // adds the pattern as the first child of the given AND group element
        subrule.addChild( 0,
                          pattern );
    }

}
