import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import DismissableAlert from "../DismissableAlert";
import Loading from '../Loading'
import {connect} from "react-redux";
import {dateProperties} from "../../store/Properties";
import {removeMessage} from "../../actions/message/messageActions";
import moment from "moment";

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

  test() {
    let date = moment();
    console.log(date);
    console.log(dateProperties.dateFormat);
    console.log(moment(date).format(dateProperties.dateFormat));
  }

  render() {
    const {messages} = this.props.logHolder;
    if (this.state.loggedOut) {
      return <Redirect to='/login'/>;
    }

    this.test();

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
