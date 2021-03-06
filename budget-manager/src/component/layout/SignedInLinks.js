import React from 'react';
import {NavLink} from 'react-router-dom';
import {connect} from "react-redux";
import {logOut} from '../../actions/user/logOut';

const SignedInLinks = (props) => {
  const {logHolder, userHolder} = props;
  return (
    <div className="collapse navbar-collapse mt-1" id="mainNavbar">
      <ul className="navbar-nav mr-auto mt-lg-0">
        <li className="nav-item mx-3">
          <NavLink className="nav-link" exact to="/">
            <span className="fa fa-home icon-home lnr lnr-home ion-ios-home-outline"/>
            <span> Home </span>
          </NavLink>
        </li>
        <li className="nav-item mx-3">
          <NavLink className="nav-link" to="/incomes">
            <span className="fas fa-hand-holding-usd"/>
            <span> Incomes </span>
          </NavLink>
        </li>
        <li className="nav-item mx-3">
          <NavLink className="nav-link" to="/expenses">
            <span className="fas fa-money-bill-alt"/>
            <span> Expenses </span>
          </NavLink>
        </li>
        <li className="nav-item mx-3">
          <NavLink className="nav-link" to="/statistics-schema">
            <span className="fas fa-pencil-alt"/>
            <span> Schemas </span>
          </NavLink>
        </li>
        <li className="nav-item mx-3">
          <NavLink className="nav-link" to="/statistics">
            <span className="fas fa-balance-scale"/>
            <span> Statistics </span>
          </NavLink>
        </li>
        <li className="nav-item mx-3">
          <NavLink className="nav-link" to="/settings">
            <span className="fas fa-cog"/>
            <span> Settings </span>
          </NavLink>
        </li>
      </ul>
      <div className="nav-item logout-custom-colored ml-auto mr-3" onClick={() => {
        props.logOut(userHolder.userData.userId, userHolder.jwtToken, logHolder.messages);
      }}>
        <span className="fas fa-sign-out-alt"/>
        <span> Log Out </span>
      </div>
    </div>
  )
};

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder,
    logHolder: state.logHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    logOut: (userId, jwtToken, messages) => dispatch(logOut(userId, jwtToken, messages))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(SignedInLinks);
