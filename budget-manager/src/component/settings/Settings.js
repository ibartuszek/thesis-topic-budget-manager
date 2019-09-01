import React from 'react';
import {Redirect} from 'react-router-dom';
import {connect} from "react-redux";
import UserSettings from "./UserSettings";

const Settings = (props) => {

  if (props.userHolder == null || !props.userHolder.userIsLoggedIn) {
    return <Redirect to='/login'/>;
  }

  return (
    <UserSettings/>
  )
};

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder
  }
};

export default connect(mapStateToProps)(Settings);
