import React, {Component} from 'react';

class DismissableAlert extends Component {

  constructor(props) {
    super(props);
    this.state = {
      showAlert: props.message != null,
      message: props.message
    };
    this.handleShow = this.handleShow.bind(this);
  }

  handleShow = (message, showAlert) => {
    this.setState(prevState => ({
      showAlert: prevState.message !== message ? true : !showAlert,
      message: message
    }))
  };

  render() {
    let {message, showAlert} = this.state;
    let className = this.props.success ?
      "alert alert-success alert-dismissible fade show mx-3 my-3" :
      "alert alert-danger alert-dismissible fade show mx-3 my-3";
    let alert =
      <div className={className} role="alert">
        {message}
        <button type="button" className="close" onClick={() => this.handleShow(message, showAlert)}>
          <span>&times;</span>
        </button>
      </div>;
    return showAlert ? alert : (null);
  }
}

export default DismissableAlert;
