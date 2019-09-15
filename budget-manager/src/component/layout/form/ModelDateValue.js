import React, {Component} from 'react';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {convertDate} from "../../../actions/date/dateActions";
import {dateProperties} from "../../../store/Properties";
import {validateModelDateValue} from "../../../actions/validation/modelDateValueValidation";


class ModelDateValue extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e, id, errorMessage) => {
    this.props.onChange(id, convertDate(e), errorMessage);
  };

  render() {
    const {id, model, labelTitle, placeHolder} = this.props;

    // TODO: check
    let date = model !== undefined && model !== null && model.value !== null ? new Date(model.value) : null;
    const errorMessage = date !== undefined ? validateModelDateValue(model, labelTitle) : null;

    let popperModifiers = {
      offset: {
        enabled: true,
        offset: '125px, 10px'
      }
    };

    return (
      <React.Fragment>
        <div className="input-group mt-3 custom-react-datepicker-wrapper">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <DatePicker
            className="form-control" id={id} placeholderText={placeHolder}
            dateFormat={dateProperties.calendarFormat} dateFormatCalendar={dateProperties.calendarFormat}
            selected={date} onChange={(e) => this.handleChange(e, id, errorMessage)}
            popperModifiers={popperModifiers}/>
        </div>
        <div className="custom-error-message-container mt-2">{errorMessage}</div>
      </React.Fragment>
    )
  }

}

export default ModelDateValue;
