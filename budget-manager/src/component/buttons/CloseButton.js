import React, {Component} from 'react';

class CloseButton extends Component {

  render() {
    const {buttonLabel, closePopUp} = this.props;
    return (
      <button className="btn btn-outline-danger mx-3 mt-3 mb-2" onClick={closePopUp}>
        <span>&times;</span>
        <span> {buttonLabel} </span>
      </button>
    );
  }
}

export default CloseButton;
