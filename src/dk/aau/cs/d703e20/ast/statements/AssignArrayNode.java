package dk.aau.cs.d703e20.ast.statements;

import dk.aau.cs.d703e20.ast.CodePosition;
import dk.aau.cs.d703e20.ast.expressions.ArrayParamNode;

import java.util.List;

public class AssignArrayNode extends StatementNode {
    private final String variableName;
    private final List<ArrayParamNode> paramNodes;

    public AssignArrayNode(String variableName, List<ArrayParamNode> paramNodes) {
        this.variableName = variableName;
        this.paramNodes = paramNodes;
    }

    public String getVariableName() {
        return variableName;
    }

    public List<ArrayParamNode> getParamNodes() {
        return paramNodes;
    }

    @Override
    public String prettyPrint(int indentation) {
        StringBuilder sb = new StringBuilder();

        sb.append(variableName);
        sb.append(" = {");
        String prefix = "";
        for (ArrayParamNode param : paramNodes) {
            sb.append(prefix);
            sb.append(param.prettyPrint(indentation));
            prefix = ", ";
        }
        sb.append("};");

        return sb.toString();
    }

    @Override
    public void setCodePosition(CodePosition codePosition) {

    }

    @Override
    public CodePosition getCodePosition() {
        return null;
    }
}
