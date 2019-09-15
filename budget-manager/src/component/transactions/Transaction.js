import React, {Component} from 'react';
import CardHeader from "../layout/card/CardHeader";
import MainCategoryCard from "./mainCategory/MainCategoryCard";
import SubCategoryCard from "./subCategory/SubCategoryCard";
import TransactionCard from "./transaction/TransactionCard";
import {createCardNames} from "../../actions/message/createElementIds";
import TransactionTable from "./transaction/table/TransactionTable";

class Transaction extends Component {
  /*
    state = {
      transactionsAreLoaded: false
    };

    componentDidUpdate(oldProps) {
      const {data} = this.props;
      const newProps = this.props;

      let transactionsName = data['transactionType'].toLowerCase() + 's';
      let transactionsAreLoadedName = data['transactionType'].toLowerCase() + 'sAreLoaded';

      if (!newProps.transactionHolder[transactionsAreLoadedName] && Object.keys(newProps.transactionHolder[transactionsName]).length > 0) {
        // TODO:
        // this.props.setMainCategoriesToReady(this.data['transactionType']);
      }
      if (!this.state.transactionsAreLoaded && newProps.transactionHolder[transactionsAreLoadedName]) {
        this.setState({
          ...this.state,
          transactionsAreLoaded: true
        })
      }
    }

    handleSubmit = (e) => {
      e.preventDefault();
      console.log("Transaction handle submit");
      // this.props.handleSubmit(this.state.transactionModel);
    };
  */
  render() {
    const {data, mainCategoryList, subCategoryList} = this.props;
    const cardData = createCardNames(data);

    console.log("render: Transaction");
    console.log(this.props);

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

/*
const mapStateToProps = (state) => {
  return {
    transactionHolder: state.transactionHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {
        fetchMainCategories: fetchMainCategories,
        setMainCategoriesToReady: setMainCategoriesToReady,
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(Transaction)
*/
export default Transaction
