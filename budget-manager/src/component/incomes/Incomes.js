import React, {Component} from 'react';
import {Redirect} from "react-router-dom";
import {connect} from 'react-redux';
import Loading from '../Loading'
import Transaction from "../transactions/Transaction";

class Incomes extends Component {

  data = {
    transactionType: 'INCOME',
    typeWithCapitalized: 'Income',
    typeLowerCase: 'income'
  };

  render() {
    const {categoryHolder, userHolder} = this.props;

    if (userHolder == null || !userHolder.userIsLoggedIn) {
      return <Redirect to='/login'/>;
    }

    let content = null;
    if (categoryHolder.incomeMainCategoriesAreLoaded && categoryHolder.incomeSubCategoriesAreLoaded) {
      const mainCategoryList = categoryHolder[this.data.transactionType.toLowerCase() + "MainCategories"];
      const subCategoryList = categoryHolder[this.data.transactionType.toLowerCase() + "SubCategories"];
      content = (
        <Transaction data={this.data} mainCategoryList={mainCategoryList} subCategoryList={subCategoryList}/>
      )
    } else {
      content = <Loading/>
    }
    return (
      <main>
        <div className="mt-3">
          {content}
        </div>
      </main>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    categoryHolder: state.categoryHolder,
    logHolder: state.logHolder,
    transactionHolder: state.transactionHolder,
    userHolder: state.userHolder,
  }
};

export default connect(mapStateToProps)(Incomes)
