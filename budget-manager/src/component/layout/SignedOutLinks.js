import React from 'react'
import { NavLink } from 'react-router-dom'

const SignedOutLinks = (props) => {
  return (
    <div className="collapse navbar-collapse" id="mainNavbar">
      <ul className="navbar-nav ml-auto mt-2 mt-lg-0">
        <li className="nav-item">
          <NavLink className="nav-link" exact to="/signup">
            <span className="fas fa-user-plus"></span>
            <span> Signup </span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink className="nav-link" exact to="/login">
            <span className="fas fa-sign-in-alt"></span>
            <span> Login </span>
          </NavLink>
        </li>
      </ul>
    </div>
  )
}

export default SignedOutLinks;