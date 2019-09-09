import React, {Component} from 'react';
import {validateModelAmountValue} from "../../../actions/validation/modelAmountValueValidation";

class ModelAmountValue extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e, errorMessage) => {
    this.props.onChange([e.target.id], e.target.value, errorMessage);
  };

  render() {
    const {id, model, labelTitle, placeHolder, type} = this.props;
    let errorMessage = validateModelAmountValue(model, labelTitle);
    return (
      <React.Fragment>
        <div className="input-group mt-3">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <input className="form-control" id={id} placeholder={placeHolder}
                 onChange={(e) => this.handleChange(e, errorMessage)} value={model.value} type={type}/>
        </div>
        <div className="custom-error-message-container mt-2">{errorMessage}</div>
      </React.Fragment>
    )
  }
}

export default ModelAmountValue;
