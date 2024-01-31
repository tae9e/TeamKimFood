import 'bootstrap/dist/css/bootstrap.min.css';

import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Navbar from 'react-bootstrap/Navbar';
import { SlLogin, SlPencil  } from "react-icons/sl";
import '../Css/Common.css';

function TopNav() {

    function toggleMenu() {
        const parentMenu = document.querySelector('.parent-menu');

        if (parentMenu.classList.contains('hover')) {
            parentMenu.classList.remove('hover')
        } else {
            parentMenu.classList.add('hover')
        }
    }
        return (
        <header>
            <div className="hd-top">
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
                <div className="signInUp">
                    <ul>
                        <li><a href="/signin"><SlPencil /> 회원가입</a></li>
                        <li><a href="/login"><SlLogin /> 로그인</a></li>
                    </ul>
                </div>
            </div>

            <Navbar expand="lg" className="navbar">
                <Container fluid="true" id="contNav">

                            <ul className="navbar-nav" >
                                <li className="depth-1 navbar-text parent-menu"><a href="#">카테고리</a></li>
                                    <ul>
                                        <li className="depth-2 nav-category-item">종류별 밑반찬 | 메인반찬 | 국/탕/찌개 | 디저트 | 면/만두 | 밥/죽/떡 | 퓨전
                                            | 김치/젓갈 | 장류/양념/소스/잼 | 양식 | 샐러드 | 스프 | 빵/과자 | 차/음료/술 | 기타
                                        </li>
                                        <li className="depth-2 nav-category-item">상황별</li>
                                        <li className="depth-2 nav-category-item">재료별</li>
                                    </ul>
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