import React from 'react';
import {NavLink} from 'react-router-dom';

const SignedOutLinks = () => {
  return (
    <div className="collapse navbar-collapse mt-1" id="mainNavbar">
      <ul className="navbar-nav mr-auto mt-lg-0">
        <li className="nav-item mx-3">
          <NavLink className="nav-item mr-auto nav-link" exact to="/">
            <span className="fa fa-home icon-home lnr lnr-home ion-ios-home-outline"/>
            <span> Home </span>
          </NavLink>
        </li>
      </ul>
      <ul className="navbar-nav mr-3 ml-auto mt-lg-0">
        <li className="nav-item mx-3">
          <NavLink className="nav-link" exact to="/register">
            <span className="fas fa-user-plus"/>
            <span> Register </span>
          </NavLink>
        </li>
        <li className="nav-item mx-3">
          <NavLink className="nav-link" exact to="/login">
            <span className="fas fa-sign-in-alt"/>
            <span> Login </span>
          </NavLink>
        </li>
      </ul>
    </div>
  )
};

export default SignedOutLinks;
