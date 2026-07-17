package springboot.Pure.Goat.Farms;

public class en_de {
    
    public static String encrypt(String password) {
        // Add your encryption logic here
        // For example, basic Base64 encoding:
        try {
            return java.util.Base64.getEncoder().encodeToString(password.getBytes());
        } catch (Exception e) {
            return password;
        }
    }
    
    public static String decrypt(String encryptedPassword) {
        // Add your decryption logic here
        // For example, basic Base64 decoding:
        try {
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(encryptedPassword);
            return new String(decodedBytes);
        } catch (Exception e) {
            return encryptedPassword;
        }
    }
}