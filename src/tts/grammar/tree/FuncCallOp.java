package tts.grammar.tree;

import java.util.ArrayList;

import tts.eval.FunctionEval;
import tts.eval.IValueEval;
import tts.util.SourceLocation;
import tts.vm.*;
import tts.vm.rtexcpt.ScriptRuntimeException;

public final class FuncCallOp implements IOp {

	IOp func;
	ArrayList<IOp> args;

	public FuncCallOp(IOp func, ArrayList<IOp> args) {
		this.func = func;
		this.args = args;
	}

	@Override
	public IValueEval eval(ScriptVM vm) {
		IValueEval b = func.eval(vm);
		if (b.getType() != IValueEval.EvalType.FUNCTION)
			throw new ScriptRuntimeException("function value needed", func);

		ArrayList<IValueEval> as = new ArrayList<IValueEval>();
		for (int i = 0, size = args.size(); i < size; ++i)
			as.add(args.get(i).eval(vm));

		FunctionEval fe = (FunctionEval) b;
		vm.pushCallFrame(func.getSourceLocation(), fe.getModuleName());
		IValueEval ret = fe.call(as, vm, getSourceLocation());
		vm.popCallFrame();
		return ret;
	}

	@Override
	public IOp optimize() {
		func = func.optimize();
		for (int i = 0, size = args.size(); i < size; ++i)
			args.set(i, args.get(i).optimize());
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(func).append("(");
		for (int i = 0; i < args.size(); ++i) {
			if (i != 0)
				sb.append(",");
			sb.append(args.get(i));
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public SourceLocation getSourceLocation() {
		return func.getSourceLocation();
	}

}
