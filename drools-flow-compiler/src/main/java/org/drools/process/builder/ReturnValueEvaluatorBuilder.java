package org.drools.process.builder;

import org.drools.compiler.ReturnValueDescr;
import org.drools.process.core.ContextResolver;
import org.drools.rule.builder.PackageBuildContext;
import org.drools.workflow.instance.impl.ReturnValueConstraintEvaluator;

public interface ReturnValueEvaluatorBuilder {

    public void build(final PackageBuildContext context,
                      final ReturnValueConstraintEvaluator returnValueConstraintEvaluator,
                      final ReturnValueDescr returnValueDescr,
                      final ContextResolver contextResolver);

}