import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class RockPaperScissors {

    enum Move { ROCK, PAPER, SCISSORS }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Rock Paper Scissors (Console) ===");

        while (true) {
            System.out.println("\nChoose mode: 1) Player vs CPU  2) Player vs Player  3) Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": singlePlayer(sc); break;
                case "2": twoPlayer(sc); break;
                case "3":
                case "exit":
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option — try 1, 2, or 3.");
            }
        }
    }

    // ---------------- Single Player ----------------
    private static void singlePlayer(Scanner sc) {
        System.out.println("\n-- Player vs CPU --");
        int bestOf = askRounds(sc);

        int roundsToWin = bestOf / 2 + 1;
        int playerScore = 0, cpuScore = 0, round = 1;

        while (playerScore < roundsToWin && cpuScore < roundsToWin) {
            System.out.printf("%nRound %d — choose your move (rock/paper/scissors or r/p/s): ", round);
            Move player = readMoveSimulated(sc, "Player", false);  // ✨ no space for CPU
            Move cpu = randomMove();
            System.out.println("CPU chose: " + cpu.name().toLowerCase());

            int result = decideWinner(player, cpu);
            if (result == 0) System.out.println("It's a tie!");
            else if (result == 1) { System.out.println("You win this round!"); playerScore++; }
            else { System.out.println("CPU wins this round."); cpuScore++; }

            System.out.printf("Score -> You: %d  CPU: %d%n", playerScore, cpuScore);
            round++;
        }

        if (playerScore > cpuScore) System.out.println("\n*** You won the match! ***");
        else System.out.println("\n*** CPU won the match. Better luck next time! ***");
    }

    // ---------------- Two Player ----------------
    private static void twoPlayer(Scanner sc) {
        System.out.println("\n-- Player vs Player (Local) --");
        int bestOf = askRounds(sc);

        int roundsToWin = bestOf / 2 + 1;
        int p1Score = 0, p2Score = 0, round = 1;

        while (p1Score < roundsToWin && p2Score < roundsToWin) {
            System.out.printf("%nRound %d%n", round);

            Move p1 = readMoveSimulated(sc, "Player 1", true);   // ✨ space ON for P1
            Move p2 = readMoveSimulated(sc, "Player 2", true);   // ✨ space ON for P2

            System.out.println("\nPlayer 1 chose: " + p1.name().toLowerCase());
            System.out.println("Player 2 chose: " + p2.name().toLowerCase());

            int result = decideWinner(p1, p2);
            if (result == 0) System.out.println("It's a tie!");
            else if (result == 1) { System.out.println("Player 1 wins this round!"); p1Score++; }
            else { System.out.println("Player 2 wins this round!"); p2Score++; }

            System.out.printf("Score -> Player 1: %d  Player 2: %d%n", p1Score, p2Score);
            round++;
        }

        if (p1Score > p2Score) System.out.println("\n*** Player 1 won the match! ***");
        else System.out.println("\n*** Player 2 won the match! ***");
    }

    // Ask for best-of rounds
    private static int askRounds(Scanner sc) {
        int bestOf = 3;
        while (true) {
            System.out.print("Enter odd number of rounds for 'best of' (3,5,7) [default 3]: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) break;

            try {
                int val = Integer.parseInt(input);
                if (val > 0 && val % 2 == 1) { bestOf = val; break; }
                else System.out.println("Please enter a positive odd integer.");
            } catch (NumberFormatException e) {
                System.out.println("Not a number — try again.");
            }
        }
        return bestOf;
    }

    // Reads move from player (visible) and simulates vanishing by pushing lines
    private static Move readMoveSimulated(Scanner sc, String playerName, boolean clearSpace) { // ✨ added flag
        while (true) {
            System.out.printf("%s, enter your move (rock/paper/scissors or r/p/s): ", playerName);
            String input = sc.nextLine().trim().toLowerCase();

            // Special token replacements
            switch (input) {
                case "roc": input = "rock"; break;
                case "pap": input = "paper"; break;
                case "sci": input = "scissors"; break;
            }

            Move move = parseMove(input);
            if (move != null) {
                if (clearSpace) {                          // ✨ only add spaces when true
                    for (int i = 0; i < 30; i++) System.out.println();
                }
                return move;
            }

            System.out.println("Invalid move. Please type rock/paper/scissors or r/p/s.");
        }
    }

    // Standard move parsing
    private static Move parseMove(String s) {
        switch (s) {
            case "rock": case "r": return Move.ROCK;
            case "paper": case "p": return Move.PAPER;
            case "scissors": case "scissor": case "s": return Move.SCISSORS;
            default: return null;
        }
    }

    // Random CPU move
    private static Move randomMove() {
        int pick = ThreadLocalRandom.current().nextInt(3);
        return Move.values()[pick];
    }

    // Decide winner: 0=tie, 1=a wins, 2=b wins
    private static int decideWinner(Move a, Move b) {
        if (a == b) return 0;
        if ((a == Move.ROCK && b == Move.SCISSORS) ||
            (a == Move.SCISSORS && b == Move.PAPER) ||
            (a == Move.PAPER && b == Move.ROCK)) return 1;
        return 2;
    }
}