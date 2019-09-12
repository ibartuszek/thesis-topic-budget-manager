import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import MainCategoryCard from "./mainCategory/MainCategoryCard";
import SubCategoryCard from "./subCategory/SubCategoryCard";
import {createCardNames} from "../../actions/message/createElementIds";
import TransactionCard from "./transaction/TransactionCard";

class Transaction extends Component {

  render() {
    const {mainCategoryList, subCategoryList} = this.props;
    const cardData = createCardNames(this.props.data);

    console.log("render: Transaction");
    console.log(this.props);

    return (
      <div className="card">
        <CardHeader data={cardData}/>
        <MainCategoryCard transactionType={cardData.transactionType}
                          target={cardData.createMainType}
                          subCategoryList={subCategoryList}/>
        <SubCategoryCard transactionType={cardData.transactionType}
                         target={cardData.createSubType}/>
        <TransactionCard transactionType={cardData.transactionType}
                         target={cardData.createTransaction}
                         mainCategoryList={mainCategoryList}
                         subCategoryList={subCategoryList}/>
      </div>
    )
  }
}

export default Transaction;
