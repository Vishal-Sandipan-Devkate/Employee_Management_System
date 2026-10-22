package mcoe.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class EmployeeValidator {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private EmployeeValidator() {
    }

    public static List<String> validate(String name, String email, String department) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty() || name.trim().length() > 100) {
            errors.add("Name is required and must be at most 100 characters.");
        }
        if (email == null || email.trim().isEmpty() || email.trim().length() > 255
                || !EMAIL.matcher(email.trim()).matches()) {
            errors.add("Enter a valid email address.");
        }
        if (department == null || department.trim().isEmpty() || department.trim().length() > 100) {
            errors.add("Department is required and must be at most 100 characters.");
        }
        return errors;
    }
}
