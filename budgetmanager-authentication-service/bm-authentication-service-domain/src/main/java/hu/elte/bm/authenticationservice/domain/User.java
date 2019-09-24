package hu.elte.bm.authenticationservice.domain;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = User.Builder.class)
public final class User {

    private static final int MINIMUM_EMAIL_LENGTH = 8;
    private static final int MAXIMUM_EMAIL_LENGTH = 50;
    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final int MINIMUM_PASSWORD_LENGTH = 8;
    private static final int MAXIMUM_PASSWORD_LENGTH = 16;
    private static final int MINIMUM_NAME_LENGTH = 2;
    private static final int MAXIMUM_NAME_LENGTH = 50;
    private final Long id;

    @NotEmpty(message = "Email cannot be empty!")
    @Length(min = MINIMUM_EMAIL_LENGTH, max = MAXIMUM_EMAIL_LENGTH, message = "Email must be between 8 and 50 characters!")
    @Pattern(regexp = EMAIL_REGEX, message = "Email must be given in a valid format!")
    private final String email;

    @NotEmpty(message = "Password cannot be empty!")
    @Length(min = MINIMUM_PASSWORD_LENGTH, max = MAXIMUM_PASSWORD_LENGTH, message = "Password must be between 8 and 16 characters!")
    private final String password;

    @NotEmpty(message = "First name cannot be empty!")
    @Length(min = MINIMUM_NAME_LENGTH, max = MAXIMUM_NAME_LENGTH, message = "First name must be between 2 and 50 characters!")
    private final String firstName;

    @NotEmpty(message = "Last name cannot be empty!")
    @Length(min = MINIMUM_NAME_LENGTH, max = MAXIMUM_NAME_LENGTH, message = "Last name must be between 2 and 50 characters!")
    private final String lastName;

    private User(final Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public static User createUserWithNewPassword(final User user, final String password) {
        return User.builder()
                .withId(user.getId())
                .withPassword(password)
                .withEmail(user.getEmail())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(email, user.email)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", email='" + email + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + '}';
    }

    public static final class Builder {
        private Long id;
        private String email;
        private String password;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public Builder withFirstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
