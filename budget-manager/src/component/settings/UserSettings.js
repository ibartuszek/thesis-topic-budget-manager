import React, {Component} from 'react';
import {connect} from "react-redux";
import ModelStringValue from "../layout/form/ModelStringValue";
import {updateUser} from "../../actions/user/updateUser";
import {validateModel} from "../../actions/validation/validateModel";
import {createContext} from "../../actions/common/createContext";
import {userFormMessages} from "../../store/MessageHolder";

class UserSettings extends Component {

  state = {
    userModel: {
      id: null,
      email: {
        value: '',
        errorMessage: null,
        minimumLength: 8,
        maximumLength: 50,
        regexp: "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"
      },
      password: {
        value: '',
        errorMessage: null,
        minimumLength: 8,
        maximumLength: 16
      },
      confirmationPassword: {
        value: '',
        errorMessage: null,
        minimumLength: 8,
        maximumLength: 16
      },
      firstName: {
        value: '',
        errorMessage: null,
        minimumLength: 2,
        maximumLength: 50
      },
      lastName: {
        value: '',
        errorMessage: null,
        minimumLength: 2,
        maximumLength: 50
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
          errorMessage: errorMessage
        }
      }
    }))
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const {userHolder, logHolder} = this.props;
    const {userModel} = this.state;
    let password = userModel.password.value === '' ? '********' : userModel.password.value;
    if (validateModel(userModel, password)) {
      let context = createContext(userHolder, logHolder);
      this.props.updateUser(context, userModel);
    }
  };

  componentDidMount() {
    const {userData} = this.props.userHolder;
    let userModel = {...this.state.userModel};
    userModel.id = userData.userId;
    userModel.email.value = userData.userName;
    userModel.firstName.value = userData.firstName;
    userModel.lastName.value = userData.lastName;
    this.setState({
      ...this.state,
      userModel
    });
  }

  render() {
    const {userHolder} = this.props;
    const {email, password, confirmationPassword, firstName, lastName} = this.state.userModel;
    const {
      emailLabel, emailMessage, passwordLabel, passwordMessageToChange, passwordConfirmMessage,
      firstNameLabel, firstNameMessage, lastNameLabel, lastNameMessage
    } = userFormMessages;

    return (
      <div className="mt-4 mx-3">
        <div className="card card-body mx-auto max-w-500 min-w-400">
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h1 className="mt-3 mx-auto">User data</h1>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="email" model={email}
                              labelTitle={emailLabel} placeHolder={emailMessage} type="email"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="password" model={password}
                              labelTitle={passwordLabel} placeHolder={passwordMessageToChange} type="password"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="confirmationPassword" model={confirmationPassword} passwordValue={password.value}
                              labelTitle={passwordLabel} placeHolder={passwordConfirmMessage} type="password"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="firstName" model={firstName}
                              labelTitle={firstNameLabel} placeHolder={firstNameMessage} type="text"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="lastName" model={lastName}
                              labelTitle={lastNameLabel} placeHolder={lastNameMessage} type="text"/>
            <div className="custom-error-message-container mt-3">
              {userHolder.messages.updateUserErrorMessage !== null ? <p>{userHolder.messages.updateUserErrorMessage}</p> : null}
            </div>
            <div className="custom-success-message-container mt-3">
              {userHolder.messages.updateUserMessage !== null ? <p>{userHolder.messages.updateUserMessage}</p> : null}
            </div>
            <button className="btn btn-block btn-outline-success mt-3 mb-2">
              <span className="fas fa-pencil-alt"/>
              <span> Update </span>
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
    updateUser: (context, model) => dispatch(updateUser(context, model))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(UserSettings);
