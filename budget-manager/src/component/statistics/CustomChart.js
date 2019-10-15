import React, {Component} from 'react';
import XYPlot from "react-vis/es/plot/xy-plot";
import {LineSeries, VerticalBarSeries} from "react-vis/es";

class CustomChart extends Component {

  state = {
    value: false
  };

  render() {
    const {value} = this.state;
    console.log(this.props);
    const {chartType, currency, title, types} = this.props.schema;
    const {height, width} = this.props.chartDetails;
    const {dataPoints} = this.props.chartData;

    let data = chartType === 'LINEAR'
      ? <LineSeries data={dataPoints} onNearestX={this.onNearestX}/>
      : <VerticalBarSeries data={dataPoints} onNearestX={this.onNearestX} color="#3F7D20"/>;

    return (

      <div className="custom-chart-container">
        <h4 className="text-center mt-3">{title}</h4>
        <XYPlot className="my-3 mx-auto" height={height} width={width}>
          {data}
        </XYPlot>
      </div>
    );
  }

  getChartData(data, chartType, types) {
    let incomeData = null;
    let outcomeData = null;
    let incomes = null;
    let outcomes = null;
    let legends = [];
    console.log(data);
    legends.push({"title": data[0].legend});
    if (types.length > 1) {
      incomeData = data[0].dataPoints;
      outcomeData = data[1].dataPoints;
      legends.push({"title": data[1].legend});
    } else if (types[0] === 'INCOMES') {
      incomeData = data[0];
    } else {
      outcomeData = data[0];
    }
    if (incomeData !== null) {


    }
    if (outcomeData !== null) {
      outcomes = chartType === 'LINEAR'
        ? <LineSeries data={outcomeData} onNearestX={this.onNearestX}/>
        : <VerticalBarSeries data={outcomeData} onNearestX={this.onNearestX} color="#FF6C13"/>;
    }
    return {incomes, outcomes, legends};
  }

}

export default CustomChart;
