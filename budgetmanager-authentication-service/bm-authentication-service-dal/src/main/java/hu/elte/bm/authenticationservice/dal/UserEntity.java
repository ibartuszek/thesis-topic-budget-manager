package hu.elte.bm.authenticationservice.dal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public final class UserEntity {

    private static final int EMAIL_LENGTH = 50;
    private static final int PASSWORD_LENGTH = 500;
    private static final int FIRST_NAME_LENGTH = 50;
    private static final int LAST_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = EMAIL_LENGTH, nullable = false, unique = true)
    private String email;

    @Column(length = PASSWORD_LENGTH, nullable = false)
    private String password;

    @Column(length = FIRST_NAME_LENGTH, nullable = false)
    private String firstName;

    @Column(length = LAST_NAME_LENGTH, nullable = false)
    private String lastName;

    private boolean tracking;

    UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(final boolean tracking) {
        this.tracking = tracking;
    }
}
