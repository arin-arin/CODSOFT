import java.util.Scanner;

public class Student_Grade_Calculator {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int MAX_SUBJECTS = 10;
        int MAX_MARKS = 100;
        System.out.println("Student Grade Calculator");
        System.out.println("Maximum subjects allowed: " + MAX_SUBJECTS);
        System.out.println("Maximum marks per subject: " + MAX_MARKS);
        System.out.println();
        int numSubjects = numberOfSubjects(in);// calling method to get number of subjects
        double[] marks = marks(in, numSubjects);// calling method to get marks for each subject
        double totalMarks = totalMarks(marks);// calling method to calculate total marks
        double averagePercentage = calculateAveragePercentage(totalMarks, numSubjects);// calling method to calculate average percentage
        char grade = calculateGrade(averagePercentage);// Calculate grade
        displayResults(marks, totalMarks, averagePercentage, grade, numSubjects);// Display results
    }


    static int numberOfSubjects(Scanner in) { // user inputs the total number of subjects that are to be graded
        int MAX_SUBJECTS = 10;
        int number_of_Subjects; // initializing
        do {
            System.out.print("Enter number of subjects (1-" + MAX_SUBJECTS + "): ");
            while (!in.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                System.out.print("Enter number of subjects (1-" + MAX_SUBJECTS + "): ");
                in.next();
            }
            number_of_Subjects = in.nextInt(); // giving input via scanner class
            if (number_of_Subjects < 1 || number_of_Subjects > MAX_SUBJECTS) { // creating a constraint regarding subjects, min = 2, max = 10
                System.out.println("Number of subjects must be between 1 and " + MAX_SUBJECTS);
            }
        } while (number_of_Subjects < 1 || number_of_Subjects > MAX_SUBJECTS);

        return number_of_Subjects;
    }


    static double[] marks(Scanner in, int numSubjects) { //takes inputs of marks of all the given subjects using for-loop
        double[] marks = new double[numSubjects];
        int MAX_MARKS = 100;
        System.out.println("Enter marks for each subject (out of " + MAX_MARKS + "):");

        for (int i = 0; i < numSubjects; i++) {
            do {
                System.out.print("Subject " + (i + 1) + ": ");
                while (!in.hasNextDouble()) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    System.out.print("Subject " + (i + 1) + ": ");
                    in.next();
                }
                marks[i] = in.nextDouble();

                if (marks[i] < 0 || marks[i] > MAX_MARKS) {
                    System.out.println("Marks must be between 0 and " + MAX_MARKS);
                }
            } while (marks[i] < 0 || marks[i] > MAX_MARKS);
        }

        return marks;
    }


    static double totalMarks(double[] marks) {
        double total = 0;
        for (double mark : marks) { //for every element mark in array marks, total = total + marks
            total += mark;
        }
        return total;
    }


    static double calculateAveragePercentage(double totalMarks, int numberSubjects) {
        return totalMarks / numberSubjects;
    }


    static char calculateGrade(double averagePercentage) {
        if (averagePercentage >= 90) {
            return 'A';
        } else if (averagePercentage >= 80) {
            return 'B';
        } else if (averagePercentage >= 70) {
            return 'C';
        } else if (averagePercentage >= 60) {
            return 'D';
        } else if (averagePercentage >= 50) {
            return 'E';
        } else {
            return 'F';
        }
    }


    static String getGradeDescription(char grade) {
        return switch (grade) { // enhanced switch case
            case 'A' -> "Excellent";
            case 'B' -> "Good";
            case 'C' -> "Average";
            case 'D' -> "Below Average";
            case 'E' -> "Poor";
            case 'F' -> "Fail";
            default -> "Unknown";
        };
    }


    static void displayResults(double[] marks, double totalMarks,double averagePercentage, char grade, int numSubjects) {

        int MAX_MARKS = 100;
        System.out.println("\n" + "=".repeat(50)); //repeats '=' 50 times to create a demarcation for design purposes
        System.out.println("             GRADE REPORT");
        System.out.println("=".repeat(50));

        // Display individual subject marks
        System.out.println("Subject-wise Marks:");
        System.out.println("-".repeat(25));
        for (int i = 0; i < marks.length; i++) {
            System.out.printf("Subject %d: %.2f/%d%n", (i + 1), marks[i], MAX_MARKS);
        }

        System.out.println("-".repeat(25));

        // Display summary
        System.out.printf("Total Marks:        %.2f/%.2f%n", totalMarks, (double)(numSubjects * MAX_MARKS));
        System.out.printf("Average Percentage: %.2f%%%n", averagePercentage);
        System.out.printf("Grade:              %c (%s)%n", grade, getGradeDescription(grade));
        System.out.println("=".repeat(50));

        // Display grade scale
        System.out.println("\nGrading Scale:");
        System.out.println("A (90-100): Excellent");
        System.out.println("B (80-89):  Good");
        System.out.println("C (70-79):  Average");
        System.out.println("D (60-69):  Below Average");
        System.out.println("E (50-59):  Poor");
        System.out.println("F (0-49):   Fail");

        // Performance message
        System.out.println("\nPerformance Summary:");
        if (grade == 'A') {
            System.out.println("Outstanding performance! Keep up the excellent work!");
        } else if (grade == 'B') {
            System.out.println("Good job! You're doing well.");
        } else if (grade == 'C') {
            System.out.println("Average performance. There's room for improvement.");
        } else if (grade == 'D') {
            System.out.println("Below average performance. Consider seeking help.");
        } else if (grade == 'E') {
            System.out.println("Poor performance. Additional study is recommended.");
        } else {
            System.out.println("Failed. Please consult with your teacher for improvement strategies.");
        }
    }
}
