import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {fetchMainCategories, setMainCategoriesToReady} from '../../store/actions/categoryActions';
import Loading from '../Loading'


class Incomes extends Component {

  data = {
    transactionType: 'INCOME'
  };

  state = {
    mainCategoriesAreLoaded: false,
  };

  componentDidMount() {
    this.props.fetchMainCategories(this.data);
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (!newProps.categoryHolder.mainCategoriesAreLoaded && Object.keys(newProps.categoryHolder.incomeMainCategories).length > 0) {
      this.props.setIncomeMainCategoriesToReady();
      console.log(this.props.categoryHolder);
    }
    if (oldProps.categoryHolder.mainCategoriesAreLoaded !== newProps.categoryHolder.mainCategoriesAreLoaded) {
      this.setState({
        ...this.state,
        mainCategoriesAreLoaded: newProps.categoryHolder.mainCategoriesAreLoaded

      })
    }
  }

  render() {
    let content = null;
    if (this.state.mainCategoriesAreLoaded) {
      content = (
        <Loading/>
      )
    } else {
      content = <Loading data="(fetching data...)"/>
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
    categoryHolder: state.categoryHolder
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {
        fetchMainCategories: fetchMainCategories, setIncomeMainCategoriesToReady: setMainCategoriesToReady
      },
      dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Incomes)
