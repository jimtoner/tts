package tts.grammar.tree;

import tts.eval.IValueEval;
import tts.vm.ScriptVM;

public interface IOp {

	IValueEval eval(ScriptVM vm);
}
