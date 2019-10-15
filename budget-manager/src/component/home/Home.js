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
import {createGetCustomStatisticsMock, createGetStandardStatisticsMockResponse} from "../../actions/statistics/getStandardStatistics";
import CustomChart from "../statistics/CustomChart";

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
    let customStatistics = createGetCustomStatisticsMock();

    this.fetchUserData();
    return (
      <main>
        <StandardStatistics standardStatistics={response.body.standardStatistics}/>
        <div className="card card-body custom-chart-details-container my-3 mx-auto">
          <div className="clearfix my-3 mx-auto container">
            <h3 className="mx-auto">Main statistics</h3>
            <div className="row">
              <div className="col-12 col-lg-6 my-3 text-center">
                <CustomChart chartData={customStatistics.chartData} chartDetails={this.chartDetails}
                             schema={customStatistics.schema}/>
              </div>
            </div>
          </div>
        </div>
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
