import React from 'react'
import { NavLink } from 'react-router-dom'

const SignedInLinks = (props) => {
  return (
    <div className="collapse navbar-collapse" id="mainNavbar">
      <ul className="navbar-nav mr-auto mt-2 mt-lg-0">
        <li className="nav-item">
          <NavLink className="nav-link" exact to="/">
            <span className="fa fa-home icon-home lnr lnr-home ion-ios-home-outline"></span>
            <span> Home </span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink className="nav-link" to="/incomes">
            <span className="fas fa-hand-holding-usd"></span>
            <span> Incomes </span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink className="nav-link" to="/expenses">
            <span className="fas fa-money-bill-alt"></span>
            <span> Expenses </span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink className="nav-link" to="/draft">
            <span className="fas fa-pencil-alt"></span>
            <span> Draft </span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink className="nav-link" to="/reports">
            <span className="fas fa-balance-scale"></span>
            <span> Reports </span>
          </NavLink>
        </li>
        <li className="nav-item">
          <NavLink className="nav-link" to="/settings">
            <span className="fas fa-cog"></span>
            <span> Settings </span>
          </NavLink>
        </li>
      </ul>
      <div className="nav-item ml-auto" onClick={() => {props.signOut(props)}}>
        <span className="fas fa-sign-out-alt"></span>
        <span> Log Out </span>
      </div>
    </div>
  )
}
/*
const mapDispatchToProps = () => {
  return {
    signOut: (props) => {
      console.log("Sign Out", props);
    }
  }
}
*/

export default SignedInLinks;