package hu.elte.bm.authenticationservice.web.user;

import hu.elte.bm.commonpack.validator.ModelStringValue;

public final class UserModel {

    private Long id;
    private ModelStringValue email;
    private ModelStringValue password;
    private ModelStringValue firstName;
    private ModelStringValue lastName;

    private UserModel() {
    }

    private UserModel(final Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ModelStringValue getEmail() {
        return email;
    }

    public void setEmail(final ModelStringValue email) {
        this.email = email;
    }

    public ModelStringValue getPassword() {
        return password;
    }

    public void setPassword(final ModelStringValue password) {
        this.password = password;
    }

    public ModelStringValue getFirstName() {
        return firstName;
    }

    public void setFirstName(final ModelStringValue firstName) {
        this.firstName = firstName;
    }

    public ModelStringValue getLastName() {
        return lastName;
    }

    public void setLastName(final ModelStringValue lastName) {
        this.lastName = lastName;
    }

    public static final class Builder {

        private Long id;
        private ModelStringValue email;
        private ModelStringValue password;
        private ModelStringValue firstName;
        private ModelStringValue lastName;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(final ModelStringValue email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(final ModelStringValue password) {
            this.password = password;
            return this;
        }

        public Builder withFirstName(final ModelStringValue firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(final ModelStringValue lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }

    }
}
