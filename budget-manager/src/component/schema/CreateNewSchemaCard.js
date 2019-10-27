import React, {Component} from 'react';
import SchemaForm from "./SchemaForm";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";

class CreateNewSchemaCard extends Component {

  render() {
    const {categoryHolder, target} = this.props;

    let outcomeMainCategories = categoryHolder.outcomeMainCategories;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <SchemaForm formTitle="Create schema" mainCategoryList={outcomeMainCategories}/>
          </div>
        </div>
      </React.Fragment>
    )
  }

}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder,
    categoryHolder: state.categoryHolder,
    statisticsHolder: state.statisticsHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {},
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateNewSchemaCard)
