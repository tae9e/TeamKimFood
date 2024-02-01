
import Container from 'react-bootstrap/Container';import 'bootstrap/dist/css/bootstrap.min.css';
import '../Css/Common.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Navbar from 'react-bootstrap/Navbar';
import { SlLogin, SlPencil, SlPersonCircle, SlShieldFill } from "react-icons/sl";
import React, { useState, useEffect } from 'react';
import { KAKAO_AUTH_URL } from '../OAuth';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Header() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userRole, setUserRole] = useState(null);
    const navigate = useNavigate();

      useEffect(() => {
          const token = localStorage.getItem('token');
          if (token) {
            setIsLoggedIn(true);
          }
        }, []);

   const handleLogin = (loginCredentials) => {
      axios.post('/login', loginCredentials)
        .then(response => {
          localStorage.setItem('token', response.data.token);
          setIsLoggedIn(true);
          setIsAdmin(response.data.isAdmin === 'true'); // 여기서 관리자 여부 설정
        })
        .catch(error => {
          console.log('에러 발생');
        });
    };
0
  const handleLogout = () => {
      localStorage.removeItem('token');
      setIsLoggedIn(false);
      setIsAdmin(false);
    };

   const handleAdminOrMyPageClick = () => {
       if (isAdmin) {
           navigate('/admin');
       } else {
           navigate('/mypage');
       }
   };




    return (
        <header>
            <div className="hd-top">
                <span className="logo"><a href="/main">YoriJori</a></span>
                <Form className="d-flex">
                    <Form.Control
                        type="search"
                        placeholder="Search"
                        className="me-2"
                        aria-label="Search"
                    />
                    <Button className="btn_regular" variant="outline-success">Search</Button>
                </Form>
                <ul className="signInUp">
                        {isLoggedIn && (
                                            <>
                                                {userRole === 'user' && (
                                                    <li><a href="/member-info">
                                                        {/*<SlPersonCircle /> */}
                                                        회원정보</a></li>
                                                )}
                                                {userRole === 'admin' && (
                                                    <li><a href="/admin">
                                                        {/*<SlShieldFill />*/}
                                                        관리자 페이지</a></li>
                                                )}
                                                <li><a href="/main" onClick={handleLogout}><SlLogin /> 로그아웃</a></li>
                                            </>
                                        )}
                              </ul>
            </div>

           <Navbar expand="lg" className="navbar">
                          <Container fluid="true" id="contNav">

                              <ul className="navbar-nav" >
                                  <li className="depth-1 navbar-text"><a href="#">오늘의 요리</a></li>
                                  <li className="depth-1 navbar-text"><a href="#">인기 레시피</a></li>
                                  <li className="depth-1 navbar-text"><a href="#">매거진</a></li>
                              </ul>

                          </Container>
                      </Navbar>

                  </header>
              );
          }

          export default Header;