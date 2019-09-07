import React from 'react';

const CardHeaderButton = (props) => {
  const {target, buttonName} = props;
  const dataTarget = "#" + target;
  // className="btn btn-secondary mr-2 mt-1"

  return (
    <React.Fragment>
      <button aria-controls={target} aria-expanded="false" className="btn btn-outline-info  mr-2 mt-1"
              data-target={dataTarget} data-toggle="collapse" type="button">
        <span className="fas fa-plus"/>
        <span> {buttonName} </span>
      </button>
    </React.Fragment>
  )
};

export default CardHeaderButton;
