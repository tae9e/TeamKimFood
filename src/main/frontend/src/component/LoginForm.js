import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { KAKAO_AUTH_URL } from './OAuth';
import { withRouter } from 'react-router-dom';

class LoginForm extends Component {

         submitClick = async (e) => {
            e.preventDefault();

             const email = document.getElementById('email_val').value;
             const password=document.getElementById('pwd_val').value;

             const loginData = {username: email, password: password};

             try{
                const response = await fetch('/login',{
                    method: 'POST',
                    headers: {
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify(loginData),
                });

                if(response.ok){
                    console.log('로그인');
                      this.props.history.push('/boardlist');
                }else{
                    console.log('로그인 실패');
                }
             }catch(error){
                console.error('로그인 요청 중 오류 발생: ', error)
             }
        }
    render () {
        return (
            <section className="main">
                <div className="m_login signin">
                    <h3>LOGIN</h3>
                    <div className="log_box">
                        <div className="in_ty1">
                            <input type="text" id="email_val" placeholder="이메일" />
                        </div>
                        <div  className="in_ty1">
                            <input type="password" id="pwd_val" placeholder="비밀번호" />
                        </div>
                        <div className="s_bt" type="" onClick={(e) => this.submitClick(e)}>로그인</div>
                        {/*
                        <ul className="af">
                            <li><Link to={'/register'}>회원가입</Link></li>
                            <li className="pwr_b" onClick={this.pwdResetClick}><a href="#n">비밀번호 재설정</a></li>
                        </ul>
                    </div>
                </div>
                <div className="m_login m_pw chgpw">
                    <h3 className="pw_ls">비밀번호 재설정 <span className="compl1">완료</span></h3>
                    <div className="log_box">
                        <div className="pw_one">
                            <div className="in_ty1">
                                <input type="text" id="reset_email_val" name="" placeholder="이메일"/>
                            </div>
                            <div  className="in_ty1">
                                <input type="text" id="reset_name_val" name="" placeholder="성명"/>
                            </div>
                            <div className="btn_confirm btn_confirm_m">
                                <div className="bt_ty bt_ty_m bt_ty1 cancel_ty1"
                                     onClick={this.pwdResetCancleClick}>취소</div>
                                <a href="#n" className="bt_ty bt_ty_m bt_ty2 submit_ty1"
                                   onClick={this.pwdResetConfim}>확인</a>
                            </div>
                        </div>
                        */}
                    </div>
                    </div>

                <a href={KAKAO_AUTH_URL} className="kakaobtn">
                   <img src={`${process.env.PUBLIC_URL}/kakao_login.png`} alt="카카오 로그인" />
                </a>


            </section>
        );
    }
}

export default LoginForm;