import React, {Component} from 'react';
import ModelSelectValue from "../layout/form/ModelSelectValue";

class EnumSelect extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(newCurrency) {
    const {handleModelValueChange, id} = this.props;
    handleModelValueChange(id, newCurrency);
  }

  render() {
    const {model, id, label, placeHolder} = this.props;

    return (
      <React.Fragment>
        <ModelSelectValue onChange={this.handleChange}
                          id={id} model={model.value} value={model.value} elementList={model.possibleEnumValues}
                          labelTitle={label} placeHolder={placeHolder} type="text"/>
      </React.Fragment>)
  }

}

export default EnumSelect;
