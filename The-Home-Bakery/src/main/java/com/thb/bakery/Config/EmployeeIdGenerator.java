package com.thb.bakery.Config;

import org.springframework.stereotype.Component;

@Component
public class EmployeeIdGenerator {

    private static final String PREFIX = "THB";
    private static final int STARTING_NUMBER = 13;
    private static final int DIGITS = 4;

    /**
     * Generates the next employee ID based on the latest employee ID
     * @param latestEmployeeId The latest employee ID in the system (e.g., "THB0013")
     * @return The next employee ID (e.g., "THB0014")
     */
    public String generateNextEmployeeId(String latestEmployeeId) {
        if (latestEmployeeId == null || latestEmployeeId.trim().isEmpty()) {
            // If no employees exist yet, start from THB0013
            return String.format("%s%0" + DIGITS + "d", PREFIX, STARTING_NUMBER);
        }

        try {
            // Extract the numeric part from the employee ID
            String numericPart = latestEmployeeId.replace(PREFIX, "");
            int number = Integer.parseInt(numericPart);
            int nextNumber = number + 1;

            return String.format("%s%0" + DIGITS + "d", PREFIX, nextNumber);
        } catch (NumberFormatException e) {
            // If parsing fails, start from THB0013
            return String.format("%s%0" + DIGITS + "d", PREFIX, STARTING_NUMBER);
        }
    }

    /**
     * Generates the first employee ID
     * @return "THB0013"
     */
    public String generateFirstEmployeeId() {
        return String.format("%s%0" + DIGITS + "d", PREFIX, STARTING_NUMBER);
    }
}