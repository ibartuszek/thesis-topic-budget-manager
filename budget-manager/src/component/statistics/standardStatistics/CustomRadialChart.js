import React, {Component} from 'react';
import {RadialChart} from "react-vis/es";
import {Hint} from "react-vis";

class CustomRadialChart extends Component {

  state = {
    value: false
  };

  render() {
    const {value} = this.state;
    const {currency, title} = this.props.schema;
    const {height, width} = this.props.chartDetails;
    const {sectorPoints} = this.props.chartData;

    let hint = value !== false ?
      (<Hint value={value}>
        <div className="custom-crosshair">
          <div>{value.label + ":"}</div>
          <div>{this.getHint(value, sectorPoints) + " " + currency}</div>
        </div>
      </Hint>) : null;

    return (

      <div className="custom-chart-container">
        <h4 className="text-center mt-3">{title}</h4>
        <RadialChart data={sectorPoints}
                     colorRange={['#064789', '#3F7D20', '#CE0606', '#EBD500', '#427AA1', '#FF6C13']}
                     onValueMouseOver={v => this.setState({value: v})}
                     onSeriesMouseOut={v => this.setState({value: false})}
                     className="my-3 mx-auto" height={height} width={width}
                     showLabels={true} labelsRadiusMultiplier={1.5}
                     padAngle={0.05} innerRadius={90} radius={140}>
          {hint}
        </RadialChart>
      </div>
    );
  }

  getHint(value, data) {
    let amount = null;
    for (let index = 0; index < data.length && amount === null; index++) {
      let current = data[index];
      if (current.label === value.label) {
        amount = current.angle;
      }
    }
    return amount;
  }

}

export default CustomRadialChart;
