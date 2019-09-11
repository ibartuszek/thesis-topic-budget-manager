import React, {Component} from 'react';
import ModelSelectValue from "../layout/form/ModelSelectValue";
import {transactionMessages} from "../../store/MessageHolder";

class CurrencySelect extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(newCurrency) {
    this.props.handleModelValueChange("currency", newCurrency);
  }

  render() {
    const {currency} = this.props;
    const {transactionCurrencyLabel, transactionCurrencyMessage,} = transactionMessages;

    return (
      <React.Fragment>
        <ModelSelectValue onChange={this.handleChange}
                          id="currency" model={currency.value} value={currency.value} elementList={currency.possibleEnumValues}
                          labelTitle={transactionCurrencyLabel} placeHolder={transactionCurrencyMessage} type="text"/>
      </React.Fragment>)
  }

}

export default CurrencySelect;
