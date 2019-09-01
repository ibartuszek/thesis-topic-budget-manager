import React from 'react';
import Loading from '../Loading'
import {connect} from "react-redux";

const Home = (props) => {
  const {userHolder} = props;

  const userDataMessage =
    userHolder.message === null ? null :
      <div className="alert alert-success alert-dismissible fade show mx-3 my-3" role="alert">
        {userHolder.message}
        <button type="button" className="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>;

  return (
    <React.Fragment>
      {userDataMessage}
      <Loading/>
    </React.Fragment>
  )
};

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder
  }
};

export default connect(mapStateToProps)(Home);
