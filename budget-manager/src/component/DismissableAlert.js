import React, {Component} from 'react';

class DismissableAlert extends Component {

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  handleDismiss = (message) => {
    this.props.onChange(message);
  };

  render() {
    let {message} = this.props;
    let className = message.success ?
      "alert alert-success alert-dismissible fade show mx-3 my-3" :
      "alert alert-danger alert-dismissible fade show mx-3 my-3";
    return (
      <div className={className} role="alert">
        {message.value}
        <button type="button" className="close" onClick={() => this.handleDismiss(message)}>
          <span>&times;</span>
        </button>
      </div>);
  }
}

export default DismissableAlert;
