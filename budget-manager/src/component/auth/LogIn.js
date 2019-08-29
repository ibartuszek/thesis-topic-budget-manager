import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {connect} from 'react-redux';
import {getAccessToken} from '../../actions/user/getAccessToken';
import {getUser} from '../../actions/user/getUser';
import ModelStringValue from "../layout/form/ModelStringValue";

class LogIn extends Component {
  state = {
    username: '',
    password: '',
    error: null
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
  }

  handleFieldChange(id, value) {
    this.setState({
      [id]: value
    });
  }

  handleSubmit = (e) => {
    e.preventDefault();
    let body = {
      username: this.state.username,
      password: this.state.password
    };
    this.props.getAccessToken(body);
  };

  render() {
    const {userHolder, getUser} = this.props;
    const {username, password} = this.state;

    if (userHolder.jwtToken != null && userHolder.userData == null) {
      getUser(this.state.username, userHolder.jwtToken);
    }

    if (userHolder.userIsLoggedIn) {
      return <Redirect to='/'/>;
    }

    return (
      <div className="mt-4 mx-3">
        <div className="card card-body mx-auto max-w-500 min-w-400">
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h1 className="mt-3 mx-auto">Sign in</h1>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="username" value={username}
                              labelTitle="Email" placeHolder="Please write your email address." type="email"/>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="password" value={password}
                              labelTitle="Password" type="password"/>
            <div className="custom-error-message-container mt-3">
              {userHolder.logInErrorMessage !== null ? <p>{userHolder.logInErrorMessage}</p> : null}
            </div>
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
    userHolder: state.userHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    getAccessToken: (body) => dispatch(getAccessToken(body)),
    getUser: (username, jwtToken) => dispatch(getUser(username, jwtToken))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(LogIn)
