import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import Loading from '../Loading'
import Transaction from "../transactions/Transaction";
import {createContext} from "../../actions/common/createContext";
import {fetchMainCategories, setMainCategoriesToReady} from '../../actions/category/fetchMainCategories';
import {fetchSubCategories, setSubCategoriesToReady} from "../../actions/category/fetchSubCategories";


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
    let mainCategoriesName = this.data['transactionType'].toLowerCase() + 'MainCategories';
    let mainCategoriesAreLoadedName = this.data['transactionType'].toLowerCase() + 'MainCategoriesAreLoaded';
    let subCategoriesName = this.data['transactionType'].toLowerCase() + 'SubCategories';
    let subCategoriesAreLoadedName = this.data['transactionType'].toLowerCase() + 'SubCategoriesAreLoaded';

    if (!newProps.categoryHolder[mainCategoriesAreLoadedName] && Object.keys(newProps.categoryHolder[mainCategoriesName]).length > 0) {
      this.props.setMainCategoriesToReady(this.data['transactionType']);
    }
    if (!this.state.mainCategoriesAreLoaded && newProps.categoryHolder[mainCategoriesAreLoadedName]) {
      this.setState({
        ...this.state,
        mainCategoriesAreLoaded: true
      })
    }

    if (!newProps.categoryHolder[subCategoriesAreLoadedName] && Object.keys(newProps.categoryHolder[subCategoriesName]).length > 0) {
      this.props.setSubCategoriesToReady(this.data['transactionType']);
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
    const {categoryHolder} = this.props;

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
    userHolder: state.userHolder,
    logHolder: state.logHolder,
    categoryHolder: state.categoryHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {
        fetchMainCategories: fetchMainCategories,
        fetchSubCategories: fetchSubCategories,
        setMainCategoriesToReady: setMainCategoriesToReady,
        setSubCategoriesToReady: setSubCategoriesToReady
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(Incomes)
