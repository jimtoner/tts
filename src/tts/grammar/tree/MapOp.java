package tts.grammar.tree;

import java.util.ArrayList;

import tts.eval.IValueEval;
import tts.eval.MapEval;
import tts.util.SourceLocation;
import tts.vm.ScriptVM;

public class MapOp extends Op {

	public static class Entry {
		Op key, value;

		public Entry(Op k, Op v) {
			key = k;
			value = v;
		}
	}

	ArrayList<Entry> entries;

	public MapOp(ArrayList<Entry> e, String file, int line) {
		super(new SourceLocation(file, line));
		entries = e;
	}

	@Override
	public IValueEval eval(ScriptVM vm) {
		MapEval me = new MapEval();
		for (int i = 0, size = entries.size(); i < size; ++i) {
			IValueEval k = entries.get(i).key.eval(vm);
			IValueEval v = entries.get(i).value.eval(vm);
			me.put(k, v);
		}
		return me;
	}

	@Override
	public Op optimize() {
		for (int i = 0, size = entries.size(); i < size; ++i) {
			Entry e = entries.get(i);
			e.key = e.key.optimize();
			e.value = e.value.optimize();
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0, size = entries.size(); i < size; ++i) {
			Entry e = entries.get(i);
			sb.append(e.key.toString());
			sb.append(":");
			sb.append(e.value.toString());
			sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
}
