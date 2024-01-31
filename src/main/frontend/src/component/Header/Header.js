import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { DropdownSubmenu, NavDropdownMenu} from "react-bootstrap-submenu";
import { SlLogin, SlPencil  } from "react-icons/sl";
import '../Css/Common.css';
import React, { useState, useEffect } from 'react';
import { KAKAO_AUTH_URL } from '../OAuth';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function TopNav() {
const [isLoggedIn, setIsLoggedIn] = useState(false);
const navigate = useNavigate();

  useEffect(() => {
      const token = localStorage.getItem('userToken');
      if (token) {
        setIsLoggedIn(true);
      }
    }, []);

    const handleLogout = () => {
      localStorage.removeItem('userToken');
      setIsLoggedIn(false);
    };

    const handleKakaoLogin = async () => {
      try {
        const code = new URL(window.location.href).searchParams.get('code');
        const backendUrl = 'http://localhost:8080';

        const res = await axios({
          method: 'GET',
          url: `${backendUrl}/public/auth/kakao/callback?code=${code}`,
          headers: {
            'Content-Type': 'application/json;charset=utf-8',
          },
        });
        console.log('Response', res);
        localStorage.setItem('name', res.data.account.kakaoName);
        navigate('/boardList'); // navigate 함수를 사용하여 페이지 이동을 처리합니다.
      } catch (error) {
        console.error('로그인 에러', error);
        // 에러 처리 로직
      }
    };



    return (
        <header>
            <div className="hd_top">
                <span className="logo"><a href="/">YoriJori</a></span>
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
                           {!isLoggedIn && (
                                      <>
                                          <li><a href="/signin"><SlPencil /> 회원가입</a></li>
                                          <li><a href="/login"><SlLogin /> 로그인</a></li>
                                           {/* 카카오 로그인 버튼 */}
                                           <li> <a href={KAKAO_AUTH_URL} className="kakaobtn">
                                                                  <img src={`${process.env.PUBLIC_URL}/kakao_login.png`} alt="카카오 로그인" />
                                                               </a>
</li>
                                      </>
                                  )}
                                  {isLoggedIn && (
                                      <li><a href="/" onClick={handleLogout}><SlLogin /> 로그아웃</a></li>
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

export default TopNav;