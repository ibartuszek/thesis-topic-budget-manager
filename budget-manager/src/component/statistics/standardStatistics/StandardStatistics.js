import React, {Component} from 'react';
import CustomRadialChart from "./CustomRadialChart";

// import {HorizontalGridLines, LineSeries, MarkSeries, VerticalBarSeries, VerticalGridLines, XAxis, XYPlot, YAxis} from "react-vis";

class StandardStatistics extends Component {

  render() {
    const {standardStatistics} = this.props;
    console.log(standardStatistics);
    let incomes = standardStatistics[0];


    return (
      <div className="card card-body custom-chart-details-container my-3 mx-auto">
        <CustomRadialChart height={incomes.height} width={incomes.width}
                           data={incomes.data} currency={incomes.currency}/>
      </div>
    );
  }

}

export default StandardStatistics;
