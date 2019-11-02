import React, {Component} from 'react';
import moment from "moment";
import DatePicker from "react-datepicker";
import SpinButton from "../../../buttons/SpinButton";
import {convertDate} from "../../../../actions/date/dateActions";
import {dateProperties} from "../../../../store/Properties";

class TransactionTableSearchBar extends Component {

  state = {
    locking: false
  };

  constructor(props) {
    super(props);
    this.handleLock = this.handleLock.bind(this);
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    if (this.state.locking && !nextProps.locking) {
      this.setState({
        locking: false
      })
    }
  }

  handleChange(value, id) {
    this.props.handleDateChange(id, convertDate(value));
  }

  handleLock() {
    this.setState({
      locking: true
    });
    this.props.handleLock();
  }

  render() {
    const {endDateId, endDate, endDatePlaceHolder, handleSearch, startDateId, startDate, startDatePlaceHolder} = this.props;
    const {buttonName, buttonIcon, lockStatisticsButtonEnabled} = this.props;

    let buttonText = buttonName === undefined ? " Search " : (" " + buttonName + " ");
    let buttonIconText = buttonIcon === undefined ? "fas fa-search" : buttonIcon;

    let sDate = startDate !== undefined && startDate !== null ? new Date(moment(startDate).format(dateProperties.dateFormat)) : null;
    let eDate = endDate !== undefined && endDate !== null ? new Date(moment(endDate).format(dateProperties.dateFormat)) : null;

    let popperModifiers = {
      offset: {
        enabled: true,
        offset: '125px, 10px'
      }
    };

    let lockButton = null;
    if (lockStatisticsButtonEnabled === true) {
      lockButton = <SpinButton buttonLabel="Lock statistics" icon="fas fa-pencil-alt"
                               altButtonStyle="btn btn-outline-danger my-2 mr-3 ml-auto"
                               loading={this.state.locking} handleSubmit={this.handleLock}/>
    }

    return (
      <div className="card-header form-inline">
        <DatePicker
          className="form-control date-search-field my-2 mx-3" id={startDateId} placeholderText={startDatePlaceHolder}
          dateFormat={dateProperties.calendarFormat} dateFormatCalendar={dateProperties.calendarFormat}
          selected={sDate} onChange={(e) => this.handleChange(e, startDateId)}
          popperModifiers={popperModifiers}/>
        <DatePicker
          className="form-control date-search-field my-2 mx-3" id={endDateId} placeholderText={endDatePlaceHolder}
          dateFormat={dateProperties.calendarFormat} dateFormatCalendar={dateProperties.calendarFormat}
          selected={eDate} onChange={(e) => this.handleChange(e, endDateId)}
          popperModifiers={popperModifiers}/>
        <button className="btn btn-outline-success my-2 mx-3" onClick={handleSearch}>
          <span className={buttonIconText}/>
          <span>{buttonText}</span>
        </button>
        {lockButton}
      </div>
    )
  }
}

export default TransactionTableSearchBar
