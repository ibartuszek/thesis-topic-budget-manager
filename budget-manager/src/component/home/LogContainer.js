import React, {Component} from 'react';
import {connect} from "react-redux";

class LogContainer extends Component {

  render() {
    const {messages} = this.props.logHolder;
    let messageList = null;
    if (messages !== null) {
      messageList = messages.slice(0).reverse().map((message, i) =>
        <div className={(message.success ? "bg-info" : "bg-danger") + " text-light rounded-pill mx-2 my-1 p-1 row"} key={i}>
          <div className="mx-2 d-inline col-4 col-lg-2">{message.date}:</div>
          <div className="mx-2 d-inline col">{message.value}</div>
        </div>);
    }

    return (
      <div className="card card-body mt-3">
        <h4>User logs</h4>
        {messageList}
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder
  }
};

export default connect(mapStateToProps)(LogContainer);
