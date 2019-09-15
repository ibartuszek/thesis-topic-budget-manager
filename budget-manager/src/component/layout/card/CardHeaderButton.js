import React from 'react';

const CardHeaderButton = (props) => {
  const {target, buttonName} = props;
  const dataTarget = "#" + target;

  return (
    <React.Fragment>
      <button aria-controls={target} aria-expanded="false" className="btn btn-outline-info mx-3 my-2"
              data-target={dataTarget} data-toggle="collapse" type="button">
        <span className="fas fa-plus"/>
        <span> {buttonName} </span>
      </button>
    </React.Fragment>
  )
};

export default CardHeaderButton;
