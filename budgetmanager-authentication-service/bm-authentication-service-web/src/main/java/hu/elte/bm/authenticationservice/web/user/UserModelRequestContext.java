package hu.elte.bm.authenticationservice.web.user;

public class UserModelRequestContext {

    private Long userId;
    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(final UserModel userModel) {
        this.userModel = userModel;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
