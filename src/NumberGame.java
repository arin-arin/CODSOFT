import java.util.Scanner;
import java.util.Random;

public class NumberGame {
    public static void main(String[] args) {
        System.out.println("Welcome to the Number Guessing Game!");
        Scanner in = new Scanner(System.in);
        boolean playAgain; // if the player wishes to play again
        do {
            playGame(in); // calling playGame method
            playAgain = askToPlayAgain(in);
        } while (playAgain);
        System.out.println("Thank you for playing!");
    }

    static void playGame(Scanner in) {
        int MAX_ATTEMPTS = 7;
        int RANGE_MIN = 1; // minimum possible value of the random integer
        int RANGE_MAX = 100; // maximum possible value of the random integer

        Random rd = new Random(); // generates a pseudorandom integer
        int targetNumber = rd.nextInt(RANGE_MAX - RANGE_MIN + 1) + RANGE_MIN;
        int attemptsLeft = MAX_ATTEMPTS;
        boolean won = false;

        System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess the number between " + RANGE_MIN + " and " + RANGE_MAX);

        while (attemptsLeft > 0) { //the user can have multiple guesses as long as { attemptsLeft > 0 }
            int guess = userGuess(in);
            if (guess < 0) continue; // if the answer is below minimum value, continues the game

            attemptsLeft--;

            if (guess < targetNumber) {
                System.out.println("Too low!");
            } else if (guess > targetNumber) {
                System.out.println("Too high!");
            } else {
                won = true;
                break;
            }

            System.out.println("Attempts left: " + attemptsLeft);
        }

        gameResult(won, targetNumber);
    }

    static int userGuess(Scanner in) { // takes the user guess
        System.out.print("Enter your guess: ");
        int RANGE_MIN = 1;
        int RANGE_MAX = 100;
        int guess = Integer.parseInt(in.nextLine());
        if (guess < RANGE_MIN || guess > RANGE_MAX) {
            System.out.println("Please enter a number between " + RANGE_MIN + " and " + RANGE_MAX);
            return -1;
        }
        return guess;

    }

    static void gameResult(boolean won, int targetNumber) {
        if (won) {
            System.out.println("Congratulations! You've guessed the right number");
        } else {
            System.out.println("Game over! The number was " + targetNumber);
        }
    }

    static boolean askToPlayAgain(Scanner scanner) { // whether the player wishes to play again or not can be chosen either by types "yes" or "no"
        System.out.print("Do you want to play again? (yes/no): ");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }
}