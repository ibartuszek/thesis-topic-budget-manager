import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import Home from './component/home/Home';
import Incomes from './component/incomes/Incomes';
import Draft from './component/draft/Draft';
import LogIn from './component/auth/LogIn';
import Navbar from './component/layout/Navbar';
import Outcomes from "./component/outcomes/Outcomes";
import Reports from './component/reports/Reports';
import Settings from './component/settings/Settings';
import Register from './component/auth/SignUp';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar/>
        <Switch>
          <Route exact path='/' component={Home}/>
          <Route path='/login' component={LogIn}/>
          <Route path='/register' component={Register}/>
          <Route path='/incomes' component={Incomes}/>
          <Route path='/expenses' component={Outcomes}/>
          <Route path='/draft' component={Draft}/>
          <Route path='/reports' component={Reports}/>
          <Route path='/settings' component={Settings}/>
        </Switch>
      </div>
    </BrowserRouter>
  );
}

export default App;
