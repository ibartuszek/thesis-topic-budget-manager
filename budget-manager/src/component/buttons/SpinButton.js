import React, {Component} from 'react';

class SpinButton extends Component {

  render() {
    const {altButtonStyle, buttonLabel, icon, handleSubmit, loading} = this.props;
    let buttonClass = altButtonStyle !== undefined ? altButtonStyle : "btn btn-outline-success mt-3 mb-2";

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

    return handleSubmit === undefined
      ? (<button className={buttonClass}>
        {buttonInner}
      </button>)
      : (<button className={buttonClass} onClick={handleSubmit}>
        {buttonInner}
      </button>);
  }
}

export default SpinButton;
