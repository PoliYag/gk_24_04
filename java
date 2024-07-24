import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserDataCollector {

    private static final int EXPECTED_FIELDS_COUNT = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные пользователя (Фамилия Имя Отчество датарождения номертелефона пол):");
        String input = scanner.nextLine();
        
        String[] fields = input.split(" ");
        if (fields.length != EXPECTED_FIELDS_COUNT) {
            handleDataCountError(fields.length);
            return;
        }

        try {
            String surname = fields[0];
            String name = fields[1];
            String patronymic = fields[2];
            String birthDate = parseBirthDate(fields[3]);
            long phoneNumber = parsePhoneNumber(fields[4]);
            char gender = parseGender(fields[5]);

            writeToFile(surname, name, patronymic, birthDate, phoneNumber, gender);
            System.out.println("Данные успешно сохранены.");

        } catch (InvalidBirthDateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (InvalidPhoneNumberException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (InvalidGenderException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void handleDataCountError(int actualCount) {
        if (actualCount < EXPECTED_FIELDS_COUNT) {
            System.out.println("Ошибка: Введено меньше данных, чем требуется. Ожидалось " + EXPECTED_FIELDS_COUNT + ", получено " + actualCount + ".");
        } else {
            System.out.println("Ошибка: Введено больше данных, чем требуется. Ожидалось " + EXPECTED_FIELDS_COUNT + ", получено " + actualCount + ".");
        }
    }

    private static String parseBirthDate(String date) throws InvalidBirthDateException {
        if (!date.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
            throw new InvalidBirthDateException("Дата рождения должна быть в формате dd.mm.yyyy.");
        }
        return date;
    }

    private static long parsePhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        try {
            return Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            throw new InvalidPhoneNumberException("Номер телефона должен быть целым беззнаковым числом.");
        }
    }

    private static char parseGender(String gender) throws InvalidGenderException {
        if (gender.length() != 1 || (gender.charAt(0) != 'm' && gender.charAt(0) != 'f')) {
            throw new InvalidGenderException("Пол должен быть символом 'f' или 'm'.");
        }
        return gender.charAt(0);
    }

    private static void writeToFile(String surname, String name, String patronymic, String birthDate, long phoneNumber, char gender) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(surname + ".txt", true))) {
            writer.write(String.format("%s %s %s %s %d %c", surname, name, patronymic, birthDate, phoneNumber, gender));
            writer.newLine();
        }
    }

    static class InvalidBirthDateException extends Exception {
        public InvalidBirthDateException(String message) {
            super(message);
        }
    }

    static class InvalidPhoneNumberException extends Exception {
        public InvalidPhoneNumberException(String message) {
            super(message);
        }
    }

    static class InvalidGenderException extends Exception {
        public InvalidGenderException(String message) {
            super(message);
        }
    }
}
