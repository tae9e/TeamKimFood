import React, { useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import axios from "axios";

import BoardList from './component/BoardList';
import LoginForm from './component/LoginForm';
import Header from './component/Header/Header';
import Footer from './component/Footer/Footer';

import 'bootstrap/dist/css/bootstrap.min.css';
import './component/Css/Common.css';
import './component/Css/Layout.css';
import PersonalTreat from "./component/Footer/PersonalTreat";

function App() {

    return(
            <div className="App">
                <Header />

                <Routes>
                    <Route exact path='/' Component={ BoardList } />
                    <Route path='/login' Component={ LoginForm } />
                    <Route path='/personaltreat' Component={ PersonalTreat } />
                    {/*<Route path='/BoardContent/:recipe_id' component={ BoardContent } />*/}
                </Routes>

                <Footer />
            </div>
        )
    }

export default App;
