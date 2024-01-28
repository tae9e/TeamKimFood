import React, { Component } from 'react';

class Footer extends Component {
    render () {
        return (
            <footer className="footer">
                    <div className="ft_r">
                        <ul>
                            <li className="ft_li"><a href="/personaltreat">개인정보처리방침</a></li>
                            <li className="ft_li"><a href="#n">이메일주소무단수집거부</a></li>
                        </ul>
                    </div>
                    <br />
                    <div className="ft_p">
                        <span>주소 : 서울시 종로구 인사동　</span>
                        <span>Tel : 02-1234-5678</span>
                    </div>
                    <p>COPYRIGHT TeamKim, ALL RIGHTS RESERVED.{this.props.name}</p>
            </footer>
        );
    }
}

export default Footer;