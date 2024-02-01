import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';

const MemberRegistrationForm = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const navigate = useNavigate();

  const onSubmit = async (data) => {
    try {
      const response = await axios.post('/login/member', data);
      alert(response.data); // 회원가입 성공 메시지
      navigate('/login');
    } catch (error) {
      if (error.response && error.response.data) {
        // 서버로부터 받은 오류 메시지를 상태에 저장 (필요한 경우)
        // setErrors(error.response.data);  // 이 부분은 react-hook-form에서 자동으로 처리해 줍니다.
      }
    }
  };

  return (
    <div className={'flex justify-center items-center my-20 '}>
      <form onSubmit={handleSubmit(onSubmit)} className={'w-full max-w-xs border p-3 rounded-lg'}>
        <div className={'mb-1 border p-3 rounded-lg'}>
          <label className={'block text-gray-700 text-sm font-bold mb-2'}>이름:</label>
          <input
            type="text"
            name="name"
            {...register('name', { required: '이름을 입력해주세요.' })}
            placeholder={'ex)홍길동'}
            className={`shadow appearance-none border ${errors.name ? 'border-red-500' : ''} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline`}
          />
          {errors.name && <p style={{ color: 'red' }}>{errors.name.message}</p>}
        </div>

        <div className={'mb-1 border p-3 rounded-lg'}>
          <label className={'block text-gray-700 text-sm font-bold mb-2'}>비밀번호:</label>
          <input
            type="password"
            name="password"
            {...register('password', {
              required: '비밀번호는 최소 8자리 이상이어야 합니다.',
              minLength: {
                value: 8,
                message: '비밀번호는 최소 8자리 이상이어야 합니다.'
              }
            })}
            placeholder={'비밀번호를 입력해주세요!'}
            className={`shadow appearance-none border ${errors.password ? 'border-red-500' : ''} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline`}
          />
          {errors.password && <p style={{ color: 'red' }}>{errors.password.message}</p>}
        </div>

        <div className={'mb-1 border p-3 rounded-lg'}>
          <label className={'block text-gray-700 text-sm font-bold mb-2'}>이메일:</label>
          <input
            type="email"
            name="email"
            {...register('email', {
              required: '올바른 이메일 형식이 아닙니다.',
              pattern: {
                value: /^(?:\w+\.?)*\w+@(?:\w+\.)+\w+$/,
                message: '올바른 이메일 형식이 아닙니다.'
              }
            })}
            placeholder={'ex) hong@yorijori.shop'}
            className={`shadow appearance-none border ${errors.email ? 'border-red-500' : ''} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline`}
          />
          {errors.email && <p style={{ color: 'red' }}>{errors.email.message}</p>}
        </div>

        <div className={'mb-1 border p-3 rounded-lg'}>
          <label className={'block text-gray-700 text-sm font-bold mb-2'}>닉네임:</label>
          <input
            type="text"
            name="nickname"
            {...register('nickname', { required: '닉네임을 입력해주세요.' })}
            placeholder={'별명을 만들어주세요!'}
            className={`shadow appearance-none border ${errors.nickname ? 'border-red-500' : ''} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline`}
          />
          {errors.nickname && <p style={{ color: 'red' }}>{errors.nickname.message}</p>}
        </div>

        <div className={'mb-1 border p-3 rounded-lg'}>
          <label className={'block text-gray-700 text-sm font-bold mb-2'}>전화번호:</label>
          <input
            type="text"
            name="phoneNumber"
            {...register('phoneNumber', {
              required: '전화번호 형식은 000-0000-0000입니다.',
              pattern: {
                value: /^010[.-]?(\d{3}|\d{4})[-]?(\d{4})$/,
                message: '전화번호 형식은 000-0000-0000입니다.'
              }
            })}
            placeholder={'010-0000-0000'}
            className={`shadow appearance-none border ${errors.phoneNumber ? 'border-red-500' : ''} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline`}
          />
          {errors.phoneNumber && <p style={{ color: 'red' }}>{errors.phoneNumber.message}</p>}
        </div>

        <div className={'mb-4'}>
          <button
            type="submit"
            className={"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"}
          >회원가입</button>
        </div>
      </form>
    </div>
  );
};

export default MemberRegistrationForm;