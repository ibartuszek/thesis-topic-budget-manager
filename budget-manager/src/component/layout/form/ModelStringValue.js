import React, {Component} from 'react';
import {validateModelStringValue} from "../../../actions/validation/modelStringValueValidation";

class ModelStringValue extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e, errorMessage) => {
    this.props.onChange([e.target.id], e.target.value, errorMessage);
  };

  render() {
    const {disabled, id, model, labelTitle, placeHolder, type, passwordValue} = this.props;
    let errorMessage = validateModelStringValue(model, labelTitle, passwordValue);
    let locked = disabled !== undefined ? disabled : false;
    return (
      <React.Fragment>
        <div className="input-group mt-3">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <input className="form-control" id={id} placeholder={placeHolder} disabled={locked}
                 onChange={(e) => this.handleChange(e, errorMessage)} value={model.value} type={type}/>
        </div>
        <div className="custom-error-message-container mt-2">{errorMessage}</div>
      </React.Fragment>
    )
  }
}

export default ModelStringValue;
