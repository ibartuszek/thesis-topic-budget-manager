import React from 'react'
import {NavLink} from 'react-router-dom'

const SignedOutLinks = () => {
  return (
    <div className="collapse navbar-collapse" id="mainNavbar">
      <ul className="navbar-nav mr-auto mt-2 mt-lg-0">
        <li className="nav-item mr-auto">
          <NavLink className="nav-item mr-auto nav-link" exact to="/">
            <span className="fa fa-home icon-home lnr lnr-home ion-ios-home-outline"/>
            <span> Home </span>
          </NavLink>
        </li>
      </ul>
      <ul className="navbar-nav ml-auto mt-2 mt-lg-0">
        <li className="nav-item">
          <NavLink className="nav-link" exact to="/signup">
            <span className="fas fa-user-plus"/>
            <span> Signup </span>
          </NavLink>
        </li>
        <li className="nav-item">
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
