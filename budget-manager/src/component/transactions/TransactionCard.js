import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import {createCardNames} from "../../actions/message/createElementIds";
import MainCategoryForm from "./MainCategoryForm";

class TransactionCard extends Component {

  render() {
    const cardData = createCardNames(this.props.data);
    return (
      <div className="card">
        <CardHeader data={cardData}/>
        <MainCategoryForm transactionType={cardData.transactionType}
                          target={cardData.createMainType}
                          subCategoryListName={cardData.subCategorySetName}/>
      </div>
    )
  }
}

export default TransactionCard;
