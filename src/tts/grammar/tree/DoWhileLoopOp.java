package tts.grammar.tree;

import tts.eval.*;
import tts.vm.*;

public class DoWhileLoopOp implements IOp {

	IOp body, brk_exp;

	public DoWhileLoopOp(IOp body, IOp brk) {
		this.body = body;
		this.brk_exp = brk;
	}

	@Override
	public IValueEval eval(ScriptVM vm) {
		while (true) {
			try {
				body.eval(vm);
			} catch (BreakLoopException e) {
				break;
			} catch (ContinueLoopException e) {
				// do nothing
			}

			IValueEval ve = brk_exp.eval(vm);
			if (ve.getType() != IValueEval.Type.BOOLEAN)
				throw new ScriptRuntimeException("");
			BooleanEval be = (BooleanEval) ve;
			if (!be.getValue())
				break;
		}
		return VoidEval.instance;
	}
}