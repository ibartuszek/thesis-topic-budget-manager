import React, {Component} from 'react';
import {findElementById} from "../../../../actions/common/listActions";

class TransactionTableRow extends Component {

  constructor(props) {
    super(props);
    this.showTransactionEdit = this.showTransactionEdit.bind(this);
  }

  showTransactionEdit(transaction) {
    this.props.showTransactionEdit(transaction);
  }

  showTransactionDelete(transaction) {
    this.props.showTransactionDelete(transaction);
  }

  render() {
    const {transactionList, id} = this.props;
    const transaction = findElementById(transactionList, id);
    const {amount, currency, date, description, endDate, locked, mainCategory, monthly, subCategory, title,} = transaction;

    const editButton = locked ? null : (
      <button className="btn btn-warning btn-sm fas fa-edit" onClick={() => this.showTransactionEdit(transaction)}/>);
    const deleteButton = locked ? null : (
      <button className="btn btn-danger btn-sm fas fa-trash-alt" onClick={() => this.showTransactionDelete(transaction)}/>);
    return (
      <tr>
        <td className="remove-button-cell">{editButton}</td>
        <td>{title.value}</td>
        <td>{amount.value}</td>
        <td>{currency.value}</td>
        <td>{mainCategory.name.value}</td>
        <td>{subCategory !== undefined && subCategory !== null ? subCategory.name.value : ''}</td>
        <td>{monthly ? "Yes" : "No"}</td>
        <td>{date.value}</td>
        <td>{endDate !== undefined && endDate !== null ? endDate.value : ''}</td>
        <td>{description !== undefined && description !== null ? description.value : ''}</td>
        <td className="remove-button-cell ml-3">{deleteButton}</td>
      </tr>
    );
  }

}

export default TransactionTableRow;
