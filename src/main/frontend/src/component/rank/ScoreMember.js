import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

function ScoreMember(){
    const [inputData, setInputData] = useState([]);

    useEffect(()=>{
        const fetchRecipes = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/api/rank/users`);
                setInputData(res.data);
            } catch (e) {
                console.error(e.message);
            }
        };
        fetchRecipes();
    }, []);


    return (
        <div className="container mx-auto px-4 sm:px-8">
            <h3 className="text-xl font-semibold leading-tight my-6">유저 랭킹</h3>
            <div className="py-4">
                <div className="shadow overflow-hidden rounded border-b border-gray-200">
                    <table className="min-w-full bg-white">
                        <thead className="bg-emerald-200 text-white">
                        <tr>
                            <th className="text-left py-3 px-4 uppercase font-semibold text-sm">닉네임</th>
                            <th className="text-left py-3 px-4 uppercase font-semibold text-sm">총 추천수</th>
                        </tr>
                        </thead>
                        <tbody className="text-gray-700">
                        {inputData.length > 0 ? (
                            inputData.map((rowData, index) => (
                                <tr key={rowData.id}>
                                    <td className="text-left py-3 px-4">{rowData.nickname}</td>
                                    <td className="text-left py-3 px-4">{rowData.totalScore}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="2" className="text-center py-3 px-4">충분한 데이터가 없습니다.</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    );
}

export default ScoreMember;