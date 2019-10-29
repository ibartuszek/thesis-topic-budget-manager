import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import MainCategoryCard from "./mainCategory/MainCategoryCard";
import SubCategoryCard from "./subCategory/SubCategoryCard";
import TransactionCard from "./transaction/TransactionCard";
import {createCardNames} from "../../actions/message/createElementIds";
import TransactionTable from "./transaction/table/TransactionTable";

class Transaction extends Component {

  render() {
    const {data, mainCategoryList, subCategoryList} = this.props;
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
                           target={cardData.createTransaction}
                           mainCategoryList={mainCategoryList}
                           subCategoryList={subCategoryList}/>
        </div>
        <TransactionTable transactionType={cardData.transactionType}
                          mainCategoryList={mainCategoryList}
                          subCategoryList={subCategoryList}/>
      </React.Fragment>
    )
  }
}

export default Transaction
