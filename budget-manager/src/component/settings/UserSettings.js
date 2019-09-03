import React, {Component} from 'react';
import {connect} from "react-redux";
import ModelStringValue from "../layout/form/ModelStringValue";
import {updateUser} from "../../actions/user/updateUser";
import {validateUserModel} from "../../actions/validation/validateUserModel";

class UserSettings extends Component {

  state = {
    userModel: {
      id: null,
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
          errorMessage: errorMessage
        }
      }
    }))
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const {jwtToken, userData} = this.props.userHolder;
    const {userModel} = this.state;
    let password = userModel.password.value === '' ? '********' : userModel.password.value;
    if (validateUserModel(this.state.userModel, password)) {
      this.props.updateUser(userModel, userData['userId'], jwtToken);
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
    console.log(this.state);
  }

  render() {
    const {userHolder} = this.props;
    const {email, password, firstName, lastName} = this.state.userModel;

    return (
      <div className="mt-4 mx-3">
        <div className="card card-body mx-auto max-w-500 min-w-400">
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h1 className="mt-3 mx-auto">User data</h1>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="email" model={email}
                              labelTitle="Email" placeHolder="Please write your new email address." type="email"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="password" model={password}
                              labelTitle="Password" placeHolder="Please write your new password." type="password"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="firstName" model={firstName}
                              labelTitle="First name" placeHolder="Please write your new first name." type="text"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="lastName" model={lastName}
                              labelTitle="Last name" placeHolder="Please write your new last name." type="text"/>
            <div className="custom-error-message-container mt-3">
              {userHolder.updateUserErrorMessage !== null ? <p>{userHolder.updateUserErrorMessage}</p> : null}
            </div>
            <div className="custom-success-message-container mt-3">
              {userHolder.updateUserMessage !== null ? <p>{userHolder.updateUserMessage}</p> : null}
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
    userHolder: state.userHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    updateUser: (model, userId, jwtToken) => dispatch(updateUser(model, userId, jwtToken))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(UserSettings);
