package Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Scanner;

public class Inputter {
    public static final String DATE_PATTERN = "MM/dd/yyyy";
    public static final String LICENSE_PLATE_REGEX =
            "^[0-9]{2}[A-Za-z]-[0-9]{3}\\.[0-9]{2}$";
    private final Scanner scanner;

    public Inputter(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getString(String inpMsg, int minLength, int maxLength) {
        while (true) {
            String value = readLine(inpMsg).trim();
            if (value.length() >= minLength && value.length() <= maxLength) {
                return value;
            }
            System.out.printf("Enter between %d and %d characters.%n", minLength, maxLength);
        }
    }

    public String getNonEmptyString(String inpMsg) {
        while (true) {
            String value = readLine(inpMsg).trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field must not be empty.");
        }
    }

    public String getString(String inpMsg, String regex, String errorMessage) {
        while (true) {
            String value = readLine(inpMsg).trim();
            if (value.matches(regex)) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    public int getAnInteger(String inpMsg, int min, int max) {
        while (true) {
            Integer number = tryParseInt(readLine(inpMsg).trim());
            if (number != null && number >= min && number <= max) {
                return number;
            }
            System.out.printf("Enter a whole number from %d to %d.%n", min, max);
        }
    }

    public long getLong(String inpMsg, long minInclusive) {
        while (true) {
            Long number = tryParseLong(readLine(inpMsg).trim());
            if (number != null && number >= minInclusive) {
                return number;
            }
            System.out.printf("Enter a whole number greater than or equal to %d.%n",
                    minInclusive);
        }
    }

    public int getOneOf(String inpMsg, int... allowed) {
        if (allowed == null || allowed.length == 0) {
            throw new IllegalArgumentException("allowed values must not be empty");
        }
        while (true) {
            Integer number = tryParseInt(readLine(inpMsg).trim());
            if (number != null && contains(allowed, number)) {
                return number;
            }
            System.out.println("Choose one of: " + Arrays.toString(allowed));
        }
    }

    public LocalDate getDate(String inpMsg, String pattern) {
        DateTimeFormatter formatter = strictFormatter(pattern);
        while (true) {
            String value = readLine(inpMsg).trim();
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.println("Enter a valid date using " + pattern + ".");
            }
        }
    }

    public boolean getYesNo(String inpMsg) {
        while (true) {
            String value = readLine(inpMsg + " (Y/N): ").trim();
            if ("Y".equalsIgnoreCase(value) || "YES".equalsIgnoreCase(value)) {
                return true;
            }
            if ("N".equalsIgnoreCase(value) || "NO".equalsIgnoreCase(value)) {
                return false;
            }
            System.out.println("Enter Y or N.");
        }
    }

    public String getOptionalString(String inpMsg, String current,
            int minLength, int maxLength) {
        while (true) {
            String value = readLine(inpMsg + " [" + current + "]: ").trim();
            if (value.isEmpty()) {
                return current;
            }
            if (value.length() >= minLength && value.length() <= maxLength) {
                return value;
            }
            System.out.printf("Enter between %d and %d characters, "
                    + "or press Enter to keep the current value.%n",
                    minLength, maxLength);
        }
    }

    public String getOptionalNonEmptyString(String inpMsg, String current) {
        String value = readLine(inpMsg + " [" + current + "]: ").trim();
        return value.isEmpty() ? current : value;
    }

    public long getOptionalLong(String inpMsg, long current, long minInclusive) {
        while (true) {
            String value = readLine(inpMsg + " [" + current + "]: ").trim();
            if (value.isEmpty()) {
                return current;
            }
            Long number = tryParseLong(value);
            if (number != null && number >= minInclusive) {
                return number;
            }
            System.out.printf("Enter a whole number greater than or equal to %d, "
                    + "or press Enter to keep the current value.%n",
                    minInclusive);
        }
    }

    public int getOptionalOneOf(String inpMsg, int current, int... allowed) {
        if (allowed == null || allowed.length == 0) {
            throw new IllegalArgumentException("allowed values must not be empty");
        }
        while (true) {
            String value = readLine(inpMsg + " [" + current + "]: ").trim();
            if (value.isEmpty()) {
                return current;
            }
            Integer number = tryParseInt(value);
            if (number != null && contains(allowed, number)) {
                return number;
            }
            System.out.println("Choose one of " + Arrays.toString(allowed)
                    + ", or press Enter to keep the current value.");
        }
    }

    public LocalDate getOptionalDate(String inpMsg, LocalDate current, String pattern) {
        DateTimeFormatter formatter = strictFormatter(pattern);
        String currentText = current == null ? "" : formatter.format(current);
        while (true) {
            String value = readLine(inpMsg
                    + (currentText.isEmpty() ? ": " : " [" + currentText + "]: ")).trim();
            if (value.isEmpty()) {
                if (current == null) {
                    System.out.println("A date is required.");
                    continue;
                }
                return current;
            }
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.println("Enter a valid date using " + pattern
                        + ", or press Enter to keep the current value.");
            }
        }
    }

    public String readLine(String inpMsg) {
        System.out.print(inpMsg);
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("No more input is available.");
        }
        return scanner.nextLine();
    }

    private static DateTimeFormatter strictFormatter(String pattern) {
        String strictPattern = pattern.replace("yyyy", "uuuu");
        return DateTimeFormatter.ofPattern(strictPattern)
                .withResolverStyle(ResolverStyle.STRICT);
    }

    private static Integer tryParseInt(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static Long tryParseLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static boolean contains(int[] allowed, int number) {
        for (int option : allowed) {
            if (option == number) {
                return true;
            }
        }
        return false;
    }
}
