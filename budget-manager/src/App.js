import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import Navbar from './component/layout/Navbar';
import LogIn from './component/auth/LogIn';
import SignUp from './component/auth/SignUp';
import Home from './component/home/Home';
import Incomes from './component/incomes/Incomes';
import Expenses from './component/expenses/Expenses';
import Draft from './component/draft/Draft';
import Reports from './component/reports/Reports';
import Settings from './component/settings/Settings';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar/>
        <Switch>
          <Route exact path='/' component={Home}/>
          <Route path='/login' component={LogIn}/>
          <Route path='/signup' component={SignUp}/>
          <Route path='/incomes' component={Incomes}/>
          <Route path='/expenses' component={Expenses}/>
          <Route path='/draft' component={Draft}/>
          <Route path='/reports' component={Reports}/>
          <Route path='/settings' component={Settings}/>
        </Switch>
      </div>
    </BrowserRouter>
  );
}

export default App;
