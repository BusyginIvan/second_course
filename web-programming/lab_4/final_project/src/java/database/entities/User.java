package database.entities;

import com.sun.istack.internal.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor
public class User {
    @Id @NotNull private String username;
    @NotNull private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", referencedColumnName = "username")
    private List<Point> points;

    public User(String username, String password) {
        setUsername(username); setPassword(password);
        points = new ArrayList<>();
    }
}
