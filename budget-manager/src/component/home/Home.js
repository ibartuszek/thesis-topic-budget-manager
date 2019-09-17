import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import DismissableAlert from "../DismissableAlert";
import Loading from '../Loading'
import {connect} from "react-redux";
import {removeMessage} from "../../actions/message/messageActions";
import {getAccessCookie} from "../../actions/user/cookie/getAccessCookie";
import {setAccessToken} from "../../actions/user/setAccessToken";
import {getUser} from "../../actions/user/getUser";

class Home extends Component {
  state = {
    loggedOut: false,
    email: {
      value: ''
    }
  };

  constructor(props) {
    super(props);
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

  componentDidUpdate(prevProps) {
    const {userIsLoggedIn} = this.props.userHolder;
    if (userIsLoggedIn !== prevProps.userHolder.userIsLoggedIn) {
      this.setState({
        loggedOut: !userIsLoggedIn
      });
    }
  }

  fetchUserData() {
    const {userHolder, logHolder} = this.props;
    const {email} = this.state;
    if (userHolder.jwtToken !== null && email.value !== '' && !userHolder.userIsLoggedIn) {
      this.props.getUser(this.state.email.value, userHolder.jwtToken, logHolder.messages);
    }
  }

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {messages} = this.props.logHolder;
    if (this.state.loggedOut) {
      return <Redirect to='/login'/>;
    }

    this.fetchUserData();

    const messageList = messages.slice(0).reverse().map((message, i) =>
      <DismissableAlert key={i} message={message} onChange={this.handleDismiss}/>);
    return (
      <React.Fragment>
        {messageList}
        <Loading/>
      </React.Fragment>
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
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message)),
    setAccessToken: (accessToken) => dispatch(setAccessToken(accessToken)),
    getUser: (email, jwtToken, messages) => dispatch(getUser(email, jwtToken, messages)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Home);
