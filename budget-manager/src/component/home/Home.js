import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import Loading from '../Loading'
import LogContainer from "./LogContainer";
import {connect} from "react-redux";
import {getAccessCookie} from "../../actions/user/cookie/getAccessCookie";
import {getUser} from "../../actions/user/getUser";
import {removeMessage} from "../../actions/message/messageActions";
import {setAccessToken} from "../../actions/user/setAccessToken";
import StandardStatistics from "../statistics/standardStatistics/StandardStatistics";
import {createGetStandardStatisticsMockResponse} from "../../actions/statistics/getStandardStatistics";
import CustomStatistics from "../statistics/customStatistics/CustomStatistics";
import {createGetCustomScaleStatisticsMock, createGetCustomSumStatisticsMock} from "../../actions/statistics/getCustomStatistics";

class Home extends Component {
  state = {
    loggedOut: false,
    email: {
      value: ''
    }
  };

  chartDetails = {
    height: 350,
    width: 400,
  };

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

  render() {
    if (this.state.loggedOut) {
      return <Redirect to='/login'/>;
    }

    let logs = null;
    if (this.props.userHolder.userIsLoggedIn === true && this.props.logHolder.messages.length > 0) {
      logs = <LogContainer/>
    }

    // TODO: delete:
    let response = createGetStandardStatisticsMockResponse();
    let exampleScale = createGetCustomScaleStatisticsMock();
    let exampleSum = createGetCustomSumStatisticsMock();

    this.fetchUserData();
    return (
      <main>
        <StandardStatistics standardStatistics={response.body.standardStatistics}/>
        <CustomStatistics customStatistics={exampleScale.customStatistics}/>
        <CustomStatistics customStatistics={exampleSum.customStatistics}/>
        {logs}
        <Loading/>
      </main>
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
