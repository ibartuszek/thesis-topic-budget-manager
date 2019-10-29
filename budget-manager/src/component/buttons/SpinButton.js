import React, {Component} from 'react';

class SpinButton extends Component {

  render() {
    const {buttonLabel, icon, loading} = this.props;

    let buttonInner = loading ? (
      <React.Fragment>
        <div className="spinner-border spinner-border-sm" role="status">
          <span className="sr-only">Loading...</span>
        </div>
        <span> {buttonLabel} </span>
      </React.Fragment>
    ) : (
      <React.Fragment>
        <span className={icon}/>
        <span> {buttonLabel} </span>
      </React.Fragment>
    );

    return (
      <button className="btn btn-outline-success mt-3 mb-2">
        {buttonInner}
      </button>
    );
  }
}

export default SpinButton;
