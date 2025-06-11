package jewellery;
import java.util.Random;

public class ID_generator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generate(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        Random random = new Random();
        StringBuilder id = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            // Pick a random character from CHARACTERS
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            id.append(randomChar);
        }

        return id.toString();
    }
}
