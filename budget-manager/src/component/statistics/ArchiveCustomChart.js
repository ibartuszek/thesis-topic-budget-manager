import React, {Component} from 'react';
import {DiscreteColorLegend, LineSeries, VerticalBarSeries, XAxis, XYPlot, YAxis} from "react-vis";
import Crosshair from "react-vis/es/plot/crosshair";

// import {HorizontalGridLines, LineSeries, MarkSeries, VerticalBarSeries, VerticalGridLines, XAxis, XYPlot, YAxis} from "react-vis";

class ArchVCustomChart extends Component {

  state = {
    id: 1,
    title: "Example chart",
    type: ["INCOME", "OUTCOME"],
    startDate: "2019-10-01",
    endDate: "2019-10-31",
    currency: "EUR",
    mainCategory: "test main category",
    subCategory: "test supplementary category",
    monthly: true,
    chartType: "linear",
    xAxisName: "day",
    yAxisName: "amount",
    height: 300,
    width: 500,
    data: [
      {
        legend: "test1",
        data: [
          {x: 0, y: 8},
          {x: 1, y: 5},
          {x: 2, y: 4},
          {x: 3, y: 9},
          {x: 4, y: 1},
          {x: 5, y: 7},
          {x: 6, y: 6},
          {x: 7, y: 3},
          {x: 8, y: 2},
          {x: 9, y: 0}
        ]
      }, {
        legend: "test2",
        data: [
          {x: 0, y: 18},
          {x: 1, y: 15},
          {x: 2, y: 14},
          {x: 3, y: 19},
          {x: 4, y: 11},
          {x: 5, y: 17},
          {x: 6, y: 16},
          {x: 7, y: 13},
          {x: 8, y: 12},
          {x: 9, y: 10}
        ]
      }
    ],
    crossHairValues: []
  };


  onMouseLeave = () => {
    this.setState({
      crossHairValues: []
    });
  };

  onNearestX = (value, {index}) => {
    let target = [];
    target.push(this.getCrossHair(this.state.data[0].data, index));
    target.push(this.getCrossHair(this.state.data[1].data, index));
    console.log(target);
    this.setState({
      crossHairValues: target
    });
  };

  render() {
    const {chartType, crossHairValues, currency, data, endDate, height, mainCategory, subCategory, startDate, title, type, width, xAxisName, yAxisName} = this.state;

    // TODO: make calculate button

    const chartData = this.getChartData(type, data, chartType);
    let incomes = chartData.incomes;
    let outcomes = chartData.outcomes;
    let legends = chartData.legends;
    let crossHairElement = crossHairValues.length < 2 ? null :
      (<Crosshair values={crossHairValues}>
        <div className="custom-crosshair">
          <div>{"At point " + crossHairValues[0].x + ":"}</div>
          <div>{legends[0].title + ": " + crossHairValues[0].y}</div>
          <div>{legends[1].title + ": " + crossHairValues[1].y}</div>
        </div>
      </Crosshair>);
    let mainType = type === 'INCOME' ? "Income" : "Expense";
    let mainCategoryTitle = mainCategory === null || mainCategory === undefined ? null : (
      <li>Main category:
        <span className="d-block mb-2 ml-3">{mainCategory}</span>
      </li>);
    let subCategoryTitle = subCategory === null || subCategory === undefined ? null : (
      <li>Supplementary category:
        <span className="d-block mb-2 ml-3">{subCategory}</span>
      </li>);

    return (
      <div className="clearfix mx-auto my-3">
        <div className="card card-body custom-chart-details-container my-3 mx-auto">
          <h5>Details</h5>
          <ul className="custom-list">
            <li>Type:
              <span className="d-block mb-1 ml-3">{mainType}</span>
            </li>
            <li>Currency:
              <span className="d-block mb-1 ml-3">{currency}</span>
            </li>
            <li>Start date:
              <span className="d-block mb-1 ml-3">{startDate}</span>
            </li>
            <li>End date:
              <span className="d-block mb-1 ml-3">{endDate}</span>
            </li>
            {mainCategoryTitle}
            {subCategoryTitle}
          </ul>
        </div>
        <div className="custom-chart-container my-3 mx-lg-auto">
          <h4 className="text-center mt-3">{title}</h4>
          <div className="mt-3 mb-5">
            <XYPlot height={height} width={width} onMouseLeave={this.onMouseLeave}>
              <DiscreteColorLegend style={{position: 'absolute', left: '10px', top: '300px'}}
                                   orientation="horizontal" items={legends}/>
              {incomes}
              {outcomes}
              <XAxis title={xAxisName}/>
              <YAxis title={yAxisName}/>
              {crossHairElement}
            </XYPlot>
          </div>
        </div>
        <div className="float-none my-3">
          <div className="alert alert-success d-block" role="alert">
            A simple success alert with <a href="#" className="alert-link">an example link</a>. Give it a click if you like.
          </div>
        </div>
      </div>
    );
  }

  getChartData(type, data, chartType) {
    let incomeData = null;
    let outcomeData = null;
    let incomes = null;
    let outcomes = null;
    let legends = [];
    legends.push({"title": data[0].legend});
    if (type.length > 1) {
      incomeData = data[0].data;
      outcomeData = data[1].data;
      legends.push({"title": data[1].legend});
    } else if (type[0] === 'INCOMES') {
      incomeData = data[0];
    } else {
      outcomeData = data[0];
    }
    if (incomeData !== null) {
      incomes = chartType === 'linear'
        ? <LineSeries data={incomeData} onNearestX={this.onNearestX}/>
        : <VerticalBarSeries data={incomeData} onNearestX={this.onNearestX}/>;

    }
    if (outcomeData !== null) {
      outcomes = chartType === 'linear'
        ? <LineSeries data={outcomeData} onNearestX={this.onNearestX}/>
        : <VerticalBarSeries data={outcomeData} onNearestX={this.onNearestX}/>;
    }
    return {incomes, outcomes, legends};
  }

  getCrossHair(data, index) {
    for (let i = 0; i < data.length; i++) {
      let actual = data[i];
      if (actual.x === index) {
        return actual;
      }
    }
  }

}

export default ArchVCustomChart;
