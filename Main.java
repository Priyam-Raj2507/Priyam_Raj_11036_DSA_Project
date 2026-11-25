import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class main {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Student> students = new ArrayList<>();
    static String fileName = "students.txt";

    static class Student {
        int roll;
        String name;
        int attended;
        int held;

        Student(int roll, String name) {
            this.roll = roll;
            this.name = name;
            attended = 0;
            held = 0;
        }

        double percentage() {
            if (held == 0) return 0;
            return attended * 100.0 / held;
        }
    }

    public static void main(String[] args) {
        loadStudents();

        while (true) {
            System.out.println("\n1. Add student");
            System.out.println("2. Mark attendance");
            System.out.println("3. Search student");
            System.out.println("4. List defaulters");
            System.out.println("5. Sort by attendance");
            System.out.println("6. Save and exit");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(input.nextLine());

            if (choice == 1) addStudent();
            else if (choice == 2) markAttendance();
            else if (choice == 3) searchStudent();
            else if (choice == 4) listDefaulters();
            else if (choice == 5) sortStudents();
            else if (choice == 6) {
                saveStudents();
                System.out.println("Saved. Goodbye!");
                break;
            } else System.out.println("Invalid choice.");
        }
    }

    static Student findStudent(int roll) {
        for (Student s : students) {
            if (s.roll == roll) return s;
        }
        return null;
    }

    static void addStudent() {
        System.out.print("Roll number: ");
        int roll = Integer.parseInt(input.nextLine());
        if (findStudent(roll) != null) {
            System.out.println("Roll number already exists.");
            return;
        }

        System.out.print("Name: ");
        String name = input.nextLine();
        students.add(new Student(roll, name));
        System.out.println("Student added.");
    }

    static void markAttendance() {
        System.out.print("Roll number: ");
        int roll = Integer.parseInt(input.nextLine());
        Student s = findStudent(roll);
        if (s == null) {
            System.out.println("Roll " + roll + " not found.");
            return;
        }

        System.out.print("Present or Absent? (P/A): ");
        String status = input.nextLine().trim().toUpperCase();
        if (!status.equals("P") && !status.equals("A")) {
            System.out.println("Enter P or A only.");
            return;
        }

        s.held++;
        if (status.equals("P")) s.attended++;
        System.out.printf("%d | %s | %d/%d | %.1f%%\n",
                s.roll, s.name, s.attended, s.held, s.percentage());
    }

    static void searchStudent() {
        System.out.print("Roll number to search: ");
        int roll = Integer.parseInt(input.nextLine());
        Student s = findStudent(roll);
        if (s == null) System.out.println("Roll " + roll + " not found.");
        else System.out.printf("%d | %s | Attendance: %d/%d | %.1f%%\n",
                s.roll, s.name, s.attended, s.held, s.percentage());
    }

    static void listDefaulters() {
        boolean found = false;
        for (Student s : students) {
            if (s.percentage() < 75) {
                System.out.printf("%d | %s | %.1f%%\n", s.roll, s.name, s.percentage());
                found = true;
            }
        }
        if (!found) System.out.println("No defaulters found.");
    }

    static void sortStudents() {
        Collections.sort(students, new Comparator<Student>() {
            public int compare(Student a, Student b) {
                return Double.compare(b.percentage(), a.percentage());
            }
        });

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("%d. %d | %s | %.1f%%\n",
                    i + 1, s.roll, s.name, s.percentage());
        }
    }

    static void loadStudents() {
        try {
            Scanner file = new Scanner(new File(fileName));
            while (file.hasNextLine()) {
                String[] data = file.nextLine().split("\\|");
                Student s = new Student(Integer.parseInt(data[0]), data[1]);
                s.attended = Integer.parseInt(data[2]);
                s.held = Integer.parseInt(data[3]);
                students.add(s);
            }
            file.close();
        } catch (FileNotFoundException e) {
            // The file is created when the program saves for the first time.
        }
    }

    static void saveStudents() {
        try {
            PrintWriter file = new PrintWriter(fileName);
            for (Student s : students) {
                file.println(s.roll + "|" + s.name + "|" + s.attended + "|" + s.held);
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not save the file.");
        }
    }
}
