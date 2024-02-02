import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Survey() {
  const navigate = useNavigate();
  const [modalOpen, setModalOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [surveyComplete, setSurveyComplete] = useState(false);
  const [message, setMessage] = useState('');
    const [surveyData, setSurveyData] = useState({
      situation: '',
      foodStuff: '',
      foodNationType: ''
    });

     useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/signin');
        }
      }, [navigate]);


  const handleChange = (e) => {
    setSurveyData({
      ...surveyData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("handleSubmit 호출됨", surveyData);
    try {
      const response = await axios.post('/survey/list', surveyData);
      console.log("요청 성공", response); // 요청 성공시 로그
      setSurveyComplete(true);
      setMessage("설문조사가 등록되었습니다.");
      setModalOpen(true);
    } catch (error) {
      console.error('설문조사가 완료되지 못했습니다.', error);
      setMessage("설문조사 등록 중 오류가 발생했습니다."); // 사용자에게 오류 메시지 표시
      setModalOpen(true); // 오류 발생 시에도 모달을 열어서 메시지를 보여줌
    }
  };

  const closeModal = () => { // 모달 닫기 함수 추가
    setModalOpen(false);
    navigate('/');
  };

  return (
    <form onSubmit={handleSubmit}>
           <div className="flex flex-col items-center justify-center my-20">
              <div className="border p-3 rounded-lg text-center">
                <label className="text-2xl font-bold text-gray-700 mb-4">고객 선호도 설문조사</label>
                <div className="space-y-6">
                  <div>
                    <label className="block text-lg font-semibold text-gray-700 mb-2 pb-2 border-b border-gray-300">상황:</label>
                    <div className="mt-2 border border-gray-300 rounded p-3">
                      {["혼자", "가족", "친구", "연인", "일상", "상차림"].map((situation) => (
                        <div key={situation} className="flex items-center mb-2">
                          <input
                            id={`situation-${situation}`}
                            name="situation"
                            type="radio"
                            value={situation}
                            onChange={handleChange}
                            className="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300"
                          />
                          <label htmlFor={`situation-${situation}`} className="ml-3 block text-sm font-medium text-gray-700">
                            {situation}
                          </label>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div>
                    <label className="block text-lg font-semibold text-gray-700 mb-2 pb-2 border-b border-gray-300">음식재료:</label>
                    <div className="mt-2 border border-gray-300 rounded p-3">
                      {["고기", "해산물", "채소", "과일", "디저트", "음료"].map((foodStuff) => (
                        <div key={foodStuff} className="flex items-center mb-2">
                          <input
                            id={`foodStuff-${foodStuff}`}
                            name="foodStuff"
                            type="radio"
                            value={foodStuff}
                            onChange={handleChange}
                            className="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300"
                          />
                          <label htmlFor={`foodStuff-${foodStuff}`} className="ml-3 block text-sm font-medium text-gray-700">
                            {foodStuff}
                          </label>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div>
                    <label className="block text-lg font-semibold text-gray-700 mb-2 pb-2 border-b border-gray-300">음식타입:</label>
                    <div className="mt-2 border border-gray-300 rounded p-3">
                      {["한식", "중식", "일식", "양식"].map((foodNationType) => (
                        <div key={foodNationType} className="flex items-center mb-2">
                          <input
                            id={`foodNationType-${foodNationType}`}
                            name="foodNationType"
                            type="radio"
                            value={foodNationType}
                            onChange={handleChange}
                            className="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300"
                          />
                          <label htmlFor={`foodNationType-${foodNationType}`} className="ml-3 block text-sm font-medium text-gray-700">
                            {foodNationType}
                          </label>
                        </div>
                      ))}
                    </div>
                  </div>

                      <button type="submit">설문 제출</button>
                              </div>
                            </div>
                          </div>
                  {modalOpen && (
                    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-start pt-10">
                      <div className="bg-white p-4 m-auto shadow-lg rounded">
                        <p>{message}</p>
                        <button onClick={closeModal} className="bg-blue-500 text-white rounded px-4 py-2">확인</button>
                      </div>
                    </div>
                  )}
                  </form>
                );
              }


export default Survey;