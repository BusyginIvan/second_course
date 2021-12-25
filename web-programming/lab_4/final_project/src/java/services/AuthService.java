package services;

import database.repositories.UserRepository;
import database.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Stateless
public class AuthService {
    @EJB
    private UserRepository userRepository;
    @EJB
    private TokenService tokenService;

    public AuthResult login(@NotNull String username, @NotNull String password)
            throws NoSuchAlgorithmException {
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPassword().equals(encrypt(password)))
                return AuthResult.token(tokenService.generate(username));
            else
                return AuthResult.message("Неверный пароль.");
        } else
            return AuthResult.message("Не существует пользователя с таким именем.");
    }

    public AuthResult register(@NotNull String username, @NotNull String password)
            throws NoSuchAlgorithmException {
        if (userRepository.exists(username)) {
            return AuthResult.message("Пользователь с таким именем уже существует.");
        } else {
            userRepository.save(new User(username, encrypt(password)));
            return AuthResult.token(tokenService.generate(username));
        }
    }

    public Optional<String> getUsernameByToken(String token) {
        return tokenService.verify(token);
    }

    private String encrypt(String str) throws NoSuchAlgorithmException {
        try {
            StringBuilder sb = new StringBuilder();
            byte[] bytes = MessageDigest.getInstance("SHA-1").digest(str.getBytes());
            for (byte b : bytes) {
                sb.append(Integer.toHexString((b >> 4) & 15));
                sb.append(Integer.toHexString(b & 15));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("Ошибка сервера при попытке зашифровать пароль.");
        }
    }
}
