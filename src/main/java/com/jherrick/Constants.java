package com.jherrick;

public final class Constants {

	private Constants() {
	}

	// holds representations of bitboards with three-in-a-row
	public static final int WIN_BITBOARDS[] = new int[] { 0x124, 0x92, 0x49, 0x1C0, 0x38, 0x7, 0x111, 0x54 };

	// constants to clear bit at particular index
	// ex: CLRBIT[3] contains 111110111 to clear index 3
	public static final int CLRBIT[] = new int[] { 0xFF, 0x17F, 0x1BF, 0x1DF, 0x1EF, 0x1F7, 0x1FB, 0x1fD, 0x1FE };

	// constants that contain a single set bit to be used as a mask
	// ex: BIT_MASKS[3] contains 000001000
	public static final int BIT_MASKS[] = new int[] { 0x100, 0x80, 0x40, 0x20, 0x10, 0x8, 0x4, 0x2, 0x1 };

	// Boards that have two in a rows
	public static final int[] TWOINAROWS = { 0x180, 0xC0, 0x30, 0x18, 0x6, 0x3, 0x120, 0x24, 0x90, 0x12, 0x48, 0x9,
			0x110, 0x11, 0x50, 0x14 };

	// The elements in OPENTHIRDS correspond to the third squares that have to be
	// open for each element of TWOINAROWS to have a chance of getting a win
	public static final int[] OPENTHIRDS = { 0x40, 0x100, 0x8, 0x20, 0x1, 0x4, 0x4, 0x100, 0x2, 0x80, 0x1, 0x40, 0x1,
			0x100, 0x4, 0x40 };

	// ASCII codes for printing boards
	public static final int O_ASCII = 79;
	public static final int X_ASCII = 88;

	// programming constants for debugging, etc.
	public static final int REPORTING_LEVEL = 3;
	public static final int PRINT_DETAIL = 1;

	public static final int[] EVAL_CONSTANTS = { 1, 2, 3 };
}
