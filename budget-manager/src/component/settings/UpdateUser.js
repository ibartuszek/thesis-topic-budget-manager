import React, {Component} from 'react';
import {connect} from "react-redux";
import ModelStringValue from "../layout/form/ModelStringValue";
import {updateUser} from "../../actions/user/updateUser";
import {validateModel} from "../../actions/validation/validateModel";
import {createContext} from "../../actions/common/createContext";
import {userFormMessages} from "../../store/MessageHolder";
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import AlertMessageComponent from "../AlertMessageComponent";
import {createUserEmptyUser} from "../../actions/user/createUser";

class UpdateUser extends Component {

  state = {
    userModel: createUserEmptyUser()
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  componentDidMount() {
    const {userData} = this.props.userHolder;
    let userModel = userData;
    this.setState({
      ...this.state,
      userModel
    });
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
    const {userHolder, logHolder, updateUser} = this.props;
    const {userModel} = this.state;
    let password = userModel.password.value === '' ? '********' : userModel.password.value;
    if (validateModel(userModel, password)) {
      let context = createContext(userHolder, logHolder);
      updateUser(context, userModel);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {logHolder} = this.props;
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
            <AlertMessageComponent message={getMessage(logHolder.messages, "updateUserMessage", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "updateUserErrorMessage", false)} onChange={this.handleDismiss}/>
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
    updateUser: (context, model) => dispatch(updateUser(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(UpdateUser);
