import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import SubCategoryCard from "./subCategory/SubCategoryCard";
import TransactionForm from "./TransactionForm";
import {createCardNames} from "../../actions/message/createElementIds";
import MainCategoryCard from "./mainCategory/MainCategoryCard";

class TransactionCard extends Component {

  render() {
    const {mainCategoryList, subCategoryList} = this.props;
    const cardData = createCardNames(this.props.data);

    console.log("render: TransactionCard");
    console.log(this.props);

    return (
      <div className="card">
        <CardHeader data={cardData}/>
        <MainCategoryCard transactionType={cardData.transactionType}
                          target={cardData.createMainType}
                          subCategoryList={subCategoryList}/>
        <SubCategoryCard transactionType={cardData.transactionType}
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
