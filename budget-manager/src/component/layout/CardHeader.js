import React from 'react';

const CardHeader = (props) => {
  return (
    <div className="card-header float-left my-auto">
        <button aria-controls="createIncome" aria-expanded="false" className="btn btn-secondary mr-2 mt-1"
                data-target="#createIncome" data-toggle="collapse" type="button">
          <span className="fas fa-plus"></span>
          <span> {props.createButtonName} </span>
        </button>
        <button aria-controls="createIncomeMainType" aria-expanded="false" className="btn btn-secondary mr-2 mt-1"
                data-target="#createIncomeMainType" data-toggle="collapse" type="button">
          <span className="fas fa-plus"></span>
          <span> Create new main type </span>
        </button>
        <button aria-controls="createIncomeSubType" aria-expanded="false" className="btn btn-secondary mr-2 mt-1"
                data-target="#createIncomeSubType" data-toggle="collapse" type="button">
          <span className="fas fa-plus"></span>
          <span> Create new subtype </span>
        </button>
      </div>
  )
}

export default CardHeader;