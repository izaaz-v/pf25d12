public class AIPlayer {
    private Seed mySeed;
    private Seed opponentSeed;

    public AIPlayer(Seed playerSeed) {
        this.mySeed = playerSeed;
        this.opponentSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    public int[] findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (board.cells[r][c].content == Seed.NO_SEED) {
                    board.cells[r][c].content = mySeed;
                    int score = minimax(board, 0, false);
                    board.cells[r][c].content = Seed.NO_SEED;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = r;
                        bestMove[1] = c;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        State result = evaluateBoard(board);
        if (result != State.PLAYING) {
            return score(result);
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int r = 0; r < Board.ROWS; r++) {
                for (int c = 0; c < Board.COLS; c++) {
                    if (board.cells[r][c].content == Seed.NO_SEED) {
                        board.cells[r][c].content = mySeed;
                        bestScore = Math.max(bestScore, minimax(board, depth + 1, false));
                        board.cells[r][c].content = Seed.NO_SEED;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int r = 0; r < Board.ROWS; r++) {
                for (int c = 0; c < Board.COLS; c++) {
                    if (board.cells[r][c].content == Seed.NO_SEED) {
                        board.cells[r][c].content = opponentSeed;
                        bestScore = Math.min(bestScore, minimax(board, depth + 1, true));
                        board.cells[r][c].content = Seed.NO_SEED;
                    }
                }
            }
            return bestScore;
        }
    }

    private int score(State result) {
        if ((result == State.CROSS_WON && mySeed == Seed.CROSS) || (result == State.NOUGHT_WON && mySeed == Seed.NOUGHT)) {
            return 10;
        } else if ((result == State.CROSS_WON && opponentSeed == Seed.CROSS) || (result == State.NOUGHT_WON && opponentSeed == Seed.NOUGHT)) {
            return -10;
        } else {
            return 0;
        }
    }

    private State evaluateBoard(Board board) {
        for (int i = 0; i < 3; i++) {
            if (board.cells[i][0].content != Seed.NO_SEED && board.cells[i][0].content == board.cells[i][1].content && board.cells[i][1].content == board.cells[i][2].content)
                return board.cells[i][0].content == Seed.CROSS ? State.CROSS_WON : State.NOUGHT_WON;
            if (board.cells[0][i].content != Seed.NO_SEED && board.cells[0][i].content == board.cells[1][i].content && board.cells[1][i].content == board.cells[2][i].content)
                return board.cells[0][i].content == Seed.CROSS ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (board.cells[0][0].content != Seed.NO_SEED && board.cells[0][0].content == board.cells[1][1].content && board.cells[1][1].content == board.cells[2][2].content)
            return board.cells[0][0].content == Seed.CROSS ? State.CROSS_WON : State.NOUGHT_WON;
        if (board.cells[0][2].content != Seed.NO_SEED && board.cells[0][2].content == board.cells[1][1].content && board.cells[1][1].content == board.cells[2][0].content)
            return board.cells[0][2].content == Seed.CROSS ? State.CROSS_WON : State.NOUGHT_WON;

        for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++) if (board.cells[r][c].content == Seed.NO_SEED) return State.PLAYING;
        return State.DRAW;
    }
}