package engine;

public class Bitboard {

    //This class contains constants for bit boards.

    //Rank 8
    public static final long H8 = 1L;
    public static final long G8 = H8 << 1;
    public static final long F8 = G8 << 1;
    public static final long E8 = F8 << 1;
    public static final long D8 = E8 << 1;
    public static final long C8 = D8 << 1;
    public static final long B8 = C8 << 1;
    public static final long A8 = B8 << 1;

    //Rank 7
    public static final long H7 = A8 << 1;
    public static final long G7 = H7 << 1;
    public static final long F7 = G7 << 1;
    public static final long E7 = F7 << 1;
    public static final long D7 = E7 << 1;
    public static final long C7 = D7 << 1;
    public static final long B7 = C7 << 1;
    public static final long A7 = B7 << 1;

    //Rank 6
    public static final long H6 = A7 << 1;
    public static final long G6 = H6 << 1;
    public static final long F6 = G6 << 1;
    public static final long E6 = F6 << 1;
    public static final long D6 = E6 << 1;
    public static final long C6 = D6 << 1;
    public static final long B6 = C6 << 1;
    public static final long A6 = B6 << 1;

    //Rank 5
    public static final long H5 = A6 << 1;
    public static final long G5 = H5 << 1;
    public static final long F5 = G5 << 1;
    public static final long E5 = F5 << 1;
    public static final long D5 = E5 << 1;
    public static final long C5 = D5 << 1;
    public static final long B5 = C5 << 1;
    public static final long A5 = B5 << 1;

    //Rank 4
    public static final long H4 = A5 << 1;
    public static final long G4 = H4 << 1;
    public static final long F4 = G4 << 1;
    public static final long E4 = F4 << 1;
    public static final long D4 = E4 << 1;
    public static final long C4 = D4 << 1;
    public static final long B4 = C4 << 1;
    public static final long A4 = B4 << 1;

    //Rank 3
    public static final long H3 = A4 << 1;
    public static final long G3 = H3 << 1;
    public static final long F3 = G3 << 1;
    public static final long E3 = F3 << 1;
    public static final long D3 = E3 << 1;
    public static final long C3 = D3 << 1;
    public static final long B3 = C3 << 1;
    public static final long A3 = B3 << 1;

    //Rank 2
    public static final long H2 = A3 << 1;
    public static final long G2 = H2 << 1;
    public static final long F2 = G2 << 1;
    public static final long E2 = F2 << 1;
    public static final long D2 = E2 << 1;
    public static final long C2 = D2 << 1;
    public static final long B2 = C2 << 1;
    public static final long A2 = B2 << 1;

    //Rank 1
    public static final long H1 = A2 << 1;
    public static final long G1 = H1 << 1;
    public static final long F1 = G1 << 1;
    public static final long E1 = F1 << 1;
    public static final long D1 = E1 << 1;
    public static final long C1 = D1 << 1;
    public static final long B1 = C1 << 1;
    public static final long A1 = B1 << 1;

    //Ranks
    public static final long RANK_1 = A1 | B1 | C1 | D1 | E1 | F1 | G1 | H1;
    public static final long RANK_2 = A2 | B2 | C2 | D2 | E2 | F2 | G2 | H2;
    public static final long RANK_3 = A3 | B3 | C3 | D3 | E3 | F3 | G3 | H3;
    public static final long RANK_4 = A4 | B4 | C4 | D4 | E4 | F4 | G4 | H4;
    public static final long RANK_5 = A5 | B5 | C5 | D5 | E5 | F5 | G5 | H5;
    public static final long RANK_6 = A6 | B6 | C6 | D6 | E6 | F6 | G6 | H6;
    public static final long RANK_7 = A7 | B7 | C7 | D7 | E7 | F7 | G7 | H7;
    public static final long RANK_8 = A8 | B8 | C8 | D8 | E8 | F8 | G8 | H8;

    //Files
    public static final long FILE_A = A1 | A2 | A3 | A4 | A5 | A6 | A7 | A8;
    public static final long FILE_B = B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8;
    public static final long FILE_C = C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8;
    public static final long FILE_D = D1 | D2 | D3 | D4 | D5 | D6 | D7 | D8;
    public static final long FILE_E = E1 | E2 | E3 | E4 | E5 | E6 | E7 | E8;
    public static final long FILE_F = F1 | F2 | F3 | F4 | F5 | F6 | F7 | F8;
    public static final long FILE_G = G1 | G2 | G3 | G4 | G5 | G6 | G7 | G8;
    public static final long FILE_H = H1 | H2 | H3 | H4 | H5 | H6 | H7 | H8;
}
