import React, {Component} from 'react';

class AlertMessageComponent extends Component {

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  handleDismiss = (message) => {
    this.props.onChange(message);
  };

  render() {
    const {message} = this.props;
    let messageClassName = message.success ? "alert-success my-3" : "alert-danger my-2";
    let alertMessageComponent = message.value === null ? null
      : (<div className={"alert " + messageClassName} role="alert">
        {message.value}
        <button type="button" className="close" onClick={() => this.handleDismiss(message)}>
          <span>&times;</span>
        </button>
      </div>);

    return (
      <React.Fragment>
        {alertMessageComponent}
      </React.Fragment>
    )
  };

}

export default AlertMessageComponent;