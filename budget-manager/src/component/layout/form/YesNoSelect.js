import React, {Component} from 'react';
import ModelSelectValue from "./ModelSelectValue";

class YesNoSelect extends Component {

  data = {
    values: ["Yes", "No"]
  };

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(valueText) {
    let {valueName} = this.props;
    let value = valueText === this.data.values[0];
    this.props.handleFieldChange(valueName, value);
  }

  render() {
    const {value, valueName, valueLabel, valueMessage} = this.props;
    const {values} = this.data;
    let valueText = value ? values[0] : values[1];

    return (
      <React.Fragment>
        <ModelSelectValue onChange={this.handleChange}
                          id={valueName} model={valueText} value={valueText} elementList={values}
                          labelTitle={valueLabel} placeHolder={valueMessage} type="text"/>
      </React.Fragment>)
  }

}

export default YesNoSelect;
