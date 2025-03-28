package motorph.employeeportal;

/**
 *
 * @author Jam
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Attendance {
    /**
     * Displays the attendance records of the specified employee.
     * Reads data from a CSV file hosted on Google Drive and calculates hours worked.
     * @param employeeId The ID of the employee
     */
    public void displayAttendance(String employeeId) {
        System.out.println("\nAttendance Records for Employee #" + employeeId + ":");

        String fileUrl = "https://drive.google.com/uc?export=download&id=1lQMufI6JKpVuEsQSBnbc9RkXdnfbGi2T";
        
        try (Scanner scanner = new Scanner(downloadFile(fileUrl))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip the header line
            }

            String employeeName = null; // Store the employee's name only once
            boolean recordFound = false; // To check if the employee exists in the file

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] columns = line.split(",");
                if (columns.length < 6) {
                    continue; // Skip malformed lines
                }

                String empNumber = columns[0].trim();
                if (!empNumber.equals(employeeId)) {
                    continue; // Process only records that match the given employee ID
                }

                recordFound = true;

                // Set the employee's name only once
                if (employeeName == null) {
                    employeeName = columns[1].trim() + " " + columns[2].trim(); // Last Name + First Name
                    System.out.println("Employee Name: " + employeeName);
                }

                String logIn = columns[4].trim();
                String logOut = columns[5].trim();
                
                int minsLogIn = convertToMinutes(logIn);
                int minsLogOut = convertToMinutes(logOut);
                
                int totalMinsWorked = minsLogOut - minsLogIn - 60;
                int hoursWorked = totalMinsWorked / 60;
                int minsWorked = totalMinsWorked % 60;

                System.out.println("Hours Worked: " + hoursWorked + " hours, " + minsWorked + " minutes");
            }

            if (!recordFound) {
                System.out.println("No attendance records found for Employee ID: " + employeeId);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the attendance file.");
        }
    }

    private int convertToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    private InputStream downloadFile(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getInputStream();
    }
}
