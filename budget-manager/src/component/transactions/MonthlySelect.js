import React, {Component} from 'react';
import ModelSelectValue from "../layout/form/ModelSelectValue";
import {transactionMessages} from "../../store/MessageHolder";

class MonthlySelect extends Component {

  data = {
    monthlyValues: ["Yes", "No"]
  };

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(monthlyValue) {
    let monthly = monthlyValue === this.data.monthlyValues[0];
    this.props.handleFieldChange("monthly", monthly);
  }

  render() {
    const {monthly} = this.props;
    const {monthlyValues} = this.data;
    const {transactionMonthlyLabel, transactionMonthlyMessage,} = transactionMessages;
    let monthlyValue = monthly ? monthlyValues[0] : monthlyValues[1];

    return (
      <React.Fragment>
        <ModelSelectValue onChange={this.handleChange}
                          id="monthly" model={monthlyValue} value={monthlyValue} elementList={monthlyValues}
                          labelTitle={transactionMonthlyLabel} placeHolder={transactionMonthlyMessage} type="text"/>
      </React.Fragment>)
  }

}

export default MonthlySelect;
