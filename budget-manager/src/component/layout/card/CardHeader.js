import React from 'react';
import CardHeaderButton from "./CardHeaderButton";


const CardHeader = (props) => {
  const {
    createTransactionButtonName, createTransaction, createMainTypeButtonName,
    createMainType, createSubTypeButtonName, createSubType
  } = props.data;
  return (
    <div className="card-header float-left my-auto">
      <CardHeaderButton target={createTransaction} buttonName={createTransactionButtonName}/>
      <CardHeaderButton target={createMainType} buttonName={createMainTypeButtonName}/>
      <CardHeaderButton target={createSubType} buttonName={createSubTypeButtonName}/>
    </div>
  )
};

export default CardHeader;
