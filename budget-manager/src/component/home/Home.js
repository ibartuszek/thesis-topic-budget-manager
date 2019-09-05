import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import Loading from '../Loading'
import {connect} from "react-redux";
import DismissableAlert from "../DismissableAlert";
import {removeMessage} from "../../actions/message/messageActions";

class Home extends Component {
  state = {
    loggedOut: false
  };

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  componentDidUpdate(prevProps) {
    const {userIsLoggedIn} = this.props.userHolder;
    if (userIsLoggedIn !== prevProps.userHolder.userIsLoggedIn) {
      this.setState({
        loggedOut: !userIsLoggedIn
      });
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
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Home);
