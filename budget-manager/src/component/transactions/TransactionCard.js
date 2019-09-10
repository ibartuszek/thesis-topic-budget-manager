import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import MainCategoryForm from "./mainCategory/MainCategoryForm";
import SubCategoryForm from "./subCategory/SubCategoryForm";
import {createCardNames} from "../../actions/message/createElementIds";
import TransactionForm from "./TransactionForm";

class TransactionCard extends Component {

  render() {
    const {mainCategoryList, subCategoryList} = this.props;
    const cardData = createCardNames(this.props.data);

    console.log("render: TransactionCard");
    console.log(this.props);

    return (
      <div className="card">
        <CardHeader data={cardData}/>
        <MainCategoryForm transactionType={cardData.transactionType}
                          target={cardData.createMainType}
                          subCategoryList={subCategoryList}/>
        <SubCategoryForm transactionType={cardData.transactionType}
                         target={cardData.createSubType}/>
        <TransactionForm transactionType={cardData.transactionType}
                         target={cardData.createTransaction}
                         mainCategoryList={mainCategoryList}
                         subCategoryList={subCategoryList}/>
      </div>
    )
  }
}

export default TransactionCard;
