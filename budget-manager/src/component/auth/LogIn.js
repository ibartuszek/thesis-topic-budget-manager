import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {connect} from 'react-redux';
import ModelStringValue from "../layout/form/ModelStringValue";
import {getAccessToken} from '../../actions/user/getAccessToken';
import {getUser} from '../../actions/user/getUser';
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import AlertMessageComponent from "../AlertMessageComponent";
import {getAccessCookie} from "../../actions/user/cookie/getAccessCookie";
import {setAccessToken} from "../../actions/user/setAccessToken";
import {getStatisticsSchemas} from "../../actions/statistics/getStatisticsSchemas";
import {createContext} from "../../actions/common/createContext";

class LogIn extends Component {
  state = {
    email: {
      value: ''
    },
    password: {
      value: ''
    }
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  componentDidMount() {
    const cookie = getAccessCookie();
    if (cookie !== undefined && cookie !== null) {
      this.setState({
        email: {
          value: cookie['userName']
        }
      });
      this.props.setAccessToken(cookie['jwtToken']);
    }
  }

  fetchUserData() {
    const {userHolder, logHolder} = this.props;
    const {email} = this.state;
    if (userHolder.jwtToken !== null && email.value !== '' && !userHolder.userIsLoggedIn) {
      this.props.getUser(this.state.email.value, userHolder.jwtToken, logHolder.messages);
    }
  }

  handleFieldChange(id, value, errorMessage) {
    this.setState(prevState => ({
      [id]: {
        ...prevState[id],
        value: value,
        errorMessage: errorMessage
      }
    }))
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const {email, password} = this.state;
    const {messages} = this.props.logHolder;
    this.props.getAccessToken(email.value, password.value, messages);
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  getStatisticsSchemas(context) {
    this.props.getStatisticsSchemas(context);
  }

  render() {
    const {userHolder, logHolder} = this.props;
    const {email, password} = this.state;

    if (userHolder.userIsLoggedIn) {
      let context = createContext(userHolder, logHolder);
      this.getStatisticsSchemas(context);
      return <Redirect to='/'/>;
    }

    this.fetchUserData();

    return (
      <div className="mt-4 mx-3">
        <div className="card card-body mx-auto max-w-500 min-w-400">
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h1 className="mt-3 mx-auto">Sign in</h1>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="email" model={email}
                              labelTitle="Email" type="email"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="password" model={password}
                              labelTitle="Password" type="password"/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "logInErrorMessage", false)} onChange={this.handleDismiss}/>
            <button className="btn btn-block btn-outline-success mt-3 mb-2">
              <span className="fas fa-sign-in-alt"/>
              <span> Login </span>
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
    setAccessToken: (accessToken) => dispatch(setAccessToken(accessToken)),
    getUser: (email, jwtToken, messages) => dispatch(getUser(email, jwtToken, messages)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message)),
    getStatisticsSchemas: (context) => dispatch(getStatisticsSchemas(context))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(LogIn)
