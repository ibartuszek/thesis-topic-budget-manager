import React from 'react';
import SignedInLinks from './SignedInLinks';
import SignedOutLinks from './SignedOutLinks';
import {connect} from "react-redux";

const Navbar = (props) => {
  const links = props.userHolder.userIsLoggedIn ? <SignedInLinks props={props}/> : <SignedOutLinks/>;
  return (
    <nav className="navbar navbar-expand-md navbar-light bg-light navbar-expand-md">
      <button aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation"
              className="navbar-toggler" data-target="#mainNavbar" data-toggle="collapse" type="button">
        <span className="navbar-toggler-icon"/>
      </button>
      {links}
    </nav>
  )
};

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder
  }
};

export default connect(mapStateToProps)(Navbar)
