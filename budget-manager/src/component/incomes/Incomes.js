import React, {Component} from 'react';
import {Redirect} from "react-router-dom";
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import Loading from '../Loading'
import Transaction from "../transactions/Transaction";
import {createContext} from "../../actions/common/createContext";
import {fetchMainCategories} from '../../actions/category/fetchMainCategories';
import {fetchSubCategories} from "../../actions/category/fetchSubCategories";


class Incomes extends Component {

  data = {
    transactionType: 'INCOME',
    typeWithCapitalized: 'Income',
    typeLowerCase: 'income'
  };

  state = {
    mainCategoriesAreLoaded: false,
    subCategoriesAreLoaded: false,
  };

  componentDidMount() {
    const {userHolder, logHolder} = this.props;
    if (userHolder.userIsLoggedIn) {
      let context = createContext(userHolder, logHolder);
      this.props.fetchMainCategories(context, this.data['transactionType']);
      this.props.fetchSubCategories(context, this.data['transactionType']);
    }
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    let mainCategoriesAreLoadedName = this.data['transactionType'].toLowerCase() + 'MainCategoriesAreLoaded';
    let subCategoriesAreLoadedName = this.data['transactionType'].toLowerCase() + 'SubCategoriesAreLoaded';

    if (!this.state.mainCategoriesAreLoaded && newProps.categoryHolder[mainCategoriesAreLoadedName]) {
      this.setState({
        ...this.state,
        mainCategoriesAreLoaded: true
      })
    }

    if (!this.state.subCategoriesAreLoaded && newProps.categoryHolder[subCategoriesAreLoadedName]) {
      this.setState({
        ...this.state,
        subCategoriesAreLoaded: true
      })
    }
  }

  render() {
    const {mainCategoriesAreLoaded, subCategoriesAreLoaded} = this.state;
    const {categoryHolder, userHolder} = this.props;

    if (userHolder == null || !userHolder.userIsLoggedIn) {
      return <Redirect to='/login'/>;
    }

    let content = null;
    if (mainCategoriesAreLoaded && subCategoriesAreLoaded) {
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

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {
        fetchMainCategories: fetchMainCategories,
        fetchSubCategories: fetchSubCategories,
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(Incomes)
