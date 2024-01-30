
import React, { useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import axios from "axios";
import React from 'react';
import { Route, Routes } from 'react-router-dom';
import axios from "axios";



import BoardList from './component/BoardList';
import LoginForm from './component/LoginForm';
import Header from './component/Header/Header';
import Footer from './component/Footer/Footer';
import ProtectedRoute from "./component/manage/ProtectedRoute";
import ManagePage from "./component/manage/ManagePage";
import 'bootstrap/dist/css/bootstrap.min.css';
import './component/Css/Common.css';
import './component/Css/Layout.css';
import MemberManagement from "./component/manage/MemberManagement";
import RecipeManagement from "./component/manage/RecipeManagement";
import Dashboard from "./component/manage/Dashboard";
import PersonalTreat from "./component/Footer/PersonalTreat";
import RecipeSave from "./component/recipe/RecipeSave";
import LoginHandler from "./component/manage/LoginHandler";
import MemberRegistrationForm from "./component/member/MemberRegistrationForm";


function App() {

    return(
            <div className="App flex flex-col min-h-screen">
                <Header />
                <div className={'flex-grow mb-5'}>
                <Routes>
                    <Route exact path='/' Component={ BoardList } />
                    <Route path='/login' Component={ LoginForm } />
                    <Route path='/personaltreat' Component={ PersonalTreat } />
                    <Route path={'/signin'} Component={MemberRegistrationForm}/>
                    {/*<Route path='/BoardContent/:recipe_id' component={ BoardContent } />*/}
                    <Route path={"/api/recipes/save"} Component={RecipeSave}/>
                    <Route path="/admin" element={<ProtectedRoute><ManagePage/></ProtectedRoute>} />
                    <Route path="/admin/members" element={<ProtectedRoute><MemberManagement/></ProtectedRoute>} />
                    <Route path="/admin/recipes" element={<ProtectedRoute><RecipeManagement/></ProtectedRoute>} />
                    <Route path="/admin/dashboard" element={<ProtectedRoute><Dashboard/></ProtectedRoute>} />
                    <Route path="/public/auth/kakao/callback" element={<ProtectedRoute><LoginHandler /></ProtectedRoute>} />
                </Routes>
                </div>

                <Footer />
            </div>
        )
    }

export default App;
