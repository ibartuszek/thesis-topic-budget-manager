package hu.elte.bm.authenticationservice.web.user;

import hu.elte.bm.authenticationservice.web.common.ResponseModel;

public class UserModelResponse extends ResponseModel {

    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(final UserModel userModel) {
        this.userModel = userModel;
    }
}
