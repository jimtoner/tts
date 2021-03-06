package tts.grammar.tree;

import java.util.ArrayList;

import tts.eval.*;
import tts.eval.IValueEval.EvalType;
import tts.vm.ScriptVM;
import tts.vm.rtexcpt.*;

public final class FuncCallOp extends Op {

	Op func;
	ArrayList<Op> args;

	public FuncCallOp(Op func, ArrayList<Op> args) {
		super(func.getSourceLocation());
		this.func = func;
		this.args = args;
	}

	@Override
	public IValueEval eval(ScriptVM vm) {
		IValueEval b = func.eval(vm);
		if (b.getType() == EvalType.NULL)
			throw new ScriptNullPointerException(func.getSourceLocation());
		else if (b.getType() != IValueEval.EvalType.FUNCTION)
			throw new ScriptRuntimeException("function value needed", func.getSourceLocation());

		ArrayList<IValueEval> as = new ArrayList<IValueEval>();
		for (int i = 0, size = args.size(); i < size; ++i)
			as.add(args.get(i).eval(vm));

		FunctionEval fe = (FunctionEval) b;
		vm.enterFrame(func.getSourceLocation(), fe.getModuleName());
		final IValueEval ret;
		try {
			ret = fe.call(as, vm, getSourceLocation());
		} catch (ScriptLogicException e) {
			vm.leaveFrame();
			throw e;
		}
		vm.leaveFrame();
		return ret;
	}

	@Override
	public Op optimize() {
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
}
