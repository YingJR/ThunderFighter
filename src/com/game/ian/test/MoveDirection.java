package com.game.ian.test;

public enum MoveDirection {
	None(0), Up(1), Down(2), Left(3), Right(4);

	private int _value = -1;

	private MoveDirection(int value) {
		this._value = value;
	}

	int get_value() {
		return this._value;
	}

	MoveDirection getByValue(int value) {
		MoveDirection[] directions = values();
		for (int i = 0; i < directions.length; i++) {
			if (directions[i].get_value() == value) {
				return directions[i];
			}
		}
		return None;
	}

	public static MoveDirection getRandomDirection() {
		int value = (int) (Math.random() * (Right.get_value() - None.get_value() + 1));

		return None.getByValue(value);
	}
}
