import React, {Component} from 'react';
import {connect} from "react-redux";
import TransactionTableRow from "./TransactionTableRow";
import TransactionDeletePopUp from "../TransactionDeletePopUp";
import TransactionEditPopUp from "../TransactionEditPopUp";
import {sortTransactions} from "../../../../actions/transaction/sortTransactions";

class TransactionTableBody extends Component {

  state = {
    transactions: [],
    editableTransaction: null,
    deletableTransaction: null,
  };

  constructor(props) {
    super(props);
    this.showTransactionEdit = this.showTransactionEdit.bind(this);
  }

  showTransactionEdit = (transaction) => {
    let editableTransaction = transaction !== null ? transaction : null;
    this.setState({
      editableTransaction: editableTransaction,
    })
  };

  showTransactionDelete = (transaction) => {
    let deletableTransaction = transaction !== null ? transaction : null;
    this.setState({
      deletableTransaction: deletableTransaction,
    })
  };

  sortTable(field, transactions) {
    this.setState({
      transactions: sortTransactions(field, transactions)
    });
  }

  render() {
    const {transactionType, transactionHolder} = this.props;
    const {deletableTransaction, editableTransaction} = this.state;
    let data = transactionHolder[transactionType.toLowerCase() + 's'];

    let tableBody = data.map((transaction, index) =>
      <TransactionTableRow key={index} transaction={transaction} index={index}
                           showTransactionEdit={this.showTransactionEdit}
                           showTransactionDelete={this.showTransactionDelete}/>
    );

    let editTransaction = editableTransaction === null ? null : (
      <TransactionEditPopUp transactionModel={editableTransaction} transactionType={transactionType}
                            showTransactionEdit={this.showTransactionEdit}/>);

    let deleteTransaction = deletableTransaction === null ? null : (
      <TransactionDeletePopUp transactionModel={deletableTransaction} transactionType={transactionType}
                              showTransactionDelete={this.showTransactionDelete}/>
    );

    return (
      <div className="mx-auto table-responsive">
        <table className="table table-striped table-hover">
          <thead>
          <tr>
            <th className="remove-button-cell invisible">
              <button className="btn btn-warning btn-sm fas fa-edit"/>
            </th>
            <th className="custom-table-column">
              <span className="fas fa-sort" onClick={() => this.sortTable("title", data)}> Title </span>
            </th>
            <th className="custom-table-column">
              <span className="fas fa-sort" onClick={() => this.sortTable("amount", data)}> Amount </span>
            </th>
            <th className="custom-table-column">
              <span className="fas fa-sort" onClick={() => this.sortTable("currency", data)}> Currency </span>
            </th>
            <th className="custom-table-column">
              <span className="fas fa-sort" onClick={() => this.sortTable('mainCategory', data)}> Main type </span>
            </th>
            <th className="custom-table-column">
              <span> Subtype </span>
            </th>
            <th className="custom-table-column">
              <span className="fas fa-sort" onClick={() => this.sortTable('monthly', data)}> Monthly </span>
            </th>
            <th className="custom-table-column">
              <span className="fas fa-sort" onClick={() => this.sortTable('date', data)}> Date </span>
            </th>
            <th className="custom-table-column">
              <span> End date </span>
            </th>
            <th className="custom-table-column">
              <span> Description </span>
            </th>
            <th className="remove-button-cell invisible ml-3">
              <button className="btn btn-danger btn-sm fas fa-trash-alt"/>
            </th>
          </tr>
          </thead>
          <tbody>
          {tableBody}
          </tbody>
        </table>
        {editTransaction}
        {deleteTransaction}
      </div>
    );
  }

}

const mapStateToProps = (state) => {
  return {
    transactionHolder: state.transactionHolder,
  }
};

export default connect(mapStateToProps)(TransactionTableBody);
