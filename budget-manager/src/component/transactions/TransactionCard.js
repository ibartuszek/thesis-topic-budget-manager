import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import MainCategoryForm from "./MainCategoryForm";
import SubCategoryForm from "./subCategory/SubCategoryForm";
import {createCardNames} from "../../actions/message/createElementIds";
import TransactionForm from "./TransactionForm";

class TransactionCard extends Component {

  render() {
    const cardData = createCardNames(this.props.data);
    return (
      <div className="card">
        <CardHeader data={cardData}/>
        <MainCategoryForm transactionType={cardData.transactionType}
                          target={cardData.createMainType}
                          subCategoryListName={cardData.subCategorySetName}/>
        <SubCategoryForm transactionType={cardData.transactionType}
                         target={cardData.createSubType}/>
        <TransactionForm transactionType={cardData.transactionType}
                         target={cardData.createTransaction}/>
      </div>
    )
  }
}

export default TransactionCard;
