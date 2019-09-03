import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {connect} from "react-redux";
import ModelStringValue from "../layout/form/ModelStringValue";
import {getAccessToken} from "../../actions/user/getAccessToken";
import {registerUser} from "../../actions/user/registerUser";
import {validateUserModel} from "../../actions/validation/validateUserModel";

class SignUp extends Component {

  state = {
    userModel: {
      email: {
        value: '',
        errorMessage: null,
        minimumLength: 8,
        maximumLength: 50,
        regexp: "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$",
        possibleEnumValues: null
      },
      password: {
        value: '',
        errorMessage: null,
        minimumLength: 8,
        maximumLength: 16,
        regexp: null,
        possibleEnumValues: null
      },
      firstName: {
        value: '',
        errorMessage: null,
        minimumLength: 2,
        maximumLength: 50,
        regexp: null,
        possibleEnumValues: null
      },
      lastName: {
        value: '',
        errorMessage: null,
        minimumLength: 2,
        maximumLength: 50,
        regexp: null,
        possibleEnumValues: null
      },
    }
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
  }

  handleFieldChange(id, value, errorMessage) {
    this.setState(prevState => ({
      userModel: {
        ...prevState.userModel,
        [id]: {
          ...prevState.userModel[id],
          value: value,
          signUpErrorMessage: errorMessage
        }
      }
    }))
  }

  handleSubmit = (e) => {
    e.preventDefault();
    if (validateUserModel(this.state.userModel)) {
      this.props.registerUser(this.state.userModel);
    }
  };

  render() {
    const {userHolder} = this.props;
    const {email, password, firstName, lastName} = this.state.userModel;

    if (userHolder.userData != null) {
      this.props.getAccessToken(email.value, password.value);
    }

    if (userHolder.jwtToken != null && userHolder.userIsLoggedIn) {
      return <Redirect to='/'/>;
    }

    return (
      <div className="mt-4 mx-3">
        <div className="card card-body mx-auto max-w-500 min-w-400">
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h1 className="mt-3 mx-auto">Sign up</h1>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="email" model={email}
                              labelTitle="Email" placeHolder="Please write your email address." type="email"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="password" model={password}
                              labelTitle="Password" placeHolder="Please write your password." type="password"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="firstName" model={firstName}
                              labelTitle="First name" placeHolder="Please write your first name." type="text"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="lastName" model={lastName}
                              labelTitle="Last name" placeHolder="Please write your last name." type="text"/>
            <div className="custom-error-message-container mt-3">
              {userHolder.signUpErrorMessage !== null ? <p>{userHolder.signUpErrorMessage}</p> : null}
            </div>
            <button className="btn btn-block btn-outline-success mt-3 mb-2">
              <span className="fas fa-user-plus"/>
              <span> Register </span>
            </button>
          </form>
        </div>
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    getAccessToken: (username, password) => dispatch(getAccessToken(username, password)),
    registerUser: (model) => dispatch(registerUser(model))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);
