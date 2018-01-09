package main;

public class MoveAndValue implements Comparable<MoveAndValue> {
	public Move move;
	public int value;

	public MoveAndValue(Move _move, int _value) {
		move = _move;
		value = _value;
	}

	public int compareTo(MoveAndValue y) {
		return this.value - y.value;
	}
}
