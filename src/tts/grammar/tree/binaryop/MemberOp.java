package tts.grammar.tree.binaryop;

import tts.eval.*;
import tts.eval.IValueEval.EvalType;
import tts.grammar.tree.Op;
import tts.vm.ScriptVM;
import tts.vm.rtexcpt.ScriptNullPointerException;
import tts.vm.rtexcpt.ScriptRuntimeException;

public final class MemberOp extends Op {

	Op body;
	String member;

	public MemberOp(Op body, String member) {
		super(body.getSourceLocation());
		this.body = body;
		this.member = member;
	}

	@Override
	public IValueEval eval(ScriptVM vm) {
		IValueEval b = body.eval(vm);
		if (b.getType() == EvalType.NULL)
			throw new ScriptNullPointerException(body.getSourceLocation());
		else if (!(b instanceof ObjectEval))
			throw new ScriptRuntimeException("value/object has no member", body.getSourceLocation());

		return ((ObjectEval) b).member(member, getSourceLocation());
	}

	@Override
	public Op optimize() {
		body = body.optimize();
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(body).append(".").append(member);
		return sb.toString();
	}
}
