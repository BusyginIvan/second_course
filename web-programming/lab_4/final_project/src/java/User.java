import com.sun.istack.internal.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor
public class User {
    @Id @NotNull private String login;
    @NotNull private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner_login", referencedColumnName = "login")
    private List<Point> points;

    public User(String login, String password) {
        setLogin(login); setPassword(password);
        points = new ArrayList<>();
    }
}
