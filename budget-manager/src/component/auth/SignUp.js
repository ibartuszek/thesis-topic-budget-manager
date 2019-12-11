import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import ModelStringValue from "../layout/form/ModelStringValue";
import YesNoSelect from "../layout/form/YesNoSelect";
import {createEmptyUser} from "../../actions/user/createUserMethods";
import {getAccessToken} from "../../actions/user/getAccessToken";
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import {registerUser} from "../../actions/user/registerUser";
import {validateModel} from "../../actions/validation/validateModel";
import {userFormMessages} from "../../store/MessageHolder"

class SignUp extends Component {

  state = {
    userModel: createEmptyUser()
  };

  constructor(props) {
    super(props);
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  handleModelValueChange(id, value, errorMessage) {
    this.setState(prevState => ({
      userModel: {
        ...prevState.userModel,
        [id]: {
          ...prevState.userModel[id],
          value: value,
          signUpErrorMessage: errorMessage
        }
      }
    }));
  }

  handleFieldChange(id, value) {
    this.setState(prevState => ({
      userModel: {
        ...prevState.userModel,
        [id]: value
      }
    }));
  }

  handleSubmit = (e) => {
    const {registerUser, logHolder} = this.props;
    const {userModel} = this.state;

    e.preventDefault();
    if (validateModel(userModel)) {
      registerUser(userModel, logHolder.messages);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {userHolder, logHolder} = this.props;
    const {email, password, confirmationPassword, firstName, lastName, tracking} = this.state.userModel;
    const {
      emailLabel, emailMessage, passwordLabel, passwordMessage, passwordConfirmMessage,
      firstNameLabel, firstNameMessage, lastNameLabel, lastNameMessage, trackingLabel, trackingMessage
    } = userFormMessages;

    if (userHolder.userData != null) {
      this.props.getAccessToken(email.value, password.value, logHolder.messages);
    }

    if (userHolder.jwtToken != null && userHolder.userIsLoggedIn) {
      return <Redirect to='/'/>;
    }

    return (
      <div className="mt-4 mx-3">
        <div className="card card-body mx-auto max-w-500 min-w-400">
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h1 className="mt-3 mx-auto">Sign Up</h1>
            <ModelStringValue onChange={this.handleModelValueChange}
                              id="email" model={email}
                              labelTitle={emailLabel} placeHolder={emailMessage} type="email"/>
            <ModelStringValue onChange={this.handleModelValueChange}
                              id="password" model={password}
                              labelTitle={passwordLabel} placeHolder={passwordMessage} type="password"/>
            <ModelStringValue onChange={this.handleModelValueChange}
                              id="confirmationPassword" model={confirmationPassword} passwordValue={password.value}
                              labelTitle={passwordLabel} placeHolder={passwordConfirmMessage} type="password"/>
            <ModelStringValue onChange={this.handleModelValueChange}
                              id="firstName" model={firstName}
                              labelTitle={firstNameLabel} placeHolder={firstNameMessage} type="text"/>
            <ModelStringValue onChange={this.handleModelValueChange}
                              id="lastName" model={lastName}
                              labelTitle={lastNameLabel} placeHolder={lastNameMessage} type="text"/>
            <YesNoSelect handleFieldChange={this.handleFieldChange} value={tracking} valueName="tracking"
                         valueLabel={trackingLabel} valueMessage={trackingMessage}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "signUpErrorMessage", false)} onChange={this.handleDismiss}/>
            <button className="btn btn-block btn-outline-success mt-3 mb-2">
              <span className="fas fa-user-plus"/>
              <span> Sign up </span>
            </button>
          </form>
        </div>
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder,
    logHolder: state.logHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    getAccessToken: (username, password, messages) => dispatch(getAccessToken(username, password, messages)),
    registerUser: (model, messages) => dispatch(registerUser(model, messages)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);
