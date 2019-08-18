package hu.elte.bm.authenticationservice.web.user;

public class UserModelRequestContext {

    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(final UserModel userModel) {
        this.userModel = userModel;
    }
}
