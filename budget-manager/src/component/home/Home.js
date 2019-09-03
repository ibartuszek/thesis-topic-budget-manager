import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import Loading from '../Loading'
import {connect} from "react-redux";
import DismissableAlert from '../DismissableAlert'

class Home extends Component {
  state = {
    loggedOut: false
  };

  componentDidUpdate(prevProps) {
    const {userIsLoggedIn} = this.props.userHolder;
    if (userIsLoggedIn !== prevProps.userHolder.userIsLoggedIn) {
      this.setState({
        loggedOut: !userIsLoggedIn
      });
    }
  }

  render() {
    const {userHolder} = this.props;
    if (this.state.loggedOut) {
      return <Redirect to='/login'/>;
    }

    return (
      <React.Fragment>
        <DismissableAlert message={userHolder.logInMessage} success={true}/>
        <DismissableAlert message={userHolder.logInErrorMessage} success={false}/>
        <DismissableAlert message={userHolder.logOutMessage} success={true}/>
        <DismissableAlert message={userHolder.logOutErrorMessage} success={false}/>
        <DismissableAlert message={userHolder.signUpMessage} success={true}/>
        <DismissableAlert message={userHolder.signUpErrorMessage} success={false}/>
        <DismissableAlert message={userHolder.updateUserMessage} success={true}/>
        <DismissableAlert message={userHolder.updateUserErrorMessage} success={false}/>
        <Loading/>
      </React.Fragment>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder
  }
};

export default connect(mapStateToProps)(Home);
