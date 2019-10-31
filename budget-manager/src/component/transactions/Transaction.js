import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import MainCategoryCard from "./mainCategory/MainCategoryCard";
import SubCategoryCard from "./subCategory/SubCategoryCard";
import TransactionCard from "./transaction/TransactionCard";
import TransactionTable from "./transaction/table/TransactionTable";
import {createCardNames} from "../../actions/message/createElementIds";

class Transaction extends Component {

  render() {
    const {data, subCategoryList} = this.props;
    const cardData = createCardNames(data);

    return (
      <React.Fragment>
        <div className="card">
          <CardHeader data={cardData}/>
          <MainCategoryCard transactionType={cardData.transactionType}
                            target={cardData.createMainType}
                            subCategoryList={subCategoryList}/>
          <SubCategoryCard transactionType={cardData.transactionType}
                           target={cardData.createSubType}/>
          <TransactionCard transactionType={cardData.transactionType}
                           target={cardData.createTransaction}/>
        </div>
        <TransactionTable transactionType={cardData.transactionType}/>
      </React.Fragment>
    )
  }
}

export default Transaction
