import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import Loading from '../Loading'
import LogContainer from "./LogContainer";
import {connect} from "react-redux";
import {getAccessCookie} from "../../actions/user/cookie/getAccessCookie";
import {getUser} from "../../actions/user/getUser";
import {removeMessage} from "../../actions/message/messageActions";
import {setAccessToken} from "../../actions/user/setAccessToken";
import {createContext} from "../../actions/common/createContext";
import {fetchSchemas} from "../../actions/schema/fetchSchemas";
import {fetchMainCategories} from "../../actions/category/fetchMainCategories";
import {fetchSubCategories} from "../../actions/category/fetchSubCategories";
import {getFirstPossibleDay} from "../../actions/transaction/getFirstPossibleDay";

class Home extends Component {
  state = {
    loggedOut: false,
    email: {
      value: ''
    }
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

  fetchAdditionalUserData() {
    const {categoryHolder, logHolder, statisticsHolder, transactionHolder, userHolder} = this.props;
    let context = createContext(userHolder, logHolder);
    if (!categoryHolder.incomeMainCategoriesAreLoaded) {
      this.props.fetchMainCategories(context, 'INCOME');
    }
    if (!categoryHolder.outcomeMainCategoriesAreLoaded) {
      this.props.fetchMainCategories(context, 'OUTCOME');
    }
    if (!categoryHolder.incomeSubCategoriesAreLoaded) {
      this.props.fetchSubCategories(context, 'INCOME');
    }
    if (!categoryHolder.outcomeSubCategoriesAreLoaded) {
      this.props.fetchSubCategories(context, 'OUTCOME');
    }
    if (!statisticsHolder.schemasAreLoaded) {
      this.props.getStatisticsSchemas(context);
    }
    if (transactionHolder.firstPossibleDay === null) {
      this.props.getFirstPossibleDay(context);
    }
  }

  render() {
    if (this.state.loggedOut) {
      return <Redirect to='/login'/>;
    }

    let logs = null;
    if (this.props.userHolder.userIsLoggedIn === true) {
      this.fetchAdditionalUserData();
      if (this.props.logHolder.messages.length > 0) {
        logs = <LogContainer/>
      }
    } else {
      this.fetchUserData();
    }

    return (
      <main>
        {logs}
        <Loading/>
      </main>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    categoryHolder: state.categoryHolder,
    logHolder: state.logHolder,
    statisticsHolder: state.statisticsHolder,
    transactionHolder: state.transactionHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message)),
    setAccessToken: (accessToken) => dispatch(setAccessToken(accessToken)),
    getUser: (email, jwtToken, messages) => dispatch(getUser(email, jwtToken, messages)),
    getStatisticsSchemas: (context) => dispatch(fetchSchemas(context)),
    fetchMainCategories: (context, transactionType) => dispatch(fetchMainCategories(context, transactionType)),
    fetchSubCategories: (context, transactionType) => dispatch(fetchSubCategories(context, transactionType)),
    getFirstPossibleDay: (context) => dispatch(getFirstPossibleDay(context))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Home);
