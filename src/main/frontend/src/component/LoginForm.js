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
                const response = await fetch('public/login',{
                    method: 'POST',
                    headers: {
                        'Content-Type':'application/json',
                    },
                    body: JSON.stringify(loginData),
                });

                if(response.ok){
                    console.log('로그인');
                    const data = await response.json();
                    localStorage.setItem('token', data.token);
                    console.log(data.token);
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
                     <div className="log_box flex flex-col items-center">
                         <div className="mb-4 mt-4">
                             <input type="text" id="email_val" placeholder="이메일" className="w-64 py-2 px-3 rounded-md border" />
                         </div>
                         <div className="mb-4">
                             <input type="password" id="pwd_val" placeholder="비밀번호" className="w-64 py-2 px-3 rounded-md border" />
                         </div>
                         <div className="mb-4">
                             <button className="w-64 bg-black hover:bg-blue-700 text-white font-bold py-2 px-4 rounded cursor-pointer">
                                 로그인
                             </button>
                         </div>
                         <div className="mt-0.1">
                             <a href={KAKAO_AUTH_URL} className="kakaobtn w-64 h-10 rounded-lg flex items-center justify-center text-lg"> {/* 글꼴 크기 조절 */}
                                 <img src={`${process.env.PUBLIC_URL}/kakao_login.png`} alt="카카오 로그인" />
                             </a>
                         </div>
                     </div>
                 </div>
             </section>

                <a href={KAKAO_AUTH_URL} className="kakaobtn">
                   <img src={`${process.env.PUBLIC_URL}/kakao_login.png`} alt="카카오 로그인" />
                </a>


            </section>
        );
    }
}

export default LoginForm;