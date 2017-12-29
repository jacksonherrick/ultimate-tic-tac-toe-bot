public final class Constants {

    private Constants() {}

    // holds representations of bitboards with three-in-a-row
    public static final int WIN_BITBOARDS[] = new int[] {
      0x124, 0x92, 0x49, 0x1C0, 0x38, 0x7, 0x111, 0x54
    };

    // constants to clear bit at particular index
    // ex: CLRBIT[3] contains 111110111 to clear index 3
    public static final int CLRBIT[] = new int[] {
      0x1FE, 0x1FD, 0x1FB, 0x1F7, 0x1EF, 0x1DF, 0x1BF, 0x17F, 0xFF
    };

    public static final int BIT_MASKS[] = new int[] {
      0x1, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80, 0x100
    };

    // ASCII codes for printing boards
    public static final int O_ASCII = 79;
    public static final int X_ASCII = 88;

    // programming constants for debugging, etc.
    public static final int REPORTING_LEVEL = 3;
    public static final int PRINT_DETAIL = 1;
}
