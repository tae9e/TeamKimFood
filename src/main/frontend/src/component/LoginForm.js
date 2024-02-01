import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
    const [loginData, setLoginData] = useState({
        username: '',
        password: '',
    });
    const [errors, setErrors] = useState({}); // 유효성 검사 오류를 저장할 상태
    const navigate = useNavigate();

    const handleChange = (e) => {
        setLoginData({ ...loginData, [e.target.name]: e.target.value });
        setErrors({ ...errors, [e.target.name]: '' });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('/public/login', loginData);
            localStorage.setItem('token', response.data.token);
            alert('방문해주셔서 감사합니다.'); // 로그인 성공 메시지
            navigate('/'); // 성공적으로 로그인되면 홈페이지로 이동
        } catch (error) {
            if (error.response && error.response.data) {
                // 서버로부터 받은 오류 메시지를 상태에 저장
                setErrors(error.response.data);
            }
        }
    };

    return (
        <div className={'flex justify-center items-center my-20'}>
            <form onSubmit={handleSubmit} className={'w-full max-w-xs border p-3 rounded-lg'}>
                <div className={'mb-1 border p-3 rounded-lg'}>
                    <label className={'block text-gray-700 text-sm font-bold mb-2'}>이메일:</label>
                    <input
                        type="email"
                        name="username"
                        value={loginData.username}
                        onChange={handleChange}
                        required
                        placeholder={'ex) example@example.com'}
                        className={'shadow appearance-none border ${errors.email ? \'border-red-500\' : \'\'} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}
                    />
                    {errors.email && <p style={{ color: 'red' }}>{errors.email}</p>}
                </div>
                <div className={' mb-1 border p-3 rounded-lg'}>
                    <label className={'block text-gray-700 text-sm font-bold mb-2'}>비밀번호:</label>
                    <input type="password" name="password" value={loginData.password} onChange={handleChange} required
                           className={'shadow appearance-none border ${errors.password ? \'border-red-500\' : \'\'} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}
                    />
                    {errors.password && <p style={{ color: 'red' }}>{errors.password}</p>}
                </div>
                <div className={'mb-4'}>
                    <button type="submit"
                            className={"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"}
                    >로그인</button>
                </div>
            </form>
        </div>
    );
};

export default LoginForm;