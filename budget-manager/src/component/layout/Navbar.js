import React from 'react';
import SignedInLinks from './SignedInLinks';
import SignedOutLinks from './SignedOutLinks';

const Navbar = (props) => {
  // const { auth, profile } = props;
  // const links = auth.uid ? <SignedInLinks profile={profile}/> : <SignedOutLinks />
  return (
    <nav className="navbar navbar-expand-md navbar-light bg-light navbar-expand-md">
      <button aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation" 
        className="navbar-toggler" data-target="#mainNavbar" data-toggle="collapse" type="button">
        <span className="navbar-toggler-icon"></span>
      </button>
      <SignedInLinks />
      <SignedOutLinks />
    </nav>
  )
}

export default Navbar;