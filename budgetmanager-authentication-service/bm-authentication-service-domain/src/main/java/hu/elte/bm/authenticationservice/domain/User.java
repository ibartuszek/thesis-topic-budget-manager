package hu.elte.bm.authenticationservice.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = User.Builder.class)
public final class User {

    private final Long id;

    @NotNull(message = "Email is compulsory")
    // @NotBlank(message = "Email is compulsory")
    @Length(min = 8, max = 50, message = "Email must be between 8 and 50 characters!")
    @Pattern(regexp = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", message = "Email must be given in a valid format!")
    private final String email;

    @Length(min = 8, max = 16, message = "Password must be between 8 and 16 characters!")
    private final String password;

    @Length(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    private final String firstName;

    @Length(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
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
            User result = new User(this);
            // validate(result);
            return result;
        }
/*
        private void validate(final User result) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<User>> violations = validator.validate(result);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(
                    new HashSet<ConstraintViolation<?>>(violations));
            }
        }
*/
    }
}
