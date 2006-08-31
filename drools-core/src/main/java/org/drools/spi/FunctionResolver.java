package org.drools.spi;

import java.util.List;

public interface FunctionResolver {
    public List getFunctionImports();

    public void addFunctionImport(String functionImport);

    public String resolveFunction(String functionName, int numberOfArgs);
}
